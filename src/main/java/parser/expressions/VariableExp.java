package j2script.expressions;

import j2script.names.Variable;

public class VariableExp implements Exp {
    public final Variable var;

    public VariableExp(final Variable var) {
        this.var = var;
    }

    public String emit() {
        return var.toString();
    }
    public String toString() {
        return var + "";
    }
}