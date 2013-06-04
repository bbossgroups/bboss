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
package com.frameworkset.orm.engine.model;

/*
 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.Serializable;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.log4j.Logger;

// I don't know if the peer system deals
// with the recommended mappings.
//
//import java.sql.Date;
//import java.sql.Time;
//import java.sql.Timestamp;

/**
 * A class that maps JDBC types to their corresponding
 * Java object types, and Java native types. Used
 * by Column.java to perform object/native mappings.
 *
 * These are the official SQL type to Java type mappings.
 * These don't quite correspond to the way the peer
 * system works so we'll have to make some adjustments.
 * <pre>
 * -------------------------------------------------------
 * SQL Type      | Java Type            | Peer Type
 * -------------------------------------------------------
 * CHAR          | String               | String
 * VARCHAR       | String               | String
 * LONGVARCHAR   | String               | String
 * NUMERIC       | java.math.BigDecimal | java.math.BigDecimal
 * DECIMAL       | java.math.BigDecimal | java.math.BigDecimal
 * BIT           | boolean OR Boolean   | Boolean
 * TINYINT       | byte OR Byte         | Byte
 * SMALLINT      | short OR Short       | Short
 * INTEGER       | int OR Integer       | Integer
 * BIGINT        | long OR Long         | Long
 * REAL          | float OR Float       | Float
 * FLOAT         | double OR Double     | Double
 * DOUBLE        | double OR Double     | Double
 * BINARY        | byte[]               | ?
 * VARBINARY     | byte[]               | ?
 * LONGVARBINARY | byte[]               | ?
 * DATE          | java.sql.Date        | java.util.Date
 * TIME          | java.sql.Time        | java.util.Date
 * TIMESTAMP     | java.sql.Timestamp   | java.util.Date
 *
 * -------------------------------------------------------
 * A couple variations have been introduced to cover cases
 * that may arise, but are not covered above
 * BOOLEANCHAR   | boolean OR Boolean   | String
 * BOOLEANINT    | boolean OR Boolean   | Integer
 * </pre>
 *
 * @author <a href="mailto:jvanzyl@apache.org">Jason van Zyl</a>
 * @author <a href="mailto:mpoeschl@marmot.at>Martin Poeschl</a>
 * @version $Id: TypeMap.java,v 1.10 2004/08/26 05:18:38 seade Exp $
 */
public class TypeMap implements Serializable
{
    /** Logging class from commons.logging */
    private static Logger log = Logger.getLogger(Column.class);
    
    private static final SchemaType[] TEXT_TYPES =
    {
        SchemaType.CHAR, SchemaType.VARCHAR, SchemaType.LONGVARCHAR, 
        SchemaType.CLOB, SchemaType.DATE, SchemaType.TIME, 
        SchemaType.TIMESTAMP, SchemaType.BOOLEANCHAR
    };

    public static final String CHAR_OBJECT_TYPE = "\"\"";
    public static final String VARCHAR_OBJECT_TYPE = "\"\"";
    public static final String LONGVARCHAR_OBJECT_TYPE = "\"\"";
    public static final String CLOB_OBJECT_TYPE = "\"\"";
    public static final String NUMERIC_OBJECT_TYPE = "new BigDecimal(0)";
    public static final String DECIMAL_OBJECT_TYPE = "new BigDecimal(0)";
    public static final String BIT_OBJECT_TYPE = "new Boolean(true)";
    public static final String TINYINT_OBJECT_TYPE = "new Byte((byte)0)";
    public static final String SMALLINT_OBJECT_TYPE = "new Short((short)0)";
    public static final String INTEGER_OBJECT_TYPE = "new Integer(0)";
    public static final String BIGINT_OBJECT_TYPE = "new Long(0)";
    public static final String REAL_OBJECT_TYPE = "new Float(0)";
    public static final String FLOAT_OBJECT_TYPE = "new Double(0)";
    public static final String DOUBLE_OBJECT_TYPE = "new Double(0)";
    public static final String BINARY_OBJECT_TYPE = "new Object()"; //?
    public static final String VARBINARY_OBJECT_TYPE = "new Object()"; //?
    public static final String LONGVARBINARY_OBJECT_TYPE = "new Object()"; //?
    public static final String BLOB_OBJECT_TYPE = "new Object()"; //?
    public static final String DATE_OBJECT_TYPE = "new Date()";
    public static final String TIME_OBJECT_TYPE = "new Date()";
    public static final String TIMESTAMP_OBJECT_TYPE = "new Date()";
    public static final String BOOLEANCHAR_OBJECT_TYPE = "\"\"";
    public static final String BOOLEANINT_OBJECT_TYPE = "new Integer(0)";

