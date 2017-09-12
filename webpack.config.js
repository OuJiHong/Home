/**
 * Created by OJH on 2017/9/12.
 */

var path = require("path");
var glob = require("glob");

var publichPath = "";
var devtool = "eval-source-map";

if(process.env.NODE_ENV == "production"){
    //生产配置

}

var CleanWebpackPlugin = require("clean-webpack-plugin");
var HtmlWebpackPlugin = require("html-webpack-plugin");
var ExtractTextPlugin = require("extract-text-webpack-plugin");

var webpack = require("webpack");


var config = {
    context:path.join(__dirname , "/"),
    entry:{
        index:"./src/index.js"
    },
    output:{
        path:path.join(__dirname , "/dist") ,
        filename:"[name].js",
        publicPath:publichPath
    },
    devtool:devtool,
    module:{
        rules:[
            {
                test:/\.css$/,
                use: ExtractTextPlugin.extract({
                    fallback: "style-loader",
                    use: "css-loader"
                })
            },{
                test:/\.html$/,
                loader:"html-loader"
            },{
                test:/\.(png|jpg|jpeg|bmp|svg)$/,
                use:[
                    {
                        loader:"file-loader",
                        options:{
                            name:"asset/img/[name]-[hash].[ext]"
                        }
                    }
                ]
            },{
                test:/\.(eot|ttf|woff|woff2)$/,
                use:[
                    {
                        loader:"file-loader",
                        options:{
                            name:"asset/font/[name].[ext]"
                        }
                    }
                ]
            }
        ]
    },
    resolve:{
        alias:{}
    },
    plugins:[
      new CleanWebpackPlugin(["*"],{
            root:path.join(__dirname, "dist"),
            verbose:true,
            dry:false
      }),
        new HtmlWebpackPlugin({
            template:"src/index.html"
        }),
        new webpack.optimize.CommonsChunkPlugin({
            name:"vendor",
            minChunks:function(module){
                return module.context && module.context.indexOf("node_modules") !== -1;
            }
        }),
        new ExtractTextPlugin("style.css")
    ],
    externals:{
        jquery:"window.jQuery"
    }
};


//配置页面提取插件
var pages = Object.keys(getEntry('src/views/**/*.html', 'src/views/'));
pages.forEach(function(pathname) {
    var conf = {
        filename: '../views/' + pathname + '.html', //生成的html存放路径，相对于path
        template: 'src/views/' + pathname + '.html', //html模板路径
        inject: false,  //js插入的位置，true/'head'/'body'/false
    };
    if (pathname in config.entry) {
        conf.favicon = 'src/image/favicon.ico';
        conf.inject = 'body';
        conf.chunks = ['vendors', pathname];
        conf.hash = true;
    }
    config.plugins.push(new HtmlWebpackPlugin(conf));
});

/**
 * 获取文件信息
 * @param globPath
 * @param pathDir
 * @returns {{}}
 */
function getEntry (globPath, pathDir) {
    var files = glob.sync(globPath)
    var entries = {}, entry, dirname, basename, pathname, extname

    for (var i = 0; i < files.length; i++) {
        entry = files[i]
        dirname = path.dirname(entry)
        extname = path.extname(entry)
        basename = path.basename(entry, extname)
        pathname = path.normalize(path.join(dirname, basename))
        pathDir = path.normalize(pathDir)
        if (pathname.startsWith(pathDir)) {
            pathname = pathname.substring(pathDir.length)
        }
        entries[pathname] = ['./' + entry]
    }
    return entries
}


module.exports = config;


