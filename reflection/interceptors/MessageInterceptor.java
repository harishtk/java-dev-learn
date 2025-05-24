
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

public class MessageInterceptor implements Interceptor<SomeInterceptedService, String> {

    @Override
    public String intercept(SomeInterceptedService interceptedObject, Method method, Object... args) {
        try {
            if (args.length == 1) {

                // validate arguments 
                String input = (String) args[0];
                Objects.requireNonNull(input, "Input is null");
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Input is empty");
                }

                // calling the service method
                String resultObj = (String) method.invoke(interceptedObject, input);
                return resultObj + " [was intercepted]";
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        throw new IllegalArgumentException(
            "Arguments should contain exactly one argument type of String"
        );
    }
    
}
