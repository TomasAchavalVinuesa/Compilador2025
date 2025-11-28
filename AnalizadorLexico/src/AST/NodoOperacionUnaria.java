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
public class NodoOperacionUnaria extends NodoAST{ //Se encarga de las operaciones con un solo operando
    private String operador;
    private NodoAST operando; //nodo hijo que puede ser un literal o una expresión más compleja

    public NodoOperacionUnaria(String operador, NodoAST operando) { //Constructor
        this.operador = operador;
        this.operando = operando;
    }

    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {
        String tipo = operando.analizar(ts); //obtiene el tipo del dato a operar
        if (operador.equals("!")) { //Si usa el operador unitario para boleanos tenemos que revisar que sea booleano el operando
            if (!tipo.equals("bool")) throw new ExcepcionSemantica("Operador '!' requiere bool.");
            return "bool"; //El resultado siempre sería un booleano (se le avisa al padre)
        } else if (operador.equals("-")) { //Este operador es para operandos enteros, por lo tanto lanza una excepción si el operando no lo es
            if (!tipo.equals("int")) throw new ExcepcionSemantica("Operador '-' unario requiere int.");
            return "int"; //Esta operación devuelve un entero por eso le avisa al nodo padre que el resultado será un entero
        }
        return "ERROR"; //Por si hay algún error no contemplado
    }

    @Override
    public Object generarCodigo() { //De esta manera es como se puede diferenciar una resta de el operador minus
        String op = (String) operando.generarCodigo(); //Obtenemos el valor/variable temporal del nodo hijo
        String temporal = Generador.getInstancia().nuevoTemporal(); //pedimos una nueva variable temporal para almacenar el resultado de la operación
        if (operador.equals("!")) { //Como true se escribiría como 1 y false se escribe como 0 entonces podemos representar la operación ! como 1- ese boleano
            Generador.getInstancia().escribir(temporal + " = 1 - " + op);
        } else {
            Generador.getInstancia().escribir(temporal + " = minus " + op); //Se usa minus para especificar que es una operación unaria
        }
        return temporal; //retorna el nombre de la variable donde quedó el resultado de la operación
    }

    @Override
    public String imprimir(String indent) {
        return indent + "UNARIO (" + operador + ")\n" + operando.imprimir(indent + "  "); //Como lo muestra en el arbol AST y llama al metodo del hijo para que este se muestre
    } 
}
