<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

[#include "include/head.ftl"/]

<title>请求内容未找到</title>

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
	 		<div class="error">404</div>
	 		<div class="info">抱歉，您访问的页面不存在，请重新加载</div>
	 		<div class="footer">
	 			<a class="btn" href="javascript:location.reload();">重新加载</a>
	 			&nbsp;&nbsp;&nbsp;&nbsp;
	 			<a class="btn" href="${base}/">返回首页</a>
	 		</div>
		</div>
	</div>
</body>
</html>