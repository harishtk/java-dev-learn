import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Objects;

public class ReadingAnnotations {
    public static void main(String[] args) {
        var app = new ReadingAnnotations();
        app.discoverAnnotations();
        app.repeatingAnnotations();
        app.obtainingAnnotationTypes();
    }

    public void discoverAnnotations() {
        Class<?> c = Person.class;
        
        boolean isBean = c.isAnnotationPresent(Bean.class);
        System.out.println("isBean = " + isBean);

        Annotation[] annotations = c.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("annotation = " + annotation);
        }

        Serialized serializedAnnotation = c.getAnnotation(Serialized.class);
        SerializedFormat format = serializedAnnotation.format();
        System.out.println("format = " + format);
    }

    public void repeatingAnnotations() {
        try {
            Class<?> personClass = Person.class;
            Field nameField = personClass.getDeclaredField("name");

            Annotation[] annotations = nameField.getAnnotations();
            System.out.println("# annotations = " + annotations.length);

            Validators validators = (Validators) annotations[0];
            for (Validator validator : validators.value()) {
                System.out.println("validator = " + validator);
            }

            // Alternate approach (recommended)
            Annotation[] annotations1 = nameField.getAnnotationsByType(Validator.class);
            System.out.println("# annotations = " + annotations1.length);

            for (Annotation annotation : annotations1) {
                System.out.println("annotation = " + annotation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void obtainingAnnotationTypes() {
        Class<?> c = SubPerson.class;

        AnnotatedType superClass = c.getAnnotatedSuperclass();
        Annotation[] superClassAnnotations = superClass.getAnnotations();
        for (Annotation annotation : superClassAnnotations) {
            System.out.println("annotation on super class = " + annotation);
        }

        AnnotatedType[] interfaces = c.getAnnotatedInterfaces();
        for (AnnotatedType implementedInterface : interfaces) {
            Annotation[] interfaceAnnotations = implementedInterface.getAnnotations();
            for (Annotation interfaceAnnotation : interfaceAnnotations) {
                System.out.println("annotation on implemented interface = " + interfaceAnnotation);
            }
        }

        System.out.println();
        try {
            Message m = new Message("");
        } catch (EmptyStringException e) {
            try {
                Class<?> messageClass = Message.class;
                // Since Message is a inner class, it requires enclosing class as its first parameter
                Constructor<?> constructor = messageClass.getConstructor(ReadingAnnotations.class, String.class);

                AnnotatedType[] annotatedExceptionTypes = constructor.getAnnotatedExceptionTypes();
                for (AnnotatedType annotatedExceptionType : annotatedExceptionTypes) {
                    boolean isAnnotationPresent =
                        annotatedExceptionType.isAnnotationPresent(ReThrowAsRuntimeException.class);
                    if (isAnnotationPresent) {
                        throw new RuntimeException(e);
                    }
                }
                System.out.println(e.getMessage());
                e.printStackTrace();
            } catch (NoSuchMethodException nsme) {
                nsme.printStackTrace();
            }
        }
    }
    
    public class SubPerson 
        extends @NotNull Person
        implements @NotNull Serializable {}

    @Serialized @Bean
    public class Person {

        @Validator(ValidationRules.NON_NULL)
        @Validator(ValidationRules.NON_EMPTY)
        private String name;
    }

    enum SerializedFormat { BINARY, XML, JSON; }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface Bean {}

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Serialized {
        SerializedFormat format() default SerializedFormat.JSON;
    }

    enum ValidationRules {
        NON_NULL, NON_EMPTY, NON_ZERO;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Validators {
        Validator[] value();
    }

    @Target({ElementType.FIELD})
    @Repeatable(Validators.class)
    @interface Validator {
        ValidationRules value();
    }


    @Target({ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface  NotNull {}

    class EmptyStringException extends Exception {
        public EmptyStringException(String message) {
            super(message);
        }
    }

    @Target({ElementType.TYPE_USE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ReThrowAsRuntimeException {}

    public class Message {
        private final String name;

        public Message(String name) throws @ReThrowAsRuntimeException EmptyStringException {
            Objects.requireNonNull(name);
            if (name.isEmpty()) {
                throw new EmptyStringException("name is empty");
            }
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
