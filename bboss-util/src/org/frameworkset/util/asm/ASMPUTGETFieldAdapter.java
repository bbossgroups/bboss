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

import java.util.List;

import org.frameworkset.util.ClassUtil.PropertieDescription;

import bboss.org.objectweb.asm.ClassAdapter;
import bboss.org.objectweb.asm.ClassVisitor;
import bboss.org.objectweb.asm.FieldVisitor;
import bboss.org.objectweb.asm.MethodVisitor;
import bboss.org.objectweb.asm.Opcodes;

/**
 * <p>Title: ASMClassAdapter.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-12-31
 * @author biaoping.yin
 * @version 1.0
 */
public class ASMPUTGETFieldAdapter extends ClassAdapter{
	private Class beanType;
	private String beanTypeName;
	private List<PropertieDescription> propertieDescriptionies;
	
	public ASMPUTGETFieldAdapter(Class beanType,List<PropertieDescription> propertieDescriptionies) {
		super(Opcodes.ASM4);
		init( beanType, propertieDescriptionies);
		
	}

	public ASMPUTGETFieldAdapter(ClassVisitor cv,Class beanType,List<PropertieDescription> propertieDescriptionies) {
		super(cv, Opcodes.ASM4);
		init( beanType, propertieDescriptionies);
	}
	
	private void init(Class beanType,List<PropertieDescription> propertieDescriptionies)
	{
		this.beanType = beanType;
		this.beanTypeName = this.beanType.getName();
		beanTypeName = beanTypeName.replace(".", "/");
		this.propertieDescriptionies = propertieDescriptionies;
	}
	
	private PropertieDescription contain(String name)
	{
		for(int i = 0; i < this.propertieDescriptionies.size(); i ++)
		{
			PropertieDescription p = this.propertieDescriptionies.get(i);
			if(p.getName().equals(name))
			{
				return p;
			}
		}
		return null;
	}
	
	@Override
	public FieldVisitor visitField(int access, String name,
			String desc, String signature, Object value) {
		PropertieDescription pro = contain( name);
		if(pro != null)
		{
			/**
			 * //我们接着需要增加一个execute方法
 MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "execute",
         "()V", null,
         null);
 //开始增加代码
 mv.visitCode();
 //接下来，我们需要把新的execute方法的内容，增加到这个方法中
 mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
 mv.visitLdcInsn("Before execute");
 mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"); 
 mv.visitVarInsn(Opcodes.ALOAD, 0);
 mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/vanadies/bytecode/example/asm3/ClassWriterAopExample$Foo$1", "execute$1", "()V");
 mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
 mv.visitLdcInsn("End execute");
 mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
 mv.visitInsn(Opcodes.RETURN);
 mv.visitMaxs(0, 0); //这个地方，最大的操作数栈和最大的本地变量的空间，是自动计算的，是因为构造ClassWriter的时候使用了ClassWriter.COMPUTE_MAXS
 mv.visitEnd();
 //到这里，就完成了execute方法的添加。

			 */
			 MethodVisitor mv = null;
			//我们接着需要增加一个execute方法
			if(pro.getReadMethod() == null)
			{
				String getMd = new StringBuffer(6).append(AsmUtil.bboss_field_getMethod_prex)
	            .append(Character.toTitleCase(name.charAt(0)))
	            .append(name.substring(1))
	            .toString();
				  mv = cv.visitMethod(Opcodes.ACC_PUBLIC, getMd,
			                 "()" + desc, null,
			                 null);
			        
//			         mv = cw.visitMethod(ACC_PUBLIC, getMd, "()I", null, null);
						mv.visitCode();
						mv.visitVarInsn(Opcodes.ALOAD, 0);
						mv.visitFieldInsn(Opcodes.GETFIELD, beanTypeName, name, desc);
						mv.visitInsn(Opcodes.ARETURN);
						mv.visitMaxs(1, 1);
						// mw.visitInsn(RETURN);
						// this code uses a maximum of two stack elements and two local
						// variables
						mv.visitEnd();
			}
			
			if(pro.getWriteMethod() == null)
			{
				String setMd = new StringBuffer(6).append(AsmUtil.bboss_field_setMethod_prex)
		            .append(Character.toTitleCase(name.charAt(0)))
		            .append(name.substring(1))
		            .toString();
			
	       
				mv = cv.visitMethod(Opcodes.ACC_PUBLIC, setMd,
		                 "("+desc+")V", null,
		                 null);
				mv.visitCode();
				mv.visitVarInsn(Opcodes.ALOAD, 0);
				mv.visitVarInsn(Opcodes.ALOAD, 1);
				mv.visitFieldInsn(Opcodes.PUTFIELD, beanTypeName, name, desc);
				mv.visitInsn(Opcodes.RETURN);
				mv.visitMaxs(2, 2);
				// mw.visitInsn(RETURN);
				// this code uses a maximum of two stack elements and two local
				// variables
				mv.visitEnd();
			}
	           
	         //到这里，就完成了execute方法的添加。
		}
		return super.visitField(access, name, desc, signature, value);
	}
}
