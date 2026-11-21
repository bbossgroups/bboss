package org.frameworkset.util.yml;
/**
 * Copyright 2026 bboss
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 * @author biaoping.yin
 * @Date 2026/11/2
 */
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * YAML 解析并扁平化为 Properties 风格 Map。
 * - key: 点分路径，如 spring.cloud.nacos.discovery.server-addr
 * - value: String，或 List<String>（当 YAML 中是 list/多文档/同 key 多值时）
 * - 所有 scalar 一律保留字符串原样，不做类型转换
 */
public class FlatYamlParser {
	
	private final String src;
	/** 当前文档结束下标（区间解析，不复制子串） */
	private final int len;
	private int pos;
	/** 是否为别名（*anchor）生成深拷贝；扁平化只读节点，可关闭以省去整棵子树的复制 */
	private final boolean copyAliases;
	/** 最近一次 skipBlankAndComments 后内容行的缩进宽度（tab 计 2 列） */
	private int lineIndent;
	/** 根映射容量提示（按文档内容行数预估），仅消费一次 */
	private int rootMapCapacity = -1;
	private final Map<String, Object> anchors = new HashMap<>();
	/** YAML 合并键：plain scalar <<（tag:yaml.org,2002:merge），引号包裹的 "<<" 不参与合并 */
	private static final String MERGE_KEY = "<<";
	
	private FlatYamlParser(String src, int from, int to, boolean copyAliases) {
		this.src = src;
		this.pos = from;
		this.len = to;
		this.copyAliases = copyAliases;
	}
	
	/** 解析单个文档并扁平化 */
	public static Map<String, Object> parseFlat(String yaml) {
		// 扁平化只读取解析结果，无需为别名做深拷贝（省去 anchor 子树的整棵复制）
		int[] contentLines = new int[1];
		List<Map<String, Object>> docs = parseAll(yaml, false, contentLines);
		// 按内容行数预估 key 数量，避免结果 Map 反复扩容
		int estimated = contentLines[0];
		Map<String, Object> out = new LinkedHashMap<>(estimated > 0 ? estimated + (estimated >> 1) + 1 : 16);
		// 多文档合并：每个文档先各自扁平化，再合并进 out，避免同 key 被覆盖
		for (Map<String, Object> d : docs) {
			Map<String, Object> tmp = new LinkedHashMap<>();
			flatten("", d, tmp);
			for (Map.Entry<String, Object> e : tmp.entrySet()) {
				mergeInto(out, e.getKey(), e.getValue());
			}
		}
		return out;
	}
	
	/** classpath 资源前缀，如 classpath:/bootstrap.yml */
	private static final String CLASSPATH_PREFIX = "classpath:";
	
	/**
	 * 从文件路径读取 YAML 内容并扁平化。
	 * 查找顺序：
	 * 1. path 以 classpath: 开头时，直接从 classpath 读取；
	 * 2. 否则先判断文件系统中的文件是否存在，存在则读取文件内容；
	 * 3. 文件不存在时退化为从 classpath 读取同名资源（如 bootstrap.yml、/bootstrap.yml）。
	 * 兼容 JDK 8（不使用 Files.readString / Files.readAllBytes 之外的 JDK 11+ API）。
	 *
	 * @param path 文件路径（支持相对/绝对路径）或 classpath 资源路径
	 * @return 扁平化结果；文件系统与 classpath 均找不到时抛 FileNotFoundException
	 */
	public static Map<String, Object> parseFlatFile(String path) throws IOException {
		return parseFlatFile(path, StandardCharsets.UTF_8);
	}
	
	/** 从 Path 读取 YAML 内容并扁平化，文件不存在时从 classpath 读取同名资源 */
	public static Map<String, Object> parseFlatFile(Path path) throws IOException {
		return parseFlatFile(path, StandardCharsets.UTF_8);
	}
	
	/** 从 Path 读取 YAML 内容并扁平化，文件不存在时从 classpath 读取同名资源 */
	public static Map<String, Object> parseFlatFile(Path path, Charset charset) throws IOException {
		return parseFlatFile(path == null ? null : path.toString(), charset);
	}
	
	/**
	 * 从文件路径（或 classpath 资源路径）读取 YAML 内容并扁平化。
	 *
	 * @param path    文件路径或 classpath 资源路径
	 * @param charset 字符集，为空时按 UTF-8 处理
	 */
	public static Map<String, Object> parseFlatFile(String path, Charset charset) throws IOException {
		if (path == null || path.trim().isEmpty()) {
			throw new FileNotFoundException("Yaml file path is blank.");
		}
		Charset cs = charset == null ? StandardCharsets.UTF_8 : charset;
		String location = path.trim();
		// 1. 显式 classpath: 前缀，直接走 classpath
		if (location.startsWith(CLASSPATH_PREFIX)) {
			InputStream in = openClasspathResource(location.substring(CLASSPATH_PREFIX.length()));
			if (in == null) {
				throw new FileNotFoundException("Yaml resource not found in classpath: " + location);
			}
			return parseFlatStreamAndClose(in, cs);
		}
		// 2. 文件系统中的文件存在，优先读取文件
		Path file = toPath(location);
		if (file != null && Files.exists(file)) {
			if (Files.isDirectory(file)) {
				throw new IOException("Yaml path is a directory: " + location);
			}
			return parseFlat(new String(Files.readAllBytes(file), cs));
		}
		// 3. 文件不存在，退化为从 classpath 读取同名资源
		InputStream in = openClasspathResource(location);
		if (in == null) {
			throw new FileNotFoundException("Yaml resource not found in file system and classpath: " + location);
		}
		return parseFlatStreamAndClose(in, cs);
	}
	
