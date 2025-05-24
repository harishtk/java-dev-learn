
import java.lang.reflect.Field;

public class FieldAccess {
    public static void main(String[] args) {
        User maria = new User("Maria");
        User jacob = new User("Jacob");

        try {
            Class<?> userClass = User.class;
            Field nameField = userClass.getDeclaredField("name");
            nameField.setAccessible(true);

            nameField.set(maria, "Maria (changed)");

            System.out.println("Name from maria: " + nameField.get(maria));
            System.out.println("Name from jacob: " + nameField.get(jacob));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}


