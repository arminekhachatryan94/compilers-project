package j2script;

import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;

import java.io.File;
import java.io.IOException;

import j2script.*;
import j2script.tokens.*;
import j2script.access.*;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;


public class CodeGenTest {

	public void assertResult(String expected, Exp expression) throws IOException {
		
		try {
			Codegen code = new Codegen();
			final File file = File.createTempFile("test", ".js");
			code.writeExptoFile(expression, file);
			final String output = readFile(file);
			assertEquals(expected, output);
			file.delete();

		} catch (Exception e) {
			// System.out.println("somethings up");
		}

	}


	public void assertResultStatements(String expected, Statement stmt) throws IOException {
		
		try {
			Codegen code = new Codegen();
			final File file = File.createTempFile("test", ".js");
			code.writeStatementstoFile(stmt, file);
			final String output = readFile(file);
			assertEquals(expected, output);
			file.delete();

		} catch (Exception e) {
			// System.out.println("somethings up");
		}

	}

	public void assertResultProgram(String expected, Program program) throws IOException {
		
		try {
			Codegen code = new Codegen();
			final File file = File.createTempFile("test", ".js");
			code.writeProgramtoFile(program, file);
			final String output = readFile(file);
			assertEquals(expected, output);
			file.delete();

		} catch (Exception e) {
			// System.out.println("somethings up");
		}

	}

	private String readFile(File file) throws IOException {
		// File file = new File(pathname);
		StringBuilder fileContents = new StringBuilder((int)file.length());        

		try (Scanner scanner = new Scanner(file)) {
			while(scanner.hasNextLine()) {
			fileContents.append(scanner.nextLine());// + System.lineSeparator());
			}
			return fileContents.toString();
		}
	}

	/* Number Expressions */
	@Test
	public void testInt() throws IOException {
	        assertResult("99", new NumberExp(99));
	}


	/* Binop Expressions */
	@Test // 1 + 2
	public void testAddition() throws IOException {
	        assertResult("1 + 2", new BinopExp(new NumberExp(1), new PlusOp(), new NumberExp(2)));
	}

	@Test // 2 - 2 
	public void testSubtraction() throws IOException {
	        assertResult("2 - 2", new BinopExp(new NumberExp(2),
			new MinusOp(),
			new NumberExp(2)));
	}

	@Test // 1 - 2 
	public void testSubtractionWithNegativeResult() throws IOException {
	        assertResult("1 - 2", new BinopExp(new NumberExp(1),
			new MinusOp(),
			new NumberExp(2)));
	}


	@Test // 2 * 2 
	public void testMult() throws IOException {
	        assertResult("2 * 2", new BinopExp(new NumberExp(2),
			new MultOp(),
			new NumberExp(2)));
	}


	@Test // 2 / 2
	public void testDiv() throws IOException {
	        assertResult("2 / 2", new BinopExp(new NumberExp(2),
			new DivOp(),
			new NumberExp(2)));
	}

	/*@Test // 2 / 0 = 0
	public void testDiv() throws IOException {
	        assertResult(null, new BinopExp(new NumberExp(2),
		new DivOp(),
		new NumberExp(2)));
	}*/

	@Test // 2 / 4
	public void testDivTwo() throws IOException {
	        assertResult("2 / 4", new BinopExp(new NumberExp(2),
			new DivOp(),
			new NumberExp(4)));
	}


	/* Complex Algebraic Equations */
	@Test // 1 - 2 / 3
	public void testArithmeticEquation() throws IOException {
	        assertResult("1 - 2 / 3", new BinopExp(new NumberExp(1),
			new MinusOp(),
			new BinopExp(new NumberExp(2), new DivOp(), new NumberExp(3))
		));
	}

	@Test // 1 + 2 - 3
	public void testArithmeticEquationTwo() throws IOException {
	        assertResult("1 + 2 - 3", new BinopExp(new BinopExp(new NumberExp(1),
			new PlusOp(),
			new NumberExp(2)),
			new MinusOp(),
			new NumberExp(3)));
	}

	@Test // 1 * 2 - 3
	public void testArithmeticEquationThree() throws IOException {
	        assertResult("1 * 2 - 3", new BinopExp(new BinopExp(new NumberExp(1),
			new MultOp(),
			new NumberExp(2)),
			new MinusOp(),
			new NumberExp(3)));
	}

	@Test // 3 * 2 / 3 
	public void testArithmeticEquationFour() throws IOException {
	        assertResult("3 * 2 / 3", new BinopExp(new BinopExp(new NumberExp(3),
			new MultOp(),
			new NumberExp(2)),
			new DivOp(),
			new NumberExp(3)));
	}


	@Test 
	public void testbool() throws IOException {
	        assertResult("false", new BoolExp(false));
	}

	@Test // methodOne(4, intName)
	public void testFucntionCall() throws IOException {
    List<Exp> expressions = new ArrayList<>();
    Collections.addAll(expressions, new NumberExp(4), new VariableExp(new Variable("intName")));
	  assertResult("methodOne(4, intName)", new VarMethodExp(new Variable("var"), new MethodName("methodOne"), expressions));
	}

	/* Class Expression */
	@Test // new Foo<>(4, true)
	public void testClassObjects() throws IOException {
		List<Exp> expressions = new ArrayList<>();
		 expressions.add(new NumberExp(4));
		 expressions.add(new BoolExp(true));
	        assertResult("Foo(4, true)", new ClassExp(new ClassName("Foo"), new ArrayList<>(), expressions));
	}

	/* Class Expression */
	@Test // new Foo<>()
	public void testClassObjectsWithNoParameters() throws IOException {
		List<Exp> expressions = new ArrayList<>();
	        assertResult("Foo()", new ClassExp(new ClassName("Foo"), new ArrayList<>(), expressions));
	}

	@Test
	public void testIfTrue() throws IOException {
		/*
		if (true) x = 0;
		else x = 1;
		*/
		assertResultStatements("if (true) { x = 0 } else { x = 1}", new IfStatement(new BoolExp(true), new VarAssignment(new Variable("x"), new NumberExp(0)), new VarAssignment(new Variable("x"), new NumberExp(1))));
	}

	@Test
	public void testIfFalse() throws IOException {
		/*
		if (false) x = 0;
		else x = 1;
		*/
		assertResultStatements("if (false) { x = 0 } else { x = 1}", new IfStatement(new BoolExp(false), new VarAssignment(new Variable("x"), new NumberExp(0)), new VarAssignment(new Variable("x"), new NumberExp(1))));
	}

	@Test
	public void testWhileLoop() throws IOException{
		/*
		while(true)
			x = 0;
		*/
		assertResultStatements("while(true) {x = 0}", new WhileStatement(new BoolExp(true), new VarAssignment(new Variable("x"), new NumberExp(0))));

	}

