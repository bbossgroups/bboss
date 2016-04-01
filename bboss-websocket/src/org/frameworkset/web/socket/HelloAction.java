package org.frameworkset.web.socket;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by shanl on 14-3-2.
 */
@WebServlet(name="HelloAction" ,urlPatterns={"/HelloAction"},loadOnStartup=1,
        initParams={
                @WebInitParam(name="name",value="xiazdong"),
                @WebInitParam(name="age",value="20")
})
public class HelloAction extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletConfig config = getServletConfig();
        HttpSession session = request.getSession();
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("Hello world"+"<br />");
        out.println("name :"+config.getInitParameter("name")+"<br/>");
        out.println("session id :"+session.getId());
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
