/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AST;
import TablaSimbolos.TablaSimbolos;
/**
 *
 * @author tavto
 */
public class NodoLiteral extends NodoAST{ //Son los Valores constantes, esta hoja del Arbol no tiene hijos
    private String valor; //Es un valor como puede ser un numero o un true/false 

    public NodoLiteral(String valor) { //Constructor
        this.valor = valor;
    }
    
    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica { //Funciona para validar el tipo y el rango en que se encuentra. La tabla de simbolos no la usa pero la tiene que recibir por ser un NodoAST
        if (valor.matches("\\d+")) { //Expresión regular (basicamente pregunta si el string está compuesto por numeros nomas)
            try {
                // Intentar parsear como Long para verificar si entra en Int
                long val = Long.parseLong(valor); //Transformamos el valor numérico en un tipo Long para poder trabajar con números de más de 32 bits sin desbordamientos
                if (val > 2147483647L) { //Como el nodo padre es el único que sabe si el número es positivo o negativo entonces perdemos 1 numero posible del rango ya que si lo incluimos al 2147483648 y este es positivo, entonces se desbordaría, ocasionando un mayor fallo 
                    throw new ExcepcionSemantica("El entero " + valor + " excede el límite de 32 bits.");
                } //Si el numero con signo es muy grande (mayor a 32 bits) entonces lanza la excepción
                return "int"; //retorna el tipo int
            } catch (NumberFormatException e) {
                throw new ExcepcionSemantica("Número inválido: " + valor); //Por si aparece algún error inesperado 
            }
        } else if (valor.equalsIgnoreCase("True") || valor.equalsIgnoreCase("False")) { //Por cuestiones de robustes se ignora si tiene mayuscula en esta parte pero eso se controla en el lexer, por lo que no hay que preocuparse por tomar un identificador en mayuscula como FALSE como un booleano
            return "bool";
        }
        throw new ExcepcionSemantica("Literal desconocido: " + valor); //Error por si sucede algo inesperado 
    }
    
    @Override
    public Object generarCodigo() {
        if (valor.equals("true")) return "1"; //En caso de booleanos se escribe 1 o 0 y en caso de un número se escribe el mismo numero
        if (valor.equals("false")) return "0";
        return valor;
    }
    
    @Override
    public String imprimir(String indent) {
        return indent + "LITERAL (" + valor + ")\n"; //Forma de mostrarlo en el arbol
    }
}
