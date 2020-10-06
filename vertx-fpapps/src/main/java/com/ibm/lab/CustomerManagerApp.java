package com.ibm.lab;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

class Customer {
    private int id;
    private String name;

    public Customer() {

    }

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

class CustomerRepostiory {
    List<Customer> customerList;

    public CustomerRepostiory() {
        customerList = new ArrayList<>();
        customerList.add(new Customer(1, "a"));
        customerList.add(new Customer(2, "b"));
    }

    public void findAll(Consumer<List<Customer>> consumer) {
        consumer.accept(customerList);
    }

    public void save(Customer customer, Consumer<List<Customer>> consumer, Consumer<String> error) {
        if (customerList.add(customer)) {
            consumer.accept(customerList);
        } else {
            error.accept("Customer could not add");
        }
    }
}


public class CustomerManagerApp {
    public static void main(String[] args) {
        CustomerRepostiory customerRepostiory = new CustomerRepostiory();
        //customerRepostiory.findAll(response -> System.out.println(response));
        customerRepostiory.findAll(System.out::println);
        customerRepostiory.save(new Customer(3, "c"), res -> {
            System.out.println(res);
        }, err -> {
            System.out.println(err);
        });


    }

}
