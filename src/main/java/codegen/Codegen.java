package j2script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import j2script.expressions.*;
import j2script.statements.*;
import j2script.types.ClassType;
import j2script.names.*;
import j2script.VTableClassTable;
import j2script.declarations.*;
import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;

public class Codegen{
    //Code generated will be inserted into the list
    private final List<String> Code;
    private Map<ClassName, ClassDef> classes;
    //The actual vtables per class
    private List<VTableClassTable> listofVtables;
    private Map<ClassName, VTableClassTable> compmap;
    private HashMap<MethodName, Integer> offsets;
    private HashMap<Variable, Exp> variables;


    //Associate instantiated object names to classes for method calls
    private HashMap<String, ClassName> objToClass;
    //

    // private final Map<ClassName, List<MethodName>> vtableMethodOffsets;
    public Codegen(){
        Code = new ArrayList<String>();
        classes = new HashMap<ClassName,ClassDef>();
        listofVtables = new ArrayList<>();
        compmap = new HashMap<ClassName, VTableClassTable>();
        offsets = new HashMap<MethodName, Integer>();
        variables = new HashMap<Variable,Exp>();
        objToClass = new HashMap<String,ClassName>();
    }
    public void compileExp(Exp exp){
        if (exp instanceof BinopExp){
            compileBinOpExp((BinopExp)exp);
        }
        else if (exp instanceof NumberExp){
            compileNumberExp((NumberExp)exp);
        }
        else if (exp instanceof ClassExp){
            compileClassExp((ClassExp)exp);
        }
        else if (exp instanceof VarMethodExp){
            compileVarMethodExp((VarMethodExp)exp);
        }
        else if (exp instanceof BoolExp){
            compileBoolExp((BoolExp)exp);
        }
    }
    public void compileProgram(Program prog){
        for (ClassDef cls : prog.classDefs) {
            classes.put(cls.name,cls);
        }
        for (ClassDef cls : prog.classDefs) {
            compileClass(cls);
        }
        if (prog.statement instanceof Block){
            // System.out.println("Working it is");
            Block b = (Block) prog.statement;
            for(Statement stmt: b.statements){
                compileStatement(stmt);
            }
            // System.out.println("Working it");
        }
        else{
            // System.out.println("Working");

            compileStatement(prog.statement);
        }
        // System.out.println("this is the endof the program");
        for(String item : Code){
            // System.out.println(item);
        }
        
    }
    public void compileParentclass(ClassDef cls){
        //We know this is the top of the ladder, print the methods out as is.
        int count = 0;
        Map<MethodName, MethodDef> methodMap = new HashMap<>();
        Map<MethodName, Integer> offsets = new HashMap<>();
        List<String> vTable = new ArrayList<>(); 
        String vtable = "var " + cls.name.toString() + "_" + "vtable = [";
        for (MethodDef md : cls.methodDefs) {
            String method = "var " + cls.name.toString() + "_" + md.name.toString() + " = function(" ;
            if (md.varDecs.size() != 0){
                String params= "";
                for (int i = 0; i < md.varDecs.size();i++) {
                    params += md.varDecs.get(i).toString();
                    params += ",";
                }
                method += params;

                method +="self) {";
                Code.add(method);
            }
            else{
                method +="self) {";
                Code.add(method);
            }
            if ( md.body instanceof Block){
                Block b = (Block) md.body;
                for (int i =0 ; i < b.statements.size();i++){
                    compileStatement(b.statements.get(i));
                }
            }
            else{
                compileStatement(md.body);
            }
            method = "};";
            Code.add(method);
            methodMap.put(md.name, md);
            offsets.put(md.name, count);
            vTable.add(cls.name.toString() + "_" + md.name.toString());
            vtable += cls.name.toString() + "_" + md.name.toString();
            if (count + 1 < cls.methodDefs.size()){
                vtable += ", ";
            }
            count++;
        }
        vtable += "];";
        Code.add(vtable);        
        VTableClassTable v = new VTableClassTable(vTable,cls, methodMap, offsets);
        compmap.put(cls.name, v);
    }
    public void compileChildClass(ClassDef cls){
        Map<MethodName, MethodDef> methodMap = new HashMap<>();
        Map<MethodName, Integer> offsets = new HashMap<>();
        //Inherits parents stuff
        VTableClassTable parent = compmap.get(cls.extendedClass.extendsName);
        List<String> vTable = new ArrayList<>(parent.vTable.size()+1); 

        // System.out.println("This classes parent is" + cls.extendedClass.extendsName);
        // System.out.println("This classes parent is" + parent.theClass.name);

        // System.out.println("This class is" + cls.name);
        methodMap.putAll(parent.methodMap);
        // System.out.println(methodMap.toString());
        offsets.putAll(parent.offsets);
        // System.out.println(parent.vTable.toString());
        // System.out.println(vTable.toString());
        for (String s : parent.vTable) {
            vTable.add(s);
        }
        // System.out.println(vTable.toString());
        // System.out.println("This classes parents vtable is " + vTable.toString());

        int count = parent.vTable.size();

        String vtable = "var " + cls.name.toString() + "_" + "vtable = [";
        for (MethodDef md : cls.methodDefs) {
            String method = "var " + cls.name.toString() + "_" + md.name.toString() + " = function(" ;
            if (md.varDecs.size() != 0){
                String params= "";
                for (int i = 0; i < md.varDecs.size();i++) {
                    params += md.varDecs.get(i).toString();
                    params += ",";
                }
                method += params;
                method +="self) {\n\t" + md.body.toString() + "};";

            }
            else{
                method +="self) {\n\t" + md.body.toString() + "};";
            }
            // String method = "var " + cls.name.toString() + "_" + md.name.toString() + " = function(self) {\n\t" + md.body.toString() + "};" ;
            // System.out.println("hello the method checked is " + method);
            Code.add(method);
            //check if method is being overridden
            if (methodMap.get(md.name) != null){
                //Check if it is being overloaded
                if(methodMap.get(md.name).varDecs.equals(md.varDecs)){
                    int j=0;
                    // System.out.println("I am here for this class" + cls.name.toString());
                    // System.out.println("I am here for this method" + md.name.toString());
                    // System.out.println("BY the way the exclass is " + cls.extendedClass.extendsName.toString());
                    // String exclass = cls.extendedClass.name.toString();                  
                    //replace in childs vtable
                    methodMap.replace(md.name, md);
                    for (int i = 0 ; i < vTable.size(); i++) {
                        String s =vTable.get(i);
                        int t = s.indexOf("_") + 1;
                        String helper = s.substring(t, s.length());
                        // System.out.println(helper + " and " + md.name.toString());
                        if (helper.equals(md.name.toString())){
                            // System.out.println("I have reached here for: " + s );
                            vTable.set(i,cls.name.toString() + "_" +md.name.toString());
                            j=i;
                        }
                    }
                    offsets.replace(md.name, j);
                }
                else{
                    // System.out.println("Hello i am not being overridden");
                    methodMap.put(md.name, md);
                    offsets.put(md.name, count);
                    vTable.add(cls.name.toString() + "_" + md.name.toString());
                    count++;
                }
            }
            //Its not being overridden, add it to the vtable.
            else{
                // System.out.println("Hello i am not being overridden");
                methodMap.put(md.name, md);
                offsets.put(md.name, count);
                vTable.add(cls.name.toString() + "_" + md.name.toString());
                count++;
            }

        }
        int k =0;
        for (String s : vTable) {
            vtable += s;
            if (k + 1 < vTable.size()){
                vtable += ", ";
            }
            k++;
        }
        vtable += "];";
        // System.out.println("Class " + cls.name.toString() + " has this vtable " + vtable.toString());
        Code.add(vtable);    
        VTableClassTable v = new VTableClassTable(vTable,cls, methodMap, offsets);
        compmap.put(cls.name, v);
    }
    public void compileClass(ClassDef cls){
        //If doesnt extend and hasnt been compiled, compile it.
        // System.out.println("Class " + cls.name.toString() + 
        // "extended = " + (cls.extendedClass == null) + (compmap.get(cls.name) == null));
        if (cls.extendedClass == null && compmap.get(cls.name) == null){
            compileParentclass(cls);
        }
        else{
            //FIND PARENT AND COMPILE IT IF IT HASNT BEEN SO
            // System.out.println("This is the result of compmap ");
            // System.out.println("This is the result of compmap " +
            // compmap.get(cls.extendedClass.extendsName) == null);
            if (compmap.get(cls.extendedClass.extendsName) == null){
                compileClass(classes.get(cls.extendedClass.extendsName));
            }
            //Now compile the extended class itself
            compileChildClass(cls);
        }
    }
    public void compileStatement(Statement stmt){
        // System.out.println("in compile statement");
        if (stmt instanceof IfStatement){
            compileIfStmt((IfStatement)stmt);
        }
        else if (stmt instanceof WhileStatement){
            compileWhileStmt((WhileStatement)stmt);
        }
        else if (stmt instanceof Block){
            Block b = (Block) stmt;
            for (int i=0; i < b.statements.size(); i++){
                compileStatement(b.statements.get(i));
            }
        }
        else if (stmt instanceof BreakStatement){
            BreakStatement bs = (BreakStatement) stmt;
            Code.add(bs.toString() + ";");
        }
        else if (stmt instanceof PrintStatement){
            PrintStatement ps = (PrintStatement) stmt;
            Code.add("console.log(" + ps.exp.emit() + ");");
        }
        else if (stmt instanceof ReturnExpStatement){
            ReturnExpStatement re= (ReturnExpStatement) stmt;
            Code.add("return " + re.exp.emit() + ";");
        }
        else if (stmt instanceof ReturnVoidStatement){
            Code.add("return;");
        }
        else if (stmt instanceof VarAssignment){
            compilevarassign((VarAssignment)stmt);
            
        }
        else if (stmt instanceof VarDecAssignment){
            // System.out.println("in vardec assignment");
            compilevarDecAssign((VarDecAssignment)stmt);
        }
        else{
            Exp e = (Exp)stmt;
            compileExp(e);
        }
    }
    // public String vardecHelper(VTableClassTable vt, String actualCode){
    //     //the class does have parameters, instantiate them
    //     //Check if its multiple statements
    //     if (vt.theClass.constructor.body instanceof Block){
    //         Block b = (Block) vt.theClass.constructor.body;
    //         for (int i=0; i < b.statements.size();i++){
    //             Statement s = b.statements.get(i);
    //             if (s instanceof VarAssignment){
    //                 VarAssignment va = (VarAssignment) s;
    //                 //If the child constructor has instantiated a field, dont instantiate parents. Otherwise, instantiate it
    //                 if (variables.get(va.variable) != null){
    //                     //Do nothing
    //                 }
    //                 else{
    //                     actualCode += ",\n\t" + va.variable.toString() + ": " + va.exp.emit();
    //                 }
    //             }
    //         }
    //         // actualCode += "\n}";
    //     }
    //     else{
    //         Statement s = vt.theClass.constructor.body;
    //         if (s instanceof VarAssignment){
    //             VarAssignment va = (VarAssignment) s;
    //                 //If the child constructor has instantiated a field, dont instantiate parents. Otherwise, instantiate it
    //                 if (variables.get(va.variable) != null){
    //                     //Do nothing
    //                 }
    //                 else{
    //                     //TODO: Check parameters of constructor and put appropriate parameter value into instantiation
    //                     actualCode += ",\n\t" + va.variable.toString() + ": " /*+ va.exp.emit()*/;
    //                 }
    //             // actualCode += "\n}";
    //         }
    //     }
    //     return actualCode;
    // }
    public void compilevarassign(Statement s){
        VarAssignment va = (VarAssignment) s;
        if ( va.exp instanceof VarMethodExp){
            String actualCode = "var " + va.variable.toString() + " = ";
            VarMethodExp e = (VarMethodExp)va.exp;
            //get the index of the offset in the vtable
            String var = e.var.toString();
            ClassName cn = objToClass.get(var);
            VTableClassTable vt = compmap.get(cn);
            int offs = vt.offsets.get(e.methodName);
            String params = "";
            for (int i = 0; i < e.parameters.size(); i++){
                params += e.parameters.get(i).emit();
                params += ", ";
            }
    
            actualCode += var + "." + "vtable[" + offs + "](" + params + var + ");";
            Code.add(actualCode);
        }
        else{
            String ActualCode = va.variable.toString() + " = " +va.exp.emit();
            Code.add(ActualCode);
        }

    }
    public void compileobjHelper(Statement s, VTableClassTable vt, Map<String,String> varstostrings, VarDecAssignment v){
        // if (s instanceof VarAssignment){
        //     boolean hasFound = false;
        //     VarAssignment va = (VarAssignment) s;
        //     VarDecAssignment temp = v;
        //     v = new VarDecAssignment(temp.varDec,(ClassExp)temp.exp);
        //     //Now check va.exp.emit() compared to parameter, if equal substitute with actual param and write method and save to map from varstostrings
        //     for (int i = 0; i < vt.theClass.constructor.parameters.size(); i++){
        //         if (vt.theClass.constructor.parameters.get(i).var.toString() == va.exp.emit()){
        //             String actualCode = ",\n\t" + va.variable.toString() + ": " + v.exp.parameters.get(i).emit();
        //             varstostrings.put(va.variable.toString(), actualCode);
        //             i = vt.theClass.constructor.parameters.size();
        //             hasFound = true;
        //         }
        //     }
        //     if (hasFound ==false){
        //         String actualCode = ",\n\t" + va.variable.toString() + ": " + va.exp.emit();
        //         varstostrings.put(va.variable.toString(), actualCode);
        //     }
        //     // actualCode += "\n}";
        // }
        // else if (s instanceof SuperStatement){
        //     //Compile parent
        //     VTableClassTable parent = compmap.get(vt.theClass.extendedClass.name);
        //     varstostrings = compileObj(parent, varstostrings, v);
        // }
    }
    public Map<String,String> compileObj(VTableClassTable vt, Map<String,String> varstostrings, List<Exp> actualparams){
        //Just run through the constructor
        // System.out.println("start of compileobj");
        // System.out.println(vt.theClass);
        if (vt.theClass.constructor.body instanceof Block){
            // System.out.println("start of compileobj if");
            Block b = (Block) vt.theClass.constructor.body;
            for (int j=0; j < b.statements.size();j++){
                Statement s = b.statements.get(j);
                if (s instanceof SuperStatement){
                    SuperStatement ss = (SuperStatement) s;
                    // List<Exp> superparams = ss.exp;
                    VTableClassTable parent = compmap.get(vt.theClass.extendedClass.extendsName);
                    varstostrings=compileObj(parent, varstostrings, actualparams);
                }
                else if (s instanceof VarAssignment){
                    boolean hasFound = false;
                    VarAssignment va = (VarAssignment) s;
                    //Now check va.exp.emit() compared to parameter, if equal substitute with actual param and write method and save to map from varstostrings
                    for (int i = 0; i < vt.theClass.constructor.parameters.size(); i++){
                        if (vt.theClass.constructor.parameters.get(i).var.toString() == va.exp.emit()){
                            String actualCode = ",\n\t" + va.variable.toString() + ": " + actualparams.get(i).emit();
                            varstostrings.put(va.variable.toString(), actualCode);
                            i = vt.theClass.constructor.parameters.size();
                            hasFound = true;
                        }
                    }
                    if (hasFound ==false){
                        String actualCode = ",\n\t" + va.variable.toString() + ": " + va.exp.emit();
                        varstostrings.put(va.variable.toString(), actualCode);
                    }
                    // actualCode += "\n}";
                }
    
                // compileobjHelper(s, vt, varstostrings, v);
            }
            // actualCode += "\n}";
        }
        else{
            Statement s = vt.theClass.constructor.body;
            // System.out.println("start of compileobj else");
            if (s instanceof SuperStatement){
                // System.out.println("start of seccompileobj else");

                SuperStatement ss = (SuperStatement) s;
                // List<Exp> superparams = ss.exp;
                VTableClassTable parent = compmap.get(vt.theClass.extendedClass.extendsName);
                // System.out.println("The parents name is " + vt.theClass.extendedClass.extendsName.toString());
                // System.out.println(parent != null);
                varstostrings=compileObj(parent, varstostrings, actualparams);
            }
            else if (s instanceof VarAssignment){
                boolean hasFound = false;
                VarAssignment va = (VarAssignment) s;
                // System.out.println("this worked");
                //Now check va.exp.emit() compared to parameter, if equal substitute with actual param and write method and save to map from varstostrings
                for (int i = 0; i < vt.theClass.constructor.parameters.size(); i++){
                    if (vt.theClass.constructor.parameters.get(i).var.toString() == va.exp.emit()){
                        String actualCode = ",\n\t" + va.variable.toString() + ": " + actualparams.get(i).emit();
                        varstostrings.put(va.variable.toString(), actualCode);
                        i = vt.theClass.constructor.parameters.size();
                        hasFound = true;
                    }
                }
                // System.out.println("this worked after for loop");
                if (hasFound ==false){
                    String actualCode = ",\n\t" + va.variable.toString() + ": " + va.exp.emit();
                    varstostrings.put(va.variable.toString(), actualCode);
                }
                // actualCode += "\n}";
            }
            // compileobjHelper(s, vt, varstostrings, v);
        }
        // System.out.println("this worked");
        
        return varstostrings;
    }
    public void compilevarDecAssign(Statement stmt){
        //Assuming this type checks, check if it is a class type and if so then create a json with the appropriate fields.
        VarDecAssignment v = (VarDecAssignment)stmt;
        // System.out.println("in vardecassign");
        if (v.varDec.type instanceof ClassType && v.exp instanceof ClassExp){
            // System.out.println("in vardecassign for " + v.varDec.type.toString() + v.varDec.var.toString() );
            //Associate a variable to its string value incase you have to replace with child 
            Map<String, String> varstostrings = new HashMap<String,String>();
            //
            ClassExp c = (ClassExp)v.exp;
            List<Exp> params = c.parameters;
            ClassName cname = new ClassName(v.varDec.type.toString());
            // System.out.println("name of class is " + cname.toString());
            for (Map.Entry<ClassName, VTableClassTable> item : compmap.entrySet()) {
                // System.out.println(item.getKey().toString());
            }
            objToClass.put(v.varDec.var.toString(),cname);
            String actualCode = "var " + v.varDec.var.toString() + " = {\n\tvtable: " + cname.toString() + "_vtable";
            VTableClassTable vt = compmap.get(cname);
            // System.out.println(vt);
            
            varstostrings = compileObj(vt, varstostrings, params);
            // System.out.println("this worked" );

            for (Map.Entry<String, String> item : varstostrings.entrySet()) {
                actualCode += item.getValue();
            }
            actualCode += "\n}";
            //Object has been instantiated, vtable first, then parent instantiated stuff then child instantiated stuff
            Code.add(actualCode);
            // //Check if it extends, were assuming super has been checked and exists. If it does extend, run parents constructor then do childs
            // if (vt.theClass.extendedClass != null){
            //     VTableClassTable parent = compmap.get(vt.theClass.extendedClass.name);
            //     actualCode += vardecHelper(vt, actualCode);
            //     actualCode += vardecHelper(parent, actualCode);
            //     actualCode += "\n}";
            //     //Clear the variables hashmap once were done
            //     for (Map.Entry<Variable, Exp> item : variables.entrySet()) {
            //         variables.remove(item);
            //     }

            // }
            // //If it doesnt extend, check the parameters
            // else{
            //     actualCode +=vardecHelper(vt, actualCode);
            //     actualCode += "\n}";
            //     //Clear the variables hashmap once were done
            //     for (Map.Entry<Variable, Exp> item : variables.entrySet()) {
            //         variables.remove(item);
            //     }
            // }
        }
        else if ( v.exp instanceof VarMethodExp){
            String actualCode = "var " + v.varDec.var.toString() + " = ";
            VarMethodExp e = (VarMethodExp)v.exp;
            //get the index of the offset in the vtable
            String var = e.var.toString();
            ClassName cn = objToClass.get(var);
            VTableClassTable vt = compmap.get(cn);
            int offs = vt.offsets.get(e.methodName);
            String params = "";
            for (int i = 0; i < e.parameters.size(); i++){
                params += e.parameters.get(i).emit();
                params += ", ";
            }
    
            actualCode += var + "." + "vtable[" + offs + "](" + params + var + ");";
            Code.add(actualCode);
        }
        else if (v.exp instanceof VariableExp){
            String actualCode = "var " + v.varDec.var.toString() + " = " + v.exp.emit();
            Code.add(actualCode);
        }
        else{
            //Anything else int bool whatever
            String actualCode = "var " + v.varDec.var.toString() + " = " + v.exp.emit(); 
            Code.add(actualCode);
        }
    }
    public void compileIfStmt(Statement ifstmt){
        IfStatement e = (IfStatement)ifstmt;
        Code.add(e.emit());
    }
    public void compileWhileStmt(Statement whilestmt){
        WhileStatement e = (WhileStatement)whilestmt;
        Code.add(e.emit());
    }
    public void compileBinOpExp(Exp exp){
        BinopExp e = (BinopExp)exp;
        Code.add(e.emit());
    }