    public static final String CHAR_NATIVE_TYPE = "String";
    public static final String VARCHAR_NATIVE_TYPE = "String";
    public static final String LONGVARCHAR_NATIVE_TYPE = "String";
    public static final String CLOB_NATIVE_TYPE = "String";
    
    public static final String NUMERIC_NATIVE_TYPE = "BigDecimal";
    public static final String DECIMAL_NATIVE_TYPE = "BigDecimal";
    
    public static final String BIT_NATIVE_TYPE = "boolean";
    public static final String BOOLEANCHAR_NATIVE_TYPE = "boolean";
    public static final String BOOLEANINT_NATIVE_TYPE = "boolean";
    
    public static final String TINYINT_NATIVE_TYPE = "byte";
    
    public static final String SMALLINT_NATIVE_TYPE = "short";
    
    
    public static final String INTEGER_NATIVE_TYPE = "int";
    public static final String BIGINT_NATIVE_TYPE = "long";
    
    public static final String REAL_NATIVE_TYPE = "float";
    
    public static final String FLOAT_NATIVE_TYPE = "double";
    public static final String DOUBLE_NATIVE_TYPE = "double";
    
    public static final String DATE_NATIVE_TYPE = "Date";
    public static final String TIME_NATIVE_TYPE = "Date";
    public static final String TIMESTAMP_NATIVE_TYPE = "Date";
    
    public static final String BINARY_NATIVE_TYPE = "byte[]";
    public static final String VARBINARY_NATIVE_TYPE = "byte[]";
    public static final String LONGVARBINARY_NATIVE_TYPE = "byte[]";
    public static final String BLOB_NATIVE_TYPE = "byte[]";
    
    

    public static final String BIT_NATIVE_OBJECT_TYPE = "Boolean";
    public static final String TINYINT_NATIVE_OBJECT_TYPE = "Byte";
    public static final String SMALLINT_NATIVE_OBJECT_TYPE = "Short";
    public static final String INTEGER_NATIVE_OBJECT_TYPE = "Integer";
    public static final String BIGINT_NATIVE_OBJECT_TYPE = "Long";
    public static final String REAL_NATIVE_OBJECT_TYPE = "Float";
    public static final String FLOAT_NATIVE_OBJECT_TYPE = "Double";
    public static final String DOUBLE_NATIVE_OBJECT_TYPE = "Double";
    public static final String BOOLEANCHAR_NATIVE_OBJECT_TYPE = "Boolean";
    public static final String BOOLEANINT_NATIVE_OBJECT_TYPE = "Boolean";

