package streams;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamWithOptionals {
    public static void main(String[] args) {
        var maria = new Author("Maria");
        var james = new Author("James");
        var patricia = new Author("Patricia");
        var michael = new Author("Michael");

        var a0 = new Article("About As You Like It", 2015, List.of(maria));
        var a1 = new Article("About King John", 2015, List.of(james));
        var a2 = new Article("About The Winter's Tale", 2016, List.of(patricia));
        var a3 = new Article("About Richard II", 2017, List.of(michael));
        var a4 = new Article("About Richard III", 2019, List.of(maria, patricia));
        var a5 = new Article("About Henry VIII", 20219, List.of(patricia, michael));
        var a6 = new Article("About Romeo and Juliet", 2020, List.of(maria, patricia, james));
        var a7 = new Article("About Macbeth", 2021, List.of(maria, james, michael));
        var a8 = new Article("About Hamlet", 2021, List.of(patricia, james, michael));
        var a9 = new Article("About King Lear", 2022, List.of(maria, james, patricia, michael));
        var articles = List.of(a0, a1, a2, a3, a4, a5, a6, a7, a8, a9);

        BiFunction<Article, Author, Stream<PairOfAuthors>> buildPairOfAuthors =
                (article, author) -> article.authors().stream()
                        .flatMap(other -> PairOfAuthors.of(author, other).stream());

        Function<Article, Stream<PairOfAuthors>> toPairOfAuthors =
                article -> article.authors().stream()
                        .flatMap(author -> buildPairOfAuthors.apply(article, author));

        Map<PairOfAuthors, Long> numberOfAuthorsTogether =
            articles.stream()
                    .flatMap(toPairOfAuthors)
                    .collect(
                        Collectors.groupingBy(
                            Function.identity(),
                            Collectors.counting()
                        )
                    );
        
        System.out.println("Number of authors together: " + numberOfAuthorsTogether);

        Function<Map<PairOfAuthors, Long>, Map.Entry<PairOfAuthors, Long>> maxExtracter =
                map -> map.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .orElseThrow();

        Map.Entry<PairOfAuthors, Long> pair = maxExtracter.apply(numberOfAuthorsTogether);

        System.out.println(
                "The authors that published the most together are " +
                        pair.getKey().first().name() + " and " + pair.getKey().second().name() +
                        ", they wrote " + pair.getValue() + " articles together.");
    }

    record Author(String name) implements Comparable<Author> {
        @Override
        public int compareTo(Author o) {
            return name.compareTo(o.name);
        }
    }

    record Article(String title, int inceptionYear, List<Author> authors) {}

    record PairOfAuthors(Author first, Author second) {
        public static Optional<PairOfAuthors> of(Author first, Author second) {
            if (first.compareTo(second) < 0) {
                return Optional.of(new PairOfAuthors(first, second));
            } else {
                return Optional.empty();
            }
        }
    }
}
