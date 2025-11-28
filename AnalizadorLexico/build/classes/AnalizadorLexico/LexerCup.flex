package AnalizadorLexico;
import java_cup.runtime.Symbol;
%%
%class LexerCup
%type java_cup.runtime.Symbol
%cup
%full
%line
%char
L = [a-zA-Z_]
D = [0-9]
ID = {L}({L}|{D})*
NUM = {D}+
WS = [ \t\r]+  
NEWLINE = \n
%{
    private Symbol symbol(int type, Object value){
        return new Symbol(type, yyline, yycolumn, value);
    }
    private Symbol symbol(int type){
        return new Symbol(type, yyline, yycolumn);
    }
%}
%%
{D}+{L}({L}|{D})* {return new Symbol(sym.error, yychar, yyline, yytext());}
"if" {return new Symbol(sym.If, yycolumn, yyline, yytext());}
"then" {return new Symbol(sym.Then, yycolumn, yyline, yytext());}
"else" {return new Symbol(sym.Else, yycolumn, yyline, yytext());}
"while" {return new Symbol(sym.While, yycolumn, yyline, yytext());}
"var" {return new Symbol(sym.Var, yycolumn, yyline, yytext());}
"int" {return new Symbol(sym.Int, yychar, yyline, yytext());}
"bool" {return new Symbol(sym.Bool, yychar, yyline, yytext());}
"void" {return new Symbol(sym.Void, yychar, yyline, yytext());}
"main" {return new Symbol(sym.Main, yychar, yyline, yytext());}
"return" {return new Symbol(sym.Return, yychar, yyline, yytext());}
"true" {return new Symbol(sym.True, yychar, yyline, yytext());}
"false" {return new Symbol(sym.False, yychar, yyline, yytext());}
{NEWLINE} {/*ignore*/}
{WS} {/*ignore*/}
"//".* {/*ignore*/}
"&&" {return new Symbol(sym.And, yycolumn, yyline, yytext());}
"||" {return new Symbol(sym.Or, yycolumn, yyline, yytext());}
"!" {return new Symbol(sym.Not, yycolumn, yyline, yytext());}
">" {return new Symbol(sym.Mayor, yycolumn, yyline, yytext());}
"<" {return new Symbol(sym.Menor, yycolumn, yyline, yytext());}
"==" {return new Symbol(sym.IgualIgual, yycolumn, yyline, yytext());}
"%" {return new Symbol(sym.Modulo, yycolumn, yyline, yytext());}
"=" {return new Symbol(sym.Igual, yychar, yyline, yytext());}
"+" {return new Symbol(sym.Suma, yychar, yyline, yytext());}
"-" {return new Symbol(sym.Resta, yychar, yyline, yytext());}
"*" {return new Symbol(sym.Multiplicacion, yychar, yyline, yytext());}
"/" {return new Symbol(sym.Division, yychar, yyline, yytext());}
"(" {return new Symbol(sym.ParentesisAbre, yychar, yyline, yytext());}
")" {return new Symbol(sym.ParentesisCierra, yychar, yyline, yytext());}
";" {return new Symbol(sym.PuntoYComa, yychar, yyline, yytext());}
"{" {return new Symbol(sym.LlaveAbre, yychar, yyline, yytext());}
"}" {return new Symbol(sym.LlaveCierra, yychar, yyline, yytext());}
{ID} {return new Symbol(sym.Identificador, yychar, yyline, yytext());}
{NUM} {return new Symbol(sym.Numero, yychar, yyline, yytext());}
 . {return new Symbol(sym.error, yychar, yyline, yytext());}
