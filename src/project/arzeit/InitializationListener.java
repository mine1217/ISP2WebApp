package project.arzeit;

import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContext;
   
@WebListener
public class InitializationListener implements ServletContextListener{
  
  public void contextInitialized(ServletContextEvent event){
    ServletContext context=event.getServletContext();
    context.setAttribute("dataSource", new DataSource()); // 登録処理
  }
   
  public void contextDestroyed(ServletContextEvent event){
    ServletContext context=event.getServletContext();
    context.removeAttribute("dataSource");  // 削除処理
  }

}