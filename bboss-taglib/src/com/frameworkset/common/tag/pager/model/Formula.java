/*****************************************************************************
 *                                                                           *
 *  This file is part of the tna framework distribution.                     *
 *  Documentation and updates may be get from  biaoping.yin the author of    *
 *  this framework							     							 *
 *                                                                           *
 *  Sun Public License Notice:                                               *
 *                                                                           *
 *  The contents of this file are subject to the Sun Public License Version  *
 *  1.0 (the "License"); you may not use this file except in compliance with *
 *  the License. A copy of the License is available at http://www.sun.com    *
 *                                                                           *
 *  The Original Code is tag. The Initial Developer of the Original          *
 *  Code is biaoping yin. Portions created by biaoping yin are Copyright     *
 *  (C) 2000.  All Rights Reserved.                                          *
 *                                                                           *
 *  GNU Public License Notice:                                               *
 *                                                                           *
 *  Alternatively, the contents of this file may be used under the terms of  *
 *  the GNU Lesser General Public License (the "LGPL"), in which case the    *
 *  provisions of LGPL are applicable instead of those above. If you wish to *
 *  allow use of your version of this file only under the  terms of the LGPL *
 *  and not to allow others to use your version of this file under the SPL,  *
 *  indicate your decision by deleting the provisions above and replace      *
 *  them with the notice and other provisions required by the LGPL.  If you  *
 *  do not delete the provisions above, a recipient may use your version of  *
 *  this file under either the SPL or the LGPL.                              *
 *                                                                           *
 *  biaoping.yin (yin-bp@163.com)                                            *
 *                                                                           *
 *****************************************************************************/
package com.frameworkset.common.tag.pager.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.frameworkset.common.tag.exception.FormulaException;
import com.frameworkset.common.tag.pager.tags.PagerDataSet;
import com.frameworkset.util.StringUtil;
import com.frameworkset.util.ValueObjectUtil;

/**
 * 计算公式的值,公式中包含的参数有两种:变量,常量
 * 包含的操作有:
 * status的值表示字符的种类，主要有
 * -1：开始状态
 * 0: 数字开始
 * 1: 中间数字
 * 2：数字结束
 * 3：空格开始
 * 4：空格中
 * 5：{
 * 6: }
 * 7: 字母开始
 * 8：中间字母
 * 9：*乘法
 * 10：/整除
 * 11：％求余
 * 12：sum求和
 * 13：count计数
 * 14：avage求平均数
 * 15:+加法
 * 16：除法
 * @author biaoping.yin
 * created on 2005-5-21
 * version 1.0
 */
public class Formula implements ModelObject
{   
    /**
     * 求变量值相关的数据源
     */
    private PagerDataSet dataSet;
    
    
	/**
	 * 标识公式是否已经解析完成
	 */
	private boolean parsered = false;
	
	/**保存解析后所获取到的所有token*/
	private List tokens = null;
    /**
     * 数学表达式，比如:
     * ({value1} + 4)/{value2}
     * 其中"{"和"}"之间的单词表示字段名称与相应数据库表或者值对象的属性名称一致
     * 公式计算时会采用实际的属性值替换本变量，再计算表达式的值
     */
    private String formula = null;
    /**
     * 约定栈顶操作符与栈外操作符一样时，根据操作得先后顺序（从左致右还是从右到左）
     * 操作符堆栈
     * 根据的栈外操作和栈顶操作的优先级比较进行相应的操作（同级操作符之间栈内优先级比栈外优先级要高）：
     * a.栈外操作符优先级高，则栈外操作符进栈
     * b.栈外操作符优先级低，栈顶操作符出栈，然后操作数堆栈中栈顶操作数出栈（如果操作是单操作数，
     * 则只取一个操作数，如果是双操作数则连续获取两次操作数），进行计算计算完成后结果进操作数堆栈
     * c.重复b操作，直到栈外操作符的优先级比栈内优先级高时再作如下操作：
     *  1.如果栈外操作符为‘）’，则栈内操作符必为‘（’，直接出栈。
     *  2.如果操作符栈为空则结束计算，操作数堆栈栈顶元素即为公式计算结果
     *  3.否则操作数进栈
     * 待进一步处理的情况：
     * '{'没有作为操作符来处理，
     */
    private java.util.Stack operations = null;
    /**操作数堆栈*/
    private java.util.Stack operationParms = null;
    
    /**
     * 跟踪操作符错误堆栈
     */
    private java.util.Stack operStackTrace = new java.util.Stack();
    
    /**
     * 操作符堆栈外操作符优先级表
     */
    private static int[]  inPriorTable = new int[] {8,6,6,6,5,5,9,9,9,2,1,2,1};
    /**
     * 操作符堆栈内操作符优先级表
     */
    private static int[] outPriorTable = new int[] {7,6,6,6,5,5,9,9,9,10,1,10,0};
    
    /**
     * 操作符号码
     */
    private static final char OPER_LB = '{';  //1  
    private static final char OPER_RB = '}';  //1
    private static final char OPER_LP = '(';  //2
    private static final char OPER_RP = ')';  //3
    private static final char OPER_SUM = 's'; //4
    private static final char OPER_COUNT = 'c'; //4    
    private static final char OPER_AVG = 'a'; //4 
    private static final char OPER_SUB = '-'; //5
    private static final char OPER_ADD = '+'; //5    
    private static final char OPER_MOD = '%'; //6
    private static final char OPER_DIV = '/'; //6
    private static final char OPER_MUL = '*'; //6
    private static final char OPER_POW = '^'; //7
    
    
    
    
    //private static char OPER_PARENTHESIS = '(';
    
    /**
     * 操作符内码定义
     */
    private final static int CODE_RB = 13;
    private final static int CODE_LB = 12;
    private final static int CODE_RP = 11;
    private final static int CODE_LP = 10;
    private final static int CODE_COUNT = 9;
    private final static int CODE_SUM = 8;
    private final static int CODE_AVG = 7;    
    
    private final static int CODE_SUB = 6;
    private final static int CODE_ADD = 5;
    private final static int CODE_MOD = 4;
    private final static int CODE_DIV = 3;
    private final static int CODE_MUL = 2;  
    private final static int CODE_POW = 1;
    
