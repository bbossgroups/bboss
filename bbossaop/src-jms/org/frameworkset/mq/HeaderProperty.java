package org.frameworkset.mq;

/**
 * 
 * <p>
 * Title: HeaderProperty.java
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * bboss workgroup
 * </p>
 * <p>
 * Copyright (c) 2007
 * </p>
 * 
 * @Date 2009-8-8 ÉÏÎç11:56:42
 * @author biaoping.yin
 * @version 1.0
 */
public class HeaderProperty implements java.io.Serializable
{
    private String SCHEDULE_ID; // VARCHAR(50) not null,

    private int task_id;
    
    public static final String protype_string = "string";
    public static final String protype_int = "int";
    public static final String protype_long = "long";
    public static final String protype_float = "float";
    public static final String protype_double = "double";
    public static final String protype_short = "short";
    public static final String protype_byte = "byte";
    public static final String protype_boolean = "boolean";
    public static final String protype_object = "Object";
    
    private Object object = null;
    
    public Object getObject()
    {
        return object;
    }
    public HeaderProperty()
    {
        
    }
    public HeaderProperty(String name,Object value)
    {
        this.PRO_NAME = name;
        this.object = value;
        if(value != null)
        {
            if(value instanceof String)
            {
                this.VALUE_string = (String)value;
                this.PRO_TYPE = HeaderProperty.protype_string;
            }
            else if(value instanceof Integer)
            {
                this.VALUE_int = ((Integer)value).intValue();
                this.PRO_TYPE = HeaderProperty.protype_int;
            }
            else if(value instanceof Short)
            {
                this.VALUE_short = ((Short)value).shortValue();
                this.PRO_TYPE = HeaderProperty.protype_short;
            }
            else if(value instanceof Long)
            {
                this.VALUE_long = ((Long)value).longValue();
                this.PRO_TYPE = HeaderProperty.protype_long;
            }
            else if(value instanceof Double)
            {
                this.VALUE_double = ((Double)value).doubleValue();
                this.PRO_TYPE = HeaderProperty.protype_double;
            }
            else if(value instanceof Float)
            {
                this.VALUE_float = ((Float)value).floatValue();
                this.PRO_TYPE = HeaderProperty.protype_float;
            }
            else if(value instanceof Byte)
            {
                this.VALUE_byte = ((Byte)value).byteValue();
                this.PRO_TYPE = HeaderProperty.protype_byte;
            }
            else if(value instanceof Boolean)
            {
                this.VALUE_BOOLEAN = ((Boolean)value).booleanValue();
                this.PRO_TYPE = HeaderProperty.protype_boolean;
            }
            else if(value instanceof Object)
            {
                this.VALUE_OBJECT = value;
                this.PRO_TYPE = HeaderProperty.protype_object;
            }
        }
        else
        {
            this.VALUE_OBJECT = value;
            this.PRO_TYPE = HeaderProperty.protype_object;
        }
    }

    public int getTask_id()
    {

        return task_id;
    }

    public void setTask_id(int task_id)
    {

        this.task_id = task_id;
    }

    public String getSCHEDULE_ID()
    {
        return SCHEDULE_ID;
    }

    public void setSCHEDULE_ID(String schedule_id)
    {
        SCHEDULE_ID = schedule_id;
    }

    public String getPRO_NAME()
    {
        return PRO_NAME;
    }

    public void setPRO_NAME(String pro_name)
    {
        PRO_NAME = pro_name;
    }

    public String getPRO_TYPE()
    {
        return PRO_TYPE;
    }

    public void setPRO_TYPE(String pro_type)
    {
        PRO_TYPE = pro_type;
    }

    public String getVALUE_string()
    {
        return VALUE_string;
    }

    public void setVALUE_string(String value_string)
    {
        this.object = value_string;
        VALUE_string = value_string;
    }

    public int getVALUE_int()
    {
        return VALUE_int;
    }

    public void setVALUE_int(int value_int)
    {
        this.object = new Integer(value_int);
        VALUE_int = value_int;
    }

    public short getVALUE_short()
    {
        return VALUE_short;
    }

    public void setVALUE_short(short value_short)
    {
        this.object = new Short(value_short);
        VALUE_short = value_short;
    }

    public long getVALUE_long()
    {
        return VALUE_long;
    }

    public void setVALUE_long(long value_long)
    {
        this.object = new Long(value_long);
        VALUE_long = value_long;
    }

    public double getVALUE_double()
    {
        return VALUE_double;
    }

