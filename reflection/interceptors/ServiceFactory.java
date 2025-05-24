
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ServiceFactory {
    public static <T, R> R invoke(
        Class<? extends T> serviceClass, String methodName, Object... args
    ) {
        try {
            // Getting the instance of the service
            T service = serviceClass.getConstructor().newInstance();

            // Locating the service method
            Class<?>[] parameterClasses =
                Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
            Method method = serviceClass.getDeclaredMethod(methodName, parameterClasses);

            // locating the Intercept Annotation
            if (method.isAnnotationPresent(Intercept.class)) {
                Intercept intercept = method.getDeclaredAnnotation(Intercept.class);
                Class<? extends Interceptor<T, R>> interceptorClass =
                    (Class<? extends Interceptor<T, R>>) intercept.value();

                // creating an instance of interceptor
                Interceptor<T, R> interceptor = interceptorClass.getConstructor().newInstance();

                // intercepting the service method
                R returnedObj = interceptor.intercept(service, method, args);
                return returnedObj;
            } else {
                // invoking service method
                R returnedObj = (R) method.invoke(service, args);
                return returnedObj;
            }
        } catch (InstantiationException | IllegalAccessException |
                    InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
