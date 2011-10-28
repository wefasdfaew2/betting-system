package org.apache.jsp.BetAgentManager;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class mainform_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write('\r');
      out.write('\n');

	Object isLog = session.getAttribute("isLogin");
	if (isLog == null) {
		response.sendRedirect("/BetAgentManager/index.jsp");
	}

      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\" />\r\n");
      out.write("<title>Bet Agent Manager</title>\r\n");
      out.write("<link type=\"text/css\"\r\n");
      out.write("\thref=\"css/ui-lightness/jquery-ui-1.8.16.custom.css\" rel=\"stylesheet\" />\r\n");
      out.write("<script type=\"text/javascript\" src=\"js/jquery-1.6.2.min.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"js/jquery-ui-1.8.16.custom.min.js\"></script>\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("\tfunction reloadImg(id) {\r\n");
      out.write("\t\tvar obj = document.getElementById(id);\r\n");
      out.write("\t\tvar src = obj.src;\r\n");
      out.write("\t\tvar pos = src.indexOf('?');\r\n");
      out.write("\t\tif (pos >= 0) {\r\n");
      out.write("\t\t\tsrc = src.substr(0, pos);\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\tvar date = new Date();\r\n");
      out.write("\t\tobj.src = src + '?v=' + date.getTime();\r\n");
      out.write("\t\treturn false;\r\n");
      out.write("\t}\r\n");
      out.write("\tfunction logOut() {\r\n");
      out.write("\t\twindow.location = \"/BetAgentManager/index.jsp\";\r\n");
      out.write("\t}\r\n");
      out.write("\tfunction getXMLObject() //XML OBJECT\r\n");
      out.write("\t{\r\n");
      out.write("\t\tvar xmlHttp = false;\r\n");
      out.write("\t\ttry {\r\n");
      out.write("\t\t\txmlHttp = new ActiveXObject(\"Msxml2.XMLHTTP\") // For Old Microsoft Browsers\r\n");
      out.write("\t\t} catch (e) {\r\n");
      out.write("\t\t\ttry {\r\n");
      out.write("\t\t\t\txmlHttp = new ActiveXObject(\"Microsoft.XMLHTTP\") // For Microsoft IE 6.0+\r\n");
      out.write("\t\t\t} catch (e2) {\r\n");
      out.write("\t\t\t\txmlHttp = false // No Browser accepts the XMLHTTP Object then false\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\tif (!xmlHttp && typeof XMLHttpRequest != 'undefined') {\r\n");
      out.write("\t\t\txmlHttp = new XMLHttpRequest(); //For Mozilla, Opera Browsers\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\treturn xmlHttp; // Mandatory Statement returning the ajax object created\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tfunction disableButton() {\r\n");
      out.write("\t\t$(\"#btnSubmit\").button(\"option\", \"disabled\", true);\r\n");
      out.write("\t\tvar query = \"LoginSbobet?username=\" + $(\"#username\").val()\r\n");
      out.write("\t\t\t\t+ \"&password=\" + $(\"#password\").val() + \"&from=\"\r\n");
      out.write("\t\t\t\t+ $(\"#datepickerfromSbo\").val() + \"&to=\"\r\n");
      out.write("\t\t\t\t+ $(\"#datepickertoSbo\").val();\r\n");
      out.write("\t\t$.get(query);\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tfunction disableButtonIbet() {\r\n");
      out.write("\t\t$(\"#btnIbetSubmit\").button(\"option\", \"disabled\", true);\r\n");
      out.write("\t\tvar query = \"LoginIbet?username=\" + $(\"#ibetusername\").val()\r\n");
      out.write("\t\t\t\t+ \"&password=\" + $(\"#ibetpassword\").val() + \"&from=\"\r\n");
      out.write("\t\t\t\t+ $(\"#datepickerfromIbet\").val() + \"&to=\"\r\n");
      out.write("\t\t\t\t+ $(\"#datepickertoIbet\").val() + \"&captcha=\"\r\n");
      out.write("\t\t\t\t+ $(\"#ibetCaptcha\").val();\r\n");
      out.write("\t\t$.get(query);\r\n");
      out.write("\t}\r\n");
      out.write("\tfunction getQuery(site) {\r\n");
      out.write("\t\tvar query = \"DbQuery?ip1=\" + $(\"#ip1\").val() + \"&ip2=\"\r\n");
      out.write("\t\t\t\t+ $(\"#ip2\").val() + \"&ip3=\" + $(\"#ip3\").val() + \"&ip4=\"\r\n");
      out.write("\t\t\t\t+ $(\"#ip4\").val() + \"&site=\" + site//$('input[name=radio]:checked').val() \r\n");
      out.write("\t\t\t\t+ \"&date=\" + $(\"#date\").val() + \"&datefrom=\"\r\n");
      out.write("\t\t\t\t+ $(\"#datefrom\").val() + \"&dateto=\" + $(\"#dateto\").val();\r\n");
      out.write("\t\treturn query;\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tfunction findDB() {\r\n");
      out.write("\t\tvar site = $('input[name=radio]:checked').val();\r\n");
      out.write("\r\n");
      out.write("\t\tvar activeIndex = $(\"#accordion\").accordion('option', 'active');\r\n");
      out.write("\t\tif (site == \"sbobet\" || site == \"all\") {\r\n");
      out.write("\t\t\t$(\"#divResult1\").load(getQuery(\"sbobet\"));\r\n");
      out.write("\t\t\tif (activeIndex != 0)\r\n");
      out.write("\t\t\t\t$(\"#accordion\").accordion(\"option\", \"active\", 0);\r\n");
      out.write("\t\t}\r\n");
      out.write("\t\tif (site == \"ibet\" || site == \"all\") {\r\n");
      out.write("\t\t\t$(\"#divResult3\").load(getQuery(\"ibet\"));\r\n");
      out.write("\t\t\tif (activeIndex != 2)\r\n");
      out.write("\t\t\t\t$(\"#accordion\").accordion(\"option\", \"active\", 2);\r\n");
      out.write("\t\t}\r\n");
      out.write("\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tvar timer;\r\n");
      out.write("\tvar seconds = 5; // how often should we refresh the DIV?\r\n");
      out.write("\r\n");
      out.write("\tfunction startActivityRefresh() {\r\n");
      out.write("\t\ttimer = setInterval(function() {\r\n");
      out.write("\t\t\t//$('#divResult4').load('TailerHandler');\r\n");
      out.write("\t\t\t$.get(\"TailerHandler\", function(data) {\r\n");
      out.write("\t\t\t\t$(\"#divResult4\").append(data);\r\n");
      out.write("\t\t\t\t$(\"#divResult4\").scrollTop($(\"#divResult4\")[0].scrollHeight);\r\n");
      out.write("\t\t\t}, 'html');\r\n");
      out.write("\t\t}, seconds * 1000)\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\tfunction cancelActivityRefresh() {\r\n");
      out.write("\t\tclearInterval(timer);\r\n");
      out.write("\t}\r\n");
      out.write("\r\n");
      out.write("\t$(document).ready(function() {\r\n");
      out.write("\t\tvar content = $(\"#divResult1\");\r\n");
      out.write("\t\tstartActivityRefresh();\r\n");
      out.write("\t\t//Manage click events\r\n");
      out.write("\r\n");
      out.write("\t});\r\n");
      out.write("\r\n");
      out.write("\t$(function() {\r\n");
      out.write("\t\t// Radio\r\n");
      out.write("\t\t$(\"#radio\").buttonset();\r\n");
      out.write("\r\n");
      out.write("\t\t// Button\r\n");
      out.write("\t\t$(\"input:submit, button\").button({\r\n");
      out.write("\t\t\ticons : {\r\n");
      out.write("\t\t\t\tprimary : \"ui-icon-zoomin\"\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t$(\".btnFind\").button({\r\n");
      out.write("\t\t\ticons : {\r\n");
      out.write("\t\t\t\tprimary : \"ui-icon-zoomin\"\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t$(\".logoutBtn\").button({\r\n");
      out.write("\t\t\ticons : {\r\n");
      out.write("\t\t\t\tprimary : \"ui-icon-key\"\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t// Accordion\r\n");
      out.write("\t\tvar icons = {\r\n");
      out.write("\t\t\theader : \"ui-icon-circle-arrow-e\",\r\n");
      out.write("\t\t\theaderSelected : \"ui-icon-circle-arrow-s\"\r\n");
      out.write("\t\t};\r\n");
      out.write("\t\t$(\"#accordion\").accordion({\r\n");
      out.write("\t\t\theader : \"h3\",\r\n");
      out.write("\t\t\tclearStyle : true,\r\n");
      out.write("\t\t\tfillSpace : true,\r\n");
      out.write("\t\t\tcollapsible : true,\r\n");
      out.write("\t\t\ticons : icons,\r\n");
      out.write("\t\t\tactive : 3\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t//$(\"#accordion\").draggable();\r\n");
      out.write("\r\n");
      out.write("\t\t// Tabs\r\n");
      out.write("\t\t$('#tabs').tabs();\r\n");
      out.write("\t\t$('#tabs').draggable();\r\n");
      out.write("\r\n");
      out.write("\t\t// Dialog\t\t\t\r\n");
      out.write("\t\t$('#dialog').dialog({\r\n");
      out.write("\t\t\tautoOpen : false,\r\n");
      out.write("\t\t\twidth : 600,\r\n");
      out.write("\t\t\tbuttons : {\r\n");
      out.write("\t\t\t\t\"Ok\" : function() {\r\n");
      out.write("\t\t\t\t\t$(this).dialog(\"close\");\r\n");
      out.write("\t\t\t\t},\r\n");
      out.write("\t\t\t\t\"Cancel\" : function() {\r\n");
      out.write("\t\t\t\t\t$(this).dialog(\"close\");\r\n");
      out.write("\t\t\t\t}\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t// Dialog Link\r\n");
      out.write("\t\t$('#dialog_link').click(function() {\r\n");
      out.write("\t\t\t$('#dialog').dialog('open');\r\n");
      out.write("\t\t\treturn false;\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t// Datepicker From\r\n");
      out.write("\t\t$(\r\n");
      out.write("\t\t\t\t'#datepickerfromSbo,#datepickertoSbo,#datepickerfromIbet,#datepickertoIbet,#date,#datefrom,#dateto')\r\n");
      out.write("\t\t\t\t.datepicker({\r\n");
      out.write("\t\t\t\t\tinline : true,\r\n");
      out.write("\t\t\t\t\tshowButtonPanel : true,\r\n");
      out.write("\t\t\t\t\tchangeMonth : true,\r\n");
      out.write("\t\t\t\t\tchangeYear : true\r\n");
      out.write("\t\t\t\t});\r\n");
      out.write("\t\t// Datepicker To\r\n");
      out.write("\t\t$('#datepickerto').datepicker({\r\n");
      out.write("\t\t\tinline : true,\r\n");
      out.write("\t\t\tshowButtonPanel : true,\r\n");
      out.write("\t\t\tchangeMonth : true,\r\n");
      out.write("\t\t\tchangeYear : true\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t// Slider\r\n");
      out.write("\t\t$('#slider').slider({\r\n");
      out.write("\t\t\trange : true,\r\n");
      out.write("\t\t\tvalues : [ 17, 67 ]\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t// Progressbar\r\n");
      out.write("\t\t$(\"#progressbar\").progressbar({\r\n");
      out.write("\t\t\tvalue : 20\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t\t//hover states on the static widgets\r\n");
      out.write("\t\t$('#dialog_link, ul#icons li').hover(function() {\r\n");
      out.write("\t\t\t$(this).addClass('ui-state-hover');\r\n");
      out.write("\t\t}, function() {\r\n");
      out.write("\t\t\t$(this).removeClass('ui-state-hover');\r\n");
      out.write("\t\t});\r\n");
      out.write("\r\n");
      out.write("\t});\r\n");
      out.write("</script>\r\n");
      out.write("<style type=\"text/css\">\r\n");
      out.write("/*demo page css*/\r\n");
      out.write("body {\r\n");
      out.write("\tfont: 62.5% \"Trebuchet MS\", sans-serif;\r\n");
      out.write("\tmargin: 50px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".demoHeaders {\r\n");
      out.write("\tmargin-top: 2em;\r\n");
      out.write("\talign: left;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write(".accordionContent {\r\n");
      out.write("\twidth: 300px;\r\n");
      out.write("\tfloat: left;\r\n");
      out.write("\tbackground: #95B1CE;\r\n");
      out.write("\tdisplay: none;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#dialog_link {\r\n");
      out.write("\tpadding: .4em 1em .4em 20px;\r\n");
      out.write("\ttext-decoration: none;\r\n");
      out.write("\tposition: relative;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#dialog_link span.ui-icon {\r\n");
      out.write("\tmargin: 0 5px 0 0;\r\n");
      out.write("\tposition: absolute;\r\n");
      out.write("\tleft: .2em;\r\n");
      out.write("\ttop: 50%;\r\n");
      out.write("\tmargin-top: -8px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("ul#icons {\r\n");
      out.write("\tmargin: 0;\r\n");
      out.write("\tpadding: 0;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("ul#icons li {\r\n");
      out.write("\tmargin: 2px;\r\n");
      out.write("\tposition: relative;\r\n");
      out.write("\tpadding: 4px 0;\r\n");
      out.write("\tcursor: pointer;\r\n");
      out.write("\tfloat: left;\r\n");
      out.write("\tlist-style: none;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("ul#icons span.ui-icon {\r\n");
      out.write("\tfloat: left;\r\n");
      out.write("\tmargin: 0 4px;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#tabs {\r\n");
      out.write("\twidth: 100%;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#accordion {\r\n");
      out.write("\twidth: 100%;\r\n");
      out.write("\tfloat: right;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#tableLogin {\r\n");
      out.write("\twidth: 600px;\r\n");
      out.write("}\r\n");
      out.write("</style>\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("<table width=100%>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td>\r\n");
      out.write("\t\t<h1>Welcome to Bet Agent Manager!</h1>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=\"right\">\r\n");
      out.write("\t\t<button class=\"logoutBtn\" onclick=\"logOut()\">Log out</button>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("<!-- Tabs -->\r\n");
      out.write("\r\n");
      out.write("<table width=100%>\r\n");
      out.write("\t<tr>\r\n");
      out.write("\t\t<td width=30%>\r\n");
      out.write("\t\t<h2 class=\"demoHeaders\">Login</h2>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td align=\"left\">\r\n");
      out.write("\t\t<h2 class=\"demoHeaders\"></h2>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("\t<tr height=200px>\r\n");
      out.write("\t\t<td valign=\"top\"><!-- Tabs -->\r\n");
      out.write("\t\t<div id=\"tabs\">\r\n");
      out.write("\t\t<ul>\r\n");
      out.write("\t\t\t<li><a href=\"#tabs-1\">SboBet</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"#tabs-2\">3in1</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"#tabs-3\">ibet</a></li>\r\n");
      out.write("\t\t\t<li><a href=\"#tabs-4\">Result</a></li>\r\n");
      out.write("\t\t</ul>\r\n");
      out.write("\t\t<div id=\"tabs-1\"><!-- Start login Form-->\r\n");
      out.write("\t\t<table width=\"300\" cellspacing=\"0\" cellpadding=\"2\" border=\"0\"\r\n");
      out.write("\t\t\talign=\"center\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litName\">Username:</td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 30px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"username\" name=\"username\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litPwd\">Password</span> :</td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"password\" class=\"Textfield\" id=\"password\" name=\"password\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litFrom\">From</span> :</td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" id=\"datepickerfromSbo\" name=\"txtFrom\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litFrom\">To</span> :</td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" id=\"datepickertoSbo\" name=\"txtTo\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td></td>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: left;\">\r\n");
      out.write("\t\t\t\t<input type=\"submit\" style=\"cursor: pointer;\"\r\n");
      out.write("\t\t\t\t\tonclick=\"disableButton();\" class=\"button\" value=\"Sign In\"\r\n");
      out.write("\t\t\t\t\tname=\"btnSubmit\" id=\"btnSubmit\">\r\n");
      out.write("\t\t\t\t<div class=\"loginResult\"></div>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t</tbody>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t<!-- End login Form--></div>\r\n");
      out.write("\t\t<div id=\"tabs-2\"></div>\r\n");
      out.write("\t\t<div id=\"tabs-3\"><!-- Start login Form-->\r\n");
      out.write("\t\t<table width=\"300\" cellspacing=\"0\" cellpadding=\"2\" border=\"0\"\r\n");
      out.write("\t\t\talign=\"center\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litibetName\">Username:</td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 30px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"ibetusername\" name=\"ibetusername\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litibetPwd\">Password</span> :</td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"ibetpassword\" class=\"Textfield\" id=\"ibetpassword\"\r\n");
      out.write("\t\t\t\t\tname=\"ibetpassword\"></td>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litibetFrom\">From</span> :</td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" id=\"datepickerfromIbet\" name=\"txtibetFrom\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litibetTo\">To</span> :</td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" id=\"datepickertoIbet\" name=\"txtibetTo\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td><img id=\"imgIbetCaptcha\" alt=\"captcha\" src=\"IbetCaptcha\"\r\n");
      out.write("\t\t\t\t\talign=right onclick=\"return reloadImg('imgIbetCaptcha');\"></td>\r\n");
      out.write("\t\t\t\t<td><input type=\"text\" id=\"ibetCaptcha\" name=\"ibetCaptcha\">\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td></td>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: left;\">\r\n");
      out.write("\t\t\t\t<input type=\"submit\" style=\"cursor: pointer;\"\r\n");
      out.write("\t\t\t\t\tonclick=\"disableButtonIbet();\" class=\"button\" value=\"Sign In\"\r\n");
      out.write("\t\t\t\t\tname=\"btnIbetSubmit\" id=\"btnIbetSubmit\">\r\n");
      out.write("\t\t\t\t<div class=\"loginIbetResult\"></div>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t</tbody>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t<!-- End login Form--></div>\r\n");
      out.write("\t\t<div id=\"tabs-4\">\r\n");
      out.write("\t\t<table width=100% cellspacing=\"0\" cellpadding=\"2\" border=\"0\"\r\n");
      out.write("\t\t\talign=\"center\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litip1\">&nbsp&nbsp&nbsp&nbsp&nbspIp1</span><span\r\n");
      out.write("\t\t\t\t\tonclick=\"alert('B01');\">:</span></td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 50px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"ip1\" name=\"ip1\"></td>\r\n");
      out.write("\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litip2\">ip2</span> <span onclick=\"alert('B01');\">:</span></td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 50px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"ip2\" name=\"ip2\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litip3\">ip3</span> <span onclick=\"alert('B01');\">:</span></td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 50px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"ip3\" name=\"ip3\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litip4\">ip4</span> <span onclick=\"alert('B01');\">:</span></td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 50px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"ip4\" name=\"ip4\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litDate\">Date</span> <span onclick=\"alert('B01');\">:</span></td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 50px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"date\" name=\"date\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litDate\">From Date</span> <span onclick=\"alert('B01');\">:</span></td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 50px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"datefrom\" name=\"datefrom\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; width: 10px; text-align: right;\"><span\r\n");
      out.write("\t\t\t\t\tid=\"litDate\">To Date</span> <span onclick=\"alert('B01');\">:</span></td>\r\n");
      out.write("\t\t\t\t<td style=\"vertical-align: top; line-height: 22px; width: 50px;\"><input\r\n");
      out.write("\t\t\t\t\ttype=\"text\" class=\"Textfield\" id=\"dateto\" name=\"dateto\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td></td>\r\n");
      out.write("\t\t\t\t<td><input type=\"submit\" style=\"cursor: pointer;\"\r\n");
      out.write("\t\t\t\t\tonclick=\"findDB()\" class=\"btnFind\" value=\"Find\" name=\"btnFind\"\r\n");
      out.write("\t\t\t\t\tid=\"btnFind\"></td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<td\r\n");
      out.write("\t\t\t\t\tstyle=\"vertical-align: top; line-height: 22px; text-align: left;\"\r\n");
      out.write("\t\t\t\t\tcolspan=\"2\">\r\n");
      out.write("\r\n");
      out.write("\t\t\t\t<div id=\"radio\" align=\"center\"><input type=\"radio\" id=\"radio1\"\r\n");
      out.write("\t\t\t\t\tname=\"radio\" value=\"all\" /><label for=\"radio1\">All</label> <input\r\n");
      out.write("\t\t\t\t\ttype=\"radio\" id=\"radio2\" name=\"radio\" checked=\"checked\"\r\n");
      out.write("\t\t\t\t\tvalue=\"sbobet\" /><label for=\"radio2\">sbobet</label> <input\r\n");
      out.write("\t\t\t\t\ttype=\"radio\" id=\"radio3\" name=\"radio\" value=\"3in1\" /><label\r\n");
      out.write("\t\t\t\t\tfor=\"radio3\">3in1</label> <input type=\"radio\" id=\"radio4\"\r\n");
      out.write("\t\t\t\t\tname=\"radio\" value=\"ibet\" /><label for=\"radio4\">ibet</label></div>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t</tbody>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t\t<td valign=\"top\"><!-- Accordion -->\r\n");
      out.write("\t\t<div id=\"accordion\">\r\n");
      out.write("\t\t<div>\r\n");
      out.write("\t\t<h3><a href=\"#\">Sbobet</a></h3>\r\n");
      out.write("\t\t<div id=\"divResult1\">Sbobet result here</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div>\r\n");
      out.write("\t\t<h3><a href=\"#\">3in1</a></h3>\r\n");
      out.write("\t\t<div id=\"divResult2\">3in1 result here</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div>\r\n");
      out.write("\t\t<h3><a href=\"#\">ibet</a></h3>\r\n");
      out.write("\t\t<div id=\"divResult3\">ibet result here</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t<div>\r\n");
      out.write("\t\t<h3><a href=\"#\">console</a></h3>\r\n");
      out.write("\t\t<div id=\"divResult4\">console output of server</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t\t</td>\r\n");
      out.write("\t</tr>\r\n");
      out.write("</table>\r\n");
      out.write("</body>\r\n");
      out.write("</html>\r\n");
      out.write("\r\n");
      out.write("\r\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
