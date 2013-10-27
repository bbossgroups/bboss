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

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;
import org.frameworkset.util.ClassUtil.PropertieDescription;

import bboss.org.objectweb.asm.ClassReader;
import bboss.org.objectweb.asm.ClassWriter;



/**
 * <p>Title: AsmUtil.java</p> 
 * <p>Description: </p>
 * <p>bboss workgroup</p>
 * <p>Copyright (c) 2008</p>
 * @Date 2011-12-29
 * @author biaoping.yin
 * @version 1.0
 */
public class AsmUtil {
	private static final ASMClassLoader classLoader = new ASMClassLoader();
	private static final Logger log = Logger.getLogger(AsmUtil.class);
	
	public static final String bboss_field_getMethod_prex = "___bboss_pull";
	public static final String bboss_field_setMethod_prex = "___bboss_put";
	
//	public static class MethodNode
//	{
//		String field
//	}
//	public static Method addFieldWriteMethodToClass(Class clazz,Field field) throws IOException
//	{
//		ClassWriter cw = new ClassWriter(new ClassReader(clazz.getCanonicalName()),0);
//	    CheckClassAdapter ca = new CheckClassAdapter(cw);
//	    AddMethodAdaptor am = new AddMethodAdaptor(ca);
//	    
//	}
	
