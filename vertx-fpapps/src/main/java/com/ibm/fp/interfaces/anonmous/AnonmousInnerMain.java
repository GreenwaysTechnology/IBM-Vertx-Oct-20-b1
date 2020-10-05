package com.ibm.fp.interfaces.anonmous;

interface Greeter {
    void sayGreet();
}

public class AnonmousInnerMain {
    public static void main(String[] args) {
        Greeter greeter = null;
        //annonous inner class
        greeter = new Greeter() {
            @Override
            public void sayGreet() {
                System.out.println("Greeter");
            }
        };
        greeter.sayGreet();
        greeter = new Greeter() {
            @Override
            public void sayGreet() {
                System.out.println("Hello");
            }
        };
        greeter.sayGreet();
        greeter = new Greeter() {
            @Override
            public void sayGreet() {
                System.out.println("Hai");
            }
        };
        greeter.sayGreet();

    }
}
