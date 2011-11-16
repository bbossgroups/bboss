<% StringBuffer buf = new StringBuffer();
buf.append("{")                                                      
	.append("\"total\":239,")                                                      
	.append("\"rows\":[")                                                      
	.append("{\"code\":\"001\",\"name\":\"Name 1\",\"addr\":\"Address 1\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"002\",\"name\":\"Name 2\",\"addr\":\"Address 2\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"003\",\"name\":\"Name 3\",\"addr\":\"Address 3\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"004\",\"name\":\"Name 4\",\"addr\":\"Address 4\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"005\",\"name\":\"Name 5\",\"addr\":\"Address 5\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"006\",\"name\":\"Name 6\",\"addr\":\"Address 6\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"007\",\"name\":\"Name 7\",\"addr\":\"Address 7\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"008\",\"name\":\"Name 8\",\"addr\":\"Address 8\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"009\",\"name\":\"Name 9\",\"addr\":\"Address 9\",\"col4\":\"col4 data\"},")                                                      
	.append("{\"code\":\"010\",\"name\":\"Name 10\",\"addr\":\"Address 10\",\"col4\":\"col4 data\"}")                                                      
	.append("]")                                                      
	.append("}"        );
	out.print(buf);
	System.out.println(buf);
	System.out.println(request.getParameter("name"));
	System.out.println(request.getParameter("value"));
	System.out.println("pageNumber:"+request.getParameter("page"));
	System.out.println("pageSize:"+request.getParameter("rows"));
	
%>
                                                   
