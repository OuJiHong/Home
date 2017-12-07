/**
 * 地区选择
 * 
 */

(function($){
	
	var cacheData = {
		_cacheIndex:0,
		nextIndex:function(){
			cacheData._cacheIndex++;
			return cacheData._cacheIndex;
		},
		_areaCache:{},
		setArea:function(id, areaData){
			cacheData._areaCache[id] = areaData;
		},
		getArea:function(id){
			return cacheData._areaCache[id];
		}
	};
	
	
	$.fn.areaSelect = function(options){
		
		var _select = this;
		
		var cacheOp = _select.data("areaSelect-cacheOp");
		if(cacheOp != null){
			return cacheOp;
		}
		
		var config = {
				url:null,
				queryUrl:null,
				data:null,
				idKey:"id",
				textKey:"text",
				parentKey:"parent",
				value:null,
				disabledValues:[],
				param:{parentId:"T{id}"},
				requestMethod:"get",
				multiple:true,
				synParent:false,
				levelLimit:1,
				contentTemplate:null,
				referName:"refer",
				titleIdent:"header-title",
				levelWrapperIdent:"level-wrapper",
				areaIdent:"area-item",
				selectedClass:"selected",
				loadingText:"loading...",
				titleText:"请选择",
				onSelected:null,
				nameSpace:".areaSelect"
		};
		
		$.extend(config,options);
		
		var nameSpace = config.nameSpace;
		var areaTemplate = {
				container:"<div class='areaSelect-container'></div>",
				header:"<div class='areaSelect-header'></div>",
				headerTitle:"<div class='areaSelect-title'></div>",
				loading:"<div class='areaSelect-loading'></div>",
				levelWrapper:"<div class='areaSelect-wrapper'></div>",
				checkTool:"<div class='areaSelect-checkTool'></div>",
				checkAllBtn:"<label class='areaSelect-checkAll'><input type='checkbox'/>全选</label>"
		};
		
		if(config.multiple){
			_select.prop("multiple",true);
		}
		
		_select.hide();
		_select.addClass("areaSelect-hidden");
		var _container = $(areaTemplate.container);
		_select.after(_container);
		var _areaHeader = $(areaTemplate.header);
		_container.append(_areaHeader);
		
		/**
		 * 获取所有标题
		 * 
		 */
		function getTitles(){
			return _areaHeader.find("["+config.titleIdent + "]");
		}
		
		/**
		 * 获取所有条目
		 */
		function getAreaItems(levelWrapper){
			return levelWrapper.find("["+config.areaIdent+"]");
		}
		
		/**
		 * 
		 * 获取指定级别的包裹容器
		 * 
		 */
		function getLevelWrapper(level){
			var wrapperDom = _container.find("["+config.levelWrapperIdent+"='"+level+"']");
			if(wrapperDom.length == 0){
				var containerId = _container.attr("id") || "";
				var wrapperId = "areaSelectWrapper_" + containerId + cacheData.nextIndex();
				var titId = "areaSelectTip_" + containerId + cacheData.nextIndex();
				wrapperDom = $(areaTemplate.levelWrapper);
				wrapperDom.attr("id",wrapperId);
				wrapperDom.attr(config.levelWrapperIdent, level);
				_container.append(wrapperDom);
				//标题
				var headerTitle = $(areaTemplate.headerTitle);
				headerTitle.attr("id",titId);
				headerTitle.attr(config.titleIdent,level);
				headerTitle.html(config.titleText);
				_areaHeader.append(headerTitle);
				
				headerTitle.attr(config.referName,wrapperId);
				wrapperDom.attr(config.referName,titId);
			}
			return wrapperDom;
			
		}
		
		/**
		 * 获取所有级别
		 * 
		 */
		function getAllLevelWrapper(){
			return _container.find("["+config.levelWrapperIdent+"]");
		}
		
		/**
		 * 默认模版
		 */
		function defaultTemplate(){
			var def = $("<script type='text/html'></script>");
			var tmpStr = "<div class='areaSelect-list'>" +
							"{each $data as item index}" +
								"<div class='areaSelect-item' "+config.areaIdent+"='{index}' >" +
									"{item}" + 
								"</div>" +
							"{/each}" +
						"</div>";
			def.html(tmpStr);
			return def;
		} 
		
		/**
		 * 获取模版
		 */
		function fetchTemplate(){
			if(config.contentTemplate != null){
				//自定义模版需要指定：config.areaIdent 标识
				return $(config.contentTemplate);
			}
			return defaultTemplate();
		}
		
		/**
		 * 渲染数据
		 * 
		 */
		function renderData(levelWrapper, data){
			var $templateDom = fetchTemplate();
			levelWrapper.empty();
			levelWrapper.html($templateDom.render(data));
			
			_select.empty();
			_select.append("<option value=''></option>");
			var areaList = levelWrapper.find("["+config.areaIdent+"]");
			//添加数据
			$.each(data,function(index, areaData){
				var $areaItem = areaList.filter("["+config.areaIdent+"='"+index+"']");
				$areaItem.click(function(evt){
					var $this = $(this);
					if($this.hasClass("areaSelect-disabled")){
						evt.preventDefault();
						evt.stopPropagation();
						evt.stopImmediatePropagation();
					}
				});
				var storeAreaData = {};
				if(typeof areaData == "string"){
					//构建默认对象
					storeAreaData[config.idKey] = index;
					storeAreaData[config.textKey] = areaData;
				}else{
					storeAreaData = areaData;
				}
				//内置函数~
				storeAreaData.$fullText = function(separate){
					var fullText = [];
					var parent = this[config.parentKey];
					while(parent != null){
						fullText.push(parent[config.textKey]);
						parent = parent[config.parentKey];
					}
					fullText.reverse().push(this[config.textKey]);
					var comStr = fullText.join(separate || "");
					return comStr;
				};
				
				$areaItem.data("areaData", storeAreaData);
				_select.append("<option value='"+storeAreaData[config.idKey]+"'></option>");
			});
			
			//禁用值
			if(config.disabledValues != null && config.disabledValues.length > 0){
				$.each(config.disabledValues, function(index , id){
					areaList.each(function(dIndex, areaItem){
						var $areaItem = $(areaItem);
						var areaData = $areaItem.data("areaData");
						if(areaData != null && areaData[config.idKey] == id){
							$areaItem.addClass("areaSelect-disabled");
							$areaItem.attr("disabled", "disabled");
							//同步父级元素禁用
							if(config.synParent){
								$areaItem.parent().addClass("areaSelect-disabled");
							}
						}
					});
				});
			}
			
			//可全选和反选
			var curLevel =  levelWrapper.attr(config.levelWrapperIdent);
			if(config.multiple && config.levelLimit == curLevel){
				var $checkTool = $(areaTemplate.checkTool);
				var $checkAllBtn = $(areaTemplate.checkAllBtn);
				$checkAllBtn.find("input").on("click",levelWrapper,function(evt){
					var $this = $(this);
					var bindWrapper = evt.data;
					var checkboxVal = $this.prop("checked");
					var areaItems = getAreaItems(bindWrapper).not(".areaSelect-disabled");
					if(checkboxVal){
						selectItemHandler(areaItems,true);
					}else{
						selectItemHandler(areaItems,false);
					}
					
				});
				
				$checkTool.append($checkAllBtn);
				
				levelWrapper.append($checkTool);
			}
			

			
		}
		
		/**
		 * 
		 * 加载内容,
		 * 动态对象"T{}"
		 * 
		 */
		function loadContent(levelWrapper, itemData, afterFn){
			var requestData = {};
			if(itemData != null && config.param != null){
				$.each(config.param,function(key,value){
					requestData[key] = value;
					if(typeof value == "string"){
						requestData[key] = value.replace(/T\{\s*(\w+)\s*\}/,function(p0,p1){
							return itemData[p1] || "";
						});
					}
				});
				
			}
			
			$.ajax({
				url:config.url,
				type:config.requestMethod,
				dataType:"json",
				data:requestData,
				beforeSend:function(){
					var loadingDom = $(areaTemplate.loading);
					loadingDom.html("<span class='areaSelect-loading-icon'>" + config.loadingText + "</span>");
					var baseHeight = levelWrapper.innerHeight();
					if(baseHeight > 0){
						loadingDom.css({"height":baseHeight,"lineHeight":baseHeight + "px"});
					}
					levelWrapper.html(loadingDom);
				},
				success:function(data){
					renderData(levelWrapper,data);
					afterFn && afterFn.call(levelWrapper,data);
					//缓存
					if(itemData != null && itemData[config.idKey] != null){
						cacheData.setArea(itemData[config.idKey],data);
					}
					
				},
				error:function(xhr,textStatus,error){
					levelWrapper.html("<span style='color:red;padding:4px;'>loading error:" + error + "</span>");
				},
				complete:function(){
					//nothing
				}
			});
		}
		
		/**
		 * 选中处理
		 * 
		 */
		function selectItemHandler($areaItem, forceSelected, afterFn){
			if(typeof forceSelected == "function"){
				afterFn = forceSelected;
				forceSelected = null;
			}
			
			if($areaItem == null || $areaItem.length == 0){
				console.error("selectItem error: areaItem is empty - " + $areaItem);
				return false;
			}
			
			var areaData = $areaItem.data("areaData") || {}; 
			var areaId = areaData[config.idKey];//主键
			var levelWrapper = $areaItem.closest("["+config.levelWrapperIdent+"]");
			var level = parseInt(levelWrapper.attr(config.levelWrapperIdent)) || 0;
			var $title = $("#" + levelWrapper.attr(config.referName));
			$title.show();
			
			//成功选择
			var isSelected = false;
			if(config.multiple){
				//切换选中,非强制约束
				if(!forceSelected && $areaItem.hasClass(config.selectedClass)){
					$areaItem.removeClass(config.selectedClass).prop("checked",false);
				}else{
					$areaItem.addClass(config.selectedClass).prop("checked",true);
					isSelected = true;
				}
			}else{
				getAreaItems(levelWrapper).removeClass(config.selectedClass).prop("checked",false);
				$areaItem.addClass(config.selectedClass).prop("checked",true);//选中
				isSelected = true;
			}
			
			//更新文本和父级对象
			var titleAry = [];
			var idAry = [];
			var previousWrapper = levelWrapper.prev("["+config.levelWrapperIdent+"]");
			var parentItem = findAreaItem(previousWrapper);
			var parentData = parentItem.data("areaData");
			findAreaItem(levelWrapper).each(function(index, item){
				var areaData = $(item).data("areaData");
				titleAry.push(areaData[config.textKey] || "-");
				idAry.push(areaData[config.idKey]);
				//设置父级对象
				areaData[config.parentKey] = parentData;
			});
			
			if(titleAry.length > 0){
				$title.html(titleAry.join(","));
			}else{
				$title.html(config.titleText);
			}
			
			if(level == config.levelLimit){
				_select.val(idAry);//达到指定级别则赋值
			}else{
				_select.val(null);
			}
			
			
			if(isSelected){
				//加载下一个视图
				if(level < config.levelLimit){
					levelWrapper.hide();//隐藏之前的
					var nextLevel = level + 1;
					var nextLevelWrapper = getLevelWrapper(nextLevel);
					nextLevelWrapper.show();
					var nextTitle = $("#" + nextLevelWrapper.attr(config.referName));
					getTitles().removeClass("active");
					nextTitle.addClass("active");
					nextTitle.show();
					if(cacheData.getArea(areaId) != null){
						renderData(nextLevelWrapper, cacheData.getArea(areaId) );
						afterFn && afterFn.call($areaItem, cacheData.getArea(areaId) );
					}else{
						loadContent(nextLevelWrapper,areaData, afterFn);
					}
				}else{
					config.onSelected && config.onSelected.call($areaItem,areaData);
					afterFn && afterFn.call($areaItem, []);
				}
				
			}
			
			
			
		}
		
		/**
		 * 
		 * 查找条目,默认找选中的
		 * 
		 */
		function findAreaItem(levelWrapper, id){
			var areaItems = getAreaItems(levelWrapper);
			var findItem = null;
			if(id != null){
				//查找id
				areaItems.each(function(index, areaItem){
					var $areaItem = $(areaItem);
					var areaData = $areaItem.data("areaData");
					if(areaData != null && areaData[config.idKey] == id){
						findItem  = $areaItem;
						return false;
					} 
				});
				
			}else{
				//查找选中
				findItem = $();
				areaItems.each(function(index, areaItem){
					var $areaItem = $(areaItem);
					if($areaItem.hasClass(config.selectedClass)){
						findItem  = findItem.add($areaItem);
					}
				});
			}
			
			return findItem;
		}
		
		
		/**
		 * 初始化选中的值
		 * 
		 */
		function initValue(initData){ 
			if(initData == null || initData.length == 0){
				return;
			}
			
			var firstData = null;
			var dataIsArray = false;
			if($.isArray(initData)){
				firstData = initData[0];//多选的值，以第一个树路径为准
				dataIsArray = true;
			}else{
				firstData = initData;
			}
			
			//字符串默认为id
			if(typeof firstData == "string"){
				if(config.queryUrl == null){
					console.error("initValue:required queryUrl - " + firstData);
					return false;
				}
				var queryData = {};
				queryData[config.idKey] = firstData;//默认取id
				$.ajax({
					url:config.queryUrl,
					type:"get",
					dataType:"json",
					data:queryData,
					async:false,
					success:function(data){
						firstData = data;
					},
					error:function(xhr,textStatus,error){
						console.error("query "+firstData+" failed:" + error);
					}
				});
				
				if(firstData == null){
					console.error("initValue query failed - " + firstData);
				}
				
			}
			
			var treePath = firstData.treePath;//地区树路径
			if(treePath != null){
				var ids = treePath.replace(/^\s*,|,\s*$/g,"").split(",");
				ids.push(firstData[config.idKey]);//加上自身id
				var idIndex = -1;
				var nextFlow = function(){
					idIndex++;
					if(idIndex < ids.length){
						var levelWrapper = getLevelWrapper(idIndex);
						var findItem = findAreaItem(levelWrapper, ids[idIndex]);
						if(findItem != null && findItem.length != 0){
							selectItemHandler(findItem,true,function(){
								nextFlow();
							});
						}else{
							nextFlow();
						}
					}else{
						//结束，选中所有的值
						if(dataIsArray){
							var lastWrapper = getAllLevelWrapper().last();
							$.each(initData,function(index, value){
								var findItem = null;
								if(typeof value == "string"){
									findItem = findAreaItem(lastWrapper,value);
								}else{
									findItem = findAreaItem(lastWrapper,value[config.idKey]);
								}
								if(findItem != null){
									selectItemHandler(findItem,true);
								}
							});
							
						}
						//end...
					}
				};
				
				nextFlow();
				
			}else{
				console.error("initValue error: treePath not found ");
			}
			
		}
		
		/**
		 * 初始化
		 * 
		 */
		function initArea(){
			var rootLevelWrapper = getLevelWrapper(0);
			rootLevelWrapper.css({display:"block"});//强制显示
			var headerTitle = $("#" + rootLevelWrapper.attr(config.referName));
			headerTitle.css({display:"block"});//强制显示
			headerTitle.addClass("active");
			if(config.data != null){
				renderData(rootLevelWrapper,config.data);
			}else{
				//config.value 为初始数据
				loadContent(rootLevelWrapper, null, function(){
					initValue(config.value);
				});
			}
			
			//init event
			_container.on("click" + nameSpace,"["+config.areaIdent+"]",function(evt){
				var $areaItem = $(this);
				selectItemHandler($areaItem);
			});
			
			_container.on("click" + nameSpace,"["+config.titleIdent+"]",function(evt){
				var $headerTitle = $(this);
				$headerTitle.html(config.titleText);
				if(!$headerTitle.hasClass("active")){
					//激活
					$headerTitle.addClass("active");
					$headerTitle.nextAll("["+config.titleIdent+"]").html(config.titleText).hide();
					var levelWrapper = $("#" + $headerTitle.attr(config.referName));
					levelWrapper.show();
					levelWrapper.nextAll("["+config.levelWrapperIdent+"]").hide();
					//clear
					var areaItems = getAreaItems(levelWrapper);
					areaItems.removeClass(config.selectedClass).prop("checked",false);
					
					_select.val(null);
					
				}
				
			});
			
			_select.on("change" + nameSpace, function(){
				initValue(_select.val());
			});
			
		}
		
		
		initArea();
		
		var op  = {
			container:_container,
			config:config,
			setValue:function(areaData){
				initValue(areaData);
			},
			getParents:function(){
				var allWrapper =  getAllLevelWrapper().not(":last");
				var parents = $();
				allWrapper.each(function(index, wrapper){
					var selectedItem = findAreaItem($(wrapper));
					parents = parents.add(selectedItem);
				});
				
				return parents;
			},
			getSelected:function(){
				var lastWrapper = getAllLevelWrapper().last();
				return findAreaItem(lastWrapper);
			},
			getValues:function(){
				var items = this.getSelected();
				var valueAry = [];
				items.each(function(index,item){
					valueAry.push($(item).data("areaData"));
				});
				return valueAry;
			},
			destroy:function(){
				_select.removeData("areaSelect-cacheOp");
				_select.off(config.nameSpace);
				_container.off(config.nameSpace);
			}
		}
		
		_select.data("areaSelect-cacheOp",op);
		
		return op;
	};
	
	
	//init resource
	var areaCss = shop.base +"/resources/lib/css/areaSelect.css?_tag=" + shop.initDate;
	util.loadCss(areaCss);
	
	
})(jQuery)