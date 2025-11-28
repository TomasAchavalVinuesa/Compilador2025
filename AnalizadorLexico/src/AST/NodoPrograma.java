/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AST;

import java.util.List;
import TablaSimbolos.*;

/**
 *
 * @author tavto
 */
public class NodoPrograma extends NodoAST { //Representa la función main y desde acá surge todo
    
    private String tipoRetornoMain;
    private List<NodoAST> listaSentencias; //Lista ordenada de todas las instrucciones que están dentro de los {}

    public NodoPrograma(String tipoRetornoMain, List<NodoAST> listaSentencias) { //Constructor. Recibe los datos del parser y los alamcena (objeto creado a partir de la regla inicio)
        this.tipoRetornoMain = tipoRetornoMain;
        this.listaSentencias = listaSentencias;
    }
    
    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {  //Esto va a funcionar como una casacadda de validaciones de la semántica   
        ts.setTipoRetornoEsperado(this.tipoRetornoMain.toLowerCase()); //Configura el tipo de dato esperado y lo pasa a minusculas por las dudas
        for (NodoAST sentencia : listaSentencias) { //Se va a ir iterando sobre todas las sentencias que forman parte del main
            sentencia.analizar(ts);  //Si una de las sentencias lanza una excepción entonces se detiene el bucle y se muestra la excepción en la parte de análisis semántico
        }
        return "OK"; //Si el bucle no tiene excepciones entonces el código pasó el analisis semántico
    }

    @Override
    public Object generarCodigo() { //Coordina toda la generación del código de salida, también todo en cascada
        Generador.Generador.getInstancia().limpiar(); //Nos aseguramos de que no quede código de antes (Ya que el generador es un singleton)
        for (NodoAST sentencia : listaSentencias) { //lo mismo que con el análisis semántico, recorre la lista de sentencias y les pide que hagan su parte (escribir sus instrucciones de código)
            sentencia.generarCodigo();
        }
        return Generador.Generador.getInstancia().obtenerCodigo(); //Cuando ya todos escribieron sus partes, este método recupera el trabajo final y lo devuelve a la interfaz gráfica para que lo muestre
    }
    
    @Override
    public String imprimir(String indent) { //Todo en cascada, incluso la generación visual del arbol 
        StringBuilder sb = new StringBuilder();
        sb.append(indent).append("PROGRAMA\n"); //arranca con la palabra programa y luego le pide a cada sentencia que escriba su parte
        for (NodoAST sentencia : listaSentencias) {
            sb.append(sentencia.imprimir(indent + "  ")); //Envia la indentación para que se entienda la representación gráfica
        }
        return sb.toString(); //Retorna todo el texto del arbol para que la interfaz gráfica lo muestre en el análisis sintáctico
    }
}
