package com.ibm.vertx.microservices.communication.eventbus;


//PUB-SUB - ONE TO many


import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.example.util.Runner;
import io.vertx.ext.web.Router;


class Address {
  public static String PUB_SUB_ADDRESS = "news.in.covid";
  public static String POINT_TO_POINT = "covid.fin.request";
  public static String REQUEST_REPLY = "covid.lab.report";

}

class LabVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    //pub-sub
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.REQUEST_REPLY);
    //handle /process the message/news
    messageConsumer.handler(news -> {
      System.out.println("Request -  : " + news.body());
      //sending reply /ack
      news.reply("Patient is Crictal, Need More attention");
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}

class ReportVerticle extends AbstractVerticle {

  public void sendReport() {
    vertx.setTimer(5000, ar -> {
      String message = "Report of Mr.x";
      vertx.eventBus().request(Address.REQUEST_REPLY, message, asyncResult -> {
        if (asyncResult.succeeded()) {
          System.out.println(asyncResult.result().body());
        } else {
          System.out.println(asyncResult.cause());
        }
      });
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    sendReport();
  }
}

class CenertalFinanceVerticle extends AbstractVerticle {

  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.POINT_TO_POINT);
    //handle /process the message/news
    messageConsumer.handler(news -> {
      System.out.println("Request 1 -  : " + news.body());
    });
  }

  public void consume2() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.POINT_TO_POINT);
    //handle /process the message/news
    messageConsumer.handler(news -> {
      System.out.println("Request 2 -  : " + news.body());
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
    consume2();
  }
}


//point to point publisher
class FinanceRequestVerticle extends AbstractVerticle {

  public void requestFinance() {
    System.out.println("Finance Request started....");
    vertx.setTimer(5000, ar -> {
      //point to point : send method
      String message = "Dear Team, We request that we want 1 Billion Money for Covid";
      //point to point ; send
      vertx.eventBus().send(Address.POINT_TO_POINT, message);
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    requestFinance();
  }
}


/////////////////////////////
//pub-sub ; one provider many consumer
class NewsSevenVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    messageConsumer.handler(news -> {
      System.out.println(this.getClass().getName() + news.body());
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}

class BBCVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    messageConsumer.handler(news -> {
      System.out.println(this.getClass().getName() + news.body());
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}

class NDTVVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    messageConsumer.handler(news -> {
      System.out.println(this.getClass().getName() + news.body());
    });
  }

  @Override
  public void start() throws Exception {
    super.start();
    consume();
  }
}

//publisher publish news to all news tv channels
class PublisherVerticle extends AbstractVerticle {

  public void publish() {
    //one to many
    vertx.setTimer(1000, handler -> {
      String message = "Last 24 Hrs covid count is 80000";
      EventBus eventBus = vertx.eventBus();
      eventBus.publish(Address.PUB_SUB_ADDRESS, message);
    });
  }

  public void publishviaHttp() {
    HttpServer httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.get("/api/notification").handler(routingContext -> {
      String message = "Last 24 Hrs covid count is 80000";
      EventBus eventBus = vertx.eventBus();
      eventBus.publish(Address.PUB_SUB_ADDRESS, message);
      routingContext.response().end("Published");
    });
    httpServer.requestHandler(router);
    httpServer.listen(9000, httpServerAsyncResult -> {
      System.out.println("Notification Server " + httpServerAsyncResult.result().actualPort());
    });

  }

  @Override
  public void start() throws Exception {
    super.start();
    // publish();
    publishviaHttp();
  }
}

public class EventBusMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(EventBusMainVerticle.class);
  }

  public void pubsub() {
    vertx.deployVerticle(new PublisherVerticle());
    vertx.deployVerticle(new NDTVVerticle());
    vertx.deployVerticle(new BBCVerticle());
    vertx.deployVerticle(new NewsSevenVerticle());
  }

  public void pointToPoint() {
    vertx.deployVerticle(new CenertalFinanceVerticle());
    vertx.deployVerticle(new FinanceRequestVerticle());
  }

  public void requestReply() {
    vertx.deployVerticle(new ReportVerticle());
    vertx.deployVerticle(new LabVerticle());
  }

  @Override
  public void start() throws Exception {
    super.start();
    // pubsub();
    //pointToPoint();
    requestReply();
  }
}
