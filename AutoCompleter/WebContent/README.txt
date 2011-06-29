Notice that user and technical guide is given below:

USER GUIDE:
1) This project is used to get counties of a selected province.
2) To run application, just put AutoCompleter.war into an installed "Apache Tomcat 6.0" wtpWebApplications
   directory. After starting up server, navigate "http://localhost:8080/AutoCompleter"
3) To select a province, just try-to-type the name of desired province, interactively (via ajax) available 
   (starts with entered prefix) provinces are listed below corresponding text box.
4) Select (just click) one of these available provinces.
5) When you select a province, its counties are listed on a combo box below (via ajax).

TECHNICAL GUIDE:
1) This project uses two different ajax requests through a servlet, AutoCompleterServlet.java
   i) One of them brings available province names into a table
   ii) The other one puts selected province's counties into a combo box.
2) Provinces are not read from a relational database, just dumped into a static list from "cities.xml"
   on initialization.
3) index page resides in ./WebContent/WEB-INF/jsp/index.jsp
4) auto completer javascript (which also makes ajax requests and parse response's returned xml) resides
   in ./WebContent/scripts/autocomplete.js
5) Note that you must use "Apache Tomcat 6.0" to run this application.

For any question, suggestion or review; please mail to "tugcem.oral@gmail.com" 