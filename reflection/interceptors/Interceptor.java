
import java.lang.reflect.Method;

public interface  Interceptor<T, R> {
    R intercept(T interceptedObject, Method method, Object... args);
}