	/** 字符串转 Path，含非法字符（如 classpath 风格的资源路径）时返回 null，交由 classpath 处理 */
	private static Path toPath(String location) {
		try {
			return Paths.get(location);
		} catch (RuntimeException e) {
			return null;
		}
	}
	
	/**
	 * 从 classpath 打开资源流，依次尝试线程上下文类加载器、当前类加载器、绝对路径（以 / 开头）。
	 *
	 * @param location 资源路径，允许以 / 或 ./ 开头，允许使用 \ 作为分隔符
	 * @return 资源流，未找到时返回 null
	 */
	private static InputStream openClasspathResource(String location) {
		if (location == null) return null;
		String resource = location.trim().replace('\\', '/');
		while (resource.startsWith("./")) resource = resource.substring(2);
		while (resource.startsWith("/")) resource = resource.substring(1);
		if (resource.isEmpty()) return null;
		ClassLoader tccl = Thread.currentThread().getContextClassLoader();
		if (tccl != null) {
			InputStream in = tccl.getResourceAsStream(resource);
			if (in != null) return in;
		}
		ClassLoader cl = FlatYamlParser.class.getClassLoader();
		if (cl != null && cl != tccl) {
			InputStream in = cl.getResourceAsStream(resource);
			if (in != null) return in;
		}
		// 兼容 Class.getResourceAsStream 的绝对路径语义（如 "/bootstrap.yml"）
		return FlatYamlParser.class.getResourceAsStream("/" + resource);
	}
	
	/** 从 InputStream 读取 YAML 内容并扁平化（调用方负责关闭流） */
	public static Map<String, Object> parseFlatStream(InputStream in) throws IOException {
		return parseFlatStream(in, StandardCharsets.UTF_8);
	}
	
	/** 从 InputStream 读取 YAML 内容并扁平化（调用方负责关闭流） */
	public static Map<String, Object> parseFlatStream(InputStream in, Charset charset) throws IOException {
		if (in == null) {
			throw new FileNotFoundException("Yaml input stream is null.");
		}
		return parseFlat(new String(readAllBytes(in), charset == null ? StandardCharsets.UTF_8 : charset));
	}
	
	/** 解析内部打开的 classpath 流并负责关闭 */
	private static Map<String, Object> parseFlatStreamAndClose(InputStream in, Charset charset) throws IOException {
		try {
			return parseFlatStream(in, charset);
		} finally {
			try { in.close(); } catch (IOException ignore) { }
		}
	}
	
	private static byte[] readAllBytes(InputStream in) throws IOException {
		// 按可用字节数预分配，避免大配置文件读取时 ByteArrayOutputStream 反复扩容拷贝
		int initial = in.available();
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream(initial > 0 ? initial : 4096);
		byte[] buf = new byte[8192];
		int n;
		while ((n = in.read(buf)) != -1) bos.write(buf, 0, n);
		return bos.toByteArray();
	}
	
	/**
	 * 解析全部文档（每文档一个嵌套 Map，不做扁平化），主要用于内部/多文档聚合。
	 * 采用下标扫描直接定位文档分隔符（--- / ...），避免 split 生成全量行数组、
	 * 再 String.join 拼回文档带来的 2~3 倍全文内存拷贝。
	 */
	public static List<Map<String, Object>> parseAll(String yaml) {
		return parseAll(yaml, true, null);
	}

	/**
	 * 解析全部文档。
	 *
	 * @param copyAliases     是否为别名（*anchor）生成深拷贝
	 * @param contentLinesOut 可选出参，返回有效内容行总数（用于结果 Map 预分配容量）
	 */
	private static List<Map<String, Object>> parseAll(String yaml, boolean copyAliases, int[] contentLinesOut) {
		List<Map<String, Object>> result = new ArrayList<>();
		if (yaml == null) return result;
		// 处理 UTF-8 BOM：只偏移起始下标，不再复制整串
		int length = yaml.length();
		int offset = (!yaml.isEmpty() && yaml.charAt(0) == '\uFEFF') ? 1 : 0;
		int docStart = offset;
		int lineStart = offset;
		int docContentLines = 0;
		int totalContentLines = 0;
		while (lineStart < length) {
			int lineEnd = lineStart;
			while (lineEnd < length && yaml.charAt(lineEnd) != '\n' && yaml.charAt(lineEnd) != '\r') lineEnd++;
			if (isDocSeparatorLine(yaml, lineStart, lineEnd)) {
				flushDoc(yaml, docStart, lineStart, docContentLines, copyAliases, result);
				totalContentLines += docContentLines;
				docContentLines = 0;
				docStart = skipLineBreak(yaml, lineEnd, length);
			} else if (hasContent(yaml, lineStart, lineEnd)) {
				// 顺带统计内容行，替代后续对每个文档整段再扫一遍
				docContentLines++;
			}
			int nextLine = skipLineBreak(yaml, lineEnd, length);
			if (nextLine <= lineStart) break; // 防御：异常输入时避免死循环
			lineStart = nextLine;
		}
		flushDoc(yaml, docStart, length, docContentLines, copyAliases, result);
		totalContentLines += docContentLines;
		if (contentLinesOut != null) contentLinesOut[0] = totalContentLines;
		return result;
	}