    public static final String CHAR_VILLAGE_METHOD = "asString()";
    public static final String VARCHAR_VILLAGE_METHOD = "asString()";
    public static final String LONGVARCHAR_VILLAGE_METHOD = "asString()";
    public static final String CLOB_VILLAGE_METHOD = "asString()";
    public static final String NUMERIC_VILLAGE_METHOD = "asBigDecimal()";
    public static final String DECIMAL_VILLAGE_METHOD = "asBigDecimal()";
    public static final String BIT_VILLAGE_METHOD = "asBoolean()";
    public static final String TINYINT_VILLAGE_METHOD = "asByte()";
    public static final String SMALLINT_VILLAGE_METHOD = "asShort()";
    public static final String INTEGER_VILLAGE_METHOD = "asInt()";
    public static final String BIGINT_VILLAGE_METHOD = "asLong()";
    public static final String REAL_VILLAGE_METHOD = "asFloat()";
    public static final String FLOAT_VILLAGE_METHOD = "asDouble()";
    public static final String DOUBLE_VILLAGE_METHOD = "asDouble()";
    public static final String BINARY_VILLAGE_METHOD = "asBytes()";
    public static final String VARBINARY_VILLAGE_METHOD = "asBytes()";
    public static final String LONGVARBINARY_VILLAGE_METHOD = "asBytes()";
    public static final String BLOB_VILLAGE_METHOD = "asBytes()";
    public static final String DATE_VILLAGE_METHOD = "asUtilDate()";
    public static final String TIME_VILLAGE_METHOD = "asUtilDate()";
    public static final String TIMESTAMP_VILLAGE_METHOD = "asUtilDate()";
    public static final String BOOLEANCHAR_VILLAGE_METHOD = "asBoolean()";
    public static final String BOOLEANINT_VILLAGE_METHOD = "asBoolean()";

    public static final String BIT_VILLAGE_OBJECT_METHOD = "asBooleanObj()";
    public static final String TINYINT_VILLAGE_OBJECT_METHOD = "asByteObj()";
    public static final String SMALLINT_VILLAGE_OBJECT_METHOD = "asShortObj()";
    public static final String INTEGER_VILLAGE_OBJECT_METHOD = "asIntegerObj()";
    public static final String BIGINT_VILLAGE_OBJECT_METHOD = "asLongObj()";
    public static final String REAL_VILLAGE_OBJECT_METHOD = "asFloatObj()";
    public static final String FLOAT_VILLAGE_OBJECT_METHOD = "asDoubleObj()";
    public static final String DOUBLE_VILLAGE_OBJECT_METHOD = "asDoubleObj()";
    public static final String BOOLEANCHAR_VILLAGE_OBJECT_METHOD = "asBooleanObj()";
    public static final String BOOLEANINT_VILLAGE_OBJECT_METHOD = "asBooleanObj()";

    public static final String CHAR_PP_METHOD = "getString(ppKey)";
    public static final String VARCHAR_PP_METHOD = "getString(ppKey)";
    public static final String LONGVARCHAR_PP_METHOD = "getString(ppKey)";
    public static final String NUMERIC_PP_METHOD = "getBigDecimal(ppKey)";
    public static final String DECIMAL_PP_METHOD = "getBigDecimal(ppKey)";
    public static final String BIT_PP_METHOD = "getBoolean(ppKey)";
    public static final String TINYINT_PP_METHOD = "getByte(ppKey)";
    public static final String SMALLINT_PP_METHOD = "getShort(ppKey)";
    public static final String INTEGER_PP_METHOD = "getInt(ppKey)";
    public static final String BIGINT_PP_METHOD = "getLong(ppKey)";
    public static final String REAL_PP_METHOD = "getFloat(ppKey)";
    public static final String FLOAT_PP_METHOD = "getDouble(ppKey)";
    public static final String DOUBLE_PP_METHOD = "getDouble(ppKey)";
    public static final String BINARY_PP_METHOD = "getBytes(ppKey)";
    public static final String VARBINARY_PP_METHOD = "getBytes(ppKey)";
    public static final String LONGVARBINARY_PP_METHOD = "getBytes(ppKey)";
    public static final String DATE_PP_METHOD = "getDate(ppKey)";
    public static final String TIME_PP_METHOD = "getDate(ppKey)";
    public static final String TIMESTAMP_PP_METHOD = "getDate(ppKey)";
    public static final String BOOLEANCHAR_PP_METHOD = "getBoolean(ppKey)";
    public static final String BOOLEANINT_PP_METHOD = "getBoolean(ppKey)";

