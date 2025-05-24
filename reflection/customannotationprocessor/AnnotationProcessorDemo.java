public class AnnotationProcessorDemo {
    public static void main(String[] args) {
        User user = new User(null);
        // try {
        //     for (Field field : User.class.getDeclaredFields()) {
        //     if (field.isAnnotationPresent(NonNull.class)) {
        //         field.setAccessible(true);
        //         Object value = field.get(user);
        //         if (value == null) {
        //             throw new IllegalStateException(field.getName() + " must not be null.");
        //         }
        //     }
        // }
        // } catch (IllegalAccessException e) {
        //     e.printStackTrace();
        // }

        // System.out.println("All @NonNull fields are non-null.");
    }
}