    /**定义操作符与操作内码映射*/
    private static OPERCode[] operCodes = new OPERCode[] {
            new OPERCode(OPER_POW,CODE_POW),
            new OPERCode(OPER_MUL,CODE_MUL),
            new OPERCode(OPER_DIV,CODE_DIV),
            new OPERCode(OPER_MOD,CODE_MOD),
            new OPERCode(OPER_ADD,CODE_ADD),
            new OPERCode(OPER_SUB,CODE_SUB),
            new OPERCode(OPER_AVG,CODE_AVG),
            new OPERCode(OPER_SUM,CODE_SUM),
            new OPERCode(OPER_COUNT,CODE_COUNT), 
            new OPERCode(OPER_LP,CODE_LP),
            new OPERCode(OPER_RP,CODE_RP),
            new OPERCode(OPER_LB,CODE_LB),
            new OPERCode(OPER_RB,CODE_RB)           
    };
    
    private static char PARENTHESES = '"';    
    
    //private static char SEMICOLON_STRING = '\\\"';
    
    private static String FUNCTION_SUM = "sum";    
    private static String FUNCTION_COUNT = "count";    
    private static String FUNCTION_AVG = "avg";
    
    /**存储转换为字符数组后的公式*/
    private char[] formulaChars = null; 
    /**
     * 记录分析公式字符的当前位置
     */
    private int curpos = 0;
    
    /**
     * 十进制基数
     */
    private static final int RADIX = 10;
    
    /**
     * 十进制基数
     */
    private static final double D_RADIX = 0.1;
    
    /**
     * 定义操作数类型
     */
    private static final int OPT_TYPE_VARIABLE = 0;
    private static final int OPT_TYPE_INTEGER = 1;
    private static final int OPT_TYPE_DOUBLE = 2;
    private static final int OPT_TYPE_STRING = 3;
    private static final int OPT_TYPE_LONG = 4;
    
//    /**
//     * 十进制小数位基数
//     */
//    private static final int FRADIX = 0.1;
    
    
    /**
     * 状态符号，根据不同的状态组合确定不同的操作
     */
    /**公式开始*/
    private static final int STATUS_FORMULA_BEGIN = 0;
    /**变量开始*/
    private static final int STATUS_VARIABLE_BEGIN = 1;
    /**变量结束*/
    private static final int STATUS_VARIABLE_END = 2;
    /**数开始*/
    private static final int STATUS_NUMCONST_BEGIN = 3;
    /**数结束*/
    private static final int STATUS_NUMCONST_END = 4;    
    /**
     * 小数位开始状态
     */
    private static final int STATUS_FNUMCONST_BEGIN = 5;
    /**字符串开始*/
    private static final int STATUS_STRCONST_BEGIN = 6;
    /**字符串结束*/
    private static final int STATUS_STRCONST_END = 7;
    /**操作符开始*/
    private static final int STATUS_OPERATION_BEGIN = 8;
    /**操作符结束*/
    private static final int STATUS_OPERATION_END = 9;
    
    
    /**
     * 公式分析当前状态标识，缺省为公式开始状态
     */
    private int status = STATUS_FORMULA_BEGIN;
    /**
     * 分析的上一次状态标识
     */
    private int oldstatus = status;
    
    
    
    
    public Formula(PagerDataSet dataSet,String formula)
    {
        this.dataSet = dataSet;
        this.formula = formula;
        formulaChars = formula.trim().toCharArray();
    }
    
    /**
     * 重置dataSet属性为null
     *
     */
    public void reset()
    {
    	this.dataSet = null;
    }
    
    /**
     * 重置dataSet属性为当前的dataSet对象
     * @param dataSet
     */
    public void setDataSet(PagerDataSet dataSet)
    {
    	this.dataSet = dataSet;
    }
    
    /**
     * 当前操作符进栈
     * @param oper
     * @param curpos
     */
    private void push(char oper, int curpos)
    {
        
        this.operStackTrace.push(new OPERPOS(oper,curpos));
    }
    
    /**
     * 弹出当前操作符所对应的关标识
     * @param curChar
     * @param curpos
     * @return OPERPOS
     * @throws FormulaException
     */
    private OPERPOS pop(char curChar,int curpos) throws FormulaException
    {        
        OPERPOS ret = null;
        try
        {
            ret = (OPERPOS)operStackTrace.pop();
            if(getMatchOper(ret.getOper()) != curChar)
            {
                throw new FormulaException("illegal formula:operator symbol[" + ret.getOper() + ":"+ ret.getCurpos() + "] not matched [" + curChar + ":"+ curpos + "]");
            }
            return ret;
        }
        catch(java.util.EmptyStackException e)
        {
            throw new FormulaException("illegal formula:operator symbol not matched [" + curChar + "],index:["+ curpos + "]");
        }
    }
    