    private static Hashtable jdbcToJavaObjectMap = null;
    private static Hashtable jdbcToJavaNativeMap = null;
    private static Hashtable jdbcToJavaNativeObjectMap = null;
    private static Hashtable jdbcToVillageMethodMap = null;
    private static Hashtable jdbcToVillageObjectMethodMap = null;
    private static Hashtable jdbcToPPMethodMap = null;
    private static Hashtable torqueTypeToJdbcTypeMap = null;
    private static Hashtable jdbcToTorqueTypeMap = null;
    private static boolean isInitialized = false;

    /**
     * Initializes the SQL to Java map so that it
     * can be used by client code.
     */
    public synchronized static void initialize()
    {
        if (!isInitialized)
        {
            // Create JDBC -> Java object mappings.
            jdbcToJavaObjectMap = new Hashtable();

            jdbcToJavaObjectMap.put(SchemaType.CHAR, CHAR_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.VARCHAR, VARCHAR_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.LONGVARCHAR, LONGVARCHAR_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.CLOB, CLOB_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.NUMERIC, NUMERIC_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.DECIMAL, DECIMAL_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.BIT, BIT_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.TINYINT, TINYINT_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.SMALLINT, SMALLINT_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.INTEGER, INTEGER_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.BIGINT, BIGINT_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.REAL, REAL_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.FLOAT, FLOAT_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.DOUBLE, DOUBLE_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.BINARY, BINARY_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.VARBINARY, VARBINARY_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.LONGVARBINARY, LONGVARBINARY_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.BLOB, BLOB_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.DATE, DATE_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.TIME, TIME_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.TIMESTAMP, TIMESTAMP_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.BOOLEANCHAR, BOOLEANCHAR_OBJECT_TYPE);
            jdbcToJavaObjectMap.put(SchemaType.BOOLEANINT, BOOLEANINT_OBJECT_TYPE);

            // Create JDBC -> native Java type mappings.
            jdbcToJavaNativeMap = new Hashtable();

            jdbcToJavaNativeMap.put(SchemaType.CHAR, CHAR_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.VARCHAR, VARCHAR_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.LONGVARCHAR, LONGVARCHAR_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.CLOB, CLOB_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.NUMERIC, NUMERIC_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.DECIMAL, DECIMAL_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.BIT, BIT_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.TINYINT, TINYINT_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.SMALLINT, SMALLINT_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.INTEGER, INTEGER_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.BIGINT, BIGINT_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.REAL, REAL_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.FLOAT, FLOAT_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.DOUBLE, DOUBLE_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.BINARY, BINARY_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.VARBINARY, VARBINARY_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.LONGVARBINARY, LONGVARBINARY_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.BLOB, BLOB_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.DATE, DATE_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.TIME, TIME_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.TIMESTAMP, TIMESTAMP_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.BOOLEANCHAR, BOOLEANCHAR_NATIVE_TYPE);
            jdbcToJavaNativeMap.put(SchemaType.BOOLEANINT, BOOLEANINT_NATIVE_TYPE);

            jdbcToJavaNativeObjectMap = new Hashtable();
            jdbcToJavaNativeObjectMap.put(SchemaType.BIT, BIT_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.TINYINT, TINYINT_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.SMALLINT, SMALLINT_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.INTEGER, INTEGER_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.BIGINT, BIGINT_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.REAL, REAL_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.FLOAT, FLOAT_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.DOUBLE, DOUBLE_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.BOOLEANCHAR,
                                          BOOLEANCHAR_NATIVE_OBJECT_TYPE);
            jdbcToJavaNativeObjectMap.put(SchemaType.BOOLEANINT,
                                          BOOLEANINT_NATIVE_OBJECT_TYPE);

            // Create JDBC -> Village asX() mappings.
            jdbcToVillageMethodMap = new Hashtable();

            jdbcToVillageMethodMap.put(SchemaType.CHAR, CHAR_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.VARCHAR, VARCHAR_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.LONGVARCHAR, LONGVARCHAR_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.CLOB, CLOB_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.NUMERIC, NUMERIC_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.DECIMAL, DECIMAL_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.BIT, BIT_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.TINYINT, TINYINT_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.SMALLINT, SMALLINT_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.INTEGER, INTEGER_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.BIGINT, BIGINT_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.REAL, REAL_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.FLOAT, FLOAT_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.DOUBLE, DOUBLE_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.BINARY, BINARY_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.VARBINARY, VARBINARY_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.LONGVARBINARY, LONGVARBINARY_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.BLOB, BLOB_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.DATE, DATE_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.TIME, TIME_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.TIMESTAMP, TIMESTAMP_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.BOOLEANCHAR, BOOLEANCHAR_VILLAGE_METHOD);
            jdbcToVillageMethodMap.put(SchemaType.BOOLEANINT, BOOLEANINT_VILLAGE_METHOD);


            jdbcToVillageObjectMethodMap = new Hashtable();
            jdbcToVillageObjectMethodMap.put(SchemaType.BIT, BIT_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.TINYINT,
                                             TINYINT_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.SMALLINT,
                                             SMALLINT_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.INTEGER,
                                             INTEGER_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.BIGINT,
                                             BIGINT_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.REAL, REAL_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.FLOAT, FLOAT_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.DOUBLE,
                                             DOUBLE_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.BOOLEANCHAR,
                                             BOOLEANCHAR_VILLAGE_OBJECT_METHOD);
            jdbcToVillageObjectMethodMap.put(SchemaType.BOOLEANINT,
                                             BOOLEANINT_VILLAGE_OBJECT_METHOD);

            // Create JDBC -> ParameterParser getX() mappings.
            jdbcToPPMethodMap = new Hashtable();

            jdbcToPPMethodMap.put(SchemaType.CHAR, CHAR_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.VARCHAR, VARCHAR_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.LONGVARCHAR, LONGVARCHAR_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.NUMERIC, NUMERIC_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.DECIMAL, DECIMAL_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.BIT, BIT_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.TINYINT, TINYINT_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.SMALLINT, SMALLINT_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.INTEGER, INTEGER_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.BIGINT, BIGINT_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.REAL, REAL_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.FLOAT, FLOAT_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.DOUBLE, DOUBLE_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.BINARY, BINARY_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.VARBINARY, VARBINARY_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.LONGVARBINARY, LONGVARBINARY_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.DATE, DATE_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.TIME, TIME_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.TIMESTAMP, TIMESTAMP_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.BOOLEANCHAR, BOOLEANCHAR_PP_METHOD);
            jdbcToPPMethodMap.put(SchemaType.BOOLEANINT, BOOLEANINT_PP_METHOD);

            // Create JDBC -> Java object mappings.
            torqueTypeToJdbcTypeMap = new Hashtable();

            Iterator iter = SchemaType.iterator();
            while (iter.hasNext()) 
            {
                SchemaType type = (SchemaType) iter.next();
                torqueTypeToJdbcTypeMap.put(type, type);
            }
            torqueTypeToJdbcTypeMap.put(SchemaType.BOOLEANCHAR, SchemaType.CHAR);
            torqueTypeToJdbcTypeMap.put(SchemaType.BOOLEANINT, SchemaType.INTEGER);

            // Create JDBC type code to torque type map.
            jdbcToTorqueTypeMap = new Hashtable();

            jdbcToTorqueTypeMap.put(new Integer(Types.CHAR), SchemaType.CHAR);
            jdbcToTorqueTypeMap.put(new Integer(Types.VARCHAR), SchemaType.VARCHAR);
            jdbcToTorqueTypeMap.put(new Integer(Types.LONGVARCHAR), SchemaType.LONGVARCHAR);
            jdbcToTorqueTypeMap.put(new Integer(Types.CLOB), SchemaType.CLOB);
            jdbcToTorqueTypeMap.put(new Integer(Types.NUMERIC), SchemaType.NUMERIC);
            jdbcToTorqueTypeMap.put(new Integer(Types.DECIMAL), SchemaType.DECIMAL);
            jdbcToTorqueTypeMap.put(new Integer(Types.BIT), SchemaType.BIT);
            jdbcToTorqueTypeMap.put(new Integer(Types.TINYINT), SchemaType.TINYINT);
            jdbcToTorqueTypeMap.put(new Integer(Types.SMALLINT), SchemaType.SMALLINT);
            jdbcToTorqueTypeMap.put(new Integer(Types.INTEGER), SchemaType.INTEGER);
            jdbcToTorqueTypeMap.put(new Integer(Types.BIGINT), SchemaType.BIGINT);
            jdbcToTorqueTypeMap.put(new Integer(Types.REAL), SchemaType.REAL);
            jdbcToTorqueTypeMap.put(new Integer(Types.FLOAT), SchemaType.FLOAT);
            jdbcToTorqueTypeMap.put(new Integer(Types.DOUBLE), SchemaType.DOUBLE);
            jdbcToTorqueTypeMap.put(new Integer(Types.BINARY), SchemaType.BINARY);
            jdbcToTorqueTypeMap.put(new Integer(Types.VARBINARY), SchemaType.VARBINARY);
            jdbcToTorqueTypeMap.put(new Integer(Types.LONGVARBINARY), SchemaType.LONGVARBINARY);
            jdbcToTorqueTypeMap.put(new Integer(Types.BLOB), SchemaType.BLOB);
            jdbcToTorqueTypeMap.put(new Integer(Types.DATE), SchemaType.DATE);
            jdbcToTorqueTypeMap.put(new Integer(Types.TIME), SchemaType.TIME);
            jdbcToTorqueTypeMap.put(new Integer(Types.TIMESTAMP), SchemaType.TIMESTAMP);

            isInitialized = true;
        }
    }