	/** 判断 [start, end) 行内容（忽略首尾空白）是否为 YAML 文档分隔符 --- / ... */
	private static boolean isDocSeparatorLine(String text, int start, int end) {
		int s = start;
		int e = end;
		while (s < e && Character.isWhitespace(text.charAt(s))) s++;
		while (e > s && Character.isWhitespace(text.charAt(e - 1))) e--;
		if (e - s != 3) return false;
		char c0 = text.charAt(s);
		return (c0 == '-' || c0 == '.') && text.charAt(s + 1) == c0 && text.charAt(s + 2) == c0;
	}

	/** 跳过行结束符（\r\n / \r / \n），返回下一行起始下标（不超过 limit） */
	private static int skipLineBreak(String text, int index, int limit) {
		int i = index;
		if (i < limit && text.charAt(i) == '\r') i++;
		if (i < limit && text.charAt(i) == '\n') i++;
		return i;
	}

	/** 解析 [start, end) 区间内的文档；区间内仅有空行/注释行时忽略 */
	@SuppressWarnings("unchecked")
	private static void flushDoc(String yaml, int start, int end, int contentLines, boolean copyAliases, List<Map<String, Object>> result) {
		if (contentLines <= 0 || start >= end) return;
		Object o = parseDoc(yaml, start, end, contentLines, copyAliases);
		if (o instanceof Map) result.add((Map<String, Object>) o);
		else {
			Map<String, Object> wrap = new LinkedHashMap<>();
			wrap.put("", o);
			result.add(wrap);
		}
	}

	/** 判断单行 [start, end) 是否为有效内容行（既非空行也非注释行） */
	private static boolean hasContent(String text, int start, int end) {
		int p = start;
		while (p < end && (text.charAt(p) == ' ' || text.charAt(p) == '\t')) p++;
		return p < end && text.charAt(p) != '#';
	}

	private static Object parseDoc(String src, int from, int to, int contentLines, boolean copyAliases) {
		FlatYamlParser p = new FlatYamlParser(src, from, to, copyAliases);
		// 用文档内容行数预估根映射容量
		p.rootMapCapacity = contentLines;
		p.skipBlankAndComments();
		if (p.pos >= p.len) return new LinkedHashMap<>();
		// 顶层节点的父缩进取 -1，否则缩进为 0 的顶层 key 会被 parseNode 判为非法层级而被丢弃
		Object v = p.parseNode(-1);
		return v == null ? new LinkedHashMap<>() : v;
	}
	
	// ==================== 扁平化 ====================
	
	/**
	 * 扁平化规则：
	 * - Map: 递归展开，路径 a.b.c
	 * - List<Map>: 展开为 a.0.x, a.1.x
	 * - List<String>: 保持为 List<String>（值可能合并同 key）
	 * - scalar: 存 String
	 * - 顶层 "" 路径的 list/scalar 特殊处理
	 */
	@SuppressWarnings("unchecked")
	private static void flatten(String prefix, Object value, Map<String, Object> out) {
		if (value instanceof Map) {
			Map<String, Object> m = (Map<String, Object>) value;
			for (Map.Entry<String, Object> e : m.entrySet()) {
				String path = prefix.isEmpty() ? e.getKey() : prefix + "." + e.getKey();
				flatten(path, e.getValue(), out);
			}
		} else if (value instanceof List) {
			List<Object> list = (List<Object>) value;
			// 判断元素是否全为 scalar
			boolean allScalar = true;
			for (Object e : list) {
				if (e instanceof Map || e instanceof List) { allScalar = false; break; }
			}
			if (allScalar) {
				List<String> strs = new ArrayList<>();
				for (Object e : list) strs.add(e == null ? "" : e.toString());
				mergeInto(out, prefix, strs);
			} else {
				// 元素为 Map/List → 用索引展开
				for (int i = 0; i < list.size(); i++) {
					String path = prefix + "." + i;
					flatten(path, list.get(i), out);
				}
			}
		} else {
			// scalar: 统一存 String
			String s = value == null ? "" : value.toString();
			mergeInto(out, prefix, s);
		}
	}
	
