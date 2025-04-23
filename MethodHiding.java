public class MethodHiding {
    public static void main(String[] args) {
        method();
    }

    public static void method() {
        System.out.println("Method from MethodHiding class");

        abstract class Animal {
            private String name;

            Animal(String name) {
                this.name = name;
            }

            abstract void makeSound();

            public void name() {
                System.out.println("Animal[name = " + name + "]");
            }

            private static void someMethod() {
                System.out.println("Some method from Animal class");
            }

            public String getName() {
                return name;
            }
        }

        class Dog extends Animal {
            private String breed;

            Dog(String name, String breed) {
                super(name);
                this.breed = breed;
            }

            @Override
            void makeSound() {
                System.out.println("Bow bow");
            }

            public void name() {
                System.out.println("Dog[name = " + getName() + ", breed = " + breed + "]");
            }

            public static void someMethod() {
                System.out.println("Some method from Dog class");
            }
        }

        Animal dog = new Dog("Buddy", "Golden Retriever");
        dog.name();
        dog.makeSound();
        Dog.someMethod();
        Animal.someMethod();
    }
}
