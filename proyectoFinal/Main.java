

import java.io.FileReader;            
import java.io.InputStreamReader;     
import java.io.Reader;       
import java.io.BufferedReader;
import java.io.FileInputStream;        


public class Main {
    public static void main(String[] args) {
        try {
            Parser parser ;
            // Para ver si la entrada es desde la terminal o desde un archivo
            if (args.length > 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(args[0]), "UTF-8"));
                     
                    parser = new Parser(reader);
                } else {
                    Reader reader;
                    
                System.out.println("Escribe una expresion y presiona ENTER:");
                reader = new InputStreamReader(System.in); 
                parser = new Parser(reader);
            }
            
            // Crear una instancia del parser
               
            // Inicia el proceso de parser
            parser.parse();   
            //Si se pone en true activa el modo depuracion del lexer
            //Tambien hay que poner la bandera %debug en el lexer
            parser.yydebug = false;                    
            
        } catch (Exception e) {
            System.err.println("Error : " + e.getMessage());
           
        }
    }
}