	/**
	 * 将 value 合并进 out 的 key：
	 * - 无已有值：直接放入（调用方传入的 List 均为新建对象，无需再复制一次）
	 * - 已有值为 List：追加（若 value 也是 List 则 addAll，否则 add）
	 * - 已有值为 scalar：包成 List 后追加
	 */
	@SuppressWarnings("unchecked")
	private static void mergeInto(Map<String, Object> out, String key, Object value) {
		Object existing = out.get(key);
		if (existing == null) {
			out.put(key, value);
			return;
		}
		List<String> list;
		if (existing instanceof List) {
			list = (List<String>) existing;
		} else {
			list = new ArrayList<>();
			list.add(String.valueOf(existing));
			out.put(key, list);
		}
		if (value instanceof List) {
			for (Object e : (List<Object>) value) list.add(e == null ? "" : e.toString());
		} else {
			list.add(value == null ? "" : value.toString());
		}
	}

	/**
	 * YAML 合并键 << 语义：把来源映射中当前映射尚不存在的 key 注入目标映射。
	 * - 显式定义的 key 优先（无论写在 << 之前还是之后）；
	 * - 来源为映射序列（<<: [*a, *b]）时，靠前的映射优先；
	 * - 来源不是映射/映射序列（如标量、非映射别名）时忽略。
	 */
	@SuppressWarnings("unchecked")
	private void mergeMapping(Map<String, Object> target, Object source) {
		if (source instanceof Map) {
			mergeEntries(target, (Map<String, Object>) source);
		} else if (source instanceof List) {
			for (Object item : (List<Object>) source) {
				if (item instanceof Map) mergeEntries(target, (Map<String, Object>) item);
			}
		}
	}

	/** 把 source 中目标映射缺失的 key/value 注入 target，已存在的 key 不覆盖 */
	private static void mergeEntries(Map<String, Object> target, Map<String, Object> source) {
		for (Map.Entry<String, Object> e : source.entrySet()) {
			if (!target.containsKey(e.getKey())) target.put(e.getKey(), e.getValue());
		}
	}
	
	// ==================== 核心解析 ====================
	
	/**
	 * 解析一个节点（映射/序列/流式/标量）。
	 * 约定：调用前需已执行 skipBlankAndComments，pos 位于内容首字符，行缩进见 lineIndent。
	 * parentIndent 为父节点缩进宽度，顶层传 -1。
	 */
	private Object parseNode(int parentIndent) {
		skipBlankAndComments();
		if (pos >= len) { rootMapCapacity = -1; return null; }
		int indent = lineIndent;
		if (parentIndent >= 0 && indent <= parentIndent) { rootMapCapacity = -1; return null; }
		char c = src.charAt(pos);
		// 根节点不是映射时，容量提示不再适用
		if (c == '{' || c == '[') { rootMapCapacity = -1; return parseFlow(); }
		if (c == '-' && isDashSeq()) { rootMapCapacity = -1; return parseSequence(indent); }
		return parseMappingOrScalar(indent);
	}
	
	@SuppressWarnings("unchecked")
	private Object parseMappingOrScalar(int indent) {
		// 根映射按内容行数预分配容量，避免大配置反复扩容；提示仅消费一次
		int capacityHint = rootMapCapacity;
		rootMapCapacity = -1;
		Map<String, Object> map = capacityHint > 0 ? new LinkedHashMap<>(capacityHint) : new LinkedHashMap<>();
		Object scalarOnly = null;
		boolean hasKey = false;
		
		while (pos < len) {
			skipBlankAndComments();
			if (pos >= len) break;
			if (lineIndent != indent) break;
			char c = src.charAt(pos);
			if (c == '-' && isDashSeq()) break;
			if (c == '{' || c == '[') break;
			
			int save = pos;
			// 引号包裹的 "<<" 是普通 key，只有未加引号的 << 才是 YAML 合并键
			boolean quotedKey = pos < len && (src.charAt(pos) == '"' || src.charAt(pos) == '\'');
			String key = readKey();
			if (key == null) { pos = save; break; }
			hasKey = true;
			skipInlineSpaces();
			if (pos < len && src.charAt(pos) == ':') {
				pos++;
				skipInlineSpaces();
				Object value;
				if (pos < len && (src.charAt(pos) == '\n' || src.charAt(pos) == '\r')) {
					consumeNewline();
					// 值在下一行：先探明下一内容行的缩进，再交给子节点解析
					skipBlankAndComments();
					if (pos < len) {
						int nextIndent = lineIndent;
						if (nextIndent > indent) value = parseNode(indent);
						else if (nextIndent == indent && src.charAt(pos) == '-' && isDashSeq()) value = parseSequence(nextIndent);
						else value = null;
					} else value = null;
				} else {
					value = parseInlineValueOrAnchoredBlock(indent);
				}
				if (!quotedKey && MERGE_KEY.equals(key)) {
					mergeMapping(map, value);
				} else {
					map.put(key, value);
				}
			} else {
				pos = save;
				Object inline = parseInlineValue();
				if (map.isEmpty()) scalarOnly = inline;
				consumeToLineEnd();
				break;
			}
		}
		if (!hasKey && scalarOnly != null) return scalarOnly;
		return map;
	}
	
