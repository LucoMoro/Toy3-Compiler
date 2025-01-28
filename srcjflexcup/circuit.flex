// Circuit1.flex
//         ../jflex-1.8.2/bin/jflex -d src srcjflexcup/circuit.flex
// CS2A Language Processing
//
// Description of lexer for circuit description language.
//
// Ian Stark
package esercitazione4;
import java_cup.runtime.Symbol; //This is how we pass tokens to the parser
//import jdk.incubator.vector.DoubleVector;

%%
// Declarations for JFlex
%unicode // We wish to read text files

%cup // Declare that we expect to use Java CUP
// Abbreviations for regular expressions
%line
%column

%{
  private StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }

  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace = {LineTerminator} | [ \t\f]

/** Comments **/
Comment = {TraditionalComment} | {EndOfLineComment}
TraditionalComment = "/*" [^*] ~"*/" | "/*" "*"+ "/"
EndOfLineComment = "//" {InputCharacter}* {LineTerminator}?

/** Identifiers **/
Identifier = [:jletter:] [:jletterdigit:]*

/** Numbers **/
digit = [0-9]
digitWithoutZero = [1-9]
digits = {digit}*
optionalFraction = "." {digits}
optionalExp = [eE] ("-" | "+")? {digits}

IntegerLiteral = ({digitWithoutZero}{digits} | 0 )
DoubleLiteral = ({digitWithoutZero}{digits} | 0 ){optionalFraction}

EscChar = '\\[ntbrf\\\'\"]' | ''
CharC = '[^\\[ntbrf\\\'\"]]' | {EscChar}

%state STRING
%state COMMENT
%%

/** Keywords **/
<YYINITIAL> "if" { return symbol(sym.IF); }
<YYINITIAL> "then" { return symbol(sym.THEN); }
<YYINITIAL> "else" { return symbol(sym.ELSE); }
<YYINITIAL> "while" { return symbol(sym.WHILE); }
<YYINITIAL> "do" { return symbol(sym.DO); }
<YYINITIAL> "return" { return symbol(sym.RETURN); }
<YYINITIAL> "ref" { return symbol(sym.REF); }
<YYINITIAL> "def" { return symbol(sym.DEF); }
<YYINITIAL> "int" { return symbol(sym.INT); }
<YYINITIAL> "bool" { return symbol(sym.BOOL); }
<YYINITIAL> "double" { return symbol(sym.DOUBLE); }
<YYINITIAL> "string" { return symbol(sym.STRING); }
<YYINITIAL> "char" { return symbol(sym.CHAR); }
<YYINITIAL> "program" { return symbol(sym.PROGRAM); }
<YYINITIAL> "begin" { return symbol(sym.BEGIN); }
<YYINITIAL> "end" { return symbol(sym.END); }

<YYINITIAL> {

    /** Operators **/
    ">" { return symbol(sym.GT); }
    ">=" { return symbol(sym.GE); }
    "<" { return symbol(sym.LT); }
    "<=" { return symbol(sym.LE); }
    "==" { return symbol(sym.EQ); }
    "<>" { return symbol(sym.NE); }
    "not" { return symbol(sym.NOT); }
    "and" { return symbol(sym.AND); }
    "or" { return symbol(sym.OR); }
    "+" { return symbol(sym.PLUS); }
    "-" { return symbol(sym.MINUS); }
    "*" { return symbol(sym.TIMES); }
    "/" { return symbol(sym.DIV); }

    /** Parenthesis & symbols **/
    "(" { return symbol(sym.LPAR); }
    ")" { return symbol(sym.RPAR); }
    "," { return symbol(sym.COMMA); }
    ";" { return symbol(sym.SEMI); }
    "|" { return symbol(sym.PIPE); }
    ":=" { return symbol(sym.ASSIGN); }
    "{" { return symbol(sym.LBRAC); }
    "}" { return symbol(sym.RBRAC); }
    ":" { return symbol(sym.COLON); }
    "=" { return symbol(sym.ASSIGNDECL); }

     /** Read & Write **/
     "<<" { return symbol(sym.IN); }
     ">>" { return symbol(sym.OUT); }
     "!>>" { return symbol(sym.OUTNL); }


     /** Constants **/
     "true" { return symbol(sym.TRUE); }
     "false" { return symbol(sym.FALSE); }
     {IntegerLiteral} { return symbol(sym.INT_CONST, Integer.parseInt(yytext())); }
     {DoubleLiteral} { return symbol(sym.DOUBLE_CONST, Double.parseDouble(yytext())); }
     {CharC} { if(yytext().length()==2) return symbol(sym.CHAR_CONST, '\0');
               if(yytext().length()==3) return symbol(sym.CHAR_CONST, yytext().charAt(1));
               return symbol(sym.CHAR_CONST, yytext().charAt(2));
             }
     \" { yybegin(STRING); }

     /** Identifiers **/
     {Identifier} { return symbol(sym.ID, yytext());}

     {WhiteSpace} { /* ignore */ }

      "/*" { yybegin(COMMENT); }

     {Comment} { /* ignore */ }

}

<STRING> {
      \" { yybegin(YYINITIAL);
          String result = string.toString();
          string.setLength(0);
          return symbol(sym.STRING_CONST, result);
      }
      [^\"]+ { string.append( yytext() ); } //[^\n\r\"\\]+ { string.append( yytext() ); }
      \\t { string.append('\t'); }
      \\n { string.append('\n'); }
      \\r { string.append('\r'); }
      \\\" { string.append('\"'); }
      \\ { string.append('\\'); }
      <<EOF>> { throw new IllegalArgumentException("String constant not correctly closed, line: " + yyline + "; column: " +yycolumn); }
}

<COMMENT> {
    "*/" { yybegin(YYINITIAL); }
    {LineTerminator} {}
    . {}
    <<EOF>> { throw new Error("Comment not correctly closed, line: " + yyline + "; column: " +yycolumn); }
}

[^] { throw new Error("Illegal character <"+yytext()+"> line: " + yyline + "; column: " +yycolumn); }


