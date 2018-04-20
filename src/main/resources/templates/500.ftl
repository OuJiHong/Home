<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

[#include "include/head.ftl"/]

<title>错误页面</title>

<style>

	.pad-t15{
		padding-top:15px;
	}


</style>
</head>
<body>
	<div class="pad-t15"></div>
	<div class="container">
		<div class="panel panel-default">
			<div class="panel-heading">
				<span class="panel-title">内部服务器错误</span>
			</div>
		 	<div class="panel-body">
		 		<h1>500</h1>
		 		<div>服务器压力山大，无法处理请求，请联系管理员</div>
		 		<div>原因:
		 			<span style="color:red;">
		 				[#if message??]
		 					${message.msg}
		 				[/#if]
		 				[#if exception??]
		 					${exception.localizedMessage}
		 				[/#if]
		 			</span>
		 		</div>
		 	</div>
		 	<div class="panel-footer">
	 			<a class="btn btn-primary" href="javascript:location.reload();">重新加载</a>
	 			&nbsp;&nbsp;&nbsp;&nbsp;
	 			<a class="btn btn-default" href="${base}/">返回首页</a>
	 		</div>
	 	</div>
	</div>
	
	
</body>
</html>