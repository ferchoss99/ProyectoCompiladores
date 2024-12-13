//package proyecto.jflex;


import java.io.Reader;
// Nota: si lo puedo importar aunque todavia no este creado 
//import proyecto.byacc.Parser;  
//import proyecto.byacc.ParserVal;  






%%

// Para usar el parser dentro del lexer
%{
   
    private Parser parser;  // Instancia del parser 

   // constructor de lexer
    public Lexer(Reader r, Parser parser) {
        this(r);  
        this.parser = parser;  // Asigna el parser a la variable de instancia
    }
 

%}


/// Configuraciones del lexer
%public       // Declara la clase como pública
%class Lexer  // Define el nombre de la clase como "Lexer"
%standalone   // Puede usar independiente 
%unicode      // caracteres Unicode


// Definición de un número decimal
Numero = ([1-9][0-9]*|0)(\\.[0-9]+)?  
id = [a-zA-Z_][a-zA-Z0-9_]* 
runa = '[a-zA-ZáéíóúÁÉÍÓÚäëïöüÄËÏÖÜñÑ@#%\^\&\*\(\)\[\]\{\}\|;:,<>/\?~`\-+=\\\n\t\r\b\f]' 
letra = [a-zA-Z]
palabra = {letra}+

double = ([0-9]+(\.[0-9]*)?|\.[0-9]+)([eE][-+]?[0-9]+)?

float = ([0-9]+(\.[0-9]*)?|\.[0-9]+)([eE][-+]?[0-9]+)?[fF]

complejos = ({double}|{float})?([-+]?{double}|{float})?i


//Para debuguear
//%debug
%%








// Reglas con sus acciones lexicas
"+"          { return Parser.SUMA; }   
"*"          { return Parser.MULTI; }  
"/"          { return Parser.DIVI; }    
"-"          { return Parser.RESTA; }  
"^"          { return Parser.POTE; }  // Potencia
"("          { return Parser.LPAR; } // (
")"          { return Parser.RPAR; } // )
"//"         { return Parser.DIVISIONENTERA; } // División entera

// Operadores lógicos y de comparación
"||"         { return Parser.OR; }           // OR lógico
"&&"         { return Parser.AND; }          // AND lógico
"=="         { return Parser.IGUAL; }        // Igualdad
"!="         { return Parser.DESIGUAL; }     // Diferente
"<"          { return Parser.MENORQUE; }     // Menor que
"<="         { return Parser.MENORIGUAL; }   // Menor o igual
">"          { return Parser.MAYORQUE; }     // Mayor que
">="         { return Parser.MAYORIGUAL; }   // Mayor o igual
"!"          { return Parser.NEG; }          // Negación lógica


// Palabras clave
"proto"      { return Parser.PROTO; }        // Palabra clave proto
"struct"     { return Parser.STRUCT; }       // Palabra clave struct

"ptr"        { return Parser.PTR; }          // Palabra clave ptr
"int"        { return Parser.INT; }          // Palabra clave int
"float"      { return Parser.FLOAT; }        // Palabra clave float
"double"     { return Parser.DOUBLE; }       // Palabra clave double

"complex"    { return Parser.COMPLEX; }      // Palabra clave complex
"rune"       { return Parser.RUNE;} 
/*[runa]       {  String valor =yytext();
                parser.setValor(new ParserVal(valor));
                return Parser.LITERAL_RUNA; }   */      // Palabra clave rune


"void"       { return Parser.VOID; }         // Palabra clave void
"string"     { return Parser.STRING; }       // Palabra clave string



