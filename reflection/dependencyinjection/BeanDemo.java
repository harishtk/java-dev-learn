

public class BeanDemo {
    public static void main(String[] args) {
        BeanFactory beanFactory = BeanFactory.INSTANCE;

        Message message1 = beanFactory.getInstanceOf(Message.class, "Hello");
        System.out.println("message1 = " + message1);

        Message message2 = beanFactory.getInstanceOf(Message.class, "world");
        System.out.println("message2 = " + message2);

        MyDatabase db1 = beanFactory.getInstanceOf(MyDatabase.class);
        MyDatabase db2 = beanFactory.getInstanceOf(MyDatabase.class);
        System.out.println("Instances of MyDatabase are same? " + (db1 == db2));

        System.out.println("Creating an instance of MyApplication through BeanFactory");
        MyApplication app = BeanFactory.INSTANCE.getInstanceOf(MyApplication.class);
        System.out.println("App has been created with DB service? " + app.isDbServiceSet());
    }
}
