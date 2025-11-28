/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TablaSimbolos;

/**
 *
 * @author tavto
 */
public class Simbolo {
    private String nombre; 
    private String tipo;   

    public Simbolo(String nombre, String tipo) { //Constructor
        this.nombre = nombre; //nombre variable
        this.tipo = tipo; //Tipo 
    }
//métodos getter and setter
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }  
}
//Esta es la forma en la que puedo guardar los datos para saber el tipo de cada uno
//Con los getters puedo hacer que los nodos AST lean la información de las variables y hagan las validaciones que requieran en el análisis semántico
//Sinceramente no creo utilizar los setters ya que me servirían para cambiar el nombre o el tipo de una variable pero como aún no sé cual es la función que debe tener var lo dejo 