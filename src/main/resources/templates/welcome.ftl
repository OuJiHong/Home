<!DOCTYPE html>
<html>
	<head>
		<title>子页面</title>
		[#include "/include/head.ftl" /]
	</head>
	<body>
		<div class="container">
			<h1 class="text-center">欢迎使用</h1>
			<div>
				<a href="${base}/admin/user/exportUserList" class="btn btn-default" >导出user数据</a>
				<a href="${base}/admin/user/exportUserList?zip=true" class="btn btn-default" >导出user压缩包</a>
			</div>
			
			<form action="${base}/admin/user/importUserList" method="post" enctype="multipart/form-data" class="form form-horizontal" >
				<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
				<div class="form-group">
					<div class="col-4">
						<label>上传文件:</label>
						<input type="file" name="file" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<button type="submit" class="btn btn-primary">上传</button>
				</div>
			</form>
		</div>
	</body>
</html>




