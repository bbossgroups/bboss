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

import java.util.List;
import bboss.org.objectweb.asm.Opcodes;
import bboss.org.objectweb.asm.Type;

/**
 * An extended {@link BasicVerifier} that performs more precise verifications. This verifier
 * computes exact class types, instead of using a single "object reference" type (as done in {@link
 * BasicVerifier}).
 *
 * @author Eric Bruneton
 * @author Bing Ran
 */
public class SimpleVerifier extends BasicVerifier {

  /** The type of the Object class. */
  private static final Type OBJECT_TYPE = Type.getObjectType("java/lang/Object");

  /** The type of the class that is verified. */
  private final Type currentClass;

  /** The type of the super class of the class that is verified. */
  private final Type currentSuperClass;

  /** The types of the interfaces directly implemented by the class that is verified. */
  private final List<Type> currentClassInterfaces;

  /** Whether the class that is verified is an interface. */
  private final boolean isInterface;

  /** The loader to use to load the referenced classes. */
  private ClassLoader loader = getClass().getClassLoader();

  /**
   * Constructs a new {@link SimpleVerifier}. <i>Subclasses must not use this constructor</i>.
   * Instead, they must use the {@link #SimpleVerifier(int, Type, Type, List, boolean)} version.
   */
  public SimpleVerifier() {
    this(null, null, false);
  }

  /**
   * Constructs a new {@link SimpleVerifier} to verify a specific class. This class will not be
   * loaded into the JVM since it may be incorrect. <i>Subclasses must not use this constructor</i>.
   * Instead, they must use the {@link #SimpleVerifier(int, Type, Type, List, boolean)} version.
   *
   * @param currentClass the type of the class to be verified.
   * @param currentSuperClass the type of the super class of the class to be verified.
   * @param isInterface whether the class to be verifier is an interface.
   */
  public SimpleVerifier(
      final Type currentClass, final Type currentSuperClass, final boolean isInterface) {
    this(currentClass, currentSuperClass, null, isInterface);
  }

  /**
   * Constructs a new {@link SimpleVerifier} to verify a specific class. This class will not be
   * loaded into the JVM since it may be incorrect. <i>Subclasses must not use this constructor</i>.
   * Instead, they must use the {@link #SimpleVerifier(int, Type, Type, List, boolean)} version.
   *
   * @param currentClass the type of the class to be verified.
   * @param currentSuperClass the type of the super class of the class to be verified.
   * @param currentClassInterfaces the types of the interfaces directly implemented by the class to
   *     be verified.
   * @param isInterface whether the class to be verifier is an interface.
   */
  public SimpleVerifier(
      final Type currentClass,
      final Type currentSuperClass,
      final List<Type> currentClassInterfaces,
      final boolean isInterface) {
    this(
        /* latest api = */ ASM9,
        currentClass,
        currentSuperClass,
        currentClassInterfaces,
        isInterface);
    if (getClass() != SimpleVerifier.class) {
      throw new IllegalStateException();
    }
  }

  /**
   * Constructs a new {@link SimpleVerifier} to verify a specific class. This class will not be
   * loaded into the JVM since it may be incorrect.
   *
   * @param api the ASM API version supported by this verifier. Must be one of the {@code
   *     ASM}<i>x</i> values in {@link Opcodes}.
   * @param currentClass the type of the class to be verified.
   * @param currentSuperClass the type of the super class of the class to be verified.
   * @param currentClassInterfaces the types of the interfaces directly implemented by the class to
   *     be verified.
   * @param isInterface whether the class to be verifier is an interface.
   */
  protected SimpleVerifier(
      final int api,
      final Type currentClass,
      final Type currentSuperClass,
      final List<Type> currentClassInterfaces,
      final boolean isInterface) {
    super(api);
    this.currentClass = currentClass;
    this.currentSuperClass = currentSuperClass;
    this.currentClassInterfaces = currentClassInterfaces;
    this.isInterface = isInterface;
  }

  /**
   * Sets the <code>ClassLoader</code> to be used in {@link #getClass}.
   *
   * @param loader the <code>ClassLoader</code> to use.
   */
  public void setClassLoader(final ClassLoader loader) {
    this.loader = loader;
  }

  @Override
  public BasicValue newValue(final Type type) {
    if (type == null) {
      return BasicValue.UNINITIALIZED_VALUE;
    }

    boolean isArray = type.getSort() == Type.ARRAY;
    if (isArray) {
      switch (type.getElementType().getSort()) {
        case Type.BOOLEAN:
        case Type.CHAR:
        case Type.BYTE:
        case Type.SHORT:
          return new BasicValue(type);
        default:
          break;
      }
    }

    BasicValue value = super.newValue(type);
    if (BasicValue.REFERENCE_VALUE.equals(value)) {
      if (isArray) {
        value = newValue(type.getElementType());
        StringBuilder descriptor = new StringBuilder();
        for (int i = 0; i < type.getDimensions(); ++i) {
          descriptor.append('[');
        }
        descriptor.append(value.getType().getDescriptor());
        value = new BasicValue(Type.getType(descriptor.toString()));
      } else {
        value = new BasicValue(type);
      }
    }
    return value;
  }

