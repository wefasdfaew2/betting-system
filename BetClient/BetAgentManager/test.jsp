<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>JSP and Servlet using AJAX</title>
<script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript">
 
function getXMLObject()  //XML OBJECT
{
   var xmlHttp = false;
   try {
     xmlHttp = new ActiveXObject("Msxml2.XMLHTTP")  // For Old Microsoft Browsers
   }
   catch (e) {
     try {
       xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")  // For Microsoft IE 6.0+
     }
     catch (e2) {
       xmlHttp = false   // No Browser accepts the XMLHTTP Object then false
     }
   }
   if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
     xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
   }
   return xmlHttp;  // Mandatory Statement returning the ajax object created
}
 
var xmlhttp = new getXMLObject();	//xmlhttp holds the ajax object
 
function ajaxFunction() {
  var getdate = new Date();  //Used to prevent caching during ajax call
  if(xmlhttp) { 
    xmlhttp.open("GET","DbQuery?ip1=1");// + getdate.getTime(),true); //gettime will be the servlet name
    xmlhttp.onreadystatechange  = handleServerResponse;
    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xmlhttp.send(null);
  }
}
 
function handleServerResponse() {
   if (xmlhttp.readyState == 4) {
     if(xmlhttp.status == 200) {
       //$("btntime").html(xmlhttp.responseText); //Update the HTML Form element 
     	$("div").html(xmlhttp.responseText);
     }
     else {
        alert("Error during AJAX call. Please try again");
     }
   }
}
</script>
<body>
<form name="myForm">
Server Time:<input type="text" name="time" id="btntime"/>
<br />
<input type="button" onClick="javascript:ajaxFunction();" value="Click to display Server Time on Textbox"/>
<br />
</form>
<div></div>

</body>
</head>
</html>