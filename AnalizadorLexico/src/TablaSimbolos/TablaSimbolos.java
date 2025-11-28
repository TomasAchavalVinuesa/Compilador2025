/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TablaSimbolos;

import java.util.HashMap;

/**
 *
 * @author tavto
 */
public class TablaSimbolos {
    private HashMap<String, Simbolo> tabla; //Uso una estructura Clave-valor para poder encontrar rápido los símbolos
    private String tipoRetornoEsperado; //El tipo que debe retornar el main
    
    public TablaSimbolos() { //Constructor
        this.tabla = new HashMap<>();
        this.tipoRetornoEsperado = "void"; //Valor por defecto
    }
    
    public void setTipoRetornoEsperado(String tipo) {
        this.tipoRetornoEsperado = tipo;
    }
    
    public String getTipoRetornoEsperado() {
        return this.tipoRetornoEsperado;
    } //Getter y Setter para comprobar semántica sobre el tipo del main y su retorno

    public boolean insertar(Simbolo s) {  //método para insertar un simbolo a la tabla sin duplicados
        if (tabla.containsKey(s.getNombre())) { //revisa si el nombre del simbolo ya está en la tabla 
            return false; //Al retornar false permite indicarle a nuestro compilador que la variable ya había sido definida antes y así marcar el error
        }
        tabla.put(s.getNombre(), s); //Si no existe, entonces la almacena siendo el nombre la clave, y su tipo el valor
        return true; //Indica que no hubo problema
    }

    public Simbolo buscar(String nombre) { //Con esto me aseguro que la variable haya sido declarada antes de usarla, si no fue declarada y la llamo, entonces me devuelve null
        return tabla.get(nombre);
    }
    
    public String imprimir() { //Esto es simplemente para mostrar por interfaz la tabla, no es necesario pero sirve para corroborar que todo funcione correctamente
        StringBuilder sb = new StringBuilder(); //Nota: acá aprendí que StringBuilder es mutable y más eficiente que usar concatenación de Strings
        sb.append("NOMBRE\t\tTIPO\n");
        sb.append("--------\t\t------\n");
        
        for (Simbolo s : tabla.values()) { //Ciclo for para recorrer toda la tabla y añadir cada simbolo en el StringBuilder 
            sb.append(s.getNombre())
              .append("\t\t")
              .append(s.getTipo())
              .append("\n");
        }
        return sb.toString(); //lo devuelvo como un simple String
    }
}

//Funciona como un registro de todos los símbolos que tengamos con sus nombres y tipos