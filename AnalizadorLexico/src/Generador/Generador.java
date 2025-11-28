/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Generador;

/**
 *
 * @author tavto
 */
public class Generador {
    private static Generador instancia; //Esta variable guarda la única copia del generador  (static porque pertenece a la clase no a un objeto y private para que no la puedan tocar desde fuera)
    private int contadorTemporales; //lleva la cuenta de las variables temporales para que nunca se repitan
    private int contadorEtiquetas; //lo mismo pero para las etiquetas
    private StringBuilder codigo; //Acá se va acumulando todo el código generado (de tipo StringBuilder por motivos de eficiencia)

    private Generador() { //IMPORTANTE QUE SEA PRIVADO EL CONSTRUCTOR, así solo esta clase puede crear un generador
        this.contadorTemporales = 0;
        this.contadorEtiquetas = 0;
        this.codigo = new StringBuilder();
    }

    public static Generador getInstancia() { //método llamable desde cualquier lugar y que en caso de que no exista un generador, pide que se instancie 1, si ya existe entonces retorna el existente
        if (instancia == null) {
            instancia = new Generador();
        }
        return instancia;
    } //Fundamental para implementar singleton

    public void limpiar() { //Método para reiniciar el generador a 0 una vez que vamos a volver a compilar (para que no se mezclen contadores ni código de lo anterior)
        this.contadorTemporales = 0;
        this.contadorEtiquetas = 0;
        this.codigo = new StringBuilder();
    }

    public String nuevoTemporal() {
        return "t" + (++contadorTemporales); //Hace un preincremento donde aumenta el número de contador y luego devuelve ese valor cuando se pide una variable temporal (Ej: t1)
    }

    public String nuevaEtiqueta() {
        return "L" + (++contadorEtiquetas); //Lo mismo que nuevoTemporal pero con las etiquetas
    }

    public void escribir(String instruccion) {
        this.codigo.append(instruccion).append("\n"); //método para añadir una linea de código nueva (de paso da siempre un salto de linea para que se vea bien)
    }

    public String obtenerCodigo() {
        return this.codigo.toString(); //Convierte el StringBuilder que fuimos armando en un String para mostrarlo en la interfaz gráfica 
    }
}

//Nota conceptual: Acá se usó el patrón singleton ya que este nos garantiza que vamos a tener una única instanca del generador y que será accesible por todo el programa, y eso nos sirve para evitar problemas en el código de salida como el caso de la generación de variables temporales (Tn) que deben de ser únicas y cosecutivas