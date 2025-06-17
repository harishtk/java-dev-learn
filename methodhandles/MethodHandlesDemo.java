
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;



public class MethodHandlesDemo {

    private static String MAGIC = "initial value static field";

    private String abc = "Initial value";

    public static void main(String[] args) {
        var app = new MethodHandlesDemo();
        app.methodTypes();
        app.accessingFields();
        app.workingWithArrays();
        app.exceptionHandling();
        app.typeTransformation();
    }

    public void methodTypes() {
        try {
            final MethodHandles.Lookup lookup = MethodHandles.lookup();

            // Virtual methods lookup
            MethodType replaceMethodType = MethodType.methodType(String.class, char.class, char.class);
            MethodHandle replaceMethodHandle = lookup.findVirtual(String.class, "replace", replaceMethodType);
            String result = (String) replaceMethodHandle.invokeExact("dummy", 'd', 'm');
            System.out.println("Replace method handle: invokeExact() " + result);

            String result2 = (String) replaceMethodHandle.invoke((Object) "dummy", (Object) 'd', (Object) 'm');
            System.out.println("Replace method handle: invoke() " + result2);

            String result3 = (String) replaceMethodHandle.invokeWithArguments("dummy", 'd', 'm');
            System.out.println("Replace method handle: invokeWithArguments() " + result3);

            // Static methods lookup
            MethodType valueOfMethodType = MethodType.methodType(String.class, Object.class);
            MethodHandle valueOfMethodHandle = lookup.findStatic(String.class, "valueOf", valueOfMethodType);
            System.out.println("Static method handle: " + valueOfMethodHandle);
            
        } catch (NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void accessingFields() {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();

            // Static field access
            MethodHandle setterStaticMethodHandle = lookup.findStaticSetter(MethodHandlesDemo.class, "MAGIC", String.class);
            MethodHandle getterStaticMethodHandle = lookup.findStaticGetter(MethodHandlesDemo.class, "MAGIC", String.class);

            setterStaticMethodHandle.invoke("new value static field");
            String staticFieldResult = (String) getterStaticMethodHandle.invoke();

            System.out.println("MAGIC = " + staticFieldResult);

            // Instance field access
            MethodHandle setterMethodHandle = lookup.findSetter(MethodHandlesDemo.class, "abc", String.class);
            MethodHandle getterMethodHandle = lookup.findGetter(MethodHandlesDemo.class, "abc", String.class);

            setterMethodHandle.invoke(this, "new value");
            String result = (String) getterMethodHandle.invoke(this);
            System.out.println("abc = " + result);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void workingWithArrays() {
        try {
            MethodHandle arrayConstructor = MethodHandles.arrayConstructor(String[].class);
            String[] arr = (String[]) arrayConstructor.invoke(5);

            MethodHandle elementSetter = MethodHandles.arrayElementSetter(String[].class);
            elementSetter.invoke(arr, 4, "test");

            System.out.println("arr: " + Arrays.toString(arr));

            MethodHandle elementGetter = MethodHandles.arrayElementGetter(String[].class);
            String element = (String) elementGetter.invoke(arr, 4);
            
            System.out.println("Element: " + element);

            MethodHandle arrayLength = MethodHandles.arrayLength(String[].class);
            int length = (int) arrayLength.invoke(arr);
            System.out.println("length = " + length);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public void exceptionHandling() {
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            MethodHandle methodHandle = lookup.findStatic(MethodHandlesDemo.class, "problematicMethod", MethodType.methodType(int.class, String.class));
            MethodHandle handler = lookup.findStatic(MethodHandlesDemo.class, "exceptionHandler", MethodType.methodType(int.class, IllegalArgumentException.class, String.class));
            MethodHandle wrapped = MethodHandles.catchException(methodHandle, IllegalArgumentException.class, handler);

            MethodHandle cleanupMethod = lookup.findStatic(MethodHandlesDemo.class, "cleanupMethod", 
                MethodType.methodType(int.class, Throwable.class, int.class, String.class));
            MethodHandle wrappedWithFinally = MethodHandles.tryFinally(wrapped, cleanupMethod);

            System.out.println(wrappedWithFinally.invoke("valid"));
            System.out.println(wrappedWithFinally.invoke("invalid"));

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (Throwable t) {
            throw new RuntimeException(t);
        } 
    }

    public void typeTransformation() {
        try {
            var lookup = MethodHandles.lookup();
            MethodHandle targetMethodHandle = lookup.findStatic(MethodHandlesDemo.class, "test", MethodType.methodType(String.class,  Object.class));
            MethodHandle adatper = targetMethodHandle.asType(
                MethodType.methodType(String.class, String.class)
            );

            String originalResult = (String) targetMethodHandle.invoke(1111);
            String adapterResult = (String) adatper.invoke("aaaa");

            System.out.println("originalResult: from(111) " + originalResult);
            System.out.println("adapterResult: from('aaaa') " + adapterResult);

            adapterResult = (String) adatper.invoke(1111);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static int problematicMethod(String arg) throws IllegalArgumentException {
        if ("invalid".equals(arg)) {
            throw new IllegalArgumentException();
        }
        return 1;
    }

    public static int exceptionHandler(IllegalArgumentException e, String arg) {
        // log exception.
        return 0;
    }

    public static int cleanupMethod(Throwable e, int result, String arg) {
        System.out.println("inside finally block");
        return result;
    }

    public static String test(Object arg) {
        return String.valueOf(arg);
    }
}
