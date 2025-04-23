package functional;

import java.nio.file.*;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

// this class demonstrans on how to refactor the imperative code to functional code
public class ImperativeToFunctional {
    public static void main(String[] args) {
        ImperativeToFunctional app = new ImperativeToFunctional();
        app.simpleLoops();
        app.complexLoops();
        app.simpleForEach();
        app.transforming();
        app.dataSources();
    }

    public void simpleLoops() {
        // Imperative
        System.out.print("Imperative: ");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        System.out.print("Functional: ");

        IntConsumer printer = (s) -> System.out.print(s + " ");
        // Functional
        IntStream
                .range(0, 10)
                .forEach(printer);

        System.out.println();
    }

    public void complexLoops() {
        // Imperative
        System.out.print("Imperative: ");
        for (int i = 0;; i += 2) {
            if (i >= 20) {
                break;
            }
            System.out.print(i + " ");
        }
        System.out.print("\nFunctional: ");

        IntConsumer printer = (s) -> System.out.print(s + " ");
        IntStream
                .iterate(0, i -> i + 2)
                .takeWhile(i -> i < 20)
                .forEach(printer);
        System.out.println();
    }

    public void simpleForEach() {
        Consumer<String> print = (s) -> System.out.print(s + " ");

        List<String> names = List.of("John", "Jane", "Tina", "Tim");
        // Imperative
        System.out.print("Imperative: ");
        for (String name : names) {
            print.accept(name);
        }

        System.out.println();
        System.out.print("Functional: ");

        // Functional
        names.forEach(print::accept);
        System.out.println();

        // Functional with Stream
        System.out.print("Functional with Stream: ");
        Predicate<String> length3 = s -> s.length() == 3;
        names.stream()
                .filter(length3)
                .forEach(print);
        System.out.println();
    }

    public void transforming() {
        Consumer<String> print = (s) -> System.out.print(s + " ");
        List<String> names = List.of("John", "Jane", "Tina", "Tim");

        // Imperative
        System.out.print("Imperative: ");
        for (String name : names) {
            print.accept(name.toUpperCase());
        }

        System.out.println();
        System.out.print("Functional: ");
        // Functional
        names.stream()
                .map(String::toUpperCase)
                .forEach(print);
        System.out.println();
    }

    public void dataSources() {
        // Imperative
        try {
            final var fileName = "ImperativeToFunctional.java";
            final var wordOfInterest = "public";

            // Imperative
            try (var reader = Files.newBufferedReader(
                    FileSystems.getDefault()
                            .getPath("D:\\Temp\\functional", fileName))) {
                String line;
                long count = 0;

                while ((line = reader.readLine()) != null) {
                    if (line.contains(wordOfInterest)) {
                        count++;
                    }
                }

                System.out.println("Found " + count + " occurrences of " + wordOfInterest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Functional
        try {
            final var fileName = "ImperativeToFunctional.java";
            final var wordOfInterest = "public";

            long count;
            try (var lines = Files.lines(Path.of("D:\\Temp\\functional", fileName))) {
                count = lines.filter(line -> line.contains(wordOfInterest)).count();
            }

            System.out.println("Found " + count + " occurrences of " + wordOfInterest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
