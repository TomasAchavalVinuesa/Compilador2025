/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AST;
import Generador.Generador;
import TablaSimbolos.*;
/**
 *
 * @author tavto
 */
public class NodoRetorno extends NodoAST{ //Es la sentencia Return
    private NodoAST expresion; //Expresión que retorna

    public NodoRetorno(NodoAST expresion) { //Constructor
        this.expresion = expresion;
    }
    
    public NodoAST getExpresion() { //getter para permitir acceso externo (atributo privado (encapsulamiento))
        return expresion;
    }

    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {
        String tipoExpresion = this.expresion.analizar(ts); //Obtenemos el tipo del nodo hijo
        String tipoEsperado = ts.getTipoRetornoEsperado(); //Obtenemos de la tabla de simbolos el tipo de retorno esperado para el main
        
        if (!tipoExpresion.equals(tipoEsperado)) { //Comparamos que sean iguales
            throw new ExcepcionSemantica("Error Semántico: Tipo de retorno incorrecto. " + "Se esperaba '" + tipoEsperado + "' pero se encontró '" + tipoExpresion + "'");
        } //Si son distintos lanza error

        if (tipoExpresion.startsWith("ERROR")) {
            throw new ExcepcionSemantica("Error Semántico dentro de la expresión de retorno: " + tipoExpresion);
        } //Si un nodo hijo retorna error esto lanza una excepción
        return "OK"; //Todos los return OK son para indicar éxito, son prácticamente innecesarios pero los uso para los casos inesperados poder retornar ERROR y en la clase padre lanzar excepción
    }

    @Override
    public Object generarCodigo() {
        String valor = (String) expresion.generarCodigo(); //Le pide a la clase hija que genere su código y le devuelva el valor de retorno o el nombre de la variable temporal donde se almacena el resultado de la expresión
        Generador.getInstancia().escribir("return " + valor); //Escribe la instrucción de retorno (ejemplo return t2
        return null; //Es una sentencia final así que no produce nada que luego pueda usarse
    }
    
    @Override
    public String imprimir(String indent) { //hace la representación del return en el arbol
        String res = indent + "RETORNO\n";
        res += expresion.imprimir(indent + "  "); //como nodo hijo tiene todo el subarbol que genere la expresión
        return res; //retorna el subarbol del return + expresion
    }
}
