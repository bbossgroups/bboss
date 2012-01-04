/*
 *  Copyright 2008 biaoping.yin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.frameworkset.util.asm;

import bboss.org.objectweb.asm.ClassAdapter;
import bboss.org.objectweb.asm.ClassVisitor;
import bboss.org.objectweb.asm.MethodVisitor;
import bboss.org.objectweb.asm.Opcodes;
import static bboss.org.objectweb.asm.Opcodes.ACC_INTERFACE;
import static bboss.org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static bboss.org.objectweb.asm.Opcodes.ALOAD;
import static bboss.org.objectweb.asm.Opcodes.ARETURN;

/**
 * <p>Title: AddMethodAdaptor.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-12-29
 * @author biaoping.yin
 * @version 1.0
 */
public class AddMethodAdaptor  extends ClassAdapter {

	  private boolean isInterface;

	  private boolean isMethodPresent;

	  public AddMethodAdaptor(ClassVisitor cv) {
	    super(cv,Opcodes.ASM4);
	  }

	  @Override
	  public void visit(int version, int access, String name,
	      String signature, String superName, String[] interfaces) {
	    cv.visit(version, access, name, signature, superName, interfaces);
	    isInterface = (access & ACC_INTERFACE) != 0;
	  }

	  public MethodVisitor visitMethod(int access, String name,
	      String desc, String signature, String[] exceptions) {
	    if (name.equals("getThis") && desc.equals("()Ljava/lang/Object;")) {
	      isMethodPresent = true;
	    }
	    return cv.visitMethod(access, name, desc, signature, exceptions);
	  }

	  public void visitEnd() {
	    if (!isMethodPresent && !isInterface) {
	      MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "getThis",
	          "()Ljava/lang/Object;", null, null);
	      mv.visitCode();
	      mv.visitVarInsn(ALOAD, 0);
	      mv.visitInsn(ARETURN);
	      mv.visitMaxs(1, 1);
	      mv.visitEnd();
	    }
	    cv.visitEnd();
	  }

}
