package collections;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

public class ListPerformance {
    public static void main(String[] args) {
        int size = 1000000;

        // ArrayList performance
        long startTime = System.nanoTime();
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            arrayList.add(i);
        }
        long endTime = System.nanoTime();
        System.out.println("ArrayList add: " + (endTime - startTime) / 1000000 + " ms");

        // Vector performance
        startTime = System.nanoTime();
        List<Integer> vector = new Vector<>();
        for (int i = 0; i < size; i++) {
            vector.add(i);
        }
        endTime = System.nanoTime();
        System.out.println("Vector add: " + (endTime - startTime) / 1000000 + " ms");
    }
}
