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
package bboss.org.objectweb.asm.util;

import bboss.org.objectweb.asm.AnnotationVisitor;
import bboss.org.objectweb.asm.Attribute;
import bboss.org.objectweb.asm.Opcodes;
import bboss.org.objectweb.asm.RecordComponentVisitor;
import bboss.org.objectweb.asm.TypePath;
import bboss.org.objectweb.asm.TypeReference;

/**
 * A {@link RecordComponentVisitor} that checks that its methods are properly used.
 *
 * @author Eric Bruneton
 * @author Remi Forax
 */
public class CheckRecordComponentAdapter extends RecordComponentVisitor {

  /** Whether the {@link #visitEnd()} method has been called. */
  private boolean visitEndCalled;

  /**
   * Constructs a new {@link CheckRecordComponentAdapter}. <i>Subclasses must not use this
   * constructor</i>. Instead, they must use the {@link #CheckRecordComponentAdapter(int,
   * RecordComponentVisitor)} version.
   *
   * @param recordComponentVisitor the record component visitor to which this adapter must delegate
   *     calls.
   * @throws IllegalStateException If a subclass calls this constructor.
   */
  public CheckRecordComponentAdapter(final RecordComponentVisitor recordComponentVisitor) {
    this(/* latest api =*/ Opcodes.ASM9, recordComponentVisitor);
    if (getClass() != CheckRecordComponentAdapter.class) {
      throw new IllegalStateException();
    }
  }

  /**
   * Constructs a new {@link CheckRecordComponentAdapter}.
   *
   * @param api the ASM API version implemented by this visitor. Must be one of {@link Opcodes#ASM8}
   *     or {@link Opcodes#ASM9}.
   * @param recordComponentVisitor the record component visitor to which this adapter must delegate
   *     calls.
   */
  protected CheckRecordComponentAdapter(
      final int api, final RecordComponentVisitor recordComponentVisitor) {
    super(api, recordComponentVisitor);
  }

  @Override
  public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
    checkVisitEndNotCalled();
    // Annotations can only appear in V1_5 or more classes.
    CheckMethodAdapter.checkDescriptor(Opcodes.V1_5, descriptor, false);
    return new CheckAnnotationAdapter(super.visitAnnotation(descriptor, visible));
  }

  @Override
  public AnnotationVisitor visitTypeAnnotation(
      final int typeRef, final TypePath typePath, final String descriptor, final boolean visible) {
    checkVisitEndNotCalled();
    int sort = new TypeReference(typeRef).getSort();
    if (sort != TypeReference.FIELD) {
      throw new IllegalArgumentException(
          "Invalid type reference sort 0x" + Integer.toHexString(sort));
    }
    CheckClassAdapter.checkTypeRef(typeRef);
    CheckMethodAdapter.checkDescriptor(Opcodes.V1_5, descriptor, false);
    return new CheckAnnotationAdapter(
        super.visitTypeAnnotation(typeRef, typePath, descriptor, visible));
  }

  @Override
  public void visitAttribute(final Attribute attribute) {
    checkVisitEndNotCalled();
    if (attribute == null) {
      throw new IllegalArgumentException("Invalid attribute (must not be null)");
    }
    super.visitAttribute(attribute);
  }

  @Override
  public void visitEnd() {
    checkVisitEndNotCalled();
    visitEndCalled = true;
    super.visitEnd();
  }

  private void checkVisitEndNotCalled() {
    if (visitEndCalled) {
      throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
    }
  }
}
