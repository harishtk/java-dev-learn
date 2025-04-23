public class ObjectCloning {
    public static void main(String[] args) {
        Cat mommyCat = new Cat("Mommy", 10);
        Dog dog1 = new Dog("Buddy", 5, mommyCat);
        Dog dog2 = dog1.clone();
        
        System.out.println(dog1);
        dog1.age = 10;
        mommyCat.age = 20;
        System.out.println(dog1);
        System.out.println(dog2);
        System.out.println(dog1 == dog2);

        System.out.println("Is cat clonable: " + (mommyCat instanceof Cloneable));
        System.out.println("Is dog clonable: " + (dog1 instanceof Cloneable));
        // Cat cloned = (Cat) mommyCat.clone();
    }

    static class Cat implements Cloneable {
        String name;
        int age;
        Cat(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void makeSound() {
            System.out.println("Meow!");
        }

        public Cat clone() {
            try {
                return (Cat) super.clone();
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        @Override
        public String toString() {
            return "Cat [name=" + name + ", age=" + age + "]";
        }
    }

    static class Dog implements Cloneable {
        String name;
        int age;
        Cat parent;
        Dog(String name, int age) {
            this(name, age, null);
        }

        Dog(String name, int age, Cat parent) {
            this.name = name;
            this.age = age;
            this.parent = parent;
        }

        public void makeSound() {
            System.out.println("Woof!");
        }

        public Dog clone() {
            try {
                Dog clonedDog = (Dog) super.clone();
                if (parent != null) {
                    clonedDog.parent = parent.clone();
                }
                return clonedDog;
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        @Override
        public String toString() {
            return "Dog [name=" + name + ", age=" + age + ", parent=" + parent + "]";
        }

    }
}