"func"       { return Parser.FUNC; }         // Palabra clave func
"if"         { return Parser.IF; }           // Palabra clave if
"else"       { return Parser.ELSE; }         // Palabra clave else
"while"      { return Parser.WHILE; }        // Palabra clave while
"do"         { return Parser.DO; }           // Palabra clave do
"break"      { return Parser.BREAK; }        // Palabra clave break
"return"     { return Parser.RETURN; }       // Palabra clave return
"switch"     { return Parser.SWITCH; }       // Palabra clave switch
"case"       { return Parser.CASE; }         // Palabra clave case
"default"    { return Parser.DEFAULT; }      // Palabra clave default
"print"      { return Parser.PRINT; }        // Palabra clave print
"scan"       { return Parser.SCAN; }         // Palabra clave scan
"true"       { return Parser.TRUE; }         // Palabra clave true
"false"      { return Parser.FALSE; }        // Palabra clave false



// Delimitadores y operadores adicionales
"{"          { return Parser.LLLAVE; }       // Llave izquierda
"}"          { return Parser.RLLAVE; }       // Llave derecha
"["          { return Parser.LCORCHETE; }    // Corchete izquierdo
"]"          { return Parser.RCORCHETE; }    // Corchete derecho
","          { return Parser.COMA; }         // Coma
";"          { return Parser.SEMICOLON; }    // Punto y coma
":"          { return Parser.COLON; }        // Dos puntos
"."          { return Parser.PUNTO; }        // Punto
"="          { return Parser.ASIG; }         // Asignación







// Regla para reconocer numeros 

// 


{Numero} { 
        // Convierte el texto del token a un valor double
	 double value = Double.parseDouble(yytext());
        /// setValor es un metodo que se usa para asignar el valor semantico del token que se acaba de leer 
        /// ParserVal es una clase auxiliar para guardar informacion , enteros ,double, string u objetos 
     parser.setValor(new ParserVal(value));
    return Parser.LITERAL_ENTERA;  // Retorna el token NUMERO, indicando que es un número
    //return Parser.LITERAL_DOBLE;
}

//PENDIENTE LOS 3 TIPOS DE NUMEROS CONVIERTEN EN DOUBLE  POR FACILIDAD 

/*{complejos} { 
        // Convierte el texto del token a un valor double
	 double value = Double.parseDouble(yytext());
        /// setValor es un metodo que se usa para asignar el valor semantico del token que se acaba de leer 
        /// ParserVal es una clase auxiliar para guardar informacion , enteros ,double, string u objetos 
     parser.setValor(new ParserVal(value));
    //return Parser.LITERAL_COMPLEJA;  // Retorna el token NUMERO, indicando que es un número
    return Parser.LITERAL_ENTERA;
}*/



{double} { 
        // Convierte el texto del token a un valor double
	 double value = Double.parseDouble(yytext());
        /// setValor es un metodo que se usa para asignar el valor semantico del token que se acaba de leer 
        /// ParserVal es una clase auxiliar para guardar informacion , enteros ,double, string u objetos 
     parser.setValor(new ParserVal(value));
    return Parser.LITERAL_ENTERA;  // Retorna el token NUMERO, indicando que es un número
}


{float} { 
        // Convierte el texto del token a un valor double
	 double value = Double.parseDouble(yytext());
        /// setValor es un metodo que se usa para asignar el valor semantico del token que se acaba de leer 
        /// ParserVal es una clase auxiliar para guardar informacion , enteros ,double, string u objetos 
     parser.setValor(new ParserVal(value));
    //return Parser.LITERAL_FLOTANTE;  // Retorna el token NUMERO, indicando que es un número
    return Parser.LITERAL_ENTERA;
}




{id}    {   String valor =yytext();
            parser.setValor(new ParserVal(valor));
            return Parser.ID ;}




[palabra]       {  String valor =yytext();
                parser.setValor(new ParserVal(valor));
                return Parser.LITERAL_STRING; }        




// Espacios en blanco
([\s\t\r\n]+)     { }

// Regla para manejar saltos de lInea 
//\n           { return Parser.SALTOLINEA; }  // Retorna un token para el salto de línea

// Regla para manejar el fin de archivo (EOF)
<<EOF>>      { return -1; }      // Retorna -1 para indicar el fin de archivo

// El punto puede ser cualquier cosa asi que si no es un caracter reconocido 
// mandar un error lexico 
.            { System.err.println("Error léxico: " + yytext()); }

