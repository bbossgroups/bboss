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

import bboss.org.objectweb.asm.AnnotationVisitor;
import bboss.org.objectweb.asm.Opcodes;

/**
 * An {@link AnnotationVisitor} that remaps types with a {@link Remapper}.
 *
 * @author Eugene Kuleshov
 */
public class AnnotationRemapper extends AnnotationVisitor {

  /**
   * The descriptor of the visited annotation. May be {@literal null}, for instance for
   * AnnotationDefault.
   */
  protected final String descriptor;

  /** The remapper used to remap the types in the visited annotation. */
  protected final Remapper remapper;

  /**
   * Constructs a new {@link AnnotationRemapper}. <i>Subclasses must not use this constructor</i>.
   * Instead, they must use the {@link #AnnotationRemapper(int,AnnotationVisitor,Remapper)} version.
   *
   * @param annotationVisitor the annotation visitor this remapper must delegate to.
   * @param remapper the remapper to use to remap the types in the visited annotation.
   * @deprecated use {@link #AnnotationRemapper(String, AnnotationVisitor, Remapper)} instead.
   */
  @Deprecated
  public AnnotationRemapper(final AnnotationVisitor annotationVisitor, final Remapper remapper) {
    this(/* descriptor= */ null, annotationVisitor, remapper);
  }

  /**
   * Constructs a new {@link AnnotationRemapper}. <i>Subclasses must not use this constructor</i>.
   * Instead, they must use the {@link #AnnotationRemapper(int,String,AnnotationVisitor,Remapper)}
   * version.
   *
   * @param descriptor the descriptor of the visited annotation. May be {@literal null}.
   * @param annotationVisitor the annotation visitor this remapper must delegate to.
   * @param remapper the remapper to use to remap the types in the visited annotation.
   */
  public AnnotationRemapper(
      final String descriptor, final AnnotationVisitor annotationVisitor, final Remapper remapper) {
    this(/* latest api = */ Opcodes.ASM9, descriptor, annotationVisitor, remapper);
  }

  /**
   * Constructs a new {@link AnnotationRemapper}.
   *
   * @param api the ASM API version supported by this remapper. Must be one of the {@code
   *     ASM}<i>x</i> values in {@link Opcodes}.
   * @param annotationVisitor the annotation visitor this remapper must delegate to.
   * @param remapper the remapper to use to remap the types in the visited annotation.
   * @deprecated use {@link #AnnotationRemapper(int, String, AnnotationVisitor, Remapper)} instead.
   */
  @Deprecated
  protected AnnotationRemapper(
      final int api, final AnnotationVisitor annotationVisitor, final Remapper remapper) {
    this(api, /* descriptor= */ null, annotationVisitor, remapper);
  }

  /**
   * Constructs a new {@link AnnotationRemapper}.
   *
   * @param api the ASM API version supported by this remapper. Must be one of the {@code
   *     ASM}<i>x</i> values in {@link Opcodes}.
   * @param descriptor the descriptor of the visited annotation. May be {@literal null}.
   * @param annotationVisitor the annotation visitor this remapper must delegate to.
   * @param remapper the remapper to use to remap the types in the visited annotation.
   */
  protected AnnotationRemapper(
      final int api,
      final String descriptor,
      final AnnotationVisitor annotationVisitor,
      final Remapper remapper) {
    super(api, annotationVisitor);
    this.descriptor = descriptor;
    this.remapper = remapper;
  }

  @Override
  public void visit(final String name, final Object value) {
    super.visit(mapAnnotationAttributeName(name), remapper.mapValue(value));
  }

  @Override
  public void visitEnum(final String name, final String descriptor, final String value) {
    super.visitEnum(mapAnnotationAttributeName(name), remapper.mapDesc(descriptor), value);
  }

  @Override
  public AnnotationVisitor visitAnnotation(final String name, final String descriptor) {
    AnnotationVisitor annotationVisitor =
        super.visitAnnotation(mapAnnotationAttributeName(name), remapper.mapDesc(descriptor));
    if (annotationVisitor == null) {
      return null;
    } else {
      return annotationVisitor == av
          ? this
          : createAnnotationRemapper(descriptor, annotationVisitor);
    }
  }

  @Override
  public AnnotationVisitor visitArray(final String name) {
    AnnotationVisitor annotationVisitor = super.visitArray(mapAnnotationAttributeName(name));
    if (annotationVisitor == null) {
      return null;
    } else {
      return annotationVisitor == av
          ? this
          : createAnnotationRemapper(/* descriptor= */ null, annotationVisitor);
    }
  }

  /**
   * Constructs a new remapper for annotations. The default implementation of this method returns a
   * new {@link AnnotationRemapper}.
   *
   * @param annotationVisitor the AnnotationVisitor the remapper must delegate to.
   * @return the newly created remapper.
   * @deprecated use {@link #createAnnotationRemapper(String, AnnotationVisitor)} instead.
   */
  @Deprecated
  protected AnnotationVisitor createAnnotationRemapper(final AnnotationVisitor annotationVisitor) {
    return new AnnotationRemapper(api, /* descriptor= */ null, annotationVisitor, remapper);
  }

  /**
   * Constructs a new remapper for annotations. The default implementation of this method returns a
   * new {@link AnnotationRemapper}.
   *
   * @param descriptor the descriptor of the visited annotation.
   * @param annotationVisitor the AnnotationVisitor the remapper must delegate to.
   * @return the newly created remapper.
   */
  protected AnnotationVisitor createAnnotationRemapper(
      final String descriptor, final AnnotationVisitor annotationVisitor) {
    return new AnnotationRemapper(api, descriptor, annotationVisitor, remapper)
        .orDeprecatedValue(createAnnotationRemapper(annotationVisitor));
  }

  /**
   * Returns either this object, or the given one. If the given object is equal to the object
   * returned by the default implementation of the deprecated createAnnotationRemapper method,
   * meaning that this method has not been overridden (or only in minor ways, for instance to add
   * logging), then we can return this object instead, supposed to have been created by the new
   * createAnnotationRemapper method. Otherwise we must return the given object.
   *
   * @param deprecatedAnnotationVisitor the result of a call to the deprecated
   *     createAnnotationRemapper method.
   * @return either this object, or the given one.
   */
  final AnnotationVisitor orDeprecatedValue(final AnnotationVisitor deprecatedAnnotationVisitor) {
    if (deprecatedAnnotationVisitor.getClass() == getClass()) {
      AnnotationRemapper deprecatedAnnotationRemapper =
          (AnnotationRemapper) deprecatedAnnotationVisitor;
      if (deprecatedAnnotationRemapper.api == api
          && deprecatedAnnotationRemapper.av == av
          && deprecatedAnnotationRemapper.remapper == remapper) {
        return this;
      }
    }
    return deprecatedAnnotationVisitor;
  }

  /**
   * Maps an annotation attribute name with the remapper. Returns the original name unchanged if the
   * descriptor of the annotation is {@literal null}.
   *
   * @param name the name of the annotation attribute.
   * @return the new name of the annotation attribute.
   */
  private String mapAnnotationAttributeName(final String name) {
    if (descriptor == null) {
      return name;
    }
    return remapper.mapAnnotationAttributeName(descriptor, name);
  }
}
