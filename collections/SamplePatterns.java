package collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class SamplePatterns {
    public static void main(String[] args) {
        final var app = new SamplePatterns();
        app.arrayOfElements();
        app.filteringElements();
        app.usingIterator();
        app.changeOrderOfArrays();
        app.immutableWrappers();
    }

    public void arrayOfElements() {
        Collection<String> strings = List.of("one", "two");
        System.out.println("strings = " + strings);

        String[] largerTab = {"three", "three", "three", "I", "was", "there"};
        System.out.println("largerTab = " + Arrays.toString(largerTab) + " size = " + largerTab.length);

        String[] result = strings.toArray(largerTab);
        System.out.println("result = " + Arrays.toString(result) + " size = " + result.length);

        System.out.println("Same arrays? " + (result == largerTab));
    }

    public void filteringElements() {
        Collection<String> strings = new ArrayList<>();
        strings.add("one");
        strings.add("two");
        strings.add(null);
        strings.add("");
        strings.add("four");
        strings.add("");
        strings.add("five");
        strings.add(null);

        Predicate<String> isNull = (s) -> s == null;
        Predicate<String> isEmpty = (s) -> s.length() == 0;
        Predicate<String> isNullOrEmpty = isNull.or(isEmpty);

        strings.removeIf(isNullOrEmpty);
        System.out.println(strings);
    }

    public void usingIterator() {
        // Collection<String> strings = List.of("one", "two", "three", "four");
        Collection<String> strings = new ArrayList<>(List.of("one", "two", "three", "four"));

        System.out.println("strings = " + strings);
        for (Iterator<String> iter = strings.iterator(); iter.hasNext();) {
            final String ele = iter.next();
            if (ele.length() == 3) {
                System.out.println(ele);
            } else {
                iter.remove(); // This will not work on immutable collection such as List
            }
        }

        System.out.println("strings = " + strings);
    }

    public void changeOrderOfArrays() {
        List<String> strings = Arrays.asList("0", "1", "2", "3", "4");
        System.out.println("strings = " + strings);

        int fromIdx = 1, toIdx = 4; 
        Collections.rotate(strings.subList(fromIdx, toIdx), -1);
        System.out.println("strings = " + strings);
    }

    public void immutableWrappers() {
        List<String> strings = new ArrayList<>(Arrays.asList("0", "1", "2", "3", "4"));
        // Create a immutable version of strings
        List<String> immutableStrings = Collections.unmodifiableList(strings);

        // The following code will throw UnsupportedOperationException
        // immutableStrings.add("5");

        // But the reference list strings is mutable, 
        // doing so will also update the immutableStrings object indirectly
        strings.add("5");
        System.out.println("strings = " + strings);

        // To make sure it is immutable or at least the wrapped one,
        // we make a copy of the strings defensively followed by
        // wrapping it into an Collections#unmodifiableList
        List<String> reallyImmutableStrings = List.copyOf(strings);

        strings.add("6");
        System.out.println("strings = " + strings);
        System.out.println("reallyImmutableStrings = " + reallyImmutableStrings);
    }
}
