<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

[#include "include/head.ftl"/]

<title>错误页面</title>

<style>


.container{
	text-align:center;
	background-color:#eee;
	padding-left:15px;
	padding-right:15px;
	line-height:1.8;
	max-width:1170px;
	margin:auto;
}

.container .panel{
	padding:40px 0px;
	text-align:center;
}


.container .error{
	font-size:60px;
	color:#883322;
	text-shadow:1px 1px 6px rgba(0,0,0,0.2);
}

.container .info{
	font-size:14px;
	color:#555;
	text-align:left;
}


.btn{
	color:#4787ce;
	font-size:16px;
	line-height:1.5;
}


</style>
</head>
<body>
	<div class="container">
		<div class="panel">
			<div class="error">500</div>
		 	<div class="info">
		 		<div>请求的操作出错啦！</div>
		 		<div>原因:
		 			<span style="color:red;">
		 				[#if message??]
		 					${message.msg}
		 				[/#if]
		 				[#if exception??]
		 					${exception.message}
		 				[/#if]
		 			</span>
		 		</div>
		 	</div>
	 	</div>
	</div>
	
	
</body>
</html>