    /**
     * Report whether this object has been initialized.
     *
     * @return true if this object has been initialized
     */
    public static boolean isInitialized()
    {
        return isInitialized;
    }

    /**
     * Return a Java object which corresponds to the
     * JDBC type provided. Use in MapBuilder generation.
     *
     * @param jdbcType the JDBC type
     * @return name of the Object
     */
    public static String getJavaObject(SchemaType jdbcType)
    {
        // Make sure the we are initialized.
        if (!isInitialized)
        {
            initialize();
        }
        return (String) jdbcToJavaObjectMap.get(jdbcType);
    }

    /**
     * Return native java type which corresponds to the
     * JDBC type provided. Use in the base object class generation.
     *
     * @param jdbcType the JDBC type
     * @return name of the native java type
     */
    public static String getJavaNative(SchemaType jdbcType)
    {
        // Make sure the we are initialized.
        if (!isInitialized)
        {
            initialize();
        }
        return (String) jdbcToJavaNativeMap.get(jdbcType);
    }

    /**
     * Return native java type which corresponds to the
     * JDBC type provided. Use in the base object class generation.
     *
     * @param jdbcType the JDBC type
     * @return name of the Object
     */
    public static String getJavaNativeObject(SchemaType jdbcType)
    {
        // Make sure the we are initialized.
        if (!isInitialized)
        {
            initialize();
        }
        String s = (String) jdbcToJavaNativeObjectMap.get(jdbcType);
        if (s == null)
        {
            s = (String) jdbcToJavaNativeMap.get(jdbcType);
        }
        return s;
    }

