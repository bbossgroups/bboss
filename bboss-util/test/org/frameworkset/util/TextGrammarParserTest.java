package org.frameworkset.util;

import org.frameworkset.util.tokenizer.TextGrammarParser;
import org.frameworkset.util.tokenizer.TextGrammarParser.GrammarToken;
import org.junit.Test;

import java.util.List;

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

}
