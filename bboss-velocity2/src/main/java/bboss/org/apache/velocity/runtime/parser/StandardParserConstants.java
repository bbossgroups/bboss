/* Generated By:JJTree&JavaCC: Do not edit this line. StandardParserConstants.java */
package bboss.org.apache.velocity.runtime.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface StandardParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int LONE_SYMBOL = 1;
  /** RegularExpression Id. */
  int ZERO_WIDTH_WHITESPACE = 2;
  /** RegularExpression Id. */
  int INDEX_LBRACKET = 3;
  /** RegularExpression Id. */
  int LOGICAL_OR_2 = 4;
  /** RegularExpression Id. */
  int PIPE = 5;
  /** RegularExpression Id. */
  int INDEX_RBRACKET = 6;
  /** RegularExpression Id. */
  int LBRACKET = 7;
  /** RegularExpression Id. */
  int RBRACKET = 8;
  /** RegularExpression Id. */
  int COMMA = 9;
  /** RegularExpression Id. */
  int DOUBLEDOT = 10;
  /** RegularExpression Id. */
  int COLON = 11;
  /** RegularExpression Id. */
  int LEFT_CURLEY = 12;
  /** RegularExpression Id. */
  int RIGHT_CURLEY = 13;
  /** RegularExpression Id. */
  int LPAREN = 14;
  /** RegularExpression Id. */
  int RPAREN = 15;
  /** RegularExpression Id. */
  int REFMOD2_RPAREN = 16;
  /** RegularExpression Id. */
  int ESCAPE_DIRECTIVE = 17;
  /** RegularExpression Id. */
  int SET_DIRECTIVE = 18;
  /** RegularExpression Id. */
  int DOLLAR = 19;
  /** RegularExpression Id. */
  int DOLLARBANG = 20;
  /** RegularExpression Id. */
  int HASH = 24;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT_START = 25;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 26;
  /** RegularExpression Id. */
  int FORMAL_COMMENT = 27;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 28;
  /** RegularExpression Id. */
  int TEXTBLOCK = 29;
  /** RegularExpression Id. */
  int WHITESPACE = 32;
  /** RegularExpression Id. */
  int NEWLINE = 33;
  /** RegularExpression Id. */
  int SUFFIX = 34;
  /** RegularExpression Id. */
  int STRING_LITERAL = 35;
  /** RegularExpression Id. */
  int TRUE = 36;
  /** RegularExpression Id. */
  int FALSE = 37;
  /** RegularExpression Id. */
  int MINUS = 38;
  /** RegularExpression Id. */
  int PLUS = 39;
  /** RegularExpression Id. */
  int MULTIPLY = 40;
  /** RegularExpression Id. */
  int DIVIDE = 41;
  /** RegularExpression Id. */
  int MODULUS = 42;
  /** RegularExpression Id. */
  int LOGICAL_AND = 43;
  /** RegularExpression Id. */
  int LOGICAL_OR = 44;
  /** RegularExpression Id. */
  int LOGICAL_LT = 45;
  /** RegularExpression Id. */
  int LOGICAL_LE = 46;
  /** RegularExpression Id. */
  int LOGICAL_GT = 47;
  /** RegularExpression Id. */
  int LOGICAL_GE = 48;
  /** RegularExpression Id. */
  int LOGICAL_EQUALS = 49;
  /** RegularExpression Id. */
  int LOGICAL_NOT_EQUALS = 50;
  /** RegularExpression Id. */
  int LOGICAL_NOT = 51;
  /** RegularExpression Id. */
  int EQUALS = 52;
  /** RegularExpression Id. */
  int END = 53;
  /** RegularExpression Id. */
  int IF_DIRECTIVE = 54;
  /** RegularExpression Id. */
  int ELSEIF = 55;
  /** RegularExpression Id. */
  int ELSE = 56;
  /** RegularExpression Id. */
  int DIGIT = 57;
  /** RegularExpression Id. */
  int INTEGER_LITERAL = 58;
  /** RegularExpression Id. */
  int FLOATING_POINT_LITERAL = 59;
  /** RegularExpression Id. */
  int EXPONENT = 60;
  /** RegularExpression Id. */
  int LETTER = 61;
  /** RegularExpression Id. */
  int DIRECTIVE_CHAR = 62;
  /** RegularExpression Id. */
  int WORD = 63;
  /** RegularExpression Id. */
  int BRACKETED_WORD = 64;
  /** RegularExpression Id. */
  int ALPHA_CHAR = 65;
  /** RegularExpression Id. */
  int IDENTIFIER_CHAR = 66;
  /** RegularExpression Id. */
  int IDENTIFIER = 67;
  /** RegularExpression Id. */
  int OLD_ALPHA_CHAR = 68;
  /** RegularExpression Id. */
  int OLD_IDENTIFIER_CHAR = 69;
  /** RegularExpression Id. */
  int OLD_IDENTIFIER = 70;
  /** RegularExpression Id. */
  int DOT = 71;
  /** RegularExpression Id. */
  int LCURLY = 72;
  /** RegularExpression Id. */
  int RCURLY = 73;
  /** RegularExpression Id. */
  int REFERENCE_TERMINATOR = 74;
  /** RegularExpression Id. */
  int DIRECTIVE_TERMINATOR = 75;
  /** RegularExpression Id. */
  int DOUBLE_ESCAPE = 76;
  /** RegularExpression Id. */
  int ESCAPE = 77;
  /** RegularExpression Id. */
  int TEXT = 78;
  /** RegularExpression Id. */
  int INLINE_TEXT = 79;
  /** RegularExpression Id. */
  int EMPTY_INDEX = 80;

  /** Lexical state. */
  int PRE_DIRECTIVE = 0;
  /** Lexical state. */
  int PRE_REFERENCE = 1;
  /** Lexical state. */
  int PRE_OLD_REFERENCE = 2;
  /** Lexical state. */
  int REFERENCE = 3;
  /** Lexical state. */
  int REFMODIFIER = 4;
  /** Lexical state. */
  int OLD_REFMODIFIER = 5;
  /** Lexical state. */
  int REFMOD3 = 6;
  /** Lexical state. */
  int REFINDEX = 7;
  /** Lexical state. */
  int DIRECTIVE = 8;
  /** Lexical state. */
  int REFMOD2 = 9;
  /** Lexical state. */
  int DEFAULT = 10;
  /** Lexical state. */
  int REFMOD = 11;
  /** Lexical state. */
  int IN_MULTILINE_COMMENT = 12;
  /** Lexical state. */
  int IN_FORMAL_COMMENT = 13;
  /** Lexical state. */
  int IN_SINGLE_LINE_COMMENT = 14;
  /** Lexical state. */
  int ALT_VAL = 15;
  /** Lexical state. */
  int IN_TEXTBLOCK = 16;
  /** Lexical state. */
  int IN_MULTI_LINE_COMMENT = 17;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\"\\u001c\"",
    "\"\\u001c\"",
    "\"[\"",
    "\"||\"",
    "\"|\"",
    "\"]\"",
    "\"[\"",
    "\"]\"",
    "\",\"",
    "\"..\"",
    "\":\"",
    "\"{\"",
    "\"}\"",
    "\"(\"",
    "\")\"",
    "\")\"",
    "<ESCAPE_DIRECTIVE>",
    "<SET_DIRECTIVE>",
    "<DOLLAR>",
    "<DOLLARBANG>",
    "\"#[[\"",
    "<token of kind 22>",
    "\"#*\"",
    "\"#\"",
    "\"##\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*#\"",
    "\"*#\"",
    "\"]]#\"",
    "<token of kind 30>",
    "<token of kind 31>",
    "<WHITESPACE>",
    "<NEWLINE>",
    "<SUFFIX>",
    "<STRING_LITERAL>",
    "\"true\"",
    "\"false\"",
    "\"-\"",
    "\"+\"",
    "\"*\"",
    "\"/\"",
    "\"%\"",
    "<LOGICAL_AND>",
    "<LOGICAL_OR>",
    "<LOGICAL_LT>",
    "<LOGICAL_LE>",
    "<LOGICAL_GT>",
    "<LOGICAL_GE>",
    "<LOGICAL_EQUALS>",
    "<LOGICAL_NOT_EQUALS>",
    "<LOGICAL_NOT>",
    "\"=\"",
    "<END>",
    "<IF_DIRECTIVE>",
    "<ELSEIF>",
    "<ELSE>",
    "<DIGIT>",
    "<INTEGER_LITERAL>",
    "<FLOATING_POINT_LITERAL>",
    "<EXPONENT>",
    "<LETTER>",
    "<DIRECTIVE_CHAR>",
    "<WORD>",
    "<BRACKETED_WORD>",
    "<ALPHA_CHAR>",
    "<IDENTIFIER_CHAR>",
    "<IDENTIFIER>",
    "<OLD_ALPHA_CHAR>",
    "<OLD_IDENTIFIER_CHAR>",
    "<OLD_IDENTIFIER>",
    "<DOT>",
    "\"{\"",
    "\"}\"",
    "<REFERENCE_TERMINATOR>",
    "<DIRECTIVE_TERMINATOR>",
    "\"\\\\\\\\\"",
    "\"\\\\\"",
    "<TEXT>",
    "<INLINE_TEXT>",
    "<EMPTY_INDEX>",
  };

}
