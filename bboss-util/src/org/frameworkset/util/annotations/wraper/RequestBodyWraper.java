package org.frameworkset.util.annotations.wraper;

import org.frameworkset.util.annotations.AnnotationUtils;
import org.frameworkset.util.annotations.RequestBody;

public class RequestBodyWraper {
	/**
	 * 指定响应的数据类型
	 * @return
	 */
	private String datatype;
	/**
	 * 指定响应的数据编码字符集
	 * @return
	 */
	private String charset;
	public RequestBodyWraper(RequestBody body,Class data) {
		datatype = AnnotationUtils.converDefaultValue(body.datatype());
		if(datatype == null)
		{
			if(String.class.isAssignableFrom(data))
				datatype = "String";
			else
				datatype = "json";
		}
		charset =AnnotationUtils.converDefaultValue( body.charset());
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}

}
