package streams;

import java.util.List;

public class SimpleStream {
    public static void main(String[] args) {
        SimpleStream app = new SimpleStream();
        app.streamDemo();
        app.flatMapDemo();
        app.debugStreams();
    }

    void streamDemo() {
        // Imperative approach
        int totalPopulation = 0;
        for (City city : cities) {
            totalPopulation += city.population;
        }

        System.out.println("Total population: " + totalPopulation);

        // Functional approach
        int totalPopulation2 = cities.stream()
            .map(city -> city.population)
            .reduce(0, (a, b) -> a + b);

        System.out.println("Total population: " + totalPopulation2);

        // Printing populations of cities over 2 million
        System.out.println("Cities with population over 2 million:");
        cities.stream()
            .filter(city -> city.population > 2_000_000)
            .map(City::name)
            .forEach(System.out::println);
    }

    void flatMapDemo() {

        City newYork = new City("New York", 8_258);
        City losAngeles = new City("Los Angeles", 3_821);
        Country usa = new Country("USA", List.of(newYork, losAngeles));

        City london = new City("London", 8_866);
        City manchester = new City("Manchester", 568);
        Country uk = new Country("United Kindgom", List.of(london, manchester));

        City paris = new City("Paris", 2_103);
        City marseille = new City("Marseille", 877);
        Country france = new Country("France", List.of(paris, marseille));

        List<Country> countries = List.of(usa, uk, france);

        int totalPopulation = countries.stream()
                .flatMap(country -> country.cities().stream())
                .mapToInt(City::population)
                .sum();
        System.out.println("Total population = " + totalPopulation);
    }

    void debugStreams() {
        List<String> numberStrings = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten");

        List<String> result = numberStrings
            .stream()
            .peek(s -> System.out.println("Starting with: " + s))
            .filter(s -> s.startsWith("t"))
            .peek(s -> System.out.println("Filtered: " + s))
            .map(String::toUpperCase)
            .peek(s -> System.out.println("Mapped: " + s))
            .sorted()
            .peek(s -> System.out.println("Sorted: " + s))
            .toList();
        System.out.println("Result: " + result);
    }

    record City(String name, int population) {}
    record Country(String name, List<City> cities) {}

    // Example list of 10 cities with various population
    List<City> cities = List.of(
        new City("New York", 8_500_000),
        new City("Los Angeles", 4_000_000),
        new City("Chicago", 2_700_000),
        new City("Houston", 2_300_000),
        new City("Phoenix", 1_700_000),
        new City("Philadelphia", 1_600_000),
        new City("San Antonio", 1_500_000),
        new City("San Diego", 1_400_000),
        new City("Dallas", 1_300_000),
        new City("San Jose", 1_000_000)
    );
}
