package generics;
import java.util.ArrayList;
import java.util.List;

/**
 * Wildcard Type	        Syntax	            Example	Use Case
 * Unbounded Wildcard	    <?>	List<?> list	When the type is unknown or irrelevant
 * Upper Bounded Wildcard	<? extends Type>	List<? extends Number> list	When you want to read from a collection
 * Lower Bounded Wildcard	<? super Type>	    List<? super Integer> list	When you want to write to a collection
 */
@SuppressWarnings("unused")
public class Wildcard {
    public static void main(String[] args) {
        // Wildcard
        // ? is a wildcard character that can be used in place of any type
        // It is used to represent an unknown type
        // It is used in generics to represent an unknown type
     
        Wildcard wildcard = new Wildcard();
        wildcard.unboundedExample();
        wildcard.upperBoundedExample();
        wildcard.lowerBoundedExample();
        wildcard.wildcardRelations();
    }


    public void unboundedExample() {
        // Example:
        // With List<?> you can store unknown type of elements and read from it.
        List<?> list = new ArrayList<>(); // list of unknown type.
        List<String> stringList = new ArrayList<>();
        stringList.add("Ele 1");
        stringList.add("Ele 2");

        list = stringList; // OK
        System.out.println(list); // [Ele 1, Ele 2]

        List<Integer> integerList = new ArrayList<>();
        integerList.add(1);
        integerList.add(2);

        list = integerList; // OK
        System.out.println(list); // [1, 2]

        // The following code will not compile
        // list.add("Hello"); // Error: add(capture<?>) in List cannot be applied to (String)
    }

    public void upperBoundedExample() {
        // Example:
        List<? extends String> list = new ArrayList<>(); // list of unknown type.
        List<String> stringList = new ArrayList<>();
        stringList.add("Ele 1");
        stringList.add("Ele 2");

        list = stringList; // OK
        System.out.println(list); // [Ele 1, Ele 2]

        // The following code will not compile
        // list.add("Hello"); // Error: add(capture<?>) in List cannot be applied to (String)
    }

    public void lowerBoundedExample() {
        // Example:
        List<? super String> list = new ArrayList<>(); // list of unknown type.
        List<String> stringList = new ArrayList<>();
        stringList.add("Ele 1");
        stringList.add("Ele 2");

        list = stringList; // OK
        System.out.println(list); // [Ele 1, Ele 2]

        list.add("Hello"); // OK
        System.out.println(list); // [Ele 1, Ele 2, Hello]
        System.out.println(stringList); // [Ele 1, Ele 2, Hello]
    }

    public void wildcardRelations() {
        List<Integer> integerList0 = new ArrayList<>();
        // The following code will not compile
        // List<Number> numberList0 = integerList0; // Error: incompatible types: List<Integer> cannot be converted to List<Number> 

        List<? extends Integer> integerList = new ArrayList<>();
        List<? extends Number> numberList = integerList; // OK

        List<? super DomesticAnimal> domesticAnimalList = new ArrayList<>();
        List<? super WildAnimal> wildAnimalList = new ArrayList<>();
        List<? super Dog> dogList = domesticAnimalList; // OK
        List<? super Cat> catList = domesticAnimalList; // OK

        List<? super Lion> lionList = new ArrayList<>(); // OK
        
        List<? extends Animal> animalList = new ArrayList<>();
        // The following code will not compile
        // animalList.addAll(lionList); // Error: incompatible types: List<? super Lion> cannot be converted to List<? extends Animal>

        // Correct way to add elements to animalList
        List<Animal> animalList2 = new ArrayList<>();
        animalList2.add(new Lion());
        animalList2.add(new Dog());
        animalList2.add(new Cat());
        System.out.println(animalList2);

    }

    private interface Animal {
        void makeSound();
    }

    private class DomesticAnimal implements Animal {
        @Override
        public void makeSound() {
            System.out.println("Domestic Animal makes sound");
        }
    }

    private class Dog extends DomesticAnimal {
        @Override
        public void makeSound() {
            System.out.println("Dog barks");
        }
    }

    private class Cat extends DomesticAnimal {
        @Override
        public void makeSound() {
            System.out.println("Cat meows");
        }
    }

    private class WildAnimal implements Animal {
        @Override
        public void makeSound() {
            System.out.println("Wild Animal makes sound");
        }
    }

    private class Lion extends WildAnimal {
        @Override
        public void makeSound() {
            System.out.println("Lion roars");
        }
    }

    class WildCardHelper {
        void foo(List<?> i) {
            // i.set(0, i.get(0)); // Error: set(int, capture<?>) in List cannot be applied to (int, capture<?>)

            // the below statement will compile, because now the compiler is able to infer the type of T
            fooHelper(i); // Now it's OK. 
        }

        private <T> void fooHelper(List<T> l) {
            l.set(0, l.get(0)); // OK
        }
    }

    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (int i = 0; i < src.size(); i++) {
            dest.set(i, src.get(i));
        }
    }
}