	@Test
	public void testWhileLoopWithIfStmt() throws IOException {
		/*
		while(true)
			if(true)
			x =0;
			else
			x =1;
		*/
		assertResultStatements("while(true) {if (true) { x = 0 } else { x = 1}}", new WhileStatement(new BoolExp(true), 
			new IfStatement(new BoolExp(true), 
				new VarAssignment(new Variable("x"), 
					new NumberExp(0)), 
				new VarAssignment(new Variable("x"), new NumberExp(1)))));

	}

	@Test
	public void testWhileLoopWithIfStmtWithBreaks() throws IOException{
		/*
		while(true)
			if(true)
			break;
			else
			break;
		*/
		assertResultStatements("while(true) {if (true) { break } else { break}}", new WhileStatement(new BoolExp(true), 
			new IfStatement(new BoolExp(true), 
				new BreakStatement(), 
				new BreakStatement())));

	}

	@Test
	public void testNestedIfStmts() throws IOException{
		/*
		if(true)
			if(true)
				x = 1;
			else 
				x = 2;
		else
			x = 3;
		*/
		Statement ifNestedStm = new IfStatement(
			new BoolExp(true), 
			new VarAssignment(
				new Variable("x"), new NumberExp(1)
				), 
			new VarAssignment(new Variable("x"), new NumberExp(2)));

		assertResultStatements("if (true) { if (true) { x = 1 } else { x = 2} } else { x = 3}", new IfStatement(new BoolExp(true), ifNestedStm, new VarAssignment(new Variable("x"), new NumberExp(3))));

	}

	@Test
	public void testNestedWhileStmts() throws IOException{
		/*
		while(x) 
			while(y)
				x = true;
		*/

		Statement whileNested = new WhileStatement(new VariableExp(new Variable("y")), new VarAssignment(new Variable("x"), new BoolExp(true)));
		assertResultStatements("while(x) {while(y) {x = true}}", new WhileStatement(new VariableExp(new Variable("x")), whileNested));

	}

	@Test
	public void testIfTrueWithInequality() throws IOException {
		/*
		if (x<2) x = 0;
		else x = 1;
		*/

		assertResultStatements("if (x < 2) { x = 0 } else { x = 1}", new IfStatement(new BinopExp(new VariableExp(new Variable("x")), new LessThanOp(), new NumberExp(2)), new VarAssignment(new Variable("x"), new NumberExp(0)), new VarAssignment(new Variable("x"), new NumberExp(1))));


	}

	@Test
	public void testIfTrueWithInequalityWithNested() throws IOException {
		/*
		if(x<22)
			if(x==2)
				x = 1;
			else 
				x = 2;
		else
			x = 3;
		*/

		Statement ifNestedStm = new IfStatement(new BinopExp(new VariableExp(new Variable("x")), new EqualsOp(), new NumberExp(2)), new VarAssignment(new Variable("x"), new NumberExp(1)), new VarAssignment(new Variable("x"), new NumberExp(2)));

		assertResultStatements("if (x < 22) { if (x == 2) { x = 1 } else { x = 2} } else { x = 3}", new IfStatement(new BinopExp(new VariableExp(new Variable("x")), new LessThanOp(), new NumberExp(22)), ifNestedStm, new VarAssignment(new Variable("x"), new NumberExp(3))));
	}

	@Test
	public void testWhileLoopWithIfStmtWithBreaksAndEquality() throws IOException {
		/*
		while(x<22)
			if(x==21)
			break;
			else
			x=21;
		*/	
			assertResultStatements("while(x < 22) {if (x == 21) { break } else { break}}", 
				new WhileStatement(
					new BinopExp(new VariableExp(new Variable("x")), new LessThanOp(), new NumberExp(22)), 
					new IfStatement(
						new BinopExp(new VariableExp(new Variable("x")), new EqualsOp(), new NumberExp(21)), 
						new BreakStatement(), 
						new BreakStatement()
						)
					)
				);

	}

	@Test
	public void testVarDecAssign() throws IOException {
		//bool v = true;

		assertResultStatements("var v = true", new VarDecAssignment(new VarDec(new BooleanType(), new Variable("v")), new BoolExp(true)));

	}

