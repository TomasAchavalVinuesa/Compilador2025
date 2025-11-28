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
public class NodoDeclaracion extends NodoAST {
    
    private String tipo; //Tipo de dato declarado
    private String identificador; //nombre variable
    private NodoAST valorInicial; //nodo opcional, puede ser una expresión, un valor o simplmente null
    
    public NodoDeclaracion(String tipo, String identificador, NodoAST valorInicial) { //Constructor
        this.tipo = tipo;
        this.identificador = identificador;
        this.valorInicial = valorInicial;
    }
    
    public String getTipo() {
        return tipo;
    }
    public String getIdentificador() {
        return identificador;
    }//Getters por si luego requiero preguntar estos valores desde fuera (Encapsulamiento)
    
    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica { //Con este método registramos la variable en memoria
        Simbolo nuevoSimbolo = new Simbolo(this.identificador, this.tipo); //Crea el simbolo con su nombre y su tipo
        if (!ts.insertar(nuevoSimbolo)) {
            throw new ExcepcionSemantica("Error Semántico: La variable '" + this.identificador + "' ya ha sido declarada.");
        }//Inserta el simbolo en la tabla de simbolos y si ese método nos devuelve true (no hubo problemas) entonces no lanza la excepción, si nos devuelve false entonces lanzamos excepción de que ya fue declarada

        if (this.valorInicial != null) { //Si el nodo de valor inicial no está vacio entonces entra en este if
            String tipoExpresion = this.valorInicial.analizar(ts); //Obtenemos el tipo del nodo hijo (Expresión de la derecha) haciendo una llamada a su método analizar 
            if (!this.tipo.equals(tipoExpresion)) {
                throw new ExcepcionSemantica("Error Semántico: No se puede inicializar el tipo " + this.tipo + " con un valor de tipo " + tipoExpresion);
            }//Revisa que los tipos de la variable declarada y de la expresión sean iguales, si no me tira una excepción
        }
        
        return "OK";
    }

    @Override
    public Object generarCodigo() {
        if (valorInicial != null) {
            String valor = (String) valorInicial.generarCodigo(); //Llama a al método generarCódigo de su nodo hijo (o sea de la expresión) y este le retorna un valor o el nombre de la variable temporal que esta haya generado
            Generador.getInstancia().escribir(identificador + " = " + valor); //Añade una linea de código donde valor es lo que devuelve el nodo hijo
        }
        return null; //Ya que si no hay un valor inicial entonces no genera código ejecutable pero si almacena espacio (Al crear y almacenar el simbolo)
    }
    
    @Override
    public String imprimir(String indent) {
        String res = indent + "DECLARACION (" + tipo + " " + identificador + ")\n"; //Muestra en pantalla la declaración con su tipo y nombre de variable
        if (valorInicial != null) { //si tiene una expresión en la derecha entonces le añade
            res += indent + "  =\n"; //Identación para decorar
            res += valorInicial.imprimir(indent + "    "); //lo que genere el nodo hijo 
        }
        return res; 
    }
}
