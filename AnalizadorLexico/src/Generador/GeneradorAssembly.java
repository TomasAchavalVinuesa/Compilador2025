/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Generador;

import java.util.*;
import java.util.regex.*;

/**
 *
 * @author tavto
 */
public class GeneradorAssembly { //Traduce linea a linea el código que generamos en 3DC a Assembly x86-64 
    private static final String CABECERA = "    .section .data\n"; //Esta parte es la sección donde "viven" las variables (.data)
            //.section .data le dice al ensamblador que lo que se escriba a partir de ahí son datos (no código) Acá van las variables globales

    private static final String CODIGO_INICIO = "\n    .section .text\n    .global main\nmain:\n    push rbp\n    mov rbp, rsp\n\n"; //Esta parte define la sección .text que sería el código ejecutable, declara el main como global y prepara el stack grame 
            //.section .text le dice al ensamblador que acá empieza el código ejecutable
            //.global main hace visible el main (punto de entrada)
            //main es la etiqueta donde empieza el programa
            //push rbp guarda el puntero base anterior (rbp) en la pila (push) y esto y la parte siguiente son algo llamado "prologo de función"
            //mov rbp, rsp lo que dice que el movamos el puntero base (rbp) a la posición actual de la pila (rsp) (O sea que esta última parte prepara el ambiente de trabajo para que esté "limpio" y la función main trabaje sin romper lo que había antes
    
    private static final String CODIGO_FIN = "\n    pop rbp\n    ret\n"; //Acá se define la parte del final donde primero se limpia el stack y luego retorna "ret" para terminar el programa
            //pop rbp saca el valor viejo de la pila y lo pone en rbp (básicamente restaura el flujo de trabajo de lo que venía haciendo hasta que apareció nuestro codigo)
    //nota: Esos 3 strings son parte estructural de todo programa para el caso de Linux/Gcc (o ese vi que era el tipo de assembly que tenía que generar según lo que aparecía en unos de los documentos del campus)
    private Set<String> variablesDeclaradas = new HashSet<>(); //Conjunto de variables que vamos a ir escribiendo en la sessión de datos (.data) para no declararlos 2 veces
    private StringBuilder codigoData = new StringBuilder(); //Acumula las declaraciones de variables (como puede ser x: .quad 0)
    private StringBuilder codigoInstrucciones = new StringBuilder(); //Acumula las instrucciones 

    public String generar(String codigo3DC) {
        variablesDeclaradas.clear();
        codigoData.setLength(0);
        codigoInstrucciones.setLength(0); //Reinicia todo para esta nueva compilación para que no se mezclen cosas de la anterior
        
        codigoData.append(CABECERA);//Agrega la cabecera de datos  en el acumulador de datos
        codigoInstrucciones.append(CODIGO_INICIO); //agrega la cabecera de instrucciones al acumulador de instrucciones

        String[] lineas = codigo3DC.split("\n"); //divide el texto que recibe como parametro en lineas 
        for (String linea : lineas) { //recorre cada linea
            linea = linea.trim(); //Quitamos espacios sobrantes
            if (linea.isEmpty()) continue; //Si al sacarle los espacios la linea queda vacia entonces la salteamos
            procesarLinea(linea); //llamamos a la función procesarLinea y le pasamos como parametro cada una de las lúneas
        }
        
        codigoInstrucciones.append(CODIGO_FIN); //Una vez generado todo el código añade la cabecera del final

        return codigoData.toString() + codigoInstrucciones.toString(); //Retorna la parte de los datos y luego la de las instrucciones en forma de string para que la interfaz gráfica la pueda mostrar
    }

