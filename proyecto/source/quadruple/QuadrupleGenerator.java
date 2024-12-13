package quadruple;

import java.util.ArrayList;
import java.util.List;

public class QuadrupleGenerator {
    private List<Quadruple> quadruples;  // Lista de cuádruplas
    private int tempCounter;            // Contador para variables temporales
    public int fila;

    /**
     * Administra y guarda las quadruplas en una lista 
     */
    public QuadrupleGenerator() {
        this.quadruples = new ArrayList<>();
        this.tempCounter = 0;
        this.fila=0;
    }

    // Genera una nueva variable temporal
    public String newTemp() {
        return "t" + (tempCounter++);
    }

    // Agrega una cuádrupla
    public void emit(String operator, String operand1, String operand2, String result) {
        Quadruple quad = new Quadruple(operator, operand1, operand2, result);
        quadruples.add(quad);
        fila=fila+1;
    }

    // Obtiene la lista de cuádruplas generadas
    public List<Quadruple> getQuadruples() {
        return quadruples;
    }

    // Imprime todas las cuádruplas
    public void printQuadruples() {
        System.out.println(" \n\n\nCUADRUPLAS GENERADAS \n");
        int contador = 0;
        for (Quadruple q : quadruples) {
            System.out.println(contador +" : "+q);
            contador++;
        }
    }


    public Quadruple get(int i){
         return quadruples.get(i);
        
    }

    public int tamanio (){
        return fila;

    }
}
