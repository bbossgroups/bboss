package com.frameworkset.common.mbean;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.ReflectionException;

public class DynamicMBeanSupport implements DynamicMBean {
    protected MBeanInfo mbeanInfo = null;
    protected Hashtable attributes = new Hashtable();
    protected Hashtable notifications = new Hashtable();
    protected Hashtable constructors = new Hashtable();
    protected Hashtable operations = new Hashtable();

    protected String description = "Description of the MBean";

    public DynamicMBeanSupport() {
        addMBeanAttribute("description", "java.lang.String",
                          true, true, false, "Description of the MBean");
        addMBeanConstructor(this.getClass().getConstructors()[0],
                            "Default Constructor");
    }

    public Object invoke(String method, Object args[],
                         String types[]) throws MBeanException,
        ReflectionException {
        try {
            Class c = this.getClass();
            Class sig[] = null;
            if (types != null) {
                sig = new Class[types.length];
                for (int i = 0; i < types.length; i++) {
                    sig[i] = Class.forName(types[i]);
                }
            }
            Method m = c.getDeclaredMethod(method, sig);
            Object returnObject = (Object) m.invoke(this, args);
            return returnObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Object getAttribute(String name) throws MBeanException,
        AttributeNotFoundException,
        ReflectionException {
        try {
            Class c = this.getClass();
            Method m = c.getDeclaredMethod("get" + name, null);
            return m.invoke((Object)this, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setAttribute(Attribute attribute) throws MBeanException,
        AttributeNotFoundException, ReflectionException,
        InvalidAttributeValueException {
        String fname = attribute.getName();
        Object fvalue = attribute.getValue();
        try {
            Class c = this.getClass();
            String type = getType(fname, false, true);
            if (type == null) {
                throw new AttributeNotFoundException(fname);
            }
            Class[] types = {Class.forName(type)};
            Method m = c.getDeclaredMethod("set" + fname, types);
            Object[] args = {fvalue};
            m.invoke((Object)this, args);
        } catch (AttributeNotFoundException ae) {
            throw ae;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public AttributeList setAttributes(AttributeList attributes) {
        Attribute[] atts = (Attribute[]) attributes.toArray();
        AttributeList list = new AttributeList();
        for (int i = 0; i < atts.length; i++) {
            Attribute a = atts[i];
            try {
                this.setAttribute(a);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } //for
        return attributes;
    }

    public AttributeList getAttributes(String[] names) {
        AttributeList list = new AttributeList();
        for (int i = 0; i < names.length; i++) {
            try {
                list.add(new Attribute(names[i],
                                       this.getAttribute(names[i])));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public MBeanInfo getMBeanInfo() {
        try {
            buildDynamicMBeanInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mbeanInfo;
    }

    protected void addMBeanOperation(String name,
                                     String[] paramTypes,
                                     String[] paramNames, String[] paramDescs,
                                     String desc,
                                     String rtype, int type) {
        MBeanParameterInfo[] params = null;
        if (paramTypes != null) {
            params = new MBeanParameterInfo[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                params[i] = new MBeanParameterInfo(paramNames[i],
                    paramTypes[i], paramDescs[i]);
            }
        }
        operations.put(name, new MBeanOperationInfo(name, desc,
            params,
            rtype, type));
    }

    protected void addMBeanAttribute(String fname, String ftype,
                                     boolean read, boolean write, boolean is,
                                     String desc) {
        attributes.put(fname, new MBeanAttributeInfo(fname, ftype,
            desc, read, write, is));
    }

    protected void addMBeanConstructor(Constructor c,
                                       String desc) {
        this.constructors.put(c,
                              new MBeanConstructorInfo(desc, c));
    }


    private void buildDynamicMBeanInfo() throws Exception {
        MBeanOperationInfo[] ops =
            new MBeanOperationInfo[operations.size()];
        copyInto(ops, operations);

        MBeanAttributeInfo[] atts =
            new MBeanAttributeInfo[attributes.size()];
        copyInto(atts, attributes);

        MBeanConstructorInfo[] cons =
            new MBeanConstructorInfo[constructors.size()];
        copyInto(cons, constructors);

        mbeanInfo = new MBeanInfo(
            this.getClass().getName(), description,
            atts, cons, ops, null);
    }


    private void copyInto(Object[] array, Hashtable table) {
        Vector temp = new Vector(table.values());
        temp.copyInto(array);
    }

    private String getType(String attName,
                           boolean read, boolean write) {
        boolean allowed = true;
        if (attributes.containsKey(attName)) {
            MBeanAttributeInfo temp = (MBeanAttributeInfo)
                                      attributes.get(attName);
            if (read) {
                if (!temp.isReadable()) {
                    allowed = false;
                }
            }
            if (write) {
                if (!temp.isWritable()) {
                    allowed = false;
                }
            }
            if (!allowed) {
                return null;
            } else {
                return temp.getType();
            }
        } else {
            return null;
        }
    }
} //class