	@SuppressWarnings("unchecked")
	private List<Object> parseSequence(int indent) {
		List<Object> list = new ArrayList<>();
		while (pos < len) {
			skipBlankAndComments();
			if (pos >= len) break;
			if (lineIndent != indent) break;
			if (!(src.charAt(pos) == '-' && isDashSeq())) break;
			int dashPos = pos;
			pos++;
			skipInlineSpaces();
			// 序列项内 key 的列位置（- 之后），用于判断锚点绑定的块是否属于该项
			int itemIndent = indent + (pos - dashPos);
			if (pos >= len || src.charAt(pos) == '\n' || src.charAt(pos) == '\r') {
				consumeNewline();
				list.add(parseNode(indent));
			} else {
				char c = src.charAt(pos);
				if (c == '{' || c == '[') { list.add(parseFlow()); consumeToLineEnd(); }
				else if (c == '|' || c == '>') { list.add(parseBlockScalar(indent + 2)); consumeToLineEnd(); }
				else {
					int save = pos;
					// 引号包裹的 "<<" 是普通 key，只有未加引号的 << 才是 YAML 合并键
					boolean quotedKey = pos < len && (src.charAt(pos) == '"' || src.charAt(pos) == '\'');
					// plain key 一次扫描直达冒号，避免命中失败后再完整扫描一遍标量
					String k = quotedKey ? tryReadKey() : readPlainKeyToColon();
					if (k != null && pos < len && src.charAt(pos) == ':') {
						pos++;
						skipInlineSpaces();
						Map<String, Object> item = new LinkedHashMap<>();
						Object v;
						if (pos >= len || src.charAt(pos) == '\n' || src.charAt(pos) == '\r') {
							consumeNewline();
							v = parseNode(indent + 1);
						} else if (src.charAt(pos) == '|' || src.charAt(pos) == '>') {
							v = parseBlockScalar(indent + 2); consumeToLineEnd();
						} else {
							v = parseInlineValueOrAnchoredBlock(itemIndent);
							skipBlankAndComments();
							if (pos < len) {
								int ni = lineIndent;
								if (ni > indent) {
									Object more = parseMappingOrScalar(ni);
									if (more instanceof Map) ((Map<String, Object>) more).forEach(item::put);
								}
							}
						}
						if (!quotedKey && MERGE_KEY.equals(k)) {
							mergeMapping(item, v);
						} else {
							item.put(k, v);
						}
						list.add(item);
					} else {
						pos = save;
						list.add(parseInlineValueOrAnchoredBlock(indent));
					}
				}
			}
		}
		return list;
	}
	
	// ==================== Inline / Flow / Block Scalar ====================
	
	private Object parseInlineValue() {
		skipInlineSpaces();
		if (pos >= len) return null;
		char c = src.charAt(pos);
		String anchor = null;
		if (c == '&') { pos++; anchor = readWord(); skipInlineSpaces(); if (pos < len) c = src.charAt(pos); }
		Object v;
		if (pos >= len) v = "";
		else if (c == '{' || c == '[') v = parseFlow();
		else if (c == '*') { pos++; Object ref = anchors.get(readWord()); v = copyAliases ? deepCopy(ref) : ref; }
		else if (c == '|' || c == '>') v = parseBlockScalar(-1);
		else v = parseScalarUntilLineEnd();
		if (anchor != null) anchors.put(anchor, v);
		return v;
	}

	/**
	 * 解析映射值 / 序列项：
	 * - 当前值仅为行尾锚点声明（如 "base: &amp;b"）时，锚点绑定后续缩进更深的块节点（映射或序列）；
	 * - 否则按普通行内值解析（行内锚点、flow、标量、块标量等）。
	 * 锚点后无块内容时保持原行为：值为空字符串。
	 *
	 * @param parentIndent 当前所在的映射/序列项缩进，只有缩进更深的行才归属于该锚点
	 */
	private Object parseInlineValueOrAnchoredBlock(int parentIndent) {
		String anchor = tryReadLineEndAnchor();
		if (anchor == null) {
			Object value = parseInlineValue();
			consumeToLineEnd();
			return value;
		}
		skipBlankAndComments();
		Object value = "";
		if (pos < len) {
			if (lineIndent > parentIndent) {
				value = parseNode(parentIndent);
			} else if (lineIndent == parentIndent && src.charAt(pos) == '-' && isDashSeq()) {
				value = parseSequence(lineIndent);
			}
		}
		anchors.put(anchor, value);
		return value;
	}

	/**
	 * 识别独占行尾的锚点声明（如 "base: &amp;b"、"base: &amp;b # note"）。
	 * 命中时消费到换行之后并返回锚点名；未命中时恢复 pos 并返回 null。
	 */
	private String tryReadLineEndAnchor() {
		if (pos >= len || src.charAt(pos) != '&') return null;
		int save = pos;
		pos++;
		String name = readWord();
		skipInlineSpaces();
		boolean atLineEnd = pos >= len || src.charAt(pos) == '\n' || src.charAt(pos) == '\r' || src.charAt(pos) == '#';
		if (name.isEmpty() || !atLineEnd) { pos = save; return null; }
		if (pos < len && src.charAt(pos) == '#') {
			while (pos < len && src.charAt(pos) != '\n' && src.charAt(pos) != '\r') pos++;
		}
		if (pos < len) consumeNewline();
		return name;
	}
	
