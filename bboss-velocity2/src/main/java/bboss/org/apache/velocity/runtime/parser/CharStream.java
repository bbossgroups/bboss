package bboss.org.apache.velocity.runtime.parser;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/* Generated By:JavaCC: Do not edit this line. CharStream.java Version 2.1 */

/**
 * This interface describes a character stream that maintains line and
 * column number positions of the characters.  It also has the capability
 * to backup the stream to some extent.  An implementation of this
 * interface is used in the TokenManager implementation generated by
 * JavaCCParser.
 *
 * All the methods except backup can be implemented in any fashion. backup
 * needs to be implemented correctly for the correct operation of the lexer.
 * Rest of the methods are all used to get information like line number,
 * column number and the String that constitutes a token and are not used
 * by the lexer. Hence their implementation won't affect the generated lexer's
 * operation.
 */

public interface CharStream
{
    /**
    * Returns the next character from the selected input.  The method
    * of selecting the input is the responsibility of the class
    * implementing this interface.  Can throw any java.io.IOException.
    * @return read char
    * @throws java.io.IOException
    */
    char readChar() throws java.io.IOException;

    /**
    * Returns the column number of the last character for current token (being
    * matched after the last call to BeginTOken).
    * @return ending column number
    */
    int getEndColumn();

    /**
    * Returns the line number of the last character for current token (being
    * matched after the last call to BeginTOken).
    * @return ending line number
    */
    int getEndLine();

    /**
    * Returns the column number of the first character for current token (being
    * matched after the last call to BeginTOken).
    * @return starting column number
    */
    int getBeginColumn();

    /**
    * Returns the line number of the first character for current token (being
    * matched after the last call to BeginTOken).
    * @return starting line number
    */
    int getBeginLine();

    /**
    * Backs up the input stream by amount steps. Lexer calls this method if it
    * had already read some characters, but could not use them to match a
    * (longer) token. So, they will be used again as the prefix of the next
    * token and it is the implemetation's responsibility to do this right.
    * @param amount
    */
    void backup(int amount);

    /**
    * Returns the next character that marks the beginning of the next token.
    * All characters must remain in the buffer between two successive calls
    * to this method to implement backup correctly.
    * @return next token start char
    * @throws java.io.IOException
    */
    char BeginToken() throws java.io.IOException;

    /**
    * Returns a string made up of characters from the marked token beginning
    * to the current buffer position. Implementations have the choice of returning
    * anything that they want to. For example, for efficiency, one might decide
    * to just return null, which is a valid implementation.
    * @return token image
    */
    String GetImage();

    /**
    * <p>Returns an array of characters that make up the suffix of length 'len' for
    * the currently matched token. This is used to build up the matched string
    * for use in actions in the case of MORE. A simple and inefficient
    * implementation of this is as follows :</p>
    * <pre><code>
    *   {
    *      String t = GetImage();
    *      return t.substring(t.length() - len, t.length()).toCharArray();
    *   }
    *   </code></pre>
    *   @param len suffix len
    *   @return suffix
    */
    char[] GetSuffix(int len);

    /**
    * The lexer calls this function to indicate that it is done with the stream
    * and hence implementations can free any resources held by this class.
    * Again, the body of this function can be just empty and it will not
    * affect the lexer's operation.
    */
    void Done();

}
