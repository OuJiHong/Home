<!DOCTYPE html>
<html>
	<head>
		<title>后台管理主页</title>
		[#include "/include/head.ftl" /]
	</head>
	<body>
		<!--  布局   -->
		<div class="navbar navbar-default">
			<div class="container-fluid">
				<div class="navbar-header">
					<span class="navbar-brand">后台管理</span>
				</div>
				<ul class="nav navbar-nav">
					<li>
						<a href="#">用户管理</a>
					</li>
					<li>
						<a href="#">模板管理</a>
					</li>
				</ul>				
			</div>
			
		</div>
		<div class="container-fluid">
			<div class="row">
				<!--  菜单 	 -->
				<div class="col-sm-2">
					<ul class="list-group">
						<li class="list-group-item">列表1 </li>
						<li class="list-group-item">列表2 </li>
						<li class="list-group-item">列表3 </li>
					</ul>
				</div>
				<!--  内容  -->
				<div class="col-sm-10">
					<div class="embed-responsive embed-responsive-4by3">
						<iframe class="embed-responsive-item"  src="" name="contentFrame"></iframe>
					</div>
				</div>
				
			</div>
		</div>
	</body>
</html>