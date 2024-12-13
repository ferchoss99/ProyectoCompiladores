%{
  
  import java.lang.Math;
  import java.io.Reader;
  import java.io.IOException;
  import java.io.*;
  import java.util.*;
  import tabla.SymbolTable;
  import java.util.HashMap;
  import java.util.ArrayList;
  import java.util.List;

  import utils.Context;







%}




/// los tokes seran los terminales que seran reconocidos

%token SUMA RESTA MULTI DIVI POTE  LPAR RPAR  NUMERO SALTOLINEA ID
%token PROTO STRUCT PTR INT FLOAT DOUBLE COMPLEX RUNE VOID STRING FUNC IF ELSE WHILE DO BREAK RETURN
%token SWITCH CASE DEFAULT PRINT SCAN TRUE FALSE
%token LLLAVE RLLAVE LCORCHETE RCORCHETE COMA SEMICOLON COLON PUNTO ASIG OR AND IGUAL DESIGUAL MENORQUE MENORIGUAL MAYORQUE
%token MAYORIGUAL DIVISIONENTERA NEG
%token  LITERAL_RUNA  LITERAL_ENTERA LITERAL_CADENA PRED   LITERAL_COMPLEJA LITERAL_FLOTANTE LITERAL_DOBLE


// Precedencias y asociatividades de operadores
%left RESTA SUMA          
%left MULTI DIVI           
%right POTE               
//%left NEG                 // Negación unaria
%nonassoc LPAR RPAR   // Paréntesis son no asociativos

%left OR       /* Precedencia más baja */
%left AND
%left IGUAL DESIGUAL
%left MENORQUE MENORIGUAL MAYORQUE MAYORIGUAL
%left MAS MENOS
%left MULT DIV MOD DIVENTERA
%right NEG      /* Negación lógica y menos unario tienen mayor precedencia */
%nonassoc '(' ')' /* Paréntesis son no asociativos para evitar problemas de ambigüedad */





/* Gramatica */

%%
input :
    /* Cadena vacía */
  | input line          
;

line:           
    programa    
    

programa:
      //Cuando termine todas las acciones semanticas va a traducir a codigo MIPS
      decl_proto decl_var decl_func  { 
                                        parserActions.printGeneratedCode();
                                        System.out.println("\n\n\n");

                                        tablaSimbolos.printTable();
                                        System.out.println("\n\n\n");
                                        Traductor traductor = new Traductor(parserActions.listaCuadruplas());
                                        traductor.crear(tablaSimbolos);
                                     }





decl_proto:
    PROTO tipo ID LPAR argumentos RPAR SEMICOLON decl_proto
    | /* ε */      



decl_var:
    tipo lista_var SEMICOLON decl_var   
      {
        //Aqui se declaran las variables y se guardan en la tabla de datos
        //Aqui tengo que recorrer el $2sval y separar en comas para guardarlos en la tabla de simbolos
        String[] elementos = $2.sval.split(",");
        for (String elemento : elementos) {
          tablaSimbolos.addSymbol(elemento,$1.sval,elemento);  
        }
       
      }
    | /* ε */  





tipo:
      basico compuesto    {$$=$1;}
      | STRUCT LLLAVE decl_var RLLAVE
      | puntero


puntero:
      PTR basico


basico:
      INT   {$$=new ParserVal("int");}
    
    | FLOAT  {$$=new ParserVal("float");}
    
    | DOUBLE {$$=new ParserVal("double");} 

    | COMPLEX
    | RUNE
    | VOID
    | STRING



compuesto:
      LCORCHETE LITERAL_ENTERA RCORCHETE compuesto
    | /* ε */  

lista_var:
    lista_var COMA ID    
    {
      //Genera ID  recursivamente 
      $$ = new ParserVal ($1.sval +","+ $3.sval);
    
    }
    | ID     
        //regresa el parserVal que contiene el id reconocido
        { $$=$1; } 


//Entra y sale de rango de una tabla de simbolos cada que entra a una funcion 
decl_func:{tablaSimbolos.enterScope("decl_func");}  
    FUNC tipo ID LPAR argumentos RPAR bloque decl_func  {tablaSimbolos.exitScope();}
    | /* ε */  

argumentos:
    lista_args
    | /* ε */ 

lista_args:    
    lista_args COMA tipo ID 
    | tipo ID 

bloque:   
        LLLAVE declaraciones instrucciones RLLAVE  {$$ = $3;}
      
        



declaraciones:
    decl_var
    /* ε */ 


instrucciones:
    instrucciones  sentencia  {$$ = $1;}

    |  sentencia   {$$ = $1;}