	@Test
	public void testClass() throws IOException{
		//class Car{
			//constructor(bool bmw) {
				//bool name = bmw;
			//}
		//}

		//Car car = new Car(true);

		List<Exp> parameters = new ArrayList<>();
		parameters.add(new BoolExp(true));
		List<VarDec> emptyVarDecs = new ArrayList<>();
		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new BooleanType(), new Variable("bmw")));
		List<MethodDef> methodDefs = new ArrayList<>();
		List<ClassDef> classes = new ArrayList<>();
		final ClassDef classOne = new ClassDef(
            new ClassName("Car"), 
            new Constructor(varDecs, new VarDecAssignment(new VarDec(new BooleanType(), new Variable("name")), 
            new VariableExp(new Variable("bmw")))), 
            emptyVarDecs, 
            methodDefs,
            new ArrayList<>());
		classes.add(classOne);
		Program program = new Program(classes, new VarDecAssignment(new VarDec(new ClassType(new ClassName("Car"), new ArrayList<>()), new Variable("car")), new ClassExp(new ClassName("Car"), new ArrayList<>(), parameters)));
		assertResultProgram("var Car_vtable = [];var car = {	vtable: Car_vtable}", program);
	}

	@Test
	public void testClasswithOneMethod() throws IOException{
		//class Car{
			//boolean name;
			//constructor() {
				//name = true;
			//}
			//public Boolean getName(){
				//return name;
			//}
		//}

		//Car car = new Car();
		//Boolean name = car.getName();


		//var Car_getName = function(self) {return name;};
		//var Car_vtable = [Car_getName];
		//var car = {	vtable: Car_vtable,	name: true}
		//var name = car.vtable[0](car);

		
		List<VarDec> emptyVarDecs = new ArrayList<>();
		emptyVarDecs.add(new VarDec(new BooleanType(), new Variable("name")));
		List<Exp> expressions = new ArrayList<>();

		List<Exp> parameters = new ArrayList<>();
		//parameters.add(new BoolExp(true));

		ArrayList<VarDec> varDecs = new ArrayList<>();
		//varDecs.add(new VarDec(new BooleanType(), new Variable("bmw")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new BooleanType(), new MethodName("getName"), varDecs, new ReturnExpStatement(new VariableExp(new Variable("name")))));
	
		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Car"), new ArrayList<>()), new Variable("car")), new ClassExp(new ClassName("Car"), new ArrayList<>(), parameters)));
		statements.add(new VarDecAssignment(new VarDec(new BooleanType(), new Variable("name")), new VarMethodExp(new Variable("car"), new MethodName("getName"), expressions)));
		
		List<ClassDef> classes = new ArrayList<>();
		final ClassDef classOne = new ClassDef(new ClassName("Car"), 
			new Constructor(varDecs, new VarAssignment(new Variable("name"), new BoolExp(true)))
				, emptyVarDecs, methodDefs, new ArrayList<>());
		classes.add(classOne);

		Program program = new Program(classes, new Block(statements));
		assertResultProgram("var Car_getName = function(self) {return name;};var Car_vtable = [Car_getName];var car = {	vtable: Car_vtable,	name: true}var name = car.vtable[0](car);", program);
		}

	@Test
	public void testClasswithMethodsAndInstanceVar() throws IOException{
		//class Car{
			//Boolean name;
			//constructor(Boolean bmw) {
				//name = bmw;
			//}
			//public Boolean getName(){
				//return name;
			//}
			//public void setName(Boolean bmw){
				//name = bmw;
			//}
		//}

		//Car car = new Car(true);
		//Boolean n = car.getName();

		//OUTPUT
		//var Car_getName = function(self) {return name;};
		//var Car_setName = function(BooleanType bmw,self) {name = bmw};
		//var Car_vtable = [Car_getName, Car_setName];
		//var car = {	vtable: Car_vtable,	name: true}
		//var n = car.vtable[0](car);
		
		List<VarDec> emptyVarDecs = new ArrayList<>();
		List<VarDec> emptyyVarDecs = new ArrayList<>();
		List<Exp> expressions = new ArrayList<>();

		List<Exp> parameters = new ArrayList<>();
		parameters.add(new BoolExp(true));

		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new BooleanType(), new Variable("bmw")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new BooleanType(), new MethodName("getName"), emptyyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("name")))));
		methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setName"), varDecs, new VarAssignment( new Variable("name"),new VariableExp(new Variable("bmw")))));
	
		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Car"), new ArrayList<>()), new Variable("car")), new ClassExp(new ClassName("Car"), new ArrayList<>(), parameters)));
		statements.add(new VarDecAssignment(new VarDec(new BooleanType(), new Variable("n")), new VarMethodExp(new Variable("car"), new MethodName("getName"), expressions)));
		
		emptyVarDecs.add(new VarDec(new BooleanType(), new Variable("name")));
		List<ClassDef> classes = new ArrayList<>();
		final ClassDef classOne = new ClassDef(new ClassName("Car"), new Constructor(varDecs, new VarAssignment(new Variable("name"), new VariableExp(new Variable("bmw")))), emptyVarDecs, methodDefs, new ArrayList<>());
		classes.add(classOne);

		Program program = new Program(classes, new Block(statements));
		assertResultProgram("var Car_getName = function(self) {return name;};var Car_setName = function(BooleanType bmw,self) {name = bmw};var Car_vtable = [Car_getName, Car_setName];var car = {	vtable: Car_vtable,	name: true}var n = car.vtable[0](car);", program);
		}
	

	@Test
	public void testVirtualMethodCallWithInheritance() throws IOException {
		/*class ClassOne {
			int id;
			constructor(){
				id = 50;
			} 
			public int getId() {
				return id;
			}
		}
		class Bran extends ClassOne{
			
			constructor(){
				super();
			}
			public Boolean getName(){
				return false;
			}
		}

		ClassOne studenOne = new ClassOne();
		Bran student = new Bran();

		int resultID = studentOne.getId();
		int studentId = student.getId();
		*/

		/*
		output
		*/
 

		//var ClassOne_getId = function(self) {return id;};
		//var ClassOne_vtable = [ClassOne_getId];
		//var Bran_getName = function(self) {	return false};
		//var Bran_vtable = [ClassOne_getId, Bran_getName];
		//var studentOne = {	vtable: ClassOne_vtable,	id: 50}
		//var student = {	vtable: Bran_vtable,	id: 50}
		//var resultID = studentOne.vtable[0](studentOne);
		//var studentID = student.vtable[0](student);


		List<ClassDef> classes = new ArrayList<>();

		List<VarDec> emptyVarDecs = new ArrayList<>();
		ArrayList<VarDec> emptyyVarDecs = new ArrayList<>();

		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new IntType(), new Variable("id")));
		//varDecs.add(new VarDec(new IntType(), new Variable("gpa")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("id")))));

		VarAssignment var = new VarAssignment(new Variable("id"), new NumberExp(50));
		//VarDecAssignment varDecAssgnForConstructor = new VarDecAssignment(new VarDec(new IntType(), new Variable("number")), new NumberExp(50));

		classes.add(new ClassDef(new ClassName("ClassOne"), new Constructor(emptyyVarDecs, var), varDecs, methodDefs, new ArrayList<>()));
		


		List<VarDec> varDecsForSecondClass = new ArrayList<>();
		//varDecsForSecondClass.add(new VarDec(new BooleanType(), new Variable("name")));

		List<MethodDef> methodDefsForSecondClass = new ArrayList<>();
		methodDefsForSecondClass.add(new MethodDef(new PublicAccess(), new BooleanType(), new MethodName("getName"), emptyVarDecs, new ReturnExpStatement(new BoolExp(false))));

		classes.add(new ClassDef(new ClassName("Bran"), 
			new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), 
			new Extends(new ClassName("ClassOne"), new ArrayList<>()), 
			varDecsForSecondClass, 
			methodDefsForSecondClass, 
            new ArrayList<>()
        ));

		List<Exp> parameters = new ArrayList<>();
		List<Exp> expressions = new ArrayList<>();

		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("ClassOne"), new ArrayList<>()), new Variable("studentOne")), new ClassExp(new ClassName("ClassOne"), new ArrayList<>(), parameters)));
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Bran"), new ArrayList<>()), new Variable("student")), new ClassExp(new ClassName("Bran"), new ArrayList<>(), parameters)));
		

		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("resultID")), new VarMethodExp(new Variable("studentOne"), new MethodName("getId"), expressions)));
		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("studentID")), new VarMethodExp(new Variable("student"), new MethodName("getId"), expressions)));
		
		Statement st = new Block(statements);
		Program program = new Program(classes, st);
		assertResultProgram("var ClassOne_getId = function(self) {return id;};var ClassOne_vtable = [ClassOne_getId];var Bran_getName = function(self) {	return false};var Bran_vtable = [ClassOne_getId, Bran_getName];var studentOne = {	vtable: ClassOne_vtable,	id: 50}var student = {	vtable: Bran_vtable,	id: 50}var resultID = studentOne.vtable[0](studentOne);var studentID = student.vtable[0](student);", program);



	}

	@Test
	public void testOverloading() throws IOException {
		/*class ClassOne {
			int id;
			constructor(){
				id = 50;
			} 
			public void setId() {
				 id = 1;
			}
		}
		class Bran extends ClassOne{

			constructor(){
				super();
			}
			public void setId(int i){
				 id = i;
			}
		}

		ClassOne studenOne = new ClassOne();
		Bran student = new Bran();

		int resultID = studentOne.getId();
		Boolean studentName = student.getName();
		int studentId = student.getId();
		*/



		List<ClassDef> classes = new ArrayList<>();

		List<VarDec> emptyVarDecs = new ArrayList<>();
		ArrayList<VarDec> emptyyVarDecs = new ArrayList<>();

		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new IntType(), new Variable("id")));
		//varDecs.add(new VarDec(new IntType(), new Variable("gpa")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setId"), emptyVarDecs, new VarAssignment(new Variable("id"), new NumberExp(1))));

		VarAssignment var = new VarAssignment(new Variable("id"), new NumberExp(50));
		//VarDecAssignment varDecAssgnForConstructor = new VarDecAssignment(new VarDec(new IntType(), new Variable("number")), new NumberExp(50));

		classes.add(new ClassDef(new ClassName("ClassOne"), new Constructor(emptyyVarDecs, var), varDecs, methodDefs, new ArrayList<>()));
		
		List<VarDec> vardecc = new ArrayList<>();
		vardecc.add(new VarDec(new IntType(), new Variable("i")));

		List<VarDec> varDecsForSecondClass = new ArrayList<>();
		//varDecsForSecondClass.add(new VarDec(new BooleanType(), new Variable("name")));

		List<MethodDef> methodDefsForSecondClass = new ArrayList<>();
		methodDefsForSecondClass.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setId"), vardecc, new VarAssignment(new Variable("id"), new VariableExp(new Variable("i")))));

		List<Exp> n = new ArrayList<>();
		n.add(new NumberExp(2));
		classes.add(new ClassDef(new ClassName("Bran"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("ClassOne"), new ArrayList<>()), varDecsForSecondClass, methodDefsForSecondClass, new ArrayList<>()));


		List<Exp> parameters = new ArrayList<>();
		List<Exp> expressions = new ArrayList<>();

		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("ClassOne"), new ArrayList<>()), new Variable("studentOne")), new ClassExp(new ClassName("ClassOne"), new ArrayList<>(), parameters)));
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Bran"), new ArrayList<>()), new Variable("student")), new ClassExp(new ClassName("Bran"), new ArrayList<>(), parameters)));
		

		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("resultID")), new VarMethodExp(new Variable("studentOne"), new MethodName("getId"), expressions)));
		statements.add(new VarDecAssignment(new VarDec(new BooleanType(), new Variable("studentName")), new VarMethodExp(new Variable("student"), new MethodName("getName"), expressions)));

		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("studentID")), new VarMethodExp(new Variable("student"), new MethodName("getId"), expressions)));
		
		Statement st = new Block(statements);
		Program program = new Program(classes, st);
		assertResultProgram("", program);
		//assertResultProgram("var ClassOne_setId = function(self) {	id = 1};var ClassOne_vtable = [ClassOne_setId];var Bran_setId = function(self) {	id = i};var Bran_vtable = [ClassOne_setId];", program);



	}

	@Test
	public void testVirtualMethodCallWithoutInheritance() throws IOException {
		/*class ClassOne {
			int id;
			constructor(){
				id = 50;
			} 
			public int getId() {
				return id;
			}
		}
		class Bran extends ClassOne{
			constructor(){
				super();
			}
			public int getId() {
				return 5;
			}
		}

		Bran studen = new Bran();

		int resultID = student.getId(); //should be 5
		*/

		//var ClassOne_getId = function(self) {return id;};
		//var ClassOne_vtable = [ClassOne_getId];
		//var Bran_getId = function(self) {	return 5};
		//var Bran_vtable = [Bran_getId];
		//var student = {	vtable: Bran_vtable,	id: 50}
		//var resultID = student.vtable[0](student);


		List<ClassDef> classes = new ArrayList<>();

		List<VarDec> emptyVarDecs = new ArrayList<>();
		ArrayList<VarDec> emptyyVarDecs = new ArrayList<>();

		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new IntType(), new Variable("id")));
		//varDecs.add(new VarDec(new IntType(), new Variable("gpa")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("id")))));

		//VarDecAssignment varDecAssgnForConstructor = new VarDecAssignment(new VarDec(new IntType(), new Variable("number")), new NumberExp(50));
		VarAssignment var = new VarAssignment(new Variable("id"), new NumberExp(50));

		classes.add(new ClassDef(new ClassName("ClassOne"), new Constructor(emptyyVarDecs, var), varDecs, methodDefs, new ArrayList<>()));
		

		
		List<VarDec> varDecsForSecondClass = new ArrayList<>();
		//varDecsForSecondClass.add(new VarDec(new BooleanType(), new Variable("name")));
		List<MethodDef> methodDefss = new ArrayList<>();
		methodDefss.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new NumberExp(5))));
		
		classes.add(new ClassDef(new ClassName("Bran"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("ClassOne"), new ArrayList<>()), varDecsForSecondClass, methodDefss, new ArrayList<>()));


		List<Exp> parameters = new ArrayList<>();
		List<Exp> expressions = new ArrayList<>();

		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Bran"), new ArrayList<>()), new Variable("student")), new ClassExp(new ClassName("Bran"), new ArrayList<>(), parameters)));
		

		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("resultID")), new VarMethodExp(new Variable("student"), new MethodName("getId"), expressions)));
		
		Statement st = new Block(statements);
		Program program = new Program(classes, st);
		assertResultProgram("var ClassOne_getId = function(self) {return id;};var ClassOne_vtable = [ClassOne_getId];var Bran_getId = function(self) {	return 5};var Bran_vtable = [Bran_getId];var student = {	vtable: Bran_vtable,	id: 50}var resultID = student.vtable[0](student);", program);
	}

	@Test
    public void testInheritFromClassOneWithMultipleChildren() throws IOException {
    	/*class ClassOne {
			int id;
			constructor(){
				id = 50;
			} 
			public int getId() {
				return id;
			}
		}
		class Bran extends ClassOne{
			constructor(){
				super();
			}
			public int getId() {
				return 5;
			}
			public void setId(int i) {
				id = i;
			}
		}
		class Arya extends ClassOne{
			constructor(){
				super();
			}
			public Boolean getName(){
				return name;
			}
		}

		Bran bran = new Bran();
		Arya arya = new Arya();

		int iDisFifty= arya.getId();

		int iDisFive = bran.getId();
	

    	*/

		//[var ClassOne_getId = function(self) {return id;};
		//var ClassOne_vtable = [ClassOne_getId];
		//var Bran_getId = function(self) {	return 5};
		//var Bran_setId = function(IntType i,self) {	id = i};
		//var Bran_vtable = [Bran_getId, Bran_setId];
		//var Arya_getName = function(self) {	return name};
		//var Arya_vtable = [ClassOne_getId, Arya_getName];
		//var bran = {	vtable: Bran_vtable,	id: 50}
		//var arya = {	vtable: Arya_vtable,	id: 50}
		//var iDisFive = bran.vtable[0](bran);
		//var iDisFifty = arya.vtable[0](arya);


		List<ClassDef> classes = new ArrayList<>();

		List<VarDec> emptyVarDecs = new ArrayList<>();
		ArrayList<VarDec> emptyyVarDecs = new ArrayList<>();

		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new IntType(), new Variable("id")));
		//varDecs.add(new VarDec(new IntType(), new Variable("gpa")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("id")))));

		//VarDecAssignment varDecAssgnForConstructor = new VarDecAssignment(new VarDec(new IntType(), new Variable("id")), new NumberExp(50));
		VarAssignment var = new VarAssignment(new Variable("id"), new NumberExp(50));

		classes.add(new ClassDef(new ClassName("ClassOne"), new Constructor(emptyyVarDecs, var), varDecs, methodDefs, new ArrayList<>()));
		
		List<VarDec> param = new ArrayList<>();
		param.add(new VarDec(new IntType(), new Variable("i")));


		List<VarDec> varDecsForSecondClass = new ArrayList<>();
		//varDecsForSecondClass.add(new VarDec(new BooleanType(), new Variable("name")));
		List<MethodDef> methodDefss = new ArrayList<>();
		methodDefss.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new NumberExp(5))));
		methodDefss.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setId"), param, new VarAssignment(new Variable("id"), new VariableExp(new Variable("i")))));
		
		List<Exp> n = new ArrayList<>();
		n.add(new NumberExp(2));
		classes.add(new ClassDef(new ClassName("Bran"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("ClassOne"), new ArrayList<>()), varDecsForSecondClass, methodDefss, new ArrayList<>()));


		List<MethodDef> methodDefsForSecondClass = new ArrayList<>();
		methodDefsForSecondClass.add(new MethodDef(new PublicAccess(), new BooleanType(), new MethodName("getName"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("name")))));

		classes.add(new ClassDef(new ClassName("Arya"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("ClassOne"), new ArrayList<>()), varDecsForSecondClass, methodDefsForSecondClass, new ArrayList<>()));


		List<Exp> parameters = new ArrayList<>();
		List<Exp> expressions = new ArrayList<>();

		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Bran"), new ArrayList<>()), new Variable("bran")), new ClassExp(new ClassName("Bran"), new ArrayList<>(), parameters)));
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Arya"), new ArrayList<>()), new Variable("arya")), new ClassExp(new ClassName("Arya"), new ArrayList<>(), parameters)));
		

		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("iDisFive")), new VarMethodExp(new Variable("bran"), new MethodName("getId"), expressions)));
		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("iDisFifty")), new VarMethodExp(new Variable("arya"), new MethodName("getId"), expressions)));
		
		Statement st = new Block(statements);
		Program program = new Program(classes, st);
		assertResultProgram("var ClassOne_getId = function(self) {return id;};var ClassOne_vtable = [ClassOne_getId];var Bran_getId = function(self) {	return 5};var Bran_setId = function(IntType i,self) {	id = i};var Bran_vtable = [Bran_getId, Bran_setId];var Arya_getName = function(self) {	return name};var Arya_vtable = [ClassOne_getId, Arya_getName];var bran = {	vtable: Bran_vtable,	id: 50}var arya = {	vtable: Arya_vtable,	id: 50}var iDisFive = bran.vtable[0](bran);var iDisFifty = arya.vtable[0](arya);", program);
		}

    @Test
    public void testInheritanceWithGrandchildren() throws IOException {
    	/*class ClassOne {
			int id;
			constructor(){
				id = 50;
			} 
			public int getId() {
				return id;
			}
		}
		class Bran extends ClassOne{
			constructor(){
				super();
			}
			public int getId() {
				return 5;
			}
			public void setId(int i) {
				id = i;
			}
		}
		class Arya extends Bran{
			constructor(){
				super();
			}
			public Boolean getName(){
				return name;
			}
		}
	
		Bran bran = new Bran();
		Arya arya = new Arya();

		int iDisFive= bran.getId();
		int iDisFiveHereToo = arya.getId();


		var ClassOne_getId = function(self) {return id;};
		var ClassOne_vtable = [ClassOne_getId];
		var Bran_getId = function(self) {	return 5};
		var Bran_setId = function(IntType i,self) {	id = i};
		var Bran_vtable = [Bran_getId, Bran_setId];
		var Arya_getName = function(self) {	return name};
		var Arya_vtable = [Bran_getId, Bran_setId, Arya_getName];
		var bran = {	vtable: Bran_vtable,	id: 50}
		var arya = {	vtable: Arya_vtable,	id: 50}
		var iDisFive = bran.vtable[0](bran);
		var iDisFiveHereToo = arya.vtable[0](arya);
    	*/

		List<ClassDef> classes = new ArrayList<>();

		List<VarDec> emptyVarDecs = new ArrayList<>();
		ArrayList<VarDec> emptyyVarDecs = new ArrayList<>();

		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new IntType(), new Variable("id")));
		//varDecs.add(new VarDec(new IntType(), new Variable("gpa")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("id")))));

		//VarDecAssignment varDecAssgnForConstructor = new VarDecAssignment(new VarDec(new IntType(), new Variable("id")), new NumberExp(50));
		VarAssignment var = new VarAssignment(new Variable("id"), new NumberExp(50));

		classes.add(new ClassDef(new ClassName("ClassOne"), new Constructor(emptyyVarDecs, var), varDecs, methodDefs, new ArrayList<>()));

		List<VarDec> varDecsForSecondClass = new ArrayList<>();
		//varDecsForSecondClass.add(new VarDec(new BooleanType(), new Variable("name")));

		List<VarDec> param = new ArrayList<>();
		param.add(new VarDec(new IntType(), new Variable("i")));

		List<MethodDef> methodDefss = new ArrayList<>();
		methodDefss.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new NumberExp(5))));
		methodDefss.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setId"), param, new VarAssignment(new Variable("id"), new VariableExp(new Variable("i")))));
		
		List<Exp> n = new ArrayList<>();
		n.add(new NumberExp(2));
		classes.add(new ClassDef(new ClassName("Bran"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("ClassOne"), new ArrayList<>()), varDecsForSecondClass, methodDefss, new ArrayList<>()));


		List<MethodDef> methodDefsForSecondClass = new ArrayList<>();
		methodDefsForSecondClass.add(new MethodDef(new PublicAccess(), new BooleanType(), new MethodName("getName"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("name")))));

		classes.add(new ClassDef(new ClassName("Arya"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("Bran"), new ArrayList<>()), varDecsForSecondClass, methodDefsForSecondClass, new ArrayList<>()));


		List<Exp> parameters = new ArrayList<>();
		List<Exp> expressions = new ArrayList<>();

		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Bran"), new ArrayList<>()), new Variable("bran")), new ClassExp(new ClassName("Bran"), new ArrayList<>(), parameters)));
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Arya"), new ArrayList<>()), new Variable("arya")), new ClassExp(new ClassName("Arya"), new ArrayList<>(), parameters)));
		

		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("iDisFive")), new VarMethodExp(new Variable("bran"), new MethodName("getId"), expressions)));
		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("iDisFiveHereToo")), new VarMethodExp(new Variable("arya"), new MethodName("getId"), expressions)));
		
		Statement st = new Block(statements);
		Program program = new Program(classes, st);
		assertResultProgram("var ClassOne_getId = function(self) {return id;};var ClassOne_vtable = [ClassOne_getId];var Bran_getId = function(self) {	return 5};var Bran_setId = function(IntType i,self) {	id = i};var Bran_vtable = [Bran_getId, Bran_setId];var Arya_getName = function(self) {	return name};var Arya_vtable = [Bran_getId, Bran_setId, Arya_getName];var bran = {	vtable: Bran_vtable,	id: 50}var arya = {	vtable: Arya_vtable,	id: 50}var iDisFive = bran.vtable[0](bran);var iDisFiveHereToo = arya.vtable[0](arya);", program);
    }

    @Test
    public void testInheritanceWithSameMethodsInChildClassInDifferentOrder() throws IOException {
    	/*class ClassOne {
			int id;
			constructor(){
				id = 50;
			} 
			public int getId() {
				return id;
			}
		}
		class Bran extends ClassOne{
			
			constructor(){
				super();
			}
			public int getId() {
				return 5;
			}
			public void setId(int i) {
				id = i;
			}
		}
		class Arya extends Bran{
			constructor(){
				super();
			}
			public Boolean getName(){
				return name;
			}
			public int getId() {
				return 5;
			}
			public void setId(int i) {
				id = i;
			}
		}
	
		Bran bran = new Bran();
		Arya arya = new Arya();

		int iDisFive= arya.getId();

		int iDisFiveHereToo = bran.getId();

    	*/

		/*
		var ClassOne_getId = function(self) {return id;};
		var ClassOne_vtable = [ClassOne_getId];
		var Bran_getId = function(self) {	return 5};
		var Bran_setId = function(IntType i,self) {	id = i};
		var Bran_vtable = [Bran_getId, Bran_setId];
		var Arya_getName = function(self) {	return name};
		var Arya_getId = function(self) {	return 5};
		var Arya_setId = function(IntType i,self) {	id = i};
		var Arya_vtable = [Arya_getId, Arya_setId, Arya_getName];
		var bran = {	vtable: Bran_vtable,	id: 50}
		var arya = {	vtable: Arya_vtable,	id: 50}
		var iDisFive = bran.vtable[0](bran);
		var iDisFiveHereToo = arya.vtable[0](arya);

		*/
		List<ClassDef> classes = new ArrayList<>();

		List<VarDec> emptyVarDecs = new ArrayList<>();
		ArrayList<VarDec> emptyyVarDecs = new ArrayList<>();

		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new IntType(), new Variable("id")));
		//varDecs.add(new VarDec(new IntType(), new Variable("gpa")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("id")))));

		//VarDecAssignment varDecAssgnForConstructor = new VarDecAssignment(new VarDec(new IntType(), new Variable("id")), new NumberExp(50));
		VarAssignment var = new VarAssignment(new Variable("id"), new NumberExp(50));

		classes.add(new ClassDef(new ClassName("ClassOne"), new Constructor(emptyyVarDecs, var), varDecs, methodDefs, new ArrayList<>()));

		List<VarDec> varDecsForSecondClass = new ArrayList<>();
		//varDecsForSecondClass.add(new VarDec(new BooleanType(), new Variable("name")));

		List<VarDec> param = new ArrayList<>();
		param.add(new VarDec(new IntType(), new Variable("i")));

		List<MethodDef> methodDefss = new ArrayList<>();
		methodDefss.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new NumberExp(5))));
		methodDefss.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setId"), param, new VarAssignment(new Variable("id"), new VariableExp(new Variable("i")))));
		
		classes.add(new ClassDef(new ClassName("Bran"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("ClassOne"), new ArrayList<>()), varDecsForSecondClass, methodDefss, new ArrayList<>()));


		List<MethodDef> methodDefsForSecondClass = new ArrayList<>();
		methodDefsForSecondClass.add(new MethodDef(new PublicAccess(), new BooleanType(), new MethodName("getName"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("name")))));
		methodDefsForSecondClass.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new NumberExp(5))));
		methodDefsForSecondClass.add(new MethodDef(new PublicAccess(), new VoidType(), new MethodName("setId"), param, new VarAssignment(new Variable("id"), new VariableExp(new Variable("i")))));
		
		List<Exp> n = new ArrayList<>();
		n.add(new NumberExp(2));
		classes.add(new ClassDef(new ClassName("Arya"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("Bran"), new ArrayList<>()), varDecsForSecondClass, methodDefsForSecondClass, new ArrayList<>()));


		List<Exp> parameters = new ArrayList<>();
		List<Exp> expressions = new ArrayList<>();

		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Bran"), new ArrayList<>()), new Variable("bran")), new ClassExp(new ClassName("Bran"), new ArrayList<>(), parameters)));
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Arya"), new ArrayList<>()), new Variable("arya")), new ClassExp(new ClassName("Arya"), new ArrayList<>(), parameters)));
		

		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("iDisFive")), new VarMethodExp(new Variable("bran"), new MethodName("getId"), expressions)));
		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("iDisFiveHereToo")), new VarMethodExp(new Variable("arya"), new MethodName("getId"), expressions)));
		
		Statement st = new Block(statements);
		Program program = new Program(classes, st);
		//assertResultProgram("var ClassOne_getId = function(self) {	return id};var ClassOne_vtable = [ClassOne_getId];var Bran_getId = function(self) {	return 5};var Bran_setId = function(self) {	id = i};var Bran_vtable = [Bran_getId, Bran_setId];var Arya_getName = function(self) {	return name};var Arya_getId = function(self) {	return 5};var Arya_setId = function(self) {	id = i};var Arya_vtable = [Arya_getId, Arya_setId, Arya_getName];", program);
		assertResultProgram("var ClassOne_getId = function(self) {return id;};var ClassOne_vtable = [ClassOne_getId];var Bran_getId = function(self) {	return 5};var Bran_setId = function(IntType i,self) {	id = i};var Bran_vtable = [Bran_getId, Bran_setId];var Arya_getName = function(self) {	return name};var Arya_getId = function(self) {	return 5};var Arya_setId = function(IntType i,self) {	id = i};var Arya_vtable = [Arya_getId, Arya_setId, Arya_getName];var bran = {	vtable: Bran_vtable,	id: 50}var arya = {	vtable: Arya_vtable,	id: 50}var iDisFive = bran.vtable[0](bran);var iDisFiveHereToo = arya.vtable[0](arya);", program);
    }



    @Test
    public void testMultipleClasseswithInheritance() throws IOException {
    	/*class ClassOne {
			int id;
			constructor(){
				id = 50;
			} 
			public int getId() {
				return id;
			}
		}
		class Bran extends ClassOne{
			constructor(){
				super();
			}
			public int getId() {
				return 5;
			}
		}

		class ClassTwo {
			int a;
			constructor(){
				 a = 50;
			} 
			public int getA() {
				return a;
			}
		}
		class Arya extends ClassTwo{
			constructor(){
				super();
			}
			public int getName() {
				return 5;
			}
		}
		
	
		Bran b = new Bran();
		Arya a = new Arya();
		int iDisFive = b.getId();
		int fifty = a.getA();



		var ClassOne_getId = function(self) {return id;};
		var ClassOne_vtable = [ClassOne_getId];
		var Bran_getId = function(self) {	return 5};
		var Bran_vtable = [Bran_getId];
		var ClassTwo_getA = function(self) {return a;};
		var ClassTwo_vtable = [ClassTwo_getA];
		var Arya_getName = function(self) {	return 5};
		var Arya_vtable = [ClassTwo_getA, Arya_getName];
		var b = {	vtable: Bran_vtable,	id: 50}
		var a = {	vtable: Arya_vtable,	a: 50}
		var iDisFive = b.vtable[0](b);
		var fifty = a.vtable[0](a);
    	*/

    	List<ClassDef> classes = new ArrayList<>();

		List<VarDec> emptyVarDecs = new ArrayList<>();
		ArrayList<VarDec> emptyyVarDecs = new ArrayList<>();

		ArrayList<VarDec> varDecs = new ArrayList<>();
		varDecs.add(new VarDec(new IntType(), new Variable("id")));
		//varDecs.add(new VarDec(new IntType(), new Variable("gpa")));

		List<MethodDef> methodDefs = new ArrayList<>();
		methodDefs.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("id")))));

		//VarDecAssignment varDecAssgnForConstructor = new VarDecAssignment(new VarDec(new IntType(), new Variable("number")), new NumberExp(50));
		VarAssignment var = new VarAssignment(new Variable("id"), new NumberExp(50));

		classes.add(new ClassDef(new ClassName("ClassOne"), new Constructor(emptyyVarDecs, var), varDecs, methodDefs, new ArrayList<>()));
		


		List<VarDec> varDecsForSecondClass = new ArrayList<>();
		//varDecsForSecondClass.add(new VarDec(new BooleanType(), new Variable("name")));
		List<MethodDef> methodDefss = new ArrayList<>();
		methodDefss.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getId"), emptyVarDecs, new ReturnExpStatement(new NumberExp(5))));
		
		classes.add(new ClassDef(new ClassName("Bran"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("ClassOne"), new ArrayList<>()), varDecsForSecondClass, methodDefss, new ArrayList<>()));

		ArrayList<VarDec> varDecss = new ArrayList<>();
		varDecss.add(new VarDec(new IntType(), new Variable("a")));
		//varDecss.add(new VarDec(new IntType(), new Variable("b")));

		List<MethodDef> methodDeff = new ArrayList<>();
		methodDeff.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getA"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("a")))));
		VarAssignment varr = new VarAssignment(new Variable("a"), new NumberExp(50));

		classes.add(new ClassDef(new ClassName("ClassTwo"), new Constructor(emptyyVarDecs, varr), varDecss, methodDeff, new ArrayList<>()));


		List<MethodDef> methodDefsForSecondClass = new ArrayList<>();
		methodDefsForSecondClass.add(new MethodDef(new PublicAccess(), new BooleanType(), new MethodName("getName"), emptyVarDecs, new ReturnExpStatement(new NumberExp(5))));

		classes.add(new ClassDef(new ClassName("Arya"), new Constructor(emptyyVarDecs, new SuperStatement(new ArrayList<Exp>())), new Extends(new ClassName("ClassTwo"), new ArrayList<>()), varDecsForSecondClass, methodDefsForSecondClass, new ArrayList<>()));


		List<Exp> parameters = new ArrayList<>();
		List<Exp> expressions = new ArrayList<>();

		List<Statement> statements = new ArrayList<>();
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Bran"), new ArrayList<>()), new Variable("b")), new ClassExp(new ClassName("Bran"), new ArrayList<>(), parameters)));
		statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Arya"), new ArrayList<>()), new Variable("a")), new ClassExp(new ClassName("Arya"), new ArrayList<>(), parameters)));
				

		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("iDisFive")), new VarMethodExp(new Variable("b"), new MethodName("getId"), expressions)));
		statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("fifty")), new VarMethodExp(new Variable("a"), new MethodName("getA"), expressions)));
		
		Statement st = new Block(statements);
		Program program = new Program(classes, st);
		assertResultProgram("var ClassOne_getId = function(self) {return id;};var ClassOne_vtable = [ClassOne_getId];var Bran_getId = function(self) {	return 5};var Bran_vtable = [Bran_getId];var ClassTwo_getA = function(self) {return a;};var ClassTwo_vtable = [ClassTwo_getA];var Arya_getName = function(self) {	return 5};var Arya_vtable = [ClassTwo_getA, Arya_getName];var b = {	vtable: Bran_vtable,	id: 50}var a = {	vtable: Arya_vtable,	a: 50}var iDisFive = b.vtable[0](b);var fifty = a.vtable[0](a);", program);
	}

	@Test
	public void testFourthGenerationInheritanceVtables() throws IOException {
		/*
		Class One{
			int one;
			constructor(int i) {
				one = i;
			}
			public int getOne() {
				return one;
			}
		}

		Class Two extends One {
			constructor(int i) {
				super(i);
			}
			public int getTwo() {
				return two;
			}
			public int getOne() {
				return 1;
			}

		}

		Class Three extends Two{
			constructor(int i) {
				super(i);
			} 
			public int getThree() {
				return three;
			}
		}

		Class Four extends Three {
			
			constructor(int i) {
				super(i);
			}
			public int getOne() {
				return 1;
			}
			public int getFour() {
				return four;
			}
		}

		

		One o = new One(1);
		Two t = new Two(2);
		One th = new Three(3);
		Four f = new Four(4);
		int one = f.getOne();


		var One_getOne = function(self) {return one;};
		var One_vtable = [One_getOne];
		var Two_getTwo = function(self) {	return two};
		var Two_getOne = function(self) {	return 1};
		var Two_vtable = [Two_getOne, Two_getTwo];
		var Three_getThree = function(self) {	return three};
		var Three_vtable = [Two_getOne, Two_getTwo, Three_getThree];
		var Four_getOne = function(self) {	return 1};
		var Four_getFour = function(self) {	return four};
		var Four_vtable = [Four_getOne, Two_getTwo, Three_getThree, Four_getFour];
		var o = {	vtable: One_vtable,	one: 1}
		var t = {	vtable: Two_vtable,	one: 2}
		var th = {	vtable: One_vtable,	one: 3}
		var f = {	vtable: Four_vtable,	one: 4}
		var one = f.vtable[0](f);

		*/
		List<VarDec> emptyVarDecs = new ArrayList<>();
		List<ClassDef> classes = new ArrayList<>();

		List<Statement> statements = new ArrayList<>();

		List<Exp> parametersOne = new ArrayList<>();
		parametersOne.add(new NumberExp(1));
			statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("One"), new ArrayList<>()), new Variable("o")), new ClassExp(new ClassName("One"), new ArrayList<>(), parametersOne)));

		List<Exp> parametersTwo = new ArrayList<>();
		parametersTwo.add(new NumberExp(2));
			statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Two"), new ArrayList<>()), new Variable("t")), new ClassExp(new ClassName("Two"), new ArrayList<>(), parametersTwo)));

		List<Exp> parametersThree = new ArrayList<>();
		parametersThree.add(new NumberExp(3));
			statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("One"), new ArrayList<>()), new Variable("th")), new ClassExp(new ClassName("Three"), new ArrayList<>(), parametersThree)));

		List<Exp> parametersFour = new ArrayList<>();
		parametersFour.add(new NumberExp(4));
			statements.add(new VarDecAssignment(new VarDec(new ClassType(new ClassName("Four"), new ArrayList<>()), new Variable("f")), new ClassExp(new ClassName("Four"), new ArrayList<>(), parametersFour)));

