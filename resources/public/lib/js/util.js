/**
 * util通用工具
 * 
 */
(function($){
	
	if(window.util != null){
		throw new Error("util严重冲突，存在同名对象！");
	}
	
	var util = {};
	//缓存数据
	util.cache = {idSeq:0, $loadingId:[],moduleData:{},imgColors:{}};
	
	/**
	 * 显示遮罩
	 */
	util.showMask = function(content,delay){
		if($.isNumeric(delay)){
			delay = parseInt(delay);
		}else{
			delay = 0;
		}
		util.cache.idSeq++;
		var id = "platform_mask_" + util.cache.idSeq;
		var mask  = $("<div class='mask'><div class='mask-overlay'></div><div class='mask-content'></div></div>");
		mask.attr("id",id);
		var $content = mask.find(".mask-content");
		$content.html(content);
		mask.appendTo("body");
		//update pos
		function resetPosition(){
			var curContent = mask.find(".mask-content");
			var pContainer = curContent.offsetParent();
			var pSize = {width:pContainer.innerWidth(),height:pContainer.innerHeight()};
			var availableLeft = (pSize.width -curContent.innerWidth());
			var availableTop  = (pSize.height - curContent.innerHeight());
			var curLeft = availableLeft / 2;
			var curTop = availableTop / 2;			
			curContent.css({left:curLeft,top:curTop});
			if(delay > 0){
				setTimeout(function(){
					mask.fadeOut(function(){
						mask.remove();
					})
				},delay);
			}
		};
		
		function showFn(isImmediate){
			mask.find(".mask-content").show().addClass("active");
			if(isImmediate){
				mask.show();
			}else{
				mask.fadeIn(100);
			}
		};
		
		function hideFn(isImmediate){
			mask.find(".mask-content").removeClass("active").hide();
			if(isImmediate){
				mask.hide();
			}else{
				mask.fadeOut(100);
			}
		};
		
		showFn();
		resetPosition();//init once
		return {
			id:id,
			resetPosition:resetPosition,
			show:showFn,
			hide:hideFn,
			destroy:function(){
				$("#"+this.id).remove();
			}
		};
	};
	
	/**
	 * 隐藏遮罩
	 */
	util.removeMask = function(id){
		var $dest = null;
		if(id != null){
			$dest = $("#" + id);
		}else{
			$dest = $(".mask");
		}
		$dest.find(".mask-content").hide();
		$dest.fadeOut(100,function(){
			$(this).remove();
		});
	};
	
	/**
	 * 显示提示信息
	 */
	util.loading = function(msg,delay){
		var $content = $("<div class='mask-loading'></div>");
		var $textWrap = $("<div class='mask-loading-text'></div>");
		$textWrap.html(msg);
		$content.html($textWrap);
		var maskObj = util.showMask($content,delay);
		util.cache.$loadingId.push(maskObj.id);
		return maskObj.id;
	};
	
	/**
	 * 隐藏提示信息
	 */
	util.hideLoading = function(){
		var loadingIdList = util.cache.$loadingId;
		if(loadingIdList.length > 0){
			util.removeMask(loadingIdList.shift());
		}else{
			//移除相关所有
			$(".mask-loading").closest(".mask").remove();
		}
	};
	
	/**
	 * 消息框
	 * warn,success,error
	 * 
	 */
	util.message = function(){
		
		var options = {
			modal:false,
			delay:3000,
			type:"warn",
			content:""
		};
		
		if ($.isPlainObject(arguments[0])) {
			$.extend(options, arguments[0]);
		} else if(arguments.length <= 2){
			//消息类型type是可选的，第一个参数可以不设置
			var optionalModal = (typeof arguments[1]);
			if(optionalModal == "boolean" || optionalModal == "undefined"){
				options.content = arguments[0];
				options.modal = arguments[1] || false;
			}else{
				options.type = arguments[0];
				options.content = arguments[1];
			}
		}else{
			//三个以上的参数处理
			var extProp = ["type","content","modal"];
			for(var i = 0; i < arguments.length; i++){
				var p = arguments[i];
				options[extProp[i]] = p;
			}
		}
		
		
		var $overlay = null;
		if(options.modal === true){
			$overlay = $("<div class='dialogOverlay'></div>");
			$overlay.appendTo("body");
			$overlay.show();
		}
		
		var $message = $("<div class='xxMessage'><div class='messageContent "+options.type+"'><span class='defaultIcon  " + options.type + "Icon'><\/span></div><\/div>");
		$message.appendTo("body");
		
		var $content = $message.children("div.messageContent");
		$content.append(options.content);
		
		setTimeout(function() {
			if($overlay != null){
				$overlay.remove();
			}
			$message.remove();
		}, options.delay);
		
		
		$message.show().addClass("active");
		
		return $message;
		
	};
	
	
	/**
	 * 
	 * 通用对话框
	 * 
	 */
	util.dialog = function(options, $target){
		var notOptions = options == null ? true : false;
		//可选参数类型
		if(typeof options != "object"){
			if($.isFunction(options)){
				options = {onOk:options};//转换为对象
			}else{
				options = {content:options};//转换为对象
			}
		}
		
		util.cache.idSeq++;
		var dialogName = "dialogName" + util.cache.idSeq;
		
		var config = {
				name:dialogName,
				title:"提示信息",
				type:"none",
				url:null,
				content:"",
				ok:"确定",
				cancel:"取消",
				onInit:null,
				onShow: null,
				onHide: null,
				onOk: null,
				onCancel: null,
				hideCancel:false,
				hideFooter:false,
				buttons:null,
				width:null,
				height:"auto",
				modal: true,
				show:true,
				stopPropagation:true,
				nameSpace:".utilDialog"
		};
		
		//获取绑定的目标
		if($target != null){
			var opObj = $target.data("util-dialog");
			if(opObj != null){
				if(notOptions && opObj.container.parent().length > 0){
					return opObj;//直接返回缓存对象
				}
				config = opObj.config;
				opObj.destroy();
			}else{
				options.content = $target.html();//优先内容
				//是否是模版格式
				if($target.prop("nodeName") != "SCRIPT"){
					$target.empty();//清空，不存留副本
				}
			}
		}
		
		$.extend(config,options);
		
		var wrapContent = "<div class='dialog'></div>";
		$wrapContent = $(wrapContent);
		if(config.width != null){
			$wrapContent.css({width:config.width});//set css
		}
		var $title = $("<div class='dialog-title'>"+config.title+"<span class='dialog-close'>&times;</span></div>");
		var $content = $("<div class='dialog-content'><span class='dialog-icon-"+config.type+"'></span></div>");
		var innerHeight = window.innerHeight ? window.innerHeight : $(window).height();
		$content.css({maxHeight:innerHeight * 0.8,height:config.height});//高度上限
		$content.append(config.content);
		
		if(config.url != null){
			var frameDom = $("<iframe name='"+config.name+"' frameborder='0' height='100%' width='100%' src='"+config.url+"'></iframe>");
			frameDom.on("load",function(){
				$(this).siblings(".loadingIcon").remove();
			});
			//扩展element关闭方法
			frameDom.get(0).close = function(){
				hideFn();
			};
			
			$content.append("<span class='loadingIcon' style='position:absolute;width:auto;padding-left:20px;background-position:left center;'>loading...</span>");
			$content.append(frameDom);
		}
		
		$wrapContent.append($title).append($content);
		//init buttons
		if(config.buttons == null){
			config.buttons = [];
			if(config.ok != null){
				config.buttons.push({text:config.ok,className:"dialog-btn-ok", handler:config.onOk});
			}
			//cancel btn
			if(config.cancel != null && !config.hideCancel){
				config.buttons.push({text:config.cancel,className:"dialog-btn-cancel", handler:config.onCancel});
			}
		}
			
		var $footer = $("<div class='dialog-footer'></div>");
		if(config.buttons != null){
			$.each(config.buttons, function(index, buttonObj){
				var $btn = $("<a href='javascript:;' class='dialog-button' >"+buttonObj.text+"</a>");
				if(buttonObj.className != null){
					$btn.addClass(buttonObj.className);
				}
				
				$btn.on("click", buttonObj, function(evt){
					var cacheObj = evt.data;
					if(config.stopPropagation){
						evt.stopPropagation();
					}
					//next
					var returnVal = invoke(cacheObj.handler, $wrapContent);
					if(returnVal == null || returnVal !== false){
						hideFn();
					}
				});
				$footer.append($btn);
			});
		}
		
		$wrapContent.append($footer);
		//隐藏底部
		if(config.hideFooter){
			$footer.hide();
		}
		
		var maskObj = util.showMask($wrapContent);
		var showId = maskObj.id;
		if(!config.modal){
			//不需要遮罩
			$("#"+showId).find(".mask-overlay").hide();
		}
		if(!config.show){
			//默认隐藏
			maskObj.hide(true);
		}
		
		function invoke(fn){
			try{
				var args = Array.prototype.slice.call(arguments,1);
				return fn && fn.apply(config, args);
			}catch(e){
				console.error("dialog failed:" + e.stack);
			}
		}
		
		function showFn(){
			config.onShow && config.onShow($wrapContent);
			maskObj.show();
		}
		
		function hideFn(){
			config.onHide && config.onHide($wrapContent);
			if($target != null){
				maskObj.hide();
			}else{
				//临时数据直接移除
				util.removeMask(showId);
			}
		}
		
		function resetPosition(){
			maskObj.resetPosition();
		}
		
		
		function changeContent(content){
			$content.html(content);
		}
		
		
		$title.on("mousedown" + config.nameSpace, function(evt){
			var $dest = $(this).closest(".mask-content");
			var zIndex = $dest.css("zIndex");
			$dest.css({zIndex:zIndex++});
			var offset = $(this).offset();
			var dialogX = evt.pageX - offset.left;
			var dialogY = evt.pageY - offset.top;
			$("body").on("mousemove"  + config.nameSpace,function(evt){
				$dest.css({left:evt.clientX - dialogX,top:evt.clientY - dialogY,margin:0});
			});
		});
		$("body").on("mouseup"  + config.nameSpace,function(){
			$(this).off("mousemove"  + config.nameSpace);
		});
		
		$title.find(".dialog-close").click(function(evt){
			if(config.stopPropagation){
				evt.stopPropagation();
			}
			//next
			var returnVal = invoke(config.onCancel, $wrapContent);
			if(returnVal == null || returnVal !== false){
				hideFn();
			}
		});
		
		//init invoke
		invoke(config.onInit, $wrapContent);
		
		//dialog obj
		var opObj = {
			config:config,
			showId:showId,
			target:$target,
			container:$wrapContent,
			show:showFn,
			hide:hideFn,
			close:hideFn,
			resetPosition:resetPosition,
			changeContent:changeContent,
			destroy:function(){
				$("body").off(this.config.nameSpace);
				util.removeMask(this.showId);
				if(this.target != null){
					this.target.removeData("util-dialog");
				}
				
			}
		};
		
		if($target != null){
			$target.data("util-dialog", opObj);
		}
		
		return opObj;
	};
	
	/**
	 * 确认提示对话框
	 * @param titleOptinal 可选参数
	 * 
	 */
	util.confirm = function(titleOptional, msg, callBackFn){
		if(msg == null || typeof msg == "function"){
			callBackFn = msg;
			msg = titleOptional;
			titleOptional = "确认提示";
		}
		
		if(titleOptional == null){
			titleOptional = "确认提示";
		}
		
		return util.dialog({
			title:titleOptional,
			type:"confirm", 
			content:msg,
			onOk:function(){
				callBackFn && callBackFn.call(this,true);
			},
			onCancel:function(){
				callBackFn && callBackFn.call(this,false);
			}
			
		});
	};
	
	/**
	 * 提示信息
	 * @param titleOptinal 可选参数
	 * 
	 */
	util.alert = function(titleOptional, msg, callBackFn){
		if(msg == null || typeof msg == "function" ){
			callBackFn = msg;
			msg = titleOptional;
			titleOptional = "提示信息";
		}
		
		if(titleOptional == null){
			titleOptional = "提示信息";
		}
		
		return util.dialog({
			title:titleOptional,
			type:"info",
			content:msg, 
			onOk:callBackFn, 
			hideCancel:true,
			stopPropagation:true
		});
	};
	
	/**
	 * ajax请求
	 */
	util.ajax = function(conf){
		var setting = {
			showMask:true,
			url:"/",
			type:"get",
			dataType:"text",
			cache:false,
			data:null,
			traditional:true,
			beforeSend:null,
			success:null,
			error:null,
			complete:null
		};
		
		$.extend(setting,conf);
		if(setting.showMask){
			util.loading("正在加载中...");
		}
		var xhr = $.ajax({
			url:setting.url,
			type:setting.type,
			dataType:setting.dataType,
			data:setting.data,
			contentType:setting.contentType,
			cache:setting.cache,
			traditional:setting.traditional,
			beforeSend:setting.beforeSend,
			success:function(data, xhr, textStatus){
				var successFn = setting.success;
				var failedFn = setting.error;
				var assertData = data || {};
				if(assertData.type == null || assertData.type == "" || assertData.type == "success"){
					//success
					if(successFn){
						successFn(data,textStatus,xhr);
					}
				}else{
					//failed
					if(failedFn){
						failedFn(xhr,textStatus, assertData.content);//error invoke
					}else{
						$.message(assertData.type, assertData.content);
					}
				}
				
			},
			error:function(xhr, textStatus, error){
				var failedFn = setting.error;
				if(failedFn){
					failedFn(xhr,textStatus,error);//error invoke
				}else{
					//忽略登录重定向提示
					var loginStatus = xhr.getResponseHeader("loginStatus");
					if (loginStatus != "accessDenied") {
						$.message("error","请求响应失败-" + xhr.status);
					}
				}
			},
			complete:function(xhr,textStatus){
				util.hideLoading();
				if($.isFunction(setting.complete)){
					setting.complete(xhr,textStatus);
				}
			}
		});
		
		return xhr;
	};
	
	//设置了默认提示的requestJson
	util.requestJson = function(){
		var firstParam = arguments[0];
		var url = null;//必须
		var data = null;//可选
		var method = null;//可选
		var successFn = null;//可选
		var failedFn = null;//可选
		var showMask = true;//可选
		var methodList = ["get","post","put","delete","head","options"];
		if(firstParam != null && typeof firstParam != "string"){
			firstParam = $(firstParam);
			url = firstParam.attr("action");
			data = firstParam.serializeArray();
			method = firstParam.attr("method");
		}else{
			url = firstParam;
		}
		
		for(var i = 1; i < arguments.length; i++){
			var param = arguments[i];
			switch(typeof param){
				case "object":
					data = param;
					break;
				case "string":
					if($.inArray(param.toLowerCase(),methodList) != -1){
						method = param;
					}else{
						data = param;//设置为请求数据
					}
					break;
				case "boolean":
					showMask = param;
					break;
				case "function":
					if(successFn == null){
						successFn = param;//第一个为成功函数
					}else{
						failedFn = param;
					}
					break;
			}
		}
		
		return util.ajax({
			url:url,
			type:method || "post",
			dataType:"json",
			data:data,
			showMask:showMask,
			success:successFn,
			error:failedFn
		});
	};
	
	//不提示的Json请求
	util.silentJson = function(){
		var args = Array.prototype.slice.call(arguments,0);
		args.push(false);
		return util.requestJson.apply(this,args);
	};
	
	//强制post请求
	util.postJson = function(){
		var args = Array.prototype.slice.call(arguments,0);
		args.push("post");
		return util.requestJson.apply(this,args);
	};
	
	//序列化表单数据
	util.formData = function(formOrSelector){
		var $form = $(formOrSelector);
		var ary = $form.serializeArray();
		var data = {};
		for(var i = 0; i < ary.length; i++){
			data[ary[i].name] = $.trim(ary[i].value);
		}
		return data;
	};
	
	//下拉选择的菜单
	util.popupMenu = function(options, target){
		var $target = $(target).first();
		var cacheOp = $target.data("util-popupMenu");
		if(cacheOp != null){
			if(options == null){
				return cacheOp;
			}
			//重新初始化
			cacheOp.destroy();
		}
		
		var setting = {
			namespace:".utilPopupMenu",
			content:"",
			data:null,
			split:true,
			showEvent:"mouseenter",
			hideEvent:"mouseleave",
			onShow:null,
			onHide:null,
			direction:"bottom",
			width:"auto",
			handler:null
		};
		$.extend(setting,options);
		util.cache.idSeq++;
		var generatorId = "platform_popupMenu_" + util.cache.idSeq; 
		var $container = $("<div class='common-popupMenu'></div>");
		$container.attr("id",generatorId);
		$container.css({width:setting.width});
		$container.appendTo("body");
		//同步边框
		if($target.css("border-style") != "none"){
			$container.css("border-color",$target.css("border-color"));
		}
		//改变位置
		var changePosition = function(){
			var offset = $target.offset();
			var left = 0;
			var top = 0;
			if(setting.direction == "top"){
				left = offset.left;
				top = offset.top - $container.outerHeight();
			}else{
				left = offset.left;
				top = offset.top + $target.outerHeight();
			}
			$container.css({left:left,top:top});
		};
		
		//改变内容
		var changeContent = function(contentOrData){
			//数组数据，动态添加
			if($.isArray(contentOrData)){
				$container.empty();
				//add data
				var ul = $("<ul class='list'></ul>");
				$.each(contentOrData, function(indexOrKey, item){
					var li = $("<li><a href='javascript:;'>--</a></li>");
					var anchor = li.children("a");
					anchor.data("util-popup-data",item);
					if(setting.split){
						//分割显示的样式
						li.addClass("split");
					}
					if(item.name != null){
						anchor.html(item.name);
					}
					if(item.className != null){
						anchor.addClass(item.className);
					}
					if(item.handler != null){
						anchor.on("click",function(evt){
							evt.stopPropagation();
							var data = $(this).data("util-popup-data");
							if(data.handler){
								data.handler(data, evt);
							}
						});
					}else if(setting.handler != null){
						anchor.on("click",function(evt){
							evt.stopPropagation();
							setting.handler($(this).data("util-popup-data"),evt);
						});
					}
					
					if(item.url != null){
						anchor.attr("href",item.url);
					}
					if(item.target != null){
						anchor.attr("target",item.target);
					}
					ul.append(li);
				});
				$container.append(ul);
			}else{
				//普通数据
				$container.html(contentOrData);
			}
			
			//修正位置
			changePosition();
		};
		
		//显示
		var show = function(){
			try{
				setting.onShow && setting.onShow.call($container);
			}catch(e){
				console.error("popupMenu show failed",e.stack);
			}
			changePosition();
			$container.show();
		};
		
		//隐藏
		var hide = function(){
			try{
				setting.onHide && setting.onHide.call($container);
			}catch(e){
				console.error("popupMenu hide failed",e.stack);
			}
			$container.hide();
		};
		
		//添加默认数据
		if(setting.content != null){
			changeContent(setting.content);
		}
		if(setting.data != null){
			changeContent(setting.data);
		}
		
		$target.on(setting.showEvent + setting.namespace,function(evt){
			evt.stopPropagation();
			show();
		});
		
		var hideTarget = $target;
		if(setting.hideEvent == "click"){
			hideTarget = $(document);
			$target.on(setting.hideEvent + setting.namespace,function(evt){
				evt.preventDefault();
				evt.stopPropagation();
			});
			$container.on(setting.hideEvent,function(evt){
				evt.stopPropagation();
			});
		}
		
		hideTarget.on(setting.hideEvent + setting.namespace, function(evt){
			var $relatedTarget = $(evt.relatedTarget);
			if($relatedTarget.get(0) == $container.get(0) 
					|| $relatedTarget.closest(".common-popupMenu").get(0) == $container.get(0)){
				$container.off(setting.hideEvent).on(setting.hideEvent,function(evt){
					evt.stopPropagation();
					hide();
				});
				return;
			}
			return hide();
		});
		

		//操作
		var popupOp = {
				setting:setting,
				target:$target,
				container:$container,
				changePosition:changePosition,
				changeContent:changeContent,
				show:show,
				hide:hide,
				destroy:function(){
					var showEvt = this.setting.showEvent + this.setting.namespace;
					var hideEvt = this.setting.hideEvent + this.setting.namespace;
					this.target.off(showEvt);
					this.target.off(hideEvt);
					$(document).off(hideEvt);
					this.container.remove();
					this.target.removeData("util-popupMenu");
				}
		};
		
		
		$target.data("util-popupMenu",popupOp);
		return popupOp;
	};
	
	//悬浮提示,添加的事件不能叠加
	util.tip = function(options,target){
		var $target = $(target).first();
		if($target.length == 0){
			console.error("target selector '"+$target.selector+"' not found");
			return null;
		}
		
		var cacheOp = $target.data("util-tip");
		if(cacheOp != null){
			if(options == null){
				return cacheOp;
			}
			//重新初始化
			cacheOp.destroy();
		}
		var imgURL = "data:image/gif;base64,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";
		var setting = {
				namespace:".utilTip",
				content:"",
				url:null,
				params:null,
				type:"get",
				dataType:"text",
				success:null,
				error:null,
				loadingMsg:"<span style='color:gray;'><img src='"+imgURL+"'/> 正在加载数据...</span>",
				errorMsg:"<span style='color:red;padding:4px;'>数据加载失败:{0}</span>",
				direction:"bottom",
				showEvent:"mouseenter",
				hideEvent:"mouseleave",
				onShow:null,
				onHide:null,
				offsetX:0,
				offsetY:0,
				maxWidth:"auto",
				zIndex:"",
				linger:false,
				xhr:null
		};
		
		$.extend(setting,options);
		
		//默认取title属性
		if($.trim(setting.content) == ""){
			setting.content = $target.attr("title");
			$target.removeAttr("title");
		}
		
		util.cache.idSeq++;
		var generatorId = "platform_tip_" + util.cache.idSeq; 
		var $container = $("<div class='tip-container'><div class='tip-arrow'></div></div>");
		
		//展示的消息内容在鼠标一定范围内不隐藏
		if(setting.linger){
			var spillSize = -12;
			var $bgHold = $("<div></div>");
			$bgHold.css({
				position:"absolute",
				zIndex:"-1",
				top:spillSize,
				bottom:spillSize,
				left:spillSize,
				right:spillSize
			});
			$container.append($bgHold);
		}
		//$container.css({"pointer-events":"none"});//穿透解决方案
		$container.attr("id",generatorId);
		$container.append(setting.content || "");
		$container.css({maxWidth:setting.maxWidth, zIndex:setting.zIndex, "word-wrap":"break-word"});//限制宽度
		$container.appendTo("body");
		
		var directionList = ["bottom","top","left","right"];
		if($.inArray(setting.direction,directionList) == -1){
			setting.direction = directionList[0];
		}
		$container.addClass(setting.direction);
		
		//修改位置
		var changePosition = function(){
			var offset = $target.offset();
			var wth = $target.outerWidth();
			var hth = $target.outerHeight();
			var cWth = $container.outerWidth();
			var cHth = $container.outerHeight();
			var left = 0;
			var top = 0;
			var borderSize = 9;
			switch(setting.direction){
				case "top":
					left = offset.left + ((wth - cWth) / 2);
					top = offset.top - cHth - borderSize;
					break;
				case "bottom":
					left = offset.left + ((wth - cWth) / 2);
					top = offset.top + hth + borderSize;
					break;
				case "left":
					left = offset.left - cWth - borderSize;
					top = offset.top + ((hth - cHth) / 2);
					break;
				case "right":
					left = offset.left + wth + borderSize;
					top = offset.top + ((hth - cHth) / 2);
					break;
			}
			
			if(setting.offsetX != null){
				left += setting.offsetX; 
			}
			
			if(setting.offsetY != null){
				top += setting.offsetY;
			}
			
			//边界范围限制
			var $parentDom = $container.offsetParent();
			var right = left + cWth;
			var bottom = top + cHth;
			var maxRight = $parentDom.scrollLeft() + $parentDom.innerWidth();
			var maxBottom = $parentDom.scrollTop() + $parentDom.innerHeight();
			
			if(setting.direction == "top" || setting.direction == "bottom"){
				var $arrow = $container.children(".tip-arrow");
				if(left < 0){
					$arrow.css({left:$arrow.position().left + left});
					left = 0;
				}else if (right > maxRight){
					var rightOffset = right - maxRight;
					$arrow.css({left:(cWth / 2) + rightOffset});
					left -= rightOffset;
				}else{
					$arrow.css({left:""});
				}
			}
			
			$container.css({left:left,top:top});
		};
		
		//修改内容
		var changeContent = function(content){
			$container.html(content || "");
			changePosition();
		};
		 
		//显示,evt可选
		var show = function(evt){
			var rs = null;
			try{
				rs = setting.onShow && setting.onShow(evt);
			}catch(e){
				console.error("tip show failed",e.stack);
			}
			$container.show();
			changePosition();
			//加载动态内容
			if(setting.url != null){
				if(setting.xhr != null && setting.xhr.readyState != 4){
					//正在加载数据，不终止请求
					return rs;
				}
				
				$.ajax({
					url:setting.url,
					data:setting.params,
					type:setting.type,
					dataType:setting.dataType,
					beforeSend:function(xhr){
						setting.xhr = xhr;
						changeContent(setting.loadingMsg);
					},
					success:function(data,textStatus,xhr){
						if(setting.success){
							var result = setting.success(data,textStatus,xhr);
							changeContent(result);
						}else{
							changeContent(data);
						}
						setting.xhr = null;
					},
					error:function(xhr,textStatus, e){
						if(setting.error){
							var result = setting.error(xhr,textStatus,e);
							changeContent(result);
						}else{
							var errorMsg = setting.errorMsg;
							if(errorMsg != null){
								var args = [xhr.status,e.message,xhr.responseText];
								errorMsg = errorMsg.replace(/\{\s*(\d+)\s*\}/,function($0,$1){
									return args[$1];
								});
							}
							changeContent(errorMsg);
						}
						setting.xhr = null;
					}
				});
			}
			
			return rs;
		};
		
		//隐藏,evt可选
		var hide = function(evt){
			var rs = null;
			try{
				rs = setting.onHide && setting.onHide(evt);
			}catch(e){
				console.error("tip hide failed",e.stack);
			}
			$container.hide();
			return rs;
		};
		
		$target.on(setting.showEvent + setting.namespace,function(evt){
			evt.stopPropagation();
			return show(evt);
		});
		
		var hideDest = $target;
		//特殊处理click
		if(setting.hideEvent == "click"){
			$container.on(setting.hideEvent,function(evt){
				evt.stopPropagation();//防止关闭
			});
			hideDest = $(document);//通过文档范围隐藏
		}
		//隐藏目标
		hideDest.on(setting.hideEvent + setting.namespace,function(evt){
			evt.stopPropagation();
			var closeDest = function(evt){
				evt.currentTarget = $target.get(0);//设置为默认目标
				return hide(evt);
			};
			var $relatedTarget = $(evt.relatedTarget);
			if($relatedTarget.get(0) == $container.get(0) 
					|| $relatedTarget.closest(".tip-container").get(0) == $container.get(0)){
				$container.off(setting.hideEvent).on(setting.hideEvent,function(evt){
					evt.stopPropagation();
					return closeDest(evt);
				});
				return;
			}
			
			return closeDest(evt);
		});
		

		//操作
		var tipOp = {
				setting:setting,
				changePosition:changePosition,
				changeContent:changeContent,
				show:show,
				hide:hide,
				target:$target,
				container:$container,
				destroy:function(){
					var showEvt = this.setting.showEvent + this.setting.namespace;
					var hideEvt = this.setting.hideEvent + this.setting.namespace;
					$(document).off(hideEvt)
					this.target.off(hideEvt);
					this.target.off(showEvt);
					this.target.removeData("util-tip");
					this.container.remove();
				}
		};
		
		$target.data("util-tip",tipOp);
		//返回提示框对象
		return tipOp;
	};
	
	/**
	 * 字符串格式化
	 * 
	 */
	util.format = function( source, params ) {
		if ( arguments.length === 1 ) {
			return function() {
				var args = $.makeArray(arguments);
				args.unshift(source);
				return util.format.apply( this, args );
			};
		}
		if ( arguments.length > 2 && params.constructor !== Array  ) {
			params = $.makeArray(arguments).slice(1);
		}
		if ( params.constructor !== Array ) {
			params = [ params ];
		}
		$.each(params, function( i, n ) {
			source = source.replace( new RegExp("\\{" + i + "\\}", "g"), function() {
				return n;
			});
		});
		return source;
	};
	
	/**
	 * 解析url
	 */
	util.parseHash = function(hash) {
		var tag, query, param = {};
		var arr = [];
		if(hash != null){
			arr = hash.split('?');
		} 
		tag = arr[0];
		if (arr.length > 1) {
			var seg, s;
			query = arr[1];
			seg = query.split('&');
			for (var i = 0; i < seg.length; i++) {
				if (!seg[i]){
					continue;
				}
				s = seg[i].split('=');
				param[s[0]] = s[1];
			}
		}
		return {
			hash : hash,
			tag : tag,
			query : query,
			param : param
		}
	};

	/**
	 * 格式化date
	 * 
	 * @param date
	 * @param format
	 */
	util.formatDate = function(date, format) {
		
		if(typeof date == "number" || $.isNumeric(date)){
			date = new Date(parseInt(date));
		}
		
		if(date == null || !(date instanceof Date)){
			return "";
		}
		
		var o = {
			"M+" : date.getMonth() + 1, // month
			"d+" : date.getDate(), // day
			"h+" : date.getHours() % 12, // hour % 12
			"H+" : date.getHours(), // hour
			"m+" : date.getMinutes(), // minute
			"s+" : date.getSeconds(), // second
			"q+" : Math.floor((date.getMonth() + 3) / 3), // quarter
			"S" : date.getMilliseconds() //millisecond
		}
		
		if(format == null){
			format = "yyyy-MM-dd HH:mm:ss";
		}
		
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (date.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	};
	
	/**
	 * html 编码
	 * 
	 */
	util.escapeHtml = function(content){
		if(content == null){
			return content;
		}
		
		var escapeMap = {
		    "<": "&#60;",
		    ">": "&#62;",
		    '"': "&#34;",
		    "'": "&#39;",
		    "&": "&#38;"
		};

		return content.toString().replace(/&(?![\w#]+;)|[<>"']/g, function (s) {
		    return escapeMap[s];
		});
	}
	
	/**
	 * 缩写,按字节计算宽度
	 * 
	 */
	util.abbreviate = function(text, size, ellipsis){
		if(text == null || size == null || size <= 0){
			return text;
		}
		if(ellipsis == null){
			ellipsis = "...";
		}
		
		var str = text.toString();
		var chPattern = /[\u4e00-\u9fa5\ufe30-\uffa0]+$/;
		var newStr = "";
		var countWidth = 0;
		for(var i = 0 ; i < str.length; i++){
			var c = str[i];
			if(chPattern.test(c)){
				countWidth += 2;
			}else{
				countWidth += 1;
			}
			if(countWidth > size){
				continue;
			}
			
			newStr += c;
		}
		
		if(countWidth <= size){
			ellipsis = "";
		}
		
		return newStr + ellipsis;
	}
	
	/**
	 * 转换数据大小单位
	 */
	util.convertByte = function (str){
		if(str == null){
			return 0;
		}
		var size = 0;
		var unit = str.match(/^\s*(\d+)([a-zA-Z]+)\s*$/);
		if(unit != null){
			var lowerUnit = unit[2].toLowerCase();
			switch(lowerUnit){
			case "b":
				size = parseInt(unit[1]);
				break;
			case "kb":
				size = parseInt(unit[1]) * 1024;
				break;
			case "mb":
				size = parseInt(unit[1]) * 1024 * 1024;
				break;
			}
		}else{
			size = parseInt(str);
		}
		return size;
	};
	
	/**
	 * 刷新页面
	 * 
	 */
	util.refresh = function(immediate){
		if(immediate){
			location.reload(true);
		}else{
			setTimeout(function(){
				location.reload(true);
			},600);
		}
	};
	
	/**
	 * 分解为上下文绝对路径
	 */
	util.resolvePath = function(path, isNoCache){
		var basePath = shop.base;
		var noCacheIdent = shop.initDate;
		var absPattern = new RegExp("^/");
		var noCacheFn = function(path){
			if(isNoCache){
				if(path.indexOf("?") == -1){
					return path + "?_tag=" + noCacheIdent; 
				}else{
					return path + "&_tag=" + noCacheIdent;
				}
			}
			
			return path;
		};
		
		if(absPattern.test(path)){
			if(basePath != null && basePath != ""){
				var basePattern = new RegExp("^" + basePath.replace("/", "\/"));
				if(basePattern.test(path)){
					return noCacheFn(path);
				}
				
				return noCacheFn(basePath + path);
			}
			
			return noCacheFn(path);
		}
		
		return noCacheFn(path);
		
	};
	
	/**
	 * 加载js文件
	 */
	util.loadJs = function(){
		var loadAry = [];
		$.each(arguments, function(index, param){
			if(typeof param == "string"){
				loadAry.push(param);
			}else{
				loadAry = loadAry.concat(param);
			}
		});
		
		var loadHandler = function(url){
			var deferred = $.Deferred();
			var absUrl = util.resolvePath(url, true);
			var scriptDom = document.createElement("script");
			scriptDom.src = absUrl;
			scriptDom.onload = function(){
				deferred.resolve(this);
			};
			scriptDom.onerror = function(){
				deferred.reject(this);
			};
			var headDom = document.head || document.getElementsByTagName("head")[0];
			headDom.appendChild(scriptDom);
			
			return deferred;
		}
		
		var flowDefer = $.Deferred();
		
		var recursive = function(){
			if(loadAry.length > 0){
				var item = loadAry.shift();
				loadHandler(item).done(function(url){
					recursive();
				}).fail(function(){
					flowDefer.reject();
				});
			}else{
				flowDefer.resolve();
			}
		};
		
		recursive();
		
		return flowDefer.promise();
		
	};
	
	/**
	 * 加载css文件
	 */
	util.loadCss = function(){
		var loadAry = [];
		$.each(arguments, function(index, param){
			if(typeof param == "string"){
				loadAry.push(param);
			}else{
				loadAry = loadAry.concat(param);
			}
		});
		
		var flowDefer = $.Deferred();
		
		var loadHandler = function(url){
			var deferred = $.Deferred();
	    	var link = document.createElement("link");
	    	var absUrl = util.resolvePath(url, true);
			link.href = absUrl;
			link.rel = "stylesheet";
			link.onload = function(){
				deferred.resolve(this);
			};
			
			link.onerror = function(){
				deferred.reject(this);
			};
			
			var headDom = document.head || document.getElementsByTagName("head")[0];
			headDom.appendChild(link);
		    
		    return deferred;
		}
		
		var recursive = function(){
			if(loadAry.length > 0){
				var item = loadAry.shift();
				loadHandler(item).done(function(url){
					recursive();
				}).fail(function(){
					flowDefer.reject();
				});
			}else{
				flowDefer.resolve();
			}
		};
		
		recursive();
		
	    return flowDefer.promise();
	};
	
	
	//注册函数
	util.register = function(name, fnCallback){
		if(util.cache.moduleData[name] != null){
			console.warn("不允许重复注册函数:" + name);
			return util.cache.moduleData[name];
		}
		return util.cache.moduleData[name] = fnCallback;
	};
	
	//获取模块
	util.fetchModule = function(name){
		if(name != null){
			var fnCallback = util.cache.moduleData[name];
			if(fnCallback != null){
				return fnCallback;
			}
		}
	};
	
	//初始化操作
	util.dataExtend = function(selector){
		var invokeFn = function(invokeStr, $this){
			if($.trim(invokeStr) != ""){
				var targetObj = $this;
				var fnName = invokeStr;
				var param = invokeStr.match(/\(.*\)/);
				if(param != null){
					fnName = invokeStr.substring(0,param.index);
				}else{
					invokeStr = invokeStr + "()";
				}
				
				//查找局部范围
				var findParam = util.fetchModule(fnName);
				//查找全局函数
				if(findParam == null){
					findParam = window[fnName];//默认范围
					targetObj = null;
				}
				
				if(findParam == null){
					console.error("未找到定义的函数：" + fnName +",表达式：" + invokeStr);
					return;
				}
				
				//执行目标
				try{
					var callFn = new Function(fnName, invokeStr);
					callFn.call($this, $.proxy(findParam, targetObj));
				}catch(e){
					console.error("invoke failed", e.stack);
				}
				
			}
		};
		
		var $dom = $(selector);
		var allInit  = $dom.find("[data-init]");
		allInit.each(function(index, dom){
			var $this = $(dom);
			var initVal = $this.attr("data-init");
			var initialized = $this.data("dataInit-initialized");
			if(initialized){
				console.error("target " + initVal + " already initialized!!");
				return;
			}
			invokeFn(initVal,$this);
			$this.data("dataInit-initialized",true);
		});
		
		//所有默认绑定的事件
		var clickEventName = "click.dataExtend";
		$dom.off(clickEventName);//移除原有空间事件
		$dom.on(clickEventName, "[data-click]", function(evt){
			evt.stopPropagation();//禁止冒泡
			var $this = $(evt.currentTarget);
			var invokeStr = $this.attr("data-click");
			invokeFn(invokeStr,$this);
			
		});
		
		var changeEventNames = ["change.dataExtend", "keyup.dataExtend"];
		$.each(changeEventNames, function(index, name){
			$dom.off(name);//移除原有空间事件
			$dom.on(name, "[data-change]", function(evt){
				evt.stopPropagation();//禁止冒泡
				var $this = $(evt.currentTarget);
				var invokeStr = $this.attr("data-change");
				invokeFn(invokeStr,$this);
			});
		});
		
		
		
	};
	
	//注入全局命名空间
	window.util = util;
	try{
		//already been used
		Object.defineProperty(window, "util", {
			get:function(){
				return util;
			},
			set:function(){
				throw new Error("name 'util' already been used");
			},
			configuration:false
		});
	}catch(e){
		console.log("warning info - " + e.message);
	}
	
	//扩展方法默认调用
	$(function(){
		//a标签禁用
		var $doc = $(document);
		$doc.find("a").click(function(evt){
			//预绑定事件，可阻止jquery的后续操作
			if($(this).is("[disabled]")){
				evt.preventDefault();
				evt.stopImmediatePropagation();
			}
		});
		$doc.on("click", "a[disabled]", function(evt){
			evt.preventDefault();
			evt.stopImmediatePropagation();
		});
		
		//其他
		util.dataExtend(document);
		
	});
	
})(jQuery);

