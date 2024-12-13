import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;

import tabla.SymbolTable;

/**
 * Clase que se encarga de traducir las cuadruplas en codigo MIPS
 */
public class Traductor {

    //Lista que contiene la cuadruplas traducidas
    List<String> lista= new ArrayList<>(); 

    //Lista que contiene las variables de la tabla de simbolos
    List<String> listaVariables= new ArrayList<>(); 
    int counter = 0; // Contador interno para generar etiquetas unicas
    Map<String, String> mapa = new HashMap<>();

    private QuadrupleGenerator listaCuadruplas;

    public Traductor(QuadrupleGenerator listCua){
        listaCuadruplas = listCua;

    }

   


    public void traducir (){
        for (int i = 0; i < listaCuadruplas.tamanio(); i++) {
            Quadruple quad = listaCuadruplas.get(i);
            
            //Verifica si la cuadrupla es referencia por una instruccion y si es asi 
            //le pone una bandera 
            verificarIndice(i);
            
            //esto es porque hay instruccion que contienen 2 renglones 
            if( !traducirQuadupla(quad,i)[0].equals("")){
                lista.add( traducirQuadupla(quad,i)[0]);
            }
           lista.add( traducirQuadupla(quad,i)[1]);


        } 

    }


    public String [] traducirQuadupla( Quadruple quad ,int indice ){
        String operador = quad.getOperator();
        String operando1= quad.getOperand1();
        String operando2 = quad.getOperand2();
        String result = quad.getResult();
        
        //Esto es para traducir el numero de la instruccion en una bandera y 
        // esa bandera la guarda en un mapa 
        String temp = traducirEtiqueta(operador, result);
        if (!temp.equals("")){
            result=temp;
        }
        
        //La traduccion depende del operador 
        String aux="";
        String traducida="";
        switch (operador) {
            case "+": 
                    
                    traducida="add "+"$"+result+", "+"$"+operando1+", "+"$"+operando2;
                break;
            case "-":
                    traducida="sub "+"$"+result+", "+"$"+operando1+", "+"$"+operando2;
                break;

            case "*":
                    traducida="mul "+"$"+result+", "+"$"+operando1+", "+"$"+operando2;
            
                break;
            case "/":
                    traducida="div "+"$"+result+", "+"$"+operando1+", "+"$"+operando2;
                    
                break;
            case "=":
                    traducida="move "+"$"+result+", "+"$"+operando1;
                break;



            case "<":
                traducida="slt "+"$"+result+", "+"$"+operando1+", "+"$"+operando2;

                break;


            case "<=":
                traducida="slt "+"$"+result+", "+"$"+operando2+", "+"$"+operando1;
                aux="xori "+"$"+result+", "+"$"+result+", "+"1";
                

                break;

            case ">":
                traducida="slt "+"$"+result+", "+"$"+operando2+", "+"$"+operando1;
               

                break;



            case ">=":
                traducida="slt "+"$"+result+", "+"$"+operando1+", "+"$"+operando2;
                aux="xori "+"$"+result+", "+"$"+result+", "+"1";


                break;

            //pendiente
            case "==":
                traducida="move "+"$"+result+", "+"$"+operando1;


                break;

            //pendiente
            case "!=":
                traducida="move "+"$"+result+", "+"$"+operando1;

                break;


            case "AND":
                //se transforma en 1 o en 0 segun corresponda 
                String entrada1="$"+operando1;
                String entrada2="$"+operando2;

                if (operando1.equals("true")){
                        entrada1="1";
                }
                if (operando1.equals("false")){
                    entrada1="0";
                }
                if (operando2.equals("true")){
                    entrada2="1";
                }
                if (operando2.equals("false")){
                    entrada2="0";
                }

                traducida="and "+"$"+result+", "+entrada1+", "+entrada2;

                break;


            case "OR":
                entrada1="$"+operando1;
                entrada2="$"+operando2;

                if (operando1.equals("true")){
                        entrada1="1";
                }
                if (operando1.equals("false")){
                    entrada1="0";
                }
                if (operando2.equals("true")){
                    entrada2="1";
                }
                if (operando2.equals("false")){
                    entrada2="0";
                }

                traducida="or "+"$"+result+", "+entrada1+", "+entrada2;
                aux="sne "+"$"+result+", "+"$"+result+", "+"$zero";


                break;



            case "!":
                traducida="xori "+"$"+result+", "+"$"+operando1+", "+"1";

                break;


            case "if":
                     // Patron para identificar operadores
                     //Esto se ocupa porque el operando1 viene todo junto de la forma (B1 rel.op B2)  
                    String operatorPattern = "==|!=|<=|>=|<|>";
                     // Compilar el patron
                    Pattern pattern = Pattern.compile(operatorPattern);
                    Matcher matcher = pattern.matcher(operando1);

                    // Arreglo para guardar los resultados
                    String[] parts = new String[3];

                    int lastIndex = 0;

                    // Buscar coincidencia del operador (unico)
                    if (matcher.find()) {
                        int start = matcher.start();

                        // Parte antes del operador
                        parts[0] = operando1.substring(0, start).trim();

                        // Operador encontrado
                        parts[1] = matcher.group();

                        // Parte despues del operador
                        parts[2] = operando1.substring(matcher.end()).trim();
                    }
                    
                     // Switch para manejar operadores
                    switch (parts[1]) {
                        case "==":
                            traducida="beq "+"$"+parts[0]+ ", $"+parts[2]+", $"+result;

                           
                            break;
                        case "!=":
                            traducida="bne "+"$"+parts[0]+ ", $"+parts[2]+", $"+result;
                            
                            break;
                        case "<":
                            aux="slt"+ " $t0"+ ", $"+parts[0]+", $"+parts[2]; 
                            traducida="bne "+"$t0"+", $zero, "+result;
                             
                            break;
                        case "<=":
                             aux="sle"+ " $t0"+ ", $"+parts[0]+", $"+parts[2]; 
                             traducida="bne "+"$t0"+", $zero, "+result;
                            
                            break;
                        case ">":
                            aux="sgt"+ " $t0"+ ", $"+parts[0]+", $"+parts[2]; 
                            traducida="bne "+"$t0"+", $zero, "+result;
                            break;
                        case ">=":
                            aux="sge"+ " $t0"+ ", $"+parts[0]+", $"+parts[2]; 
                            traducida="bne "+"$t0"+", $zero, "+result;
                        
                            break;
                        default:
                            System.out.println("Operador no reconocido");
                    }

                    
                break;
            case "goto":
                    traducida="j "+result;
                break;
        


            default:
                break;
        }

        String[] x = {aux,traducida};

        return  x ;
        
    }


   