    /**
     * 获取操作符匹配的开标识或关标识
     * @param curChar
     * @return char
     */
    private char getMatchOper(char curChar)
    {
        if(curChar == OPER_LB)
            return OPER_RB;
        if(curChar == OPER_LP)
            return OPER_RP;
        if(curChar == OPER_RB)
            return OPER_LB;
        if(curChar == OPER_RP)
            return OPER_LP;
        if(curChar == PARENTHESES)
            return PARENTHESES;
        return ' ';
        
    }
    /**
     * 解析文法
     * 获取公式中的单个单元，目前包括：操作符，变量，常量
     * 
     * @return 二维数组，第一维存储操作符内码，第二维存储操作数，第三维存储操作数类型（变量，数，字符串）
     */
    private Object[] getToken() throws FormulaException
    {
        /**
         * 返回值
         */
        Object[] obj = new Object[3];
        //存放当前字符
        char curChar = ' ';
        //存放变量名称/字符串的名称
        String token = "";
        double dradix = 1.0;
        //存放数字的值
        double number = 0.0;        
        //定义以下两个变量，当公式中包含小数类型的操作数时分别用来记录小数和整数数字个数
        int integerNum = 0;//记录整数数字个数
        int dNum = 0;//记录小数数字个数
        
        for(; curpos < formulaChars.length; curpos ++)
        {            
            curChar = this.formulaChars[curpos];
            //根据不同的状态采取不同的操作
            switch(status)
            {                	
            	case STATUS_FORMULA_BEGIN://公式开始
            	    if(curChar < '0' || curChar > '9')//如果不是数字
                    {
                	    if(curChar == OPER_LB)
                	    {
                	       
                	        //修改老状态为当前状态到
                	        oldstatus = status;
                	        
                	        //变量开始，修改当前状态为变量开始
                	        status = STATUS_VARIABLE_BEGIN;
                	        push(curChar,curpos);
                	        continue;
                	        
                    	    
                	    }	                	    
                	    else if(curChar == PARENTHESES)//如果是分号，表示字符串开始
                	    {	
                	        push(curChar,curpos);
                	        oldstatus = status;
                	        status = STATUS_STRCONST_BEGIN;
                	        continue;
                	    }
                	    else if(curChar == OPER_RB || curChar == OPER_RP) //如果是‘}’,标识公式非法，抛异常信息
                	    {
                	        throw new FormulaException("Illegle formula start or need a oprator data:char[" + curChar + "],index:["+ curpos + "]");
                	    }
                	    else if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
        	            {
        	                continue;
        	            }
                	    
                	    //获取操作码
                	    int code = this.getOperCode(curChar);                	    
                	    
                	    //如果是操作码返回操作码的内码
                	    if(code != -1)
                	    {
                	        oldstatus = status;
                	        status = STATUS_OPERATION_END;
                	        //检测如果是sum操作                	        
                	        obj[0] = new Integer(code);
                	        if(curChar == OPER_LP)
                	            push(curChar,curpos);
                	        return obj;
                	    }
                	    else //否则抛出异常信息
                	    {
                	        throw new FormulaException("Illegle formula start:[" + curChar + "],index:["+ curpos + "]");                	         
                	    }
                	    //throw new FormulaException("Illegle formula start:[" + curChar + "],index:["+ curpos + "]");
                    }
                	else //如果是数字则
                	{
                	    oldstatus = status;
                	    status  = STATUS_NUMCONST_BEGIN;	                	    
                	    number = curChar - '0' ;
                	    if(this.curpos == this.formulaChars.length -1)
                	    {
                	        obj[1] = new Integer(new Double(number).intValue());
                	        obj[2] = new Integer(OPT_TYPE_INTEGER) ;
                	        return obj;
                	    } 
                	    
                	    
                	}
                	break;
            	case STATUS_OPERATION_END://操作符结束
            	    if(curChar < '0' || curChar > '9')//如果不是数字
                    {
                	    if(curChar == OPER_LB)
                	    {
                	       
                	        //修改老状态为当前状态到
                	        oldstatus = status;
                	        //变量开始，修改当前状态为变量开始
                	        status = STATUS_VARIABLE_BEGIN;
                	        push(curChar,curpos);
                	        continue;
                	    }	                	    
                	    else if(curChar == PARENTHESES)//如果是分号，表示字符串开始
                	    {	                	        
                	        oldstatus = status;
                	        status = STATUS_STRCONST_BEGIN;
                	        push(curChar,curpos);
                	        continue;
                	    }
//                	    else if((curChar == OPER_RB || curChar == OPER_RP) && oldstatus != this.STATUS_OPERATION_END) //如果是‘}’,标识公式非法，抛异常信息
//                	    {
//                	        throw new FormulaException("Illegle formula start or need a oprator data:[" + curChar + "],index:["+ curpos + "]");
//                	    }
                	    else if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
        	            {
        	                continue;
        	            }
                	    else if(oldstatus == STATUS_NUMCONST_END ||
                	            oldstatus == STATUS_STRCONST_END ||
                	            oldstatus == STATUS_VARIABLE_END)
                	    {
                	        if(curChar == OPER_ADD ||
            	                curChar == OPER_DIV ||
                	            curChar == OPER_MOD ||
                	            curChar == OPER_MUL ||
                	            curChar == OPER_SUB )
                	            if(this.isMutiple(curChar))
                	                throw new FormulaException("Illegle formula operator symbol:multiple operator[" + curChar + "],index:["+ curpos + "]");
                	    }
                	    
                	    //获取操作码
                	    int code = this.getOperCode(curChar);                	    
                	    
                	    //如果是操作码返回操作码的内码
                	    if(code != -1)
                	    {
                	        oldstatus = status;
                	        status = STATUS_OPERATION_END;
                	        //检测如果是sum操作            
                	        if(curChar == OPER_LP)
                	            push(curChar,curpos);
                	        else if(curChar == OPER_RP)
                	        {
                	            
                	            pop(curChar,curpos);
                	            
                	        }
                	        obj[0] = new Integer(code);
                	        return obj;
                	    }
                	    else //否则抛出异常信息
                	    {
                	        throw new FormulaException("Illegle operator start:[" + curChar + "],index:["+ curpos + "]");                	         
                	    }
                	    //throw new FormulaException("Illegle formula start:[" + curChar + "],index:["+ curpos + "]");
                    }
                	else //如果是数字则
                	{
                	    oldstatus = status;
                	    status  = STATUS_NUMCONST_BEGIN;	                	    
                	    number = curChar - '0' ;
                	    if(this.curpos == this.formulaChars.length -1)
                	    {
                	        obj[1] = new Integer(new Double(number).intValue());
                	        obj[2] = new Integer(OPT_TYPE_INTEGER) ;
                	        return obj;
                	    } 
                	}
                	break;
                	
            	case STATUS_VARIABLE_BEGIN://变量开始                	
            	    if(curChar == OPER_RB )
            	    {
            	        if(token.length() == 0)
            	        {
            	            pop(curChar,curpos);
            	            throw new FormulaException("Illegle variable end:[" + curChar + "],index:["+ curpos + "]");
            	        }
                	    else
                	    {
                	        oldstatus = status;
                	        status = STATUS_VARIABLE_END;
                	        obj[1] = token;
                	        obj[2] = new Integer(OPT_TYPE_VARIABLE);
                	        pop(curChar,curpos);
                	        return obj;	                	        
                	    }
                	        
            	    }
            	    else if(curChar == OPER_ADD || 
            	            curChar == OPER_DIV ||
            	            curChar == OPER_MOD ||
            	            curChar == OPER_MUL ||
            	            curChar == OPER_SUB ||
            	            curChar == OPER_LP  ||
            	            curChar == OPER_RP ||
            	            curChar == OPER_LB 
            	            
            	            )
            	        throw new FormulaException("Illegle variable define:[" + curChar + "],index:["+ curpos + "]");
            	        
            	    else if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
    	            {
    	                continue;
    	            }
            	        
            	    token += curChar;            	    
            	    continue;
            	case STATUS_NUMCONST_BEGIN://数字开始
            	    if('0' <= curChar && curChar <= '9')
            	    {
            	        number = number * RADIX + (curChar - '0');
            	        integerNum ++;
            	        if(this.curpos == this.formulaChars.length -1)
                	    {
                	        obj[1] = new Integer(new Double(number).intValue());
                	        obj[2] = new Integer(OPT_TYPE_INTEGER) ;
                	        return obj;
                	    } 
            	    }
            	    else if(curChar == '.') //小数位开始
            	    {
            	        this.oldstatus = status;
            	        status = STATUS_FNUMCONST_BEGIN;
            	        dradix = 1.0/RADIX;
            	        continue;
            	    }  
            	    else if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
    	            {
    	                continue;
    	            }
            	    else //获取数字结束
            	    {
            	        this.oldstatus = status;
            	        this.status = STATUS_NUMCONST_END;           	        
            	        
            	        curpos --;
            	        
            	        obj[1] = new Integer(new Double(number).intValue());
            	        obj[2] = new Integer(OPT_TYPE_INTEGER) ;
            	        return obj;
            	    }
            	    continue;
            	 case STATUS_FNUMCONST_BEGIN://获取小数位数值
            	     if('0' <= curChar && curChar <= '9')
            	     {
            	         number = number + (curChar - '0') * dradix;
            	         dradix /= RADIX;            
            	         dNum ++;
            	         if(this.curpos == this.formulaChars.length -1)
                 	    {
            	             String formats = "";
                 	         for(int i = 0; i < integerNum; i ++)
                 	         {
                 	             formats += "#";
                 	         }
                 	         if(dNum != 0)
                 	             formats += ".";
                 	         for(int i = 0; i < dNum; i ++)
                 	         {
                 	             formats += "#";
                 	         }
                 	         DecimalFormat formart = new DecimalFormat(formats);
                 	         number = Double.parseDouble(formart.format(number));
                	         obj[1] = new Double(number);
                 	         obj[2] = new Integer(OPT_TYPE_DOUBLE);
                 	         return obj;
                 	    } 
            	         continue;
            	     }
            	     else if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
     	             {
     	                 continue;
     	             }
            	     else //如果出现非数字则数值获取结束
            	     {
            	         this.oldstatus = status;
            	         this.status = STATUS_NUMCONST_END;
            	         //定义数据得格式，解决重新够建double类型得数据小数部分数据值漂移得问题
            	         //例如：数23.23334通过解析后会变为23.23333999999999999999
            	         //所以通过记录数得整数位数和小数位数，
            	         //然后根据这两个数字够建格式化串##.#####对解析后得数字进行格式化,结果为正确得原来得数字
            	         //
            	         String formats = "";
             	         for(int i = 0; i < integerNum; i ++)
             	         {
             	             formats += "#";
             	         }
             	         if(dNum != 0)
             	             formats += ".";
             	         for(int i = 0; i < dNum; i ++)
             	         {
             	             formats += "#";
             	         }
             	         DecimalFormat formart = new DecimalFormat(formats);
             	         number = Double.parseDouble(formart.format(number));
            	         obj[1] = new Double(number);
             	         obj[2] = new Integer(OPT_TYPE_DOUBLE);   
             	         
             	         
             	         curpos --;
             	         return obj;
            	     }
            	     
            	 case STATUS_STRCONST_BEGIN:
            	     if(curChar == PARENTHESES)
            	     {
            	         this.oldstatus = status;
            	         this.status = STATUS_STRCONST_END;
            	         obj[1] = token;
            	         obj[2] = new Integer(OPT_TYPE_STRING);
            	         pop(curChar,curpos);
            	         return obj;            	         
            	     }
//            	     else if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
//     	             {
//     	                 continue;
//     	             }
            	     else            	         
            	     {
            	         token += curChar  ;
            	         continue;
            	     }
            	 case STATUS_NUMCONST_END:
            	 case STATUS_VARIABLE_END:
            	 case STATUS_STRCONST_END:    
            	     if(curChar == OPER_ADD || //
        	            curChar == OPER_DIV || 
        	            curChar == OPER_MOD ||            	             
        	            curChar == OPER_MUL || 
        	            curChar == OPER_POW ||
        	            curChar == OPER_SUB ||
        	            curChar == OPER_RP
            	      )
            	     {
            	         int code = getOperCode(curChar);
            	         obj[0] = new Integer(code);
            	         oldstatus = status;
            	         this.status = STATUS_OPERATION_END;
            	         if(curChar == OPER_RP)
            	         {
            	             pop(curChar,curpos);
            	         }
            	         return obj;
            	         
            	     }
            	     else if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
     	             {
     	                 continue;
     	             }
            	     else 
            	         throw new FormulaException("Illegle operator symbol :[" + curChar + "],index:["+ curpos + "]");
            }   
        }
       
        throw new FormulaException("illegal formula end:[" + (char)curChar + "],index:["+ curpos + "]");
    }
    
