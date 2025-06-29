// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package bboss.org.objectweb.asm.tree.analysis;

import bboss.org.objectweb.asm.tree.AbstractInsnNode;

/**
 * An exception thrown if a problem occurs during the analysis of a method.
 *
 * @author Bing Ran
 * @author Eric Bruneton
 */
public class AnalyzerException extends Exception {

  private static final long serialVersionUID = 3154190448018943333L;

  /** The bytecode instruction where the analysis failed. */
  public final transient AbstractInsnNode node;

  /**
   * Constructs a new {@link AnalyzerException}.
   *
   * @param insn the bytecode instruction where the analysis failed.
   * @param message the reason why the analysis failed.
   */
  public AnalyzerException(final AbstractInsnNode insn, final String message) {
    super(message);
    this.node = insn;
  }

  /**
   * Constructs a new {@link AnalyzerException}.
   *
   * @param insn the bytecode instruction where the analysis failed.
   * @param message the reason why the analysis failed.
   * @param cause the cause of the failure.
   */
  public AnalyzerException(
      final AbstractInsnNode insn, final String message, final Throwable cause) {
    super(message, cause);
    this.node = insn;
  }

  /**
   * Constructs a new {@link AnalyzerException}.
   *
   * @param insn the bytecode instruction where the analysis failed.
   * @param message the reason why the analysis failed.
   * @param expected an expected value.
   * @param actual the actual value, different from the expected one.
   */
  public AnalyzerException(
      final AbstractInsnNode insn,
      final String message,
      final Object expected,
      final Value actual) {
    super(
        (message == null ? "Expected " : message + ": expected ")
            + expected
            + ", but found "
            + actual);
    this.node = insn;
  }
}
