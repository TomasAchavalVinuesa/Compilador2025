package AnalizadorLexico;
import static AnalizadorLexico.Tokens.*;
%%
%class Lexer
%public
%unicode
%type Tokens
L = [a-zA-Z_]
D = [0-9]
ID = {L}({L}|{D})*
NUM = {D}+
WS = [ \t\r]+  
%{
    public String lexeme;
%}
%%
{D}+{L}({L}|{D})* {lexeme = yytext(); return ERROR;}
"if" {lexeme=yytext(); return If;}
"then" {lexeme=yytext(); return Then;}
"else" {lexeme=yytext(); return Else;}
"while" {lexeme=yytext(); return While;}
"var" {lexeme=yytext(); return Var;}
"int" {lexeme=yytext(); return Int;}
"bool" {lexeme=yytext(); return Bool;}
"void" {lexeme=yytext(); return Void;}
"main" {lexeme=yytext(); return Main;}
"return" {lexeme=yytext(); return Return;}
"true" { lexeme = yytext(); return True; }
"false" { lexeme = yytext(); return False; }
{WS} {/*ignore*/}
"//".* {/*ignore*/}
"&&" { lexeme = yytext(); return And; }
"||" { lexeme = yytext(); return Or; }
"!" { lexeme = yytext(); return Not; }
">" { lexeme = yytext(); return Mayor; }
"<" { lexeme = yytext(); return Menor; }
"==" { lexeme = yytext(); return IgualIgual; }
"%" { lexeme = yytext(); return Modulo; }
"\n" { lexeme = yytext(); return Linea; }
"=" { lexeme = yytext(); return Igual; }
"+" { lexeme = yytext(); return Suma; }
"-" { lexeme = yytext(); return Resta; }
"*" { lexeme = yytext(); return Multiplicacion; }
"/" { lexeme = yytext(); return Division; }
"(" { lexeme = yytext(); return ParentesisAbre; }
")" { lexeme = yytext(); return ParentesisCierra; }
";" { lexeme = yytext(); return PuntoYComa; }
"{" { lexeme = yytext(); return LlaveAbre; }
"}" { lexeme = yytext(); return LlaveCierra; }
{ID} {lexeme=yytext(); return Identificador;}
{NUM} {lexeme=yytext(); return Numero;}
 . {return ERROR;}