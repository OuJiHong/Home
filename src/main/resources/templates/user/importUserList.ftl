<!DOCTYPE html>
<html>
	<head>
		<title>子页面</title>
		[#include "/include/head.ftl" /]
	</head>
	<body>
		<div class="wrapper">
			<div class="panel panel-default">
				<div class="panel-heading">导出结果</div>
				<div class="panel-body">
					<ul>
						[#list list as uu]
						<li>${uu}</li>
						[/#list]
					</ul>
				</div>
			</div>
		</div>
	</body>
</html>