List<Exp> expressions = new ArrayList<>();
			statements.add(new VarDecAssignment(new VarDec(new IntType(), new Variable("one")), new VarMethodExp(new Variable("f"), new MethodName("getOne"), expressions)));
		
		ArrayList<VarDec> constructorVarDec = new ArrayList<>();
		constructorVarDec.add(new VarDec(new IntType(), new Variable("i")));

		List<VarDec> varDecsOne = new ArrayList<>();
		varDecsOne.add(new VarDec(new IntType(), new Variable("one")));

		List<MethodDef> methodDefsOne = new ArrayList<>();
		methodDefsOne.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getOne"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("one")))));

		ClassDef classOne = new ClassDef(new ClassName("One"), new Constructor(constructorVarDec, new VarAssignment(new Variable("one"), new VariableExp(new Variable("i")))), varDecsOne, methodDefsOne, new ArrayList<>());


		List<VarDec> varDecsTwo = new ArrayList<>();
		//varDecsTwo.add(new VarDec(new IntType(), new Variable("two")));

		List<MethodDef> methodDefsTwo = new ArrayList<>();
		methodDefsTwo.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getTwo"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("two")))));
		methodDefsTwo.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getOne"), emptyVarDecs, new ReturnExpStatement(new NumberExp(1)) ));

		List<Exp> n = new ArrayList<>();
		n.add(new VariableExp(new Variable("i")));
		ClassDef classTwo = new ClassDef(new ClassName("Two"), new Constructor(constructorVarDec, new SuperStatement(n)), new Extends(new ClassName("One"), new ArrayList<>()), varDecsTwo, methodDefsTwo, new ArrayList<>());
		
		List<VarDec> varDecsThree = new ArrayList<>();
		//varDecsThree.add(new VarDec(new IntType(), new Variable("three")));

		List<MethodDef> methodDefsThree = new ArrayList<>();
		methodDefsThree.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getThree"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("three")))));
		
	
		ClassDef classThree = new ClassDef(new ClassName("Three"), new Constructor(constructorVarDec, new SuperStatement(n)), new Extends(new ClassName("Two"), new ArrayList<>()), varDecsThree, methodDefsThree, new ArrayList<>());
		
		
		List<VarDec> varDecsFour = new ArrayList<>();
		//varDecsFour.add(new VarDec(new IntType(), new Variable("four")));

		List<MethodDef> methodDefsFour = new ArrayList<>();
		methodDefsFour.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getOne"), emptyVarDecs, new ReturnExpStatement(new NumberExp(1))));
		methodDefsFour.add(new MethodDef(new PublicAccess(), new IntType(), new MethodName("getFour"), emptyVarDecs, new ReturnExpStatement(new VariableExp(new Variable("four")))));
		
		
		ClassDef classFour = new ClassDef(new ClassName("Four"), new Constructor(constructorVarDec, new SuperStatement(n)), new Extends(new ClassName("Three"), new ArrayList<>()), varDecsFour, methodDefsFour, new ArrayList<>());

		classes.add(classOne);
		classes.add(classTwo);
		classes.add(classThree);
		classes.add(classFour);

		Statement block = new Block(statements);
		Program program = new Program(classes, block);
		assertResultProgram("var One_getOne = function(self) {return one;};var One_vtable = [One_getOne];var Two_getTwo = function(self) {	return two};var Two_getOne = function(self) {	return 1};var Two_vtable = [Two_getOne, Two_getTwo];var Three_getThree = function(self) {	return three};var Three_vtable = [Two_getOne, Two_getTwo, Three_getThree];var Four_getOne = function(self) {	return 1};var Four_getFour = function(self) {	return four};var Four_vtable = [Four_getOne, Two_getTwo, Three_getThree, Four_getFour];var o = {	vtable: One_vtable,	one: 1}var t = {	vtable: Two_vtable,	one: 2}var th = {	vtable: One_vtable,	one: 3}var f = {	vtable: Four_vtable,	one: 4}var one = f.vtable[0](f);", program);
	}
}