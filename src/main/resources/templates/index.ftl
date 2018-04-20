<!DOCTYPE html>
<html>
	<head>
		<title>后台管理主页</title>
		[#include "/include/head.ftl" /]
		<!--  限定跳转目标	 -->
		<base target="contentFrame" />
		
		<style>
		
			.content-wrapper{
				position:relative;
			}
			
			.indexFrame-wrapper{
				position:absolute;
				top:0;
				bottom:0;
				left:0;
				right:0;
			}
			
			#contentFrame{
				position:absolute;
				width:100%;
				height:100%;
				border:none;
				overflow:auto;
				
			}
		</style>
	</head>
	<body class="skin-blue sidebar-min">
		<div class="wrapper">
			
			
			<!--  start  -->
			<header class="main-header">
			
			    <!-- Logo -->
			    <a href="${base}/" class="logo">
			      <!-- mini logo for sidebar mini 50x50 pixels -->
			      <span class="logo-mini"><b>M</b></span>
			      <!-- logo for regular state and mobile devices -->
			      <span class="logo-lg"><b>HOME</b></span>
			    </a>
			
			    <!-- 头部导航栏 -->
			    <nav class="navbar navbar-static-top" role="navigation">
			      <!-- Sidebar toggle button-->
			      <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
			        <span class="sr-only">切换导航按钮</span>
			      </a>
			      <!-- 右侧菜单  -->
			      <div class="navbar-custom-menu">
			        <ul class="nav navbar-nav">
			          <!-- User Account Menu -->
			          <li class="dropdown user user-menu">
			            <!-- Menu Toggle Button -->
			            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
			              <!-- 用户头像  -->
			              <img src="${defaultImage}" class="user-image" alt="用户图片">
			              <!-- 超小设备隐藏 -->
			              <span class="hidden-xs">${user.name}</span>
			            </a>
			            <ul class="dropdown-menu">
			              <!-- 用户图片菜单  -->
			              <li class="user-header">
			                <img src="${defaultImage}" class="img-circle" alt="用户图片">
			                <p>
			                  ${user.name}
			                  <small>${user.detail}</small>
			                </p>
			              </li>
			              <!-- 菜单底部，主体已被忽略  -->
			              <li class="user-footer">
			                <div class="pull-left">
			                  <a href="javascript:userProfile()" class="btn btn-default btn-flat">用户属性</a>
			                </div>
			                <div class="pull-right">
			                  <a href="javascript:logout()" class="btn btn-default btn-flat">退出平台</a>
			                </div>
			              </li>
			              <!--  用户图片菜单结束  -->
			            </ul>
			          </li>
			          <!-- Control Sidebar Toggle Button -->
			          <li>
			            <a href="#" data-toggle="control-sidebar"><i class="fa fa-gears"></i></a>
			          </li>
			        </ul>
			      </div>
			    </nav>
			  </header>
			  
			
			
			<aside class="main-sidebar">

				    <!-- sidebar: style can be found in sidebar.less -->
				    <section class="sidebar">

				      <!-- search form (Optional) -->
				      <form action="#" method="get" class="sidebar-form">
				        <div class="input-group">
				          <input type="text" name="q" class="form-control" placeholder="搜索...">
				          <span class="input-group-btn">
				              <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
				              </button>
				            </span>
				        </div>
				      </form>
				      <!-- /.search form -->
				
				      <!-- Sidebar Menu -->
				      <ul class="sidebar-menu tree" data-widget="tree">
				        <li class="header">管理菜单</li>
				        <!-- 可添加图标 -->
				        <li class="active">
				        	<a href="#"><i class="fa fa-user"></i> <span>用户管理</span></a>
				        </li>
				        <li>
				        	<a href="#"><i class="fa fa-link"></i> <span>角色管理</span></a>
				        </li>
				        <li class="treeview">
					          <a href="#">
					          	<i class="fa fa-link"></i> <span>树形菜单</span>
					            <span class="pull-right-container">
					                <i class="fa fa-angle-left pull-right"></i>
					             </span>
					          </a>
					          <ul class="treeview-menu">
					            <li><a href="#">菜单1</a></li>
					            <li><a href="#">菜单2</a></li>
					          </ul>
				        </li>
				      </ul>
				    </section>
				    <!-- /.sidebar -->
				  </aside>
				  
				  
				  <div class="content-wrapper">
				  		<div class="indexFrame-wrapper">
							<iframe src="welcome" id="contentFrame" name="contentFrame" ></iframe>    
				  		</div>
				  </div>
					  
					  
			<!-- end  -->
			
		</div>
		
		
		<!--  other  -->
		
		<div class="modal fade" id="exitDialog">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">退出提示</div>
					<div class="modal-body">是否退出平台？</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default"  data-value="0">取消</button>
						<button type="button" class="btn btn-primary"  data-value="1">退出</button>
					</div>
				</div>
			</div>
		</div>
		
		<form id="exitForm" action="${base}/admin/logout" method="post" target="_top" class="hidden">
			<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		</form>
		
		<script>
		
			function userProfile(){
				
				
			}
			
			
			function logout(){
				$("#exitDialog").modal("show");
			}
			
			
			$(function(){
				//init
				var $modal = $("#exitDialog");
				$modal.on("click", ".btn", function(){
					var $this = $(this);
					var value = $this.data("value");
					if(value == "1"){
						$modal.modal("hide");
						$("#exitForm").submit();
					}else if(value = "0"){
						$modal.modal("hide");
					}
				});
			});
			
		</script>
	</body>
</html>




