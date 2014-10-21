package com.risedisplay.reports.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class NSCustProfileServlet extends HttpServlet {
	
	public static final Logger LOGGER = Logger.getLogger(NSCustProfileServlet.class.getName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		
		UserService userService = UserServiceFactory.getUserService();
	    User user = userService.getCurrentUser();
	    if (user == null) {
	    	// send to Google login page
	    	resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
	    	return;
	    }
	    
	    LOGGER.info(user.getAuthDomain());
	    LOGGER.info(user.getEmail());
	    
	    if(user.getEmail().toLowerCase().contains("risevision.com") || 
	    		user.getEmail().toLowerCase().contains("risedisplay.com") || 
	    		user.getEmail().toLowerCase().contains("m.farooq2000@gmail.com")){
//		    userService.createLogoutURL("/logout")
	    	req.getRequestDispatcher("/index.jsp").forward(req, resp);
	    }else {
	    	resp.getWriter().print("Invalid authentication");
	    }
	}
}
