package com.ibm.vertx.core.http;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.ArrayList;
import java.util.List;

class Employee {
  private int id;
  private String name;
  private String dept;
  private double salary;

  public Employee(int id, String name, String dept, double salary) {
    super();
    this.id = id;
    this.name = name;
    this.dept = dept;
    this.salary = salary;
  }

  public Employee() {
    super();
    // TODO Auto-generated constructor stub
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }

  public double getSalary() {
    return salary;
  }

  public void setSalary(double salary) {
    this.salary = salary;
  }

}

public class RestPost {
  public static void main(String[] args) {
    List<Employee> employees = new ArrayList<>();
    Vertx vertx = Vertx.vertx();
    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router
      .post("/addEmployee")
      .handler(routingContext -> {
        routingContext.getBodyAsJson();
        final Employee employee = Json.decodeValue(routingContext.getBody(), Employee.class);
        HttpServerResponse serverResponse = routingContext.response();
        serverResponse.setChunked(true);
        System.out.println(Json.encodePrettily(employee));
        employees.add(employee);
        serverResponse.end(employees.size() + " Employee added successfully...");
      });
    server.requestHandler(router).listen(9090);

  }
}
