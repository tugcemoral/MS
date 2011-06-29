<%@ page language="java" 
import="java.util.*" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <title>File Uploader Server</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<script type="text/javascript" src="<%=path%>/scripts/jquery/jquery-1.3.2.min.js">  </script>
	<script type="text/javascript" src="<%=path%>/scripts/jquery/plugins/popupWindow/jquery.popupWindow.js">  </script>
  </head>
  <body>
  This page is served as "File Uploader" main page.
<!--          <applet-->
<!--          		codebase="."-->
<!--	            code="wjhk.jupload2.JUploadApplet"-->
<!--	            name="JUpload"-->
<!--	            archive="wjhk.jupload.jar"-->
<!--	            width="640"-->
<!--	            height="300"-->
<!--	            mayscript="true"-->
<!--	            alt="The java pugin must be installed.">-->
<!--             param name="type"    value="application/x-java-applet;version=1.5" /  -->
<!--            <param name="postURL" value="http://192.168.2.12/" />-->
<!--            <param name="showLogWindow" value="false" />-->
<!--            Java 1.5 or higher plugin required. -->
<!--        </applet>-->
  </body>
</html>