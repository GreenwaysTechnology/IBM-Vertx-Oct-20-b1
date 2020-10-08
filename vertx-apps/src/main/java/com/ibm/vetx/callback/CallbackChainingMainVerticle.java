package com.ibm.vetx.callback;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.example.util.Runner;

class CallbackVerticle extends AbstractVerticle {
  //callback nesting
  public Future<String> getUser() {
    Promise<String> promise = Promise.promise();
    String userName = "admin";
    //biz logic
    if (userName != null) {
      promise.complete(userName);
    } else {
      promise.fail("User not found");
    }

    return promise.future();
  }

  public Future<String> login(String userName) {
    Promise<String> promise = Promise.promise();
    //biz logic
    String status = "login Success";
    String error = "login failed";
    if (userName.equals("admin")) {
      promise.complete(status);
    } else {
      promise.fail(error);
    }

    return promise.future();
  }

  public Future<String> showPage(String status) {
    Promise promise = Promise.promise();
    if (status.equals("login Success")) {
      //output
      promise.complete("Welcome to Admin page");
    } else {
      promise.fail(new RuntimeException("You are inside Guest page"));
    }
    return promise.future();
  }

  @Override
  public void start() throws Exception {
    super.start();
    getUser().onComplete(asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println("Get user is called");
        //nesting
        login(asyncResult.result()).onComplete(loginres -> {
          if (loginres.succeeded()) {
            System.out.println("login is called");
            showPage(loginres.result()).onComplete(pageres -> {
              if (pageres.succeeded()) {
                System.out.println("Page is called");
                System.out.println(pageres.result());
              } else {
                System.out.println(pageres.cause());
              }
            });
          } else {
            System.out.println(loginres.cause());
          }
        });

      } else {
        System.out.println(asyncResult.cause());
      }
    });
  }
}

public class CallbackChainingMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(CallbackChainingMainVerticle.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    //verticle deployment itself is async; every verticle once deployed, it returns a number, deploymentId
    vertx.deployVerticle(new CallbackVerticle(), asyncResult -> {
      if (asyncResult.succeeded()) {
        System.out.println("Deployment Id : " + asyncResult.result());
      } else {
        System.out.println(asyncResult.cause());
      }
    });
  }
}
