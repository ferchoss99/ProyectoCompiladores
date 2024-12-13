# Reglas
.PHONY: lexer


run : compilar
	java Main test.txt	


compilar: tablaSimbolos lexer  quadruple proyecto main 

lexer: 
	jflex Lexer.flex

tablaSimbolos: 
	cd tabla && javac SymbolTable.java


quadruple: 
	javac quadruple/QuadrupleGenerator.java

contexto : 
	javac utils/Context.java; 

main :
	javac Main.java

clean :
	rm *.class




proyecto : 
	byaccj -J -d -v  parserProyecto.y



