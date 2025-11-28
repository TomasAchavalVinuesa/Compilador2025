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
public class NodoIf extends NodoAST{
    private NodoAST condicion; //Este nodo tiene la expresión que vamos a evaluar
    private NodoAST bloqueThen; //Nodo con el bloque en caso de true
    private NodoAST bloqueElse; //Nodo con el nloque en caso de false
    
    public NodoIf(NodoAST condicion, NodoAST bloqueThen, NodoAST bloqueElse) { //Constructor
        this.condicion = condicion;
        this.bloqueThen = bloqueThen;
        this.bloqueElse = bloqueElse;
    }
    
    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {
        String tipoCond = condicion.analizar(ts);
        if (!tipoCond.equals("bool")) {
            throw new ExcepcionSemantica("La condición del IF debe ser de tipo bool. Se encontró: " + tipoCond);
        } //Revisamos que la condición sea de tipo booleana
        bloqueThen.analizar(ts); //analiza el bloque Then
        if (bloqueElse != null) { //Si definimos una parte else entonces también llama a su método analizar 
            bloqueElse.analizar(ts);
        }
        return "OK"; //si no encuentra errores semanticos en ninguna de las 3 partes nos indica que todo está bien
    }
    
    @Override
    public Object generarCodigo() {
        String cond = (String) condicion.generarCodigo(); //llama al metodo generarCodigo del nodo hijo (condicion) para que nos devuelva la variable temporal donde almacenó el resultado de la comparación 
        String etiquetaElse = Generador.getInstancia().nuevaEtiqueta(); //Genera las nuevas etiquetas de salto tanto para cuando la condición es falsa (ej: L1)
        String etiquetaFin = Generador.getInstancia().nuevaEtiqueta(); //Como para cuando termina todo lo del if-else (ej: L2)
        Generador.getInstancia().escribir("if " + cond + " == 0 goto " + etiquetaElse); //Acá representamos false como 0 y nos dice que si la condición e sfalsa entonces nos vamos directamente a ejecutar donde indique la etiqueta del else
        bloqueThen.generarCodigo(); //Si el código no saltó en el paso anterior (caso condición verdadera) entonces se sigue el flujo normalmente y ejecuta las instrucciones de este bloque
        Generador.getInstancia().escribir("goto " + etiquetaFin); //Esto es básicamente que si ejecutamos el bloque verdadero entonces seguimos en la parte de la etiqueta fin, salteando así la parte del else (porque no queremos ejecutar ambos bloques)
        Generador.getInstancia().escribir(etiquetaElse + ":"); //esto indicará donde ejecutar si la condición fue falsa
        if (bloqueElse != null) { //en caso de que exista una parte Else entonces generamos el código de este bloque  en esta parte
            bloqueElse.generarCodigo();
        }
        Generador.getInstancia().escribir(etiquetaFin + ":"); //Escribimos la etiqueta de fin justo al terminar el else

        return null; //Sentencias no necesitan retornar nada
    }
    
    @Override
    public String imprimir(String indent) {
        String res = indent + "IF\n" + condicion.imprimir(indent + "  ") + indent + "THEN\n" + bloqueThen.imprimir(indent + "  "); //Escribe el if, la condición (mediante la llamada del método imprimir de su nodo hijo), la palabra Then y el bloque then también llamando al método imprimir de su nodo hijo
        if (bloqueElse != null) { //Con el bloque else hago lo mismo que con el bloque Then si es que hay una parte else 
            res += indent + "ELSE\n" + bloqueElse.imprimir(indent + "  ");
        }
        return res; //Retorna el arbol de todo el bloque if-else
    }
}
