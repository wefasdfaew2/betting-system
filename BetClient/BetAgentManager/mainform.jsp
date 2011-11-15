
<%
	Object isLog = session.getAttribute("isLogin");
	if (isLog == null) {
		response.sendRedirect("/BetAgentManager/index.jsp");
	}
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Bet Agent Manager</title>
<link type="text/css"
	href="css/ui-lightness/jquery-ui-1.8.16.custom.css" rel="stylesheet" />
<script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript"
	src="http://jqueryui.com/themeroller/themeswitchertool/"></script>
<script type="text/javascript">
	function reloadImg(id) {
		var obj = document.getElementById(id);
		var src = obj.src;
		var pos = src.indexOf('?');
		if (pos >= 0) {
			src = src.substr(0, pos);
		}
		var date = new Date();
		obj.src = src + '?v=' + date.getTime();
		return false;
	}
	function logOut() {
		window.location = "/BetAgentManager/index.jsp";
	}
	function getXMLObject() //XML OBJECT
	{
		var xmlHttp = false;
		try {
			xmlHttp = new ActiveXObject("Msxml2.XMLHTTP") // For Old Microsoft Browsers
		} catch (e) {
			try {
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP") // For Microsoft IE 6.0+
			} catch (e2) {
				xmlHttp = false // No Browser accepts the XMLHTTP Object then false
			}
		}
		if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
			xmlHttp = new XMLHttpRequest(); //For Mozilla, Opera Browsers
		}
		return xmlHttp; // Mandatory Statement returning the ajax object created
	}

	function disableButton() {
		$("#btnSubmit").button("option", "disabled", true);
		var query = "LoginSbobet?username=" + $("#username").val()
				+ "&password=" + $("#password").val() + "&from="
				+ $("#datepickerfromSbo").val() + "&to="
				+ $("#datepickertoSbo").val();
		$.get(query);
	}

	function disableButtonIbet() {
		$("#btnIbetSubmit").button("option", "disabled", true);
		var query = "LoginIbet?username=" + $("#ibetusername").val()
				+ "&password=" + $("#ibetpassword").val() + "&from="
				+ $("#datepickerfromIbet").val() + "&to="
				+ $("#datepickertoIbet").val() + "&captcha="
				+ $("#ibetCaptcha").val();
		$.get(query);
	}
	function getQuery(site) {
		var query = "DbQuery?ip1=" + $("#ip1").val() + "&ip2="
				+ $("#ip2").val() + "&ip3=" + $("#ip3").val() + "&ip4="
				+ $("#ip4").val() + "&site=" + site//$('input[name=radio]:checked').val() 
				+ "&date=" + $("#date").val() + "&datefrom="
				+ $("#datefrom").val() + "&dateto=" + $("#dateto").val();
		return query;
	}

	function findDB() {
		var site = $('input[name=radio]:checked').val();

		var activeIndex = $("#accordion").accordion('option', 'active');
		if (site == "sbobet" || site == "all") {
			$("#divResult1").load(getQuery("sbobet"));
			if (activeIndex != 0)
				$("#accordion").accordion("option", "active", 0);
		}
		if (site == "ibet" || site == "all") {
			$("#divResult3").load(getQuery("ibet"));
			if (activeIndex != 2)
				$("#accordion").accordion("option", "active", 2);
		}

	}

	var timer;
	var seconds = 5; // how often should we refresh the DIV?

	function startActivityRefresh() {
		timer = setInterval(function() {
			//$('#divResult4').load('TailerHandler');
			$.get("TailerHandler", function(data) {
				$("#divResult4").append(data);
				$("#divResult4").scrollTop($("#divResult4")[0].scrollHeight);
			}, 'html');
		}, seconds * 1000)
	}

	function cancelActivityRefresh() {
		clearInterval(timer);
	}
	
	$(document).ready(function() {
		var content = $("#divResult1");
		startActivityRefresh();
		//Manage click events

	});

	$(function() {
		// theme switcher
		$('#switcher').themeswitcher()
		// Radio
		$("#radio").buttonset();

		// Button
		$("input:submit, button").button({
			icons : {
				primary : "ui-icon-zoomin"
			}
		});
		$(".btnFind").button({
			icons : {
				primary : "ui-icon-zoomin"
			}
		});

		$(".logoutBtn").button({
			icons : {
				primary : "ui-icon-key"
			}
		});
		// Accordion
		var icons = {
			header : "ui-icon-circle-arrow-e",
			headerSelected : "ui-icon-circle-arrow-s"
		};
		$("#accordion").accordion({
			header : "h3",
			clearStyle : true,
			fillSpace : true,
			collapsible : true,
			icons : icons,
			active : 3
		});
		//$("#accordion").draggable();

		// Tabs
		$('#tabs').tabs();
		$('#tabs').draggable();

		// Dialog			
		$('#dialog').dialog({
			autoOpen : false,
			width : 600,
			buttons : {
				"Ok" : function() {
					$(this).dialog("close");
				},
				"Cancel" : function() {
					$(this).dialog("close");
				}
			}
		});

		// Dialog Link
		$('#dialog_link').click(function() {
			$('#dialog').dialog('open');
			return false;
		});

		// Datepicker From
		$(
				'#datepickerfromSbo,#datepickertoSbo,#datepickerfromIbet,#datepickertoIbet,#date,#datefrom,#dateto')
				.datepicker({
					inline : true,
					showButtonPanel : true,
					changeMonth : true,
					changeYear : true
				});
		// Datepicker To
		$('#datepickerto').datepicker({
			inline : true,
			showButtonPanel : true,
			changeMonth : true,
			changeYear : true
		});

		// Slider
		$('#slider').slider({
			range : true,
			values : [ 17, 67 ]
		});

		// Progressbar
		$("#progressbar").progressbar({
			value : 20
		});

		//hover states on the static widgets
		$('#dialog_link, ul#icons li').hover(function() {
			$(this).addClass('ui-state-hover');
		}, function() {
			$(this).removeClass('ui-state-hover');
		});

	});