    /**
     * Return Village asX() method which corresponds to the
     * JDBC type provided. Use in the Peer class generation.
     *
     * @param jdbcType the JDBC type
     * @return name of the Village asX() method
     */
    public static String getVillageMethod(SchemaType jdbcType)
    {
        // Make sure the we are initialized.
        if (!isInitialized)
        {
            initialize();
        }
        return (String) jdbcToVillageMethodMap.get(jdbcType);
    }

    /**
     * Return Village asX() method which corresponds to the
     * JDBC type provided. Use in the Peer class generation.
     *
     * @param jdbcType the JDBC type
     * @return name of the Village asX() method
     */
    public static String getVillageObjectMethod(SchemaType jdbcType)
    {
        // Make sure the we are initialized.
        if (!isInitialized)
        {
            initialize();
        }
        String s = (String) jdbcToVillageObjectMethodMap.get(jdbcType);
        if (s == null)
        {
            s = (String) jdbcToVillageMethodMap.get(jdbcType);
        }
        return s;
    }

    /**
     * Return ParameterParser getX() method which corresponds to the
     * JDBC type provided. Use in the Object class generation.
     *
     * @param jdbcType the JDBC type
     * @return name of the ParameterParser getX() method
     */
    public static String getPPMethod(SchemaType jdbcType)
    {
        // Make sure the we are initialized.
        if (!isInitialized)
        {
            initialize();
        }
        return (String) jdbcToPPMethodMap.get(jdbcType);
    }

