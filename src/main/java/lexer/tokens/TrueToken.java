package j2script.tokens;

public class TrueToken implements Token {
    public int hashCode() {
        return 31;
    }

    public boolean equals(Object obj) {
        return obj instanceof TrueToken;
    }

    public String toString() {
        return "true";
    }
}