    public void compileBoolExp(Exp exp){
        BoolExp e = (BoolExp)exp;
        Code.add(e.emit());
    }
    
    public void compileClassExp(Exp exp){
        ClassExp e = (ClassExp)exp;
        Code.add(e.emit());
    }

    public void compileNumberExp(Exp exp){
        NumberExp e = (NumberExp)exp;
        Code.add(e.emit());
    }

    public void compileVarMethodExp(Exp exp){
        VarMethodExp e = (VarMethodExp)exp;
        //get the index of the offset in the vtable
        String var = e.var.toString();
        ClassName cn = objToClass.get(var);
        VTableClassTable vt = compmap.get(cn);
        int offs = vt.offsets.get(e.methodName);
        String params = "";
        for (int i = 0; i < e.parameters.size(); i++){
            params += e.parameters.get(i).emit();
            params += ", ";
        }

        String actualCode = var + "." + "vtable[" + offs + "](" + params + var + ");";
        Code.add(actualCode);
    }
    public void writeExptoFile(final Exp exp, final File file) throws IOException{
        final Codegen gen = new Codegen();
        gen.compileExp(exp);
        gen.writeCompleteFile(file);
    }
    public void writeStatementstoFile(final Statement stmt, final File file) throws IOException{
        final Codegen gen = new Codegen();
        gen.compileStatement(stmt);
        gen.writeCompleteFile(file);
    }
    public void writeProgramtoFile(final Program program, final File file) throws IOException{
        final Codegen gen = new Codegen();
        gen.compileProgram(program);
        // System.out.println("this is the endof the program");
        for(String item : Code){
            // System.out.println(item);
        }
        gen.writeCompleteFile(file);
    }
    public void writeCompleteFile(final File file) throws IOException{
        final PrintWriter output= new PrintWriter(new BufferedWriter(new FileWriter(file)));
        try{
            for (final String c : Code){
                output.println(c);
            }
        }finally{
            output.close();
        }
    }
}