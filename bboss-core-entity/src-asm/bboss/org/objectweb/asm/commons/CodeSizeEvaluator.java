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
package bboss.org.objectweb.asm.commons;

import bboss.org.objectweb.asm.ConstantDynamic;
import bboss.org.objectweb.asm.Handle;
import bboss.org.objectweb.asm.Label;
import bboss.org.objectweb.asm.MethodVisitor;
import bboss.org.objectweb.asm.Opcodes;

/**
 * A {@link MethodVisitor} that approximates the size of the methods it visits.
 *
 * @author Eugene Kuleshov
 */
public class CodeSizeEvaluator extends MethodVisitor implements Opcodes {

  /** The minimum size in bytes of the visited method. */
  private int minSize;

  /** The maximum size in bytes of the visited method. */
  private int maxSize;

  public CodeSizeEvaluator(final MethodVisitor methodVisitor) {
    this(/* latest api = */ Opcodes.ASM9, methodVisitor);
  }

  protected CodeSizeEvaluator(final int api, final MethodVisitor methodVisitor) {
    super(api, methodVisitor);
  }

  public int getMinSize() {
    return this.minSize;
  }

  public int getMaxSize() {
    return this.maxSize;
  }

  @Override
  public void visitInsn(final int opcode) {
    minSize += 1;
    maxSize += 1;
    super.visitInsn(opcode);
  }

  @Override
  public void visitIntInsn(final int opcode, final int operand) {
    if (opcode == SIPUSH) {
      minSize += 3;
      maxSize += 3;
    } else {
      minSize += 2;
      maxSize += 2;
    }
    super.visitIntInsn(opcode, operand);
  }

  @Override
  public void visitVarInsn(final int opcode, final int varIndex) {
    if (varIndex < 4 && opcode != RET) {
      minSize += 1;
      maxSize += 1;
    } else if (varIndex >= 256) {
      minSize += 4;
      maxSize += 4;
    } else {
      minSize += 2;
      maxSize += 2;
    }
    super.visitVarInsn(opcode, varIndex);
  }

  @Override
  public void visitTypeInsn(final int opcode, final String type) {
    minSize += 3;
    maxSize += 3;
    super.visitTypeInsn(opcode, type);
  }

  @Override
  public void visitFieldInsn(
      final int opcode, final String owner, final String name, final String descriptor) {
    minSize += 3;
    maxSize += 3;
    super.visitFieldInsn(opcode, owner, name, descriptor);
  }

  @Override
  public void visitMethodInsn(
      final int opcodeAndSource,
      final String owner,
      final String name,
      final String descriptor,
      final boolean isInterface) {
    if (api < Opcodes.ASM5 && (opcodeAndSource & Opcodes.SOURCE_DEPRECATED) == 0) {
      // Redirect the call to the deprecated version of this method.
      super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
      return;
    }
    int opcode = opcodeAndSource & ~Opcodes.SOURCE_MASK;

    if (opcode == INVOKEINTERFACE) {
      minSize += 5;
      maxSize += 5;
    } else {
      minSize += 3;
      maxSize += 3;
    }
    super.visitMethodInsn(opcodeAndSource, owner, name, descriptor, isInterface);
  }

  @Override
  public void visitInvokeDynamicInsn(
      final String name,
      final String descriptor,
      final Handle bootstrapMethodHandle,
      final Object... bootstrapMethodArguments) {
    minSize += 5;
    maxSize += 5;
    super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
  }

  @Override
  public void visitJumpInsn(final int opcode, final Label label) {
    minSize += 3;
    if (opcode == GOTO || opcode == JSR) {
      maxSize += 5;
    } else {
      maxSize += 8;
    }
    super.visitJumpInsn(opcode, label);
  }

  @Override
  public void visitLdcInsn(final Object value) {
    if (value instanceof Long
        || value instanceof Double
        || (value instanceof ConstantDynamic && ((ConstantDynamic) value).getSize() == 2)) {
      minSize += 3;
      maxSize += 3;
    } else {
      minSize += 2;
      maxSize += 3;
    }
    super.visitLdcInsn(value);
  }

  @Override
  public void visitIincInsn(final int varIndex, final int increment) {
    if (varIndex > 255 || increment > 127 || increment < -128) {
      minSize += 6;
      maxSize += 6;
    } else {
      minSize += 3;
      maxSize += 3;
    }
    super.visitIincInsn(varIndex, increment);
  }

  @Override
  public void visitTableSwitchInsn(
      final int min, final int max, final Label dflt, final Label... labels) {
    minSize += 13 + labels.length * 4;
    maxSize += 16 + labels.length * 4;
    super.visitTableSwitchInsn(min, max, dflt, labels);
  }

  @Override
  public void visitLookupSwitchInsn(final Label dflt, final int[] keys, final Label[] labels) {
    minSize += 9 + keys.length * 8;
    maxSize += 12 + keys.length * 8;
    super.visitLookupSwitchInsn(dflt, keys, labels);
  }

  @Override
  public void visitMultiANewArrayInsn(final String descriptor, final int numDimensions) {
    minSize += 4;
    maxSize += 4;
    super.visitMultiANewArrayInsn(descriptor, numDimensions);
  }
}
