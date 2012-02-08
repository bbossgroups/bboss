<%
  javax.naming.Context context = new javax.naming.InitialContext();
  
             javax.naming.Context envContext = (javax.naming.Context) context.lookup("java:/comp/env");           
  javax.sql.DataSource datasource = (javax.sql.DataSource)envContext.lookup("LiferayPool");
  java.sql.Connection con = datasource.getConnection();
  out.println(con);
%>