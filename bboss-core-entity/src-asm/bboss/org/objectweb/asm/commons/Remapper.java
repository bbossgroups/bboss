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
import bboss.org.objectweb.asm.Opcodes;
import bboss.org.objectweb.asm.Type;
import bboss.org.objectweb.asm.signature.SignatureReader;
import bboss.org.objectweb.asm.signature.SignatureVisitor;
import bboss.org.objectweb.asm.signature.SignatureWriter;

/**
 * A class responsible for remapping types and names.
 *
 * @author Eugene Kuleshov
 */
public abstract class Remapper {

  /**
   * Returns the given descriptor, remapped with {@link #map(String)}.
   *
   * @param descriptor a type descriptor.
   * @return the given descriptor, with its [array element type] internal name remapped with {@link
   *     #map(String)} (if the descriptor corresponds to an array or object type, otherwise the
   *     descriptor is returned as is). See {@link Type#getInternalName()}.
   */
  public String mapDesc(final String descriptor) {
    return mapType(Type.getType(descriptor)).getDescriptor();
  }

  /**
   * Returns the given {@link Type}, remapped with {@link #map(String)} or {@link
   * #mapMethodDesc(String)}.
   *
   * @param type a type, which can be a method type.
   * @return the given type, with its [array element type] internal name remapped with {@link
   *     #map(String)} (if the type is an array or object type, otherwise the type is returned as
   *     is) or, of the type is a method type, with its descriptor remapped with {@link
   *     #mapMethodDesc(String)}. See {@link Type#getInternalName()}.
   */
  private Type mapType(final Type type) {
    switch (type.getSort()) {
      case Type.ARRAY:
        StringBuilder remappedDescriptor = new StringBuilder();
        for (int i = 0; i < type.getDimensions(); ++i) {
          remappedDescriptor.append('[');
        }
        remappedDescriptor.append(mapType(type.getElementType()).getDescriptor());
        return Type.getType(remappedDescriptor.toString());
      case Type.OBJECT:
        String remappedInternalName = map(type.getInternalName());
        return remappedInternalName != null ? Type.getObjectType(remappedInternalName) : type;
      case Type.METHOD:
        return Type.getMethodType(mapMethodDesc(type.getDescriptor()));
      default:
        return type;
    }
  }

  /**
   * Returns the given internal name, remapped with {@link #map(String)}.
   *
   * @param internalName the internal name (or array type descriptor) of some (array) class (see
   *     {@link Type#getInternalName()}).
   * @return the given internal name, remapped with {@link #map(String)} (see {@link
   *     Type#getInternalName()}).
   */
  public String mapType(final String internalName) {
    if (internalName == null) {
      return null;
    }
    return mapType(Type.getObjectType(internalName)).getInternalName();
  }

  /**
   * Returns the given internal names, remapped with {@link #map(String)}.
   *
   * @param internalNames the internal names (or array type descriptors) of some (array) classes
   *     (see {@link Type#getInternalName()}).
   * @return the given internal name, remapped with {@link #map(String)} (see {@link
   *     Type#getInternalName()}).
   */
  public String[] mapTypes(final String[] internalNames) {
    String[] remappedInternalNames = null;
    for (int i = 0; i < internalNames.length; ++i) {
      String internalName = internalNames[i];
      String remappedInternalName = mapType(internalName);
      if (remappedInternalName != null) {
        if (remappedInternalNames == null) {
          remappedInternalNames = internalNames.clone();
        }
        remappedInternalNames[i] = remappedInternalName;
      }
    }
    return remappedInternalNames != null ? remappedInternalNames : internalNames;
  }

  /**
   * Returns the given method descriptor, with its argument and return type descriptors remapped
   * with {@link #mapDesc(String)}.
   *
   * @param methodDescriptor a method descriptor.
   * @return the given method descriptor, with its argument and return type descriptors remapped
   *     with {@link #mapDesc(String)}.
   */
  public String mapMethodDesc(final String methodDescriptor) {
    if ("()V".equals(methodDescriptor)) {
      return methodDescriptor;
    }

    StringBuilder stringBuilder = new StringBuilder("(");
    for (Type argumentType : Type.getArgumentTypes(methodDescriptor)) {
      stringBuilder.append(mapType(argumentType).getDescriptor());
    }
    Type returnType = Type.getReturnType(methodDescriptor);
    if (returnType == Type.VOID_TYPE) {
      stringBuilder.append(")V");
    } else {
      stringBuilder.append(')').append(mapType(returnType).getDescriptor());
    }
    return stringBuilder.toString();
  }

