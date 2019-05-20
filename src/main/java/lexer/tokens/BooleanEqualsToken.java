package j2script.tokens;

public class BooleanEqualsToken implements Token {
    public int hashCode() {
        return 2;
    }

    public boolean equals(Object obj) {
        return obj instanceof BooleanEqualsToken;
    }

    public String toString() {
        return "==";
    }
}
