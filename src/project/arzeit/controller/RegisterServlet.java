package project.arzeit.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import project.arzeit.database.DBController;
import project.arzeit.database.DataSource;
import project.arzeit.model.AuthCModel;
import project.arzeit.model.User;

/**
 * project/arzeit/login.htmlに対応するサーブレット
 * ログインボタン押すとここに飛ぶ
 * 認証の結果を返したり 成功したらメインページに飛んだりする
 * @author Minoru Makino
 */
@WebServlet("/project/arzeit/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	

	}
}