sentencia:
    //Separe las acciones de las demas sentencias para poder operar los if y sus saltos adecuadamente 
     asignacion  {$$ = $1;}
     //Cad que va a entrar a un if o while se crea otra tabla de simbolos ,para hacer declaraciones en ella
    |{tablaSimbolos.enterScope("resolve");}    resolve   {$$ = $2;
                                  tablaSimbolos.exitScope();} 
    
resolve  :  matched_stmt M 
             //Esto asigna las etiquetas faltantes que apuntan despues del if
            {parserActions.backpatch((List<Integer>) $1.obj  , $2.ival , "para la N" ) ;}

matched_stmt:
          
          // M es un placeholder para obtener la direccion a donde ir en caso de TRUE  
          IF LPAR exp1 RPAR M matched_stmt 
                { 
                //Para mandar informacion a traves de parserVal creo un Mapa   
                Context contexto = (Context) $3.obj;
            
                //obtengo la lista de direcciones donde falta poner la bandera 
                List<Integer> l1= contexto.getVariable("B.truelist");
                List<Integer> l2= contexto.getVariable("B.falselist");

                //Se resuelve las banderas faltantes 
                parserActions.backpatch(l1,$5.ival, "B.truelist" );
                //Se regresan las banderas que apuntan a la siguiente instruccion (despues de if)
                $$ = new ParserVal(parserActions.merge(l2, (List<Integer>) $6.obj));
                

          }

         
        
        | IF LPAR exp1 RPAR M  matched_stmt  ELSE N M matched_stmt  
          {  Context contexto = (Context) $3.obj;
               
                //Obtenemos direcciones
                List<Integer> l1= contexto.getVariable("B.truelist");
                List<Integer> l2= contexto.getVariable("B.falselist");

                //resuelve las banderas con las direcciones y con los placeholders M y N
                parserActions.backpatch(l1,$5.ival ,"Btruelist1");
                parserActions.backpatch(l2,$9.ival,"Bfalselist2");
                List<Integer> temp= parserActions.merge((List<Integer>)$6.obj,(List<Integer>)$8.obj );

                $$ = new  ParserVal( parserActions.merge(temp,(List<Integer>)$10.obj) );
          }

        //Quedaron pendientes :( 



        | WHILE LPAR M exp1 RPAR M matched_stmt   
                {
                    parserActions.backpatch((List<Integer>)$7.obj,$3.ival,"");

                    Context contexto = (Context) $4.obj;
                    //Obtenemos direcciones
                    List<Integer> l1= contexto.getVariable("B.truelist");
                    List<Integer> l2= contexto.getVariable("B.falselist");

                    parserActions.backpatch((List<Integer>)$7.obj,$3.ival,"");
                    parserActions.backpatch(l1,$6.ival,"");
                    $$ = new ParserVal(l2);
                    parserActions.handleGoto2(""+$3.ival);

                    
                }



        | DO matched_stmt WHILE LPAR exp RPAR SEMICOLON
        | BREAK SEMICOLON
        | bloque
        | RETURN exp SEMICOLON
        | RETURN SEMICOLON
        | SWITCH LPAR exp RPAR LLLAVE casos RLLAVE
        | PRINT exp SEMICOLON
        | SCAN parte_izquierda
       
      

M:  /* ε */ {
            //Regresa la ubicacion de la instruccion siguiente
            int x =  parserActions.nextinstr();
            $$ = new ParserVal(x);
            
            }
;

             
N :  /* ε */ { 
              //Va a regresar la ubicacion de la instruccion siguiente
              $$ = new ParserVal( parserActions.makelist(parserActions.nextinstr()) );
              //imprime un salto para que se salte el else
              parserActions.handleGoto();
            
             }




asignacion: 
    parte_izquierda  ASIG exp  SEMICOLON  

      {
        
       //Verifica que exista el simbolo es decir que haya sido declarado primero 
       SymbolTable.Symbol simbolo = tablaSimbolos.getSymbol($1.sval);
        if(simbolo != null){
          
          parserActions.handleAsignacion($1 ,$3 );
          $$ =new ParserVal ((new ArrayList<Integer>()));
          
        }         
        
        else{
          System.out.println("Simbolo no existe");
          $$ =new ParserVal ((new ArrayList<Integer>()));
        }
       
      }
      


casos:
      caso casos
    | PRED
    | /* ε */

caso:
      CASE opcion COLON instrucciones

opcion:
      LITERAL_ENTERA
    | LITERAL_RUNA

parte_izquierda:
      ID localizacion
    | ID     {  $$=$1;}

localizacion:
      arreglo
    | estructurado

