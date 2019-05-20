package j2script;

import j2script.*;
import j2script.tokens.*;
import j2script.access.*;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;
import org.junit.Test;
import java.util.List;
import j2script.TypeChecker;
import java.util.ArrayList;

public class TypeCheckerTest {

	//assigning types
    @Test(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithIntType() throws TypeErrorException {
    	/*
	 class Foo{
            constructor()
                int x = "test";  <--  int to string  
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			//new StringExp("test")
    			new VariableExp(new Variable("test"))
    			)
    		), 
    	varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }
    @Test
    public void testNumberExp() throws TypeErrorException {
        /*
     class Foo{
            constructor()
                int x = 0;  <--  int to string  
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                //new StringExp("test")
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithBooleanType() throws TypeErrorException{
    	/*
	 class Foo{
            constructor()
                boolean x = 0;  <--   
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new BooleanType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test
    public void testBooleanExp() throws TypeErrorException {
        /*
     class Foo{
            constructor()
                Boolean x = true; 
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new BooleanType(), 
                    new Variable("x")
                    ), 
                //new StringExp("test")
                new BoolExp(true)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithClassType() throws TypeErrorException{
    	/*
	 class Foo{
            constructor()
                int x = 0;    
        }
      class Boo{
            Boo()
                int x = 0;  
       }
        Foo y = Boo(); <--

    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
    	new ClassExp(new ClassName("Boo"), new ArrayList<Type>(), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs, new ArrayList<TypeVariable>()));
   	classDef.add(new ClassDef(
    	new ClassName("Boo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs, new ArrayList<TypeVariable>()));
    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test
    public void testClassType() throws TypeErrorException{
        /*
     class Foo{
            constructor()
                int x = 0;    
        }
      
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));
    
    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class) 
    public void testClassWithInstanceVarAssignmentConstructor() throws TypeErrorException{
    /** class foo{
    int x;
    constructor(boolean y) 
        x = y ; <--- int to boolean 
    }

    Foo y = Foo(true);
**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();
	    classExp.add(new BoolExp(true));

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	    varDec.add(new VarDec(new BooleanType(), new Variable("y")));
	    List<VarDec> varDecNonEmpty = new ArrayList<>();
	    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarDecAssignment(
	    			new VarDec(
	    				new IntType(), 
	    				new Variable("x")
	    				), 
	    			new VariableExp(new Variable("y"))
	    			)
	    		), 
	    	varDecNonEmpty, methodDefs, new ArrayList<TypeVariable>()));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}

    //Assigning types tests end

	//return types
    @Test(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithVoidTypeReturn() throws TypeErrorException {
    	/*
	 class Foo{
            constructor()
                int x = 0;   
            private void methodA() {
				return 1; <-- no return, its void
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new VoidType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new NumberExp(1))));
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    

    @Test(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithIntTypeReturn() throws TypeErrorException {
    	/*
	 class Foo{
            constructor()
                int x = 0;   
            private int methodA() {
				return true; <-- return int 
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(
        new PrivateAccess(), 
        new IntType(), 
        new MethodName("methodA"), 
        varDec, 
        new ReturnExpStatement(
            new BoolExp(true)
            )
        )
    );
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    @Test//(expected = TypeErrorException.class) 
    public void testClassWithReturnExpWorking() throws TypeErrorException {
        /*
     class Foo{
            constructor()
                int x = 0;   
            private int methodA() {
                return 1;
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new NumberExp(1))));
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class) 
    public void testClassWithWrongVarDecStatementWithNoReturn() throws TypeErrorException {
    	/*
	 class Foo{
            constructor()
                int x = 0;   
            private int methodA() {
				int y = 0; <-- should return
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
    	);

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new VarDecAssignment(new VarDec(
    				new IntType(), 
    				new Variable("y")
    				), 
    			new NumberExp(0)
    )));
   	classDef.add(new ClassDef(
    	new ClassName("Foo"), 
    	new Constructor(
    		varDec, 
    		new VarDecAssignment(
    			new VarDec(
    				new IntType(), 
    				new Variable("x")
    				), 
    			new NumberExp(0)
    			)
    		), 
    	varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    	
    }

    // return type tests end
    

	@Test(expected = TypeErrorException.class) 
	    public void testClassWithInstanceVarAssignmentNotIncludedInStatement() throws TypeErrorException {
	    /** class foo{
	    int x;
	    constructor(int y) 
	        x = y ; 
	    }

	    Foo y = Foo(); <-- should have paramter
	**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	    varDec.add(new VarDec(new BooleanType(), new Variable("y")));
	    List<VarDec> varDecNonEmpty = new ArrayList<>();
	    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarDecAssignment(
	    			new VarDec(
	    				new IntType(), 
	    				new Variable("x")
	    				), 
	    			new VariableExp(new Variable("y"))
	    			)
	    		), 
	    	varDecNonEmpty, methodDefs, new ArrayList<TypeVariable>()));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}

	@Test(expected = TypeErrorException.class)
	    public void testClassWithInstanceVarAssignmentNotIncludedInStatementReverse() throws TypeErrorException{
	    /** class foo{
	    int x;
	    constructor() 
	        x = 0 ; 
	    }

	    Foo y = Foo(1); <-- should not have paramter
	**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();
	    classExp.add(new NumberExp(1));

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	    List<VarDec> varDecNonEmpty = new ArrayList<>();
	    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarDecAssignment(
	    			new VarDec(
	    				new IntType(), 
	    				new Variable("x")
	    				), 
	    			new NumberExp(0)
	    			)
	    		), 
	    	varDecNonEmpty, methodDefs, new ArrayList<TypeVariable>()));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}

	@Test(expected = TypeErrorException.class) 
	    public void testVariablesDefinedBeforeAssigning() throws TypeErrorException {
	    /** class foo{
	    
	    constructor() 
	        x = 0 ; <-- not declared
	    }

	    Foo y = Foo();
	**/
	    List<ClassDef> classDef = new ArrayList<>();
	    List<MethodDef> methodDefs = new ArrayList<>(); //empty
	    List<Exp> classExp = new ArrayList<>();

	    Statement statement = new VarDecAssignment(
	    	new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
	    	new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
	    	);

	    List<VarDec> varDec = new ArrayList<>(); //empty
	   	classDef.add(new ClassDef(
	    	new ClassName("Foo"), 
	    	new Constructor(
	    		varDec, 
	    		new VarAssignment(
	    			new Variable("x"), 
	    			new NumberExp(0)
	    			)
	    		), 
	    	varDec, methodDefs, new ArrayList<TypeVariable>()));

	    final Program program = new Program(classDef, statement);
	    TypeChecker.typecheckProgram(program);
	}

	//scope tests
    @Test(expected = TypeErrorException.class) 
        public void testScopeofInstanceVarAlreadyDeclared() throws TypeErrorException {
        /** class foo{
        int x;
        constructor() 
           int x = 0 ; not allowed
        }

        Foo y = Foo();
    **/
        List<ClassDef> classDef = new ArrayList<>();
        List<MethodDef> methodDefs = new ArrayList<>(); //empty
        List<Exp> classExp = new ArrayList<>();

        Statement statement = new VarDecAssignment(
            new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
            new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
            );

        List<VarDec> varDec = new ArrayList<>(); //empty
        varDec.add(new VarDec(new IntType(), new Variable("x")));
        List<VarDec> varDecEmpty = new ArrayList<>(); //empty
        classDef.add(new ClassDef(
            new ClassName("Foo"), 
            new Constructor(
                varDecEmpty, 
                new VarDecAssignment(
                    new VarDec(
                        new IntType(), 
                        new Variable("x")
                        ), 
                    new NumberExp(0)
                    )
                ), 
            varDec, methodDefs, new ArrayList<TypeVariable>()));

        final Program program = new Program(classDef, statement);
        TypeChecker.typecheckProgram(program);
    }

    @Test
        public void testScopeofInstanceVarAlreadyDeclaredWithSameVariable() throws TypeErrorException {
        /** class foo{
        int x;
        constructor() 
           int z = 0 ; 
        }

        Foo y = Foo();
    **/
        List<ClassDef> classDef = new ArrayList<>();
        List<MethodDef> methodDefs = new ArrayList<>(); //empty
        List<Exp> classExp = new ArrayList<>();

        Statement statement = new VarDecAssignment(
            new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
            new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
            );

        List<VarDec> varDec = new ArrayList<>(); //empty
        varDec.add(new VarDec(new IntType(), new Variable("x")));
        List<VarDec> varDecEmpty = new ArrayList<>(); //empty
        classDef.add(new ClassDef(
            new ClassName("Foo"), 
            new Constructor(
                varDecEmpty, 
                new VarDecAssignment(
                    new VarDec(
                        new IntType(), 
                        new Variable("z")
                        ), 
                    new NumberExp(0)
                    )
                ), 
            varDec, methodDefs, new ArrayList<TypeVariable>()));

        final Program program = new Program(classDef, statement);
        TypeChecker.typecheckProgram(program);
    }

        @Test(expected = TypeErrorException.class) //why is this stack overflow
        public void testScopeOfSameVariableOutsideofLoop() throws TypeErrorException {
        /** class foo{
        int x;
        constructor() 
           while(true) 
                int y = 2;
            y = 3;
        }

        Foo z = Foo();
    **/
        List<ClassDef> classDef = new ArrayList<>();
        List<MethodDef> methodDefs = new ArrayList<>(); //empty
        List<Exp> classExp = new ArrayList<>();

        Statement statement = new VarDecAssignment(
            new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("z")), 
            new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
            );

        List<Statement> blockStatements = new ArrayList<>();
        blockStatements.add(new WhileStatement(new BoolExp(true), new VarDecAssignment(new VarDec(new IntType(), new Variable("y")), new NumberExp(2))));
        blockStatements.add(new VarAssignment(new Variable("y"), new NumberExp(3)));
        List<VarDec> varDec = new ArrayList<>(); //empty
        varDec.add(new VarDec(new IntType(), new Variable("x")));
        List<VarDec> varDecEmpty = new ArrayList<>(); //empty
        classDef.add(new ClassDef(
            new ClassName("Foo"), 
            new Constructor(
                varDecEmpty, 
                new Block(blockStatements)
                ), 
            varDec, methodDefs, new ArrayList<TypeVariable>()));

        final Program program = new Program(classDef, statement);
        TypeChecker.typecheckProgram(program);
    }
	
