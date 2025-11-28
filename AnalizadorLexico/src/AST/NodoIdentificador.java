/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AST;

import TablaSimbolos.*;

/**
 *
 * @author tavto
 */
public class NodoIdentificador extends NodoAST{
    private String nombre; 

    public NodoIdentificador(String nombre) { //Constructor
        this.nombre = nombre;
    }

    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {
        Simbolo s = ts.buscar(this.nombre); //Buscamos que la variable exista en la tabla de simbolos
        if (s == null) {
            throw new ExcepcionSemantica("Error Semántico: La variable '" + this.nombre + "' no ha sido declarada.");
        } //Si la variable no está en la tabla es porque aun no fue declarada
        return s.getTipo(); //Retornamos el tipo de la variable para que el nodo padre la pueda comparar 
    }
    
    @Override
    public Object generarCodigo() {
        return nombre; //Simplemente devuelve el nombre de la variable
    }
    
    @Override
    public String imprimir(String indent) {
        return indent + "VARIABLE (" + nombre + ")\n"; //Muestra la variable en el arbol
    }
}
