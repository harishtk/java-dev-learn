package collections;

import java.util.ArrayList;

public class ArrayListGrowthTracker {
    public static void main(String[] args) {
        ArrayList<Integer> list = new ArrayList<>();
        int initialCapacity = getCapacity(list);
        System.out.println("Initial capacity: " + initialCapacity);

        for (int i = 0; i < 20; i++) {
            list.add(i);
            int currentCapacity = getCapacity(list);
            if (currentCapacity != initialCapacity) {
                System.out.println("Capacity changed to: " + currentCapacity + " after adding " + (i + 1) + " elements.");
                initialCapacity = currentCapacity;
            }
        }

        System.out.println("Final size: " + list.size());
        System.out.println("Final capacity: " + getCapacity(list));
    }

    // The method won't work as expected due to the some security restrictions
    // introduced from Java 9, which prevent access to the internal array of an
    // ArrayList. The method will return -1 in such cases.
    // Helper method to get the capacity of an ArrayList using reflection
    public static int getCapacity(ArrayList<?> list) {
        try {
            java.lang.reflect.Field dataField = ArrayList.class.getDeclaredField("elementData");
            if (!dataField.canAccess(list)) {
                dataField.setAccessible(true);
            }
            Object[] elementData = (Object[]) dataField.get(list);
            return elementData != null ? elementData.length : 0;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.err.println("Error while accessing capacity: " + e.getMessage());
            return -1; // Indicate failure
        }
    }
}
