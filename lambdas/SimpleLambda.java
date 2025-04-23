package lambdas;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SimpleLambda {
    public static void main(String[] args) {
        List<String> strings = List.of("a", "ab", "abc", "abcd", "abcde", "abcdef");
        System.out.println(retainStringsOfLength(3, strings));

        capturingAndNonCapturingLambdas();
    }

    static List<String> retainStringsOfLength(int length, List<String> strings) {
        final Predicate<String> predicate = s -> s.length() == length;
        return strings.stream().filter(predicate).collect(Collectors.toList());
    }

    static void capturingAndNonCapturingLambdas() {
        int x = 10;
        final Predicate<Integer> nonCapturing = i -> i > 10;

        // x is captured and effectively final, 
        // so even if it changes, the lambda will use the value it was created with 
        final Predicate<Integer> capturing = i -> i > x;
        
        System.out.println(nonCapturing.test(5));
        System.out.println(capturing.test(5));
        System.out.println(capturing.test(15));
    }
}
