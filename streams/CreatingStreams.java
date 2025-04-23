package streams;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CreatingStreams {
    public static void main(String[] args) {
        System.out.println("Creating Streams from varous sources");
        fromIter();
        fromVarargs();
        usingSupplier();
        fromRangeOfNumbers();
        fromRandom();
        usingStreamBuilders();
        fromHttp();
    }

    static void fromIter() {
        System.out.println("Creating Streams from an Iterator");
        Iterator<Integer> iter = new Iterator<>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < 10;
            }

            @Override
            public Integer next() {
                return i++;
            }
        };

        // Create a stream from an iterator
        long estimatedSize = 10L;
        int characteristics = Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.SIZED;
        Spliterator<Integer> spliterator = Spliterators.spliterator(iter, estimatedSize, characteristics);

        // Create a stream from a spliterator
        boolean parallel = false;
        Stream<Integer> stream = StreamSupport.stream(spliterator, parallel);
        
        List<Integer> ints = stream.toList();
        System.out.println(ints);
    }

    static void fromVarargs() {
        System.out.println("Creating Streams from varargs");
        // Create a stream from varargs
        Stream<String> stream = Stream.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j");
        List<String> strings = stream.toList();
        System.out.println(strings);

        // Create a stream from an array
        String[] array = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j"};
        Stream<String> stream2 = Arrays.stream(array);
        System.out.println(stream2.collect(Collectors.toList()));
    }

    static void usingSupplier() {
        System.out.println("Creating Streams using a Supplier");
        // Create a stream using a supplier
        Stream<String> stream = Stream.generate(() -> "Hello").limit(10);
        List<String> strings = stream.toList();
        System.out.println(strings);

        // Supplier example for a Fibonacci sequence
        Stream<Pair<Integer, Integer>> fib = Stream.iterate(
            new Pair<>(0, 1),
            p -> new Pair<>(p.second, p.first + p.second)
        );

        // Limit is applied here to restrict the stream to 10 elements
        List<Integer> fibList = fib.limit(10)
            .map(p -> p.first)
            .collect(Collectors.toList());
        System.out.println(fibList);
    }

    static void fromRangeOfNumbers() {
        String[] letters = "A B C D".split(" ");
        System.out.println("Creating Streams from a range of numbers");

        // Create a stream from a range of numbers
        List<String> listLetters = 
            IntStream.range(0, 10)
                .mapToObj(i -> letters[i % letters.length])
                .collect(Collectors.toList());
        System.out.println(listLetters);
    }

    static void fromRandom() {
        Random random = new Random(314L);

        System.out.println("Creating Streams from random numbers");
        List<String> letters =
            random.doubles(1_000, 0d, 1d)
                .mapToObj(rand ->
                    rand < 0.5 ? "A" : // 50% of A
                    rand < 0.8 ? "B" : // 30% of B
                    rand < 0.9 ? "C" :  // 10% of C
                    "D" // 10% of D
                ).toList();
        
        Map<String, Long> counts = letters.stream()
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        counts.forEach((k, v) -> System.out.println(k + " :: " + v));
    }

    static void usingStreamBuilders() {
        System.out.println("Creating Streams using Stream.Builder");
        Stream.Builder<String> streamBuilder = Stream.builder();
        streamBuilder.add("A").add("B").add("C").add("D").add("E");

        Stream<String> stream = streamBuilder.build();

        List<String> strings = stream.collect(Collectors.toList());
        System.out.println(strings);
    }

    static void fromHttp() {
        // The URI of the file
        URI uri = URI.create("https://www.gutenberg.org/files/98/98-0.txt");

        // The code to open create an HTTP request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(uri).build();

        // The code to send the request and receive the response
        try {
            HttpResponse<Stream<String>> response = client.send(request, HttpResponse.BodyHandlers.ofLines());

            // The code to process the response
            List<String> lines;
            try (
                Stream<String> stream = response.body();
                // Create a buffered file output stream
                BufferedWriter writer = new BufferedWriter(new FileWriter("A_Tale_of_Two_Cities.txt"));
                // BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream("A_Tale_of_Two_Cities.txt"));
            ) {
                var filteredStream = stream
                    .dropWhile(line -> !line.equals("A TALE OF TWO CITIES"))
                    .takeWhile(line -> !line.equals("*** END OF THE PROJECT GUTENBERG EBOOK A TALE OF TWO CITIES ***"));

                    lines = filteredStream.peek(line -> {
                        try {
                            writer.write(line + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }).collect(Collectors.toList());

                writer.flush();
            }
            System.out.println("# lines = " + lines.size());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class Pair<First, Second> {
        final First first;
        final Second second;
        public Pair(First first, Second second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair[first=" + first + ", second=" + second + "]";
        }
    }
}
