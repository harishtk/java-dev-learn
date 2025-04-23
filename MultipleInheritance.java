import java.util.Arrays;

public class MultipleInheritance {
    public static void main(String[] args) {
        // Driver code
        final String name = "Hello, World!";
        SomeClass someClass = new SomeClass();
        someClass.print(name);

        ((FirstInterface) someClass).print(name);
        ((SecondInterface) someClass).print(name);

        someClass.someMethod();
    }

    public static interface FirstInterface {
        default void print(String name) {
            final String className = "FirstInterface";
            System.out.println(String.format("[%s]: %s", className, name));
        }

        void someMethod();
    }

    public static interface SecondInterface {
        default void print(String name) {
            final String className = "SecondInterface";
            System.out.println(String.format("[%s]: %s", className, name));
        }

        void someMethod();
    }

    static class SomeClass implements FirstInterface, SecondInterface {
        // Error: class SomeClass inherits unrelated defaults for print(String) from types FirstInterface and SecondInterface
        // To resolve this error, we need to override the default method in the implementing class
        @Override
        public void print(String name) {
            System.out.println("[SomeClass]: Calling FirstInterface.print()");
            FirstInterface.super.print(name);
            System.out.println("[SomeClass]: Calling SecondInterface.print()");
            SecondInterface.super.print(name);

            // print interfaces
            System.out.println(
                Arrays.asList(
                    this.getClass().getInterfaces()
                )
            );

            System.out.println(
                Arrays.asList(
                    this.getClass().getDeclaredMethods()
                )
            );
        }

        @Override
        public void someMethod() {
            System.out.println("Some method");
        }
    }
}
