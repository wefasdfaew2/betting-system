<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="title"
	content="jQuery ui-Scrollable tabs plugin v1.0 by Aamir Afridi" />
<meta name="keywords"
	content="jquery, tabs, scrollable, scroll, aamir, afridi, aamir afridi, js, javascript, script, plugin, jquery plugin, jq, tab">

	<title>Bet Machine</title>
	<link type="text/css"
		href="css/ui-lightness/jquery-ui-1.7.2.custom.css" rel="stylesheet" />
	<link type="text/css" href="css/sh/sh_style.css" rel="stylesheet" />
	<script type="text/javascript" src="js/jquery-1.6.2.min.js"></script>
	<script type="text/javascript"
		src="http://ajax.googleapis.com/ajax/libs/jqueryui/1.8.1/jquery-ui.min.js"></script>
	<script type="text/javascript" src="js/jquery.scrollabletab.js"></script>
	<script type="text/javascript"
		src="http://jqueryui.com/themeroller/themeswitchertool/"></script>
	<script type="text/javascript" src="js/sh/sh.js"></script>
	<script type="text/javascript" src="js/sh/sh_javascript.min.js"></script>
	<script type="text/javascript" src="js/sh/sh_html.min.js"></script>
	<script type="text/javascript" src="js/jquery.init.js"></script>
	<script type="text/javascript">
		var $tabs = $('#tabs').tabs().scrollabletab({
			'closable' : true, //Default false
			'animationSpeed' : 50, //Default 100
			'loadLastTab' : true, //Default false
			'resizable' : true, //Default false
			'resizeHandles' : 'e,s,se', //Default 'e,s,se'
			'easing' : 'easeInOutExpo' //Default 'swing'
		});
		//Add new tab
		$('#addTab')
				.click(
						function() {
							var label = 'Suck tab', content = 'This is the content for the '
									+ label + 'Lorem ipsum dolor sit amet, ...'; //Content can be a jQuery object
							//Event to add new tab
							$tabs.trigger('addTab', [ label, content ]);
							return false;
						});
	</script>
	<style type="text/css">
body {
	font-size: 11px;
	font-family: "Helvetica Neue", HelveticaNeue, Helvetica, sans-serif;
	margin: 50px 70px;
}

p {
	font-size: 13px
}

h2 {
	font-size: 16px;
}

.tabs {
	width: 600px;
}

#switcher {
	position: fixed;
	right: 70px;
	top: 20px
}

pre {
	padding: 10px;
	border: 1px solid #CCC;
	background: none !important
}

.myCode {
	font-family: monospace;
	border: none
}
</style>
</head>
<body>
	<div id="switcher"></div>
	<table>
		<tr>
			<td style="margin: 20px 0 20px 8px; float: left;"><a
				class="ui-state-default ui-corner-all" id="addUiTab_2" href="#"
				style="padding: 6px 6px 6px 17px; text-decoration: none; position: relative">
					<span class="ui-icon ui-icon-plus"
					style="position: absolute; top: 4px; left: 1px"></span> Add </a>
			</td>
			<td style="clear: both; margin: 20px 0;"><a
				class="ui-state-default ui-corner-all" id="removeTab_2" href="#"
				style="padding: 6px 6px 6px 17px; text-decoration: none; position: relative">
					<span class="ui-icon ui-icon-plus"
					style="position: absolute; top: 4px; left: 1px"></span> Remove</a>
			</td>
		</tr>
	</table>

	<div id="example_2" class="tabs">
		<ul>
			<li><a href="#tabs-1-3">Tab 1</a>
			</li>
		</ul>
		<div id="tabs-1-3">
			Tab 1<br>Lorem ipsum dolor sit amet, consectetur adipiscing
				elit. Quisque hendrerit vulputate porttitor. Fusce purus leo,
				faucibus a sagittis congue, molestie tempus felis. Donec convallis
				semper enim, varius sagittis eros imperdiet in. Vivamus semper sem
				at metus mattis a aliquam neque ornare. Proin sed semper lacus. 
		</div>
	</div>
</body>
</html>