	private String parseBlockScalar(int hintIndent) {
		char style = src.charAt(pos);
		pos++;
		boolean strip = false, keep = false;
		int explicitIndent = -1;
		while (pos < len) {
			char c = src.charAt(pos);
			if (c == '+') { keep = true; pos++; }
			else if (c == '-') { strip = true; pos++; }
			else if (c >= '1' && c <= '9') { explicitIndent = c - '0'; pos++; }
			else break;
		}
		skipInlineSpaces();
		if (pos < len && src.charAt(pos) == '#') { while (pos < len && src.charAt(pos) != '\n' && src.charAt(pos) != '\r') pos++; }
		if (pos < len && (src.charAt(pos) == '\r' || src.charAt(pos) == '\n')) consumeNewline();
		
		int bodyStart = pos;
		// 第一遍扫描：只记录行边界与块缩进，不创建任何临时字符串
		// blockIndent 以字符数计（空格/tab 各 1 个字符），与第二遍切片位置保持一致
		int blockIndent = -1;
		int lastContentEnd = -1;
		int trailingBlank = 0;
		int blockStop = len;
		int scan = bodyStart;
		while (scan < len) {
			int lineEnd = scan;
			while (lineEnd < len && src.charAt(lineEnd) != '\n' && src.charAt(lineEnd) != '\r') lineEnd++;
			int indentChars = skipIndent(scan, lineEnd) - scan;
			boolean blankLine = scan + indentChars >= lineEnd;
			if (!blankLine) {
				if (blockIndent < 0) {
					blockIndent = explicitIndent > 0 ? Math.max(0, hintIndent) + explicitIndent : indentChars;
				}
				if (indentChars < blockIndent) { blockStop = scan; break; }
				lastContentEnd = lineEnd;
				trailingBlank = 0;
			} else {
				trailingBlank++;
			}
			int nextLine = skipLineBreak(src, lineEnd, len);
			if (nextLine >= len) { blockStop = len; break; }
			if (nextLine <= scan) { blockStop = lineEnd; break; }
			scan = nextLine;
		}

		// 第二遍扫描：按下标区间直接写入 StringBuilder，避免逐行 substring + String.join
		// 字面块按原长度预估容量，折叠块换行会被折叠成空格，按一半预估避免过分配
		int capacity = style == '|' ? lastContentEnd - bodyStart : Math.max(16, (lastContentEnd - bodyStart) / 2);
		StringBuilder sb = new StringBuilder(Math.max(0, capacity));
		int lineStart = bodyStart;
		boolean prevBlank = false;
		while (lineStart < lastContentEnd) {
			int lineEnd = lineStart;
			while (lineEnd < lastContentEnd && src.charAt(lineEnd) != '\n' && src.charAt(lineEnd) != '\r') lineEnd++;
			int contentFrom = Math.min(lineStart + blockIndent, lineEnd);
			boolean blankLine = skipIndent(lineStart, lineEnd) >= lineEnd;
			if (style == '|') {
				if (lineStart > bodyStart) sb.append('\n');
				if (!blankLine) sb.append(src, contentFrom, lineEnd);
			} else if (lineStart == bodyStart) {
				if (!blankLine) sb.append(src, contentFrom, lineEnd);
			} else if (blankLine) {
				sb.append('\n');
				prevBlank = true;
			} else if (prevBlank) {
				sb.append(src, contentFrom, lineEnd);
				prevBlank = false;
			} else {
				sb.append(' ').append(src, contentFrom, lineEnd);
			}
			int nextLine = skipLineBreak(src, lineEnd, len);
			if (nextLine <= lineStart) break;
			lineStart = nextLine;
		}

		// 尾部换行裁剪在 StringBuilder 上原地完成，避免 toString + substring 的整串拷贝
		int trimmed = sb.length();
		while (trimmed > 0 && sb.charAt(trimmed - 1) == '\n') trimmed--;
		sb.setLength(trimmed);
		if (keep) {
			for (int i = 0; i < trailingBlank; i++) sb.append('\n');
			if (sb.length() == 0 || sb.charAt(sb.length() - 1) != '\n') sb.append('\n');
		} else if (!strip) {
			if (lastContentEnd >= 0 || trailingBlank > 0) sb.append('\n');
		}
		pos = blockStop;
		return sb.toString();
	}

	/** 跳过行首空白（空格/制表符），返回首个非空白字符下标 */
	private int skipIndent(int from, int to) {
		int p = from;
		while (p < to && (src.charAt(p) == ' ' || src.charAt(p) == '\t')) p++;
		return p;
	}

	/** 去掉 [start, end) 首尾空白后生成子串，避免 substring + trim 的两次字符串分配 */
	private String substringTrimmed(int start, int end) {
		int e = end;
		while (e > start && isBlankChar(src.charAt(e - 1))) e--;
		int s = start;
		while (s < e && isBlankChar(src.charAt(s))) s++;
		return src.substring(s, e);
	}

	private static boolean isBlankChar(char c) {
		return c == ' ' || c == '\t' || c == '\n' || c == '\r';
	}

