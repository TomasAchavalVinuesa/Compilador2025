/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package AnalizadorLexico;
//Esta clase se encarga de generar los códigos fuentes de ambos analizadores (léxico y sintáctico)
//así evito usar linea de comando

//Importaciones
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author tavto
 */
public class Principal {
    public static void main(String[] args) throws Exception {
        String basePath = System.getProperty("user.dir") + "/src/AnalizadorLexico/"; //Raíz del proyecto de forma dinámica para evitar problemas al usar en otra PC
        String ruta1 = basePath + "Lexer.flex"; //ruta dinámica a las reglas para el analizador léxico
        String ruta2 = basePath + "LexerCup.flex"; //ruta dinámica a las reglas para el analizador sintáctico
        String[] rutaS = {"-parser", "Sintax", basePath + "Sintax.cup"};
        //Esos 3 elementos del arreglo son parámetros para el generador CUP
        //-parser es opcional e indica que el siguiente argumento es el nombre que tendrán la clase generadora del parser 
        //Sintax es el nombre que tendrá la clase generada
        //basePath con Sintax.cup es la ruta relativa al archivo de entrada con toda la gramática que debe procesar para generar el analizador
        generar(ruta1, ruta2, rutaS, basePath);
    }
    
    public static void generar(String ruta1, String ruta2, String[] rutaS, String basePath) throws IOException, Exception{
        File archivoLexer = new File(basePath + "Lexer.java");
        if (archivoLexer.exists()) {
            archivoLexer.delete();
        }else{
            System.out.println("El Archivo Lexer.java no existía antes");
        }
        //Acá revisa si existe un analizador léxico llamado Lexer.java y si es así lo elimina para no mezclar código viejo con nuevo
        File archivoLexerCup = new File(basePath + "LexerCup.java");
        if (archivoLexerCup.exists()) {
            archivoLexerCup.delete();
        }else{
            System.out.println("El Archivo LexerCup.java no existía antes");
        }
        //lo mismo pero con el analizador léxico para el analizador sintáctico
    

        File archivo;
        archivo = new File(ruta1);
        JFlex.Main.generate(archivo); //Genera el Lexer.java nuevo (analizador léxico)
        
        archivo = new File(ruta2);
        JFlex.Main.generate(archivo); //Genera el LexerCup.java (analizador léxico para el analizador sintactico)
        java_cup.Main.main(rutaS); //Genera Sintax.java y Sym.java
        //Los analizadores léxicos que generamos deberán leer el código fuente y agrupar en tokens las palabras con significados
        //Sintax.java es el analizador Sintáctico en si, este pide los tokens al Lexer y verifica que el orden de esos tokens cumpla las reglas gramaticales
        //sym.java es una clase auxiliar que funciona como diccionario o traductor entre el Lexer (LexerCup.java) y el Parser (Sintax.java) 
        //y asigna un entero único a cada tipo de token para que se entiendan entre ambas
        
        Path rutaSym = Paths.get(System.getProperty("user.dir") + "/sym.java"); //Esta ruta no es la misma que  basePath por eso lo movemos luego
        if (Files.exists(rutaSym)) {
            Path destinoSym = Paths.get(basePath + "sym.java");
            if (Files.exists(destinoSym)){
                Files.delete(destinoSym); //eliminamos el viejo para evitar problemas
            }
            Files.move(rutaSym, destinoSym); //lo movemos todo a la misma carpeta
        }else{
            System.out.println("El Archivo Sym.java no fue creado donde debería");
        }
        
        Path rutaSintax = Paths.get(System.getProperty("user.dir") + "/Sintax.java");
        if (Files.exists(rutaSintax)) {
            Path destinoSintax = Paths.get(basePath + "Sintax.java");
            if (Files.exists(destinoSintax)){
                Files.delete(destinoSintax); //eliminamos el viejo para evitar problemas
            }
            Files.move(rutaSintax, destinoSintax); //lo movemos todo a la misma carpeta
        }else{
            System.out.println("El Archivo Sintax.java no fue creado donde debería");
        }
    }
}
