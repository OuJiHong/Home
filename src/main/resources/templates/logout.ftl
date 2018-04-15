<!DOCTYPE html>
<html>
	<head>
		<title>注销页面</title>
		[#include "/include/head.ftl" /]
		
		<style>

		</style>
	</head>
	<body>
		<div class="container">
			<div class="panel panel-default">
				<div class="panel-heading">
					注销页面
				</div>
				<div class="panel-body text-center">
					[#if logout?? && logout]
						<h1 class="text-primary">注销成功</h1>
					[#else]
						<h1 class="text-danger">注销失败</h1>
					[/#if]
				</div>
				<div class="panel-footer">
						<a href="${base}/" class="btn btn-primary">返回首页</a>
				</div>
			</div>
		</div>
		
	</body>
</html>