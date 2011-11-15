if (!this.JSON) {
	this.JSON = {}
}
(function() {
	function f(n) {
		return n < 10 ? "0" + n : n
	}
	if (typeof Date.prototype.toJSON !== "function") {
		Date.prototype.toJSON = function(key) {
			return isFinite(this.valueOf()) ? this.getUTCFullYear() + "-"
					+ f(this.getUTCMonth() + 1) + "-" + f(this.getUTCDate())
					+ "T" + f(this.getUTCHours()) + ":"
					+ f(this.getUTCMinutes()) + ":" + f(this.getUTCSeconds())
					+ "Z" : null
		};
		String.prototype.toJSON = Number.prototype.toJSON = Boolean.prototype.toJSON = function(
				key) {
			return this.valueOf()
		}
	}
	var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g, gap, indent, meta = {
		"\b" : "\\b",
		"\t" : "\\t",
		"\n" : "\\n",
		"\f" : "\\f",
		"\r" : "\\r",
		'"' : '\\"',
		"\\" : "\\\\"
	}, rep;
	function quote(string) {
		escapable.lastIndex = 0;
		return escapable.test(string) ? '"'
				+ string.replace(escapable, function(a) {
					var c = meta[a];
					return typeof c === "string" ? c : "\\u"
							+ ("0000" + a.charCodeAt(0).toString(16)).slice(-4)
				}) + '"' : '"' + string + '"'
	}
	function str(key, holder) {
		var i, k, v, length, mind = gap, partial, value = holder[key];
		if (value && typeof value === "object"
				&& typeof value.toJSON === "function") {
			value = value.toJSON(key)
		}
		if (typeof rep === "function") {
			value = rep.call(holder, key, value)
		}
		switch (typeof value) {
		case "string":
			return quote(value);
		case "number":
			return isFinite(value) ? String(value) : "null";
		case "boolean":
		case "null":
			return String(value);
		case "object":
			if (!value) {
				return "null"
			}
			gap += indent;
			partial = [];
			if (Object.prototype.toString.apply(value) === "[object Array]") {
				length = value.length;
				for (i = 0; i < length; i += 1) {
					partial[i] = str(i, value) || "null"
				}
				v = partial.length === 0 ? "[]" : gap ? "[\n" + gap
						+ partial.join(",\n" + gap) + "\n" + mind + "]" : "["
						+ partial.join(",") + "]";
				gap = mind;
				return v
			}
			if (rep && typeof rep === "object") {
				length = rep.length;
				for (i = 0; i < length; i += 1) {
					k = rep[i];
					if (typeof k === "string") {
						v = str(k, value);
						if (v) {
							partial.push(quote(k) + (gap ? ": " : ":") + v)
						}
					}
				}
			} else {
				for (k in value) {
					if (Object.hasOwnProperty.call(value, k)) {
						v = str(k, value);
						if (v) {
							partial.push(quote(k) + (gap ? ": " : ":") + v)
						}
					}
				}
			}
			v = partial.length === 0 ? "{}" : gap ? "{\n" + gap
					+ partial.join(",\n" + gap) + "\n" + mind + "}" : "{"
					+ partial.join(",") + "}";
			gap = mind;
			return v
		}
	}
	if (typeof JSON.stringify !== "function") {
		JSON.stringify = function(value, replacer, space) {
			var i;
			gap = "";
			indent = "";
			if (typeof space === "number") {
				for (i = 0; i < space; i += 1) {
					indent += " "
				}
			} else {
				if (typeof space === "string") {
					indent = space
				}
			}
			rep = replacer;
			if (replacer
					&& typeof replacer !== "function"
					&& (typeof replacer !== "object" || typeof replacer.length !== "number")) {
				throw new Error("JSON.stringify")
			}
			return str("", {
				"" : value
			})
		}
	}
	if (typeof JSON.parse !== "function") {
		JSON.parse = function(text, reviver) {
			var j;
			function walk(holder, key) {
				var k, v, value = holder[key];
				if (value && typeof value === "object") {
					for (k in value) {
						if (Object.hasOwnProperty.call(value, k)) {
							v = walk(value, k);
							if (v !== undefined) {
								value[k] = v
							} else {
								delete value[k]
							}
						}
					}
				}
				return reviver.call(holder, key, value)
			}
			text = String(text);
			cx.lastIndex = 0;
			if (cx.test(text)) {
				text = text.replace(cx, function(a) {
					return "\\u"
							+ ("0000" + a.charCodeAt(0).toString(16)).slice(-4)
				})
			}
			if (/^[\],:{}\s]*$/
					.test(text
							.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, "@")
							.replace(
									/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
									"]").replace(/(?:^|:|,)(?:\s*\[)+/g, ""))) {
				j = eval("(" + text + ")");
				return typeof reviver === "function" ? walk({
					"" : j
				}, "") : j
			}
			throw new SyntaxError("JSON.parse")
		}
	}
}());
(function(A) {
	A.fn.outerHTML = function(B) {
		return (B) ? this.before(B).remove() : A("<p>").append(
				this.eq(0).clone()).html()
	}
})(jQuery);
jQuery.cookie = function(F, A, B) {
	if (arguments.length > 1 && String(A) !== "[object Object]") {
		B = jQuery.extend({}, B);
		if (A === null || A === undefined) {
			B.expires = -1
		}
		if (typeof B.expires === "number") {
			var E = B.expires, G = B.expires = new Date();
			G.setDate(G.getDate() + E)
		}
		A = String(A);
		return (document.cookie = [ encodeURIComponent(F), "=",
				B.raw ? A : encodeURIComponent(A),
				B.expires ? "; expires=" + B.expires.toUTCString() : "",
				B.path ? "; path=" + B.path : "",
				B.domain ? "; domain=" + B.domain : "",
				B.secure ? "; secure" : "" ].join(""))
	}
	B = A || {};
	var D, C = B.raw ? function(H) {
		return H
	} : decodeURIComponent;
	return (D = new RegExp("(?:^|; )" + encodeURIComponent(F) + "=([^;]*)")
			.exec(document.cookie)) ? C(D[1]) : null
};
var HDP_CONST_ = "Hdp";
var OVER_CONST_ = "Over";
var UNDER_CONST_ = "Under";
var ODD_CONST_ = "Odd";
var EVEN_CONST_ = "Even";
var CS_CONST_ = "CS";
var TG_CONST_ = "TG";
var ONE_CONST_ = "One";
var X_CONST_ = "X";
var TWO_CONST_ = "Two";
var OUT_CONST_ = "Out";
var FG_CONST_ = "FG";
var LG_CONST_ = "LG";
var NG_CONST_ = "NG";
var HH_CONST_ = "HH";
var HD_CONST_ = "HD";
var HA_CONST_ = "HA";
var DH_CONST_ = "DH";
var DD_CONST_ = "DD";
var DA_CONST_ = "DA";
var AH_CONST_ = "AH";
var AD_CONST_ = "AD";
var AA_CONST_ = "AA";
var D1_CONST_ = "D1";
var D2_CONST_ = "D2";
var PAR_CONST_ = "Par";
var HK_CONST_ = "HK";
var ID_CONST_ = "ID";
var MY_CONST_ = "MY";
var DEC_CONST_ = "DE";
function isValidOdds(C, A, D) {
	var B = false;
	if (C == HDP_CONST_ || C == OVER_CONST_ || C == UNDER_CONST_
			|| C == ODD_CONST_ || C == EVEN_CONST_) {
		if (D == MY_CONST_) {
			if (A > -1 && A <= 1 && A != -999) {
				return true
			}
		} else {
			if (D == DEC_CONST_) {
				if (A >= 1 && A != -999) {
					return true
				}
			} else {
				if (D == HK_CONST_ || D == ID_CONST_) {
					if (A != -999) {
						return true
					} else {
						return false
					}
				}
			}
		}
	} else {
		if (C == D1_CONST_ || C == D2_CONST_) {
			if (A != -999) {
				return true
			} else {
				return false
			}
		} else {
			switch (C) {
			case CS_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case TG_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case ONE_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case X_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case TWO_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case OUT_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case PAR_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case FG_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case LG_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case NG_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case HH_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case HD_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case HA_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case DH_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case DD_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case DA_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case AH_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case AD_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			case AA_CONST_:
				if (A >= 1 && A != -999) {
					B = true
				}
				break;
			default:
				break
			}
		}
	}
	return B
}
function getSportsCommon() {
	if (parent.frames[1]) {
		return parent.frames[1].getFormattedSports()
	} else {
		return "ALL_"
	}
}
function getMarketCommon() {
	return parent.frames[1].getMarket()
}
function getBetTypeCommon() {
	return parent.frames[1].getBetType()
}
function getAuthenticationCommon() {
	return parent.frames[1].getIsAuthenticated()
}
function getIsRunOnly() {
	return parent.frames[1].getIsRunOnly()
}
function getLanguageCommon() {
	return parent.frames[1].getLanguage()
}
function isInSport(C, A) {
	if (C != null && C != "") {
		var D = C.split("_");
		for ( var B = 0; B < D.length; B++) {
			if (D[B] == A) {
				return true
			}
		}
	}
	return false
}
function isOnlyInSport(E, G) {
	var F = false;
	var A = false;
	if (E != null && E != "") {
		var C = E.split("_");
		var D = new Array();
		for ( var B = 0; B < C.length; B++) {
			if (C[B] == G) {
				F = true
			} else {
				if (C[B] != "All" && C[B] != "") {
					D.push(C[B])
				}
			}
		}
		if (D.length > 0) {
			A = false
		} else {
			A = true
		}
		return (F && A)
	}
	return false
}
function hasSoccer(B) {
	if (B != null && B != "") {
		var C = B.split("_");
		for ( var A = 0; A < C.length; A++) {
			if (C[A] == "S" && C[A] != "") {
				return true
			}
		}
	}
	return false
}
function hasOthers(B) {
	if (B != null && B != "") {
		var C = B.split("_");
		for ( var A = 0; A < C.length; A++) {
			if (C[A] != "S" && C[A] != "" && C[A] != "All") {
				return true
			}
		}
	}
	return false
}
function DeleteChildren(A) {
	if (A) {
		for ( var B = A.childNodes.length - 1; B >= 0; B--) {
			var C = A.childNodes[B];
			if (C.hasChildNodes()) {
				DeleteChildren(C)
			}
			A.removeChild(C);
			if (C.outerHTML) {
				C.outerHTML = ""
			}
			C = null
		}
		A = null
	}
}
function formatter3(C) {
	var B = parseFloat(C);
	if (isNaN(B)) {
		B = 0
	}
	var A = "";
	if (B < 0) {
		A = "-"
	}
	B = Math.abs(B);
	B = parseInt((B + 0.0005) * 1000);
	B = B / 1000;
	s = new String(B);
	if (s.indexOf(".") < 0) {
		s += ".000"
	}
	if (s.indexOf(".") == (s.length - 2)) {
		s += "00"
	}
	if (s.indexOf(".") == (s.length - 3)) {
		s += "0"
	}
	s = A + s;
	return s
}
function formatter2(C) {
	var B = parseFloat(C);
	if (isNaN(B)) {
		B = 0
	}
	var A = "";
	if (B < 0) {
		A = "-"
	}
	B = Math.abs(B);
	B = parseInt((B + 0.005) * 100);
	B = B / 100;
	s = new String(B);
	if (s.indexOf(".") < 0) {
		s += ".00"
	}
	if (s.indexOf(".") == (s.length - 1)) {
		s += "00"
	}
	if (s.indexOf(".") == (s.length - 2)) {
		s += "0"
	}
	s = A + s;
	return s
}
function convertDt(dt) {
	var s = eval(dt.replace(/\/Date\((\d+)\)\//gi, "new Date($1)"));
	var dt = new Date(s);
	return dt
}
function convertUTC(D) {
	return D;
	var A = convertDt(A);
	A = new Date(A - 0 + 16 * 3600000);
	var F = A.getUTCHours().toString();
	var C = A.getUTCMinutes().toString();
	var B = A.getUTCSeconds().toString();
	F = F < 10 ? "0" + F : F;
	C = C < 10 ? "0" + C : C;
	B = B < 10 ? "0" + B : B;
	var E = (A.getUTCMonth() + 1).toString() + "/" + A.getUTCDate().toString()
			+ " " + F + ":" + C + ":" + B;
	return E
}
function convertDate(A) {
	var A = convertDt(A);
	var G = A.getHours().toString();
	var D = A.getMinutes().toString();
	var B = A.getSeconds().toString();
	G = G.length == 1 ? "0" + G : G;
	D = D.length == 1 ? "0" + D : D;
	B = B.length == 1 ? "0" + B : B;
	var E = (A.getMonth() + 1).toString();
	if (E.length == 1) {
		E = "0" + E
	}
	var C = A.getDate().toString();
	if (C.length == 1) {
		C = "0" + C
	}
	var F = E + "/" + C + " " + G + ":" + D + ":" + B;
	return F
}
function convertDate2(A) {
	var A = convertDt(A);
	var G = A.getHours().toString();
	var D = A.getMinutes().toString();
	var B = A.getSeconds().toString();
	G = G.length == 1 ? "0" + G : G;
	D = D.length == 1 ? "0" + D : D;
	B = B.length == 1 ? "0" + B : B;
	var E = (A.getMonth() + 1).toString();
	if (E.length == 1) {
		E = "0" + E
	}
	var C = A.getDate().toString();
	if (C.length == 1) {
		C = "0" + C
	}
	var F = E + "/" + C + " " + G + ":" + D;
	return F
}
function convertDate3(A) {
	var A = convertDt(A);
	var G = A.getHours().toString();
	var D = A.getMinutes().toString();
	var B = A.getSeconds().toString();
	G = G.length == 1 ? "0" + G : G;
	D = D.length == 1 ? "0" + D : D;
	B = B.length == 1 ? "0" + B : B;
	var E = (A.getMonth() + 1).toString();
	if (E.length == 1) {
		E = "0" + E
	}
	var C = A.getDate().toString();
	if (C.length == 1) {
		C = "0" + C
	}
	var F = E + "/" + C;
	return F
}
function convertDate4(A) {
	var A = convertDt(A);
	var G = A.getHours().toString();
	var D = A.getMinutes().toString();
	var B = A.getSeconds().toString();
	G = G.length == 1 ? "0" + G : G;
	D = D.length == 1 ? "0" + D : D;
	B = B.length == 1 ? "0" + B : B;
	var E = (A.getMonth() + 1).toString();
	if (E.length == 1) {
		E = "0" + E
	}
	var C = A.getDate().toString();
	if (C.length == 1) {
		C = "0" + C
	}
	var F = E + "/" + C + "<br>" + G + ":" + D;
	return F
}
function highlight(B, A) {
	B.style.backgroundColor = A
}
function getTimeCommon() {
	if (parent.topFrame != null && parent.topFrame.timerObject != null) {
		return parent.topFrame.timerObject.getTime()
	} else {
		return new Date()
	}
}
function setCookie(B, A, C) {
	var D = new Date();
	D.setDate(D.getDate() + C);
	document.cookie = B + "=" + escape(A)
			+ ((C == null) ? "" : ";expires=" + D.toGMTString())
}
function getCookie(A) {
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(A + "=");
		if (c_start != -1) {
			c_start = c_start + A.length + 1;
			c_end = document.cookie.indexOf(";", c_start);
			if (c_end == -1) {
				c_end = document.cookie.length
			}
			return unescape(document.cookie.substring(c_start, c_end))
		}
	}
	return ""
}
String.format = function() {
	var B = arguments[0];
	for ( var A = 0; A < arguments.length - 1; A++) {
		var C = new RegExp("\\{" + A + "\\}", "gm");
		B = B.replace(C, arguments[A + 1])
	}
	return B
};
function padLeft(A, C) {
	var B = A.toString().length;
	while (B < C) {
		A = "0" + A;
		B++
	}
	return A
};
var RefreshMessage = 1;
var isFirstLoad = false;
var OldJson = new Array("", "", "");
var ManualTimer = new Object();
var loadSportPTimer;
var userState = "+";
var GAMETYPEALL = "All";
var gameTypeCache = GAMETYPEALL;
function initSportPanel() {
	$("#hl_pending").html(X_PENDING);
	$("#hl_top").html(X_TOP10);
	$(window).data("BetViewMapJson", $("#BetViewMapJson").val().split(","));
	setSportsMapJson(jQuery.parseJSON($("#SportsMapJson").val()));
	resetSportP(false)
}
function checkManual(B, A) {
	if (ManualTimer[B.toString()] != null) {
		return false
	} else {
		ManualTimer[B.toString()] = setTimeout(function() {
			ManualTimer[B.toString()] = null
		}, (A || 1000))
	}
	return true
}
function clearManual(A) {
	ManualTimer[A] = null;
	return true
}
function is4DPage(A) {
	if (A != null) {
		$(window).data("is4DPage", A == true)
	}
	return $(window).data("is4DPage") == true
}
function loadSportP(B) {
	UpdateNoResponse(true);
	if (loadSportPTimer) {
		clearTimeout(loadSportPTimer)
	}
	loadSportPTimer = null;
	var C = is4DPage();
	var A = {
		"market" : m_market,
		"betView" : (C == true ? 1 : 0),
		"isRefresh" : RefreshMessage
	};
	callWebService("../Member/BetsView/Data.asmx/GetSportItems", JSON
			.stringify(A), function(D) {
		UpdateNoResponse(false);
		loadSportPTimer = setTimeout(function() {
			loadSportP(false)
		}, 30000);
		if (D == null) {
			if (isFirstLoad) {
				onTodayEmpty();
				goBetView("S", getDefaultBetView(), 0, null);
				isFirstLoad = false
			}
			return false
		}
		if (D && (D["Au"] == 0 || D["Au"] == false)) {
			checkRefirection(true, false)
		}
		onSportPanelLoad(D, B)
	}, function() {
		if (isFirstLoad) {
			onTodayEmpty();
			goBetView("S", getDefaultBetView(), 0, null);
			isFirstLoad = false
		}
		loadSportPTimer = setTimeout(function() {
			loadSportP(false)
		}, 30000)
	})
}
function onSportPanelLoad(G, D) {
	var B = 1;
	switch (m_market) {
	case "Early":
		B = 0;
		break;
	case "Today":
		B = 1;
		break;
	case "Running":
		B = 2;
		break;
	default:
		B = 1
	}
	RefreshMessage = 0;
	updateTopMessage(G["Marquee"]);
	updateLiveCenterSum(G);
	m_aut = G["Au"] == 1;
	var F = G["sport"];
	if (!F) {
		return
	}
	if (is4DPage()) {
		Update4DHtml(F, B);
		if (D) {
			if (F.length > 0) {
				goBetView(F[0][0], 13, F[0][2][1])
			}
		}
		return
	}
	if (B == 2) {
		if (!F || F.length == 0) {
			$("#divSport dl:eq(2)").empty()
		} else {
			if ($("#divSport dl:eq(2) dd").length == 0) {
				$("#divSport dl:eq(2)").html(GetLiveHtml(F))
			} else {
				var I = ReplaceLive($("#divSport dl:eq(2)"), F,
						OldJson[B]["sport"])
			}
		}
		OldJson[B] = G;
		$("#divSport dl:eq(2) dd").show();
		if (D) {
			$("#divSport dl:eq(2) dt").click()
		}
		return
	}
	UpdateTodayEarlyPanel(G);
	var C = $("#divSport dl:eq(" + B + ")");
	var H = getGameType();
	var A = getBetType();
	A = (A == 0 || A == 1 || A == 2 || A == 20 || A == 21) ? 1 : A;
	H = A == 7 ? "MIX" : H;
	$(C).find('dd[s="' + H + '"][b="' + A + '"]').wrapInner("<b></b>");
	if (D) {
		if (A == 7) {
			H = "MIX"
		}
		if (A == 8) {
			H = "OUT"
		}
		if (isFirstLoad) {
			isFirstLoad = false;
			C.find('dd[b="' + A + '"]').first().prev("dt").click();
			goBetView("S", getDefaultBetView(), 0, null)
		} else {
			var E = C.find('dt[s="' + H + '"]');
			if (E.length > 0) {
				E.first().click()
			} else {
				C.find("dt").first().click()
			}
		}
	}
}
function onTodayEmpty(A) {
	if (!A) {
		A = $("#divSport dl:eq(1)")
	}
	$(A)
			.html(
					'<div id="1_S"><dt onclick="SportClick(this)">Soccer<span>(0)</span></dt><dd bv="1" onclick="goBetView(\'S\',1,0,this)" style="display: block; ">HDP &amp; O/U <span>(0)</span></dd></div>');
	OldJson[1] = {
		"sport" : [ [ "S", 0, [ 1, 0, 0 ] ] ]
	}
}
function updateTopMessage(A) {
	if (parent && parent.frames[0] && parent.frames[0].UpdateNotice) {
		setTimeout(function() {
			try {
				parent.frames[0].UpdateNotice(A)
			} catch (B) {
			}
		}, 1000)
	} else {
		setTimeout(function() {
			updateTopMessage(A)
		}, 500)
	}
	if (A["M1"] || A["M2"]) {
		MessageCache(A)
	}
}
function MessageCache(A) {
	if (A) {
		$(document).data("TOPMESSAGECACHE", A)
	}
	return $(document).data("TOPMESSAGECACHE")
}
function Update4DHtml(E, A) {
	if (E) {
		var B = getSportsMapJson();
		var D = "";
		for ( var C = 0; C < E.length; C++) {
			var F = E[C];
			D += "<dt onclick=\"goBetView('" + F[0] + "',13," + F[2][1]
					+ ',this)" style="display: block; ">' + B[F[0]] + "<span>("
					+ F[2][1] + ")</span></dt>"
		}
		$("#div4D dl:eq(" + A + ")").html(D)
	}
}
function GetLiveHtml(G) {
	var C = getSportsMapJson();
	var D = $(window).data("BetViewMapJson");
	var F = "";
	var K = 0;
	var A = "";
	var E = "S";
	var I = 0;
	for ( var B = 0; B < G.length; B++) {
		var H = G[B][0];
		if (B == 0) {
			E = H;
			I = G[B][2][1]
		}
		K += G[B][2][1];
		A += H + "_";
		var J = gameTypeCache == GAMETYPEALL
				|| gameTypeCache.indexOf(H + "_") > -1 ? ' checked="checked" '
				: "";
		F += '<dd><input type="checkbox" value="'
				+ G[B][0]
				+ '" onclick="checkChange(this.checked,\''
				+ G[B][0]
				+ "',"
				+ G[B][2][1]
				+ ',this)" '
				+ J
				+ "/>"
				+ C[G[B][0]]
				+ "<span>("
				+ G[B][2][1]
				+ ')</span><img src="../App_Themes/Resources/Images/PaneLive.gif" width="30" height="10"></dd>'
	}
	if (G.length > 0) {
		var J = gameTypeCache == GAMETYPEALL ? ' checked="checked" ' : "";
		F = '<dd><input type="checkbox" value="" id="cbxAll" onclick="AllisCheck('
				+ K
				+ ",this.checked,'"
				+ E
				+ "',"
				+ I
				+ ');" '
				+ J
				+ "/>"
				+ C["All"] + "<span>(" + K + ")</span></dd>" + F;
		F = "<dt onclick=\"checkALL();SportClick(this);goBetView('"
				+ GAMETYPEALL + "', 1," + K + ');">' + D[1] + "<span>(" + K
				+ ")</span></dt>" + F
	}
	return F
}
function ReplaceLive(B, Q, J) {
	var H = getSportsMapJson();
	var K = false;
	var P = 0;
	var S = $(B).find("dd");
	var E = S.eq(0);
	var C = E.find("input").eq(0);
	var F = C && C.attr("checked");
	var O = F ? ' checked="checked"' : "";
	var R = 0;
	var I = getFormattedSports();
	I = "_" + I + "_";
	for ( var L = 0; L < J.length; L++) {
		var A = false;
		for ( var M = P; M < Q.length; M++) {
			if (J[L][0] == Q[M][0]) {
				for ( var N = P; N < M; N++) {
					K = true;
					R += Q[N][2][1];
					var G = Q[N][0];
					var D = O;
					if (I.indexOf("_" + G + "_") != -1) {
						D = ' checked="checked"'
					}
					S
							.eq(L + 1)
							.before(
									'<dd><input type="checkbox" value="'
											+ G
											+ '"'
											+ D
											+ " onclick=\"checkChange(this.checked,'"
											+ G
											+ "',"
											+ Q[N][2][1]
											+ ',this)"/>'
											+ H[G]
											+ "<span>("
											+ Q[N][2][1]
											+ ')</span><img src="../App_Themes/Resources/Images/PaneLive.gif" width="30" height="10"></dd>')
				}
				S.eq(L + 1).find("span").html("(" + Q[M][2][1] + ")");
				R += Q[M][2][1];
				P = M + 1;
				A = true;
				break
			}
		}
		if (!A) {
			S.eq(L + 1).remove()
		}
	}
	for ( var N = P; N < Q.length; N++) {
		K = true;
		R += Q[N][2][1];
		var G = Q[N][0];
		var D = O;
		if (I.indexOf("_" + G + "_") != -1) {
			D = ' checked="checked"'
		}
		B
				.append('<dd><input type="checkbox" value="'
						+ G
						+ '"'
						+ D
						+ " onclick=\"checkChange(this.checked,'"
						+ G
						+ "',"
						+ Q[N][2][1]
						+ ',this)"/>'
						+ H[G]
						+ "<span>("
						+ Q[N][2][1]
						+ ')</span><img src="../App_Themes/Resources/Images/PaneLive.gif" width="30" height="10"></dd>')
	}
	B.find("dt").find("span").html("(" + R + ")");
	E.find("span").html("(" + R + ")");
	return false
}
function GetLiveCheckedList() {
	var A = "";
	A = "";
	$("#divSport dl:eq(2) input[checked=true]").each(function(B, C) {
		var D = C.value;
		if (D) {
			A += D + "_"
		}
	});
	return A
}
function AllisCheck(F, E, C, B) {
	if (!checkManual("checkChange") || !checkManual("AllisCheck")) {
		$("#divSport dl:eq(2) input").first().attr("checked",
				E ? "" : "checked");
		return false
	}
	if (E) {
		$("#divSport dl:eq(2) input").attr("checked", "checked");
		gameTypeCache = GAMETYPEALL
	} else {
		$("#divSport dl:eq(2) input").attr("checked", "");
		var D = $("#divSport dl:eq(2) input:eq(1)");
		D.attr("checked", "checked");
		gameTypeCache = D.val() + "_"
	}
	var A = E ? F : B;
	goBetView(gameTypeCache, 1, A)
}
function checkALL() {
	$("#divSport dl:eq(2) input").attr("checked", "checked")
}
function checkChange(C, B, E, D) {
	if ($('#divSport dl:eq(2) input[id!="cbxAll"][checked=true]').length == 0) {
		$(D).attr("checked", "checked");
		return false
	}
	if (!checkManual("checkChange") || !checkManual("AllisCheck")) {
		$(D).attr("checked", C ? "" : "checked");
		return false
	}
	var A = $("#cbxAll");
	if (C && $("#divSport dl:eq(2) input[checked=false]").length == 1) {
		A.attr("checked", "checked");
		gameTypeCache = GAMETYPEALL
	} else {
		A.attr("checked", "");
		gameTypeCache = GetLiveCheckedList()
	}
	goBetView(gameTypeCache, 1, 0);
	return true
}
function BetViewDBtoWeb(A) {
	switch (A) {
	case 1:
		return 1;
	case 2:
		return 5;
	case 3:
		return 3;
	case 4:
		return 8;
	case 5:
		return 7;
	case 6:
		return 9;
	case 7:
		return 11;
	case 8:
		return 10;
	case 9:
		return 12;
	default:
		return 1
	}
}
function changeTab(A, E) {
	if (!checkManual("changeTab")) {
		return false
	}
	var D = "";
	switch (A) {
	case 0:
		D = "Early";
		break;
	case 1:
		D = "Today";
		break;
	case 2:
		D = "Running";
		break;
	default:
		break
	}
	var B = is4DPage();
	var C = B ? "div4D" : "divSport";
	m_market = D;
	$(E).parent().children().removeClass("current");
	$(E).addClass("current");
	$("#" + C + " dl").hide();
	$("#" + C + " dl:eq(" + A + ")").show();
	loadSportP(true)
}
function SportClick(A) {
	if (!checkManual("SportClick")) {
		return false
	}
	$("dd").hide();
	$(A).parent().find("dd").show();
	$(A).next().click()
}
function goBetViewParlay() {
	var A = $("dl div[s='MIX']").get(0);
	if (A) {
		$("div[s='MIX'] dt").click()
	}
}
function goBetView(E, G, C, F) {
	if ((!checkManual("goBetView"))) {
		return false
	}
	switchSportPanel(true);
	if (F) {
		var A = m_market == "Early" ? 0 : m_market == "Today" ? 1 : 2;
		var B = $("#divSport dl:eq(" + A + ")");
		B.find("b").each(function() {
			var H = $(this).html();
			$(this).replaceWith(H)
		});
		$(F).wrapInner("<b></b>")
	}
	m_allCount2 = C;
	m_allCount = C;
	setGameType(E);
	setBetType(G);
	if (E != GAMETYPEALL && E.charAt(E.length - 1) != "_") {
		E += "_"
	}
	var D = getBetUrl(G.toString(), m_market, m_mode, E, false);
	getMainPanel().location = D
}
function goTodayBetview() {
	resetSportP(false)
}
function hideShowSportP(A) {
	if (A) {
		showBetWindow(false);
		$("#NewSportPanel").show()
	} else {
		$("#NewSportPanel").hide()
	}
}
function switchSportPanel(B) {
	if (B) {
		var C = is4DPage();
		var A = C ? "#div4D" : "#divSport";
		$("#divSport, #div4D").hide();
		$(A).show();
		$(".BetBill span span:eq(0)").show();
		$(".BetBill span span:eq(1)").hide();
		$("#panelswitch").removeClass().addClass("ShoBill")
	} else {
		$("#panelswitch").removeClass().addClass("HidBill");
		$("#divSport, #div4D").hide();
		$(".BetBill span span:eq(1)").show();
		$(".BetBill span span:eq(0)").hide()
	}
}
function switchSport(B) {
	var A = $(B);
	var C = A.attr("className");
	if (C == "ShoBill") {
		switchSportPanel(false)
	} else {
		switchSportPanel(true)
	}
}
function resetSportP(A) {
	isFirstLoad = true;
	if (!checkManual("resetSportP")) {
		return false
	}
	$("#NewSportPanel li").removeClass("current");
	$("#NewSportPanel li:eq(1)").addClass("current");
	is4DPage(A);
	m_betMode = "S";
	m_market = "Today";
	hideShowSportP(true);
	if (!A) {
		$("#NewSportPanel li:eq(2)").show();
		$("#div4D").hide();
		$("#divSport").show();
		var B = getDefaultBetView();
		setBetType(B);
		newGameType = "S";
		$("#NewSportPanel li:eq(1)").trigger("click")
	} else {
		newGameType = "34D";
		setBetType(13);
		$("#NewSportPanel li:eq(2)").hide();
		$("#divSport").hide();
		$("#div4D").show();
		$("#NewSportPanel li:eq(1)").trigger("click")
	}
	switchSportPanel(true)
}
function UpdateTodayEarlyPanel(G) {
	var H = G["sport"];
	var K = G["MIX"];
	var C = G["OUT"];
	var B = $("#divSport dl:eq(" + (m_market == "Today" ? 1 : 0) + ")");
	if ((!H || !H.length) && (!K || !K.length) && (!C || !C.length)) {
		onTodayEmpty(B);
		return
	}
	var F = "";
	for ( var A = 0; A < H.length; A++) {
		F += H[A][0] + ","
	}
	$(B).find("div").filter(function() {
		var L = $(this).attr("s");
		return L == "MIX" || L == "OUT" || F.indexOf(L + ",") < 0
	}).remove();
	var I = $(B).find("div");
	var D = 0;
	for ( var A = 0; A < H.length; A++) {
		var E = H[A][0];
		var J = GetSportDivContent(H[A]);
		if (getBetType() != 7 && getBetType() != 8 && getGameType() == E) {
			J = J.replace(/none/g, "block")
		}
		if (D == I.length) {
			$(B).append(J)
		} else {
			if ($(I[D]).attr("s") == E) {
				$(I[D]).replaceWith(J);
				D++
			} else {
				$(I[D]).before(J)
			}
		}
	}
	if (K.length) {
		$(B).append(GetMixHtml(K))
	}
	if (C.length) {
		$(B).append(GetOutHtml(C))
	}
}
function GetSportDivContent(C) {
	var E = C[0];
	var B = C[1];
	var D = new Array();
	D.push('<div s="' + E + '">');
	D.push('<dt s="' + E + '" onclick="SportClick(this)">'
			+ GetSportDescFormSportType(E) + "<span>(" + B + ")</span></dt>");
	for ( var A = 2; A < C.length; A++) {
		D.push(GetBetTypeDDContent(E, C[A]))
	}
	D.push("</div>");
	var F = D.join("");
	D = null;
	return F
}
function GetBetTypeDDContent(D, E) {
	var F = E[0];
	var G = BetViewDBtoWeb(F);
	var C = E[1];
	var A = E[2];
	var B = GetBetTypeDescFromBetType(F);
	return '<dd s="'
			+ D
			+ '" b="'
			+ G
			+ '" onclick="goBetView(\''
			+ D
			+ "',"
			+ G
			+ ","
			+ C
			+ ',this)" style="display:none">'
			+ B
			+ "<span>("
			+ C
			+ ")</span>"
			+ ((m_market != "Early" && A) ? '<img src="../App_Themes/Resources/Images/PaneLive.gif" width="30" height="10">'
					: "") + "</dd>"
}
function GetMixHtml(E) {
	var B = getSportsMapJson();
	var C = $(window).data("BetViewMapJson");
	var G = E[0];
	var I = false;
	for ( var A = 1; A < E.length; A++) {
		if (E[A][2]) {
			I = true;
			break
		}
	}
	var F = I ? '<img src="../App_Themes/Resources/Images/PaneLive.gif" width="30" height="10">'
			: "";
	var D = '<div s="MIX"><dt onclick="SportClick(this)">' + B["MIX"]
			+ "<span>(" + G + ")</span>" + F + "</dt>";
	var H = B["MIX"] + "<span>(" + G + ")</span>";
	D += "<dd onclick=\"goBetView('All',7," + G + ',this)" s="MIX" b="7" '
			+ (getBetType() == 7 ? "" : 'style="display:none"') + ">" + H
			+ "</dd>";
	return D + "</div>"
}
function GetOutHtml(E) {
	var B = getSportsMapJson();
	var C = $(window).data("BetViewMapJson");
	var G = E[0];
	var D = '<div s="OUT"><dt onclick="SportClick(this)">' + B["OUT"]
			+ "<span>(" + G + ")</span></dt>";
	var F = (getBetType() == 8 ? "" : 'style="display:none"');
	for ( var A = 1; A < E.length; A++) {
		D += "<dd onclick=\"goBetView('" + E[A][0] + "',8," + E[A][1]
				+ ',this)" s="' + E[A][0] + '" b="8" ' + F + ">" + B[E[A][0]]
				+ "<span>(" + E[A][1] + ")</span></dd>"
	}
	return D + "</div>"
}
function GetSportDescFormSportType(A) {
	var B = getSportsMapJson();
	return B[A]
}
function GetBetTypeDescFromBetType(B) {
	var A = $(window).data("BetViewMapJson");
	return A[B]
}
function clickSwitchView(A) {
	switch (A) {
	case 1:
		$("div[s='MIX'] dt:eq(0)").trigger("click");
		break;
	case 2:
		$("dt[s='S']").trigger("click");
		break
	}
	return false
}
function CheckSwitchEnable(B) {
	var A = getMarket();
	return A == "Today" && (!is4DPage())
};
var DoBetting = false;
var DoSetting = false;
var DoBettingTimer = null;
var DoSettingTimer = null;
function StartBettingOrSettingTimer(A) {
	if (A) {
		DoBetting = true;
		setTimeout(function() {
			DoBetting = false
		}, 20000)
	} else {
		DoSetting = true;
		setTimeout(function() {
			DoSetting = false
		}, 20000)
	}
}
function ResetAndClearSettingOrBettingTimer(A) {
	if (A) {
		if (DoBettingTimer) {
			clearTimeout(DoBettingTimer);
			DoBettingTimer = null
		}
		DoBetting = false
	} else {
		if (DoSettingTimer) {
			clearTimeout(DoSettingTimer);
			DoSettingTimer = null
		}
		DoSetting = false
	}
}
var ShowBetConfirm = true;
function onCancel() {
	onErrorOk();
	return false
}
function onErrorOk() {
	hideShowSportP(true);
	scrollToUserInfo();
	resetAutoRefreshOdds();
	return false
}
function onBet() {
	if (DoBetting) {
		return false
	}
	var F = cacheBetData();
	var D = null;
	if (F && (D = F.split(",")) && D[10] && D[10] == "1") {
		var B = $("#pnParlayC > table").length;
		if (B < 3) {
			return !!alert(TXT_PARLAY_ITEMS_LIMIT)
		}
	}
	hideButtons();
	if (onCheckStake()) {
		if (confirm(confirmMessage)) {
			StartBettingOrSettingTimer(true);
			try {
				getMainPanel().RunningTableUI.getInstance().pauseUpdate()
			} catch (H) {
			}
			if (!F) {
				return
			}
			D[13] = $("#tx_stake").val().replace(/,/g, "");
			var A = D.join(",");
			var E = "../Member/BetsView/Bet.asmx/BetNow";
			var G = BetCode(C);
			A = {
				"data" : A,
				"isAuto" : getIsAutoOdds(),
				"s" : G
			};
			var I = JSON.stringify(A);
			callWebService(E, I, onBetSuccess, onBetRealFailed);
			return true
		}
	}
	showButtons();
	focusBetStake();
	return false
}
function BetCode(B) {
	var D = B.split("");
	for ( var A = 0; A < D.length; A++) {
		D[A] = D[A] + String(Math.floor((Math.random() * 10)))
	}
	return D.reverse().join("")
}
function checkOddsShortClick(A) {
	if (A == lastOddsClick) {
		return false
	}
	lastOddsClick = A;
	if (lastOddsClickTimer) {
		clearTimeout(lastOddsClickTimer)
	}
	lastOddsClickTimer = setTimeout(function() {
		lastOddsClick = ""
	}, 1000);
	return true
}
function onOddsClick(O, W, h) {
	if (DoSetting) {
		return false
	}
	var U = O + W + "";
	if (!checkOddsShortClick(U)) {
		return false
	}
	scroll(0, 0);
	CheckAndLoadAllBetList();
	var F = O;
	if (F) {
		var K = F.split(",");
		var J = null;
		try {
			J = getMainPanel().getData(K[0] + "_" + K[2] + "_" + K[3]);
			if (J != null) {
				K = J.split(",")
			}
		} catch (N) {
		}
		var P;
		var c;
		var f;
		var T;
		var g;
		var L;
		var B;
		var D;
		var R;
		var V;
		var S;
		var Q;
		var H;
		var I = 0;
		var a = 0;
		var X = 0;
		var Y = 0;
		if (K != null && K.length > 0) {
			var Z = null;
			var M = null;
			try {
				M = getMainPanel().getData(K[0] + "_INFO");
				if ((M && J) || h) {
					Z = M.split(",")
				}
			} catch (N) {
			}
			if (Z != null && Z.length > 0) {
				P = K[0];
				f = K[2];
				c = K[1];
				T = K[3];
				B = K[4];
				g = Z[1];
				L = Z[2];
				D = Z[3];
				R = Z[4];
				V = Z[5];
				S = Z[6];
				Q = Z[7];
				H = Z[9];
				I = Z[14];
				m_is1H = I == 1
			} else {
				P = K[0];
				c = K[1];
				f = K[2];
				T = K[3];
				g = K[4];
				L = K[5];
				B = K[6];
				D = K[7];
				R = K[8];
				V = K[9];
				S = K[10];
				Q = K[11];
				H = K[13];
				m_is1H = K[14] == 1
			}
			StartBettingOrSettingTimer(false);
			hideButtons();
			var A = Math.random();
			$("#tx_ran_code").val(A);
			var G = P + "," + c + "," + f + "," + T + "," + g + "," + L + ","
					+ B + "," + D + "," + R + "," + V + "," + S + "," + Q + ","
					+ I + ",0," + A;
			var E = BetCode(C);
			d = JSON.stringify({
				"data" : G,
				"isAuto" : getIsAutoOdds(),
				"s" : E
			});
			var b = "../Member/BetsView/Bet.asmx/SetData";
			callWebService(b, d, function(e) {
				ResetAndClearSettingOrBettingTimer(false);
				onSetDataMessage(e, G)
			}, onSetDataFailed)
		}
	}
}
function KeyDownHandler(B) {
	var D = (B) ? B : window.event;
	var A = (D.keyCode) ? D.keyCode : D.which;
	if (A == 13) {
		D.cancelBubble = true;
		if (isIE) {
			B.returnValue = false
		} else {
			B.stopPropagation();
			B.preventDefault()
		}
		onBet();
		return false
	} else {
		if (!((A >= 48 && A <= 57) || (A >= 96 && A <= 105) || A == 8
				|| A == 46 || A == 17)) {
			D.cancelBubble = true;
			if (isIE) {
				B.returnValue = false
			} else {
				B.stopPropagation();
				B.preventDefault()
			}
			return false
		}
	}
	return true
}
function onStakeChanged(F) {
	var B = $(F).val();
	var D = parseInt(B.replace(/,/g, ""), 10);
	var E = $("#tx_bet_type").val();
	var A = $("#tx_bet_odds").val();
	A = parseFloat(A);
	updateEstPayout(A, D, E);
	var G = getFormatNumber(D);
	if (G != "") {
		$(F).val(G)
	}
	return false
}
function updateEstPayout(A, F, E) {
	var B = "0";
	if (!isNaN(F)) {
		var D = new Mask("#########", "currencymask");
		if (E == "Hdp" || E == "Over" || E == "Under" || E == "Odd"
				|| E == "Even" || E == "D1" || E == "D2") {
			if (A < 0) {
				B = D.FormatNumber(String(F))
			} else {
				B = D.FormatNumber(formatter2(A * F))
			}
		} else {
			B = D.FormatNumber(formatter2((A - 1) * F))
		}
	}
	$("#lb_estPayVal").html(B);
	return true
}
function getFormatNumber(A) {
	if (!isNaN(A)) {
		var B = new Mask("#########", "currencymask");
		A = B.FormatNumber(String(A));
		return A
	}
	return ""
}
function refreshBetOdds(A) {
	$("#lb_bet_odds").removeClass("oddschg");
	$("#lb_hdpscore").removeClass("scorechg");
	$("#lb_hdpball").removeClass("ballchg");
	if (A["OddsChanged"]) {
		updateBetOdds(A["Odds"]);
		updateCacheBetData(A, true)
	}
}
function getBetCacheData() {
	return cacheBetData()
}
var DEFIND_COLOR = {
	"A" : "black",
	"B" : "#002273",
	"C" : "#B50000",
	"D" : "#002273"
};
var m_BetDelayTimer = null;
var ajaxManager = null;
var lastOddsClick = "";
var lastOddsClickTimer = null;
function cacheBetData(A) {
	if (typeof (A) == "string") {
		$(window).data("BETCACHE", A);
		return A
	} else {
		return $(window).data("BETCACHE")
	}
}
function updateCacheBetData(G, B) {
	if (G.StatusCode == 3) {
		var F = cacheBetData();
		if (F) {
			var D = F.split(",");
			D[1] = G.Odds;
			D[5] = G.IsHomeGive ? 1 : 0;
			D[6] = G.Ball;
			if (G.IsRun) {
				D[7] = "Running"
			}
			D[8] = G.HomeScore;
			D[9] = G.AwayScore;
			cacheBetData(D.join(","));
			if (B) {
				var E = D[3] != "Away";
				var A = $("#tx_bet_type").val();
				switch (A) {
				case "Hdp":
					setRunningScore(G.IsRun, G.HomeScore, G.AwayScore);
					setUpdateReleaseBall(E, G.IsHomeGive, G.Ball);
					break;
				case "Over":
				case "Under":
					setRunningScore(G.IsRun, G.HomeScore, G.AwayScore);
					setUpdateReleaseBall(E, true, G.Ball);
					break;
				case "One":
				case "X":
				case "Two":
					setRunningScore(G.IsRun, G.HomeScore, G.AwayScore);
					break
				}
			}
			D = null
		}
	}
}
function setRunningScore(A, E, D) {
	$("#lb_hdpscore").removeClass("scorechg");
	if (!A) {
		return
	}
	var B = "[" + E + "-" + D + "]";
	var F = $("#lb_hdpscore").text();
	if (B != F) {
		$("#lb_hdpscore").html(B).addClass("scorechg")
	}
}
function setUpdateReleaseBall(F, G, D) {
	$("#lb_hdpball").removeClass("ballchg");
	var A = "0";
	var B = false;
	if (D > 0) {
		if ((F && G) || ((!F) && (!G))) {
			A = "-" + D;
			B = true
		} else {
			A = "+" + D
		}
	}
	var E = $("#lb_hdpball").text();
	if (A != E) {
		$("#lb_hdpball").html(A).css("color", B ? "#B50000" : "#002273")
				.addClass("ballchg")
	}
}
function getIsAutoOdds() {
	return $("#chk_auto").attr("checked") != ""
}
function onCheckStake() {
	var B = $("#tx_stake");
	var G = $("#tx_min_bet").val();
	var I = $("#tx_max_bet").val();
	var H = B.val().replace(/,/g, "");
	var F = parseInt(H, 10);
	var A = parseFloat(G);
	var D = parseFloat(I);
	if (isNaN(F) || F <= 0) {
		alert(error);
		B.val(A);
		return false
	}
	if (F < A || F > D) {
		var E = F < A ? A : D;
		alert(error);
		B.val(E);
		return false
	}
	return true
}
function showBetError(A) {
	scrollToUserInfo();
	showBetWindow(true);
	resetAutoRefreshOdds();
	$("#BetWindowTitle,#BetWindow").hide();
	$("#BetErrorMessage").show();
	$("#lb_msg").html(A)
}
function onBetSuccess(A) {
	ResetAndClearSettingOrBettingTimer(true);
	resetAutoRefreshOdds();
	if (A && (A["Au"] == 0 || A["Au"] == false)) {
		checkRefirection(true, false)
	}
	A = A.d;
	if (A && A.StatusCode != null) {
		switch (A.StatusCode) {
		case 0:
		case 1:
			processSuccessBetList(A);
			break;
		case 2:
			onErrorOk();
			alert(A.Message);
			break;
		case 3:
			updateBetOdds(A.Odds);
			updateCacheBetData(A, true);
			showButtons();
			focusBetStake();
			restartAutoRefreshOdds();
			return false;
			break;
		case 4:
			onErrorOk();
			alert(A.Message);
			break;
		case 5:
			updateParlayOdds(A);
			alert(A.Message);
			focusBetStake();
			break;
		case 998:
			alert(A.Message);
			redirectOnLogout();
			break;
		case 999:
			onErrorOk();
			break;
		default:
			showBetError(A.Message);
			break
		}
		showButtons()
	}
}
function processSuccessBetList(A) {
	var B = A["BetData"];
	if (B && B.length && B[0]["RefNo"]) {
		hideShowSportP(true);
		switchSportPanel(false);
		try {
			UpdateBetListArray(B);
			onUserInfoRefresh(true)
		} catch (D) {
		}
		cacheBetData("");
		if (ShowBetConfirm) {
			alert(A.Message)
		}
		$("#tx_stake").val("")
	} else {
		showBetError(commonErrorMessage)
	}
}
function onBetRealFailed(A) {
	ResetAndClearSettingOrBettingTimer(true);
	showButtons();
	onErrorOk();
	ResetAndLoadFullBetList()
}
function focusBetStake() {
	$("#tx_stake").trigger("focus").trigger("select")
}
function hideButtons() {
	$("#tx_stake").attr("disabled", "disabled").trigger("blur");
	$("#btnBet").attr("disabled", "disabled");
	$("#btnCancel").attr("disabled", "disabled")
}
function showButtons() {
	$("#btnBet").attr("disabled", "");
	$("#btnCancel").attr("disabled", "");
	$("#tx_stake").attr("disabled", "")
}
function onRemoveItem(E) {
	var G = $(E).attr("socId");
	G = parseInt(G, 10);
	var A = {
		"socId" : G,
		"isAuto" : getIsAutoOdds()
	};
	var F = JSON.stringify(A);
	m_deleteItem = true;
	try {
		var B = "../Member/BetsView/Bet.asmx/DeleteParlayItem";
		callWebService(B, F, onParlayItemRemoved, onBetFailed)
	} catch (D) {
		hideLoading()
	}
}
function onParlayItemRemoved(A) {
	if (A && (A["Au"] == 0 || A["Au"] == false)) {
		checkRefirection(true, false)
	}
	if (A && A.d) {
		A = A.d;
		checkRefirection(getAuthenticationCommon(), A.IsAuthenticated);
		if (A.StatusCode == 0 || A.StatusCode == 5) {
			processBetData(A, A.Delay);
			return true
		} else {
			showBetError(A.Message);
			return false
		}
	}
	showButtons();
	onErrorOk();
	return false
}
function onBetFailed(B, E, A) {
	hideLoading();
	var D = $("#hf_redirectUrl").val();
	HandleError(B, D)
}
function onSetDataMessage(A, B) {
	if (A && (A["Au"] == 0 || A["Au"] == false)) {
		checkRefirection(true, false)
	}
	if (A && A.d) {
		A = A.d
	} else {
		showButtons();
		onErrorOk();
		return
	}
	checkRefirection(getAuthenticationCommon(), A.IsAuthenticated);
	if (!A.IsAuthenticated) {
		return
	}
	switch (A.StatusCode) {
	case 0:
	case 3:
	case 5:
		cacheBetData(B);
		processBetData(A, A.Delay);
		return;
		break;
	case 6:
	case 7:
		if (A.CanClose) {
			onErrorOk()
		}
		alert(A.Message);
		break;
	case 998:
		alert(A.Message);
		redirectOnLogout();
		break;
	case 999:
		onErrorOk();
		break;
	default:
		showBetError(A.Message);
		break
	}
	showButtons()
}
function updateBetOdds(A) {
	var D = $("#tx_bet_type").val();
	var B = parseInt($("#tx_stake").val(), 10);
	$("#tx_bet_odds").val(A);
	$("#lb_bet_odds").removeClass("oddschg").addClass("oddschg");
	setOddsFormat(A, D, false);
	updateEstPayout(A, B, D);
	return true
}
function updateParlayOdds(A) {
	processBetData(A, A.Delay)
}
function processBetData(H, A) {
	if (A > 0) {
		if (m_BetDelayTimer != null) {
			clearTimeout(m_BetDelayTimer)
		}
		m_BetDelayTimer = setTimeout(function() {
			processBetData(H, 0)
		}, A * 1000);
		return
	}
	updateCacheBetData(H);
	H = H.BetData;
	var G = new Mask("#########", "currencymask");
	var I = Math.ceil(H["IN"]);
	var J = Math.floor(H["AX"]);
	var F = H["B"];
	$("#tx_min_bet").val(I);
	$("#tx_max_bet").val(J);
	$("#tx_bet_type").val(F);
	$("#tx_bet_odds").val(H["O"]);
	$("#lb_estPayVal").html("0");
	$("#lb_bet_limit_val").html(
			I + " / " + G.FormatNumber(String(J).split(".")[0]));
	$("#lb_bet_odds").removeClass("oddschg");
	$("#td_tbg").css("background", "#B7D6FF");
	var D = H["IP"];
	if (!D) {
		$("#tr_chk_auto").hide();
		setBetTitle(F, H["ST"]);
		$("#pnParlay").hide();
		$("#pnNormal").show();
		if (H["GT"] == 0) {
			$("#td_tbg").css("background", "#FDD9D9")
		}
		$("#lb_bet_team").html(H["BT"]);
		$("#lb_hdpball").html(H["RB"]).css("color",
				H["GB"] ? "#B50000" : "#002273").removeClass("ballchg");
		if (H["CB"]) {
			$("#lb_hdpball").addClass("ballchg")
		}
		$("#lb_hdpscore").html(H["S"]).removeClass("scorechg");
		if (H["CS"]) {
			$("#lb_hdpscore").addClass("scorechg")
		}
		setOddsFormat(H["O"], F, false);
		if (H["C"]) {
			updateBetOdds(H["O"])
		}
		$("#lb_league").html(H["E"]).css("text-decoration", "none");
		if (F == "D1" || F == "D2" || F == "Out") {
			$("#lb_league").css("text-decoration", "underline")
		}
		$("#lb_home_team").html(H["H"]);
		$("#lb_away_team").html(H["A"])
	} else {
		$("#pnNormal").hide();
		$("#tr_chk_auto").hide();
		setBetTitle("Par", "");
		setOddsFormat(H["O"], "Par", true);
		$("#lb_bet_team,#lb_hdpball,#lb_hdpscore").html("");
		var K = [];
		if (H["PD"]) {
			for ( var B = 0; B < H["PD"].length; B++) {
				var E = H["PD"][B];
				K.push(parseTemplate($("#ParlayRow").html(), E))
			}
		}
		$("#pnParlay").show();
		$("#pnParlayC").html(K.join(""));
		K = null
	}
	showBetWindow(true);
	initDefaultBet(I, J, D);
	focusBetStake();
	if (D) {
		resetAutoRefreshOdds()
	} else {
		restartAutoRefreshOdds()
	}
	H = null;
	G = null
}
function initDefaultBet(E, G, B) {
	var D = $(window).data("pBet");
	var A = $("#tx_stake");
	E = +E;
	G = +G;
	var F = +(A.val().replace(/,/g, ""));
	if ((!B) || (isNaN(F) || F < E || F > G)) {
		if (D < E) {
			D = E
		}
		if (D > G) {
			D = G
		}
		A.val(D)
	}
	onStakeChanged(document.getElementById("tx_stake"))
}
function setBetTitle(A, B) {
	var D = "";
	switch (A) {
	case "Hdp":
		D = HDP_V_CONST + B;
		break;
	case "Over":
	case "Under":
		D = OU_V_CONST + B;
		break;
	case "Odd":
	case "Even":
		D = OE_V_CONST;
		break;
	case "TG":
		D = TG_V_CONST;
		break;
	case "CS":
		D = CS_V_CONST;
		break;
	case "FG":
	case "LG":
	case "NG":
		D = FGLG_V_CONST;
		break;
	case "HH":
	case "HD":
	case "HA":
	case "DH":
	case "DD":
	case "DA":
	case "AH":
	case "AD":
	case "AA":
		D = HTFT_V_CONST;
		break;
	case "One":
	case "Two":
	case "X":
		D = X12_V_CONST + B;
		break;
	case "Out":
		D = OUT_V_CONST;
		break;
	case "D1":
		D = D1_CONST;
		break;
	case "D2":
		D = D2_CONST;
		break
	}
	$("#lb_title").html(D)
}
function setParlayItemRB(A, B) {
	if (B) {
		switch (A) {
		case "FG":
		case "LG":
		case "NG":
			return "color: #666666;";
		default:
			return "color: " + DEFIND_COLOR["C"] + ";"
		}
	} else {
		return "color: " + DEFIND_COLOR["B"] + ";"
	}
}
function setOddsFormat(A, D, B) {
	var E = A;
	switch (D) {
	case "CS":
		break;
	case "Hdp":
	case "Over":
	case "Under":
	case "Odd":
	case "Even":
	case "D3":
	case "D4":
		E = parseFloat(A).toFixed(3);
		break;
	default:
		E = parseFloat(A).toFixed(2);
		break
	}
	if (B) {
		E = parseFloat(A).toFixed(3)
	}
	$("#lb_bet_odds").html(E).css("color", A < 0 ? "#B50000" : "#002273")
}
function showBetWindow(A) {
	$("#BetErrorMessage").hide();
	if (A) {
		hideShowSportP(false);
		hideUserInfo();
		$("#menu").show();
		$("#BetWindowTitle,#BetWindow").show();
		scrollToUserInfo();
		showButtons()
	} else {
		$("#menu").hide();
		resetAutoRefreshOdds()
	}
}
function onSetDataFailed(A) {
	ResetAndClearSettingOrBettingTimer(false);
	try {
		if (A.responseText.indexOf("Authentication failed.") > -1) {
			checkRefirection(true, false, m_redirectUrl)
		}
	} catch (B) {
	}
	showButtons();
	onErrorOk()
};
function initMiniBetListPanel() {
	$("#img_mini").bind("click", collapseBetList);
	$("#imgBtn_betRefresh").bind("click", RefreshBetList)
}
function unloadForMiniBetList() {
	betListArray = null;
	$("#img_mini").unbind("click");
	$("#imgBtn_betRefresh").unbind("click")
}
function onChangeMenu(A) {
	onShowBetList(A)
}
function SetMiniState(A) {
	miniState = A ? "+" : "-"
}
function CheckMiniBetListClosed() {
	return miniState == "-"
}
function collapseBetList() {
	if (!betListTimer) {
		onLoadFullBetList();
		setBetListTimer()
	}
	var A = getActiveMenu();
	var B = CheckMiniBetListClosed();
	showMiniBetList(B, A)
}
function ProcessCollapseBetList(A) {
	if (A) {
		ResetMiniBetListAutoDisplay(false);
		SetMiniState(A);
		$("#miniBetList").show();
		$("#img_mini").removeClass().addClass("ShoBill")
	} else {
		SetMiniState(A);
		$("#miniBetList").hide();
		$("#img_mini").removeClass().addClass("HidBill")
	}
}
function showMiniBetList(B, A) {
	ProcessCollapseBetList(B);
	if (B && A) {
		onShowBetList(A)
	}
}
function UpdateBetListArray(B) {
	if (B && B.length) {
		var A = B[0]["DangerStatus"] == "D" ? 1 : 2;
		if (betListArray != null) {
			betListArray = B.concat(betListArray)
		} else {
			betListArray = B
		}
		updateBetListHtml();
		showMiniBetList(true, A)
	}
}
function CheckAndLoadAllBetList() {
	if (!betListTimer) {
		onLoadFullBetList();
		setBetListTimer()
	}
}
function RefreshBetList() {
	if (CheckTollete("RefreshBetList", 5)) {
		onLoadFullBetList();
		setBetListTimer()
	}
}
var betListDebugger = false;
var betListLoaded = false;
var betListIncShortTime = 5000;
var betListIncLongTime = 60000;
var betListTimer = null;
var betListTimerCount = 0;
var betListTimerFullCount = 0;
var betListArray = null;
var miniState = "-";
var miniBetListActive = 1;
var miniBetListAutoDisplay = false;
var betListVersionOrigin = "00/00/0000 00:00:00.0000000";
var betListVersion = "00/00/0000 00:00:00.0000000";
var TolleteList = new Object();
function getActiveMenu() {
	return miniBetListActive
}
function setActiveMenu(A) {
	miniBetListActive = A
}
function sorttosmall(B, A) {
	if (B > A) {
		return -1
	} else {
		if (B < A) {
			return 1
		} else {
			return 0
		}
	}
}
var _processedPar = new Array();
var _glCount = 0;
var _parlayPlaceHolder = "";
function CheckTollete(A, B) {
	if (TolleteList[A]) {
		return false
	}
	TolleteList[A] = true;
	setTimeout(function() {
		TolleteList[A] = null;
		delete TolleteList[A]
	}, B * 1000);
	return true
}
function getBetTeam(D, A, M, G, C, K, E, F, J) {
	var I = "";
	var L = new Array();
	var H = getOddsColor(K);
	switch (D) {
	case D3_CONST:
		L = processHdp(A, M, G, C, K, E, F, J);
		break;
	case D4_CONST:
		L = processOU(A, M, G, C, K, E, F, J);
		break;
	case HDP_CONST:
		L = processHdp(A, M, G, C, K, E, F, J);
		break;
	case OU_CONST:
		L = processOU(A, M, G, C, K, E, F, J);
		break;
	case OE_CONST:
		L = processOE(A, M, G, C, K, E, F, J);
		break;
	case ONE_CONST:
		L = process1X2(ONE_CONST, A, M, G, C, K, E, F, J);
		break;
	case X_CONST:
		L = process1X2(X_CONST, A, M, G, C, K, E, F, J);
		break;
	case TWO_CONST:
		L = process1X2(TWO_CONST, A, M, G, C, K, E, F, J);
		break;
	case CS_CONST:
		L = processCommon(CS_CONST, A, M, G, C, K, E, F, J);
		break;
	case TG_CONST:
		L = processCommon(TG_CONST, A, M, G, C, K, E, F, J);
		break;
	case "OUT":
		L = processCommon(OUT_CONST, A, M, G, C, K, E, F, J);
		break;
	case D1_CONST:
		L = processCommon(D1_CONST, A, M, G, C, K, E, F, J);
		break;
	case D2_CONST:
		L = processCommon(D2_CONST, A, M, G, C, K, E, F, J);
		break;
	case "FLG":
		var B = "";
		if (J == "01" || J == "1") {
			B = FG_CONST;
			J = G + " (" + FG_V_CONST + ")"
		} else {
			if (J == "02" || J == "2") {
				B = FG_CONST;
				J = C + " (" + FG_V_CONST + ")"
			} else {
				if (J == "03" || J == "3") {
					B = LG_CONST;
					J = G + " (" + LG_V_CONST + ")"
				} else {
					if (J == "04" || J == "4") {
						B = LG_CONST;
						J = C + " (" + LG_V_CONST + ")"
					} else {
						B = NG_CONST;
						J = NG_V_CONST
					}
				}
			}
		}
		L = processCommon(B, A, M, G, C, K, E, F, J);
		break;
	case "HFT":
		var B = getFTHTName(J);
		J = B[1];
		L = processCommon(B[0], A, M, G, C, K, E, F, J);
		break;
	case NG_CONST:
		L = processCommon(LG_CONST, A, M, G, C, K, E, F, J);
		break;
	case HH_CONST:
		L = processCommon(HH_CONST, A, M, G, C, K, E, F, J);
		break;
	case HD_CONST:
		L = processCommon(HD_CONST, A, M, G, C, K, E, F, J);
		break;
	case HA_CONST:
		L = processCommon(HA_CONST, A, M, G, C, K, E, F, J);
		break;
	case DH_CONST:
		L = processCommon(DH_CONST, A, M, G, C, K, E, F, J);
		break;
	case DD_CONST:
		L = processCommon(DD_CONST, A, M, G, C, K, E, F, J);
		break;
	case DA_CONST:
		L = processCommon(DA_CONST, A, M, G, C, K, E, F, J);
		break;
	case AH_CONST:
		L = processCommon(AH_CONST, A, M, G, C, K, E, F, J);
		break;
	case AD_CONST:
		L = processCommon(AD_CONST, A, M, G, C, K, E, F, J);
		break;
	case AA_CONST:
		L = processCommon(AA_CONST, A, M, G, C, K, E, F, J);
		break;
	default:
		break
	}
	if (L.length == 0) {
		L.push("");
		L.push("");
		L.push("");
		L.push("");
		L.push("");
		L.push("");
		L.push("");
		L.push("")
	}
	L.push(H);
	return L
}
function getFTHTName(B) {
	var A = new Array();
	switch (B) {
	case "01":
		A.push(HH_CONST);
		A.push(HH_V_CONST);
		break;
	case "02":
		A.push(HA_CONST);
		A.push(HA_V_CONST);
		break;
	case "03":
		A.push(HD_CONST);
		A.push(HD_V_CONST);
		break;
	case "04":
		A.push(AH_CONST);
		A.push(AH_V_CONST);
		break;
	case "05":
		A.push(AA_CONST);
		A.push(AA_V_CONST);
		break;
	case "06":
		A.push(AD_CONST);
		A.push(AD_V_CONST);
		break;
	case "07":
		A.push(DH_CONST);
		A.push(DH_V_CONST);
		break;
	case "08":
		A.push(DA_CONST);
		A.push(DA_V_CONST);
		break;
	case "09":
		A.push(DD_CONST);
		A.push(DD_V_CONST);
		break;
	case "1":
		A.push(HH_CONST);
		A.push(HH_V_CONST);
		break;
	case "2":
		A.push(HA_CONST);
		A.push(HA_V_CONST);
		break;
	case "3":
		A.push(HD_CONST);
		A.push(HD_V_CONST);
		break;
	case "4":
		A.push(AH_CONST);
		A.push(AH_V_CONST);
		break;
	case "5":
		A.push(AA_CONST);
		A.push(AA_V_CONST);
		break;
	case "6":
		A.push(AD_CONST);
		A.push(AD_V_CONST);
		break;
	case "7":
		A.push(DH_CONST);
		A.push(DH_V_CONST);
		break;
	case "8":
		A.push(DA_CONST);
		A.push(DA_V_CONST);
		break;
	case "9":
		A.push(DD_CONST);
		A.push(DD_V_CONST);
		break;
	default:
		break
	}
	return A
}
function processHdp(L, F, D, E, G, I, C, B) {
	var A = "";
	var J = new Array();
	var H = getOddsColor(G);
	if (L) {
		A = F == true ? "1h-" + D : "1h-" + E
	} else {
		A = F == true ? D : E
	}
	var K = getHdpColor(I, C, F);
	J.push(A);
	J.push(K[4]);
	J.push(K[2]);
	J.push(K[3]);
	J.push(K[0]);
	J.push(K[1]);
	J.push(formatter3(G));
	J.push(HDP_V_CONST);
	return J
}
function processOU(A, N, G, B, M, C, F, L) {
	var D = "";
	var K = new Array();
	var I = getOddsColor(M);
	var J;
	var H;
	var E = getHdpColor(C, F, N);
	if (N) {
		J = getOUColor(OVER_CONST);
		H = getOUReleaseBall(OVER_CONST, F);
		K.push(OVER_V_CONST)
	} else {
		J = getOUColor(UNDER_CONST);
		H = getOUReleaseBall(UNDER_CONST, F);
		K.push(UNDER_V_CONST)
	}
	K.push(J);
	K.push(E[2]);
	K.push(E[3]);
	K.push(H[0]);
	K.push(H[1]);
	K.push(formatter3(M));
	K.push(OU_V_CONST);
	return K
}
function processOE(K, F, D, E, G, I, C, B) {
	var A = "";
	var J = new Array();
	var H = getOddsColor(G);
	if (F) {
		J.push(ODD_V_CONST)
	} else {
		J.push(EVEN_V_CONST)
	}
	J.push(HTML_BLACK);
	J.push(HTML_BLUE);
	J.push(HTML_BLUE);
	J.push("");
	J.push("");
	J.push(formatter3(G));
	J.push(OE_V_CONST);
	return J
}
function process1X2(H, L, G, E, F, D, J, K, C) {
	var B = "";
	var A = new Array();
	var I = getOddsColor(D);
	if (L) {
		B = G == true ? "1h-" + E : "1h-" + F
	} else {
		B = G == true ? E : F
	}
	if (H == ONE_CONST) {
		if (L) {
			A.push(HT_V_CONST + "." + ONE_V_CONST)
		} else {
			A.push(FT_V_CONST + "." + ONE_V_CONST)
		}
	} else {
		if (H == X_CONST) {
			if (L) {
				A.push(HT_V_CONST + "." + X_V_CONST)
			} else {
				A.push(FT_V_CONST + "." + X_V_CONST)
			}
		} else {
			if (H == TWO_CONST) {
				if (L) {
					A.push(HT_V_CONST + "." + TWO_V_CONST)
				} else {
					A.push(FT_V_CONST + "." + TWO_V_CONST)
				}
			}
		}
	}
	A.push(HTML_BLACK);
	A.push(HTML_BLUE);
	A.push(HTML_BLUE);
	A.push("");
	A.push("");
	A.push(formatter3(D));
	A.push(X12_V_CONST);
	return A
}
function processCommon(B, L, F, D, E, G, I, C, H) {
	var A = "";
	var J = new Array();
	var K = getOddsColor(G);
	J.push(H);
	J.push(HTML_BLACK);
	J.push(HTML_BLUE);
	J.push(HTML_BLUE);
	J.push("");
	J.push("");
	switch (B) {
	case CS_CONST:
		J.push(formatter2(G));
		J.push(CS_V_CONST);
		break;
	case TG_CONST:
		J.push(formatter3(G));
		J.push(TG_V_CONST);
		break;
	case OUT_CONST:
		J.push(formatter3(G));
		J.push(OUT_V_CONST);
		break;
	case D1_CONST:
		J.push(formatter3(G));
		J.push(D1_V_CONST);
		break;
	case D2_CONST:
		J.push(formatter3(G));
		J.push(D2_V_CONST);
		break;
	case FG_CONST:
		J.push(formatter3(G));
		J.push(FGLG_V_CONST);
		break;
	case LG_CONST:
		J.push(formatter3(G));
		J.push(FGLG_V_CONST);
		break;
	case NG_CONST:
		J.push(formatter3(G));
		J.push(FGLG_V_CONST);
		break;
	case HH_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	case HD_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	case HA_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	case DH_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	case DD_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	case DA_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	case AH_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	case AD_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	case AA_CONST:
		J.push(formatter3(G));
		J.push(HTFT_V_CONST);
		break;
	default:
		break
	}
	return J
}
function getOddsColor(A) {
	if (A < 0) {
		return HTML_RED
	} else {
		return HTML_BLUE
	}
}
function processParlay(K, I) {
	var J = "";
	var B = 0;
	var C = null;
	var E = "";
	var D = "";
	for ( var A = I; A < K.length; A++) {
		if (K[A].SocTransId == J || J == "") {
			var H = K[A];
			if (H.TransType != PAR_CONST) {
				break
			}
			C = H;
			B++;
			J = K[A].SocTransId;
			if (B == 1) {
				_processedPar.push(J);
				var G = parseTemplate($("#ParHeader").html(), H);
				D = G
			}
			var G = parseTemplate($("#ParSnippet").html(), H);
			E += G;
			if ((I + B) == K.length) {
				H.Counter = B;
				G = parseTemplate($("#ParSummary").html(), H);
				var F = "#" + C.SocTransId + "_Par_Header";
				E = D + G + E
			}
		} else {
			H.Counter = B;
			G = parseTemplate($("#ParSummary").html(), H);
			var F = "#" + C.SocTransId + "_Par_Header";
			E = D + G + E;
			break
		}
	}
	return E
}
function toPar(A) {
	for ( var B = 0; B < _processedPar.length; B++) {
		if (A == _processedPar[B]) {
			return false
		}
	}
	return true
}
function getHdpColor(F, B, E) {
	var H = new Array();
	var A = "";
	var G = "";
	var C = "";
	var I = "";
	var D = "";
	if (B > 0) {
		if (E && F) {
			A = HTML_RED;
			G = "-" + B
		} else {
			if (E && !F) {
				A = HTML_BLUE;
				G = "+" + B
			} else {
				if (!E && F) {
					A = HTML_BLUE;
					G = "+" + B
				} else {
					if (!E && !F) {
						A = HTML_RED;
						G = "-" + B
					} else {
						A = HTML_BLUE;
						G = "+" + B
					}
				}
			}
		}
	} else {
		if (B == 0) {
			A = HTML_BLUE;
			G = B
		}
	}
	if (F && B > 0) {
		C = HTML_RED;
		I = HTML_BLUE;
		if (E) {
			D = HTML_RED
		} else {
			D = HTML_BLUE
		}
	} else {
		if (!F && B > 0) {
			C = HTML_BLUE;
			I = HTML_RED;
			if (E) {
				D = HTML_BLUE
			} else {
				D = HTML_RED
			}
		} else {
			if (B == 0) {
				C = HTML_BLUE;
				I = HTML_BLUE;
				D = HTML_BLUE
			}
		}
	}
	H.push(A);
	H.push(G);
	H.push(C);
	H.push(I);
	H.push(D);
	return H
}
function getOUColor(B) {
	var A = "";
	if (B == OVER_CONST) {
		A = HTML_RED
	} else {
		if (B == UNDER_CONST) {
			A = HTML_BLUE
		}
	}
	return A
}
function getOUReleaseBall(C, D) {
	var E = "";
	var B = "";
	var A = new Array();
	if (C == OVER_CONST && D != 0) {
		E = "-" + D;
		B = HTML_RED
	} else {
		if (C == UNDER_CONST && D != 0) {
			B = HTML_BLUE;
			E = "+" + D
		} else {
			if (D == 0) {
				B = HTML_BLUE;
				E = D
			}
		}
	}
	A.push(B);
	A.push(E);
	return A
}
function format1D2D(B, A) {
	return B + "-" + A
}
function getStatusColor(B) {
	var A = "";
	switch (B) {
	case ACCEPT_CONST:
		A = HTML_GREEN;
		break;
	case NORMAL_CONST:
		A = HTML_GREEN;
		break;
	case REJECT_CONST:
		A = HTML_RED;
		break;
	case DANGER_CONST:
		A = HTML_BLUE;
		break;
	case REJECTGOAL_CONST:
		A = HTML_RED;
		break;
	case CANCEL_CONST:
		A = HTML_RED;
		break;
	default:
		A = "black"
	}
	return A
}
function getStatusTextS(A) {
	if (A == REJECT_CONST) {
		return "txreject"
	}
	return ""
}
function getBlinkState(B) {
	var A = "";
	switch (B) {
	case REJECT_CONST:
		A = ' class="bgreject"';
		break;
	case DANGER_CONST:
		A = ' class="bgdanger"';
		break
	}
	return A
}
function getBetStatus(B) {
	var A = "";
	switch (B) {
	case ACCEPT_CONST:
		A = ACCEPT_V_CONST;
		break;
	case REJECT_CONST:
		A = REJECT_V_CONST;
		break;
	case REJECTGOAL_CONST:
		A = REJECT_V_CONST;
		break;
	case NORMAL_CONST:
		A = ACCEPT_V_CONST;
		break;
	case CANCEL_CONST:
		A = REJECT_V_CONST;
		break;
	case DANGER_CONST:
		A = WAITING_V_CONST;
		break;
	default:
		A = ""
	}
	return A
}
function getRunScore(A, C, B) {
	if (A) {
		return "[" + C + "-" + B + "]"
	} else {
		return ""
	}
}
function onShowBetList(A) {
	setActiveMenu(A);
	if (A == 1) {
		$("#pMiniBetLeft").show();
		$("#pMiniBetRight").hide();
		$("#hl_top").removeClass("BetBill_c");
		$("#hl_pending").removeClass("BetBill_c").addClass("BetBill_c");
		showNoneInformation(true)
	} else {
		$("#pMiniBetLeft").hide();
		$("#pMiniBetRight").show();
		$("#hl_pending").removeClass("BetBill_c");
		$("#hl_top").removeClass("BetBill_c").addClass("BetBill_c");
		showNoneInformation(false)
	}
}
function CheckShowMiniBetListPosition() {
	var A = "D";
	var B = "R";
	for ( var C = 0; C < betListArray.length; C++) {
		if (betListArray[C]["DangerStatus"] == A
				|| betListArray[C]["DangerStatus"] == B) {
			return 1
		}
	}
	return 2
}
function showNoneInformation(D) {
	var B = false;
	if (betListArray) {
		var A = "A";
		var C = "N";
		if (D) {
			A = "D";
			C = "R"
		}
		for ( var E = 0; E < betListArray.length; E++) {
			if (betListArray[E]["DangerStatus"] == A
					|| betListArray[E]["DangerStatus"] == C) {
				B = true;
				break
			}
		}
	}
	if (B) {
		$("#bet_nlist").hide()
	} else {
		$("#bet_nlist").show()
	}
}
function ResetAndLoadFullBetList() {
	betListLoaded = false;
	betListTimerCount = 0;
	ResetMiniBetListAutoDisplay(true);
	onLoadFullBetList()
}
function ResetMiniBetListAutoDisplay(A) {
	miniBetListAutoDisplay = A
}
function CheckMiniBetListAutoDisplay() {
	return miniBetListAutoDisplay
}
function onLoadFullBetList() {
	UpdateNoResponse(true);
	var B = "../Member/BetsView/Data.asmx/mbl";
	var A = {
		"f" : 1,
		"v" : betListVersionOrigin
	};
	callWebService(B, JSON.stringify(A), onBetListLoadedCommon, function(C) {
		return false
	})
}
function onBetListLoadedCommon(A) {
	if (A && (A["Au"] == 0 || A["Au"] == false)) {
		checkRefirection(true, false)
	}
	if (!(A)) {
		if (betListDebugger) {
			alert("Data Error!")
		}
		return false
	}
	UpdateNoResponse(false);
	betListVersion = A["Sequence"];
	if (A["IsFull"]) {
		onFullBetListLoaded(A["BetDatas"])
	} else {
		onIncBetListLoaded(A["BetDatas"])
	}
}
function onFullBetListLoaded(A) {
	$("#bet_list").empty();
	betListArray = null;
	betListArray = A;
	if (betListArray != null) {
		betListLoaded = true;
		updateBetListHtml()
	}
}
function updateBetListHtml() {
	var E = betListArray;
	var C = parseTemplate($("#BetSnippetTemplate").html(), E);
	$("#bet_list").html(C);
	$('ul[sta="0"],div[sta="0"]').appendTo($("#pMiniBetLeft"));
	$('ul[sta="1"],div[sta="1"]').appendTo($("#pMiniBetRight"));
	var B = getActiveMenu();
	var D = CheckMiniBetListClosed();
	var A = CheckMiniBetListAutoDisplay();
	if (A && D) {
		B = CheckShowMiniBetListPosition();
		ProcessCollapseBetList(true)
	}
	onShowBetList(B)
}
function onLoadIncBetList() {
	UpdateNoResponse(true);
	var B = betListVersion || betListVersionOrigin;
	var A = "../Member/BetsView/Data.asmx/mbl";
	var C = {
		"f" : 0,
		"v" : B
	};
	callWebService(A, JSON.stringify(C), onBetListLoadedCommon, function(D) {
		return false
	})
}
function onIncBetListLoaded(I) {
	if (I && (I["Au"] == 0 || I["Au"] == false)) {
		checkRefirection(true, false)
	}
	if (!(I)) {
		if (betListDebugger) {
			alert("Data Error!")
		}
		return false
	}
	var H = I;
	var A = H["SocTranIdList"];
	var J = false;
	if (A && A.length) {
		J = true;
		var C = A.split(",");
		C.sort(sorttosmall);
		for ( var B = 0, D = C.length; B < D; B++) {
			var G = false;
			for ( var E = 0, F = betListArray.length; E < F; E++) {
				if (betListArray[E]["SocTransId"] == C[B]) {
					G = true;
					betListArray.splice(E, 1);
					E--;
					F--
				}
			}
		}
	}
	A = null;
	var K = H["Betlist"];
	if (K && K.length) {
		J = true;
		for ( var B = 0; B < K.length; B++) {
			for ( var D = 0; D < betListArray.length; D++) {
				if (betListArray[D]["SocTransId"] == K[B]["SocTransId"]
						&& betListArray[D]["SocTransParId"] == K[B]["SocTransParId"]) {
					betListArray[D]["ParDangerStatus"] = K[B]["ParDangerStatus"];
					betListArray[D]["DangerStatus"] = K[B]["DangerStatus"];
					break
				}
			}
		}
	}
	K = null;
	H = null;
	if (J) {
		updateBetListHtml()
	}
}
function setBetListTimer() {
	var A = betListIncLongTime / betListIncShortTime;
	var B = false;
	if (betListArray) {
		for ( var D = 0; D < betListArray.length; D++) {
			if (betListArray[D]["DangerStatus"] == "D") {
				B = true;
				break
			}
		}
	}
	if (B || (!betListLoaded)) {
		A = 1
	}
	if (betListTimerCount >= A) {
		if (betListLoaded) {
			onLoadIncBetList()
		} else {
			onLoadFullBetList()
		}
		betListTimerCount = 0
	}
	betListTimerCount++;
	var C = new Date();
	if (betListTimer) {
		clearTimeout(betListTimer);
		betListTimer = null
	}
	betListTimer = setTimeout(function() {
		setBetListTimer()
	}, betListIncShortTime)
}
function filterParlayTitle(A) {
	return A.replace(/Handicap/i, "HDP").replace(/Over\/Under/i, "O/U")
};
var DEFIND_ODDS_REFRESH = 10;
var m_AutoRefreshOdds = true;
var autoRefreshOddsTimer = null;
var autoRefreshOddsCount = DEFIND_ODDS_REFRESH;
var autoRefreshOddsBetting = false;
function resetAutoRefreshOdds() {
	showAutoRefreshOdds(false);
	clearAutoRefreshOddsTimer()
}
function restartAutoRefreshOdds() {
	if (!m_AutoRefreshOdds) {
		showAutoRefreshOdds(false);
		return false
	}
	var A = $("#tx_bet_type").val();
	switch (A.toUpperCase()) {
	case "HDP":
	case "OVER":
	case "UNDER":
	case "ODD":
	case "EVEN":
	case "ONE":
	case "X":
	case "TWO":
		break;
	default:
		showAutoRefreshOdds(false);
		return false
	}
	showAutoRefreshOdds(true);
	autoRefreshOddsCount = DEFIND_ODDS_REFRESH;
	autoRefreshBetOdds()
}
function showAutoRefreshOdds(A) {
	if (A) {
		$("#pl_autorefresh").show()
	} else {
		$("#pl_autorefresh").hide();
		clearAutoRefreshOddsTimer()
	}
}
function autoRefreshBetOdds() {
	clearAutoRefreshOddsTimer();
	if (!autoRefreshOddsChecked()) {
		return false
	}
	autoRefreshOddsCount--;
	$("#spautorefreshcount").html("(" + autoRefreshOddsCount + ")");
	if (autoRefreshOddsCount <= 0) {
		var A = "../Member/BetsView/Bet.asmx/RefreshOdds";
		var C = getBetCacheData();
		if (C) {
			var B = JSON.stringify({
				"code" : C
			});
			callWebService(A, B, onRefreshBetOdds, onFailedRefreshBetOdds)
		}
		return false
	}
	autoRefreshOddsTimer = setTimeout(function() {
		autoRefreshBetOdds()
	}, 1000)
}
function onRefreshBetOdds(A) {
	if (A && (A["Au"] == 0 || A["Au"] == false)) {
		checkRefirection(true, false)
	}
	if (A && A.d) {
		A = A.d;
		checkRefirection(getAuthenticationCommon(), A.IsAuthenticated);
		switch (A.StatusCode) {
		case 0:
		case 3:
			refreshBetOdds(A);
			break;
		default:
			showBetError(A.Message);
			return false
		}
	}
	restartAutoRefreshOdds()
}
function onFailedRefreshBetOdds(A) {
	showBetError(commonErrorMessage);
	return false
}
function clearAutoRefreshOddsTimer() {
	if (autoRefreshOddsTimer) {
		clearTimeout(autoRefreshOddsTimer);
		autoRefreshOddsTimer = null
	}
}
function autoRefreshOddsChecked() {
	var A = $("#chkAutoRefresh").attr("checked");
	return A == true || A == "checked"
};
var V_FAV_USERKEY = "";
var V_FAV_DOMAIN = GetDomain();
var TXT_EMPTY_FAVORITES = "My Favorite is empty.You will be redirected to the Today's Event Page.";
var Favorites = {};
function GetDomain() {
	var A = location.hostname;
	var B = A.split(".");
	if (B.length >= 3) {
		return B.slice(1).join(".")
	}
	return A
}
function ShowFavorites() {
	var E = "S";
	var D = FavoritesCache(E);
	if (D.length > 0) {
		var C = getDefaultBetView();
		if (C != 1 && C != 2 && C != 20 && C != 21) {
			C = 1
		}
		var B = "Today";
		var A = getBetUrl(String(C), B, "", "S_", "", true);
		getMainPanel().location = A;
		return
	}
	alert(TXT_EMPTY_FAVORITES);
	return
}
function FavoritesFilter(A) {
	return FavoritesCache(A).join(",")
}
function FavoritesCheck(A) {
	var B = FavoritesParse(A);
	return FavoritesCheckSingle(B.key, B.val)
}
function FavoritesCheckSingle(D, A) {
	var B = FavoritesCache(D);
	if (B.length > 0) {
		var C = jQuery.inArray(A, B);
		if (C != -1) {
			return true
		}
	}
	return false
}
function FavoritesAppend(B) {
	var A = FavoritesParse(B);
	var C = FavoritesCache(A.key);
	if (C.length > 0) {
		var D = jQuery.inArray(A.val, C);
		if (D != -1) {
			return false
		}
	}
	C.push(A.val);
	return FavoritesCache(A.key, C)
}
function FavoritesParse(C) {
	var D = C.split(":");
	var A = D[1] || "ALL";
	var B = D[0];
	return {
		"key" : A,
		"val" : B
	}
}
function FavoritesRemove(B) {
	var A = FavoritesParse(B);
	var C = FavoritesCache(A.key);
	if (C.length > 0) {
		var D = jQuery.grep(C, function(E, F) {
			return E != A.val
		});
		FavoritesCache(A.key, D);
		return true
	} else {
		return false
	}
}
function FavoritesCache(C, A) {
	C = "F." + C + V_FAV_USERKEY;
	if (typeof A != "undefined") {
		jQuery.cookie(C, A.join(","), {
			"domain" : V_FAV_DOMAIN
		});
		Favorites[C] = null;
		delete Favorites[C];
		return true
	} else {
		if (Favorites[C]) {
			return Favorites[C]
		}
		var B = jQuery.cookie(C);
		Favorites[C] = B ? B.split(",") : new Array();
		return Favorites[C]
	}
}
function FavoritesClear(A) {
	FavoritesCache(A, new Array())
};
var HDP_CONST = "HDP";
var OU_CONST = "OU";
var OVER_CONST = "Over";
var UNDER_CONST = "Under";
var OE_CONST = "OE";
var ONE_CONST = "1";
var TWO_CONST = "2";
var CS_CONST = "CS";
var TG_CONST = "TG";
var OUT_CONST = "Out";
var D1_CONST = "1D";
var D2_CONST = "2D";
var PAR_CONST = "PAR";
var FG_CONST = "FG";
var LG_CONST = "LG";
var NG_CONST = "NG";
var HH_CONST = "HH";
var HD_CONST = "HD";
var HA_CONST = "HA";
var DH_CONST = "DH";
var DD_CONST = "DD";
var DA_CONST = "DA";
var AH_CONST = "AH";
var AD_CONST = "AD";
var AA_CONST = "AA";
var D3_CONST = "3D";
var D4_CONST = "4D";
var HDP_V_CONST = "HDP";
var OVER_V_CONST = "Over";
var UNDER_V_CONST = "Under";
var ODD_V_CONST = "Odd";
var EVEN_V_CONST = "Even";
var X12_V_CONST = "1X2";
var ONE_V_CONST = "1";
var X_V_CONST = "X";
var TWO_V_CONST = "2";
var D1_V_CONST = "1D";
var D2_V_CONST = "2D";
var BET_V_CONST = "Bet";
var FGLG_V_CONST = "FG/LG";
var FG_V_CONST = "FG";
var LG_V_CONST = "LG";
var X_CONST = "X";
var NG_V_CONST = "NG";
var HTFT_V_CONST = "HT/FT";
var HH_V_CONST = "HH";
var HD_V_CONST = "HD";
var HA_V_CONST = "HA";
var DH_V_CONST = "DH";
var DD_V_CONST = "DD";
var DA_V_CONST = "DA";
var AH_V_CONST = "AH";
var AD_V_CONST = "AD";
var AA_V_CONST = "AA";
var FT_V_CONST = "FT";
var HT_V_CONST = "HT";
var OU_V_CONST = "Over/Under";
var OE_V_CONST = "Odd/Even";
var CS_V_CONST = "Correct Score";
var OUT_V_CONST = "OUTRIGHT";
var TG_V_CONST = "Total Goal";
var ACCEPT_CONST = "A";
var CANCEL_CONST = "C";
var DANGER_CONST = "D";
var NORMAL_CONST = "N";
var REJECT_CONST = "R";
var REJECTGOAL_CONST = "RG";
var WAITING_V_CONST = "Waiting";
var REJECT_V_CONST = "Reject";
var ACCEPT_V_CONST = "Accept";
var HTML_BLACK = "black";
var HTML_RED = "red";
var HTML_BLUE = "blue";
var HTML_GREEN = "green";
var HTML_DARK_GOLDEN_ROD = "#E8A317";
var m_market = "Today";
var m_betType = "1";
var m_mode = "1";
var m_betMode = "S";
var m_isFirstToday = true;
var m_isFirstEarly = true;
var m_isFirstRun = true;
var m_isSaved = false;
var m_isIn = false;
var m_aut = false;
var m_isRunOnly = false;
var m_allCount = 0;
var m_allCount2 = 0;
var buttonPressTimer;
var buttonPressSecond = 1;
var isIE = $.browser.msie;
var cookiesDomain = GetDomain();
var newGameType = "";
function getGameType() {
	return newGameType || "S"
}
function setGameType(A) {
	newGameType = A
}
function reloadPage() {
	location.reload()
}
function getSportsMapJson() {
	return $(window).data("SportsMapJson")
}
function setSportsMapJson(A) {
	return $(window).data("SportsMapJson", A)
}
function hideUserInfo() {
	userState = "-";
	$("#tbUserInfo tr").hide();
	$("#tbUserInfo tr:eq(2)").show();
	$("#tdBetCredit").addClass("b")
}
function showUserInfo() {
	userState = "+";
	$("#tbUserInfo tr").show();
	$("#tdBetCredit").removeClass("b")
}
function onUserInfoRefresh(A) {
	if (!A && !checkManual("onUserInfoRefresh", 3000)) {
		return
	}
	sendUserInfoRequest()
}
function sendUserInfoRequest() {
	callWebService("../Member/BetsView/UserInfoPanelHost.aspx?Ajax=1", "{}",
			function(A) {
				var B = A["Balance"];
				$("#tdBalance").removeClass("red");
				if (parseFloat(B.replace(/[, ]/g, "")) < 0) {
					$("#tdBalance").addClass("red")
				}
				$("#lb_currency").html(A["Currency"]);
				$("#lb_balance").html(B);
				$("#lb_outstanding").html(A["Outstanding"]);
				$("#lb_bet_credit").html(A["Bet_credit"]);
				$("#lb_given_credit").html(A["Given_credit"]);
				$("#lblLastLogin").html(A["LastLogin"]);
				$("#lblLt").html(A["Lt"]);
				$("#lblpld").html(A["pld"])
			}, onException)
}
function collapseUserInfo(D, B) {
	var A = B == null ? false : B;
	if (A && userState == "+") {
		onUserInfoRefresh();
		return
	} else {
		onUserInfoRefresh()
	}
	var C = document.getElementById("img_user");
	if (userState == "-") {
		userState = "+";
		showUserInfo();
		if (C != null) {
			C.src = "../App_Themes/Resources/img/Arrow_hide.gif"
		}
	} else {
		userState = "-";
		hideUserInfo();
		if (infoTimer) {
			clearTimeout(infoTimer)
		}
		if (C != null) {
			C.src = "../App_Themes/Resources/img/Arrow_show.gif"
		}
	}
}
function onException(C, A, D) {
	if (document.getElementById("hf_redirectUrl") != null) {
		var B = document.getElementById("hf_redirectUrl").value;
		HandleError(C, B);
		hideLoading()
	}
	return
}
function getDBMarket() {
	if (m_market == "Today") {
		return 2
	} else {
		if (m_market == "Early") {
			return 1
		} else {
			if (m_market == "Running") {
				return 3
			}
		}
	}
}
function getLanguage() {
	return m_language
}
function getIsAuthenticated() {
	return m_aut
}
function getLeagueList(C, A) {
	var B;
	if (A === B) {
		return $(window).data(C)
	} else {
		$(window).data(C, A);
		return A
	}
}
function ClearLeagueCookies() {
	var A = decodeURIComponent(document.cookie);
	var C = /(^| )([^;]*(Today|Early|Running)_[^=]+)=([^;]*)(;|$)/g;
	var B = A.replace(C, "$1")
}
function getMarket() {
	return m_market
}
function getBetType() {
	var A = $(window).data("BetType");
	if (A != null) {
		return A
	} else {
		getDefaultBetView()
	}
}
function setBetType(A) {
	$(window).data("BetType", A)
}
function getVIPDelay() {
	var A = $(window).data("pVIP");
	if (A == null) {
		return 0
	} else {
		return A
	}
}
function getAllCounter() {
	return m_allCount
}
function getTickedCounter() {
	return m_allCount2
}
function getMainPanel() {
	return parent.frames[2]
}
function scrollPanel() {
	window.location.hash = "miniAnchor"
}
function scrollToBetWindow() {
	window.location.hash = "betWindowAnchor"
}
function scrollToUserInfo() {
	window.location.hash = "userInfoAnchor"
}
function redirectOnLogout() {
	parent.location.href = "/Default.aspx"
}
function showLoading() {
	$("#loading").css("visibility", "visible")
}
function hideLoading() {
	$("#loading").css("visibility", "hidden")
}
function getDefaultBetView() {
	var A = $(window).data("pBetView");
	if (A == null) {
		return 1
	} else {
		return A
	}
}
function getIsRunOnly() {
	return m_market == "Running"
}
function getDefaultCountry() {
	var A = $(window).data("pCountry");
	if (A == null) {
		return "MY"
	} else {
		return A
	}
}
var SiteRootUrl = "../";
function getBetUrl(A, H, G, D, B, E) {
	var F = typeof (EnableBetViewParlay) != "undefined" && EnableBetViewParlay;
	if (D.indexOf("12D") != -1) {
		return SiteRootUrl + "Member/BetsView/BetLight/GV1D2D.aspx?v=" + A
				+ "&m1=" + H + "&m2=" + G + "&s=" + D
	}
	if (D.indexOf("34D") != -1) {
		return SiteRootUrl + "Member/BetOdds/HdpDouble.aspx?v=" + A + "&m1="
				+ H + "&sports=" + D
	}
	E = E ? "&fav=1" : "";
	var C = "";
	switch (A) {
	case "0":
	case "1":
		C = SiteRootUrl + "Member/BetOdds/HdpDouble.aspx?v=" + A + "&m1=" + H
				+ E + "&sports=" + D;
		break;
	case "2":
		C = SiteRootUrl + "Member/BetOdds/HdpDouble.aspx?v=" + A + "&m1=" + H
				+ E + "&sports=" + D;
		break;
	case "20":
	case "21":
		C = SiteRootUrl + "Member/BetOdds/HdpDouble.aspx?v=" + A + "&m1=" + H
				+ E + "&sports=" + D;
		break;
	case "3":
		C = SiteRootUrl + "Member/BetsView/BetLight/GVOETG.aspx?m1=" + H;
		break;
	case "4":
		break;
	case "5":
		C = SiteRootUrl + "Member/BetsView/BetLight/GVCS.aspx?v=" + A + "&m1="
				+ H + "&m2=" + G + "&s=" + D;
		break;
	case "6":
		C = SiteRootUrl + "Member/BetsView/BetLight/GVTGMain.aspx?v=" + A
				+ "&m1=" + H + "&m2=" + G + "&s=" + D;
		break;
	case "7":
		C = SiteRootUrl + "Member/BetOdds/Parlay.aspx?v=" + A
				+ "&m1=Today&sports=" + D;
		break;
	case "8":
		C = SiteRootUrl + "Member/BetsView/BetLight/GVOUT.aspx?v=" + A + "&m1="
				+ H + "&m2=" + G + "&s=" + D;
		break;
	case "9":
		C = SiteRootUrl + "Member/BetsView/GV1D2D.aspx?v=" + A + "&m1=" + H
				+ "&m2=" + G + "&s=" + D;
		break;
	case "10":
		C = SiteRootUrl + "Member/BetsView/BetLight/GVFTHT.aspx?m1=" + H;
		break;
	case "11":
		C = SiteRootUrl + "Member/BetsView/BetLight/GVFGLG.aspx?m1=" + H;
		break;
	case "12":
		C = SiteRootUrl + "Member/BetOdds/X12.aspx?m1=" + H;
		break;
	case "13":
		C = get4DUrl(A, H, G, D, B);
		break;
	default:
		C = SiteRootUrl + "Member/BetOdds/HdpDouble.aspx?v=" + A + "&m1=" + H
				+ E + "&sports=" + D
	}
	return C
}
function get4DUrl(A, F, E, C, B) {
	var D = getFormattedSports();
	if (D.indexOf("34D") != -1) {
		return SiteRootUrl + "Member/BetOdds/HdpDouble.aspx?v=" + A + "&m1="
				+ F + "&m2=" + E + "&s=" + C + "&ro=" + B + "&sports=" + C
	} else {
		if (D.indexOf("12D") != -1) {
			return SiteRootUrl + "Member/BetsView/BetLight/GV1D2D.aspx?v=" + A
					+ "&m1=" + F + "&m2=" + E + "&s=" + C
		} else {
			return SiteRootUrl + "Member/BetsView/BetLight/Blank.htm"
		}
	}
}
function getFormattedSports() {
	var A = newGameType || GAMETYPEALL;
	return ((A == GAMETYPEALL || A.charAt(A.length - 1) == "_") ? A : A + "_")
}
function buttonPressThrottle() {
	if (buttonPressTimer) {
		clearTimeout(buttonPressTimer);
		buttonPressTimer = null
	}
	if (buttonPressSecond < 2) {
		buttonPressTimer = setTimeout("buttonPressThrottle()", 1000);
		buttonPressSecond++
	} else {
		buttonPressSecond = 1
	}
}
$(document).ready(
		function() {
			$(window).unload(onUnload);
			$(document).bind("click", onPanelClick);
			$("#chkAutoRefresh").bind("click", restartAutoRefreshOdds);
			initMiniBetListPanel();
			initSportPanel();
			if (navigator.userAgent.indexOf("Chrome") > -1
					&& navigator.language.indexOf("zh") > -1) {
				document.body.style["-webkit-text-size-adjust"] = "none"
			}
		});
function onUnload() {
	unloadForMiniBetList()
}
function onPanelClick(C) {
	if (C.target.nodeName == "IMG") {
		if (C.target.id.indexOf("imgBtn_Print") != -1) {
			var B = C.target.id.split("_");
			var A = $("#" + B[2] + "_plDetailList").outerHTML();
			printBetInfo(A)
		}
	} else {
		if (C.target.nodeName == "SPAN") {
			if (C.target.id == "lb_title") {
				$("#tx_stake").trigger("focus").trigger("select")
			} else {
				if (C.target.id == "lbSportsInfo") {
					collapseBetList()
				}
			}
			if (C.target.id == "hl_pending") {
				onChangeMenu(1)
			} else {
				if (C.target.id == "hl_top") {
					onChangeMenu(2)
				}
			}
			return false
		}
	}
}
function parseBetView(A) {
	if (A == 1 || A == 2) {
		return 1
	} else {
		if (A == 3) {
			return 3
		} else {
			if (A == 5) {
				return 2
			} else {
				if (A == 7) {
					return 5
				} else {
					if (A == 8) {
						return 4
					} else {
						if (A == 9 || A == 13) {
							return 6
						} else {
							if (A == 10) {
								return 8
							} else {
								if (A == 11) {
									return 7
								} else {
									if (A == 12) {
										return 9
									} else {
										return 1
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
function printBetInfo(B) {
	var A = [];
	A.push("<html><head><title>Print Preview</title>");
	A
			.push('<link type="text/css" rel="stylesheet" href="/App_Themes/Resources/css/printbet.css" />');
	A
			.push('<link type="text/css" rel="stylesheet" href="/App_Themes/Resources/css/print.css" media="print" />');
	A
			.push('<link href="/MemberUI/App_Themes/Resources/PrintStyle.css" type="text/css" rel="stylesheet" media="print" />');
	A.push('<base target="_self"></head>');
	A.push('<body><div class="BetBill2">');
	A.push("<h3>");
	A
			.push('<input id="print" type="button" value="Print" onclick="window.print();">');
	A
			.push('<input id="close" type="button" value="Close" onclick="window.close();" />');
	A.push("</h3>");
	A.push(B);
	A.push("</div></body></html>");
	var C = window.open();
	C.document.writeln(A.join(""))
}
function UpdateNoResponse(B) {
	try {
		parent.frames[3].UpdateNoResponse(B)
	} catch (A) {
	}
};
var LiveKey = "live";
var LiveSumCacheKey = "LiveCenterSum";
var LiveCenterContainer = "LiveCenterConatiner";
var LiveCenterCountContainer = "LiveCenterCount";
var TVWindow = null;
function updateLiveCenterSum(A) {
	var B = 0;
	if (A && A[LiveKey]) {
		var D = getSportsMapJson();
		B = A[LiveKey].length;
		$(window).data(LiveSumCacheKey, A[LiveKey]);
		try {
			if (typeof (getMainPanel().LoadLiveCenterMatchList) == "function") {
				getMainPanel().LoadLiveCenterMatchList()
			}
		} catch (C) {
		}
	}
	UpdateLiveCenterCountHtml(B)
}
function UpdateLiveCenterCountHtml(B) {
	var A = $("#" + LiveCenterContainer);
	if (B > 0) {
		$(A).show()
	} else {
		B = 0;
		$(A).hide()
	}
	$("#" + LiveCenterCountContainer).html(B)
}
function onLiveCenterClick() {
	if (!checkManual("onLiveCenterClick")) {
		return false
	}
	try {
		closeTVWindow()
	} catch (A) {
	}
	getMainPanel().location.href = "../Member/BetOdds/LiveCenter.aspx"
}
function getLiveCenterSumCache() {
	return $(window).data(LiveSumCacheKey)
}
function getLiveCenterCount() {
	var A = getLiveCenterSumCache();
	return A == null ? 0 : A.length
}
function setTVWindow(A) {
	TVWindow = A
}
function closeTVWindow() {
	try {
		if (TVWindow) {
			TVWindow.close()
		}
	} catch (A) {
	}
}
function OnStreamNameChange(A, B) {
	try {
		if (TVWindow) {
			TVWindow.OnStreamNameChange(A, B)
		}
	} catch (C) {
	}
};
