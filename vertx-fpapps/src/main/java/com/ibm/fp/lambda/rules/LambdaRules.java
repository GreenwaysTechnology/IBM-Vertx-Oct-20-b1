package com.ibm.fp.lambda.rules;

//functional interface
@FunctionalInterface
interface Greeter {
    static void doNothing() {
        System.out.println("static methods");
    }
    void greet(); //abstract method
//    void saySomething();
    // void sayHello();
    default void doSomething() {
        System.out.println("default methods");
    }
}

public class LambdaRules {
    public static void main(String[] args) {
        Greeter greeter = null;
        greeter = () -> {
            System.out.println("greet");
        };
        greeter.greet();
        greeter.doSomething();
        Greeter.doNothing();
    }
}