  @Override
  protected boolean isArrayValue(final BasicValue value) {
    Type type = value.getType();
    return type != null && (type.getSort() == Type.ARRAY || type.equals(NULL_TYPE));
  }

  @Override
  protected BasicValue getElementValue(final BasicValue objectArrayValue) throws AnalyzerException {
    Type arrayType = objectArrayValue.getType();
    if (arrayType != null) {
      if (arrayType.getSort() == Type.ARRAY) {
        return newValue(Type.getType(arrayType.getDescriptor().substring(1)));
      } else if (arrayType.equals(NULL_TYPE)) {
        return objectArrayValue;
      }
    }
    throw new AssertionError();
  }

  @Override
  protected boolean isSubTypeOf(final BasicValue value, final BasicValue expected) {
    Type type = value.getType();
    Type expectedType = expected.getType();
    // Null types correspond to BasicValue.UNINITIALIZED_VALUE.
    if (type == null || expectedType == null) {
      return type == null && expectedType == null;
    }
    if (type.equals(expectedType)) {
      return true;
    }
    switch (expectedType.getSort()) {
      case Type.INT:
      case Type.FLOAT:
      case Type.LONG:
      case Type.DOUBLE:
        return false;
      case Type.ARRAY:
      case Type.OBJECT:
        if (type.equals(NULL_TYPE)) {
          return true;
        }
        // Convert 'type' to its element type and array dimension. Arrays of primitive values are
        // seen as Object arrays with one dimension less. Hence the element type is always of
        // Type.OBJECT sort.
        int dim = 0;
        if (type.getSort() == Type.ARRAY) {
          dim = type.getDimensions();
          type = type.getElementType();
          if (type.getSort() != Type.OBJECT) {
            dim = dim - 1;
            type = OBJECT_TYPE;
          }
        }
        // Do the same for expectedType.
        int expectedDim = 0;
        if (expectedType.getSort() == Type.ARRAY) {
          expectedDim = expectedType.getDimensions();
          expectedType = expectedType.getElementType();
          if (expectedType.getSort() != Type.OBJECT) {
            // If the expected type is an array of some primitive type, it does not have any subtype
            // other than itself. And 'type' is different by hypothesis.
            return false;
          }
        }
        // A type with less dimensions than expected can't be a subtype of the expected type.
        if (dim < expectedDim) {
          return false;
        }
        // A type with more dimensions than expected is seen as an array with the expected
        // dimensions but with an Object element type. For instance an array of arrays of Integer is
        // seen as an array of Object if the expected type is an array of Serializable.
        if (dim > expectedDim) {
          type = OBJECT_TYPE;
        }
        // type and expectedType have a Type.OBJECT sort by construction (see above),
        // as expected by isAssignableFrom.
        if (isAssignableFrom(expectedType, type)) {
          return true;
        }
        if (getClass(expectedType).isInterface()) {
          // The merge of class or interface types can only yield class types (because it is not
          // possible in general to find an unambiguous common super interface, due to multiple
          // inheritance). Because of this limitation, we need to relax the subtyping check here
          // if 'value' is an interface.
          return Object.class.isAssignableFrom(getClass(type));
        } else {
          return false;
        }
      default:
        throw new AssertionError();
    }
  }

  @Override
  public BasicValue merge(final BasicValue value1, final BasicValue value2) {
    Type type1 = value1.getType();
    Type type2 = value2.getType();
    // Null types correspond to BasicValue.UNINITIALIZED_VALUE.
    if (type1 == null || type2 == null) {
      return BasicValue.UNINITIALIZED_VALUE;
    }
    if (type1.equals(type2)) {
      return value1;
    }
    // The merge of a primitive type with a different type is the type of uninitialized values.
    if (type1.getSort() != Type.OBJECT && type1.getSort() != Type.ARRAY) {
      return BasicValue.UNINITIALIZED_VALUE;
    }
    if (type2.getSort() != Type.OBJECT && type2.getSort() != Type.ARRAY) {
      return BasicValue.UNINITIALIZED_VALUE;
    }
    // Special case for the type of the "null" literal.
    if (type1.equals(NULL_TYPE)) {
      return value2;
    }
    if (type2.equals(NULL_TYPE)) {
      return value1;
    }
    // Convert type1 to its element type and array dimension. Arrays of primitive values are seen as
    // Object arrays with one dimension less. Hence the element type is always of Type.OBJECT sort.
    int dim1 = 0;
    if (type1.getSort() == Type.ARRAY) {
      dim1 = type1.getDimensions();
      type1 = type1.getElementType();
      if (type1.getSort() != Type.OBJECT) {
        dim1 = dim1 - 1;
        type1 = OBJECT_TYPE;
      }
    }
    // Do the same for type2.
    int dim2 = 0;
    if (type2.getSort() == Type.ARRAY) {
      dim2 = type2.getDimensions();
      type2 = type2.getElementType();
      if (type2.getSort() != Type.OBJECT) {
        dim2 = dim2 - 1;
        type2 = OBJECT_TYPE;
      }
    }
    // The merge of array types of different dimensions is an Object array type.
    if (dim1 != dim2) {
      return newArrayValue(OBJECT_TYPE, Math.min(dim1, dim2));
    }
    // Type1 and type2 have a Type.OBJECT sort by construction (see above),
    // as expected by isAssignableFrom.
    if (isAssignableFrom(type1, type2)) {
      return newArrayValue(type1, dim1);
    }
    if (isAssignableFrom(type2, type1)) {
      return newArrayValue(type2, dim1);
    }
    if (!isInterface(type1)) {
      while (!type1.equals(OBJECT_TYPE)) {
        type1 = getSuperClass(type1);
        if (isAssignableFrom(type1, type2)) {
          return newArrayValue(type1, dim1);
        }
      }
    }
    return newArrayValue(OBJECT_TYPE, dim1);
  }