    public void setVALUE_double(double value_double)
    {
        this.object = new Double(value_double);
        VALUE_double = value_double;
    }

    public Object getVALUE_OBJECT()
    {
        return VALUE_OBJECT;
    }

    public void setVALUE_OBJECT(Object value_object)
    {
        this.object = value_object;
        VALUE_OBJECT = value_object;
    }

    public boolean isVALUE_BOOLEAN()
    {
        return VALUE_BOOLEAN;
    }

    public void setVALUE_BOOLEAN(boolean value_boolean)
    {
        this.object = new Boolean(value_boolean);
        VALUE_BOOLEAN = value_boolean;
    }

    private String PRO_NAME;// VARCHAR2(100 BYTE) not null,

    private String PRO_TYPE;// VARCHAR2(20 BYTE) default 'string',

    private String VALUE_string;// CLOB,

    private int VALUE_int; // NUMBER,

    private short VALUE_short; // NUMBER,

    private long VALUE_long; // NUMBER,

    private float VALUE_float; // NUMBER,

    private byte VALUE_byte;
    
    private int pro_id;

    public int getPro_id()
    {
        return pro_id;
    }

    public void setPro_id(int pro_id)
    {
        this.pro_id = pro_id;
    }

    public byte getVALUE_byte()
    {

        return VALUE_byte;
    }

    public void setVALUE_byte(byte value_byte)
    {
        this.object = new Byte(value_byte);
        VALUE_byte = value_byte;
    }

    public float getVALUE_float()
    {
        return VALUE_float;
    }

    public void setVALUE_float(float value_float)
    {
        this.object = new Float(value_float);
        VALUE_float = value_float;
    }

    private double VALUE_double; // NUMBER,

    private Object VALUE_OBJECT;// BLOB,

    private boolean VALUE_BOOLEAN;// NUMBER(1)

    public boolean isString()
    {
        if (getPRO_TYPE() == null || getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_string))
            return true;
        return false;

    }

    public boolean isBoolean()
    {
        if (getPRO_TYPE() == null)
            return false;
        if (getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_boolean))
            return true;
        return false;

    }

    public boolean isInteger()
    {
        if (getPRO_TYPE() == null)
            return false;
        if (getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_int))
            return true;
        return false;

    }

    public boolean isDouble()
    {
        if (getPRO_TYPE() == null)
            return false;
        if (getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_double))
            return true;
        return false;

    }

    public boolean isFloat()
    {
        if (getPRO_TYPE() == null)
            return false;
        if (getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_float))
            return true;
        return false;

    }

    public boolean isLong()
    {
        if (getPRO_TYPE() == null)
            return false;
        if (getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_long))
            return true;
        return false;

    }

    public boolean isShort()
    {
        if (getPRO_TYPE() == null)
            return false;
        if (getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_short))
            return true;
        return false;

    }

    public boolean isObject()
    {
        if (getPRO_TYPE() == null)
            return false;
        if (getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_object))
            return true;
        return false;

    }

    public boolean isByte()
    {
        if (getPRO_TYPE() == null)
            return false;
        if (getPRO_TYPE().equalsIgnoreCase(HeaderProperty.protype_byte))
            return true;
        return false;

    }

    public static String getStringSQL()
    {
        return inserts[0];
    }

    public static String getNumberSQL()
    {
        return inserts[2];
    }

    public static String getBooleanSQL()
    {
        return inserts[1];
    }

    public static String getObjectSQL()
    {
        return inserts[3];
    }

    public static String getByteSQL()
    {
        return inserts[4];
    }

    public static final String[] inserts = new String[] {
            "insert into mq_properties(task_id, pro_name, pro_type,string_value,pro_id) values(?,?,?,?,?)",
            "insert into mq_properties(task_id, pro_name, pro_type,value_boolean,pro_id) values(?,?,?,?,?)",
            "insert into mq_properties(task_id, pro_name, pro_type,value_number,pro_id) values(?,?,?,?,?)",
            "insert into mq_properties(task_id, pro_name, pro_type,value_object,pro_id) values(?,?,?,?,?)",
            "insert into mq_properties(task_id, pro_name, pro_type,value_byte,pro_id) values(?,?,?,?,?)"

    };

    public String getInsertSQL()
    {
        if (this.isString())
            return inserts[0];
        if (this.isBoolean())
            return inserts[1];
        if (this.isInteger() || this.isLong() || this.isShort() || this.isDouble() || this.isFloat())
            return inserts[2];

        if (this.isObject())
            return inserts[3];
        if (this.isByte())
            return inserts[4];
        return inserts[0];

    }

}