	/** 从 from 起扫描 plain key 的分隔冒号下标，遇到行尾/注释返回 -1 */
	private int scanKeyColon(int from) {
		int p = from;
		while (p < len) {
			char ch = src.charAt(p);
			if (ch == ':' && isKeyColon(p)) return p;
			if (ch == '\n' || ch == '\r' || ch == '#') return -1;
			p++;
		}
		return -1;
	}

	/** 扫描 plain key 到分隔冒号；命中时 pos 停在冒号上并返回 key，否则返回 null 且 pos 不变 */
	private String readPlainKeyToColon() {
		int colon = scanKeyColon(pos);
		if (colon < 0) return null;
		String key = substringTrimmed(pos, colon);
		if (key.isEmpty()) return null;
		pos = colon;
		return key;
	}
	
	private Object parseFlow() {
		char open = src.charAt(pos);
		return open == '{' ? parseFlowMap() : parseFlowList();
	}
	
	private Map<String, Object> parseFlowMap() {
		Map<String, Object> map = new LinkedHashMap<>();
		pos++;
		skipSpacesAndNewlines();
		if (pos < len && src.charAt(pos) == '}') { pos++; return map; }
		while (pos < len) {
			skipSpacesAndNewlines();
			String key = readFlowToken(true);
			skipSpacesAndNewlines();
			Object value = null;
			if (pos < len && src.charAt(pos) == ':') { pos++; skipSpacesAndNewlines(); value = readFlowValue(); }
			map.put(key, value);
			skipSpacesAndNewlines();
			if (pos < len && src.charAt(pos) == ',') { pos++; continue; }
			if (pos < len && src.charAt(pos) == '}') { pos++; break; }
			break;
		}
		return map;
	}
	
	private List<Object> parseFlowList() {
		List<Object> list = new ArrayList<>();
		pos++;
		skipSpacesAndNewlines();
		if (pos < len && src.charAt(pos) == ']') { pos++; return list; }
		while (pos < len) {
			skipSpacesAndNewlines();
			list.add(readFlowValue());
			skipSpacesAndNewlines();
			if (pos < len && src.charAt(pos) == ',') { pos++; continue; }
			if (pos < len && src.charAt(pos) == ']') { pos++; break; }
			break;
		}
		return list;
	}
	
	private Object readFlowValue() {
		skipSpacesAndNewlines();
		if (pos >= len) return "";
		char c = src.charAt(pos);
		if (c == '{' || c == '[') return parseFlow();
		if (c == '*') { pos++; Object ref = anchors.get(readWord()); return copyAliases ? deepCopy(ref) : ref; }
		if (c == '&') { pos++; String a = readWord(); skipSpacesAndNewlines(); Object v = readFlowValue(); anchors.put(a, v); return v; }
		return readFlowToken(false);
	}
	
	private String readFlowToken(boolean isKey) {
		skipSpacesAndNewlines();
		if (pos >= len) return "";
		char c = src.charAt(pos);
		if (c == '"' || c == '\'') return readQuoted(c);
		int start = pos;
		while (pos < len) {
			char ch = src.charAt(pos);
			if (isKey && (ch == ':' || ch == ',' || ch == '}' || ch == ']' || ch == '\n')) break;
			if (!isKey && (ch == ',' || ch == '}' || ch == ']' || ch == '\n')) break;
			pos++;
		}
		return substringTrimmed(start, pos);
	}
	
	private String parseScalarUntilLineEnd() {
		skipInlineSpaces();
		if (pos >= len) return "";
		char c = src.charAt(pos);
		if (c == '"' || c == '\'') return readQuoted(c);
		int start = pos;
		while (pos < len && src.charAt(pos) != '\n' && src.charAt(pos) != '\r') {
			// 行内注释：'#' 前有空白即视为注释开始，剥离（与 YAML 标量规则一致）
			if (src.charAt(pos) == '#' && pos > start && (src.charAt(pos - 1) == ' ' || src.charAt(pos - 1) == '\t')) break;
			pos++;
		}
		return substringTrimmed(start, pos); // 原样字符串，不做类型转换
	}
	
	// ==================== Key 读取 ====================
	
	private String readKey() {
		skipInlineSpaces();
		if (pos >= len) return null;
		char c = src.charAt(pos);
		if (c == '"' || c == '\'') {
			int save = pos;
			String q = readQuoted(c);
			skipInlineSpaces();
			if (pos < len && src.charAt(pos) == ':') return q;
			pos = save;
			return null;
		}
		if (c == '#' || c == '\n' || c == '\r') return null;
		return readPlainKeyToColon();
	}
	
	/**
	 * YAML 规则：冒号后必须紧跟空白或行尾，才是 key/value 分隔符。
	 * 例如 "- nacos:application.yml" 中的冒号属于标量内容，整行是一个字符串而非 key。
	 */
	private boolean isKeyColon(int p) {
		if (p + 1 >= len) return true;
		char n = src.charAt(p + 1);
		return n == ' ' || n == '\t' || n == '\n' || n == '\r';
	}
	