arreglo:
      arreglo LCORCHETE exp RCORCHETE
    | LCORCHETE exp RCORCHETE

estructurado:
      estructurado PUNTO ID
    | PUNTO ID

parametros:
      lista_param
    | /* ε */

lista_param:
      lista_param COMA exp
    | exp



// Operaciones básicas en BYACC/Java
exp:  LITERAL_ENTERA 
    {
         // Si es un número, simplemente lo retornamos.
         // lo regresamos como String , ya que el objetivo no es hacer la operacion aqui.
        $$ = new ParserVal ( Double.toString( $1.dval));

    }
    // las parserActions.handle***  generan las representaciones de codigo de 3 instrucciones 
    // y las guarda en cuadruplas 

    |exp SUMA exp  {   $$ = new ParserVal (parserActions.handleAddition($1, $3)); }
    
    | exp RESTA exp {   $$ = new ParserVal (parserActions.handleSubtraction($1, $3));   }

    | exp MULTI exp  {  $$ = new ParserVal (parserActions.handleMultiplication($1, $3));     }
        

    | LPAR exp RPAR    { $$ =$2; }  
    
    | exp DIVI exp          {$$ = new ParserVal (parserActions.handleDivi($1, $3));}


    | RESTA exp %prec NEG   { $$ = new ParserVal(parserActions.handleNEGATIVO($2)); }      
    | exp POTE exp           { $$ = new ParserVal(parserActions.handlePOTE($1, $3)); }


    | exp OR M exp           { $$ = new ParserVal(parserActions.handleOR($1, $3)); } 
    | exp AND exp            { $$ = new ParserVal(parserActions.handleAND($1, $3)); } 
    | exp IGUAL exp          { $$ = new ParserVal(parserActions.handleIGUAL($1, $3)); } 
    | exp DESIGUAL exp       { $$ = new ParserVal(parserActions.handleDESIGUAL($1, $3)); }
    | exp MENORQUE exp       { $$ = new ParserVal(parserActions.handleMENORQUE($1, $3)); }
    | exp MENORIGUAL exp     { $$ = new ParserVal(parserActions.handleMENORIGUAL($1, $3)); }
    | exp MAYORQUE exp       { $$ = new ParserVal(parserActions.handleMAYORQUE($1, $3)); }
    | exp MAYORIGUAL exp     { $$ = new ParserVal(parserActions.handleMAYORIGUAL($1, $3)); }
    | exp MOD exp            { $$ = new ParserVal(parserActions.handleMOD($1, $3)); }
    | exp DIVENTERA exp      { $$ = new ParserVal(parserActions.handleDIVENTERA($1, $3)); }
    | NEG exp                { $$ = new ParserVal(parserActions.handleNEG($2)); }
                   

    | ID   {  $$=$1;  }
    | ID '(' parametros ')'
    | TRUE    { $$= new ParserVal("true");  }

    | FALSE   { $$= new ParserVal("false"); }


    
/* 


exp:

    //Faltan estas 

    | LITERAL_ENTERA
    | LITERAL_RUNA
    | LITERAL_CADENA
    | LITERAL_FLOTANTE
    | LITERAL_DOBLE
    | LITERAL_COMPLEJA
    ;

*/



//  Las siguientes expresiones se ocupan  para tratar las expresiones booleanas que vienen de un if de manera diferente 
// porque tienen que considerar los saltos que se dan y con las de arriba no se puede .

