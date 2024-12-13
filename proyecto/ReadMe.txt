Funciona bien  cuando se utiliza para operaciones basicas 
func , if , if-else y while

Se utilizo el enfoque de codigo de 3 direcciones para obtener cuadruplas y apartir de ellas
traducirlas y obtener el codigo  ensamblador(MIPS)

El control de flujo se hizo mediante Backpatching para poder colocar las etiquetas en 
el lugar correcto y hacer solo una pasada , se intento solo colocando  etiquetas donde se necesitaban pero 
eso requeria usar atributos heredados y como en LR(1) no se ocupan pues se descarto esa opcion.

Una vez que se tenia el codigo en cuadruplas se hizo la traducion una a una para obtener el codigo ensamblador .
A etiquetas obtenidas por Backpatching se les tuvo que dar un preproso porque los saltos dependen del 
indice de la tupla , por lo cual no estaban textualmente implementadas en cuadruplas.




Para compilar 
make run

Para limpiar 
make clean


Entrada : Como entrada lee un archivo .txt donde ponemos la estructura del codigo fuente que queremos 
          traducir 

Salida : Recibimos un archivo .asm el cual contiene el codigo ensamblador correspondiente
        al codigo de entrada .          

jflex Lexer.flex
javac QuadrupleGenerator.java
byaccj -J parserProyecto.y
javac Main.java  
java Main  


