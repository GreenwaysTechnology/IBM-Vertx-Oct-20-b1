package com.ibm.fp.lambda.functionasarg;

//function as parameter.
//here handle is a function, which is passed as parameter to requestHandler of SocketHandler class

@FunctionalInterface
interface Handler {
    void handle();
}

@FunctionalInterface
interface HttpHandler<T> {
    void handle(T payload);
}

class SocketHandler {
    public void requestHandler(Handler handler) {
        handler.handle();
    }
}

class HttpServer {
    public void doService(HttpHandler<String> handler) {
        handler.handle("Hello Http!");
    }
}

//class Adder{
//    void add(int a int b){
//
//    }
//}
//Adder adder= new Adder();
//here pass values directly
//adder.add(10,10)
//int x=10;
//int y=20;
//pass values via variables
//adder.add(x,y);

public class FunctionAsParameter {
    public static void main(String[] args) {
        Handler handler = null;
        //handle function
        handler = () -> {
            System.out.println("Socket Handler function");
        };
        SocketHandler socketHandler = new SocketHandler();
        //way-1 ; pass function as parameter;
        socketHandler.requestHandler(handler);
        //way 2; pass function inline ;
        socketHandler.requestHandler(() -> System.out.println("Socket handler function"));
        ////////////////////////////////////////////////////////////////////////////////////////
        HttpHandler<String> httpHandler = null;
        HttpServer httpServer = new HttpServer();
        httpHandler = payload -> {
            System.out.println(payload);
        };
        httpServer.doService(httpHandler);
        httpServer.doService(response -> System.out.println(response));

    }
}
