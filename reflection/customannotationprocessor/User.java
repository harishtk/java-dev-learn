
import java.util.Objects;

public class User {
    @NonNull private String name;
    User(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        Objects.requireNonNull(name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User[name=" + name + "]";
    }
}