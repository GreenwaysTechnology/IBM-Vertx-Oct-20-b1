package com.ibm.fp.lambda.methodreferences;
//method reference is syntax to reduce lambda itsefl

@FunctionalInterface
interface Printer {
    void print(String message);
}

@FunctionalInterface
interface UpperCase {
    String convertToUpper(String message);
}

class MicroTask {
    public static void start() {
        System.out.println(Thread.currentThread().getName());
    }

    public void startMicroTask() {
        System.out.println(Thread.currentThread().getName());
    }
}


class Task {

    private void startMicroTask() {
        System.out.println(Thread.currentThread().getName());
    }

    public void startTask() {
        Thread thread = null;
        Runnable runner = null;
        runner = () -> System.out.println(Thread.currentThread().getName());
        thread = new Thread(runner);
        thread.start();
        //inline
        thread = new Thread(() -> System.out.println(Thread.currentThread().getName()));
        thread.start();
        //isloate thread's runnable implementation as a sperate method
        thread = new Thread(() -> this.startMicroTask());
        thread.start();
        MicroTask microTask = new MicroTask();
        thread = new Thread(() -> microTask.startMicroTask());
        thread.start();
        thread = new Thread(() -> new MicroTask().startMicroTask());
        thread.start();
        //how to remove lambda itself
        thread = new Thread(this::startMicroTask);
        thread.start();
        thread = new Thread(microTask::startMicroTask);
        thread.start();
        thread = new Thread(new MicroTask()::startMicroTask);
        thread.start();
        thread = new Thread(MicroTask::start);
        thread.start();

    }
}


public class MethodReference {
    public static void main(String[] args) {
        Task task = new Task();
        task.startTask();
        Printer printer = null;
        //lambda syntax
        printer = name -> System.out.println("Hello" + name);
        printer.print("Subramanian");
        //method reference
        printer = System.out::println;
        printer.print("Subramanian");
//////////////////////////////////////////////////////////////////////////////////
        UpperCase upperCase = null;
        upperCase = message -> message.toUpperCase();
        System.out.println(upperCase.convertToUpper("subramanian"));
        upperCase = String::toUpperCase;
        System.out.println(upperCase.convertToUpper("subramanian"));


    }
}
