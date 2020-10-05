package com.ibm.fp.interfaces;
//interface use cases

interface Greeter {
    void greet();
}
//how to use interface
class GreeterImpl implements Greeter {
    @Override
    public void greet() {
        System.out.println("Greet");
    }
}
class HelloImpl implements Greeter {
    @Override
    public void greet() {
        System.out.println("Hello");
    }
}

public class InterfaceMain {
    public static void main(String[] args) {
        Greeter greeter = null;
        //create instance for Greeterimpl
        greeter = new GreeterImpl();
        greeter.greet();
        greeter = new HelloImpl();
        greeter.greet();

    }
}
