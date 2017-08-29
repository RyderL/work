window.document.oncontextmenu = function(){ return false;} ;

__CreateJSPath = function (js) {
    var scripts = document.getElementsByTagName("script");
    var path = "";
    for (var i = 0, l = scripts.length; i < l; i++) {
        var src = scripts[i].src;
        if (src.indexOf(js) != -1) {
            var ss = src.split(js);
            path = ss[0];
            break;
        }
    }
    var href = location.href;
    href = href.split("#")[0];
    href = href.split("?")[0];
    var ss = href.split("/");
    ss.length = ss.length - 1;
    href = ss.join("/");
    if (path.indexOf("https:") == -1 && path.indexOf("http:") == -1 && path.indexOf("file:") == -1 && path.indexOf("\/") != 0) {
        path = href + "/" + path;
    }
    return path;
}
__GetWebBasePath = function(){
    var name = location.pathname.split("/")[1];
    var base = location.protocol+"//"+location.host+"/"+name+"/";
    return base;
}
//var bootPATH = __CreateJSPath("boot.js");
var webBasePATH = __GetWebBasePath();

//debugger
mini_debugger = true;   

//easyui
document.write('<link rel="stylesheet" type="text/css" href="' + webBasePATH + 'common/easyui/themes/icon.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="' + webBasePATH + 'common/css/default/spinner.css"/>');
document.write('<link rel="stylesheet" type="text/css" href="' + webBasePATH + 'common/easyui/themes/default/easyui.css"/>');
document.write('<script type="text/javascript" src="' + webBasePATH + 'common/js/json2.js" ></sc' + 'ript>');
document.write('<script type="text/javascript" src="' + webBasePATH + 'common/js/jquery.min.js" ></sc' + 'ript>');
document.write('<script type="text/javascript" src="' + webBasePATH + 'common/ext/jquery.session.js" ></sc' + 'ript>');
document.write('<script type="text/javascript" src="' + webBasePATH + 'common/ext/jquery.call.js" ></sc' + 'ript>');
document.write('<script type="text/javascript" src="' + webBasePATH + 'common/easyui/jquery.easyui.min.js" ></sc' + 'ript>');
document.write('<script type="text/javascript" src="' + webBasePATH + 'common/easyui/locale/easyui-lang-zh_CN.js" ></sc' + 'ript>');
document.write('<script type="text/javascript" src="' + webBasePATH + 'common/ext/jquery.util.js" ></sc' + 'ript>');

//skin
// var skin = getCookie("easyuiSkin");
// if (skin) {
//     document.write('<link rel="stylesheet" type="text/css" href="' + webBasePATH + 'common/easyui/themes/' + skin + '/easyui.css"/>');
// }else{
// 	document.write('<link rel="stylesheet" type="text/css" href="' + webBasePATH + 'common/easyui/themes/metro/easyui.css"/>');
// }
document.write('<link rel="stylesheet" type="text/css" href="' + webBasePATH + 'common/css/default/style.css"/>');//ztesoft-default

////////////////////////////////////////////////////////////////////////////////////////
function getCookie(sName) {
    var aCookie = document.cookie.split("; ");
    var lastMatch = null;
    for (var i = 0; i < aCookie.length; i++) {
        var aCrumb = aCookie[i].split("=");
        if (sName == aCrumb[0]) {
            lastMatch = aCrumb;
        }
    }
    if (lastMatch) {
        var v = lastMatch[1];
        if (v === undefined) return v;
        return unescape(v);
    }
    return null;
}

/**
 *  Loading Animation
 */
var _html = '<div class="loading" id="loading"><div class="loading-spinner">'+
			'  <div class="bounce1"></div>'+
			'  <div class="bounce2"></div>'+
			'  <div class="bounce3"></div>'+
			'</div></div>';
window.onload = function(){  
	var _mask = document.getElementById('loading');  
    _mask.parentNode.removeChild(_mask);  
}  
document.write(_html);
 