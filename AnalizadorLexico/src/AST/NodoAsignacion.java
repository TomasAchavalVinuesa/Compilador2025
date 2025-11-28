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
public class NodoAsignacion extends NodoAST { //Clase encargada de las asignaciones
    private String identificador; //variable que recibirá un valor (Lado izquierdo de la asignacion)
    private NodoAST expresion; //Es un nodo con el contenido de la parte derecha de la asignación (Nodo porque puede ser un valor o una expresion más compleja)

    public NodoAsignacion(String identificador, NodoAST expresion) { //Constructor
        this.identificador = identificador;
        this.expresion = expresion;
    }

    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {
        Simbolo simboloVariable = ts.buscar(this.identificador); //Busca id en la tabla de simbolos para ver que exista
        if (simboloVariable == null) {
            throw new ExcepcionSemantica("Error Semántico: La variable '" + this.identificador + "' no ha sido declarada.");
        }
        String tipoDestino = simboloVariable.getTipo(); //Tipo de la variable destino
        String tipoExpresion = this.expresion.analizar(ts); //Obtenemos el tipo de la expresión
        if (!tipoDestino.equals(tipoExpresion)) {
            throw new ExcepcionSemantica("Error Semántico: No se puede asignar el valor de tipo " + tipoExpresion + " a la variable de tipo " + tipoDestino);
        } //Comprobación de tipos en Asignaciones

        return "OK";
    }

    @Override
    public Object generarCodigo() { //Generación de Código en 3DC
        String valor = (String) expresion.generarCodigo(); //Método generar codigo del nodo hijo
        Generador.getInstancia().escribir(identificador + " = " + valor); //Añade una linea de código donde valor es lo que devuelve el nodo hijo
        return null; //Ya que no produce nada que necesite su nodo padre
    }
    
    @Override
    public String imprimir(String indent) { //indent son los espacios necesarios para que se entienda bien la jerarquía del arbol
        String res = indent + "ASIGNACION (" + identificador + ")\n"; //Imprime primera linea de la asignación, dentro del paréntesis está el nombre de la varibale
        res += indent + "  =\n"; //decorativo
        res += expresion.imprimir(indent + "    "); //llamada recursiva al método imprimir del nodo hijo
        return res; //Devuelve todo el texto que generó esta clase y sus hijos a su nodo padre
    }
    //Mostrar en pantalla la asignación 
}
