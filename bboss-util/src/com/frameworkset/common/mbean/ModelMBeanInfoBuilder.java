package com.frameworkset.common.mbean;

/**
 * <p>Title: ModelMBeanInfoBuilder</p>
 *
 * <p>Description: 构建manager resource ModelMBeanInfo
 *   if(name.equalsIgnoreCase("descriptorType"))
            isValid = value != null && (value.equals("MBean") || value.equals("attribute") || value.equals("operation") || value.equals("notification"));
        else
        if(name.equalsIgnoreCase("role"))
            isValid = value != null && (value.equals("constructor") || value.equals("operation") || value.equals("getter") || value.equals("setter"));
        else
        if(name.equalsIgnoreCase("persistPolicy"))
            isValid = value != null && (value.equals("Never") || value.equals("OnTimer") || value.equals("OnUpdate") || value.equals("NoMoreOftenThan"));

 *
 * </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: </p>
 *
 * @author biaoping.yin
 * @version 1.0
 */
import java.lang.reflect.Constructor;
import java.util.Hashtable;
import java.util.Vector;

import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.DescriptorSupport;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanInfoSupport;
import javax.management.modelmbean.ModelMBeanNotificationInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;

public class ModelMBeanInfoBuilder {
    protected Hashtable attributes = new Hashtable();
    protected Hashtable notifications = new Hashtable();
    protected Hashtable constructors = new Hashtable();
    protected Hashtable operations = new Hashtable();
    public ModelMBeanInfoBuilder() {
    }

    public void addModelMBeanMethod(String name,
                                    String[] paramTypes,
                                    String[] paramNames,
                                    String[] paramDescs,
                                    String description, String rtype,
                                    int type, Descriptor desc) {
        MBeanParameterInfo[] params = null;
        if (paramTypes != null) {
            params = new MBeanParameterInfo[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                params[i] = new MBeanParameterInfo(paramNames[i],
                    paramTypes[i], paramDescs[i]);
            }
        }
        operations.put(name + paramTypes,
                       new ModelMBeanOperationInfo(name, description,
            params, rtype, type, desc));
    }

    public void addModelMBeanNotification(String[] type,
                                          String className,
                                          String description, Descriptor desc) {
        notifications.put(className,
                          new ModelMBeanNotificationInfo(type,
            className, description, desc));
    }

    public void addModelMBeanAttribute(String fname, String ftype,
                                       boolean read, boolean write,
                                       boolean is,
                                       String description, Descriptor desc) {
        attributes.put(fname, new ModelMBeanAttributeInfo(fname,
            ftype,
            description, read, write, is, desc));
    }

    public void addModelMBeanConstructor(Constructor c,
                                         String description,
                                         Descriptor desc) {
        this.constructors.put(c,
                              new ModelMBeanConstructorInfo(description,
            c, desc));
    }

    public void addModelMBeanConstructor(Constructor c,
                                         String description) {
        this.constructors.put(c,
                              new ModelMBeanConstructorInfo(description,
            c));
    }


    public ModelMBeanInfo buildModelMBeanInfo(Descriptor desc) throws Exception {
        ModelMBeanOperationInfo[] ops =
            new ModelMBeanOperationInfo[operations.size()];
        copyInto(ops, operations);
        ModelMBeanAttributeInfo[] atts =
            new ModelMBeanAttributeInfo[attributes.size()];
        copyInto(atts, attributes);
        ModelMBeanConstructorInfo[] cons =
            new ModelMBeanConstructorInfo[constructors.size()];
        copyInto(cons, constructors);
        ModelMBeanNotificationInfo[] notifs =
            new ModelMBeanNotificationInfo[notifications.size()];
        copyInto(notifs, notifications);
        System.out.println(ops);
        return new ModelMBeanInfoSupport(
            "javax.management.modelmbean.ModelMBeanInfo",
            "description",
            atts,
            cons,
            ops,
            notifs, desc);
    }

    public Descriptor buildAttributeDescriptor(String name,
                                               String displayName,
                                               String persistPolicy,
                                               String persistPeriod,
                                               Object defaultValue,
                                               String getter, String setter,
                                               String currency) {
        Descriptor desc = new DescriptorSupport();
        if (name != null) {
            desc.setField("name", name);
        }
        desc.setField("descriptorType", "attribute");
        if (displayName != null) {
            desc.setField("displayName", displayName);
        }

        if (getter != null) {
            desc.setField("getMethod", getter);
        }
        if (setter != null) {
            desc.setField("setMethod", setter);
        }
        if (currency != null) {
            desc.setField("currencyTimeLimit", currency);
        }
        if (persistPolicy != null) {
            desc.setField("persistPolicy", persistPolicy);
        }
        if (persistPeriod != null) {
            desc.setField("persistPeriod", persistPeriod);
        }
        if (defaultValue != null) {
            desc.setField("default", defaultValue);
        }
        return desc;
    }

    public Descriptor buildOperationDescriptor(String name,
                                               String displayName, String role,
                                               Object targetObject,
                                               Object targetType,
                                               String ownerClass,
                                               String currency) {
        Descriptor desc = new DescriptorSupport();
        if (name != null) {
            desc.setField("name", name);
        }
        desc.setField("descriptorType", "operation");
        if (displayName != null) {
            desc.setField("displayName", displayName);
        }
        if (role != null) {
            desc.setField("role", role);
        }
        if (targetObject != null) {
            desc.setField("targetObject", targetObject);
        }
        if (targetType != null) {
            desc.setField("targetType", targetType);
        }
        if (ownerClass != null) {
            desc.setField("class", ownerClass);
        }
        if (currency != null) {
            desc.setField("currencyTimeLimit", currency);
        }
        return desc;
    }

    /**
     *
     * @param name String
     * @param displayName String
     * @param persistPolicy String
     * @param persistPeriod String
     * @param persistLocation String
     * @param persistName String always
     * @param log String
     * @param logFile String
     * @return Descriptor
     */
    public Descriptor buildMBeanDescriptor(String name,
                                           String displayName,
                                           String persistPolicy,
                                           String persistPeriod,
                                           String persistLocation,
                                           String persistName,
                                           String log, String logFile) {
        Descriptor desc = new DescriptorSupport();
        if (name != null) {
            desc.setField("name", name);
        }

        //tomcat自带的jmx版本比较低
        desc.setField("descriptorType", "MBean");
        if (displayName != null) {
            desc.setField("displayName", displayName);
        }
        if (persistLocation != null) {
            desc.setField("persistLocation",
                          persistLocation);
        }
        if (persistName != null) {
            desc.setField("persistName", persistName);
        }
        if (log != null) {
            desc.setField("log", log);
        }
        if (persistPolicy != null) {
            desc.setField("persistPolicy", persistPolicy);
        }
        if (persistPeriod != null) {
            desc.setField("persistPeriod", persistPeriod);
        }
        if (logFile != null) {
            desc.setField("logFile", logFile);
        }
        return desc;
    }

    private void copyInto(Object[] array, Hashtable table) {
        Vector temp = new Vector(table.values());
        temp.copyInto(array);
    }
}
