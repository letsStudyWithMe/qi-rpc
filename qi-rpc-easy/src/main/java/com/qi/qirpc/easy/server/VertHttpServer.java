package com.qi.qirpc.easy.server;

import io.vertx.core.Vertx;
public class VertHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {
        //创建Vert实例
        Vertx vertx = Vertx.vertx();

        //创建Http服务器
        io.vertx.core.http.HttpServer server = vertx.createHttpServer();

        server.requestHandler(new HttpServerHandler());

        server.listen(port,result ->{
           if (result.succeeded()){
               System.out.println("HTTP服务器启动成功，监听端口：" + port);
           }else {
               System.out.println("HTTP服务器启动失败：" + result.cause());
           }
        });
    }
}
