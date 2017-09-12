/**
 * Created by OJH on 2017/9/12.
 */

var path = require("path");
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
            name:"common",
            filename:"common.js"
        }),
        new ExtractTextPlugin("style.css")
    ],
    externals:{
        jquery:"window.jQuery"
    }
};


module.exports = config;


