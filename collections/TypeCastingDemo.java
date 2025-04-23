package collections;

import java.util.ArrayList;
import java.util.List;

/**
 * No, JVM's virtual method invocation does **not** mean that it is using
 * reflection to call the appropriate method at runtime. Virtual method
 * invocation is a core feature of the JVM and is implemented using a more
 * efficient mechanism than reflection.
 *
 * ### Virtual Method Invocation vs. Reflection
 *
 * 1. **Virtual Method Invocation**:
 *    - **Mechanism**: Virtual method invocation is a fundamental part of the
 *      JVM's method dispatch process. When a virtual method is called, the JVM
 *      uses the object's vtable (virtual method table) to determine the correct
 *      method to execute at runtime.
 *    - **Performance**: It is highly optimized and very fast because the JVM
 *      directly looks up the method in the vtable.
 *    - **Usage**: It is used for regular method calls in object-oriented
 *      programming, where the actual method to be called depends on the runtime
 *      type of the object.
 *
 * 2. **Reflection**:
 *    - **Mechanism**: Reflection involves inspecting and manipulating classes,
 *      interfaces, fields, and methods at runtime. It uses the
 *      `java.lang.reflect` package to access and invoke methods dynamically.
 *    - **Performance**: It is slower than virtual method invocation because it
 *      involves searching for the method, checking access permissions, and then
 *      invoking the method.
 *    - **Usage**: It is used for dynamic programming, such as creating objects,
 *      calling methods, and accessing fields at runtime, without knowing the
 *      class details at compile time.
 *
 * ### JVM's Virtual Method Invocation
 *
 * 1. **Vtable (Virtual Method Table)**:
 *
 * 3. **Example**:
 *    ```java
 *    interface Animal {
 *        void makeSound();
 *    }
 *
 *    class Dog implements Animal {
 *        @Override
 *        public void makeSound() {
 *            System.out.println("Woof!");
 *        }
 *    }
 *
 *    class Cat implements Animal {
 *        @Override
 *        public void makeSound() {
 *            System.out.println("Meow!");
 *        }
 *    }
 *
 *    public class VirtualMethodInvocationDemo {
 *        public static void main(String[] args) {
 *            Animal animal1 = new Dog();
 *            Animal animal2 = new Cat();
 *
 *            animal1.makeSound(); // Calls Dog's makeSound method
 *            animal2.makeSound(); // Calls Cat's makeSound method
 *        }
 *    }
 *    ```
 *    - In this example, the JVM uses virtual method invocation to call the
 *      correct `makeSound` method based on the actual type of the object
 *      (`Dog` or `Cat`).
 *
 * ### Key Differences
 *
 * | Feature                | Virtual Method Invocation | Reflection                               |
 * | ---------------------- | ------------------------- | ---------------------------------------- |
 * | **Mechanism**          | Vtable lookup             | Dynamic method lookup and invocation     |
 * | **Performance**        | Fast                      | Slower                                   |
 * | **Compile-Time Safety** | Yes                       | No (can lead to runtime exceptions)      |
 * | **Usage**              | Regular method calls       | Dynamic programming, runtime
 * manipulation |
 *
 * ### Conclusion
 *
 * Virtual method invocation is a highly optimized mechanism used by the JVM to
 * call the correct method at runtime based on the object's actual type. It is
 * much faster and more efficient than reflection, which is used for dynamic
 * programming and runtime manipulation of classes and objects.
 *  
 */
public class TypeCastingDemo {
    public static void main(String[] args) {
        List<String> strings = new ArrayList<>();

        strings.add("one");
        strings.add("two");
        strings.add("three");

        System.out.println("strings = " + strings + " is a type of " +
            strings.getClass().getName());
        if (strings instanceof ArrayList) {
            System.out.println("strings is an ArrayList implementation!");
        }
    }
}
