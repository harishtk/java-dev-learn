
import java.util.Objects;

public record Message(String message) {
    public Message {
        Objects.requireNonNull(message);
    }
}
