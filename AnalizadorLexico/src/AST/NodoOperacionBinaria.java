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
public class NodoOperacionBinaria extends NodoAST{ //Se encarga de toda operación que involucre a 2 elementos
    private NodoAST operandoIzquierdo; //Puede ser un literal, una variable o otra operación
    private String operador; 
    private NodoAST operandoDerecho;

    public NodoOperacionBinaria(NodoAST opIzquierdo, String op, NodoAST opDerecho) { //Constructor
        this.operandoIzquierdo = opIzquierdo;
        this.operador = op;
        this.operandoDerecho = opDerecho;
    }

    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {
        String t1 = operandoIzquierdo.analizar(ts); //Revisa de que tipo san cada uno de los operandos
        String t2 = operandoDerecho.analizar(ts);

        if (operador.matches("[+\\-*/%]")) { //Si el operador es uno de los aritméticos ingresa acá
            if (t1.equals("int") && t2.equals("int")) return "int";  //Si cumple que ambos términos son enteros entonces devuelve entero a su clase padre
            throw new ExcepcionSemantica("Operación aritmética " + operador + " requiere enteros."); //Si se uso una operación aritmética y uno de los valores no es de tipo entero, da esta excepción semántica
        }

        if (operador.matches("[><]")) { //Operadores relaciones, si tiene uno de esos, entonces ingresa acá
            if (t1.equals("int") && t2.equals("int")) return "bool"; //Se  comparan enteros y se devuelve bool a la clase padre
            throw new ExcepcionSemantica("Operación '" + operador + "' requiere operandos numéricos."); //En caso de usa los operadores de comparación (< o >) y que no sean ambos enteros
        }
        
        if (operador.equals("==")) { //Para la compararación con ==
            if (t1.equals(t2)) return "bool"; //si son del mismo tipo sabemos que devolverá un booleano al nodo padre
            throw new ExcepcionSemantica("Tipos incompatibles para igualdad."); //Acá nos aseguramos de que se comparen cosas del mismo tipo (Así se evita comparar un 1 (Int) con un true)
        }

        if (operador.equals("&&") || operador.equals("||")) { //Ahora con operadores lógicos
            if (t1.equals("bool") && t2.equals("bool")) return "bool"; //Si ambos son de tipo bool, devuelve bool al nodo padre
            throw new ExcepcionSemantica("Operación lógica " + operador + " requiere booleanos."); //Tira la excepción si no son ambos operandos booleanos 
        }
        return "ERROR"; //Si no es ninguno de esos casos devuelve error
    }

    @Override
    public Object generarCodigo() {
        String op1 = (String) operandoIzquierdo.generarCodigo(); //Obtiene el código de los operandos  y los alamcena (puede  retornar un literal o una variable como en el caso de t1 que muestro abajo)
        String op2 = (String) operandoDerecho.generarCodigo();
        
        String temporal = Generador.getInstancia().nuevoTemporal(); //Pide al generador un nombre para guardar el resultado de esta operación (Ejemplo en t2)
        Generador.getInstancia().escribir(temporal + " = " + op1 + " " + operador + " " + op2); //Escribe la instrucción completa en el archivo 3DC (Ejemplo t2 = 1 + t1)
        return temporal; //Retorna el nombre de donde guardó el resultado de la operación al nodo padre (retorna t2 entonces en una asignación puede quedar x = t2)
    }
    
    @Override
    public String imprimir(String indent) { //Muestra en pantalla esta parte del arbol
        String res = indent + "OPERACION (" + operador + ")\n"; 
        res += operandoIzquierdo.imprimir(indent + "  "); //Muestra ambos lados hijos del arbol
        res += operandoDerecho.imprimir(indent + "  ");
        return res; //Retorna al nodo padre toda esta rama de la operación y hacia abajo
    }
    
}
