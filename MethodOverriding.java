import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MethodOverriding {
    public static void main(String[] args) {
        Dog dog = new Dog("Buddy", 5);
        dog.makeSound();

        Animal proxyDog = (Animal) Proxy.newProxyInstance(
            Animal.class.getClassLoader(),
            new Class[]{Animal.class},
            new CustomDogInvocationHandler(dog)
        );

        System.out.println("Proxy dog: " + proxyDog);
        proxyDog.makeSound();
    }

    static class CustomDogInvocationHandler implements InvocationHandler {
        private final Object original;

        CustomDogInvocationHandler(Object original) {
            this.original = original;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals("makeSound")) {
                System.out.println("Meow!");
                return null;
            }
            return method.invoke(original, args);
        }
    }

    static interface Animal {
        void makeSound();
    }

    static class Dog implements Animal {
        private String name;
        private int age;

        Dog(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public void makeSound() {
            System.out.println("Woof!");
        }

        @Override
        public String toString() {
            return "Dog [name=" + name + ", age=" + age + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((name == null) ? 0 : name.hashCode());
            result = prime * result + age;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Dog other = (Dog) obj;
            if (name == null) {
                if (other.name != null)
                    return false;
            } else if (!name.equals(other.name))
                return false;
            if (age != other.age)
                return false;
            return true;
        }        
        
    }
}
