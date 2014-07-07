package com.instalogin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String callbackURL = null;
    	
        try {
            StringBuffer callbackURLbuffer = request.getRequestURL();
            int index = callbackURLbuffer.lastIndexOf("/");
            callbackURLbuffer.replace(index, callbackURLbuffer.length(), "").append("/callback");
            callbackURL = URLEncoder.encode(callbackURLbuffer.toString(), "UTF-8");
            
            String instagramClientId = getServletContext().getInitParameter("instagramClientId");
            
            String authURL = "https://api.instagram.com/oauth/authorize/?client_id=" + instagramClientId + 
            		"&redirect_uri=" + callbackURL + "&response_type=code";
            System.out.println("authURL: " + authURL);
            
            response.sendRedirect(authURL);

        } catch (Exception e) {
            throw new ServletException(e);
        }

    }
}
