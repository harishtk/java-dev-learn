package collections;

import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class CustomIterator {
    public static void main(String[] args) {
        // Create a user info map 
        Map<String, String> userInfo = Map.of(
            UserKeys.FIRST_NAME, "John",
            UserKeys.LAST_NAME, "Doe",
            UserKeys.EMAIL, "johndoe@example.com",
            UserKeys.PHONE, "123-456-7890"
        );
        
        // Iterate over the user info map
        UserKeys.iterator()
            .forEachRemaining(s -> System.out.println(s + ": " + userInfo.get(s)));

        sampleRangeImpl();
    }

    public static void sampleRangeImpl() {
        class MyIntRange implements Iterable<Integer> {
            private int start;
            private int end;

            MyIntRange(int start, int end) {
                this.start = start;
                this.end = end;
            }

            @Override
            public Iterator<Integer> iterator() {
                return new Iterator<>() {
                    private int index = start;

                    @Override
                    public boolean hasNext() {
                        return index < end;
                    }

                    @Override
                    public Integer next() {
                        if (index > end) {
                            throw new NoSuchElementException();
                        }
                        int currentIndex = index;
                        index++;
                        return currentIndex;
                    }
                };
            }

            static MyIntRange of(int start, int end) {
                return new MyIntRange(start, end);
            }

            static MyIntRange ofClosed(int start, int end) {
                return of(start, end + 1);
            }
        }

        MyIntRange.of(0, 10)
            .iterator()
            .forEachRemaining(System.out::println);

        MyIntRange.ofClosed(1, 10)
            .iterator()
            .forEachRemaining(System.out::println);
    }

    final class UserKeys {
        private UserKeys() {}

        public static final String FIRST_NAME = "firstName";
        public static final String LAST_NAME = "lastName";
        public static final String EMAIL = "email";
        public static final String PHONE = "phone";

        private static final String[] KEYS = {FIRST_NAME, LAST_NAME, EMAIL, PHONE};

        public static Iterator<String> iterator() {
            return new Iterator<String>() {
                private int index = 0;

                @Override
                public boolean hasNext() {
                    return index < KEYS.length;
                }

                @Override
                public String next() {
                    return KEYS[index++];
                }
            };
        }
    }

}
