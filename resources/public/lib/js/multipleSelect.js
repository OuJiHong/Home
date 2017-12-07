/**
 * 多选
 * 
 */

(function($){
	
	var cacheData = {
		_index:1,
		nextIndex:function(){
			this._index++;
			return this._index;
		}
	};
	
	//检测位置
	function hitTest(hitDom, target){
		var hitOffset = hitDom.offset();
		var targetOffset = target.offset();
		var range = {
				beforeX:hitOffset.left,
				beforeY:hitOffset.top,
				afterX:hitOffset.left + hitDom.innerWidth(),
				afterY:hitOffset.top + hitDom.innerHeight()
		};
		var targetRange = {
				beforeX:targetOffset.left,
				beforeY:targetOffset.top,
				afterX:targetOffset.left + target.innerWidth(),
				afterY:targetOffset.top + target.innerHeight()
		};
		
		if(range.beforeX >= targetRange.beforeX && range.beforeX >= targetRange.afterX){
			return false;
		}
		if(range.beforeX <= targetRange.beforeX && range.afterX <= targetRange.beforeX){
			return false;
		}
		if(range.beforeY >= targetRange.beforeY && range.beforeY >= targetRange.afterY){
			return false;
		}
		if(range.beforeY <= targetRange.beforeY && range.afterY <= targetRange.beforeY){
			return false;
		}
		
		return true;
	}
	
	
	$.fn.multipleSelect = function(options){
		var _select = this;
		var cacheOp = _select.data("multipleSelect-cacheOp");
		if(cacheOp != null){
			return cacheOp;
		}
		
		var config = {
			searchAble:true,
			dragSelect:true,
			url:null,
			param:{pageSize:"T{pageSize}", pageNumber:"T{pageNumber}",searchProperty:"name",searchValue:"T{searchValue}"},
			requestType:"get",
			data:null,
			css:{height:240,width:600},
			originalIdent:"original",
			resultIdent:"result",
			searchIdent:"search",
			operationIdent:"operation",
			itemWrapperIdent:"item-wrapper",
			itemIdent:"item",
			rangeClass:"multipleSelect-range",
			selectedClass:"selected",
			formatValue:function(item){
				if(item != null && typeof item == "object"){
					return $.trim(item.id);
				}
				return item;
			},
			formatText:function(item){
				if(item != null && typeof item == "object"){
					return item.name;
				}
				return item;
			},
			addText:"添加&gt;&gt;",
			removeText:"&lt;&lt;移除",
			onAdd:null,
			onRemove:null,
			nameSpace:".multipleSelect"
		};
		
		$.extend(config,options);
		
		var selectTemplate = {
				container:"<div class='multiple-select'></div>",
				original:"<div class='multiple-select-original'></div>",
				result:"<div class='multiple-select-result'></div>",
				itemWrapper:"<div class='multiple-select-itemWrapper'></div>",
				item:"<div class='multiple-select-item'></div>",
				search:"<div class='multiple-select-search'></div>",
				searchInput:"<input type='text' class='multiple-select-searchInput' placeholder='搜索...'/>",
				operation:"<div class='multiple-select-operation'></div>",
				operationTool:"<span class='multiple-select-operationTool clearfix'></span>",
				selectRange:"<div class='multiple-drag-select'></div>",
				loadingTip:"<div class='multiple-loading'>正在加载数据...</div>"
		};
		
		var nameSpace = config.nameSpace;
		var _container = $(selectTemplate.container);
		var containerId = "mulSelect_" + _select.attr("id") + cacheData.nextIndex();
		_container.attr("id",containerId);
		if(config.css != null){
			_container.css(config.css);
		}
		_select.attr("multiple","multiple");
		_select.after(_container);
		_select.hide();
		_select.addClass("multiple-select-hidden");
		
		//init value
		var initOptions = _select.find("option[selected]");//初始化
		var existValues = [];
		initOptions.each(function(index, option){
			existValues.push(option.value);
		});
		_select.val(existValues);
		
		
		var paginationObj = {
				pageable:false,
				pageNumber:1,
				pageSize:100,
				totalPages:null,
				searchValue:"",
				hasNextPage:function(){
					if(this.totalPages != null){
						return this.pageNumber < this.totalPages;
					}
					
					return true;
				}
		};
		
		function chNum(numStr){
			var num = parseFloat(numStr.replace("px",""));
			if(isNaN(num)){
				return 0;
			}
			return num;
		}
		
		
		function getOriginal(){
			return _container.find("[" + config.originalIdent + "]");
		}
		
		function getResult(){
			return _container.find("["+config.resultIdent+"]");
		}
		
		/**
		 * 获取列
		 */
		function getItems(category){
			if(category != null){
				return category.find("[" + config.itemIdent + "]")
			}
			return $();
		}
		
		
		/**
		 * 获取选中的列
		 */
		function getSelected(category){
			var selectedList = getItems(category).filter("." + config.selectedClass);
			return selectedList;
		}
		
		/**
		 * 渲染数据
		 * 
		 */
		function renderWrapper(dataList){
			var $original = getOriginal();
			var $result = getResult();
			var originalItemWrapper = $original.find("["+config.itemWrapperIdent+"]");
			var resultItemWrapper = $result.find("["+config.itemWrapperIdent+"]");
			if(dataList != null){
				$.each(dataList,function(index, data){
					var $item = $(selectTemplate.item);
					var realValue = config.formatValue(data);
					var text = config.formatText(data);
					$item.attr(config.itemIdent,realValue);
					$item.data("value",data);
					$item.html(text);
					originalItemWrapper.append($item);
				});
			}
			
			//init result
			var resultList = getItems($result);
			resultList.remove();
			
			//排除已有数据
			var selectedVal = _select.val();
			var itemList = getItems($original);
			var invalidList = $();
			if(selectedVal != null){
				itemList.each(function(index, item){
					var $item = $(item);
					var realVal = config.formatValue($item.data("value"));
					if($.inArray(realVal, selectedVal) != -1){
						invalidList = invalidList.add($item);
					}
				});
				
				invalidList.remove();
				
				var selectedOptions = _select.find("option:selected");
				selectedOptions.each(function(index, option){
					var $option = $(option);
					var data = $option.data("value");
					var $item = $(selectTemplate.item);
					var realValue = config.formatValue(data);
					var text = config.formatText(data);
					$item.attr(config.itemIdent,realValue);
					$item.data("value",data);
					$item.html(text);
					resultItemWrapper.append($item);
				});
				
				//clear
				_select.find("option").not(":selected").remove();
				
			}else{
				_select.empty();
			}
			
			
		}
		
		
		function initOriginal(){
			var original = $(selectTemplate.original);
			original.addClass(config.rangeClass);
			original.attr(config.originalIdent, containerId);
			var itemWrapper = $(selectTemplate.itemWrapper);
			itemWrapper.attr(config.itemWrapperIdent,containerId);
			original.append(itemWrapper);
			_container.append(original);
			
			var remoteUrl = config.url;
			if(remoteUrl != null && remoteUrl != ""){
				paginationObj.pageable = true;
				itemWrapper.on("scroll",function(evt, searchValue){
					var range = 4;//误差值
					var $wrapper = $(this);
					var beforeXhr = $wrapper.data("cache-executeXhr");
					if(beforeXhr != null){
						//wait
						return false;
					}
					
					if(searchValue != null){
						paginationObj.pageNumber = 1;
						paginationObj.searchValue = searchValue;
						var itemList = getItems(original);
						itemList.remove();//clear
					}else{
						//auto assert
						var scrollTop = $wrapper.scrollTop();
						var maxScroll = $wrapper.prop("scrollHeight") - $wrapper.innerHeight();
						if(scrollTop < (maxScroll - range)){
							return false;
						}
						
						if(!paginationObj.hasNextPage()){
							return false;
						}
						
						//下一页
						paginationObj.pageNumber++;
					}
					
					//loading 
					var requestData = $.extend({}, config.param);
					var tPattern = /T\{\s*(\w+)\s*\}/; 
					for(var key in requestData){
						var val = requestData[key];
						if(val != null && typeof val == "string" && tPattern.test(val) != null){
							val = val.replace(tPattern,function(p0,p1){
								return paginationObj[p1] == null ?  "" : paginationObj[p1];
							});
							requestData[key] = val;
						}
					}
					
					var executeXhr = $.ajax({
						type:config.requestType,
						url:remoteUrl,
						data:requestData,
						dataType:"json",
						beforeSend:function(){
							$wrapper.append(selectTemplate.loadingTip);
							var maxScrollTop = $wrapper.prop("scrollHeight") - $wrapper.innerHeight();
							$wrapper.animate({scrollTop:maxScrollTop});
						},
						success:function(page){
							$wrapper.find(".multiple-loading").remove();
							
							if(page.pageNumber != null && $.isNumeric(page.pageNumber)){
								paginationObj.pageNumber = parseInt(page.pageNumber);
							}
							if(page.pageSize != null && $.isNumeric(page.pageSize)){
								paginationObj.pageSize = parseInt(page.pageSize);
							}
							if(page.totalPages != null && $.isNumeric(page.totalPages)){
								paginationObj.totalPages = parseInt(page.totalPages);
							}
							
							renderWrapper(page.content);
						},
						error:function(xhr,textStatus,e){
							$.message("select loading error");
						},
						complete:function(){
							//固定写法
							$wrapper.find(".multiple-loading").remove();
							$wrapper.removeData("cache-executeXhr");
						}
					});
					
					$wrapper.data("cache-executeXhr", executeXhr);
					
				});
				
			}
			
			//是否存在搜索框
			if(config.searchAble){
				var search = $(selectTemplate.search);
				search.attr(config.searchIdent, containerId);
				var searchInput = $(selectTemplate.searchInput);
				var searchTimeout = null;
				searchInput.on("change keyup",function(evt){
					var $this = $(this);
					var val = $this.val();
					var itemList = getItems(original);
					
					//远程搜索
					if(paginationObj.pageable){
						clearTimeout(searchTimeout);
						searchTimeout = setTimeout(function(){
							var searchValue = $.trim(searchInput.val());
							//ignore same
							if(paginationObj.searchValue == searchValue){
								return false;
							}
							executeSearch(searchValue);
						},400);
						
					}else{
						if($.trim(val) == ""){
							itemList.removeClass("no-match");
							return false;
						}
						
						itemList.each(function(index, item){
							var $item = $(item);
							var data = $item.data("value");
							var curVal = config.formatText(data);
							if(curVal != null && curVal.toLowerCase().indexOf(val.toLowerCase()) != -1 ){
								$item.removeClass("no-match");
							}else{
								$item.addClass("no-match");
							}
						});
					}
					
				});
				
				search.append(searchInput);
				original.prepend(search);
				
			}
			
			
			
		}
		
		/**
		 * 初始化操作
		 * 
		 */
		function initOperation(){
			var operation = $(selectTemplate.operation);
			operation.attr(config.operationIdent,containerId);
			var operationTool = $(selectTemplate.operationTool);
			var addBtn = $("<a href='javascript:;' class='button'>"+config.addText+"</a>");
			var removeBtn = $("<a href='javascript:;' class='button' >"+config.removeText+"</a>");
			
			addBtn.on("mousedown",function(evt){
				evt.stopPropagation();
			}).on("click",function(){
				var original = getOriginal();
				var itemList = getSelected(original);
				if(itemList.length > 0){
					addValue(itemList);
					config.onAdd && config.onAdd(itemList);
				}
				
			});
		
			
			removeBtn.on("mousedown",function(evt){
				evt.stopPropagation();
			}).on("click",function(){
				var result = getResult();
				var itemList = getSelected(result);
				if(itemList.length > 0){
					removeValue(itemList);
					config.onRemove && config.onRemove(itemList);
				}
			});
			
			
			
			//按钮灰显控制
			_container.on("selRangeChange","["+config.originalIdent+"]",function(){
				var original = getOriginal();
				var itemList = getSelected(original);
				if(itemList.length > 0 ){
					addBtn.removeAttr("disabled");
				}else{
					addBtn.attr("disabled","disabled");
				}
			});
			
			_container.on("selRangeChange","["+config.resultIdent+"]",function(){
				var result = getResult();
				var itemList = getSelected(result);
				if(itemList.length > 0 ){
					removeBtn.removeAttr("disabled");
				}else{
					removeBtn.attr("disabled","disabled");
				}
			});

			operationTool.append(addBtn);
			operationTool.append(removeBtn);
			operation.append(operationTool);
			
			_container.append(operation);
		}
		
		/**
		 * 设置值
		 */
		function setValue(values, isRestore){
			var valueAry = [];
			if(values != null){
				_select.find("option").each(function(index, option){
					var $option = $(option);
					var data = $option.data("value");
					var realValue = config.formatValue(data);
					if($.inArray(realValue, values) != -1){
						valueAry.push(data);
					}
				});
			}
			
			if(isRestore){
				removeValue();
			}
			_select.empty();
			addValue(valueAry);
			
		}
		
		/**
		 * 添加值,约定{name:xx,value:xx}
		 * 
		 */
		function addValue(valueAry){
			var result = getResult();
			var original = getOriginal();
			if(valueAry != null){
				var selectedVal = _select.val() || [];
				$.each(valueAry,function(index,obj){
					var value = null;
					var text = null;
					if($.isPlainObject(obj)){
						value = config.formatValue(obj);
						text = config.formatText(obj);
					}else{
						var $item = $(obj);
						var data = $item.data("value");
						value = config.formatValue(data);
						text = config.formatText(data);
						obj = data;//获取实际数据
					}
					
					var $option = $("<option value='"+value+"' >"+text+"</option>");
					$option.data("value", obj);
					_select.append($option);
					selectedVal.push(value);
				});
				
				_select.val(selectedVal);
				renderWrapper();
			}
			
			getOriginal().trigger("selRangeChange");
			getResult().trigger("selRangeChange");
			
		}
		
		/**
		 * 移除值
		 */
		function removeValue(valueAry){
			var result = getResult();
			var original = getOriginal();
			var selectedVal = _select.val();
			
			if(valueAry != null && selectedVal != null){
				var restoreAry = [];
				$.each(valueAry,function(index,obj){
					var value = null;
					var text = null;
					if($.isPlainObject(obj)){
						value = config.formatValue(obj);
						text = config.formatText(obj);
					}else{
						var $item = $(obj);
						var data = $item.data("value");
						value = config.formatValue(data);
						text = config.formatText(data);
						obj = data;//获取实际数据
					}
					
					restoreAry.push(obj);
					
					var findIndex = $.inArray(value, selectedVal);
					if(findIndex != -1){
						selectedVal.splice(findIndex, 1);
					}
				});
				
				_select.val(selectedVal);
				renderWrapper(restoreAry);
				
				if(paginationObj.pageable){
					executeSearch();
				}
				
			}else{
				//清除所有
				var restoreAry = [];
				_select.val(null);
				_select.find("option:selected").each(function(index, option){
					var $option = $(option);
					var data = $option.data("value");
					if(data != null){
						restoreAry.push(data);
					}
				});
				_select.val([]);
				renderWrapper(restoreAry);
				
			}
			
			getOriginal().trigger("selRangeChange");
			getResult().trigger("selRangeChange");
		}
		
		/**
		 * 初始化
		 */
		function initResult(){
			var result = $(selectTemplate.result);
			result.addClass(config.rangeClass);
			result.attr(config.resultIdent,containerId);
			var itemWrapper = $(selectTemplate.itemWrapper);
			itemWrapper.attr(config.itemWrapperIdent,containerId);
			result.append(itemWrapper);
			_container.append(result);
			
			//是否存在搜索框
			if(config.searchAble){
				var search = $(selectTemplate.search);
				search.attr(config.searchIdent, containerId);
				var searchInput = $(selectTemplate.searchInput);
				searchInput.on("change keyup",function(evt){
					var $this = $(this);
					var val = $this.val();
					var itemList = getItems(result);
					if($.trim(val) == ""){
						itemList.removeClass("no-match");
						return false;
					}
					itemList.each(function(index, item){
						var $item = $(item);
						var data = $item.data("value");
						var curVal = config.formatText(data);
						if(curVal != null && curVal.toLowerCase().indexOf(val.toLowerCase()) != -1 ){
							$item.removeClass("no-match");
						}else{
							$item.addClass("no-match");
						}
					});
					
				});
				
				search.append(searchInput);
				result.prepend(search);
			}
			
			//数据同步
			_select.on("change",function(evt){
				var values = $(this).val();
				setValue(values, true);
			});
			
		}
		
		
		function executeSearch(value){
			var original = getOriginal();
			var originalItemWrapper = original.find("["+config.itemWrapperIdent+"]");
			if(value == null){
				value = original.find("["+config.searchIdent+"]>input").val();
			}
			originalItemWrapper.triggerHandler("scroll", [value]);
		}
		
		
		/**
		 * 初始化数据
		 * 
		 */
		function initData(){
			var optionList = _select.prop("options");
			var dataList = [];
			if(optionList != null){
				$.each(optionList,function(index, op){
					var $op = $(op);
					var realValue = $op.prop("value");
					var data = {id:realValue, name:$op.text()};
					$op.data("value", data);
					dataList.push(data);
				});
			}
			
			if(config.data != null){
				dataList = dataList.concat(config.data);
			}
			
			renderWrapper(dataList);
			
			if(paginationObj.pageable){
				executeSearch();
			}
			
		}
		
		
		//init
		initOriginal();
		initOperation();
		initResult();
		initData();
		getOriginal().trigger("selRangeChange");
		getResult().trigger("selRangeChange");
		
		//可拖拽选择
		_container.on("mousedown" + nameSpace,"["+config.itemIdent+"]", function(evt){
			evt.stopPropagation();
			evt.stopImmediatePropagation();//特殊处理
		}).on("click" + nameSpace ,"["+config.itemIdent+"]",function(evt){
			var $this = $(this);
			if($this.hasClass(config.selectedClass)){
				$this.removeClass(config.selectedClass);
			}else{
				$this.addClass(config.selectedClass);
			}
			
			getOriginal().trigger("selRangeChange");
			getResult().trigger("selRangeChange");
		});
		
		if(config.dragSelect){
			var $body = $("body");
			var $selectRange = null;
			$selectRange = $(selectTemplate.selectRange);
			$selectRange.css({position:"absolute",display:"none"});
			$body.append($selectRange);
			_container.on("mousedown" + nameSpace, function(evt){
				var $this = $(this);
				$selectRange.show();
				$selectRange.css({width:0,height:0,left:evt.pageX,top:evt.pageY});
				var oldPoint = {x:evt.pageX,y:evt.pageY};
				
				var $itemList = _container.find("["+config.itemIdent+"]");
				$body.off("mousemove" + nameSpace).on("mousemove" + nameSpace,function(evt){
					evt.preventDefault();
					evt.stopPropagation();
					var offsetX = evt.pageX - oldPoint.x;
					var offsetY = evt.pageY - oldPoint.y;
					var curX = evt.pageX;
					var curY = evt.pageY;
					if(offsetX > 0){
						curX = oldPoint.x;
					}
					if(offsetY > 0){
						curY = oldPoint.y;
					}
					$selectRange.css({width:Math.abs(offsetX),height:Math.abs(offsetY),left:curX,top:curY});
					$itemList.each(function(index, item){
						var $item = $(item);
						if(hitTest($selectRange,$item)){
							$item.addClass(config.selectedClass);
						}else{
							$item.removeClass(config.selectedClass);
						}
					});
					
				});
				
				$body.off("mouseup" + nameSpace).on("mouseup" + nameSpace, function(){
					$selectRange.hide();
					$(this).off("mousemove" + nameSpace);
					getOriginal().trigger("selRangeChange");
					getResult().trigger("selRangeChange");
				});
				
			});
		}
		
		//update css
		$(window).resize(function(){
			var original = getOriginal();
			var result = getResult();
			var operation = _container.find("["+config.operationIdent+"]");
			var baseWidth = _container.width();
			var originalItemWrapper = original.find("["+config.itemWrapperIdent+"]");
			var originalSearch = original.find("["+config.searchIdent+"]");
			originalItemWrapper.css("height",_container.height() - originalSearch.outerHeight());
			
			var resultItemWrapper = result.find("["+config.itemWrapperIdent+"]");
			var resultSearch = result.find("["+config.searchIdent+"]");
			resultItemWrapper.css("height",_container.height() - resultSearch.outerHeight());
			
			original.outerWidth(baseWidth * 0.4);
			result.outerWidth(baseWidth * 0.4);
			operation.outerWidth(baseWidth * 0.2);
		}).resize();
		
		
		var op = {
			config:config,
			selectDom:_select,
			container:_container,
			setValue:setValue,
			addValue:addValue,
			removeValue:removeValue,
			getValueDom:function(){
				var result = getResult();
				return getItems(result);
			},
			getValue:function(){
				var values = [];
				var itemList = this.getValueDom();
				itemList.each(function(index, item){
					var $item = $(item);
					values.push($item.data("value"));
				});
				
				return values;
			},
			destory:function(){
				this.container.remove();
				this.selectDom.removeData("multipleSelect-cacheOp");
				this.selectDom.show();
				$("body").off(this.config.nameSpace);
			}
		}
		
		_select.data("multipleSelect-cacheOp",op);
		return op;
		
	};
	
	
	
	//init resource
	var multipleSelectCss = shop.base +"/resources/lib/css/multipleSelect.css?_tag="+shop.initDate;
	util.loadCss(multipleSelectCss).done(function(){
		$(window).resize();
	});
	
})(jQuery)