  /**
   * Returns the given value, remapped with this remapper. Possible values are {@link Boolean},
   * {@link Byte}, {@link Short}, {@link Character}, {@link Integer}, {@link Long}, {@link Double},
   * {@link Float}, {@link String}, {@link Type}, {@link Handle}, {@link ConstantDynamic} or arrays
   * of primitive types .
   *
   * @param value an object. Only {@link Type}, {@link Handle} and {@link ConstantDynamic} values
   *     are remapped.
   * @return the given value, remapped with this remapper.
   */
  public Object mapValue(final Object value) {
    if (value instanceof Type) {
      return mapType((Type) value);
    }
    if (value instanceof Handle) {
      Handle handle = (Handle) value;
      boolean isFieldHandle = handle.getTag() <= Opcodes.H_PUTSTATIC;

      return new Handle(
          handle.getTag(),
          mapType(handle.getOwner()),
          isFieldHandle
              ? mapFieldName(handle.getOwner(), handle.getName(), handle.getDesc())
              : mapMethodName(handle.getOwner(), handle.getName(), handle.getDesc()),
          isFieldHandle ? mapDesc(handle.getDesc()) : mapMethodDesc(handle.getDesc()),
          handle.isInterface());
    }
    if (value instanceof ConstantDynamic) {
      ConstantDynamic constantDynamic = (ConstantDynamic) value;
      int bootstrapMethodArgumentCount = constantDynamic.getBootstrapMethodArgumentCount();
      Object[] remappedBootstrapMethodArguments = new Object[bootstrapMethodArgumentCount];
      for (int i = 0; i < bootstrapMethodArgumentCount; ++i) {
        remappedBootstrapMethodArguments[i] =
            mapValue(constantDynamic.getBootstrapMethodArgument(i));
      }
      String descriptor = constantDynamic.getDescriptor();
      return new ConstantDynamic(
          mapInvokeDynamicMethodName(constantDynamic.getName(), descriptor),
          mapDesc(descriptor),
          (Handle) mapValue(constantDynamic.getBootstrapMethod()),
          remappedBootstrapMethodArguments);
    }
    return value;
  }

  /**
   * Returns the given signature, remapped with the {@link SignatureVisitor} returned by {@link
   * #createSignatureRemapper(SignatureVisitor)}.
   *
   * @param signature a <i>JavaTypeSignature</i>, <i>ClassSignature</i> or <i>MethodSignature</i>.
   * @param typeSignature whether the given signature is a <i>JavaTypeSignature</i>.
   * @return signature the given signature, remapped with the {@link SignatureVisitor} returned by
   *     {@link #createSignatureRemapper(SignatureVisitor)}.
   */
  public String mapSignature(final String signature, final boolean typeSignature) {
    if (signature == null) {
      return null;
    }
    SignatureReader signatureReader = new SignatureReader(signature);
    SignatureWriter signatureWriter = new SignatureWriter();
    SignatureVisitor signatureRemapper = createSignatureRemapper(signatureWriter);
    if (typeSignature) {
      signatureReader.acceptType(signatureRemapper);
    } else {
      signatureReader.accept(signatureRemapper);
    }
    return signatureWriter.toString();
  }

  /**
   * Constructs a new remapper for signatures. The default implementation of this method returns a
   * new {@link SignatureRemapper}.
   *
   * @param signatureVisitor the SignatureVisitor the remapper must delegate to.
   * @return the newly created remapper.
   * @deprecated use {@link #createSignatureRemapper} instead.
   */
  @Deprecated
  protected SignatureVisitor createRemappingSignatureAdapter(
      final SignatureVisitor signatureVisitor) {
    return createSignatureRemapper(signatureVisitor);
  }

  /**
   * Constructs a new remapper for signatures. The default implementation of this method returns a
   * new {@link SignatureRemapper}.
   *
   * @param signatureVisitor the SignatureVisitor the remapper must delegate to.
   * @return the newly created remapper.
   */
  protected SignatureVisitor createSignatureRemapper(final SignatureVisitor signatureVisitor) {
    return new SignatureRemapper(signatureVisitor, this);
  }

