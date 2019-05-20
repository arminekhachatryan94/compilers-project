package j2script.tokens;
public class VariableToken implements Token {
    public String name;

    public VariableToken(String name) {
        this.name = name;
    }

    public VariableToken(){
        
    }

    public int hashCode() {
        return 32;
    }

    public boolean equals(Object obj) {
        return obj instanceof VariableToken;
    }

    public String toString() {
        return "Variable";
    }
}
