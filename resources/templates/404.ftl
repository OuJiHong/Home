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
	padding:20px;
	line-height:1.8;
}

.container .error{
	font-size:60px;
	color:#883322;
	text-align:center;
}

.container .info{
	font-size:14px;
	color:#555;
	text-align:center;
}


.bg-img{
	position:absolute;
	top:0px;
	bottom:0px;
	right:0px;
	left:0px;
	z-index:-1;
}

</style>
</head>
<body>
	<div class="container">
			<div class="bg-img">
				<img src="${base}/resource/image/xue.jpg" class="responsive" />
			</div>
	 		<div class="error">404</div>
	 		<div class="info">请求的内容未找到！！！</div>
	</div>
</body>
</html>