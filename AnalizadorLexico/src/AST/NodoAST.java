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
public abstract class NodoAST {
    public abstract String analizar(TablaSimbolos ts) throws ExcepcionSemantica; 
    public abstract Object generarCodigo();
    public abstract String imprimir(String indent);
}

//Esta clase funciona como plantilla para todos los tipos de nodos donde el método analizar es usado para el análisis semántico por eso necesita la tabla de simbolos
// el método generarCódigo sirve para traducir cada nodo a código 3DC
// e imprimir sirve para mostrar el Arbol de manera visual 
//a demás, esto permite el polimorfismo necesario para tener una lista de sentencias en el Sintax.cup a pesar de que sean objetos distintos (así puedo agrupar un nodo de if con un nodo de declaración)
