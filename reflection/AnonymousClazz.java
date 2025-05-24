public class AnonymousClazz {
    public static void main(String[] args) {
        Object obj = new Object() {
            @Override
            public String toString() {
                return "Object[]";
            }
        };
        Class<?> clazz = obj.getClass();
        System.out.println("class: " + clazz);
        System.out.println("object: " + obj);

        Object obj2 = new Object() {};
        Class<?> clazz2 = obj2.getClass();
        System.out.println("class: " + clazz2);

        int a = 10;
        long result = a + 20;
        System.out.println("a: " + a + "res: " + result);        

        System.out.println(Double.TYPE);

        // Superclass demo
        System.out.println("Superclasses of [NullPointerException.class]");
        Class<?> c = NullPointerException.class;
        while (c != null) {
            System.out.println("i: " + c);
            c = c.getSuperclass();
        }
    }
}
