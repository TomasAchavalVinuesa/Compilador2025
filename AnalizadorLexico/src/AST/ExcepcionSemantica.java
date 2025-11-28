/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AST;

/**
 *
 * @author tavto
 */
public class ExcepcionSemantica extends RuntimeException{
    public ExcepcionSemantica(String mensaje) {
        super(mensaje);
    }
}

//Esta clase sirve para gestionar los errores durante la ejecución del análisis semántico permitiendo que cada nodo pueda lanzar la excepción si detecta un error (por ejemplo si intento sumar un entero y un booleano)
//Los errores que se detecten con esto los va a esperar el catch del método analizarSemantico() de mi clase FrmPrincipal.java