</script>
<style type="text/css">
/*demo page css*/
body {
	font: 62.5% "Trebuchet MS", sans-serif;
	margin: 50px;
}

.demoHeaders {
	margin-top: 2em;
	align: left;
}

.accordionContent {
	width: 300px;
	float: left;
	background: #95B1CE;
	display: none;
}

#dialog_link {
	padding: .4em 1em .4em 20px;
	text-decoration: none;
	position: relative;
}

#dialog_link span.ui-icon {
	margin: 0 5px 0 0;
	position: absolute;
	left: .2em;
	top: 50%;
	margin-top: -8px;
}

ul#icons {
	margin: 0;
	padding: 0;
}

ul#icons li {
	margin: 2px;
	position: relative;
	padding: 4px 0;
	cursor: pointer;
	float: left;
	list-style: none;
}

ul#icons span.ui-icon {
	float: left;
	margin: 0 4px;
}

#tabs {
	width: 100%;
}

#accordion {
	width: 100%;
	float: right;
}

#tableLogin {
	width: 600px;
}
</style>
</head>
<body>
	<div id="switcher"></div>
	<table width=100%>
		<tr>
			<td>
				<h1>Welcome to Bet Agent Manager!</h1>
			</td>
			<td align="right">
				<button class="logoutBtn" onclick="logOut()">Log out</button>
			</td>
		</tr>

	</table>
	<!-- Tabs -->

	<table width=100%>
		<tr>
			<td width=30%>
				<h2 class="demoHeaders">Login</h2>
			</td>
			<td align="left">
				<h2 class="demoHeaders"></h2>
			</td>
		</tr>
		<tr height=200px>
			<td valign="top">
				<!-- Tabs -->
				<div id="tabs">
					<ul>
						<li><a href="#tabs-1">SboBet</a></li>
						<li><a href="#tabs-2">3in1</a></li>
						<li><a href="#tabs-3">ibet</a></li>
						<li><a href="#tabs-4">Result</a></li>
					</ul>
					<div id="tabs-1">
						<!-- Start login Form-->
						<table width="300" cellspacing="0" cellpadding="2" border="0"
							align="center">
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litName">Username: 
								</td>
								<td style="vertical-align: top; line-height: 22px; width: 30px;"><input
									type="text" class="Textfield" id="username" name="username">
								</td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; text-align: right;"><span
									id="litPwd">Password</span> :</td>
								<td style="vertical-align: top; line-height: 22px;"><input
									type="password" class="Textfield" id="password" name="password">
								</td>


							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; text-align: right;"><span
									id="litFrom">From</span> :</td>
								<td style="vertical-align: top; line-height: 22px;"><input
									type="text" id="datepickerfromSbo" name="txtFrom"></td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; text-align: right;"><span
									id="litFrom">To</span> :</td>
								<td style="vertical-align: top; line-height: 22px;"><input
									type="text" id="datepickertoSbo" name="txtTo"></td>
							</tr>

							<tr>
								<td></td>
								<td
									style="vertical-align: top; line-height: 22px; text-align: left;">
									<input type="submit" style="cursor: pointer;"
									onclick="disableButton();" class="button" value="Sign In"
									name="btnSubmit" id="btnSubmit">
									<div class="loginResult"></div>
								</td>
							</tr>
							</tbody>
						</table>
						<!-- End login Form-->
					</div>
					<div id="tabs-2"></div>
					<div id="tabs-3">
						<!-- Start login Form-->
						<table width="300" cellspacing="0" cellpadding="2" border="0"
							align="center">
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litibetName">Username: 
								</td>
								<td style="vertical-align: top; line-height: 22px; width: 30px;"><input
									type="text" class="Textfield" id="ibetusername"
									name="ibetusername"></td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; text-align: right;"><span
									id="litibetPwd">Password</span> :</td>
								<td style="vertical-align: top; line-height: 22px;"><input
									type="ibetpassword" class="Textfield" id="ibetpassword"
									name="ibetpassword"></td>


							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; text-align: right;"><span
									id="litibetFrom">From</span> :</td>
								<td style="vertical-align: top; line-height: 22px;"><input
									type="text" id="datepickerfromIbet" name="txtibetFrom">
								</td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; text-align: right;"><span
									id="litibetTo">To</span> :</td>
								<td style="vertical-align: top; line-height: 22px;"><input
									type="text" id="datepickertoIbet" name="txtibetTo"></td>
							</tr>
							<tr>
								<td><img id="imgIbetCaptcha" alt="captcha"
									src="IbetCaptcha" align=right
									onclick="return reloadImg('imgIbetCaptcha');"></td>
								<td><input type="text" id="ibetCaptcha" name="ibetCaptcha">
								</td>
							</tr>
							<tr>
								<td></td>
								<td
									style="vertical-align: top; line-height: 22px; text-align: left;">
									<input type="submit" style="cursor: pointer;"
									onclick="disableButtonIbet();" class="button" value="Sign In"
									name="btnIbetSubmit" id="btnIbetSubmit">
									<div class="loginIbetResult"></div>
								</td>
							</tr>
							</tbody>
						</table>
						<!-- End login Form-->
					</div>
					<div id="tabs-4">
						<table width=100% cellspacing="0" cellpadding="2" border="0"
							align="center">
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litip1">&nbsp&nbsp&nbsp&nbsp&nbspIp1</span><span
									onclick="alert('B01');">:</span></td>
								<td style="vertical-align: top; line-height: 22px; width: 50px;"><input
									type="text" class="Textfield" id="ip1" name="ip1"></td>

							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litip2">ip2</span> <span onclick="alert('B01');">:</span></td>
								<td style="vertical-align: top; line-height: 22px; width: 50px;"><input
									type="text" class="Textfield" id="ip2" name="ip2"></td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litip3">ip3</span> <span onclick="alert('B01');">:</span></td>
								<td style="vertical-align: top; line-height: 22px; width: 50px;"><input
									type="text" class="Textfield" id="ip3" name="ip3"></td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litip4">ip4</span> <span onclick="alert('B01');">:</span></td>
								<td style="vertical-align: top; line-height: 22px; width: 50px;"><input
									type="text" class="Textfield" id="ip4" name="ip4"></td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litDate">Date</span> <span onclick="alert('B01');">:</span>
								</td>
								<td style="vertical-align: top; line-height: 22px; width: 50px;"><input
									type="text" class="Textfield" id="date" name="date"></td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litDate">From Date</span> <span onclick="alert('B01');">:</span>
								</td>
								<td style="vertical-align: top; line-height: 22px; width: 50px;"><input
									type="text" class="Textfield" id="datefrom" name="datefrom">
								</td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; width: 10px; text-align: right;"><span
									id="litDate">To Date</span> <span onclick="alert('B01');">:</span>
								</td>
								<td style="vertical-align: top; line-height: 22px; width: 50px;"><input
									type="text" class="Textfield" id="dateto" name="dateto">
								</td>
							</tr>
							<tr>
								<td></td>
								<td><input type="submit" style="cursor: pointer;"
									onclick="findDB()" class="btnFind" value="Find" name="btnFind"
									id="btnFind"></td>
							</tr>
							<tr>
								<td
									style="vertical-align: top; line-height: 22px; text-align: left;"
									colspan="2">

									<div id="radio" align="center">
										<input type="radio" id="radio1" name="radio" value="all" /><label
											for="radio1">All</label> <input type="radio" id="radio2"
											name="radio" checked="checked" value="sbobet" /><label
											for="radio2">sbobet</label> <input type="radio" id="radio3"
											name="radio" value="3in1" /><label for="radio3">3in1</label>
										<input type="radio" id="radio4" name="radio" value="ibet" /><label
											for="radio4">ibet</label>
									</div>
								</td>
							</tr>
							</tbody>
						</table>
					</div>
				</div>
			</td>
			<td valign="top">
				<!-- Accordion -->
				<div id="accordion">
					<div>
						<h3>
							<a href="#">Sbobet</a>
						</h3>
						<div id="divResult1">Sbobet result here</div>
					</div>
					<div>
						<h3>
							<a href="#">3in1</a>
						</h3>
						<div id="divResult2">3in1 result here</div>
					</div>
					<div>
						<h3>
							<a href="#">ibet</a>
						</h3>
						<div id="divResult3">ibet result here</div>
					</div>
					<div>
						<h3>
							<a href="#">console</a>
						</h3>
						<div id="divResult4">console output of server</div>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>


