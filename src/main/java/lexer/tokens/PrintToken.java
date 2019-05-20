package j2script.tokens;
public class PrintToken implements Token {
    public int hashCode() {
        return 23;
    }

    public boolean equals(Object obj) {
        return obj instanceof PrintToken;
    }

    public String toString() {
        return "println";
    }
}
