package project.arzeit.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;


import project.arzeit.database.DataSource;
import project.arzeit.model.AuthCModel;


/**
 * project/arzeit/register.htmlに対応するサーブレット
 * 登録押すとここに飛ぶ
 * 登録の結果を返す　0以外だったら失敗
 * @author Minoru Makino
 */
@WebServlet("/project/arzeit/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
        
        ServletContext context = this.getServletContext();
        AuthCModel authC = new AuthCModel((DataSource)context.getAttribute("dataSource"));

        int code = authC.register(request.getParameter("id"), request.getParameter("pass"), request.getParameter("name")); //登録してエラーコード返すだけ

        StringBuilder json = new StringBuilder("{ \"code\": \""); //コードをjsonデ返す
        json.append(code).append("\" }");

        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.append(json.toString());
        writer.flush();

	}
}
