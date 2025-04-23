package collections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * This class demonstrates, what will happen if the Key for Map.Entry 
 * object is mutable and it's state is changed after it's insertion
 * into the Map.
 */
public class MapDemo {
    public static void main(String[] args) {
        final Key changingKey = new Key("key");
        final Key anotherKey = new Key("key1");
        final Map<Key, String> map =  new HashMap<>();
        map.put(changingKey, "value");
        map.put(anotherKey, "anotherValue");

        System.out.println(map);
        changingKey.key = "newKey";
        map.put(changingKey, "newValue");

        final Key sameHashCodeOfChangingKey = new Key("newKey");
        map.put(sameHashCodeOfChangingKey, "poisenedValue");

        System.out.println(map);

        mergeDemo();
        mutableKeyGotchas();
    }

    static void mergeDemo() {
        List<String> strings = List.of("one", "two", "three", "four", "five", "six", "seven");
        Map<Integer, List<String>> map = new HashMap<>();
        Map<Integer, List<String>> treeMap = new TreeMap<>();

        for (String str : strings) {
            int length = str.length();
            map.computeIfAbsent(length, key -> new ArrayList<String>()).add(str);
        }

        System.out.println(map);

        Map<Integer, String> mergedMap = new HashMap<>();
        map.forEach((key, value) -> {
            String mergedValue = String.join(", ", value);
            mergedMap.merge(key, mergedValue, (oldValue, newValue) -> oldValue + "," + newValue);
        }); 

        System.out.println(mergedMap);
    }

    static void mutableKeyGotchas() {
        System.out.println("Mutable Key Gotchas");
        final Key one = new Key("1");
        final Key two = new Key("2");

        Map<Key, String> map = new HashMap<>();
        map.put(one, "one");
        map.put(two, "two");

        System.out.println(map);

        one.setKey("5");

        System.out.println("map.get(one): " + map.get(one));
        System.out.println("map.get(two): " + map.get(two));

        System.out.println("map.get(new Key(1)) = " + map.get(new Key("1")));
        System.out.println("map.get(new Key(2)) = " + map.get(new Key("2")));
        System.out.println("map.get(new Key(5)) = " + map.get(new Key("5")));

        one.setKey("2");

        System.out.println("map.get(one) = " + map.get(one));
        System.out.println("map.get(two) = " + map.get(two));
        System.out.println("map.get(new Key(1)) = " + map.get(new Key("1")));
        System.out.println("map.get(new Key(2)) = " + map.get(new Key("2")));
    }

    static class Key {
        private String key;
        Key(String key) {
            this.key = key;
        }

        void setKey(String key) {
            this.key = key;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Key myKey = (Key) obj;
            return key.equals(myKey.key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }

        @Override
        public String toString() {
            return "Key[key = " + key + "]";
        }
    }
}
