/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AST;
import TablaSimbolos.TablaSimbolos;
import java.util.List;

/**
 *
 * @author tavto
 */
public class NodoBloque extends NodoAST{ //Bloques que forman parte del main, de los while, los if y los else. Este es un nodo más estructural
    private List<NodoAST> sentencias; //Lista de todo el contenido que forma parte de un bloque 

    public NodoBloque(List<NodoAST> sentencias) { //Constructor
        this.sentencias = sentencias;
    }
    
    @Override
    public String analizar(TablaSimbolos ts) throws ExcepcionSemantica {
        for (NodoAST s : sentencias) { //Recorrer cada sentencia del bloque
            s.analizar(ts); //Llama al método analizar de la sentencia que sea que tenga dentro del bloque. En caso de algún error, el propio nodo hijo será quien lo diga
        }
        return "OK"; //Si llegó hasta acá es que todo lo del bloque cumple la semantica y no saltó una excepcionsemantica de los nodos hijos
    }

    @Override
    public Object generarCodigo() { //Lo mismo que con analizar pero ahora con el método generarCodigo
        for (NodoAST sentencia : sentencias) {
            sentencia.generarCodigo();
        }
        return null; //Ya que el bloque en si no produce nada más que orden a nivel de código 3DC
    }

    @Override
    public String imprimir(String indent) { //indent son los espacios para que se entienda la jerarquía
        StringBuilder sb = new StringBuilder(); //String mutable para mejorar eficiencias
        sb.append(indent).append("BLOQUE {\n"); //Escribe Bloque con su respectiva indentación para que se entienda bien la jerarquía
        for (NodoAST s : sentencias) {
            sb.append(s.imprimir(indent + "  ")); //Lo mismo que con los otros 2 métodos, para cada nodo hijo le llamamos a su función imprimir  y le enviamos su identación
        }
        sb.append(indent).append("}\n"); //Para que se entienda cuando termina el bloque
        return sb.toString(); //Retorna todo el bloque con lo generado por sus hijos dentro
    }
}
//Nota: Si quisiera cambiar el alcance de las variables para que sean locales de cada bloque de código, deberíahacer una Tabla de símbolos para cada bloque y modificar acá para tratarlas por separado