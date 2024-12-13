
import  quadruple.*;
import java.util.ArrayList;
import java.util.List;


public class ParserActions {

    List<Integer> lista ;
    private QuadrupleGenerator codeGen = new QuadrupleGenerator();

    /**Recibe operandos y genera las acciones semanticas del archivo byacc para 
     * no ocupar tanto espacio en el archivo principal 
     */

    
     public void  handleAsignacion(ParserVal operand1, ParserVal operand2) {
     
        codeGen.emit("=", operand2.sval,"", operand1.sval);
        
    }

    public String handleAddition(ParserVal operand1, ParserVal operand2) {
        
        String temp = codeGen.newTemp();
        codeGen.emit("+",operand1.sval,operand2.sval, temp);
        
        return temp;
    }

    public String handleSubtraction(ParserVal  operand1,ParserVal  operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("-",operand1.sval,operand2.sval, temp);
        return temp;
    }

    public String handleMultiplication(ParserVal  operand1, ParserVal  operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("*",operand1.sval,operand2.sval, temp);
        return temp;
    }

    public String handleDivi(ParserVal  operand1, ParserVal  operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("/",operand1.sval,operand2.sval, temp);
        return temp;
    }

    public String handlePOTE(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("^", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleOR(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("OR", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleAND(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("AND", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleIGUAL(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("==", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleDESIGUAL(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("!=", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleMENORQUE(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("<", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleMENORIGUAL(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("<=", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleMAYORQUE(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit(">", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleMAYORIGUAL(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit(">=", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleMOD(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("%", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleDIVENTERA(ParserVal operand1, ParserVal operand2) {
        String temp = codeGen.newTemp();
        codeGen.emit("//", operand1.sval, operand2.sval, temp);
        return temp;
    }
    
    public String handleNEG(ParserVal operand1) {
        String temp = codeGen.newTemp();
        codeGen.emit("!", operand1.sval, "", temp);
        return temp;
    }

    public String handleNEGATIVO(ParserVal operand1) {
        String temp = codeGen.newTemp();
        codeGen.emit("-", operand1.sval, "", temp);
        return temp;
    }
    

    public String handleLABEL(String operand1) {
        codeGen.emit(operand1, "", "", "");
        return "";
    }

    public String handleGoto() {
        codeGen.emit("goto", "", "", "_");
        return "";
    }

    public String handleGoto2(String x) {
        codeGen.emit("goto", "", "", x);
        return "";
    }

    public String handleIfGoto( String operand ) {
            codeGen.emit("if", operand, "goto","_");
        return "";
    }



    /*Hace una lista de enteros con un unico elemento */
    public List<Integer> makelist(int indice) {

        lista = new ArrayList<>();
        lista.add(indice);
        return lista ;

    }

    public List<Integer> merge(List<Integer> p1,List<Integer> p2) {
         lista = new ArrayList<>();
 
        lista.addAll(p1);
        lista.addAll(p2);
     
        return lista;
    }    



 /**Recibe una lista de enteros y un entero ,los enteros representan los indices de cuadruplas 
  * que necesitan acompletar sus saltos medienta el numero de instruccion al que tienen que saltar 
  * 
  * @param p 
  * @param x Instruccion a donde ira el goto 
  * @param nombrelista De uso didactico, solo para indentificar cual lista es la que entro 
  */
public void backpatch(List<Integer> p , int x, String nombrelista){

    Quadruple cua ;
   
    for (int i = 0; i < p.size(); i++) {
      
        cua = codeGen.get(p.get(i));
        String operador = cua.getOperator();
       
        
        if (operador.equals("goto")){
    
            cua.setOperator(""+x,4);
        }else{
            //obtenemos la cuadrupla
            cua = codeGen.get(p.get(i));
            operador = cua.getOperand2();
            if(operador .equals("goto")){
          
            cua.setOperator(""+x, 4);

            }else{

                System.out.println("\n\n\n\n\n\n No concuerda indice \n\n\n\n\n\n ");
            }
            
            
        }
    }
   


}

    // Método para imprimir el código generado
    public void printGeneratedCode() {
        codeGen.printQuadruples();
    }

  
    public int nextinstr(){
        return codeGen.tamanio();
        
    } 

    /**
     * 
     * @return clase que contiene todas las cuadruplas generadas 
     */
    public QuadrupleGenerator  listaCuadruplas (){
      return codeGen;  
    }
}
