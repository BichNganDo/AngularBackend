package servlets;

import common.Config;
import helper.HttpHelper;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import model.UserModel;

public class AdminServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String authenValue = HttpHelper.getCookie(request, "authen");
        String username = UserModel.INSTANCE.getAuthenCookie(authenValue);
        if (username != null && !"".equals(username)) {
            Map<String, Object> pageVariables = new HashMap<>();
            pageVariables.put("app_domain", Config.APP_DOMAIN);
            pageVariables.put("static_domain", Config.STATIC_DOMAIN);
            pageVariables.put("message", "hello word");

            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println(PageGenerator.instance().getPage("index.html", pageVariables));

            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendRedirect(Config.APP_DOMAIN + "/login");
        }

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
