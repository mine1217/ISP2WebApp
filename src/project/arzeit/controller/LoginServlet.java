package project.arzeit.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import project.arzeit.database.DataSource;
import project.arzeit.model.AuthCModel;
import project.arzeit.model.User;

/**
 * project/arzeit/login.htmlに対応するサーブレット ログインボタン押すとここに飛ぶ 認証の結果を返したり
 * 成功したらメインページに飛んだりする
 * 
 * @author Minoru Makino
 */
@WebServlet("/project/arzeit/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext context = this.getServletContext();
        AuthCModel authC = new AuthCModel((DataSource) context.getAttribute("dataSource"));
        String id = request.getParameter("id");

        int code = authC.login(id, request.getParameter("pass"));

        if (code == 0) request.getSession().setAttribute("user", new User(id)); // userインスタンスをセッションに保存する
        

        StringBuilder json = new StringBuilder("{ \"code\": \"");
        json.append(code).append("\" }");
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.append(json.toString());
        writer.flush();

    }
}