    private void procesarLinea(String linea) { //Recibe 1 instrucción en 3DC y la traduce (básicamente es un switch gigante)
        if (linea.startsWith(";")) return; //Esta es la forma de eliminar los comentarios que hay en 3DC pero en principio los comentarios de código base ya fueron ignorados (esto lo pongo por si luego hago editable la parte del 3DC y me pongo a probar el método)
        
        if (linea.matches("L\\d+:")) { //si sigue la sintaxis de una etiqueta entra acá
            codigoInstrucciones.append(linea).append("\n"); //se mantienen igual
            return;
        }
        
        if (linea.startsWith("goto ")) { //esto es para los saltos incondicionales. si empieza con goto entra acá
            String label = linea.split(" ")[1]; //recorta lo escrito para obtener la etiqueta a la que saltar 
            codigoInstrucciones.append("    jmp ").append(label).append("\n");// usa la instrucción de salto + la etiqueta
            return;
        }

        if (linea.startsWith("if ")) { //Esto es para los saltos condicionales (o sea que inician con if
            String[] partes = linea.split(" "); //le sacamos los espacios y guardamos las palabras por separado
            String op = partes[1]; // Esta parte debería de ser una variable temporal como t1
            String label = partes[5]; // Esta parte debería de ser la etiqueta hacia donde saltamos si t1 es 0
            
            codigoInstrucciones.append("    mov rax, [").append(op).append("]\n"); //Carga el valor de la variable temporal (supongamos t1) en el registro acumulador (rax)
            codigoInstrucciones.append("    cmp rax, 0\n"); //Compara el valor en rax con 0
            codigoInstrucciones.append("    je ").append(label).append("\n"); //si la comparación anterior dio igual entonces salta a la etiqueta (o sea si la condición guardada en t1 es falsa entonces salta a la etiqueta L1)
            return;
        }

        if (linea.startsWith("return ")) { //para el caso del return, si empieza con eso, ingresa acá
            String var = linea.split(" ")[1]; //quita el espacio y guarda el nombre de la variable temporal
            codigoInstrucciones.append("    mov rax, [").append(var).append("]\n"); //mueve el valor del retorno al registro rax
            codigoInstrucciones.append("    leave\n    ret\n"); // leave restaura el stack pointer  y ret devuelve el control al SO
            return;
        }

        if (linea.contains("=")) { //si la línea tiene un = entonces entramos acá
            String[] partes = linea.split("="); //separa la linea en 2 partes, la primera es donde se almacenará el resultado y la segunda es la expresión 
            String destino = partes[0].trim();
            String expresion = partes[1].trim();
            
            declararVariable(destino); // Asegurar que existe en .data

            if (expresion.startsWith("minus ")) { //si la expresion empieza con minus entra acá 
                String op = expresion.split(" ")[1]; //quita el espacio y se queda con el operando
                if (esNumero(op)) { //si el operando es un número literal
                     codigoInstrucciones.append("    mov qword [").append(destino).append("], -").append(op).append("\n"); //mueve el valor negativo de op al lugar de memoria indicado por destino
                } else { //caso de que sea una variable
                    codigoInstrucciones.append("    mov rax, [").append(op).append("]\n"); //carga el valor de la variable
                    codigoInstrucciones.append("    neg rax\n"); //lo niega, haciendolo cambiar de signo
                    codigoInstrucciones.append("    mov [").append(destino).append("], rax\n"); //mueve ese valor al lugar de destino 
                }
                return;
            }

            String[] tokens = expresion.split(" "); //Para todo caso de operación binaria entra acá, primero sacamos los espacios
            if (tokens.length == 3) { //si son 3 partes la expresión 
                String op1 = tokens[0];
                String operador = tokens[1];
                String op2 = tokens[2];
                
                // Cargamos el primer operando en rax
                if (esNumero(op1)){ 
                    codigoInstrucciones.append("    mov rax, ").append(op1).append("\n");
                }
                else{
                    codigoInstrucciones.append("    mov rax, [").append(op1).append("]\n");
                }
                
                //Cargamos el segundo operando en registro auxiliar (rbx)
                if (esNumero(op2)){
                    codigoInstrucciones.append("    mov rbx, ").append(op2).append("\n");
                }
                else {
                    codigoInstrucciones.append("    mov rbx, [").append(op2).append("]\n");
                }

                switch (operador) { //depende del operador que sea es lo que va a devolver
                    case "+":
                        codigoInstrucciones.append("    add rax, rbx\n"); 
                        break;
                    case "-": 
                        codigoInstrucciones.append("    sub rax, rbx\n"); 
                        break;
                    case "*": 
                        codigoInstrucciones.append("    imul rax, rbx\n"); 
                        break;
                    case "/": 
                        codigoInstrucciones.append("    cqo\n"); //Extiende signo para división (prepara 128 bits)
                        codigoInstrucciones.append("    idiv rbx\n"); //Resultado en rax
                        break;
                    case "%": //Como en la división pero el resultado del módulo quedaba en rdx
                        codigoInstrucciones.append("    cqo\n");
                        codigoInstrucciones.append("    idiv rbx\n");
                        codigoInstrucciones.append("    mov rax, rdx\n"); //movemos el resultado a rax
                        break;
                    case "<": //agrupamos todo lo que no son aritméticos
                    case ">":
                    case "==":
                        codigoInstrucciones.append("    cmp rax, rbx\n"); //acá resta internamente rax - rbx y actualiza las banderas del procesador, si son iguales enciende ZF, si rax es menor enciende SF
                        String setInst = switch (operador) { //según el operador elegimos que instrucción leerá esas banderas
                            case "<" -> "setl";
                            case ">" -> "setg";
                            case "==" -> "sete";
                            default -> "sete"; //no debería caer nunca acá pero por las dudas
                        };
                        codigoInstrucciones.append("    ").append(setInst).append(" al\n"); //Esto escribe en assembly la operación que determinó el switch que guardara un 1/0 en el registro al ( de 8 bits) dependiendo de si fue verdadero o falso)
                        codigoInstrucciones.append("    movzx rax, al\n"); //Toma el valor de al, lo copia en rax y rellena todo el resto de 0 (ya que al es de 8 bits y rax de 64 )
                        case "&&":
                            codigoInstrucciones.append("    and rax, rbx\n"); //operación And bit a bit
                        break; 
                        case "||":
                            codigoInstrucciones.append("    or rax, rbx\n"); //operación or bit a bit
                        break;
                }
                
                codigoInstrucciones.append("    mov [").append(destino).append("], rax\n"); //mueve lo que quedó en rax a la memoria de la variable destino
                return;
            }

            if (tokens.length == 1) { //si la expresión solo tiene 1 parte significa que es un literal o una variable entonces entra acá
                if (esNumero(tokens[0])) {//si es un número)
                     codigoInstrucciones.append("    mov qword [").append(destino).append("], ").append(tokens[0]).append("\n"); //mueve el valor que está en tokens en la posición 0 (nuestro literal) hacia destino
                } else { //si es una variable
                     codigoInstrucciones.append("    mov rax, [").append(tokens[0]).append("]\n"); //carga valor de la variable
                     codigoInstrucciones.append("    mov [").append(destino).append("], rax\n"); //mueve ese valor a destino
                }
            }
        }
    }

    private void declararVariable(String nombre) {
        if (!variablesDeclaradas.contains(nombre)) {//si la variable no está declarada, entonces la añade al acumulador de variables declaradas 
            variablesDeclaradas.add(nombre);
            codigoData.append("    ").append(nombre).append(": .quad 0\n"); //y añade su código inicializandola en 0 en la sección de data
        }
    }

    private boolean esNumero(String str) { //determina si es un número ya sea negativo o positivo pero un numero
        return str.matches("-?\\d+");
    }
}
