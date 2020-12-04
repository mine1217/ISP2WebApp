package project.arzeit.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import project.arzeit.model.User;

/**
 * project/arzeit/mypage.htmlに対応するサーブレット
 * プロフィール出すだけ
 * @author Minoru Makino
 */
@WebServlet("/project/arzeit/mypage")
public class MypageServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
        String id = request.getParameter("id");
        User user = (User)request.getSession().getAttribute("user");
        int code = 0; //エラーコード

        if(user == null) code = 30; //ユーザー無かったら30出す
        request.getSession().setAttribute("user", new User(id)); //userインスタンスをセッションに保存する
        response.sendRedirect("/project/arzeit/main.html"); //mainページにリダイレクトする
        

        StringBuilder json = new StringBuilder("{ \"code\": \""); //json作る
        json.append(code).append("\", "). //ステータスコード
        append("\"id\": \"").append(user.getId()).append("\", ") //id
        .append("\"name\": \"").append(user.getName()).append("\"") //名前
        .append("}");

        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        writer.append(json.toString());
        writer.flush();
	}
}
