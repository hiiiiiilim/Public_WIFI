

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DbTest;

/**
 * Servlet implementation class historyServlet
 */
@WebServlet("/historyServlet")
public class historyServlet extends HttpServlet {
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String lat = request.getParameter("lat");
	        String lnt = request.getParameter("lnt");

	        DbTest dbTest = new DbTest();
	        dbTest.historyInsert(lat, lnt);
	        System.out.println(lat);
	        response.sendRedirect("index.jsp");
	        
	}

}
