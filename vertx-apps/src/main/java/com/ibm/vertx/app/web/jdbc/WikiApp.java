package com.ibm.vertx.app.web.jdbc;

import com.github.rjeschke.txtmark.Processor;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.example.util.Runner;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.templ.freemarker.FreeMarkerTemplateEngine;

import java.util.Date;
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
  private static final String EMPTY_PAGE_MARKDOWN =
    "# A new page\n" +
      "\n" +
      "Feel-free to write in Markdown!\n";
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

  private void pageCreateHandler(RoutingContext context) {
    String pageName = context.request().getParam("name");
    String location = "/wiki/" + pageName;
    if (pageName == null || pageName.isEmpty()) {
      location = "/";
    }
    context.response().setStatusCode(303);
    context.response().putHeader("Location", location);
    context.response().end();
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
            //table rows are stored inside context object , so that you can access where ever want
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

  private void pageUpdateHandler(RoutingContext context) {
    String id = context.request().getParam("id");   // <1>
    String title = context.request().getParam("title");
    String markdown = context.request().getParam("markdown");
    boolean newPage = "yes".equals(context.request().getParam("newPage"));  // <2>

    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        String sql = newPage ? SQL_CREATE_PAGE : SQL_SAVE_PAGE;
        JsonArray params = new JsonArray();   // <3>
        if (newPage) {
          params.add(title).add(markdown);
        } else {
          params.add(markdown).add(id);
        }
        connection.updateWithParams(sql, params, res -> {   // <4>
          connection.close();
          if (res.succeeded()) {
            context.response().setStatusCode(303);    // <5>
            context.response().putHeader("Location", "/wiki/" + title);
            context.response().end();
          } else {
            context.fail(res.cause());
          }
        });
      } else {
        context.fail(car.cause());
      }
    });
  }

  private void pageRenderingHandler(RoutingContext context) {
    String page = context.request().getParam("page");   // <1>

    dbClient.getConnection(car -> {
      if (car.succeeded()) {

        SQLConnection connection = car.result();
        connection.queryWithParams(SQL_GET_PAGE, new JsonArray().add(page), fetch -> {  // <2>
          connection.close();
          if (fetch.succeeded()) {

            JsonArray row = fetch.result().getResults()
              .stream()
              .findFirst()
              .orElseGet(() -> new JsonArray().add(-1).add(EMPTY_PAGE_MARKDOWN));
            Integer id = row.getInteger(0);
            String rawContent = row.getString(1);

            context.put("title", page);
            context.put("id", id);
            context.put("newPage", fetch.result().getResults().size() == 0 ? "yes" : "no");
            context.put("rawContent", rawContent);
            context.put("content", Processor.process(rawContent));  // <3>
            context.put("timestamp", new Date().toString());

            templateEngine.render(context.data(), "templates/page.ftl", ar -> {
              if (ar.succeeded()) {
                context.response().putHeader("Content-Type", "text/html");
                context.response().end(ar.result());
              } else {
                context.fail(ar.cause());
              }
            });
          } else {
            context.fail(fetch.cause());
          }
        });

      } else {
        context.fail(car.cause());
      }
    });
  }

  private void pageDeletionHandler(RoutingContext context) {
    String id = context.request().getParam("id");
    dbClient.getConnection(car -> {
      if (car.succeeded()) {
        SQLConnection connection = car.result();
        connection.updateWithParams(SQL_DELETE_PAGE, new JsonArray().add(id), res -> {
          connection.close();
          if (res.succeeded()) {
            context.response().setStatusCode(303);
            context.response().putHeader("Location", "/");
            context.response().end();
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
    router.get("/wiki/:page").handler(this::pageRenderingHandler); // <3>
    router.post().handler(BodyHandler.create());  // <4>
    router.post("/save").handler(this::pageUpdateHandler);
    router.post("/create").handler(this::pageCreateHandler);
    router.post("/delete").handler(this::pageDeletionHandler);


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
