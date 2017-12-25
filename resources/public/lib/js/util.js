/**
 * Created by OJH on 2017/12/20.
 * 工具类
 */

!function($){



    var util = {};

    /**
     * 加载脚本，注意代码加载的脚本是异步的
     */
    util.loadScript = function(url){
        var deferred = $.Deferred();
        var scriptDom = document.createElement("script");
        scriptDom.type = "text/javascript";
        scriptDom.src = url;
        scriptDom.onload = function(){
            deferred.resolve(scriptDom);
        };
        scriptDom.onerror = function(){
            deferred.reject(scriptDom);
        };
        var heads = document.getElementsByTagName("head");
        if(heads != null && heads.length > 0){
            heads[0].appendChild(scriptDom);
        }else{
            document.body.appendChild(scriptDom);
        }

        return deferred;
    };

    /**
     * 加载css文件
     */
    util.loadCss = function(url){
        var deferred = $.Deferred();
        var linkDom = document.createElement("link");
        linkDom.rel = "stylesheet";
        linkDom.href = url;
        linkDom.onload = function(){
            deferred.resolve(linkDom);
        };
        linkDom.onerror = function(){
            deferred.reject(linkDom);
        };
        var heads = document.getElementsByTagName("head");
        if(heads != null && heads.length > 0){
            heads[0].appendChild(linkDom);
        }else{
            document.body.appendChild(linkDom);
        }

        return deferred;
    };

    /**
     * 通用加载方法，自动识别文件后缀
     */
    util.load = function(){
        var jsPattern = /\.js$/;
        var cssPattern = /\.css$/;
        var deferreds = $.map(arguments, function(arg){
            if(typeof arg == "string"){
                if(jsPattern.test(arg)){
                    return util.loadScript(arg);
                }else if(cssPattern.test(arg)){
                    return util.loadCss(arg);
                }else{
                    //nothing 以后可以扩展其他资源
                }
            }
        });

        return $.when.apply($, deferreds);

    };

    /**
     * 转换表单为参数map
     */
    util.paramMap = function(formOrSelector, multipleIgnore){
        var param = {};
        var paramAry = $(formOrSelector).serializeArray();
        $.each(paramAry, function(index, valObj){
            if(param[valObj.name] == null){
                param[valObj.name] = valObj.value;
            }else{
                if(multipleIgnore){
                    param[valObj.name] = valObj.value;
                }else{
                    //自动以逗号分割
                    param[valObj.name] +="," + valObj.value;
                }
            }
        });

        return param;
    };

    
    /**
     * 
     *导入内容,只能在文档加载时输出
     *
     */
    util.include = function(url, noUseTemplate){
    	if(url == null){
    		return;
    	}
    	//基本数据
		var data = {
				basePath:window.basePath,
				base:window.basePath
			};
		
    	url = template.render(url, data);
    	
        $.ajax({
            url:url,
            async:false,
            success:function(content){
            	if(!noUseTemplate){
            		//默认使用用模版渲染
            		content = template.render(content, data);
            	}
            	
                document.writeln(content);                
            },
            error:function(xhr, textStatus, error){
                var errorMsg = "<div class='container text-danger'>"+url + "导入失败:" + error+"</div>";
                document.writeln(errorMsg);
            }
        })
    };
    

    try{
        //防止重复创建同名对象
        Object.defineProperty(window, "util", {
            set:function(){
                console.error("不允许重复创建util对象");
            },
            get:function(){
                return util;
            }
        });
    }catch(e){
        console.error("无法定义全局属性，使用默认操作");
        //导出约定的对象util - fallback
        window.util = util;
    }

}(jQuery);