    /**
     * 判断一个运算符号是否重复出现多次
     * @param curChar
     * @return boolean
     */
    private boolean isMutiple(char curChar)
    {
        int code = this.getOperCode(curChar);
        Object[] temp = (Object[])tokens.get(tokens.size() - 1);
        int t_code = ((Integer)temp[0]).intValue();
        return code == t_code;        
    }
	/**
	 * 获取操作符内码
	 * @param oper
	 * @return int
	 */
    private int getOperCode(char oper)
    {
        int ret = -1;
        for(int i = 0; i < operCodes.length; i ++)
        {
            if(operCodes[i].getOper() == oper)
            {
                ret = operCodes[i].getCode();
                break;
            }
            
        }
        if(ret != -1)
        {
            if(oper == 'a')
            {
                if(isAvg(oper))
                    return ret;
                else
                    return -1;
            }
            else if(oper == 's')
            {
                if(isSum(oper))
                    return ret;
                else
                    return -1;
            }else if(oper == 'c')
            {
                if(isCount(oper))
                    return ret;
                else
                    return -1;
            }   
        }
        return ret;
         
    }
    
    /**
	 * 检测是否是求平均函数
	 * @return boolean
	 */
    private boolean isAvg(char curChar)
    {             
        boolean ret = false;
        if(curChar == 'a')
        {
            curpos ++;
            curChar = this.formulaChars[curpos];            
	        if(curChar == 'v')
	        {
	            curpos ++;
	            curChar = this.formulaChars[curpos];
	            if(curChar == 'g')
	            {
	                curpos ++;
	                //int temp = 0;
	                for(; curpos < this.formulaChars.length; curpos ++)
	                {
	                    //temp ++;
	                    curChar = formulaChars[curpos];	                    
	                    if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
	                        continue;
	                    else if(curChar == OPER_LP)
	                    {	
	                        curpos --;
	                        ret = true;
	                        break;
	                    }			                    
	                }
	            }	           
	            
	        }	        	        
        }
        return ret;
    }
    
