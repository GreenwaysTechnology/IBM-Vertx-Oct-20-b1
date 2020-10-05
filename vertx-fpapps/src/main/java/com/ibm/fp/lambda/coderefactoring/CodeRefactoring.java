package com.ibm.fp.lambda.coderefactoring;

@FunctionalInterface
interface Greeter {
    void greet();
}

//parameters and args
@FunctionalInterface
interface Hello {
    //name is args
    void sayHello(String name);
}

//multi parameter
@FunctionalInterface
interface Calculator {
    void add(int a, int b);
}

//return values
@FunctionalInterface
interface Stock {
    int getStockValue();
}

//args,return value
@FunctionalInterface
interface Adder {
    int add(int a, int b);
}

public class CodeRefactoring {
    public static void main(String[] args) {
        Greeter greeter = null;
        greeter = () -> {
            System.out.println("Greet");
        };
        greeter.greet();
        //refactoring -1 : if body has only one line of code. we can remove{}
        greeter = () -> System.out.println("Greet");
        greeter.greet();
        //////////////////////////////////////////////////////////////////////////////
        Hello hello = null;
        hello = (String myName) -> System.out.println(myName);
        //parameters
        hello.sayHello("Subramanian");
        //code refactoring-2: parameter type can be removed: type inference
        hello = (myName) -> System.out.println(myName);
        //parameters
        hello.sayHello("Subramanian");
        //code refactoring-3: if single parameter with type interference,remove ()
        hello = myName -> System.out.println(myName);
        //parameters
        hello.sayHello("Subramanian");
        /////////////////////////////////////////////////////////////////////////////////////////
        Calculator calculator = null;
        calculator = (int a, int b) -> {
            int c = a + b;
            System.out.println("Calc Result " + c);
        };
        calculator.add(10, 60);
        //refactoring-4; type inference ;
        calculator = (a, b) -> {
            int c = a + b;
            System.out.println("Calc Result " + c);
        };
        calculator.add(10, 60);
        /////////////////////////////////////////////////////////////////////////////////////////////
        Stock stock = null;
        stock = () -> {
            return 100;
        };
        System.out.println(stock.getStockValue());
        //refactoring-5; if no more body, only returning value; remove {} and return statement
        stock = () -> 100;
        System.out.println(stock.getStockValue());
        ///////////////////////////////////////////////////////////////////////////////////////////
        Adder adder = null;
        //refactoring-6; receive,and return
        adder = (a, b) -> a + b;
        System.out.println(adder.add(10, 90));
    }
}
