/**
 * 
 */
package org.frameworkset.security;

/**
 * @author yinbp
 *
 * @Date:2016-12-22 19:57:32
 */
public class CoderUtil {

	/**
	 * 
	 */
	public CoderUtil() {
		// TODO Auto-generated constructor stub
	}
	/**
     * 将参数字节数组转换为16进制值表示组合而成的字符串。
     * 
     * @param arrB 需要转换的byte数组
     * @return String 转换后的字符串
     * @throws Exception
     *             JAVA异常
     */
    public static String byteGrpToHexStr(byte[] arrB) throws Exception {
        int iLen = arrB.length;
        StringBuilder tempSB = new StringBuilder(iLen * 2);
        for (int i = 0; i < iLen; i++) {
            int intTmp = arrB[i];
            while (intTmp < 0) {
                intTmp = intTmp + 256;
            }
            if (intTmp < 16) {
                tempSB.append("0");
            }
            tempSB.append(Integer.toString(intTmp, 16));
        }
        return tempSB.toString();
    }

    /**
     * 将参数16进制值表示组合而成的字符串转换为字节数组。
     * 
     * @param strIn
     *            需要转换的字符串
     * @return byte[] 转换后的byte数组
     * @throws Exception
     *             JAVA异常
     */
    public static byte[] hexStrToByteGrp(String strIn) throws Exception {
        byte[] arrB = strIn.getBytes();
        int iLen = arrB.length;
        byte[] arrOut = new byte[iLen / 2];
        for (int i = 0; i < iLen; i = i + 2) {
            String strTmp = new String(arrB, i, 2);
            arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
        }
        return arrOut;
    }
}