	public static Class addGETSETMethodForClass(List<PropertieDescription> propertieDescriptionies,Class beanType)
	{
		String className = beanType.getName();
		try {
//			String className = beanType.getName();
			ClassReader cr = new ClassReader(className);
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			cr.accept(new ASMPUTGETFieldAdapter(cw,beanType,propertieDescriptionies),0);
			beanType = classLoader.defineClass(className, cw.toByteArray());		
			return beanType;
//			Method temp = beanType.getMethod(AsmUtil.bboss_field_getMethod_prex + "Age");
			
		} catch (Exception e) {
			log.error("Add field's GET/SET Method For ["+className+"] failed:",e);
			return beanType;
		}
	}
	
	
	
//	public static void main(String[] args) throws IOException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException
//	{
////		ClassReader reader = new ClassReader("java.lang.String");
////		reader.accept(new MyClassVisitor(), 0);
//		
//		Foo foo = new Foo();
//		foo.execute("duoduo","中国");
//		
//		ClassReader cr = new ClassReader(Foo.class.getName());
//		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//		cr.accept(new ClassAdapter(cw,Opcodes.ASM4){
//
//			@Override
//			public void visit(int version, int access, String name,
//					String signature, String superName, String[] interfaces) {
//				// TODO Auto-generated method stub
//				super.visit(version, access, name, signature, superName, interfaces);
//			}
//
//			@Override
//			public MethodVisitor visitMethod(int access, String name,
//					String desc, String signature, String[] exceptions) {
//				// TODO Auto-generated method stub
//				if(name.equals("execute"))
//				{
//					return cv.visitMethod(access, name+"$bboss1", desc, signature, exceptions);
//				}
//				else
//				{
//					return cv.visitMethod(access, name, desc, signature, exceptions);
//				}
//			}
//
//			@Override
//			public FieldVisitor visitField(int access, String name,
//					String desc, String signature, Object value) {
//				if(name.equals("age"))
//				{
//					/**
//					 * //我们接着需要增加一个execute方法
//         MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "execute",
//                 "()V", null,
//                 null);
//         //开始增加代码
//         mv.visitCode();
//         //接下来，我们需要把新的execute方法的内容，增加到这个方法中
//         mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//         mv.visitLdcInsn("Before execute");
//         mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V"); 
//         mv.visitVarInsn(Opcodes.ALOAD, 0);
//         mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/vanadies/bytecode/example/asm3/ClassWriterAopExample$Foo$1", "execute$1", "()V");
//         mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
//         mv.visitLdcInsn("End execute");
//         mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
//         mv.visitInsn(Opcodes.RETURN);
//         mv.visitMaxs(0, 0); //这个地方，最大的操作数栈和最大的本地变量的空间，是自动计算的，是因为构造ClassWriter的时候使用了ClassWriter.COMPUTE_MAXS
//         mv.visitEnd();
//         //到这里，就完成了execute方法的添加。
//
//					 */
//					String className = Foo.class.getName();
//					className = className.replace(".", "/");
//					//我们接着需要增加一个execute方法
//					String getMd = new StringBuffer(6).append("get")
//		            .append(Character.toTitleCase(name.charAt(0)))
//		            .append(name.substring(1))
//		            .toString();
//					
//					String setMd = new StringBuffer(6).append("set")
//				            .append(Character.toTitleCase(name.charAt(0)))
//				            .append(name.substring(1))
//				            .toString();
//					
//			         MethodVisitor mv = cv.visitMethod(Opcodes.ACC_PUBLIC, getMd,
//			                 "()" + desc, null,
//			                 null);
//			        
////			         mv = cw.visitMethod(ACC_PUBLIC, getMd, "()I", null, null);
//						mv.visitCode();
//						mv.visitVarInsn(Opcodes.ALOAD, 0);
//						mv.visitFieldInsn(Opcodes.GETFIELD, className, name, desc);
//						mv.visitInsn(Opcodes.ARETURN);
//						mv.visitMaxs(1, 1);
//						// mw.visitInsn(RETURN);
//						// this code uses a maximum of two stack elements and two local
//						// variables
//						mv.visitEnd();
//						mv = cv.visitMethod(Opcodes.ACC_PUBLIC, setMd,
//				                 "("+desc+")V", null,
//				                 null);
//						mv.visitCode();
//						mv.visitVarInsn(Opcodes.ALOAD, 0);
//						mv.visitVarInsn(Opcodes.ALOAD, 1);
//						mv.visitFieldInsn(Opcodes.PUTFIELD, className, name, desc);
//						mv.visitInsn(Opcodes.RETURN);
//						mv.visitMaxs(2, 2);
//						// mw.visitInsn(RETURN);
//						// this code uses a maximum of two stack elements and two local
//						// variables
//						mv.visitEnd();
//			           
//			         //到这里，就完成了execute方法的添加。
//				}
//				return super.visitField(access, name, desc, signature, value);
//			}
//
//			
//			
//		},0);
//		TestClassLoader LOADER = new TestClassLoader();
//		Class c = LOADER.defineClass(Foo.class.getName(), cw.toByteArray());
////		Class c = Class.forName(Foo.class.getName() +"_bboss1");
//		Method m = c.getMethod("execute$bboss1",String.class,String.class);
//		Method gage = c.getMethod("getAge");
//		Method sage = c.getMethod("setAge",String.class);
//		Object o = c.newInstance();
//		sage.invoke(o, "18");
//		m.invoke(c.newInstance(),"duoduo","中国");
//		System.out.println(gage.invoke(o));
//		
//	}
	
	
	
//	static class TestClassLoader extends ClassLoader {
//
//	    public Class defineClass(String name, byte[] b) {
//	      return defineClass(name, b, 0, b.length);
//	    }
//	  }
//	
//	public static class Foo
//	{
//		private String age;
//		public String execute(String name,String country)
//		{
//			System.out.println("helloword:" + name);
//			return "helloword:" + name;
//		}
//	}
//	
//	public static class MyClassVisitor extends ClassVisitor
//	{
//
//		public MyClassVisitor(int api) {
//			super(api);
//			// TODO Auto-generated constructor stub
//		}
//
//		public void visit(int arg0, int arg1, String arg2, String arg3,
//				String arg4, String[] arg5) {
//			System.out.println(arg2);
//			System.out.println(arg3);
//			System.out.println(arg4);
//		}
//
//		public AnnotationVisitor visitAnnotation(String arg0, boolean arg1) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public void visitAttribute(Attribute arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		public void visitEnd() {
//			// TODO Auto-generated method stub
//			
//		}
//
//		public FieldVisitor visitField(int arg0, String arg1, String arg2,
//				String arg3, Object arg4) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public void visitInnerClass(String arg0, String arg1, String arg2,
//				int arg3) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		public MethodVisitor visitMethod(int arg0, String arg1, String arg2,
//				String arg3, String[] arg4) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//
//		public void visitOuterClass(String arg0, String arg1, String arg2) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		public void visitSource(String arg0, String arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
}