    /*Verifica si hay una etiqueta en numero y la guarda en un mapa con una nueva
     * Etiqueta de la forma Ln y regresa la nueva etiqueta 
     */
    public String traducirEtiqueta(String operador ,String direccion){
        String etiqueta="";
        if(operador .equals("goto") ||operador .equals("if")){
        etiqueta = generateLabel();
            mapa.put(direccion,etiqueta);
        }
        return etiqueta;

    }

    /*Genera una cadena de la forma Ln */
    public String generateLabel() {
        counter++; // Incrementa el contador para generar una nueva etiqueta
        return "L" + counter; // Devuelve la etiqueta en el formato deseado
    }


    /*Si el indice corresponde a donde deberia ir una etiqueta saca del
     * mapa la etiqueta que corresponde con ese indice de Quadrupla
     */
    public void verificarIndice(int i){
        String indice = ""+i;
        if (mapa.containsKey(indice)){
            //Agregar a la lista de codigo mips 
            String temp=  mapa.get(indice);
            
            lista.add (temp+":");
        }

    }

    /**Metodo principal que se encarga de traducir a MIPS y dar estructura al archivo generado 
     * 
     * @param tabla
     */
    public void crear(SymbolTable tabla){

        traducir();
       List<String> listakeys = tabla.devolverKeys();
       listaVariables.add(".data");
       for (int i = 0; i < listakeys.size(); i++) {
        listaVariables.add( listakeys.get(i) +":"+" .word  0"  );
        
        }
        listaVariables.add(" ");
        listaVariables.add(" ");
        listaVariables.add(" ");
        listaVariables.add(".text");
        listaVariables.add("main:");
        listaVariables.add(" ");


        listaVariables.addAll(lista); 
        imprimirMips();
        ListToAsmFile generador =new ListToAsmFile() ;


        try {
          
            generador.writeListToAsmFile(listaVariables, "codigo.asm");
           
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo: " + e.getMessage());
        }
    }


    public void imprimirMips (){
        System.out.println("\n\n\n CODIGO MIPS \n ");
        System.out.println("Tambien genero el .asm\n ");

        for (int i = 0; i < listaVariables.size(); i++) {
            String x  = listaVariables.get(i);
            System.out.println(x);
        }

    }


}