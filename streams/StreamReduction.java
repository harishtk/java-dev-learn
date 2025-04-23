package streams;

import java.util.List;
import java.util.Optional;

public class StreamReduction {
    public static void main(String[] args) {
        example1();
    }

    static void example1() {
        List<Integer> ints = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // Reduce the stream to a single value using a binary operator

        System.out.println("Reduce the stream to a single value using a binary operator");
        // The reduce method takes a binary operator and returns an Optional<T>
        Optional<Integer> result = ints.stream()
                .reduce((a, b) -> a + b);
        // The result is an Optional<Integer> because the stream may be empty
        // Print the result
        System.out.println("Result: " + result.orElse(0)); // 55    
    }

    static void example2() {
        List<Integer> ints = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        // Reduce the stream to a single value using an identity value and a binary operator

        System.out.println("Reduce the stream to a single value using an identity value and a binary operator");
        // The reduce method takes an identity value and a binary operator and returns a T
        // The identity value is returned if the stream is empty
        Integer result = ints.stream()
                .reduce(0, (a, b) -> a + b);
        // Print the result
        System.out.println("Result: " + result); // 55    
    }

    static void example3() {
        List<String> numbers = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");

        // Reduce the stream to single value of the lengh of the all strings in the stream
        System.out.println("Reduce the stream to single value of the length of the all strings in the stream");

        int result = numbers.stream()
                .reduce(
                        0,
                        (a, b) -> a + b.length(),
                        (a, b) -> a + b
                );
        // print the result
        System.out.println("Result: " + result); // 36
    }
}
