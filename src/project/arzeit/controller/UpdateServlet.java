package project.arzeit.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import project.arzeit.database.DataSource;
import project.arzeit.model.ScheduleModel;
import project.arzeit.model.User;

/**
 * project/arzeit/mypage.htmlに対応するサーブレット プロフィール出すだけ
 * 
 * @author Minoru Makino
 */
@WebServlet("/project/arzeit/main")
public class MainServerl extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("user");
        ServletContext context = this.getServletContext();
        ScheduleModel schedule = new ScheduleModel((DataSource) context.getAttribute("dataSource"));

        int code = 0; // エラーコード


        //スケジュールのJSONリストを作る
        StringBuilder scheduleList = new StringBuilder("\"schedule\": [");

        if (user == null) code = 30; // ユーザー無かったら30出す

        if (code == 0) {
            SimpleEntry<ArrayList<String>, Integer> result;
            result = schedule.getScheduleAtMonth(user.getId(), request.getParameter("day"));
            code = result.getValue();
            if (!result.getKey().isEmpty() && code == 0) {
                ArrayList<String> scheduleJson = schedule.scheduleToJSON(result.getKey());

                for (String s : scheduleJson) {
                    scheduleList.append(s).append(", ");
                }
                scheduleList.setLength(scheduleList.length() - 2);
            }
        }

        scheduleList.append("]");

        System.out.println(scheduleList.toString());
        //スケジュールのリスト終わり


        //送信するJSON作成
        StringBuilder json = new StringBuilder("{ \"code\": \""); // json作る
        json.append(code).append("\""); // ステータスコード
        if (code == 0) {
            json.append(", \"name\": \"").append(user.getName()).append("\", ") // なまえ
                    .append(scheduleList); //スケジュールのリスト
        }
        json.append("}");
        System.out.println(json.toString());
        //JSON終わり

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.append(json.toString());
        writer.flush();
    }
}