    /**
     * Returns the correct jdbc type for torque added types
     *
     * @param type the torque added type
     * @return name of the the correct jdbc type
     * @deprecated the type conversion is handled by the platform package
     *             (since torque 3.2)
     */
    public static SchemaType getJdbcType(SchemaType type)
    {
        // Make sure the we are initialized.
        if (!isInitialized)
        {
            initialize();
        }
        return (SchemaType) torqueTypeToJdbcTypeMap.get(type);
    }

    /**
     * Returns Torque type constant corresponding to JDBC type code.
     * Used by the Torque JDBC task.
     *
     * @param sqlType the SQL type
     * @return Torque type constant
     */
    public static SchemaType getTorqueType(Integer sqlType)
    {
        // Make sure the we are initialized.
        if (!isInitialized)
        {
            initialize();
        }
        SchemaType st = (SchemaType) jdbcToTorqueTypeMap.get(sqlType);
        if (st == null)
        {
            st = SchemaType.VARCHAR;
            log.warn("SchemaType for JdbcType '" + sqlType +
                     "' is not defined: Defaulting to '" + st + '\'');
        }
        return st;
    }

    /**
     * Returns true if the type is boolean in the java
     * object and a numeric (1 or 0) in the db.
     *
     * @param type The type to check.
     * @return true if the type is BOOLEANINT
     */
    public static boolean isBooleanInt(SchemaType type)
    {
        return SchemaType.BOOLEANINT.equals(type);
    }

    /**
     * Returns true if the type is boolean in the
     * java object and a String "Y" or "N" in the db.
     *
     * @param type The type to check.
     * @return true if the type is BOOLEANCHAR
     */
    public static boolean isBooleanChar(SchemaType type)
    {
        return SchemaType.BOOLEANCHAR.equals(type);
    }

    /**
     * Returns true if the type is boolean in the
     * java object and a Bit "1" or "0" in the db.
     *
     * @param type The type to check.
     * @return true if the type is BIT
     */
    public static boolean isBit(SchemaType type)
    {
        return SchemaType.BIT.equals(type);
    }

    /**
     * Returns true if values for the type need to be quoted.
     *
     * @param type The type to check.
     * @return true if values for the type need to be quoted.
     */
    public static final boolean isTextType(SchemaType type)
    {
        for (int i = 0; i < TEXT_TYPES.length; i++)
        {
            if (type.equals(TEXT_TYPES[i]))
            {
                return true;
            }
        }

        // If we get this far, there were no matches.
        return false;
    }
    
    /**
     * Return true if type is date
     * @param type
     * @return
     */
    public static final boolean isDate(SchemaType type)
    {
        return type.equals(SchemaType.DATE);
            
    }
}
