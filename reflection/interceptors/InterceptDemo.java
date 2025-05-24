public class InterceptDemo {
    public static void main(String[] args) {
        
        String interceptedOutput = 
            ServiceFactory.invoke(SomeInterceptedService.class, "message", "Hello");
        System.out.println("Intercepted: " + interceptedOutput);

        String nonInterceptedOutput =
            ServiceFactory.invoke(SomeNonInterceptedService.class, "message", "Hello");
        System.out.println("Non Intercepted: " + nonInterceptedOutput);
    }
}
