
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodInvocation {
    public static void main(String[] args) {
        final var app = new MethodInvocation();
        app.locatingMethods();
        app.obtainReturnTypes();
        app.obtainExceptionTypes();
        app.obtainMethodModifiers();
        app.invokingMethods();
        app.gettingThrownExceptions();
        app.obtainConstructors();
        app.workingWithArrays();
        app.workingWithEnums();
    }

    public void locatingMethods() {
        Class<?> c = User.class;

        System.out.println("Methods -->");
        for (Method method : c.getMethods()) {
            System.out.println("method = " + method);
        }

        System.out.println("Declared Methods -->");
        for (Method method : c.getDeclaredMethods()) {
            System.out.println("method = " + method);
        }
    }

    public void obtainReturnTypes() {
        try {
            Class<?> listClass = List.class;
            Method getWithIndex = listClass.getMethod("get", int.class);

            Class<?> returnType = getWithIndex.getReturnType();
            System.out.println("Return type = " + returnType);

            Type genericReturnType = getWithIndex.getGenericReturnType();
            System.out.println("Generic return type = " + genericReturnType);

            Method listOf = listClass.getMethod("of", Object.class);

            Class<?> returnType1 = listOf.getReturnType();
            System.out.println("Return type = " + returnType1);

            Type genericReturnType1 = listOf.getGenericReturnType();
            System.out.println("Generic return type = " + genericReturnType1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void obtainExceptionTypes() {
        try {
            Class<?> constructorClass = Constructor.class;
            Method newInstance = constructorClass.getMethod("newInstance", Object[].class); 

            var exceptionTypes = newInstance.getExceptionTypes();
            System.out.println("exceptionTypes:");
            for (var exceptionType : exceptionTypes) {
                System.out.println("  exceptionType = " + exceptionType);
            }

            var genericExceptionTypes = newInstance.getGenericExceptionTypes();
            System.out.println("genericExceptionType:");
            for (var exceptionType : genericExceptionTypes) {
                System.out.println("  exceptionType = " + exceptionType);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void obtainMethodModifiers() {
        try {
            Class<?> stringClass = String.class;
            Method join = stringClass.getMethod("join", CharSequence.class, Iterable.class);

            int modifiers = join.getModifiers();
            System.out.println("isStatic? " + Modifier.isStatic(modifiers));
            System.out.println("isPublic? " + Modifier.isPublic(modifiers));

            // Supported from Java 21
            // Set<AccessFlag> accessFlags = join.accessFlags();
            // System.out.println("Access flags: " + accessFlags);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void invokingMethods() {
        try {
            String s = "Hello!";

            Class<?> stringClass = String.class;
            Method lengthMethod = stringClass.getMethod("length");

            int length = (int) lengthMethod.invoke(s);
            System.out.println("Length = " + length);

            String s2 = "Hello, World!";
            Method substringMethod = stringClass.getMethod("substring", int.class, int.class);
            String substring = (String) substringMethod.invoke(s2, 6, 12);
            System.out.println("substring = " + substring);
        } catch (NoSuchMethodException|IllegalAccessException|InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void gettingThrownExceptions() {
        try {
            Path doesNotExist = Path.of("/doesntexist");

            Class<?> filesClass = Files.class;
            Method newBufferedWriterMethod = filesClass
                .getMethod("newBufferedWriter", Path.class, OpenOption[].class);
            
            try (Writer writer = (Writer) newBufferedWriterMethod.invoke(null, doesNotExist, new OpenOption[0]);) {
                writer.write("Hello");
            }
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            System.out.println("e.getTargetException() = " + targetException);
            System.out.println("Stack trace: ");
            targetException.printStackTrace();;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void obtainConstructors() {
        try {
            Class<?> stringClass = Class.forName("java.lang.String");
            Constructor<?>[] constructors = stringClass.getDeclaredConstructors();

            for (Constructor<?> constructor : constructors) {
                System.out.println("constructor = " + constructor);
            }

            Class<?> arrayListClass = ArrayList.class;
            Constructor<?>[] constructors1 = arrayListClass.getConstructors();
            for (Constructor<?> constructor : constructors1) {
                System.out.println("constructor with " + constructor.getParameterCount() + " parameters");
                for (Class<?> type : constructor.getParameterTypes()) {
                    System.out.println("  " + type.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void workingWithArrays() {
        try {
            Class<?> stringClass = String.class;
            Field valueField = stringClass.getDeclaredField("value");

            Class<?> fieldType = valueField.getType();
            boolean isArray = fieldType.isArray();
            System.out.println("isArray? " + isArray);

            Class<?> elementType = fieldType.getComponentType();
            System.out.println("elementType = " + elementType);

            // Creating new arrays
            int length = 10;
            Object o = Array.newInstance(int.class, 10);

            boolean isArray2 = o.getClass().isArray();
            System.out.println("isArray? " + isArray2);

            Class<?> componentType = o.getClass().getComponentType();
            System.out.println("componentType = " + componentType);

            int reflectiveLength = Array.getLength(o);
            System.out.println("reflectiveLength = " + reflectiveLength);

            System.out.println(Arrays.toString((int[]) o));
            for (int i = 0; i < reflectiveLength; i++) {
                Array.set(o, i, 2 * i);
            }
            System.out.println(Arrays.toString((int[]) o));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public void workingWithEnums() {
        Class<Days> c = Days.class;

        Constructor<?>[] constructors = c.getDeclaredConstructors();
        int length = constructors.length;
        System.out.println("# constructors = " + length);

        // Retrieving compiler generated fields.
        Field[] fields = c.getDeclaredFields();
        
        try {
            for (Field field : fields) {
                System.out.println(field);
                if (Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    if (field.getType().isArray()) {
                        System.out.println("  " + Arrays.toString((Object[]) field.get(null)));
                        System.out.println("  is enum constant? " + field.isEnumConstant());
                    } else {
                        System.out.println("  " + field.get(null));
                        System.out.println("  is enum constant? " + field.isEnumConstant());
                    }
                }
            }

            // Discover compiler generated methods.
            Method[] methods = c.getDeclaredMethods();

            System.out.println("Declared Methods -->");
            for (Method method : methods) {
                System.out.println("  " + method);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public enum Days {
        SATURDAY("Saturn"), SUNDAY("Sun");
        
        private final String planet;

        Days(String planet) {
            this.planet = planet;
        }

        public static Days of(String label) {
            return Arrays.stream(values())
                .filter(day -> day.planet.equals(label))
                .findAny().orElseThrow();
        }

        public String planet() {
            return this.planet;
        }
    }
}
