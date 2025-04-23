package generics;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class HeapPollution {
    public static void main(String[] args) {
        List<String> stringListA = new ArrayList<>();
        List<String> stringListB = new ArrayList<>();

        ArrayBuilder.addToList(stringListA, "Seven",  "Eight", "Nine");
        ArrayBuilder.addToList(stringListB, "Ten", "Eleven", "Twelve");

        List<List<String>> listOfStringLists = new ArrayList<List<String>>();
        ArrayBuilder.addToList(listOfStringLists, stringListA, stringListB);

        ArrayBuilder.faultyMethod(Arrays.asList("Hello!"), Arrays.asList("World!"));

        // Instantiating type parameters demo
        List<String> stringListC = new ArrayList<>();
        try {
            HeapPollution.<String>append(stringListC, String.class);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        arraysOfParameterizedTypes();
    }

    private class ArrayBuilder {
        @SafeVarargs
        public static <T> void addToList(List<T> listArg, T... elements) {
            for (T x : elements) {
                listArg.add(x);
            }
        }

        public static void faultyMethod(List<String>... l) {
            Object[] objectArray = l;
            objectArray[0] = Arrays.asList("42");
            String s = l[0].get(0);
        }
    }

    /**
     * This method appends an element to a list
     * 
     * This is a demonstration of instantiating a generic type
     * 
     * @param <E>
     * @param list
     * @param clazz
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SecurityException 
     * @throws NoSuchMethodException 
     * @throws InvocationTargetException 
     * @throws IllegalArgumentException 
     */
    public static <E> void append(List<E> list, Class<E> clazz) throws IllegalAccessException, InstantiationException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        // E elem = clazz.newInstance();
        E elem = clazz.getDeclaredConstructor().newInstance();
        list.add(elem);

        // This code won't compile
        // if (list instanceof ArrayList<Integer>) {}
    }

    // This is not allowed, but the compiler or runtime can't detect it
    public static void arraysOfParameterizedTypes() {
        // List<String>[] arr  = new ArrayList<String>[2];

        // Object[] stringLists = new List<String>[2];

        Object[] strings = new String[2];
        strings[0] = "hi"; // OK
        strings[1] = 100; // An ArrayStoreException should be thrown, but the runtime can't detect it

        // This code will not compile
        // class MathException<T> extends Exception {}
    }

}
