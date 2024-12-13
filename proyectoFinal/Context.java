import java.lang.Math;
import java.io.Reader;
import java.io.IOException;
import java.io.*;
import java.util.*;
import tabla.SymbolTable;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;


class Context {
    private HashMap<String, List<Integer>> variables;

    /**
     * Clase para administrar el hasmap de cadenas y lista de enteros 
     * su uso es mantener los indices de los saltos
     */
    public Context() {
        variables = new HashMap<>();
    }

    public void setVariable(String name, List<Integer> value) {

        if (variables.containsKey(name)) {
            variables.remove(name);
            variables.put(name, value); // Guarda la variable

        } else {
          
          variables.put(name, value); // Guarda la variable
            
        }

    }

    public List<Integer> getVariable(String name) {
        if (variables.containsKey(name)) {
            return variables.get(name); // Retorna el valor
        } else {
          
            throw new RuntimeException("Variable no definida: " + name);
        }
    }


    public void imprimir (){
        //System.out.println( variables.size()  +"      \n\n\n\n\n\n\n\n");
        for (HashMap.Entry<String, List<Integer>> entry : variables.entrySet()) {
            System.out.println("Clave: " + entry.getKey() + ", Valor: " + entry.getValue());
        }
        System.out.println("\n\n\n\n\n\n\n\n");

    }
}
