package collections;

import java.util.List;

public class ListDemo {
    public static void main(String[] args) {
        immutableListDemo();
    }

    static void immutableListDemo() {
        List<String> strings = List.of("one", "two", "three");
        System.out.println("strings = " + strings);

        System.out.println("type(strings): " + strings.getClass());
    }
}