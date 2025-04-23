package collections;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.TreeSet;

import collections.MapDemo.Key;

public class SetDemo {
    public static void main(String[] args) {
        unorderedSet();
        mutableContentInSet();
    }

    static void unorderedSet() {
        final Set<String> unorderedSet = new HashSet<>();
        final Set<String> orderedSet = new TreeSet<>();
        final Set<String> linkedHashSet = new LinkedHashSet<>();


    }

    static void mutableContentInSet() {
        Key one = new Key("1");
        Key two = new Key("2");

        Set<Key> set = new HashSet<>();
        set.add(one);
        set.add(two);

        System.out.println("set = " + set);

        // You should never mutate an object once it has been added to a Set!
        one.setKey("3");
        System.out.println("set.contains(one) = " + set.contains(one));
        boolean addedOne = set.add(one);
        System.out.println("addedOne = " + addedOne);
        System.out.println("set = " + set);
    }
}
