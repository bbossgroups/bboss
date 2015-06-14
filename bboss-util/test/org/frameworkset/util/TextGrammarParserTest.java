package org.frameworkset.util;

import java.util.List;

import org.frameworkset.util.tokenizer.TextGrammarParser;
import org.frameworkset.util.tokenizer.TextGrammarParser.GrammarToken;
import org.junit.Test;

public class TextGrammarParserTest {

	public TextGrammarParserTest() {
		// TODO Auto-generated constructor stub
	}
	@Test
	public void test()
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

}
