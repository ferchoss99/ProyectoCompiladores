import java.lang.Math;
import java.io.Reader;
import java.io.IOException;
import java.io.*;
import java.util.*;
import tabla.SymbolTable;
import java.util.HashMap;


class Lista {
    

     // Creamos el ArrayList
    List<Integer> lista ;
    QuadrupleGenerator qua;

 // Agregamos un elemento a la lista
    public Lista( QuadrupleGenerator cuadrupla) {
         qua = cuadrupla;
        
    }

    public List<Integer> makelist(int indice) {

        lista = new ArrayList<>();
        lista.add(indice);
        return lista ;

    }

    public List<Integer> merge(List<Integer> p1,List<Integer> p2) {
         list = new ArrayList<>();
 
        list.addAll(p1);
        list.addAll(p2);
     
        return list;
    }    

    public void imprimir(){
        qua.printQuadruples();
    }

  
}