    /**
	 * 检测是否是计数函数
	 * @return boolean
	 */
    private boolean isCount(char curChar)
    {                
        boolean ret = false;
        if(curChar == 'c')
        {
            curpos ++;
            curChar = this.formulaChars[curpos];            
	        if(curChar == 'o')
	        {
	            curpos ++;
	            curChar = this.formulaChars[curpos];
	            if(curChar == 'u')
	            {
	                curpos ++;
		            curChar = this.formulaChars[curpos];
		            if(curChar == 'n')
		            {
		                curpos ++;
			            curChar = this.formulaChars[curpos];
			            if(curChar == 't')
			            {			                
			                curpos ++;
			                //int temp = 0;
			                for(; curpos < this.formulaChars.length; curpos ++)
			                {
			                    //temp ++;
			                    curChar = formulaChars[curpos];	                    
			                    if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
			                        continue;
			                    else if(curChar == OPER_LP)
			                    {	
			                        curpos --;
			                        ret = true;
			                        break;
			                    }			                    
			                }
			                
			            }
		            }
	            }
	        }	        	        
        }        
        return ret;
    }
	/**
	 * 检测是否是求和函数
	 * @return boolean
	 */
    private boolean isSum(char curChar)
    {  
        boolean ret = false;
        if(curChar == 's')
        {
        	curpos ++;
            curChar = this.formulaChars[curpos];
	        if(curChar == 'u')
	        {
	            curpos ++;
	            curChar = this.formulaChars[curpos];
	            if(curChar == 'm')
	            {                
	                curpos ++;
	                //int temp = 0;
	                for(; curpos < this.formulaChars.length; curpos ++)
	                {
	                    //temp ++;
	                    curChar = formulaChars[curpos];	                    
	                    if(curChar == ' ' || curChar == '\r' || curChar == '\n' || curChar == '\t')
	                        continue;
	                    else if(curChar == OPER_LP)
	                    {	
	                        curpos --;
	                        ret = true;
	                        break;
	                    }	                    
	                }
	            }	            
	        }
        }
        return ret;
    }
    
           
    /**
     * 分析公式词法和语法结构
     * @throws FormulaException
     */
    private void parser() throws FormulaException
    {   
        if(!parsered )
        {
        	tokens = new ArrayList();
        	for(;curpos < this.formulaChars.length; curpos ++)
        	{        	
        		Object[] token  = this.getToken(); 
        		if(token  != null)
        		tokens.add(token);
        	}
        	if(!operStackTrace.isEmpty())
        	    throw new FormulaException("illegal formula end:operator symbol not matched.");
        	parsered = true;
        	
        }
    }
    
    /**
     * 根据操作符内码获取操作符,如果是'a','s','c'将分别返回'avg','sum','count'
     * @param code
     * @return String
     */
    private String getOPER(int code)
    {
        char oper = ' ';
        for(int i = 0; i < operCodes.length; i ++)
        {
            if(operCodes[i].getCode() == code)
            {
                oper = operCodes[i].getOper();
                break;
            }
            
        }        
        if(oper == 'a')
        {                
            return "avg";                
        }
        if(oper == 's')
        {
            return "sum";
        }
        if(oper == 'c')
        {
            return "count";
        }   
        
        return oper + "";
    }
    
    public String toString()
    {
        StringBuffer ret = new StringBuffer();
        for(int j = 0;j < tokens.size(); j ++)
    	{        	
    		Object[] token  = (Object[])tokens.get(j); 
    		
    		{
    		    if(token[0] != null)
    		    {
    		        int code = ((Integer)token[0]).intValue();
    		        ret.append(getOPER(code));
    		    }
    		    else
    		    {
    		        int type = ((Integer)token[2]).intValue();
    		        if(type == OPT_TYPE_STRING)
    		            ret.append("\"").append(token[1].toString()).append("\"");
    		        else if(type == OPT_TYPE_VARIABLE)
    		            ret.append("{").append(token[1].toString()).append("}");
    		        else
    		            ret.append(token[1].toString());
    		    }    		 
    		}    		
    	}
        return ret.toString();
    }
    
    /**
     * 聚合数据集中变量的值，目前系统提供了3种聚合函数：sum(求和),avg（求平均值）,count(计数)
     * @param variableName  变量可能为单个变量，也可能为复合变量，复合变量以'.'分隔
     * @return 类型为Object的值对象
     * @throws FormulaException
     */
     
    private Object evalVariableValue(int oper,String variableName,PagerDataSet dataSet) throws FormulaException
    {        
//        if(true)
//            return new Object[] {null, new Double(50),new Integer(OPT_TYPE_DOUBLE)};
        if(this.dataSet == null || dataSet.size() == 0)
            throw new FormulaException("evaluate variablevalue exception:dataSet=null or dataSet.size=0,[" + variableName + "]");
        String[] variables =  parseVariable(variableName);
        
        //判别是否是复合变量，
        //如果是则检测变量名称中是否包含数据集合索引，
        //如果包含索引则取出对应的数据集合再计算变量的值
         
        if(variables.length > 1)
	        try
	        {
	            int index = 0;
	            index = Integer.parseInt(variables[0]);
	            if(index < 0)
	                throw new FormulaException("error varialbName named:'" + variableName + "' index < 0!");
	            PagerDataSet dataSet_t = dataSet.getPagerDataSet(index);
	            String newVariable = variables[1];
	            for(int i = 2; i < variables.length; i ++)
	            {
	                newVariable += "." + variables[i]; 
	            }
	            return evalVariableValue(oper,newVariable,dataSet_t);
	        }
	        catch(Exception e)
	        {
	            
	        }
        switch(oper)
        {
        	case OPER_SUM:
        	    if(variables.length == 1)
                {        	        
        	        return new Object[] {null, new Double(dataSet.sum(variables[0]).toString()),new Integer(OPT_TYPE_DOUBLE)};        	        
                }
        	    else
        	    {   
        	        return new Object[] {null, new Double(dataSet.sum(variables[0],variables[1]).toString()),new Integer(OPT_TYPE_DOUBLE)};        	        
        	    }
        	case OPER_AVG:
        	    if(variables.length == 1)
                {
        	        return new Object[] {null, new Double(dataSet.avg(variables[0])),new Integer(OPT_TYPE_DOUBLE)};        	        
                }
        	    else
        	    {        	        
        	        return new Object[] {null,new Float(dataSet.avg(variables[0],variables[1])),new Integer(OPT_TYPE_DOUBLE)};        	        
        	    }
        	    
        	case OPER_COUNT:
        	    if(variables.length == 1)
                {        	         
        	        return new Object[] {null,new Integer(dataSet.count(variables[0])),new Integer(OPT_TYPE_INTEGER)};
                }
        	    else
        	    {
        	        return new Object[] {null,new Integer(dataSet.count(variables[0],variables[1])),new Integer(OPT_TYPE_INTEGER)};        	        
        	    }
        }
        throw new FormulaException("error operation code:" + oper + ",attribute["+ variableName +"]");
    }
    
    /**
     * 获取变量的值
     * @param variableName  变量可能为单个变量，也可能为复合变量，复合变量以'.'分隔
     * @return 类型为Object的值对象
     * @throws FormulaException
     */
     