  /**
   * Maps an annotation attribute name. The default implementation of this method returns the given
   * name, unchanged. Subclasses can override.
   *
   * @param descriptor the descriptor of the annotation class.
   * @param name the name of the annotation attribute.
   * @return the new name of the annotation attribute.
   */
  public String mapAnnotationAttributeName(final String descriptor, final String name) {
    return name;
  }

  /**
   * Maps an inner class name to its new name. The default implementation of this method provides a
   * strategy that will work for inner classes produced by Java, but not necessarily other
   * languages. Subclasses can override.
   *
   * @param name the fully-qualified internal name of the inner class (see {@link
   *     Type#getInternalName()}).
   * @param ownerName the internal name of the owner class of the inner class (see {@link
   *     Type#getInternalName()}).
   * @param innerName the internal name of the inner class (see {@link Type#getInternalName()}).
   * @return the new inner name of the inner class.
   */
  public String mapInnerClassName(
      final String name, final String ownerName, final String innerName) {
    final String remappedInnerName = this.mapType(name);

    if (remappedInnerName.equals(name)) {
      return innerName;
    } else {
      int originSplit = name.lastIndexOf('/');
      int remappedSplit = remappedInnerName.lastIndexOf('/');
      if (originSplit != -1 && remappedSplit != -1) {
        if (name.substring(originSplit).equals(remappedInnerName.substring(remappedSplit))) {
          // class name not changed
          return innerName;
        }
      }
    }

    if (remappedInnerName.contains("$")) {
      int index = remappedInnerName.lastIndexOf('$') + 1;
      while (index < remappedInnerName.length()
          && Character.isDigit(remappedInnerName.charAt(index))) {
        index++;
      }
      return remappedInnerName.substring(index);
    } else {
      return innerName;
    }
  }

  /**
   * Maps a method name to its new name. The default implementation of this method returns the given
   * name, unchanged. Subclasses can override.
   *
   * @param owner the internal name of the owner class of the method (see {@link
   *     Type#getInternalName()}).
   * @param name the name of the method.
   * @param descriptor the descriptor of the method.
   * @return the new name of the method.
   */
  public String mapMethodName(final String owner, final String name, final String descriptor) {
    return name;
  }

  /**
   * Maps an invokedynamic or a constant dynamic method name to its new name. The default
   * implementation of this method returns the given name, unchanged. Subclasses can override.
   *
   * @param name the name of the method.
   * @param descriptor the descriptor of the method.
   * @return the new name of the method.
   */
  public String mapInvokeDynamicMethodName(final String name, final String descriptor) {
    return name;
  }

  /**
   * Maps a record component name to its new name. The default implementation of this method returns
   * the given name, unchanged. Subclasses can override.
   *
   * @param owner the internal name of the owner class of the field (see {@link
   *     Type#getInternalName()}).
   * @param name the name of the field.
   * @param descriptor the descriptor of the field.
   * @return the new name of the field.
   */
  public String mapRecordComponentName(
      final String owner, final String name, final String descriptor) {
    return name;
  }

  /**
   * Maps a field name to its new name. The default implementation of this method returns the given
   * name, unchanged. Subclasses can override.
   *
   * @param owner the internal name of the owner class of the field (see {@link
   *     Type#getInternalName()}).
   * @param name the name of the field.
   * @param descriptor the descriptor of the field.
   * @return the new name of the field.
   */
  public String mapFieldName(final String owner, final String name, final String descriptor) {
    return name;
  }

  /**
   * Maps a package name to its new name. The default implementation of this method returns the
   * given name, unchanged. Subclasses can override.
   *
   * @param name the fully qualified name of the package (using dots).
   * @return the new name of the package.
   */
  public String mapPackageName(final String name) {
    return name;
  }

  /**
   * Maps a module name to its new name. The default implementation of this method returns the given
   * name, unchanged. Subclasses can override.
   *
   * @param name the fully qualified name (using dots) of a module.
   * @return the new name of the module.
   */
  public String mapModuleName(final String name) {
    return name;
  }

  /**
   * Maps the internal name of a class to its new name. The default implementation of this method
   * returns the given name, unchanged. Subclasses can override.
   *
   * @param internalName the internal name of a class (see {@link Type#getInternalName()}).
   * @return the new internal name (see {@link Type#getInternalName()}).
   */
  public String map(final String internalName) {
    return internalName;
  }
}
