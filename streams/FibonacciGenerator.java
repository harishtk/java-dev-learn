package streams;

import java.util.stream.Stream;
import java.util.function.Supplier;

public class FibonacciGenerator {
    public static void main(String[] args) {
        usingGenerate();
        usingIterate();
    }

    static void usingIterate() {
        System.out.println("Creating Streams using iterate()");
        // Create a stream of Fibonacci numbers
        Stream<Integer> fibonacciStream = Stream.iterate(new int[]{0, 1}, f -> new int[]{f[1], f[0] + f[1]})
                .map(f -> f[0])
                .limit(10);
        fibonacciStream.forEach(System.out::println);
    }

    static void usingGenerate() {
        System.out.println("Creating Streams using generate()");
        // Create a stream of Fibonacci numbers
        Stream<Integer> fibonacciStream = Stream.generate(new FibonacciSupplier()).limit(10);
        fibonacciStream.forEach(System.out::println);
    }

    private static class FibonacciSupplier implements Supplier<Integer> {
        private int prev = 0;
        private int curr = 1;

        @Override
        public Integer get() {
            int current = prev;
            int next = prev + curr;
            prev = curr;
            curr = next;
            return current;
        }
    }
}
