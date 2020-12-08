package project.arzeit.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import project.arzeit.database.DataSource;
import project.arzeit.model.AuthCModel;
import project.arzeit.model.ProfileModel;
import project.arzeit.model.User;

/**
 * project/arzeit/mypage.htmlに対応するサーブレット プロフィール更新
 * 
 * @author Minoru Makino
 */
@WebServlet("/project/arzeit/mypageUpdate")
public class MypageUpdateServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        ServletContext context = this.getServletContext();
        ProfileModel profile = new ProfileModel((DataSource) context.getAttribute("dataSource"));
        AuthCModel authC = new AuthCModel((DataSource) context.getAttribute("dataSource"));

        int code = 0; // エラーコード

        if (user == null) code = 30; // ユーザー無かったら30出す

        String updateName = (String) request.getParameter("updateName");
        String updateId = (String) request.getParameter("updateId");
        String id = user.getId();
        String name = user.getName();

        if (code == 0 && !name.equals(updateName)) code = profile.setName(id, updateName); //違いがあったときだけ変更する
        if (code == 0 && !id.equals(updateId)) code = authC.setId(id, updateId); //idは重複チェックがあるので最後に変更する

        if (code == 0) { //変更成功したらuserオブジェクトも変える
            user.setId(request.getParameter(updateId));
            user.setName(request.getParameter(updateName));
        }

        StringBuilder json = new StringBuilder("{ \"code\": \""); // json作る
        json.append(code).append("\""); // ステータスコード
        json.append("}");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.append(json.toString());
        writer.flush();
    }
}