    @Test(expected = TypeErrorException.class) 
    public void testScopeOfSameVariableOutsideofScope() throws TypeErrorException {
        /** class foo{
        constructor() 
           int y =8;
        methodA() 
            y = 9;
        }


        Foo y = Foo();
    **/
        List<ClassDef> classDef = new ArrayList<>();
        List<MethodDef> methodDefs = new ArrayList<>(); //empty
        List<Exp> classExp = new ArrayList<>();

        Statement statement = new VarDecAssignment(
            new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
            new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
            );

        List<VarDec> varDec = new ArrayList<>(); //empty
         methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new VarAssignment(new Variable("y"), new NumberExp(9))));

        List<VarDec> varDecEmpty = new ArrayList<>(); //empty
        classDef.add(new ClassDef(
            new ClassName("Foo"), 
            new Constructor(
                varDecEmpty, 
                new VarDecAssignment(
                    new VarDec(
                        new IntType(), 
                        new Variable("y")
                        ), 
                    new NumberExp(8)
                    )
                ), 
            varDec, methodDefs, new ArrayList<TypeVariable>()));

        final Program program = new Program(classDef, statement);
        TypeChecker.typecheckProgram(program);
    }

    @Test(expected = TypeErrorException.class) 
    public void testVariableNeverAssignedButReturned() throws TypeErrorException {
        /*
     class Foo{
        int x;
            constructor()
                int z = 0;   
            private int methodA() {
                return x;  <-- never declared or initialized
            }
        }
        Foo y = Foo(); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>();
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    methodDefs.add(new MethodDef(
        new PrivateAccess(), 
        new IntType(), 
        new MethodName("methodA"), 
        varDec, 
        new ReturnExpStatement(new VariableExp(new Variable("x")))));
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDecNonEmpty, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

        
    }

    @Test(expected = TypeErrorException.class) 
    public void testBreakCalledWithoutIFandWhileStmts() throws TypeErrorException {
           /*
     class Foo{
            constructor()
                int z = 0;   
            private void methodA() {
                break;
            }
        }
        Foo y = Foo(); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );
    List<Statement> statements = new ArrayList<>();
    statements.add(new BreakStatement());
    statements.add(new ReturnVoidStatement());
    List<VarDec> varDec = new ArrayList<>(); //empty
    
    methodDefs.add(new MethodDef(
        new PrivateAccess(), 
        new VoidType(), 
        new MethodName("methodA"), 
        varDec, 
        new Block(statements)
        ));
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testUniqueNamesOfClasses() throws TypeErrorException {
        /*
     class Foo{
            constructor()
                int x = 0;
        }

        class Foo {
            constructor()
                int x =0;
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                //new StringExp("test")
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                //new StringExp("test")
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class)
    public void testUniqueNamesOfVariables() throws TypeErrorException {
        /*
     class Foo{
            constructor()
                int x = 0;
        }
        Boolean y = true; <-- same variable name
        int y = 0;
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();

    Statement statementOne = new VarDecAssignment(
        new VarDec(new BooleanType(), new Variable("y")), 
        new BoolExp(true)
        );
    Statement statementTwo = new VarDecAssignment(new VarDec(new IntType(), new Variable("y")), new NumberExp(0));

    List<Statement> statement = new ArrayList<>();
    statement.add(statementOne);
    statement.add(statementOne);

    List<VarDec> varDec = new ArrayList<>(); //empty
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                //new StringExp("test")
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));


    final Program program = new Program(classDef, new Block(statement));
    TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class) 
    public void testFunctionCallWithWrongParameters() throws TypeErrorException {
    /*
      class Foo{
        constructor(int x)
          int z = 0;   
      }
      Foo y = Foo(true); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new BoolExp(true));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
    }

    @Test
    public void testBinopOperation() throws TypeErrorException {
           /*
     class Foo{
            constructor(int x)
                int z = x + 2;   
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new BinopExp(new VariableExp(new Variable("x")), new PlusOp(), new NumberExp(2))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testBinopOperationWithBoolean() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                int z = x + true;   
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new BinopExp(new VariableExp(new Variable("x")), new PlusOp(), new BoolExp(true))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test
    public void testBinopOperationMinus() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                int z = x - 2;   
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new BinopExp(new VariableExp(new Variable("x")), new MinusOp(), new NumberExp(2))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testBinopOperationWithBooleanWithMinus() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                int z = x - true;   
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new BinopExp(new VariableExp(new Variable("x")), new MinusOp(), new BoolExp(true))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test
    public void testBinopOperationMultiply() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                int z = x * 2;   
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new BinopExp(new VariableExp(new Variable("x")), new MultOp(), new NumberExp(2))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testBinopOperationWithBooleanMultiply() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                int z = x + true;   
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new BinopExp(new VariableExp(new Variable("x")), new MultOp(), new BoolExp(true))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test
    public void testBinopOperationDiv() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                int z = x + 2;   
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new BinopExp(new VariableExp(new Variable("x")), new DivOp(), new NumberExp(2))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testBinopOperationWithBooleanDiv() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                int z = x / true;   
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("z")
                    ), 
                new BinopExp(new VariableExp(new Variable("x")), new DivOp(), new BoolExp(true))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test
    public void testBinopOperationEquals() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                if(x==3)
                    x = 2;
                else 
                    x = 1;
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
                new IfStatement(
                    new BinopExp(
                        new VariableExp(
                            new Variable("x")
                            ), 
                        new EqualsOp(), 
                        new NumberExp(3)
                        ), 
                    new VarAssignment(
                        new Variable("x"), 
                        new NumberExp(2)
                        ), 
                    new VarAssignment(
                        new Variable("x"), 
                        new NumberExp(1)
                        )
                    )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testBinopOperationWithBooleanWithEquals() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                if(x==true) 
                    x =2
                else
                    x= 1
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new IfStatement(
                    new BinopExp(
                        new VariableExp(
                            new Variable("x")
                            ), 
                        new EqualsOp(), 
                        new BoolExp(true)
                        ), 
                    new VarAssignment(
                        new Variable("x"), 
                        new NumberExp(2)
                        ), 
                    new VarAssignment(
                        new Variable("x"), 
                        new NumberExp(1)
                        )
                    )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test
    public void testBinopOperationLessThanEquals() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                if(x<3)
                    x = 2;
                else 
                    x = 1;
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
                new IfStatement(
                    new BinopExp(
                        new VariableExp(
                            new Variable("x")
                            ), 
                        new LessThanOp(), 
                        new NumberExp(3)
                        ), 
                    new VarAssignment(
                        new Variable("x"), 
                        new NumberExp(2)
                        ), 
                    new VarAssignment(
                        new Variable("x"), 
                        new NumberExp(1)
                        )
                    )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testBinopOperationWithBooleanWithLessThanEquals() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                if(x<true) 
                    x =2
                else
                    x= 1
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new IfStatement(
                    new BinopExp(
                        new VariableExp(
                            new Variable("x")
                            ), 
                        new LessThanOp(), 
                        new BoolExp(true)
                        ), 
                    new VarAssignment(
                        new Variable("x"), 
                        new NumberExp(2)
                        ), 
                    new VarAssignment(
                        new Variable("x"), 
                        new NumberExp(1)
                        )
                    )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testBinopOperationWithIntegerWithLessThanEquals() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                int f = x < 2;
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                    new VarDec(
                    new IntType(), 
                    new Variable("f")
                    ), 
                new BinopExp(new VariableExp(
                            new Variable("x")
                            ), 
                        new LessThanOp(), 
                        new NumberExp(2))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test//(expected = TypeErrorException.class) 
    public void testBinopOperationWithBooleanForLessThanEquals() throws TypeErrorException {
           /*
     class Foo{
            Foo(int x)
                boolean f = x < 2;
            
        }
        Foo y = Foo(3); 
    */
    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();
    classExp.add(new NumberExp(3));

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>(); 
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDecNonEmpty, 
            new VarDecAssignment(
                    new VarDec(
                    new BooleanType(), 
                    new Variable("f")
                    ), 
                new BinopExp(new VariableExp(
                            new Variable("x")
                            ), 
                        new LessThanOp(), 
                        new NumberExp(2))
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class) 
    public void testDuplicateMethods() throws TypeErrorException {
        /*
     class Foo{
            Foo()
                int x = 0;   
            private int methodA() {
                return 1; 
            }
            private int methodA() {
                return 1; 
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new NumberExp(1))));
    methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new NumberExp(1))));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class) 
    public void testDuplicateMethodsNames() throws TypeErrorException {
        /*
     class Foo{
            Foo()
                int x = 0;   
            private int methodA() {
                return 1; 
            }
            private String methodA() {
                return 1; 
            }
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new NumberExp(1))));
    methodDefs.add(new MethodDef(new PrivateAccess(), new BooleanType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new NumberExp(1))));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test//(expected = TypeErrorException.class) 
    public void testVoidMethod() throws TypeErrorException {
        /*
     class Foo{
            Foo()
                int x = 0;   
            private void methodA() {
                return;
            }
            
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new VoidType(), new MethodName("methodA"), varDec, new ReturnVoidStatement()));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test//(expected = TypeErrorException.class) 
    public void testIntMethod() throws TypeErrorException {
        /*
     class Foo{
            Foo()
                int x = 0;   
            private int methodA() {
                return 1;
            }
            
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new IntType(), new MethodName("methodA"), varDec, new ReturnExpStatement(new NumberExp(1))));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new VarDecAssignment(
                new VarDec(
                    new IntType(), 
                    new Variable("x")
                    ), 
                new NumberExp(0)
                )
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test//(expected = TypeErrorException.class) 
    public void testMethodCallFromWithinClass() throws TypeErrorException {
        /*
     class Foo{
            Foo()
                methodA();  
            private void methodA() {
                return;
            }
            
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    methodDefs.add(new MethodDef(new PrivateAccess(), new VoidType(), new MethodName("methodA"), varDec, new ReturnVoidStatement()));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new ExpStatement(new MethodExp(new MethodName("methodA"), classExp))), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class) 
    public void testSuperMethodWithoutInheritingAClass() throws TypeErrorException {
        /*
     class Foo{
            Foo()
                super();  
            
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new SuperStatement(classExp)), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test//(expected = TypeErrorException.class) 
    public void testSuperMethodWithNoExpressions() throws TypeErrorException {
        /*
     class Boo {
        Boo()
            PrintStatement(true);
        }
     class Foo extends Boo{
            Foo()
                super();         
        }
    
        Foo y = Foo(); 
    }
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    
    classDef.add(new ClassDef(
        new ClassName("Boo"), 
        new Constructor(
            varDec, 
            new PrintStatement(new BoolExp(true))), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new SuperStatement(classExp)),
        new Extends(new ClassName("Boo"), new ArrayList<Type>()), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    
    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }


    @Test(expected = TypeErrorException.class) 
    public void testMethodCallToAMethodThatDontExist() throws TypeErrorException {
        /*
     class Foo{
            Foo()
                methodB();  
            private void methodA() {
                return;
            }
            
        }
        Foo y = Foo(); 
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>();
    List<Exp> classExp = new ArrayList<>();

    Statement statement = new VarDecAssignment(
        new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
        new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
        );

    List<VarDec> varDec = new ArrayList<>(); //empty
    List<VarDec> varDecNonEmpty = new ArrayList<>();
    varDecNonEmpty.add(new VarDec(new IntType(), new Variable("x")));
    methodDefs.add(new MethodDef(new PrivateAccess(), new VoidType(), new MethodName("methodA"), varDec, new ReturnVoidStatement()));
    
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new ExpStatement(new MethodExp(new MethodName("methodB"), classExp))), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));

    final Program program = new Program(classDef, statement);
    TypeChecker.typecheckProgram(program);
        
    }

    @Test//(expected = TypeErrorException.class) 
        public void testPrintStmt() throws TypeErrorException {
        /** class foo{
        int x;
        foo() 
           print(false);
        }

        Foo y = Foo();
    **/
        List<ClassDef> classDef = new ArrayList<>();
        List<MethodDef> methodDefs = new ArrayList<>(); //empty
        List<Exp> classExp = new ArrayList<>();

        Statement statement = new VarDecAssignment(
            new VarDec(new ClassType(new ClassName("Foo"), new ArrayList<Type>()), new Variable("y")), 
            new ClassExp(new ClassName("Foo"), new ArrayList<Type>(), classExp)
            );

        List<VarDec> varDec = new ArrayList<>(); //empty
        varDec.add(new VarDec(new IntType(), new Variable("x")));
        List<VarDec> varDecEmpty = new ArrayList<>(); //empty
        classDef.add(new ClassDef(
            new ClassName("Foo"), 
            new Constructor(
                varDecEmpty, 
                new PrintStatement(new BoolExp(false))
                ), 
            varDec, methodDefs, new ArrayList<TypeVariable>()));

        final Program program = new Program(classDef, statement);
        TypeChecker.typecheckProgram(program);
    }

    @Test(expected = TypeErrorException.class)
    public void testIfStatmentsWithBreak() throws TypeErrorException {
        /*
     class Foo{
            Foo()
                if(true)
                    printStatment(true);
                else
                    break; <-- only allowed in while loops
        }
        int y = 0;
    */


    List<ClassDef> classDef = new ArrayList<>();
    List<MethodDef> methodDefs = new ArrayList<>(); //empty
    List<Exp> classExp = new ArrayList<>();


    Statement statementTwo = new VarDecAssignment(new VarDec(new IntType(), new Variable("y")), new NumberExp(0));

    List<VarDec> varDec = new ArrayList<>(); //empty
    classDef.add(new ClassDef(
        new ClassName("Foo"), 
        new Constructor(
            varDec, 
            new IfStatement(new BoolExp(true), 
                new BreakStatement(),
                new PrintStatement(new BoolExp(true)))
            ), 
        varDec, methodDefs, new ArrayList<TypeVariable>()));


    final Program program = new Program(classDef, statementTwo);
    TypeChecker.typecheckProgram(program);
        
    }
    
    @Test//(expected = TypeErrorException.class) 
    public void testGenericWithOneTypeVariable() throws TypeErrorException {
       /*
        class GenericClass<A> {
            A a;
            constructor(A b) {
                a = b;
            }
            public A getA() {
                return a;
            }
        }
        GenericClass<int> g = new GenericClass<int>(3);
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));

        List<MethodDef> methodDefs = new ArrayList<>();
        methodDefs.add(new MethodDef(new PublicAccess(), new TypeVariable("A"), new MethodName("getA"), new ArrayList<VarDec>(), new ReturnExpStatement(new VariableExp(new Variable("a")))));

        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        //GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));

        Statement stm = new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                ));

        final Program program = new Program(classDef, stm);
        TypeChecker.typecheckProgram(program);
        
    }

    @Test(expected = TypeErrorException.class) 
    public void testGenericWithTwoTypeVariablesNotUsingOne() throws TypeErrorException {
       /*
        test case 2: expect failure
        class GenericClass<A, B> {
            A a;
            constructor(A b) {
                a = b;
            }
            public A getA() {
                return a;
            }
        }
        GenericClass<int> g = new GenericClass<int>(3); <--- expect error
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));

        List<MethodDef> methodDefs = new ArrayList<>();
        methodDefs.add(new MethodDef(new PublicAccess(), new TypeVariable("A"), new MethodName("getA"), new ArrayList<VarDec>(), new ReturnExpStatement(new VariableExp(new Variable("a")))));

        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        types.add(new TypeVariable("B"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        //GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));

        Statement stm = new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                ));

        final Program program = new Program(classDef, stm);
        TypeChecker.typecheckProgram(program);
    }

    @Test//(expected = TypeErrorException.class) 
    public void testGenericGetAWithOneTypeVariable() throws TypeErrorException {
        /* test case 1: dont expect error
        class GenericClass<A> {
            A a;
            constructor(A b) {
                a = b;
            }
            public A getA() {
                return a;
            }
        }
        GenericClass<int> g = new GenericClass<int>(3);
        int k = g.getA();
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));

        List<MethodDef> methodDefs = new ArrayList<>();
        methodDefs.add(new MethodDef(new PublicAccess(), new TypeVariable("A"), new MethodName("getA"), new ArrayList<VarDec>(), new ReturnExpStatement(new VariableExp(new Variable("a")))));

        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        // GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));

        List<Statement> stmts = new ArrayList<>();
        stmts.add(new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                ))
        );
        

        stmts.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("k")), 
            new VarMethodExp(new Variable("g"), new MethodName("getA"), new ArrayList<Exp>())));

        final Program program = new Program(classDef, new Block(stmts));
        TypeChecker.typecheckProgram(program);
    }

    @Test(expected = TypeErrorException.class) 
    public void testGenericExtraAssignmentType() throws TypeErrorException {
        /*
        test case 3: expect error
        class GenericClass<A> { <-- need another Parameter
            A a;
            constructor(A b) {
                a = b;
            }
            public A getA() {
                return a;
            }
        }
        GenericClass<int> g = new GenericClass<int, Boolean>(3); 
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));

        List<MethodDef> methodDefs = new ArrayList<>();
        methodDefs.add(new MethodDef(new PublicAccess(), new TypeVariable("A"), new MethodName("getA"), new ArrayList<VarDec>(), new ReturnExpStatement(new VariableExp(new Variable("a")))));

        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        // GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());
        classTypes.add(new BooleanType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));

        List<Statement> stmts = new ArrayList<>();
        stmts.add(new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                ))
        );
        
        stmts.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("k")), 
            new VarMethodExp(new Variable("g"), new MethodName("getA"), new ArrayList<Exp>())));

        final Program program = new Program(classDef, new Block(stmts));
        TypeChecker.typecheckProgram(program);
    }

    @Test(expected = TypeErrorException.class) 
    public void testGenericReturnTypeAndAssignmentTypeMismatch() throws TypeErrorException {
        /*
        test case 4: expect error
        class GenericClass<A> { 
            A a;
            constructor(A b) {
                a = b;
            }
            public A getA() {
                return a;
            }
        }
        GenericClass<int> g = new GenericClass<int>(3); 
        boolean b = g.getA(); <-- doesnt return boolean so error
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));

        List<MethodDef> methodDefs = new ArrayList<>();
        methodDefs.add(new MethodDef(new PublicAccess(), new TypeVariable("A"), new MethodName("getA"), new ArrayList<VarDec>(), new ReturnExpStatement(new VariableExp(new Variable("a")))));

        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        // GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));

        List<Statement> stmts = new ArrayList<>();
        stmts.add(new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                ))
        );
        
        stmts.add(new VarDecAssignment(new VarDec(new BooleanType(), new Variable("b")), 
            new VarMethodExp(new Variable("g"), new MethodName("getA"), new ArrayList<Exp>())));

        final Program program = new Program(classDef, new Block(stmts));
        TypeChecker.typecheckProgram(program);
    }

    @Test(expected = TypeErrorException.class) 
    public void testGenericInitiziationInAMethod() throws TypeErrorException {
        /*
        test case 5: expect error
        class GenericClass<A> { 
            A a;
            constructor(A b) {
                a = b;
            }
            public void setA(A c) { 
                a = c;<-- error cuz a is set to an integer
                return;
            }
        }
        GenericClass<int> g = new GenericClass<int>(3); 
        g.setA(true);
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));

        
        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));

        List<VarDec> methodPram = new ArrayList<>();
        methodPram.add(new VarDec(new TypeVariable("A"), new Variable("c")));


        List<Statement> blocks = new ArrayList<>();
        blocks.add(new VarAssignment(new Variable("a"), new VariableExp(new Variable("c"))));
        blocks.add(new ReturnVoidStatement());

        List<MethodDef> methodDefs = new ArrayList<>();
        methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setA"), 
            methodPram, 
            new Block(blocks)
            )
        );

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        // GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));

        List<Statement> stmts = new ArrayList<>();
        stmts.add(new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                ))
        );

        List<Exp> p = new ArrayList<>();
        p.add(new BoolExp(true));
        

        stmts.add(new ExpStatement( 
            new VarMethodExp(new Variable("g"), new MethodName("setA"), p)));

        final Program program = new Program(classDef, new Block(stmts));
        TypeChecker.typecheckProgram(program);

    }

    @Test(expected = TypeErrorException.class)
    public void testWrongGenericParameter() throws TypeErrorException {
        /*
        class GenericClass<A> {
            A a;
            constructor(A b) {
                a = b;
            }
        }
        GenericClass<int> g = new GenericClass<boolean>(3); <-- wrong
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));

        List<MethodDef> methodDefs = new ArrayList<>();
        
        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        //GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());

        List<Type> wrongclassTypes = new ArrayList<>();
        wrongclassTypes.add(new BooleanType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));

        Statement stm = new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                wrongclassTypes, 
                expressions
                ));

        final Program program = new Program(classDef, stm);
        TypeChecker.typecheckProgram(program);
    }

    @Test(expected = TypeErrorException.class) 
    public void testGenericsWithWrongParameters() throws TypeErrorException{
        /*
        test case 6: expect error
        class GenericClass<A,B> { 
            A a;
            B b;
            constructor(A b, B c) {
                a = b;
            }
    
        }
        GenericClass<int, boolean> g = new GenericClass<int, boolean>(true, 3); <-- the parameter doesnt go with the assignment of generics
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));
        instanceVars.add(new VarDec(new TypeVariable("B"), new Variable("b")));

        List<MethodDef> methodDefs = new ArrayList<>();
        
        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));
        constructorParam.add(new VarDec(new TypeVariable("B"), new Variable("c")));

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        // GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());
        classTypes.add(new BooleanType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new BoolExp(true));
        expressions.add(new NumberExp(3));

        List<Statement> stmts = new ArrayList<>();
        stmts.add(new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                ))
        );

        stmts.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("k")), 
            new VarMethodExp(new Variable("g"), new MethodName("getA"), new ArrayList<Exp>())));

        final Program program = new Program(classDef, new Block(stmts));
        TypeChecker.typecheckProgram(program);
    }

    @Test
    public void testGenericsthatWorks() throws TypeErrorException{
        /*
        test case 7: 
        class GenericClass<A> { 
            A a;
            constructor(A b) {
                a = b;
            }
            public void setA(A c) { 
                a = c;
                return;
            }
        }
        GenericClass<int> c = GenericClass<boolean>(3);
        c.setA(4); 
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));

        
        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("b")));

        List<VarDec> methodPram = new ArrayList<>();
        methodPram.add(new VarDec(new TypeVariable("A"), new Variable("c")));


        List<Statement> blocks = new ArrayList<>();
        blocks.add(new VarAssignment(new Variable("a"), new VariableExp(new Variable("c"))));
        blocks.add(new ReturnVoidStatement());
        List<MethodDef> methodDefs = new ArrayList<>();
        methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setA"), 
            methodPram, 
            new Block(blocks)
            )
        );

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        // GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));

        List<Statement> stmts = new ArrayList<>();
        stmts.add(new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                ))
        );

        List<Exp> p = new ArrayList<>();
        p.add(new NumberExp(4));
        

        stmts.add(new ExpStatement( 
            new VarMethodExp(new Variable("g"), new MethodName("setA"), p)));

        final Program program = new Program(classDef, new Block(stmts));
        TypeChecker.typecheckProgram(program);
    }


    @Test(expected = TypeErrorException.class) 
    public void testGenericsWithWronggenericValue() throws TypeErrorException{
        /*
        test case 7: 
        class GenericClass<A,B> { 
            A a;
            B baa;
            constructor(A aa, B b) {
                a = b; <-- another variable
            }
            
        }
        Genericclass<int, boolean> c = GenericClass<int, boolean>(3, boolean);
        */

        List<ClassDef> classDef = new ArrayList<>();

        List<VarDec> instanceVars = new ArrayList<>();
        instanceVars.add(new VarDec(new TypeVariable("A"), new Variable("a")));
        instanceVars.add(new VarDec(new TypeVariable("B"), new Variable("baa")));

        
        List<VarDec> constructorParam = new ArrayList<>();
        constructorParam.add(new VarDec(new TypeVariable("A"), new Variable("aa")));
        constructorParam.add(new VarDec(new TypeVariable("B"), new Variable("b")));

        List<MethodDef> methodDefs = new ArrayList<>();
        

        List<TypeVariable> types = new ArrayList<>();
        types.add(new TypeVariable("A"));
        classDef.add(new ClassDef(
            new ClassName("GenericClass"), 
            new Constructor(
                constructorParam, 
                new VarAssignment(
                    new Variable("a"),
                    new VariableExp(new Variable("b")))), 
            instanceVars, 
            methodDefs, 
            types));

        // GenericClass<int> g = new GenericClass<int>(3);
        List<Type> classTypes = new ArrayList<>();
        classTypes.add(new IntType());
        classTypes.add(new BooleanType());

        List<Exp> expressions = new ArrayList<>();
        expressions.add(new NumberExp(3));
        expressions.add(new BoolExp(true));


        final Program program = new Program(classDef, new VarDecAssignment(
            new VarDec(
                new ClassType(
                    new ClassName("GenericClass"), classTypes), 
                new Variable("g")), 
            new ClassExp(
                new ClassName("GenericClass"), 
                classTypes, 
                expressions
                )));
        TypeChecker.typecheckProgram(program);
    }
}