  private BasicValue newArrayValue(final Type type, final int dimensions) {
    if (dimensions == 0) {
      return newValue(type);
    } else {
      StringBuilder descriptor = new StringBuilder();
      for (int i = 0; i < dimensions; ++i) {
        descriptor.append('[');
      }
      descriptor.append(type.getDescriptor());
      return newValue(Type.getType(descriptor.toString()));
    }
  }

  /**
   * Returns whether the given type corresponds to the type of an interface. The default
   * implementation of this method loads the class and uses the reflection API to return its result
   * (unless the given type corresponds to the class being verified).
   *
   * @param type an object reference type (i.e., with Type.OBJECT sort).
   * @return whether 'type' corresponds to an interface.
   */
  protected boolean isInterface(final Type type) {
    if (currentClass != null && currentClass.equals(type)) {
      return isInterface;
    }
    return getClass(type).isInterface();
  }

  /**
   * Returns the type corresponding to the super class of the given type. The default implementation
   * of this method loads the class and uses the reflection API to return its result (unless the
   * given type corresponds to the class being verified).
   *
   * @param type an object reference type (i.e., with Type.OBJECT sort).
   * @return the type corresponding to the super class of 'type', or {@literal null} if 'type' is
   *     the type of the Object class.
   */
  protected Type getSuperClass(final Type type) {
    if (currentClass != null && currentClass.equals(type)) {
      return currentSuperClass;
    }
    Class<?> superClass = getClass(type).getSuperclass();
    return superClass == null ? null : Type.getType(superClass);
  }

  /**
   * Returns whether the class corresponding to the first argument is either the same as, or is a
   * superclass or superinterface of the class corresponding to the second argument. The default
   * implementation of this method loads the classes and uses the reflection API to return its
   * result (unless the result can be computed from the class being verified, and the types of its
   * super classes and implemented interfaces).
   *
   * @param type1 an object reference type (i.e., with Type.OBJECT sort).
   * @param type2 another object reference type (i.e., with Type.OBJECT sort).
   * @return whether the class corresponding to 'type1' is either the same as, or is a superclass or
   *     superinterface of the class corresponding to 'type2'.
   */
  protected boolean isAssignableFrom(final Type type1, final Type type2) {
    if (type1.equals(type2)) {
      return true;
    }
    if (currentClass != null && currentClass.equals(type1)) {
      Type superType2 = getSuperClass(type2);
      if (superType2 == null) {
        return false;
      }
      if (isInterface) {
        // This should always be true, given the preconditions of this method, but is kept for
        // backward compatibility.
        return type2.getSort() == Type.OBJECT || type2.getSort() == Type.ARRAY;
      }
      return isAssignableFrom(type1, superType2);
    }
    if (currentClass != null && currentClass.equals(type2)) {
      if (isAssignableFrom(type1, currentSuperClass)) {
        return true;
      }
      if (currentClassInterfaces != null) {
        for (Type currentClassInterface : currentClassInterfaces) {
          if (isAssignableFrom(type1, currentClassInterface)) {
            return true;
          }
        }
      }
      return false;
    }
    return getClass(type1).isAssignableFrom(getClass(type2));
  }

  /**
   * Loads the class corresponding to the given type. The class is loaded with the class loader
   * specified with {@link #setClassLoader}, or with the class loader of this class if no class
   * loader was specified.
   *
   * @param type an object reference type (i.e., with Type.OBJECT sort).
   * @return the class corresponding to 'type'.
   */
  protected Class<?> getClass(final Type type) {
    try {
      if (type.getSort() == Type.ARRAY) {
        // This should never happen, given the preconditions of this method, but is kept for
        // backward compatibility.
        return Class.forName(type.getDescriptor().replace('/', '.'), false, loader);
      }
      return Class.forName(type.getClassName(), false, loader);
    } catch (ClassNotFoundException e) {
      throw new TypeNotPresentException(e.toString(), e);
    }
  }
}
