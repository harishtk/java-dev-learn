package annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class ScheduleTest {
    public static void main(String[] args) {
        System.out.println(
            Arrays.asList(ScheduleTest.class.getAnnotations())
        );

        // Discover annotations
        System.out.println(
            getAnnotatedMethods(ScheduleTest.class, Schedules.class)
        );
    }

    @Schedule(dayOfMonth = "last")
    @Schedule(dayOfWeek = "Tue", hourOfDay = 18)
    public void doCleanUp() {
        System.out.println("Cleaning up..");
        System.out.println("Cleaning done.");
    }

    public static List<Method> getAnnotatedMethods(
        final Class<?> type, final Class<? extends Annotation> annotation
    ) {
        final List<Method> methods = new ArrayList<>();
        Class<?> clazz = type;

        while (clazz != Object.class) {
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return methods;
    }
}