	private String tryReadKey() {
		int save = pos;
		String k = readKey();
		if (k == null) { pos = save; return null; }
		return k;
	}
	
	// ==================== 工具 ====================
	
	private String readQuoted(char quote) {
		pos++;
		int start = pos;
		// 快路径：无转义字符时直接按下标切片，避免逐字符 StringBuilder 追加
		int p = start;
		while (p < len) {
			char c = src.charAt(p);
			if (quote == '"' && c == '\\') break;
			if (c == quote) {
				if (quote == '\'' && p + 1 < len && src.charAt(p + 1) == '\'') break; // 单引号转义，走慢路径
				pos = p + 1;
				return src.substring(start, p);
			}
			p++;
		}
		pos = start;
		StringBuilder sb = new StringBuilder();
		while (pos < len) {
			char c = src.charAt(pos);
			if (quote == '"' && c == '\\' && pos + 1 < len) {
				pos++;
				char e = src.charAt(pos);
				switch (e) {
					case 'n': sb.append('\n'); break;
					case 't': sb.append('\t'); break;
					case 'r': sb.append('\r'); break;
					case '"': sb.append('"'); break;
					case '\\': sb.append('\\'); break;
					default: sb.append(e);
				}
				pos++;
			} else if (c == quote) {
				if (quote == '\'' && pos + 1 < len && src.charAt(pos + 1) == '\'') { sb.append('\''); pos += 2; }
				else { pos++; break; }
			} else { sb.append(c); pos++; }
		}
		return sb.toString();
	}
	
	private String readWord() {
		int start = pos;
		while (pos < len) {
			char c = src.charAt(pos);
			if (Character.isWhitespace(c) || c == ',' || c == '}' || c == ']' || c == '\n' || c == '\r') break;
			pos++;
		}
		return src.substring(start, pos);
	}
	
	/**
	 * 跳过空行与注释行，返回时：
	 * - pos 指向下一个内容行的首个非空白字符（缩进已被跳过）
	 * - lineIndent 记录该内容行的缩进宽度（tab 计 2 列）
	 * 注意：若 pos 已位于内容首字符（同一行重复调用），保留 lineIndent 不变。
	 */
	private void skipBlankAndComments() {
		while (pos < len) {
			char c = src.charAt(pos);
			if (c == ' ' || c == '\t') {
				if (!isLineStart(pos)) { pos++; continue; } // 行中空白，直接跳过
				int ind = 0;
				while (pos < len) {
					char w = src.charAt(pos);
					if (w == ' ') { ind++; pos++; }
					else if (w == '\t') { ind += 2; pos++; }
					else break;
				}
				if (pos >= len) { lineIndent = ind; return; }
				char n = src.charAt(pos);
				if (n == '\r' || n == '\n' || n == '#') { lineIndent = ind; continue; } // 空行/注释行，整行丢弃
				lineIndent = ind;
				return; // 内容行：pos 停在内容首字符
			}
			if (c == '\r' || c == '\n') { consumeNewline(); continue; }
			if (c == '#') { while (pos < len && src.charAt(pos) != '\n') pos++; continue; }
			// 内容首字符：位于物理行首说明该行无缩进；否则为同一行的重复调用（保留原 lineIndent）
			if (isLineStart(pos)) lineIndent = 0;
			return;
		}
	}
	private boolean isLineStart(int p) {
		return p == 0 || src.charAt(p - 1) == '\n' || src.charAt(p - 1) == '\r';
	}
	private void skipInlineSpaces() { while (pos < len && (src.charAt(pos) == ' ' || src.charAt(pos) == '\t')) pos++; }
	private void skipSpacesAndNewlines() {
		while (pos < len) {
			char c = src.charAt(pos);
			if (c == ' ' || c == '\t' || c == '\n' || c == '\r') pos++;
			else break;
		}
	}
	private void consumeNewline() {
		if (pos < len && src.charAt(pos) == '\r') pos++;
		if (pos < len && src.charAt(pos) == '\n') pos++;
	}
	private void consumeToLineEnd() {
		// 已在行首（典型场景：块标量解析结束已消费到下一行行首）时无需再消费，否则会吞掉下一行内容
		if (isLineStart(pos)) return;
		while (pos < len && src.charAt(pos) != '\n' && src.charAt(pos) != '\r') {
			if (src.charAt(pos) == '#') { while (pos < len && src.charAt(pos) != '\n' && src.charAt(pos) != '\r') pos++; break; }
			pos++;
		}
	}
	private boolean isDashSeq() { return pos < len && src.charAt(pos) == '-' && (pos + 1 >= len || Character.isWhitespace(src.charAt(pos + 1))); }
	
	@SuppressWarnings("unchecked")
	private Object deepCopy(Object o) {
		if (o == null) return null;
		if (o instanceof Map) {
			Map<String, Object> m = new LinkedHashMap<>();
			((Map<String, Object>) o).forEach((k, v) -> m.put(k, deepCopy(v)));
			return m;
		}
		if (o instanceof List) {
			List<Object> l = new ArrayList<>();
			for (Object e : (List<Object>) o) l.add(deepCopy(e));
			return l;
		}
		return o;
	}
}