package com.ibm.vertx.app.web.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

import java.util.List;
import java.util.stream.Collectors;

class WikiVerticle extends AbstractVerticle {
  //Sql Queries
  private static final String SQL_CREATE_PAGES_TABLE = "create table if not exists Pages (Id integer identity primary key, Name varchar(255) unique, Content clob)";
  private static final String SQL_GET_PAGE = "select Id, Content from Pages where Name = ?";
  private static final String SQL_CREATE_PAGE = "insert into Pages values (NULL, ?, ?)";
  private static final String SQL_SAVE_PAGE = "update Pages set Content = ? where Id = ?";
  private static final String SQL_ALL_PAGES = "select Name from Pages";
  private static final String SQL_DELETE_PAGE = "delete from Pages where Id = ?";

  //declarations
  private JDBCClient dbClient;

  //Template engine
  private FreeMarkerTemplateEngine templateEngine;

  public Future<Void> prepareDatabase() {
    Promise<Void> promise = Promise.promise();
    //initialize database
    dbClient = JDBCClient.createShared(vertx, new JsonObject()
      .put("url", "jdbc:hsqldb:file:db/wiki")
      .put("driver_class", "org.hsqldb.jdbcDriver")
      .put("max_pool_size", 30));

    //inital table creation.
    dbClient.getConnection(ar -> {
      if (ar.failed()) {
        System.out.println("Could not open a database connection" + ar.cause());
        promise.fail(ar.cause());
      } else {
        SQLConnection connection = ar.result();
        connection.execute(SQL_CREATE_PAGES_TABLE, create -> {
          connection.close();
          if (create.failed()) {
            System.out.println("Database preparation error" + create.cause());
            promise.fail(create.cause());
          } else {
            promise.complete();
          }
        });
      }
    });

    return promise.future();
  }

  private void indexHandler(RoutingContext context) {
    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        connection.query(SQL_ALL_PAGES, res -> {
          connection.close();

          if (res.succeeded()) {
            //rows
            List<String> pages = res.result() // <1>
              .getResults() //convert result into java 8 stream.
              .stream()
              .map(json -> json.getString(0))
              .sorted()
              .collect(Collectors.toList());

            //send context data to web page
            context.put("title", "IBM - Wiki home");
            //table rows are stored inside context object , so that you can where ever want
            context.put("pages", pages);
            templateEngine.render(context.data(), "templates/index.ftl", ar -> {
              if (ar.succeeded()) {
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());
              } else {
                context.fail(ar.cause());
              }
            });

          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }

  private Future<Void> startHttpServer() {
    Promise<Void> promise = Promise.promise();
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.get("/").handler(this::indexHandler);
    templateEngine = FreeMarkerTemplateEngine.create(vertx);
    server
      .requestHandler(router)
      .listen(8080, ar -> {
        if (ar.succeeded()) {
          System.out.println("HTTP server running on port 8080");
          promise.complete();
        } else {
          System.out.println("Could not start a HTTP server" + ar.cause());
          promise.fail(ar.cause());
        }
      });
    return promise.future();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    //tell this verticle start method is finalized whith onComplete
   // prepareDatabase().compose(v -> startHttpServer()).onComplete(startPromise);
    Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());
    steps.onComplete(startPromise);
  }
}

public class WikiApp extends AbstractVerticle {
  public static void main(String[] args) {
    Runner.runExample(WikiApp.class);
  }

  @Override
  public void start() throws Exception {
    super.start();
    vertx.deployVerticle(new WikiVerticle());
  }
}
