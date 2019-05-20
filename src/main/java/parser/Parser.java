package j2script;

import j2script.tokens.*;
import j2script.access.*;
import j2script.declarations.*;
import j2script.expressions.*;
import j2script.names.*;
import j2script.operators.*;
import j2script.statements.*;
import j2script.types.*;
import j2script.ParserException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Parser {
    // begin static variables
    private static final Map<Token, Op> ADDITIVE_OP_MAP =
      new HashMap<Token, Op>() {{
          put(new AddToken(), new PlusOp());
          put(new MinusToken(), new MinusOp());
      }};
    private static final Map<Token, Op> MULTIPLICATIVE_OP_MAP =
      new HashMap<Token, Op>() {{
          put(new MultiplyToken(), new MultOp());
          put(new DivToken(), new DivOp());
      }};
    private static final Map<Token, Type> TYPE_MAP =
      new HashMap<Token, Type>() {{
          put(new IntToken(), new IntType());
          put(new BooleanToken(), new BooleanType());
          put(new StringToken(), new StringType());
      }};
      private static final Map<Token, Type> RETURNTYPE_MAP =
      new HashMap<Token, Type>() {{
          put(new IntToken(), new IntType());
          put(new BooleanToken(), new BooleanType());
          put(new StringToken(), new StringType());
          put(new VoidToken(), new VoidType());
      }};
      private static final Map<Token, Access> ACCESS_MAP =
      new HashMap<Token, Access>() {{
          put(new PublicToken(), new PublicAccess());
          put(new PrivateToken(), new PrivateAccess());
      }};

    // end static variables

    //begin instance variables
    private final List<Token> tokens;
    //The stack is to keep track of curly braces, each entry counts as a left curly brace
    Stack<Integer> CurlyBraceStack = new Stack<Integer>();

    // end instance variables

    public Parser(final List<Token> tokens) {
        this.tokens = tokens;
    }

    //The result of a parse.
    private class ParseResult<A> {
        public final A result; // AST node
        public final int tokenPos; // resulting position
        public ParseResult(final A result,
                           final int tokenPos) {
            this.result = result;
            this.tokenPos = tokenPos;
        }
    } // ParseResult

    // handles something (op something)*
    private abstract class ParseBinop {
        private final Map<Token, Op> opMap;
        public ParseBinop(final Map<Token, Op> opMap) {
            this.opMap = opMap;
        }

        public abstract ParseResult<Exp> parseSomething(final int startPos) throws ParserException;
        //
        public ParseResult<Exp> parse(final int startPos) throws ParserException 
        {
            int pos = startPos;
            ParseResult<Exp> finalResult = parseSomething(pos);
            if (finalResult == null)
                return null;            

            ParseResult<Exp> currentResult = null;
            while (finalResult.tokenPos < tokens.size()) 
            {
                final Op op = opMap.get(getToken(finalResult.tokenPos));
                if (op != null) 
                {
                    // we have an op.  We MUST have a right; continue parsing.
                    final ParseResult<Exp> right = parseSomething(finalResult.tokenPos + 1);
                    finalResult = new ParseResult<Exp>(new BinopExp(finalResult.result,
                                                                     op,
                                                                     right.result),
                                                                     right.tokenPos);
                }
                else
                    // we don't have an op.  return whatever we have
                    return finalResult;
            }//end while there are tokens

            return finalResult;
        } // parse
    } // ParseBinop

    private class ParseAdditive extends ParseBinop {
        public ParseAdditive() {
            super(ADDITIVE_OP_MAP);
        }

        public ParseResult<Exp> parseSomething(final int startPos) throws ParserException {
            return parseMultiplicative(startPos);
            //=>ParseMultiplicative().parse(startPos)=>parseSomething(0)=>parsePrimary()wtf
        }
    }//Parse Additive

    private class ParseMultiplicative extends ParseBinop {
        public ParseMultiplicative() {
            super(MULTIPLICATIVE_OP_MAP);
        }

        public ParseResult<Exp> parseSomething(final int startPos) throws ParserException {
            return parsePrimary(startPos);
        }
    }//ParseMultiplicative

    private ParseResult<Exp> parseAdditive(final int startPos) throws ParserException {
        return new ParseAdditive().parse(startPos);
    }
    
    private ParseResult<Exp> parseMultiplicative(final int startPos) throws ParserException {
        return new ParseMultiplicative().parse(startPos);
    }

    private ParseResult<Exp> parsePrimary(final int startPos) throws ParserException {
        final Token current = getToken(startPos);
        Exp resultExp;
        int resultPos;

        if (current instanceof NumberToken) {
            resultExp = new NumberExp(((NumberToken)current).number);
            resultPos = startPos + 1;
        } 
        else if (current instanceof VariableToken) {
            resultExp = new VariableExp(new Variable(((VariableToken)current).name));
            resultPos = startPos + 1;
        } 
        else if (current instanceof LeftParenToken) {
            final ParseResult<Exp> nested = parseExp(startPos + 1);
            assertTokenAtPos(new RightParenToken(), nested.tokenPos);
            resultExp = nested.result;
            resultPos = nested.tokenPos + 1;
        }
        else {
            throw new ParserException("Expected primary at " + startPos);
        }

        return new ParseResult<Exp>(resultExp, resultPos);
    }

    // Gets the token at the given position
    private Token getToken(final int pos) throws ParserException {
        assert(pos >= 0);
        if (pos < tokens.size()) {
            return tokens.get(pos);
        } else {
            throw new ParserException("No token at position " + pos);
        }
    }

    // Asserts that the given token appears at the given position
    private void assertTokenAtPos(final Token token, final int pos) throws ParserException {
        if (!getToken(pos).equals(token)) {
            throw new ParserException("Expected " + token.toString() + " at pos " + pos);
        }
    }
    private void ensureTokenIs(final int position, final Token expected) throws ParserException {
        final Token tokenHere = tokens.get(position);
        if (!expected.equals(tokenHere)) {
            throw new ParserException(expected.toString() + " expected at position: " + position);
        }
    }
    private boolean ensureToken(final int position, final Token expected) throws ParserException {
        final Token tokenHere = tokens.get(position);
        System.out.println("We are in ensure token");
        if (!expected.equals(tokenHere)) {
            return false;
        }
        else{
            return true;
        }
    }
    private boolean isSingleStmt(final List<Statement> stmt){
        //Single
        if (stmt.size() == 1){
            return true;
        }
        else{
            return false;
        }
    }

    /***************************************************
     Do not modify above lines unless adding static maps 
     ***************************************************/

    private ParseResult<Exp> parseExp(final int startPos) throws ParserException {
        return parseAdditive(startPos);
    }
    private ParseResult<Exp> ParseExpNonBinop(final int startPos) throws ParserException {
        int resultpos = startPos;
        if(ensureToken(resultpos, new NewToken())){
            ClassName name;
            ArrayList<Exp> parameters = new ArrayList<>();
            ensureTokenIs(resultpos + 1, new VariableToken(tokens.get(resultpos).toString()));
            name = new ClassName(tokens.get(resultpos).toString());
            ensureTokenIs(resultpos + 2, new LeftParenToken());
            resultpos = resultpos + 3;
            while (!ensureToken(resultpos, new RightParenToken())){
                if((ensureToken(resultpos, new VariableToken()) && !ensureToken(resultpos + 1,new LeftParenToken())) 
                || ensureToken(resultpos, new LeftParenToken())
                || tokens.get(resultpos) instanceof NumberToken){
                    final ParseResult<Exp> param = parseAdditive(resultpos);
                    parameters.add(param.result);
                    resultpos = param.tokenPos;
                    if (ensureToken(resultpos, new CommaToken())){
                        resultpos++;
                        if (ensureToken(resultpos, new RightParenToken())){
                            throw new ParserException("You must have another parameter at " + resultpos);
                        }
                    }
                }
                else{
                    final ParseResult<Exp> param = ParseExpNonBinop(resultpos);
                    parameters.add(param.result);
                    resultpos = param.tokenPos;
                    if (ensureToken(resultpos, new CommaToken())){
                        resultpos++;
                        if (ensureToken(resultpos, new RightParenToken())){
                            throw new ParserException("You must have another parameter at " + resultpos);
                        }
                    }
                }
                
            }
            resultpos++;
            final ClassExp e = new ClassExp(name,new ArrayList<>(),parameters);  // NOT RIGHT
            return new ParseResult<Exp>(e, resultpos);
        }
        else if((ensureToken(resultpos, new VariableToken()) && ensureToken(resultpos + 1,new LeftParenToken()))){
            MethodName name;
            ArrayList<Exp> parameters = new ArrayList<>();
            ensureTokenIs(resultpos + 1, new VariableToken(tokens.get(resultpos).toString()));
            name = new MethodName(tokens.get(resultpos).toString());
            ensureTokenIs(resultpos + 2, new LeftParenToken());
            resultpos = resultpos + 3;
            while (!ensureToken(resultpos, new RightParenToken())){
                if((ensureToken(resultpos, new VariableToken()) && !ensureToken(resultpos + 1,new LeftParenToken())) 
                || ensureToken(resultpos, new LeftParenToken())
                || tokens.get(resultpos) instanceof NumberToken){
                    final ParseResult<Exp> param = parseAdditive(resultpos);
                    parameters.add(param.result);
                    resultpos = param.tokenPos;
                    if (ensureToken(resultpos, new CommaToken())){
                        resultpos++;
                        if (ensureToken(resultpos, new RightParenToken())){
                            throw new ParserException("You must have another parameter at " + resultpos);
                        }
                    }
                }
                else{
                    final ParseResult<Exp> param = ParseExpNonBinop(resultpos);
                    parameters.add(param.result);
                    resultpos = param.tokenPos;
                    if (ensureToken(resultpos, new CommaToken())){
                        resultpos++;
                        if (ensureToken(resultpos, new RightParenToken())){
                            throw new ParserException("You must have another parameter at " + resultpos);
                        }
                    }
                }
                
            }
            resultpos++;
            final MethodExp e = new MethodExp(name,parameters);
            return new ParseResult<Exp>(e, resultpos);
        }
        else{
            final ParseResult<Exp> e = parseExp(resultpos);
            if(e == null){
                throw new ParserException("Not an exp at " + resultpos);
            }
            else{
                return new ParseResult<Exp>(e.result, resultpos);
            }
        }
    }

    private ParseResult<Statement> parseStatement(final int startPos) throws ParserException {
        int resultpos = startPos;
        Statement stmt;
        //This is a block statement
        if (ensureToken(resultpos, new LeftCurlyToken())){
            final ParseResult<Statement> block = parseBlock(resultpos);
            stmt = block.result;
            resultpos = block.tokenPos;
            return new ParseResult<Statement>(stmt, resultpos);
        }
        //Parse additive/primary/binop
        else if((ensureToken(resultpos, new VariableToken()) && !ensureToken(resultpos + 1,new LeftParenToken())) 
                || ensureToken(resultpos, new LeftParenToken())
                || tokens.get(resultpos) instanceof NumberToken){
                    final ParseResult<Exp> stment = parseAdditive(resultpos);
                    stmt = stment.result;
                    resultpos = stment.tokenPos;
                    ensureTokenIs(resultpos, new SemiToken());
                    resultpos++;
                    return new ParseResult<Statement>(stmt,resultpos);
        }
        //parse new constructor
        else if(ensureToken(resultpos, new NewToken())){
            final ParseResult<Exp> exp = ParseExpNonBinop(resultpos);
            stmt = exp.result;
            resultpos = exp.tokenPos;
            ensureTokenIs(resultpos, new SemiToken());
            resultpos++;
            return new ParseResult<Statement>(stmt,resultpos);
        }
        //methodcall
        else if((ensureToken(resultpos, new VariableToken()) && ensureToken(resultpos + 1,new LeftParenToken()))){
            final ParseResult<Exp> exp = ParseExpNonBinop(resultpos);
            stmt = exp.result;
            resultpos = exp.tokenPos;
            ensureTokenIs(resultpos, new SemiToken());
            resultpos++;
            return new ParseResult<Statement>(stmt,resultpos);
        }
        //return
        else if(ensureToken(resultpos, new ReturnToken())){
            final ReturnVoidStatement rvs = new ReturnVoidStatement();
            ensureTokenIs(++resultpos, new SemiToken());
            resultpos++;
            return new ParseResult<Statement>(rvs,++resultpos);
        }
        else if(ensureToken(resultpos, new BreakToken())){
            final BreakStatement bs = new BreakStatement();
            ensureTokenIs(++resultpos, new SemiToken());
            resultpos++;
            return new ParseResult<Statement>(bs,++resultpos);
        }
        else if(ensureToken(resultpos, new PrintToken())){
            ensureTokenIs(++resultpos, new LeftParenToken());
            if((ensureToken(resultpos, new VariableToken()) && !ensureToken(resultpos + 1,new LeftParenToken())) 
            || ensureToken(resultpos, new LeftParenToken())
            || tokens.get(resultpos) instanceof NumberToken
            || ensureToken(resultpos, new NewToken()) 
            || (ensureToken(resultpos, new VariableToken()) && ensureToken(resultpos + 1,new LeftParenToken()))){
                ParseResult<Exp> e = ParseExpNonBinop(++resultpos);
                ensureTokenIs(++resultpos, new RightParenToken());
                ensureTokenIs(++resultpos, new SemiToken());
                resultpos++;
                final PrintStatement ps = new PrintStatement(new StringExp(new StringName(e.toString())));
                return new ParseResult<Statement>(ps,resultpos);
            }
        }
        else if(ensureToken(resultpos, new IfToken())){
            ensureTokenIs(++resultpos, new LeftParenToken());
            final ParseResult<Exp> guard = ParseExpNonBinop(++resultpos);
            resultpos = guard.tokenPos;
            ensureTokenIs(resultpos++, new RightParenToken());
            final ParseResult<Statement> ifTrue = parseStatement(resultpos);
            resultpos = ifTrue.tokenPos;
            ensureTokenIs(resultpos++, new ElseToken());
            final ParseResult<Statement> ifFalse = parseStatement(resultpos);
            resultpos = ifFalse.tokenPos;
            final IfStatement If = new IfStatement(guard.result,ifTrue.result,ifFalse.result);
            return new ParseResult<Statement> (If, resultpos);
        }
        return null;
    }
    private ParseResult<Statement> parseBlock(final int startPos) throws ParserException{
        List<Statement> stmts= new ArrayList<Statement>();
        Block block;
        int resultpos = startPos;
        CurlyBraceStack.push(1);
        resultpos++;
        while (!ensureToken(resultpos, new RightCurlyToken())){
            if(tokens.get(resultpos) instanceof NumberToken || /*ensureToken(resultpos, new BooleanToken())
            ||*/ ensureToken(resultpos, new VariableToken()) || ensureToken(resultpos, new NewToken()) 
            ||/* ensureToken(resultpos, new StringToken()) ||*/ ensureToken(resultpos, new ReturnToken())
            || ensureToken(resultpos, new BreakToken()) || ensureToken(resultpos, new PrintToken()) 
            || ensureToken(resultpos, new IntToken()) || ensureToken(resultpos, new BooleanToken())
            || ensureToken(resultpos, new StringToken()) || ensureToken(resultpos, new LeftCurlyToken())){
                final ParseResult<Statement> stmt = parseStatement(resultpos);
                stmts.add(stmt.result);
                resultpos = stmt.tokenPos;
            }
            else{
                throw new ParserException("This is not a valid statement at " + resultpos);
            }
        }
        CurlyBraceStack.pop();
        block = new Block(stmts);
        return new ParseResult<Statement>(block, ++resultpos);
    }

    private ParseResult<MethodDef> parseMethodDef(final int startPos) throws ParserException {
        MethodDef methodDef;
        Access access;
        Type returnType;
        MethodName name;
        List<VarDec> varDecs;
        Statement statement;
        int resultpos = startPos;
        access = ACCESS_MAP.get(getToken(resultpos));
        returnType = RETURNTYPE_MAP.get(getToken(++resultpos));
        name = new MethodName(tokens.get(++resultpos).toString());
        ensureTokenIs(++resultpos,new LeftParenToken());
        resultpos++;
        do{
            varDecs = new ArrayList<VarDec>();
            if ((ensureToken(resultpos, new BooleanToken()) ||
            ensureToken(resultpos, new IntToken()) ||
            ensureToken(resultpos, new StringToken())) &&
            ensureToken(resultpos + 1, new VariableToken())){
                final Type type = TYPE_MAP.get(getToken(resultpos));
                varDecs.add(new VarDec(type, new Variable(tokens.get(resultpos+2).toString())));
                resultpos = resultpos + 2;
            }
            else{
                throw new ParserException("This is not a valid var dec at " + resultpos);
            }
        }while(ensureToken(resultpos, new CommaToken()));
        ensureTokenIs(resultpos, new RightParenToken());
        if(tokens.get(resultpos) instanceof NumberToken || /*ensureToken(resultpos, new BooleanToken())
        ||*/ ensureToken(resultpos, new VariableToken()) || ensureToken(resultpos, new NewToken()) 
        ||/* ensureToken(resultpos, new StringToken()) ||*/ ensureToken(resultpos, new ReturnToken())
        || ensureToken(resultpos, new BreakToken()) || ensureToken(resultpos, new PrintToken()) 
        || ensureToken(resultpos, new IntToken()) || ensureToken(resultpos, new BooleanToken())
        || ensureToken(resultpos, new StringToken()) || ensureToken(resultpos, new LeftCurlyToken())){
            final ParseResult<Statement> stmt = parseStatement(resultpos);
            statement = stmt.result;
            resultpos = stmt.tokenPos;
            methodDef = new MethodDef(access,returnType,name,varDecs,statement);

        }
        else{
            throw new ParserException("This is not a valid statement at " + resultpos);
        }
        return new ParseResult<MethodDef>(methodDef, resultpos);
    }

    private ParseResult<ClassDef> parseClassDef(final int startPos) throws ParserException {
        int resultpos = startPos;
        ClassDef resultClassDef = null;
        ClassName extendedClass = null;
        Constructor constructor = null;
        List<VarDec> vardecs = new ArrayList<VarDec>();
        Statement statement = null;
        List<MethodDef> methodDefs = new ArrayList<MethodDef>();
        final ClassName name = new ClassName(tokens.get(+resultpos).toString());
        if (ensureToken(++resultpos, new ExtendsToken())){
            extendedClass = new ClassName(tokens.get(++resultpos).toString());
        }
        else{
            //Extended class is null, do nothing.
        }
        ensureTokenIs(++resultpos, new LeftCurlyToken());
        CurlyBraceStack.push(1);
        resultpos++;
        while (!CurlyBraceStack.empty()){
            //this is a a vardec
            if ((ensureToken(resultpos, new BooleanToken()) ||
                ensureToken(resultpos, new IntToken()) ||
                ensureToken(resultpos, new StringToken())) &&
                ensureToken(resultpos + 1, new VariableToken()) &&
                ensureToken(resultpos + 2, new SemiToken())){
                    final Type type = TYPE_MAP.get(getToken(resultpos));
                    vardecs.add(new VarDec(type, new Variable(tokens.get(resultpos+2).toString())));
                    resultpos = resultpos + 3;
                }
                //This is a constructor
            else if(ensureToken(resultpos, new ConstructorToken())){
                ensureTokenIs(resultpos+1, new LeftParenToken());
                ArrayList<VarDec> parameters = new ArrayList<VarDec>();
                int currentpos = resultpos + 2;
                while (ensureToken(currentpos, new BooleanToken()) ||
                ensureToken(currentpos, new IntToken()) ||
                ensureToken(currentpos, new StringToken())){
                    final Type type = TYPE_MAP.get(getToken(currentpos));
                    final VarDec varDec = new VarDec(type,
                    new Variable(tokens.get(currentpos+1).toString()));
                    parameters.add(varDec);
                    currentpos = currentpos + 2;
                    //If there is a comma, more parameters
                    if (ensureToken(currentpos, new CommaToken())){
                        currentpos++;
                    }
                    //Currentpos should then have a right parenthesis
                    resultpos = currentpos;
                }
                ensureTokenIs(resultpos, new RightParenToken());
                resultpos++;
                final ParseResult<Statement> stmt = parseStatement(resultpos);
                statement = stmt.result;
                resultpos = stmt.tokenPos;
                constructor = new Constructor(parameters, statement);
            }
            //This is a method def
            else if((ensureToken(resultpos, new BooleanToken()) ||
            ensureToken(resultpos , new IntToken()) ||
            ensureToken(resultpos , new StringToken()) ||
            ensureToken(resultpos , new VoidToken())) &&
            ensureToken(resultpos + 1, new VariableToken()) &&
            ensureToken(resultpos + 2, new LeftParenToken())){
                final ParseResult<MethodDef> methoddef = parseMethodDef(resultpos);
                methodDefs.add(methoddef.result);
                resultpos = methoddef.tokenPos;
            }
            else if ((ensureToken(resultpos, new RightCurlyToken()))){
                resultpos++;
                CurlyBraceStack.pop();
            }
            else{
                throw new ParserException("This is not a valid class because it doesnt have a matching curly brace");
            }
        }
        resultClassDef = new ClassDef(name, constructor, new Extends(extendedClass, null), vardecs, methodDefs, new ArrayList<>());  //NOT RIGHT
        if (constructor == null){
            throw new ParserException("This class Does not have a constructor");
        }
        return new ParseResult<ClassDef>(resultClassDef, resultpos);
    }

    private ParseResult<Program> parseProgram(final int startPos) throws ParserException {
        final Token tokenhere = tokens.get(startPos);
        Program resultProgram = null;
        List<ClassDef> classdefs = new ArrayList<ClassDef>();
        int resultpos=startPos;
        System.out.println("We are in parse program");
        //If it is a variable token and that token is Class, this is a class def
        while (ensureToken(resultpos, new ClassToken())){
            if (ensureToken(resultpos + 1, new VariableToken()) ){
                final ParseResult<ClassDef> classDef = parseClassDef(resultpos);
                resultpos= classDef.tokenPos;
                classdefs.add(classDef.result);
            }
            else {
                throw new ParserException("This is not a valid Class at " + resultpos);
            }
        }
        if(tokens.get(resultpos) instanceof NumberToken || /*ensureToken(resultpos, new BooleanToken())
        ||*/ ensureToken(resultpos, new VariableToken()) || ensureToken(resultpos, new NewToken()) 
        ||/* ensureToken(resultpos, new StringToken()) ||*/ ensureToken(resultpos, new ReturnToken())
        || ensureToken(resultpos, new BreakToken()) || ensureToken(resultpos, new PrintToken()) 
        || ensureToken(resultpos, new IntToken()) || ensureToken(resultpos, new BooleanToken())
        || ensureToken(resultpos, new StringToken()) || ensureToken(resultpos, new LeftCurlyToken())){
            System.out.println("We are in statement part of program");
            final ParseResult Statemnt = parseStatement(resultpos);
            resultProgram = new Program(classdefs, (Statement)Statemnt.result);
            resultpos = Statemnt.tokenPos;
        }
        else{
            throw new ParserException("This is not a valid Program at " + resultpos);

        }
        
        return new ParseResult<Program>(resultProgram, resultpos);
    }
    public Program parseMe() throws ParserException {
        System.out.println("Hello");
        final ParseResult<Program> result = parseProgram(0);

        if(result.tokenPos == tokens.size() +1){
            return result.result;
        }
        else{
            throw new ParserException("Extra tokens at " + result.tokenPos);
        }
    }
}