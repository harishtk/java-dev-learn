import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleSupplier;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class FunctionInteface {
    Consumer<String> print = (s) -> System.out.println(s);
    Predicate<Integer> isEven = (n) -> n % 2 == 0;
    Function<Integer, Integer> square = (n) -> n * n;
    BiConsumer<Integer, Integer> printAdd = (a, b) -> System.out.println(a + b);
    BiPredicate<Integer, Integer> isGreater = (a, b) -> a > b;
    BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;
    UnaryOperator<Integer> doubleIt = (n) -> n * 2;
    BinaryOperator<Integer> addIt = (a, b) -> a + b;
    Supplier<Double> pi = () -> 3.14159265359; // or
    DoubleSupplier pi2 = () -> 3.14159265359; // This bypasses auto-boxing and unboxing
    Predicate<String> notNull = (s) -> s != null;

    ObjDoubleConsumer<String> objDoubleConsumer = (s, d) -> System.out.println(s + d);

    /**
     * fun <A, B, C> compose(f: (B) -> C, g: (A) -> B): (A) -> C {
     *  return { x -> f(g(x)) }
     * }
     * 
     * @param args
     */
    public static <A, B, C> Function<A, C> compose(
        Function<B, C> f, Function<A, B> g
    ) {
        return x -> f.apply(g.apply(x));
    }

    public static void main(String[] args) {
        FunctionInteface fi = new FunctionInteface();

        System.out.println("Consumer Example:");
        fi.print.accept("Hello, Consumer!");

        System.out.println("\nPredicate Example:");
        System.out.println("Is 4 even? " + fi.isEven.test(4));
        System.out.println("Is 7 even? " + fi.isEven.test(7));

        System.out.println("\nFunction Example:");
        System.out.println("Square of 5 is: " + fi.square.apply(5));

        System.out.println("\nBiConsumer Example:");
        System.out.println("Sum of 3 and 5: ");
        fi.printAdd.accept(3, 5);
        fi.objDoubleConsumer.accept("PI: ", fi.pi2.getAsDouble());

        System.out.println("\nBiPredicate Example:");
        System.out.println("Is 10 greater than 5? " + fi.isGreater.test(10, 5));
        System.out.println("Is 3 greater than 7? " + fi.isGreater.test(3, 7));

        System.out.println("\nBiFunction Example:");
        System.out.println("Sum of 8 and 2 is: " + fi.add.apply(8, 2));

        System.out.println("\nUnaryOperator Example:");
        System.out.println("Double of 6 is: " + fi.doubleIt.apply(6));

        System.out.println("\nBinaryOperator Example:");
        System.out.println("Sum of 4 and 9 is: " + fi.addIt.apply(4, 9));

        System.out.println("\nSupplier Example:");
        System.out.println("Value of PI: " + fi.pi.get());

        System.out.println("\nCompose Example:");
        Function<Integer, Integer> addOne = x -> x + 1;
        Function<Integer, Integer> squareIt = x -> x * x;
        Function<Integer, Integer> addOneSquare = compose(squareIt, addOne);
        System.out.println("Square of (1 + 1) is: " + addOneSquare.apply(1));
        System.out.println("Square of (2 + 1) is: " + addOne.andThen(squareIt).apply(1));

        System.out.println("\nPredicate Example:");
        System.out.println("Is null not null? " + fi.notNull.test(null));
        System.out.println("Is \"Hello\" not null? " + fi.notNull.test("Hello"));

        autoBoxingUnboxingTest();
        arraysListTest();
    }

    public static void autoBoxingUnboxingTest() {
        final Random random = new Random(314L);
        Supplier<Integer> randomInt = () -> random.nextInt();
        IntSupplier randomInt2 = () -> random.nextInt();

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            randomInt.get();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Supplier<Integer> Time: " + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            randomInt2.getAsInt();
        }
        endTime = System.currentTimeMillis();
        System.out.println("IntSupplier Time: " + (endTime - startTime) + "ms");
    }

    public static void arraysListTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        list.forEach(System.out::println);
        // list.add(6); // This will throw an UnsupportedOperationException
    }
}