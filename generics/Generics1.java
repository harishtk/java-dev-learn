package generics;
public class Generics1 {
    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<>();
        integerBox.set(10);
        printBox(integerBox);

        Box<Double> doubleBox = new Box<>();
        doubleBox.set(10.0);
        printBox(doubleBox);  
        
        typeInferenceDemo();
    }

    static void printBox(Box<? extends Number> box) {
        System.out.println(box.get());
    }

    static void typeInferenceDemo() {
        class MyClass<X> {
            X x;
            <T> MyClass(T t) {}
        }

        final MyClass<Integer> myObject = new MyClass<>("Hello");
        // prints the type of myObject
        System.out.println(myObject.getClass().getName());

        myObject.x = 20;
        // prints the type of myObject.x
        System.out.println(myObject.x.getClass().getName());
    }
}