    private Object evalVariableValue(PagerDataSet dataSet,String variableName) throws FormulaException
    {        
//        if(true)
//            return new Integer(5);
        if(this.dataSet == null || dataSet.size() == 0)
            throw new FormulaException("evaluate variablevalue exception:dataSet=null or dataSet.size=0");
        String[] variables =  parseVariable(variableName);
        if(variables.length == 1)
        {
        	if(variables[0].equals("rowid"))
            {
            	return new Integer(dataSet.getOuterRowid(true,0));
            }
        	else if(variables[0].equals("offset"))
        	{
        		return new Integer(dataSet.getOffset());
        	}
        	else if(variables[0].equals("rowcount"))
        	{
        		return dataSet.getRowcount();
        	}
        	else if(variables[0].equals("pagesize"))
        	{
        		return dataSet.getPageSize();
        	}
        	else if(variables[0].equals("mapkey"))//
        	{
        		return dataSet.getMapKey();
        	}
        	else if(variables[0].equals("currentcell"))
        	{
        		return dataSet.getObject();
        	}
        	Object data = dataSet.getValue(dataSet.getRowid(),variables[0]);
            if(data == null)
                throw new FormulaException("attribute '" + variableName + "' is null!");
            return data;
        }
        else
        {
            //如果是复合变量，首先检测第一个变量是否为数字，如果是则表示要获取索引所对应的数据集中当前行对应的记录的属性值
            //否则直接从当前数据集中获取变量值
            int index = 0;
            try
            {
                index = Integer.parseInt(variables[0]);
                if(index < 0)
                {
                    throw new FormulaException("error varialbName named:'" + variableName + "' index < 0!");
                }
                PagerDataSet dataSet_t = dataSet.getPagerDataSet(index);
                /**
                 * 如果是获取当前列表或者分页的行号
                 */
                if(variables[1].equals("rowid"))
                {
                	return new Integer(dataSet_t.getOuterRowid(true,0));
                }
                else if(variables[1].equals("offset"))
            	{
            		return new Integer(dataSet_t.getOffset());
            	}
                else if(variables[1].equals("rowcount"))
            	{
            		return dataSet_t.getRowcount();
            	}
                else if(variables[1].equals("pagesize"))
            	{
            		return dataSet_t.getPageSize();
            	}
            	else if(variables[1].equals("mapkey"))//
            	{
            		return dataSet_t.getMapKey();
            	}
            	else if(variables[1].equals("currentcell"))
            	{
            		return dataSet_t.getObject();
            	}
                Object t = dataSet_t.getValue(dataSet_t.getRowid(),variables[1]);
                for(int i = 2; i < variables.length; i ++)
                {
                	t = ValueObjectUtil.getValue(t,variables[i]);
                	if(t == null)
            		{
            			 throw new FormulaException("attribute [" + variableName + ">" + variables[i] + "] is null!");
            		}
                }
                return t;
             
            }
            catch(Exception e)
            {
                
            }

        	Object t = null;
        	try
        	{
            	t = dataSet.getValue(dataSet.getRowid(),variables[0]);
            	 /**
                 * 如果是获取当前列表或者分页的行号
                 */
                if(variables[0].equals("rowid"))
                {
                	return new Integer(dataSet.getOuterRowid(true,0));
                }
                else if(variables[0].equals("offset"))
            	{
            		return new Integer(dataSet.getOffset());
            	}
                else if(variables[0].equals("rowcount"))
            	{
            		return dataSet.getRowcount();
            	}
                else if(variables[0].equals("pagesize"))
            	{
            		return dataSet.getPageSize();
            	}
            	else if(variables[0].equals("mapkey"))//
            	{
            		return dataSet.getMapKey();
            	}
            	else if(variables[0].equals("currentcell"))
            	{
            		return dataSet.getObject();
            	}
            	for(int i = 1; i < variables.length; i ++)
            	{
            		t = ValueObjectUtil.getValue(t,variables[i]);
            		if(t == null)
            		{
            			 throw new FormulaException("attribute [" + variableName + ">" + variables[i] + "] is null!");
            		}
            	}
            	return t;
        	}
        	catch(FormulaException e)
        	{
        		throw e;
        	}
        	catch(Exception e)
        	{
        		throw new FormulaException("eval attribute '" + variableName + "' value failed:",e);
        	}
            
        }        
    }
    
    /**
     * 解析公式中变量，如果变量为组合变量，将其分解为单个变量，将单个变量存放到数组中
     * @param variableName
     * @return 返回存放变量的数组
     */
    private String[] parseVariable(String variableName)
    {
        String ret[] = StringUtil.split(variableName,"\\.");
        return ret;
    }
    
