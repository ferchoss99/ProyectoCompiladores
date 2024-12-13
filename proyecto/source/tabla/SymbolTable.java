package tabla;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;


public class SymbolTable {

    
    // Clase interna para representar un símbolo
    public static class Symbol {
        public String type; // Tipo de dato (int, float, etc.)
        public Object value; // Valor opcional asociado al símbolo

        public Symbol(String type, Object value) {
            this.type = type;
            this.value = value;
        }

        // Getters y setters
        public String getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "Symbol{" +
                    "type='" + type + '\'' +
                    ", value=" + value +
                    '}';
        }
    }

    // Pila de tablas de símbolos (cada tabla corresponde a un alcance)
    public Stack< Map<String, Symbol> > symbolStack;
    public String currentScope;

    public SymbolTable() {
        symbolStack = new Stack<>();
        
        // Crear la tabla global inicialmente
        symbolStack.push(new HashMap<>());
        currentScope = "global"; // El alcance inicial es global
    }

    // Método para cambiar de alcance
    public void enterScope(String scopeName) {
        symbolStack.push(new HashMap<>());
        currentScope = scopeName;
    }

    // Método para salir de un alcance
    public void exitScope() {
        if (symbolStack.size() > 1) { // Evita que la pila de tablas quede vacía
            symbolStack.pop();
            currentScope = "global"; // Volver al alcance global por defecto
        } else {
            System.out.println("Error: No se puede salir del alcance global.");
        }
    }

    // Método para agregar un símbolo
    public void addSymbol(String identifier, String type, Object value) {
        
        if (symbolStack.peek().containsKey(identifier)) {
            System.out.println("Error: El símbolo '" + identifier + "' ya está definido en este alcance.");
        } else {
            symbolStack.peek().put(identifier, new Symbol(type, value));
            
        }
    }

    // Método para buscar un símbolo en los alcances
    public Symbol getSymbol(String identifier) {
        for (int i = symbolStack.size() - 1; i >= 0; i--) {
            Map<String, Symbol> scope = symbolStack.get(i);
            if (scope.containsKey(identifier)) {
                return scope.get(identifier);
            }
        }
        return null; // Si no se encuentra, devuelve null
    }


    // Método para mostrar la tabla de símbolos
    public void printTable() {
        System.out.println("Tabla de Símbolos en el alcance: " + currentScope);
        for (Map.Entry<String, Symbol> entry : symbolStack.peek().entrySet()) {
            System.out.println("Identificador: " + entry.getKey() + " -> " + entry.getValue());
        }
    }


    public List<String>  devolverKeys() {
        List<String> lista= new ArrayList<>(); 
        for (Map.Entry<String, Symbol> entry : symbolStack.peek().entrySet()) {
           
            lista.add(entry.getKey());
        }
    return lista; 
    }
    

}
