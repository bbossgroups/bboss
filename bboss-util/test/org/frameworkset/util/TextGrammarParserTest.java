package org.frameworkset.util;

import com.frameworkset.util.RegexUtil;
import org.frameworkset.util.tokenizer.TextGrammarParser;
import org.frameworkset.util.tokenizer.TextGrammarParser.GrammarToken;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TextGrammarParserTest {

	public TextGrammarParserTest() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void testCharEnd()
	{
		
		String tokenpre = "#inlcude(";
		
		char tokenend = ')';
		String content = "#inlcude(head.html)";
		List<GrammarToken> tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);
		
		content = "asdfasdf#inlcude(#inlcude(head.html)";
		tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);
		
		content = "asdfasdf#inlcude(#inlcude(head.html))";
		tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);
		
		content = "asdfasdf#inlcude(#inlcude(head.html";
		tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);
		
		content = "#inlcude()";
		tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);
	}

	@Test
	public void testStringEnd()
	{

		String tokenpre = "#inlcude(";

		String tokenend = "))";
		String content = "#inlcude(head.html))";
		List<GrammarToken> tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);

		content = "asdfasdf#inlcude(#inlcude(head.html))";
		tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);

		content = "asdfasdf#inlcude(#inlcude(head.html))))";
		tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);

		content = "asdfasdf#inlcude(#inlcude(head.html";
		tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);

		content = "#inlcude())";
		tokens = TextGrammarParser.parser(content, tokenpre, tokenend);
		System.out.println(content+"分词结果：\r\n");
		System.out.println(tokens);
	}

	@Test
	public void varialValue(){
		String tokenpre = "${";

		String tokenend = "}";
		String str1 = "${a} hello ${b}";
		List<GrammarToken> tokens = TextGrammarParser.parser(str1, tokenpre, tokenend);
		StringBuilder pattern = new StringBuilder();
		for(GrammarToken token :tokens){
			if(token.texttoken()){
				pattern.append(token.getText());
			}
			else{
				pattern.append("(").append(".*").append(")");
			}
		}


		String str2 = "666 c  hello 777";
		String[] values = RegexUtil.parser(str2,pattern.toString());
		Map<String,String> data = new LinkedHashMap<>();
		int i = 0;
		for(GrammarToken token :tokens){
			if(token.varibletoken()){
				data.put(token.getText(),values[i]);
				i ++;
			}
		}
		System.out.println(data);


	}

}
