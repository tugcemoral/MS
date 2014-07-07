package com.instalogin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.instalogin.dao.InstagramUser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class CallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    
    private static JSONObject getWebContentFromURL(String httpURL, List <NameValuePair>  nvps) {
    	
    	HttpPost httpPost = null;
    	JSONObject json = null;
    	
    	try { 		
    		DefaultHttpClient httpclient = new DefaultHttpClient();
    		httpPost = new HttpPost(httpURL);    	
    		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
    		HttpResponse response = httpclient.execute(httpPost);
    		
		    System.out.println(response.getStatusLine());
		    System.out.println(response.toString());
		    
		    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
		    StringBuilder builder = new StringBuilder();
		    for (String line = null; (line = reader.readLine()) != null;) {
		        builder.append(line).append("\n");
		    }
		    
		    System.out.println(builder.toString());
		    
		    json = (JSONObject)new JSONParser().parse(builder.toString());
		    //System.out.println("name=" + json.get("name"));
		    //System.out.println("width=" + json.get("width"));

		    EntityUtils.consume(response.getEntity());
		    
		} catch(Exception ex) {
			
		} finally {
		    httpPost.releaseConnection();

		}
    	
    	return json;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String code = null;
        String instagramClientId = getServletContext().getInitParameter("instagramClientId");
        String instagramClientSecret = getServletContext().getInitParameter("instagramClientSecret");
        String addtionalPermissions = getServletContext().getInitParameter("addtionalPermissions");
        String redirectURL = null;
        String accessURL = null;
    	String accessToken = null;
    	JSONObject webContent = null;
    	JSONObject userJson = null;
        try {
            StringBuffer redirectURLbuffer = request.getRequestURL();
            int index = redirectURLbuffer.lastIndexOf("/");
            redirectURLbuffer.replace(index, redirectURLbuffer.length(), "").append("/callback");
            redirectURL = redirectURLbuffer.toString(); 
            //URLEncoder.encode(redirectURLbuffer.toString(), "UTF-8");
            
        	code = request.getParameter("code");
        	if(null!=code) {
        		System.out.println("Code: " + code);
        		accessURL = "https://api.instagram.com/oauth/access_token";
        		
        		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
        		nvps.add(new BasicNameValuePair("client_id", instagramClientId));
        		nvps.add(new BasicNameValuePair("client_secret", instagramClientSecret));
        		nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
        		nvps.add(new BasicNameValuePair("redirect_uri", redirectURL));
        		nvps.add(new BasicNameValuePair("code", code));
        		nvps.add(new BasicNameValuePair("scope", addtionalPermissions));       		

        		System.out.println("accessURL: " + accessURL);
        		webContent = getWebContentFromURL(accessURL,nvps);
        		accessToken = (String) webContent.get("access_token");
        		        		
        		
        		
        	} else {
        		response.sendRedirect(request.getContextPath() + "/error.html");
        		return;
        	}
        	
            if(null!=accessToken) {
            	System.out.println("accessToken: " + accessToken);
        		userJson = (JSONObject) webContent.get("user");

        		InstagramUser instagramUser = new InstagramUser(userJson.get("id").toString(), userJson.get("username").toString(),
        				userJson.get("full_name").toString(), userJson.get("profile_picture").toString(), 
        				userJson.get("website").toString(), userJson.get("bio").toString());
            	request.getSession().setAttribute("instagramUser", instagramUser);
            	System.out.println("User object: " + instagramUser.toString());
            	response.sendRedirect(request.getContextPath() + "/welcome.jsp");
            }
  
            if(null==accessToken)
            	response.sendRedirect(request.getContextPath() + "/error.html");
        	
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/error.html");
            throw new ServletException(e);
        }
        
    }
}