    /**
     * 计算表达式的值
     * 暂时没有考虑大数的加减乘除运算，在后续工作中补充这些功能
     * @param oper
     * @param left
     * @param right
     * @return Object
     * @throws FormulaException
     */
    private Object eval(int oper,Object left,Object right) throws FormulaException
    {   
        Object[] ret = new Object[3];
        Object left_value = ((Object[])left)[1];
        Object right_value = ((Object[])right)[1];
        int left_type = ((Integer)((Object[])left)[2]).intValue();
        String temp_l = left_value.toString();
        String temp_r = right_value.toString();
        if(left_type == OPT_TYPE_VARIABLE)//如果操作数1是变量计算变量值
        {
            temp_l = left_value.toString();
            left_value = evalVariableValue(dataSet,left_value.toString());
            if(left_value == null)
                throw new FormulaException("attribute '" + temp_l + "' is null!");
        }
        int right_type = ((Integer)((Object[])right)[2]).intValue();
        if(right_type == OPT_TYPE_VARIABLE)//如果操作数2是变量计算变量值
        {            
            right_value = evalVariableValue(dataSet,right_value.toString());
            if(right_value == null)
                throw new FormulaException("attribute '" + temp_r + "' is null!");
        }
        
	    if(String.class.isInstance(left_value)  || String.class.isInstance(right_value))
	    {
	        String lv = left_value.toString();        	     
	        String rv = right_value.toString();        	     
	        switch(oper)
	        {
	        	case CODE_ADD:
	        	    ret[1] = lv + rv;
	        	    ret[2] = new Integer(OPT_TYPE_STRING);
	        	    return ret;
	        	case CODE_DIV:
	        	case CODE_MOD:
	        	case CODE_MUL:
	        	case CODE_POW:
	        	case CODE_SUB:
	        	    throw new FormulaException("parameter type error for operation:String type can't be used for add,sub,mul,mod,div,pow![" + temp_l + "],[" + temp_r + "]");
	        }
	    }        	    
	    else if(Float.class.isInstance(left_value)  || Float.class.isInstance(right_value))
	    {
	        float lv = Float.class.isInstance(left_value) ?(Float)left_value:new Float(left_value.toString()).floatValue();
	        float rv = Float.class.isInstance(right_value)?(Float)right_value:new Float(right_value.toString()).floatValue();
	        switch(oper)
	        {
	        	case CODE_ADD:
	        	    ret[1] = new Double(lv + rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	case CODE_DIV:
	        	    ret[1] = new Double(lv/rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	case CODE_MOD:
	        	    ret[1] = new Double(lv%rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	    
	        	case CODE_MUL:
	        	    ret[1] = new Double(lv*rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	    
	        	case CODE_POW:
	        	    float ret_f = 1.0f;
	        	    for(int i = 0; i < (int)rv; i ++)
	        	        ret_f *= lv;
	        	    ret[1] = new Double(ret_f);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;        	        	    
	        	case CODE_SUB:
	        	    ret[1] = new Double(lv - rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;	        	    
	        }
	        
	    }        	    
	    else if(Double.class.isInstance(left_value)  || Double.class.isInstance(right_value))
	    {
	        double lv = Double.class.isInstance(left_value)?(Double)left_value:new Double(left_value.toString()).doubleValue();
	        double rv = Double.class.isInstance(right_value)?(Double)right_value:new Double(right_value.toString()).doubleValue();
	        
	        switch(oper)
	        {
	        	case CODE_ADD:
	        	    ret[1] = new Double(lv + rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	case CODE_DIV:
	        	    ret[1] = new Double(lv / rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	case CODE_MOD:
	        	    ret[1] = new Double(lv % rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	case CODE_MUL:
	        	    ret[1] = new Double(lv * rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	case CODE_POW:
	        	    double ret_d = 1.0d;
	        	    for(int i = 0; i < (int)rv; i ++)
	        	        ret_d *= lv;
	        	    ret[1] = new Double(ret_d);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;
	        	case CODE_SUB:
	        	    ret[1] = new Double(lv - rv);
	        	    ret[2] = new Integer(OPT_TYPE_DOUBLE);
	        	    return ret;        	        	  
	        }
	        
	    }
	    else if(Long.class.isInstance(left_value)  || Long.class.isInstance(right_value))
	    {
	        long lv = Long.class.isInstance(left_value)?(Long)left_value:new Long(left_value.toString()).longValue();
	        long rv = Long.class.isInstance(right_value)?(Long)right_value:new Long(right_value.toString()).longValue();
	        
	        switch(oper)
	        {
	        	case CODE_ADD:
	        	    ret[1] = new Long(lv + rv);
	        	    ret[2] = new Integer(OPT_TYPE_LONG);
	        	    return ret;
	        	case CODE_DIV:
	        	    ret[1] = new Long(lv / rv);
	        	    ret[2] = new Integer(OPT_TYPE_LONG);
	        	    return ret;
	        	case CODE_MOD:
	        	    ret[1] = new Long(lv % rv);
	        	    ret[2] = new Integer(OPT_TYPE_LONG);
	        	    return ret;
	        	case CODE_MUL:
	        	    ret[1] = new Long(lv * rv);
	        	    ret[2] = new Integer(OPT_TYPE_LONG);
	        	    return ret;
	        	case CODE_POW:
	        		long ret_d = 1;
	        	    for(int i = 0; i < (int)rv; i ++)
	        	        ret_d *= lv;
	        	    ret[1] = new Long(ret_d);
	        	    ret[2] = new Integer(OPT_TYPE_LONG);
	        	    return ret;
	        	case CODE_SUB:
	        	    ret[1] = new Long(lv - rv);
	        	    ret[2] = new Integer(OPT_TYPE_LONG);
	        	    return ret;        	        	  
	        }
	        
	    }
	    
	    else if(Integer.class.isInstance(left_value)  || Integer.class.isInstance(right_value))
	    {
	        int lv = Integer.class.isInstance(left_value)?(Integer)left_value:((Integer)left_value).intValue();
	        int rv = Integer.class.isInstance(right_value)?(Integer)right_value:((Integer)right_value).intValue();
	        switch(oper)
	        {
	        	case CODE_ADD:
	        	    ret[1] = new Integer(lv + rv);
	        	    ret[2] = new Integer(OPT_TYPE_INTEGER);
	        	    return ret;
	        	case CODE_DIV:
	        	    ret[1] = new Integer(lv / rv);
	        	    ret[2] = new Integer(OPT_TYPE_INTEGER);
	        	    return ret;
	        	case CODE_MOD:
	        	    ret[1] = new Integer(lv % rv);
	        	    ret[2] = new Integer(OPT_TYPE_INTEGER);
	        	    return ret;
	        	case CODE_MUL:
	        	    ret[1] = new Integer(lv * rv);
	        	    ret[2] = new Integer(OPT_TYPE_INTEGER);
	        	    return ret;
	        	case CODE_POW:
	        	    int ret_i = 1;
	        	    for(int i = 0; i < (int)rv; i ++)
	        	        ret_i *= lv;
	        	    ret[1] = new Integer(ret_i);
	        	    ret[2] = new Integer(OPT_TYPE_INTEGER);
	        	    return ret;
	        	case CODE_SUB:
	        	    ret[1] = new Integer(lv - rv);
	        	    ret[2] = new Integer(OPT_TYPE_INTEGER);
	        	    return ret;     	        	  
	        }
	    }
	    
	    else
	        throw new FormulaException("parameter type error for add|sub|mul|div|mod operation![" + temp_l + "],[" + temp_r + "]");        	    
        
	    throw new FormulaException("parameter type error for add|sub|mul|div|mod operation![" + temp_l + "],[" + temp_r + "]");
    }
    
    /**
     * 计算表达式的值(单目运算)
     * @param oper
     * @param left     
     * @return Object
     * @throws FormulaException
     */
    private Object eval(int oper,Object left) throws FormulaException
    {
        return evalVariableValue(oper,left.toString(),dataSet);
    }
    
    public static void main(String[] args) throws FormulaException
    {   
        //Formula f = new Formula(null,"(2 * 2 * sum({value1}))/avg({value2})");
        Formula f = new Formula(null,"(2 * 2 * sum({value1}))");
        //Formula f = new Formula(null,"2 * 2");
        f.parser();
        
        System.out.println(f.toString());
        System.out.println("result:" + f.eval());
        
        
        
    }
    
    /**
     * 返回计算公式的 值
     * @return Object
     */
    public Object getValue() throws FormulaException
    {   
        Object value = eval(); 
        return value;
    }
    
    /**
     * 根据操作符计算值
     * @param code
     * @throws FormulaException
     */
    private void eval(int code) throws FormulaException
    {
        //操作数1
        Object opt1;
        //操作数2
        Object opt2;
        //判断操作是否是聚合操作
        if(!operCodes[code - 1].isAggregation())//如果是聚合操作
        {
            //操作数2出栈
            opt2 = operationParms.pop();
            //操作数1出栈
            opt1 = operationParms.pop();
            
            //计算结果
            Object value = eval(code,opt1,opt2);
            opt1 = null;
            opt2 = null;
            //将所得的结果进操作数堆栈
            
            operationParms.push(value);	                           
        }
        else
        {
//          操作数2出栈
            opt2 = operationParms.pop();
            //计算结果
            Object value = eval(code,opt2);	                            
            opt2 = null;
            //将所得的结果进操作数堆栈
            operationParms.push(value);	                           
        }
    }
    /**
     * 通过本方法求得表达式得值
     * @return Object
     */
    private  Object eval() throws FormulaException
    {
        this.operationParms = new Stack();
        this.operations = new Stack();        
        parser();
        if(tokens == null || tokens.size() == 0)
            return null;
//        //标识表达式求值开始：false，未开始，true：开始
//        boolean start = false;
        int curcode;
        int outpirior;
        int topcode ;
        int inpirior;
       
        
label1: for(int i = 0; i < tokens.size(); i ++)
        {
            Object[] token = (Object[])tokens.get(i);
            if(token[0] != null)//是操作符
            {
                if(operations.isEmpty())
                {
                    operations.push(token[0]);                    
                    continue label1;
                }
                curcode = ((Integer)token[0]).intValue();
                outpirior = outPriorTable[curcode - 1];
                topcode = ((Integer)operations.peek()).intValue();
                inpirior = inPriorTable[topcode - 1];                
                if(outpirior > inpirior)//如果栈外操作符比栈内操作符优先权高
                {
                    operations.push(token[0]);                    
                }   
                else if(outpirior <= inpirior)//如果栈外操作符比栈内操作符优先权低或者相等时
                {
                    //操作栈顶元素出栈，得到操作符内码
                    int temp ;                    
                    do
                    {
                        //操作栈顶元素出栈
                        temp = ((Integer)operations.pop()).intValue();
                        //如果当前得操作符是运算符则执行相应得运算;
                        if(operCodes[temp - 1].isOperator())
                        {
                            eval(temp);
                        }
                        else if(operCodes[temp - 1].getOper() == OPER_LP || operCodes[temp - 1].getOper() == OPER_LB)
                        {
                            continue label1;
                        }
                        
                        //如果堆栈为空，栈外操作符进栈
                        if(operations.isEmpty())
                        {
                            operations.push(token[0]);
                            continue label1;
                        }
//                      获取操作栈顶元素
                        topcode = ((Integer)operations.peek()).intValue();                            
                        inpirior = inPriorTable[topcode - 1];
                    }
                    while(outpirior <= inpirior);//执行计算操作直到
                    
                    if(outpirior > inpirior)
                    {
	                    if(operCodes[temp - 1].getOper() == OPER_RP || operCodes[temp - 1].getOper() == OPER_RB)
	                        operations.pop();
	                    else
	                        operations.push(token[0]);
                    }   
                }   
            }
            else//操作数
                this.operationParms.push(token);
            
        }
        
        //当所有的操作符和操作数都已经进栈，并且没有运算完成，继续进行计算
        while(!operations.isEmpty())
        {
            int temp = ((Integer)operations.pop()).intValue();
            if(operCodes[temp - 1].isOperator())
            {
                eval(temp);
            }
            
        }
        //弹出最后的参数，由于'{' 和 '}'没有作为操作符号处理，可能出现操作数类型为变量的情况，因此需要
        //加以判别，当为变量时计算变量的值
        Object[] last_param = ((Object[])operationParms.pop());
        Object left_value = last_param[1];        
        int left_type = ((Integer)(last_param[2])).intValue();
        if(left_type == OPT_TYPE_VARIABLE)//如果操作数1是变量计算变量值
        {            
            left_value = evalVariableValue(dataSet,left_value.toString());
        }
        
        //返回运算结果
        return left_value;
    }
    
    /**
     * 操作符号和操作内码封装对象
     * @author biaoping.yin
     * created on 2005-7-4
     * version 1.0
     */
    private static class OPERCode
    {
        /**操作符号*/
        private char oper;
        /**操作符号内码*/
        private int code;
        
        /**
         * 够建函数
         * @param oper
         * @param code
         */
        public OPERCode(char oper, int code)
        {
            this.oper = oper;
            this.code = code;
        }
        /**
         * @return Returns the code.
         */
        public int getCode() {
            return code;
        }
        /**
         * @param code The code to set.
         */
        public void setCode(int code) {
            this.code = code;
        }
        /**
         * @return Returns the oper.
         */
        public char getOper() {
            return oper;
        }
        /**
         * @param oper The oper to set.
         */
        public void setOper(char oper) {
            this.oper = oper;
        }
        
        /**
         * 判别操作码是否是运算符
         * 
         * @return boolean
         */
        public boolean isOperator()
        {
            return oper == OPER_SUM || 
            		oper == OPER_COUNT ||
            		oper == OPER_AVG ||
            		oper == OPER_SUB ||
            		oper == OPER_ADD ||
            		oper == OPER_MOD ||
            		oper == OPER_DIV ||
            		oper == OPER_MUL ||
            		oper == OPER_POW;   
            		
        }
        
        /**
         * 判别操作码是否是运算符
         * 
         * @return boolean
         */
        public boolean isAggregation()
        {
            return oper == OPER_SUM || 
            		oper == OPER_COUNT ||
            		oper == OPER_AVG;   
            		
        }
    }

    /**
     * 记录操作符位置信息
     * @author biaoping.yin
     * created on 2005-7-6
     * version 1.0
     */
    private static class OPERPOS
    {
        
        private int curpos;
        private char oper;
        public OPERPOS(char oper,int curpos)
        {
            this.oper = oper;
            this.curpos = curpos;
        }
        /**
         * @return Returns the curpos.
         */
        public int getCurpos() {
            return curpos;
        }
        /**
         * @param curpos The curpos to set.
         */
        public void setCurpos(int curpos) {
            this.curpos = curpos;
        }
        /**
         * @return Returns the oper.
         */
        public char getOper() {
            return oper;
        }
        
        public String toString()
        {
            return oper + ":" + curpos; 
        }
        /**
         * @param oper The oper to set.
         */
        public void setOper(char oper) {
            this.oper = oper;
        }
    }
}


