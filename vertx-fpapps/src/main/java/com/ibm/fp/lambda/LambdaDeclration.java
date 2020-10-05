package com.ibm.fp.lambda;

//functional interface
interface Greeter {
    void greet(); //abstract method
}

public class LambdaDeclration {
    public static void main(String[] args) {
        Greeter greeter = null;
        greeter = () -> {
            System.out.println("Greet!!!");
        };
        greeter.greet();
        greeter = () -> {
            System.out.println("Hello!!!");
        };
        greeter.greet();
        greeter = () -> {
            System.out.println("Hai!!!");
        };
        greeter.greet();
    }
}
