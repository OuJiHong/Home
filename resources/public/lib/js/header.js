/**
 *写入公共代码,不能随意变更文件名
 */
!function(){
	
	var fileName = "/lib/js/header.js";
	var basePath = "";//上下文环境
	var lastScript = document.scripts[document.scripts.length - 1];
	var lastFind = -1;
	if(lastScript != null && (lastFind = lastScript.src.indexOf(fileName)) != -1){
		basePath = lastScript.src.substring(0,lastFind);
	}
	
	//设置到全局环境中
	window.basePath = basePath;
	
	function write(url){
		if(url == null){
			return;
		}
		
		var jsPattern = /\.js\s*$/;
		var cssPattern =/\.css\s*$/;
		
		if(jsPattern.test(url)){
			document.writeln("<script type='text/javascript' src='"+url+"'></script>");
		}else if(cssPattern.test(url)){
			document.writeln("<link type='text/css' rel='stylesheet' href='"+url+"'/>");
		}else{
			document.writeln("<link  href='"+url+"'/>");
		}
	}
	
	//以bootstrap为基础，扩展样式
	write(basePath + "/lib/bootstrap-3.3.7-dist/css/bootstrap.css");
	write(basePath + "/lib/css/common.css");
	write(basePath + "/lib/js/jquery-1.9.1.min.js");
	write(basePath + "/lib/bootstrap-3.3.7-dist/js/bootstrap.js");
	write(basePath + "/lib/js/template-web.js");
	write(basePath + "/lib/js/util.js");
	
	//扩展的fancybox插件,很好的图片播放插件，还有一个是viewer.js
	write(basePath + "/lib/fancybox-2.1.7/source/jquery.fancybox.css");
	write(basePath + "/lib/fancybox-2.1.7/source/jquery.fancybox.js");
	
}();