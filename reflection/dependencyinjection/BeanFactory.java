
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

public enum BeanFactory {
    INSTANCE;

    ConcurrentHashMap<Class<?>, Object> registry = new ConcurrentHashMap<>();

    public <T> T getInstanceOf(Class<T> beanClass, Object... args) {
        try {
            if (beanClass.isAnnotationPresent(Singleton.class)) {
                
                // Checking if the registry has an instance beanClass
                if (registry.containsKey(beanClass)) {
                    return (T) registry.get(beanClass);
                }

                // Creating a new instance of beanClass
                T bean = instantiateBeanClass(beanClass, args);

                // Adding this instance to the registry
                // putIfAbsent() is atomic in the case of ConcurrentHashMap
                registry.putIfAbsent(beanClass, bean);

                // Returning the value that landed in the map
                // It could be another one than yours
                return (T) registry.get(beanClass);
            } else {
                T bean = instantiateBeanClass(beanClass, args);
                return bean;
            }
        } catch (NoSuchMethodException | InvocationTargetException |
                InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> T instantiateBeanClass(Class<T> beanClass, Object... args)
        throws NoSuchMethodException, InstantiationException,
                IllegalAccessException, InvocationTargetException {
        // Creating an array of parameter types
        Class<?>[] parameterClasses =
            Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new);
        
        // Locating the corresponding constructor
        Constructor<T> beanConstructor = beanClass.getConstructor(parameterClasses);

        // Creating the bean
        T bean = beanConstructor.newInstance(args);

        // Getting the fields of this bean
        Field[] fields = beanClass.getDeclaredFields();

        // Filtering the fields that declare the @Inject annotation
        Field[] injectableFields = 
                Arrays.stream(fields)
                    .filter(field -> field.isAnnotationPresent(Inject.class))
                    .toArray(Field[]::new);
        
        for (Field injectableField : injectableFields) {
            // Getting the class of this field,
            // and creating instanvce of the class
            // using BeanFactory
            Class<?> fieldClass = injectableField.getType();
            Object fieldValue = BeanFactory.INSTANCE.getInstanceOf(fieldClass);

            // Setting this field to this value
            injectableField.setAccessible(true);
            injectableField.set(bean, fieldValue);
        }

        return bean;
    }   
}