exp1: 
    
         exp1 OR M exp1             { 
            Context contexto = (Context) $1.obj;
            List<Integer> B1truelist= contexto.getVariable("B.truelist");
            List<Integer> B1falselist= contexto.getVariable("B.falselist");

            Context contexto2 = (Context) $4.obj;
            List<Integer> B2truelist= contexto2.getVariable("B.truelist");
            List<Integer> B2falselist= contexto2.getVariable("B.falselist");

            parserActions.backpatch(B1falselist,$3.ival,"B1falselist");
            Context contexto3= new Context();
            contexto3.setVariable ("B.truelist", parserActions.merge(B1truelist,B2truelist));
            contexto3.setVariable ("B.falselist",B2falselist);
            $$ = new ParserVal(contexto3);

            }
        | exp1 AND M exp1     { 
            Context contexto = (Context) $1.obj;
            List<Integer> B1truelist= contexto.getVariable("B.truelist");
            List<Integer> B1falselist= contexto.getVariable("B.falselist");

            Context contexto2 = (Context) $4.obj;
            List<Integer> B2truelist= contexto2.getVariable("B.truelist");
            List<Integer> B2falselist= contexto2.getVariable("B.falselist");

            parserActions.backpatch(B1truelist,$3.ival,"B1truelist");
            
            Context contexto3= new Context();
            contexto3.setVariable ("B.truelist",B2truelist );

            contexto3.setVariable ("B.falselist",parserActions.merge(B1falselist,B2falselist));
            $$ = new ParserVal(contexto3);

            }

        | LPAR exp1 RPAR    { 
            Context contexto = (Context) $2.obj;
            List<Integer> B1truelist= contexto.getVariable("B.truelist");
            List<Integer> B1falselist= contexto.getVariable("B.falselist");
  
            Context contexto3= new Context();
            contexto3.setVariable ("B.truelist",B1truelist );
            contexto3.setVariable ("B.falselist",B1falselist);
            $$ = new ParserVal(contexto3);

            }

        | exp1 IGUAL exp1          { 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ $1.sval + " == " + $3.sval );
                parserActions.handleGoto();

                $$ = new ParserVal(context);
                
            }
        | exp1 DESIGUAL exp1       { 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ $1.sval + " != " + $3.sval );
                parserActions.handleGoto();

                $$ = new ParserVal(context);
                
            }
        | exp1 MENORQUE exp1       { 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ $1.sval + " < " + $3.sval );
                parserActions.handleGoto();

                $$ = new ParserVal(context);
                
            }
        | exp1 MENORIGUAL exp1     { 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ $1.sval + " <= " + $3.sval );
                parserActions.handleGoto();

                $$ = new ParserVal(context);
                
            }
        | exp1 MAYORQUE exp1       { 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ $1.sval + " > " + $3.sval );
                parserActions.handleGoto();

                $$ = new ParserVal(context);
                
            }
        | exp1 MAYORIGUAL exp1     { 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ $1.sval + " >= " + $3.sval );
                parserActions.handleGoto();

                $$ = new ParserVal(context);
                
            }
        | NEG exp1  { 
                  Context contexto = (Context) $2.obj;
                  List<Integer> B1truelist= contexto.getVariable("B.truelist");
                  List<Integer> B1falselist= contexto.getVariable("B.falselist");
        
                  Context contexto3= new Context();
                  contexto3.setVariable ("B.truelist",B1falselist );
                  contexto3.setVariable ("B.falselist",B1truelist);
                  $$ = new ParserVal(contexto3);

                  }
        
        //ID  tendria que buscar su valor
        | ID   {
                $$=$1;
               
              }
        | ID '(' parametros ')'
        | TRUE    {

                  Context contexto = new Context(); 
                  contexto.setVariable("B.truelist",parserActions.makelist(parserActions.nextinstr()));
                  contexto.setVariable("B.falselist", new ArrayList<Integer>() ); 
                    
                  parserActions.handleGoto();

                  $$= new ParserVal(contexto);
                  }


                  
        | FALSE   { 

                  Context contexto = new Context(); 
                  contexto.setVariable("B.truelist", new ArrayList<Integer>() );
                  contexto.setVariable("B.falselist",parserActions.makelist(parserActions.nextinstr()) ); 

                  parserActions.handleGoto();

                  $$= new ParserVal(contexto);
                  }

        | LITERAL_ENTERA 
                  {
                      // Si es un número, simplemente lo retornamos.
                      $$ = new ParserVal ( Double.toString( $1.dval));

                  }

   
        



%%



/* Instancia del lexer */
Lexer scanner;

//Generamos la tabla de simbolos
SymbolTable tablaSimbolos = new SymbolTable();
ParserActions parserActions =new ParserActions();




/* Constructor del parser */
public Parser(Reader r) {
  
  this.scanner = new Lexer(r, this);  // Inicializa el lexer con el lector de entrada
 
}

// Método para establecer  el valor del token actual 
public void setValor(ParserVal valor) {
  ///yylval es una variable para guardar el valor//el lexema // del token 
  this.yylval = valor; 
}


//METODO QUE  INICIA TODO  
  //desde el main
public void parse() {
  // este metodo pide manda a llamar yylex repetidamente para obtener los tokens uno a uno 
  this.yyparse();  
}


 
//El metodo se recomienda sobreescribirlo para permitir personalizar cómo se manejan y reportan errores
void yyerror(String error1) {
  System.out.println("Error sintactico: " + error1);  
}


// Obtener el token actual
// Debido a que implemente el lexer dentro del parser tengo que definir la función yylex para que sea 
// llamada por parse() 

int yylex() {
  int token = -1;
  try {
    token= scanner.yylex(); 
    //parserActions.printGeneratedCode();


  } catch (IOException e) {
    System.err.println("Error ");  
  }
 
  return token;  // Retorna el token obtenido
}


