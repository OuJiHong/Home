<!DOCTYPE html>
<html>
	<head>
		<title>后台管理主页</title>
		[#include "/include/head.ftl" /]
	</head>
	<body>
		<!--  布局   -->
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
						<iframe class="embed-responsive-item"  src="welcome" name="contentFrame"></iframe>
					</div>
				</div>
				
			</div>
		</div>
	</body>
</html>