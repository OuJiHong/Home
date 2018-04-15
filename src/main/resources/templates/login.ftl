<!DOCTYPE html>
<html>
	<head>
		<title>管理员登录页面</title>
		
		[#include "/include/head.ftl" /]
				
		<style>
		
		    body{
		    	background:url('${base}/lib/images/bg.jpg') no-repeat center;
		    	-webkit-background-size:100% auto;
		    	background-size:100% auto;
		    }
		    
		    
			#captchaImg{
				cursor:pointer;
				height:30px;
			}
			
			#captchaImg:hover{
				box-shadow:0px 0px 3px rgba(0,0,0,0.2);
			}
			
			.login-size{
				max-width:480px;
				margin:auto;
				margin-top:40px;
				-webkit-box-shadow:1px 1px 6px rgba(0,0,0,0.2);
				box-shadow:1px 1px 6px rgba(0,0,0,0.2);
			}
		</style>
	</head>
	
	<body>
		<div class="container">
			[#if errorMsg?? ]
				<div class="alert alert-danger alert-dismissible">
					${errorMsg}<span data-dismiss="alert" class="close">&times;</span>
				</div>
			[/#if]
			<!--  panel  -->
			
			<div class="panel panel-default  login-size">
				<div class="panel-heading">
					管理员登录
				</div>
				<div class="panel-body">
					<form class="form-horizontal" method="post"  id="loginForm" >
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
						<div class="form-group">
							<label class="col-sm-4">用户名：</label>
							<div class="col-sm-8">
								<input type="text" name="username" class="form-control" placeholder="请输入用户名">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4">密码：</label>
							<div class="col-sm-8">
								<input type="password" name="password" class="form-control" placeholder="请输入密码">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-4">验证码：</label>
							<div class="col-sm-8">
								<div class="input-group">
									<input type="text" name="captchaCode" class="form-control" placeholder="请输入验证码">
									<span class="input-group-addon" style="padding:0;">
										<span style="display:inline-block;width:120px;">
											<img src="${defaultImage}" class="img-responsive" data-src="${base}/admin/getCaptcha" id="captchaImg"/>
										</span>
									</span>								
								</div>
							</div>
						</div> 
						<div class="form-group">
							<div class="col-sm-offset-4 col-sm-8">
								<div class="row">
									<div class="col-sm-6">
										<button type="submit" class="btn btn-primary btn-block">登录</button>
									</div>
									<div class="col-sm-6">
										<button type="reset" class="btn btn-default btn-block">重置</button>
									</div>
								</div>
							</div>
						</div>
					</form>
				</div>
				
			</div>
			
			<!--  panel end -->
			
		</div>
	</body>
	
	
	<script>
		$(function(){
			//init
			$("#captchaImg").click(function(){
				var $this = $(this);
				var url = $this.attr("data-src");
				var newURL = url + "?t=" + new Date().getTime();
				$this.attr("src", newURL);
			}).click();
			
			var $loginForm = $("#loginForm");
			$loginForm.find(":submit").prop("disabled", false)
			$loginForm.on("submit", function(){
				$(this).find(":submit").prop("disabled", true);
			});
			
		});
	</script>
</html>