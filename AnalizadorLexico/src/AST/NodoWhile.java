/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AST;
import Generador.Generador;
import TablaSimbolos.TablaSimbolos;

/**
 *
 * @author tavto
 */
public class NodoWhile extends NodoAST{
    private NodoAST condicion; //Nodo con la expresión booleana para el while
    private NodoAST bloque; //Bloque (nodo) con lo que hace si la condición es true
    
    public NodoWhile(NodoAST condicion, NodoAST bloque) { //Constructor
        this.condicion = condicion;
        this.bloque = bloque;
    }
    
    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {
        String tipoCond = condicion.analizar(ts); //Revisa el tipo de la condición
        if (!tipoCond.equals("bool")) { //si no es un booleano lo que retorna la condición entonces hay excepción
            throw new ExcepcionSemantica("La condición del WHILE debe ser de tipo bool. Se encontró: " + tipoCond);
        }
        bloque.analizar(ts); //Pide al nodo hijo (o sea a todo el bloque) que revise su semántica
        return "OK"; 
    }
    
    @Override
    public Object generarCodigo() {
        String etiquetaInicio = Generador.getInstancia().nuevaEtiqueta(); //genera una etiqueta de inicio supongamos L1 (a donde esté esta etiqueta vamos a saltar cada vez que termine una vuelta)
        String etiquetaFin = Generador.getInstancia().nuevaEtiqueta(); //genera una etiqueta de fin supongamos L2 (Si la condición es falsa se salta a esta parte para salir del bucle

        Generador.getInstancia().escribir(etiquetaInicio + ":"); //escribe el punto de inicio de la evaluación "L1:" para el caso de ejemplo
        String cond = (String) condicion.generarCodigo(); //Le pide al nodo con la expresión de la condición que genere su código y guarde el resultado en 1 variable (Ejemplo en T1)
        Generador.getInstancia().escribir("if " + cond + " == 0 goto " + etiquetaFin); //Genera el código para decir "si la condición (T1) es falsa (0) entonces ve a la etiqueta de cierre L2
        bloque.generarCodigo(); //Luego le pide al nodo hijo del bloque que genere su código 
        Generador.getInstancia().escribir("goto " + etiquetaInicio); //luego escribe el código para volver a la etiqueta de inicio L1 donde se volverá a evaluar la condición
        Generador.getInstancia().escribir(etiquetaFin + ":"); //ponemos la etiqueta de cierre/fin "L2:" para cuando sea falsa la condición salgamos del bucle 
        return null; //Es una sentencia, no debe retornar nada para arriba
    }

    @Override
    public String imprimir(String indent) { //Genera el subarbol del while, pidiendole a sus nodos hijos que generen la parte de la condición y del bloque
        return indent + "WHILE\n" + condicion.imprimir(indent + "  ") + indent + "DO\n" + bloque.imprimir(indent + "  ");
    }
}
