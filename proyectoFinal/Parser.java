//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "parserProyecto.y"
  import java.lang.Math;
  import java.io.Reader;
  import java.io.IOException;
  import java.io.*;
  import java.util.*;
  import tabla.SymbolTable;
  import java.util.HashMap;
  import java.util.ArrayList;
  import java.util.List;






//#line 33 "Parser.java"




public class Parser
             implements ParserTokens
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    0,    1,    2,    3,    3,    4,    4,    6,    6,
    6,   11,    9,    9,    9,    9,    9,    9,    9,   10,
   10,    8,    8,    5,    5,    7,    7,   13,   13,   12,
   14,   15,   15,   16,   16,   18,   19,   19,   19,   19,
   19,   19,   19,   19,   19,   19,   19,   20,   22,   17,
   24,   24,   24,   26,   27,   27,   25,   25,   28,   28,
   29,   29,   30,   30,   31,   31,   32,   32,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,   23,   23,
   23,   21,   21,   21,   21,   21,   21,   21,   21,   21,
   21,   21,   21,   21,   21,   21,
};
final static short yylen[] = {                            2,
    0,    2,    1,    3,    8,    0,    4,    0,    2,    4,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    4,
    0,    3,    1,    8,    0,    1,    0,    4,    2,    4,
    1,    2,    1,    1,    1,    2,    6,   10,    5,    7,
    2,    1,    3,    2,    7,    3,    2,    0,    0,    4,
    2,    1,    0,    4,    1,    1,    2,    1,    1,    1,
    4,    3,    3,    2,    1,    0,    3,    1,    1,    3,
    3,    3,    3,    3,    2,    3,    4,    3,    3,    3,
    3,    3,    3,    3,    3,    3,    2,    1,    4,    1,
    1,    4,    4,    3,    3,    3,    3,    3,    3,    3,
    2,    1,    4,    1,    1,    1,
};
final static short yydefred[] = {                         1,
    0,    0,    2,    3,    0,    0,    0,   13,   14,   15,
   16,   17,   18,   19,    0,    0,   11,    0,    0,    0,
   12,    0,    0,    9,    0,    4,   23,    0,    0,    0,
    0,    0,    0,    0,   10,    0,    0,    0,    0,    0,
   22,    7,   29,    0,    0,   20,    0,    0,    0,    0,
    5,   28,    0,    0,    0,   31,    0,   24,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   42,    0,   33,
   34,   35,   48,    0,    0,    0,   57,    0,    0,    0,
    0,    0,   41,    0,    0,    0,   90,   91,   44,    0,
   69,    0,    0,    0,   47,   30,   32,   36,    0,    0,
   64,    0,    0,    0,    0,  104,  105,    0,  106,    0,
    0,    0,   75,    0,    0,   87,    0,    0,    0,    0,
    0,   43,   48,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   46,    0,   62,    0,   63,    0,    0,
  101,   48,   48,   48,    0,    0,    0,    0,    0,    0,
    0,    0,   73,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   85,
   86,    0,   50,   61,   94,    0,    0,    0,    0,    0,
    0,   97,   98,   99,  100,   39,    0,   89,    0,    0,
    0,  103,    0,    0,    0,    0,    0,    0,   52,    0,
    0,   49,   40,   56,   55,    0,   45,   51,   48,    0,
    0,    0,   38,
};
final static short yydgoto[] = {                          1,
    3,    4,    5,   18,   26,   19,   37,   28,   16,   24,
   17,   68,   38,   57,   69,   70,   71,   72,   73,   98,
  110,  209,  154,  200,   74,  201,  206,   77,   78,   79,
  155,  156,
};
final static short yysindex[] = {                         0,
 -259,   64,    0,    0,   64, -277,  -81,    0,    0,    0,
    0,    0,    0,    0, -241, -264,    0, -236, -222,   64,
    0, -216, -257,    0,   64,    0,    0, -261, -227,   64,
 -203, -167, -165,   64,    0, -162, -156, -184, -264, -147,
    0,    0,    0, -159,   64,    0,   64, -259, -149, -132,
    0,    0, -146,   64, -236,    0,  -42,    0, -281, -121,
 -115,  560, -142, -150, -106, -211, -116,    0,  596,    0,
    0,    0,    0, -129, -211, -109,    0, -122, -126, -160,
 -160, -107,    0, -211, -211,  145,    0,    0,    0, -211,
    0, -138, -211,  279,    0,    0,    0,    0, -211,  304,
    0, -211,  -79, -160,  159,    0,    0, -160,    0,  243,
  438,  -56,    0,  313, -211,    0, -211, -211, -211, -211,
 -211,    0,    0, -211, -211, -211, -211, -211, -211, -211,
 -211, -211,  336,    0,  370,    0,  393,    0,  554, -211,
    0,    0,    0,    0, -160, -160, -160, -160, -160, -160,
  560, -211,    0,  425,  168,  -85, -125, -125,  449,  449,
  449, -211,   81, -179, -179, -245, -245, -245, -245,    0,
    0,  -78,    0,    0,    0,  173,  560, -160, -160, -283,
 -283,    0,    0,    0,    0,    0,  402,    0, -211,  179,
 -247,    0,  -58,  212,  249,  -73,  425, -217,    0,  -77,
 -247,    0,    0,    0,    0,  -72,    0,    0,    0,  -42,
  560,  -42,    0,
};
final static short yyrindex[] = {                         0,
   26,    0,    0,    0,   37,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -35,    0,   48,    0,  -55,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -29,
    0,    0,    0,    1,    0,    0,    0,  -19,  -35,    0,
    0,    0,    0,    0,    0,    0,  -29,   26,    0,    0,
    0,    0,    0,  615,   48,    0,    0,    0,  494,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,  517,  540,    0,
    0,    0,    0,    0,    0,  -31,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  563,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  206,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  206,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -39,    0,  207,  103,  272,  209,  216,
  264,    0,  194,  -41,  147,   49,   68,  119,  133,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -244,
 -214,    0,    0,    0,    0,    0,    0,    0,    0,  202,
  -40,    0,  -80, -250, -248,    0,  -37,    0,    0,    0,
  -40,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0, -189,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  203,  -14,  211,  108,  230,    0,  273,  247,
    0,  234,    0,    0,  101,  -64,    0,    0,  -59, -112,
  -65,    0,  -57,  129,  274,    0,    0,    0,    0,    0,
  208,    0,
};
final static int YYTABLESIZE=906;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         79,
    8,   68,   82,   67,   97,   29,   92,    2,   94,   88,
  162,   75,   92,   20,   93,  111,   76,  100,   95,   42,
  147,  148,  149,  150,   22,    6,  113,  114,   23,  177,
  178,  179,  116,   33,   34,  133,    8,  198,  139,   56,
   25,  135,  141,   27,  137,   30,   84,   25,   96,   92,
   85,   93,   93,   31,   86,   95,   95,   95,   95,  157,
  158,  159,  160,  161,   35,  199,  163,  164,  165,  166,
  167,  168,  169,  170,  171,  131,  132,   87,   88,  180,
  181,  182,  183,  184,  185,   96,   96,   96,   96,   81,
   39,  186,  204,  205,  187,   54,  211,   90,   40,   91,
   41,  104,   54,   43,  190,  105,   44,   84,   82,   15,
   45,   85,  194,  195,   47,   86,   52,  193,  117,  118,
  119,  120,  121,   54,  127,  128,  129,  130,  106,  107,
   53,  197,   32,  119,  120,  121,   48,   36,   87,   88,
   80,  131,  132,   70,   54,   89,   81,   97,  108,   59,
  109,  213,   49,   83,   36,   93,  101,  122,   90,   83,
   91,  123,  124,  125,  126,  127,  128,  129,  130,   99,
  102,  103,  112,   84,  123,  124,  125,  126,  127,  128,
  129,  130,  131,  132,  115,   37,  138,   80,    8,    9,
   10,   11,   12,   13,   14,  131,  132,   37,  140,   37,
   37,   37,   37,   37,   37,  152,   37,   37,  188,  189,
   37,   37,  191,  192,  207,   79,   79,   79,   79,   79,
  202,   79,  203,   59,  210,   88,   88,   88,   88,   88,
   21,   88,   37,   27,   78,   60,    8,   61,   62,   63,
   64,   65,   77,   26,   66,   67,   66,   65,   54,   72,
   51,   53,   79,   79,   79,   68,   74,   67,   79,   79,
   79,   79,   88,   88,   88,   58,    8,    8,   88,   88,
   88,   88,   88,   88,   88,   88,   50,    8,    8,   21,
    8,    8,    8,    8,    8,   46,   55,    8,    8,   88,
   88,    8,    8,    6,    6,    6,    6,    6,    6,    6,
    6,    6,    6,    8,   76,   81,   81,   81,   81,   81,
  212,   81,   71,    8,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   82,   82,   82,   82,   82,  208,
   82,    6,    7,    8,    9,   10,   11,   12,   13,   14,
   95,    0,   81,   81,   81,    0,    0,  176,   81,   81,
   81,   81,   81,   81,   81,   81,    0,    0,    0,   70,
   70,   82,   82,   82,    0,   70,    0,   82,   82,   82,
   82,   82,   82,   82,   82,   83,   83,   83,   83,   83,
    0,   83,  125,  126,  127,  128,  129,  130,    0,   84,
   84,   84,   84,   84,    0,   84,   70,   70,   70,    0,
    0,  131,  132,   80,   80,   80,   80,   80,    0,   80,
    0,    0,   83,   83,   83,    0,    0,    0,   83,   83,
   83,   83,   83,   83,   83,   83,   84,   84,   84,    0,
    0,    0,   84,   84,   84,   84,   84,   84,   84,   84,
   80,   80,   80,    0,    0,    0,   80,   80,   80,   80,
   78,   78,   78,   78,   78,    0,   78,    0,   77,   77,
   77,   77,   77,    0,   77,   72,   72,   72,   72,    0,
    0,   72,   74,   74,   74,   74,    0,    0,   74,  124,
  125,  126,  127,  128,  129,  130,    0,   78,   78,   78,
    0,    0,    0,   78,   78,   77,   77,   77,    0,  131,
  132,   77,   72,   72,   72,  142,    0,    0,    0,   74,
   74,   74,  144,  145,  146,  147,  148,  149,  150,    0,
   76,   76,   76,   76,    0,    0,   76,    0,   71,   71,
    0,    0,    0,    0,   71,  117,  118,  119,  120,  121,
    0,    0,  143,  144,  145,  146,  147,  148,  149,  150,
  145,  146,  147,  148,  149,  150,    0,   76,   76,   76,
  117,  118,  119,  120,  121,   71,   71,   71,    0,  117,
  118,  119,  120,  121,  134,  153,    0,    0,  123,  124,
  125,  126,  127,  128,  129,  130,    0,    0,    0,    0,
    0,    0,  117,  118,  119,  120,  121,  136,  172,  131,
  132,    0,    0,  123,  124,  125,  126,  127,  128,  129,
  130,    0,  123,  124,  125,  126,  127,  128,  129,  130,
    0,    0,    0,    0,  131,  132,  117,  118,  119,  120,
  121,    0,    0,  131,  132,  123,  124,  125,  126,  127,
  128,  129,  130,    0,    0,    0,    0,    0,    0,  117,
  118,  119,  120,  121,    0,    0,  131,  132,  117,  118,
  119,  120,  121,    0,  196,  173,    0,    0,    0,  123,
  124,  125,  126,  127,  128,  129,  130,    0,    0,    0,
    0,  117,  118,  119,  120,  121,  174,    0,    0,    0,
  131,  132,  123,  124,  125,  126,  127,  128,  129,  130,
  151,  123,  124,  125,  126,  127,  128,  129,  130,  121,
    0,    0,    0,  131,  132,    0,    0,    0,    0,    0,
    0,    0,  131,  132,  123,  124,  125,  126,  127,  128,
  129,  130,    0,    0,    0,    0,    0,  143,  144,  145,
  146,  147,  148,  149,  150,  131,  132,    0,  123,  124,
  125,  126,  127,  128,  129,  130,    0,    0,    0,   58,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  131,
  132,   58,   58,   58,   58,   58,   58,   58,   58,    0,
   58,   58,   59,    0,   58,   58,    0,    0,    0,    0,
    0,    0,   58,    0,   59,   59,   59,   59,   59,   59,
   59,   59,    0,   59,   59,   60,   58,   59,   59,    0,
    0,    0,    0,    0,    0,   59,  175,   60,   60,   60,
   60,   60,   60,   60,   60,  102,   60,   60,    0,   59,
   60,   60,    0,    0,    0,    0,    0,   60,   60,   61,
   62,   63,   64,   65,    0,    0,   66,   67,    0,    0,
   54,    0,   60,  143,  144,  145,  146,  147,  148,  149,
  150,   59,  102,  102,  102,  102,  102,  102,  102,  102,
    0,    0,    0,   60,    0,   61,   62,   63,   64,   65,
    8,    0,   66,   67,    0,    0,   54,   96,    0,    0,
    0,    0,    8,    0,    8,    8,    8,    8,    8,    0,
    0,    8,    8,    0,    0,    8,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   41,   62,   41,   69,   20,   64,  267,   66,   41,
  123,  293,  263,  291,  263,   81,  298,   75,  263,   34,
  304,  305,  306,  307,  266,    0,   84,   85,  293,  142,
  143,  144,   90,  295,  296,   93,    0,  285,  104,   54,
  277,   99,  108,  266,  102,  262,  258,    0,  263,  300,
  262,  300,  301,  311,  266,  300,  301,  302,  303,  117,
  118,  119,  120,  121,  292,  313,  124,  125,  126,  127,
  128,  129,  130,  131,  132,  321,  322,  289,  290,  145,
  146,  147,  148,  149,  150,  300,  301,  302,  303,   41,
  294,  151,  310,  311,  152,  285,  209,  309,  266,  311,
  266,  262,  292,  266,  162,  266,  263,  258,   41,    2,
  295,  262,  178,  179,  262,  266,  266,  177,  257,  258,
  259,  260,  261,  313,  304,  305,  306,  307,  289,  290,
  263,  189,   25,  259,  260,  261,  296,   30,  289,  290,
  262,  321,  322,   41,  291,  296,  262,  212,  309,  266,
  311,  211,   45,  296,   47,  262,  266,  296,  309,   41,
  311,  300,  301,  302,  303,  304,  305,  306,  307,  299,
  293,  298,  280,   41,  300,  301,  302,  303,  304,  305,
  306,  307,  321,  322,   40,  266,  266,   41,  270,  271,
  272,  273,  274,  275,  276,  321,  322,  278,   40,  280,
  281,  282,  283,  284,  285,  262,  287,  288,   41,  295,
  291,  292,  291,   41,  292,  257,  258,  259,  260,  261,
  279,  263,  296,  266,  297,  257,  258,  259,  260,  261,
  266,  263,  313,  263,   41,  278,  292,  280,  281,  282,
  283,  284,   41,  263,  287,  288,   41,   41,  291,   41,
   48,  292,  294,  295,  296,  295,   41,  295,  300,  301,
  302,  303,  294,  295,  296,   55,  266,  267,  300,  301,
  302,  303,  304,  305,  306,  307,   47,  277,  278,    7,
  280,  281,  282,  283,  284,   39,   53,  287,  288,  321,
  322,  291,  292,  268,  269,  270,  271,  272,  273,  274,
  275,  276,  277,  267,   41,  257,  258,  259,  260,  261,
  210,  263,   41,  277,  267,  268,  269,  270,  271,  272,
  273,  274,  275,  276,  257,  258,  259,  260,  261,  201,
  263,  268,  269,  270,  271,  272,  273,  274,  275,  276,
   67,   -1,  294,  295,  296,   -1,   -1,  140,  300,  301,
  302,  303,  304,  305,  306,  307,   -1,   -1,   -1,  257,
  258,  294,  295,  296,   -1,  263,   -1,  300,  301,  302,
  303,  304,  305,  306,  307,  257,  258,  259,  260,  261,
   -1,  263,  302,  303,  304,  305,  306,  307,   -1,  257,
  258,  259,  260,  261,   -1,  263,  294,  295,  296,   -1,
   -1,  321,  322,  257,  258,  259,  260,  261,   -1,  263,
   -1,   -1,  294,  295,  296,   -1,   -1,   -1,  300,  301,
  302,  303,  304,  305,  306,  307,  294,  295,  296,   -1,
   -1,   -1,  300,  301,  302,  303,  304,  305,  306,  307,
  294,  295,  296,   -1,   -1,   -1,  300,  301,  302,  303,
  257,  258,  259,  260,  261,   -1,  263,   -1,  257,  258,
  259,  260,  261,   -1,  263,  257,  258,  259,  260,   -1,
   -1,  263,  257,  258,  259,  260,   -1,   -1,  263,  301,
  302,  303,  304,  305,  306,  307,   -1,  294,  295,  296,
   -1,   -1,   -1,  300,  301,  294,  295,  296,   -1,  321,
  322,  300,  294,  295,  296,  263,   -1,   -1,   -1,  294,
  295,  296,  301,  302,  303,  304,  305,  306,  307,   -1,
  257,  258,  259,  260,   -1,   -1,  263,   -1,  257,  258,
   -1,   -1,   -1,   -1,  263,  257,  258,  259,  260,  261,
   -1,   -1,  300,  301,  302,  303,  304,  305,  306,  307,
  302,  303,  304,  305,  306,  307,   -1,  294,  295,  296,
  257,  258,  259,  260,  261,  294,  295,  296,   -1,  257,
  258,  259,  260,  261,  296,  263,   -1,   -1,  300,  301,
  302,  303,  304,  305,  306,  307,   -1,   -1,   -1,   -1,
   -1,   -1,  257,  258,  259,  260,  261,  294,  263,  321,
  322,   -1,   -1,  300,  301,  302,  303,  304,  305,  306,
  307,   -1,  300,  301,  302,  303,  304,  305,  306,  307,
   -1,   -1,   -1,   -1,  321,  322,  257,  258,  259,  260,
  261,   -1,   -1,  321,  322,  300,  301,  302,  303,  304,
  305,  306,  307,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,  259,  260,  261,   -1,   -1,  321,  322,  257,  258,
  259,  260,  261,   -1,  263,  296,   -1,   -1,   -1,  300,
  301,  302,  303,  304,  305,  306,  307,   -1,   -1,   -1,
   -1,  257,  258,  259,  260,  261,  294,   -1,   -1,   -1,
  321,  322,  300,  301,  302,  303,  304,  305,  306,  307,
  263,  300,  301,  302,  303,  304,  305,  306,  307,  261,
   -1,   -1,   -1,  321,  322,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,  321,  322,  300,  301,  302,  303,  304,  305,
  306,  307,   -1,   -1,   -1,   -1,   -1,  300,  301,  302,
  303,  304,  305,  306,  307,  321,  322,   -1,  300,  301,
  302,  303,  304,  305,  306,  307,   -1,   -1,   -1,  266,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  321,
  322,  278,  279,  280,  281,  282,  283,  284,  285,   -1,
  287,  288,  266,   -1,  291,  292,   -1,   -1,   -1,   -1,
   -1,   -1,  299,   -1,  278,  279,  280,  281,  282,  283,
  284,  285,   -1,  287,  288,  266,  313,  291,  292,   -1,
   -1,   -1,   -1,   -1,   -1,  299,  263,  278,  279,  280,
  281,  282,  283,  284,  285,  263,  287,  288,   -1,  313,
  291,  292,   -1,   -1,   -1,   -1,   -1,  278,  299,  280,
  281,  282,  283,  284,   -1,   -1,  287,  288,   -1,   -1,
  291,   -1,  313,  300,  301,  302,  303,  304,  305,  306,
  307,  266,  300,  301,  302,  303,  304,  305,  306,  307,
   -1,   -1,   -1,  278,   -1,  280,  281,  282,  283,  284,
  266,   -1,  287,  288,   -1,   -1,  291,  292,   -1,   -1,
   -1,   -1,  278,   -1,  280,  281,  282,  283,  284,   -1,
   -1,  287,  288,   -1,   -1,  291,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=322;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'",null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,"SUMA","RESTA","MULTI","DIVI","POTE","LPAR","RPAR","NUMERO",
"SALTOLINEA","ID","PROTO","STRUCT","PTR","INT","FLOAT","DOUBLE","COMPLEX",
"RUNE","VOID","STRING","FUNC","IF","ELSE","WHILE","DO","BREAK","RETURN",
"SWITCH","CASE","DEFAULT","PRINT","SCAN","TRUE","FALSE","LLLAVE","RLLAVE",
"LCORCHETE","RCORCHETE","COMA","SEMICOLON","COLON","PUNTO","ASIG","OR","AND",
"IGUAL","DESIGUAL","MENORQUE","MENORIGUAL","MAYORQUE","MAYORIGUAL",
"DIVISIONENTERA","NEG","LITERAL_RUNA","LITERAL_ENTERA","LITERAL_CADENA","PRED",
"LITERAL_COMPLEJA","LITERAL_FLOTANTE","LITERAL_DOBLE","MAS","MENOS","MULT",
"DIV","MOD","DIVENTERA",
};
final static String yyrule[] = {
"$accept : input",
"input :",
"input : input line",
"line : programa",
"programa : decl_proto decl_var decl_func",
"decl_proto : PROTO tipo ID LPAR argumentos RPAR SEMICOLON decl_proto",
"decl_proto :",
"decl_var : tipo lista_var SEMICOLON decl_var",
"decl_var :",
"tipo : basico compuesto",
"tipo : STRUCT LLLAVE decl_var RLLAVE",
"tipo : puntero",
"puntero : PTR basico",
"basico : INT",
"basico : FLOAT",
"basico : DOUBLE",
"basico : COMPLEX",
"basico : RUNE",
"basico : VOID",
"basico : STRING",
"compuesto : LCORCHETE LITERAL_ENTERA RCORCHETE compuesto",
"compuesto :",
"lista_var : lista_var COMA ID",
"lista_var : ID",
"decl_func : FUNC tipo ID LPAR argumentos RPAR bloque decl_func",
"decl_func :",
"argumentos : lista_args",
"argumentos :",
"lista_args : lista_args COMA tipo ID",
"lista_args : tipo ID",
"bloque : LLLAVE declaraciones instrucciones RLLAVE",
"declaraciones : decl_var",
"instrucciones : instrucciones sentencia",
"instrucciones : sentencia",
"sentencia : asignacion",
"sentencia : resolve",
"resolve : matched_stmt M",
"matched_stmt : IF LPAR exp1 RPAR M matched_stmt",
"matched_stmt : IF LPAR exp1 RPAR M matched_stmt ELSE N M matched_stmt",
"matched_stmt : WHILE LPAR exp1 RPAR matched_stmt",
"matched_stmt : DO matched_stmt WHILE LPAR exp RPAR SEMICOLON",
"matched_stmt : BREAK SEMICOLON",
"matched_stmt : bloque",
"matched_stmt : RETURN exp SEMICOLON",
"matched_stmt : RETURN SEMICOLON",
"matched_stmt : SWITCH LPAR exp RPAR LLLAVE casos RLLAVE",
"matched_stmt : PRINT exp SEMICOLON",
"matched_stmt : SCAN parte_izquierda",
"M :",
"N :",
"asignacion : parte_izquierda ASIG exp SEMICOLON",
"casos : caso casos",
"casos : PRED",
"casos :",
"caso : CASE opcion COLON instrucciones",
"opcion : LITERAL_ENTERA",
"opcion : LITERAL_RUNA",
"parte_izquierda : ID localizacion",
"parte_izquierda : ID",
"localizacion : arreglo",
"localizacion : estructurado",
"arreglo : arreglo LCORCHETE exp RCORCHETE",
"arreglo : LCORCHETE exp RCORCHETE",
"estructurado : estructurado PUNTO ID",
"estructurado : PUNTO ID",
"parametros : lista_param",
"parametros :",
"lista_param : lista_param COMA exp",
"lista_param : exp",
"exp : LITERAL_ENTERA",
"exp : exp SUMA exp",
"exp : exp RESTA exp",
"exp : exp MULTI exp",
"exp : LPAR exp RPAR",
"exp : exp DIVI exp",
"exp : RESTA exp",
"exp : exp POTE exp",
"exp : exp OR M exp",
"exp : exp AND exp",
"exp : exp IGUAL exp",
"exp : exp DESIGUAL exp",
"exp : exp MENORQUE exp",
"exp : exp MENORIGUAL exp",
"exp : exp MAYORQUE exp",
"exp : exp MAYORIGUAL exp",
"exp : exp MOD exp",
"exp : exp DIVENTERA exp",
"exp : NEG exp",
"exp : ID",
"exp : ID '(' parametros ')'",
"exp : TRUE",
"exp : FALSE",
"exp1 : exp1 OR M exp1",
"exp1 : exp1 AND M exp1",
"exp1 : LPAR exp1 RPAR",
"exp1 : exp1 IGUAL exp1",
"exp1 : exp1 DESIGUAL exp1",
"exp1 : exp1 MENORQUE exp1",
"exp1 : exp1 MENORIGUAL exp1",
"exp1 : exp1 MAYORQUE exp1",
"exp1 : exp1 MAYORIGUAL exp1",
"exp1 : NEG exp1",
"exp1 : ID",
"exp1 : ID '(' parametros ')'",
"exp1 : TRUE",
"exp1 : FALSE",
"exp1 : LITERAL_ENTERA",
};

//#line 554 "parserProyecto.y"



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
  } catch (IOException e) {
    System.err.println("Error ");  
  }
 
  return token;  // Retorna el token obtenido
}


//#line 672 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 4:
//#line 66 "parserProyecto.y"
{ 
                                        parserActions.printGeneratedCode();
                                        System.out.println("\n\n\n");

                                        tablaSimbolos.printTable();
                                        System.out.println("\n\n\n");
                                        Traductor traductor = new Traductor(parserActions.listaCuadruplas());
                                        traductor.crear(tablaSimbolos);
                                     }
break;
case 7:
//#line 88 "parserProyecto.y"
{
        /*Aqui se declaran las variables y se guardan en la tabla de datos*/
        /*Aqui tengo que recorrer el $2sval y separar en comas para guardarlos en la tabla de simbolos*/
        String[] elementos = val_peek(2).sval.split(",");
        for (String elemento : elementos) {
          tablaSimbolos.addSymbol(elemento,val_peek(3).sval,elemento);  
        }
       
      }
break;
case 9:
//#line 104 "parserProyecto.y"
{yyval=val_peek(1);}
break;
case 13:
//#line 114 "parserProyecto.y"
{yyval=new ParserVal("int");}
break;
case 14:
//#line 116 "parserProyecto.y"
{yyval=new ParserVal("float");}
break;
case 15:
//#line 118 "parserProyecto.y"
{yyval=new ParserVal("double");}
break;
case 22:
//#line 133 "parserProyecto.y"
{
      /*Genera ID  recursivamente */
      yyval = new ParserVal (val_peek(2).sval +","+ val_peek(0).sval);
    
    }
break;
case 23:
//#line 140 "parserProyecto.y"
{ yyval=val_peek(0); }
break;
case 36:
//#line 181 "parserProyecto.y"
{parserActions.backpatch((List<Integer>) val_peek(1).obj  , val_peek(0).ival , "para la N" ) ;}
break;
case 37:
//#line 187 "parserProyecto.y"
{ 
                /*Para mandar informacion a traves de parserVal creo un Mapa   */
                Context contexto = (Context) val_peek(3).obj;
            
                /*obtengo la lista de direcciones donde falta poner la bandera */
                List<Integer> l1= contexto.getVariable("B.truelist");
                List<Integer> l2= contexto.getVariable("B.falselist");

                /*Se resuelve las banderas faltantes */
                parserActions.backpatch(l1,val_peek(1).ival, "B.truelist" );
                /*Se regresan las banderas que apuntan a la siguiente instruccion (despues de if)*/
                yyval = new ParserVal(l2);
                

          }
break;
case 38:
//#line 206 "parserProyecto.y"
{  Context contexto = (Context) val_peek(7).obj;
               
                /*Obtenemos direcciones*/
                List<Integer> l1= contexto.getVariable("B.truelist");
                List<Integer> l2= contexto.getVariable("B.falselist");

                /*resuelve las banderas con las direcciones y con los placeholders M y N*/
                parserActions.backpatch(l1,val_peek(5).ival ,"Btruelist1");
                parserActions.backpatch(l2,val_peek(1).ival,"Bfalselist2");

                yyval = new  ParserVal( val_peek(2).obj);
          }
break;
case 48:
//#line 232 "parserProyecto.y"
{
            /*Regresa la ubicacion de la instruccion siguiente*/
            int x =  parserActions.nextinstr();
            yyval = new ParserVal(x);
            
            }
break;
case 49:
//#line 241 "parserProyecto.y"
{ 
              /*Va a regresar la ubicacion de la instruccion siguiente*/
              yyval = new ParserVal( parserActions.makelist(parserActions.nextinstr()) );
              /*imprime un salto para que se salte el else*/
              parserActions.handleGoto();
            
             }
break;
case 50:
//#line 255 "parserProyecto.y"
{
        
       /*Verifica que exista el simbolo es decir que haya sido declarado primero */
       SymbolTable.Symbol simbolo = tablaSimbolos.getSymbol(val_peek(3).sval);
        if(simbolo != null){
          
          parserActions.handleAsignacion(val_peek(3) ,val_peek(1) );
        }         
        
        else{
          System.out.println("Simbolo no existe");
        }
       
      }
break;
case 58:
//#line 284 "parserProyecto.y"
{  yyval=val_peek(0);}
break;
case 69:
//#line 310 "parserProyecto.y"
{
         /* Si es un número, simplemente lo retornamos.*/
         /* lo regresamos como String , ya que el objetivo no es hacer la operacion aqui.*/
        yyval = new ParserVal ( Double.toString( val_peek(0).dval));

    }
break;
case 70:
//#line 319 "parserProyecto.y"
{   yyval = new ParserVal (parserActions.handleAddition(val_peek(2), val_peek(0))); }
break;
case 71:
//#line 321 "parserProyecto.y"
{   yyval = new ParserVal (parserActions.handleSubtraction(val_peek(2), val_peek(0)));   }
break;
case 72:
//#line 323 "parserProyecto.y"
{  yyval = new ParserVal (parserActions.handleMultiplication(val_peek(2), val_peek(0)));     }
break;
case 73:
//#line 326 "parserProyecto.y"
{ yyval =val_peek(1); }
break;
case 74:
//#line 328 "parserProyecto.y"
{yyval = new ParserVal (parserActions.handleDivi(val_peek(2), val_peek(0)));}
break;
case 75:
//#line 331 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleNEGATIVO(val_peek(0))); }
break;
case 76:
//#line 332 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handlePOTE(val_peek(2), val_peek(0))); }
break;
case 77:
//#line 335 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleOR(val_peek(3), val_peek(1))); }
break;
case 78:
//#line 336 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleAND(val_peek(2), val_peek(0))); }
break;
case 79:
//#line 337 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleIGUAL(val_peek(2), val_peek(0))); }
break;
case 80:
//#line 338 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleDESIGUAL(val_peek(2), val_peek(0))); }
break;
case 81:
//#line 339 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMENORQUE(val_peek(2), val_peek(0))); }
break;
case 82:
//#line 340 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMENORIGUAL(val_peek(2), val_peek(0))); }
break;
case 83:
//#line 341 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMAYORQUE(val_peek(2), val_peek(0))); }
break;
case 84:
//#line 342 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMAYORIGUAL(val_peek(2), val_peek(0))); }
break;
case 85:
//#line 343 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMOD(val_peek(2), val_peek(0))); }
break;
case 86:
//#line 344 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleDIVENTERA(val_peek(2), val_peek(0))); }
break;
case 87:
//#line 345 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleNEG(val_peek(0))); }
break;
case 88:
//#line 348 "parserProyecto.y"
{  yyval=val_peek(0);  }
break;
case 90:
//#line 350 "parserProyecto.y"
{ yyval= new ParserVal("true");  }
break;
case 91:
//#line 352 "parserProyecto.y"
{ yyval= new ParserVal("false"); }
break;
case 92:
//#line 380 "parserProyecto.y"
{ 
            Context contexto = (Context) val_peek(3).obj;
            List<Integer> B1truelist= contexto.getVariable("B.truelist");
            List<Integer> B1falselist= contexto.getVariable("B.falselist");

            Context contexto2 = (Context) val_peek(0).obj;
            List<Integer> B2truelist= contexto2.getVariable("B.truelist");
            List<Integer> B2falselist= contexto2.getVariable("B.falselist");

            parserActions.backpatch(B1falselist,val_peek(1).ival,"B1falselist");
            Context contexto3= new Context();
            contexto3.setVariable ("B.truelist", parserActions.merge(B1truelist,B2truelist));
            contexto3.setVariable ("B.falselist",B2falselist);
            yyval = new ParserVal(contexto3);

            }
break;
case 93:
//#line 396 "parserProyecto.y"
{ 
            Context contexto = (Context) val_peek(3).obj;
            List<Integer> B1truelist= contexto.getVariable("B.truelist");
            List<Integer> B1falselist= contexto.getVariable("B.falselist");

            Context contexto2 = (Context) val_peek(0).obj;
            List<Integer> B2truelist= contexto2.getVariable("B.truelist");
            List<Integer> B2falselist= contexto2.getVariable("B.falselist");

            parserActions.backpatch(B1truelist,val_peek(1).ival,"B1truelist");
            
            Context contexto3= new Context();
            contexto3.setVariable ("B.truelist",B2truelist );

            contexto3.setVariable ("B.falselist",parserActions.merge(B1falselist,B2falselist));
            yyval = new ParserVal(contexto3);

            }
break;
case 94:
//#line 415 "parserProyecto.y"
{ 
            Context contexto = (Context) val_peek(1).obj;
            List<Integer> B1truelist= contexto.getVariable("B.truelist");
            List<Integer> B1falselist= contexto.getVariable("B.falselist");
  
            Context contexto3= new Context();
            contexto3.setVariable ("B.truelist",B1truelist );
            contexto3.setVariable ("B.falselist",B1falselist);
            yyval = new ParserVal(contexto3);

            }
break;
case 95:
//#line 427 "parserProyecto.y"
{ 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ val_peek(2).sval + " == " + val_peek(0).sval );
                parserActions.handleGoto();

                yyval = new ParserVal(context);
                
            }
break;
case 96:
//#line 439 "parserProyecto.y"
{ 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ val_peek(2).sval + " != " + val_peek(0).sval );
                parserActions.handleGoto();

                yyval = new ParserVal(context);
                
            }
break;
case 97:
//#line 451 "parserProyecto.y"
{ 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ val_peek(2).sval + " < " + val_peek(0).sval );
                parserActions.handleGoto();

                yyval = new ParserVal(context);
                
            }
break;
case 98:
//#line 463 "parserProyecto.y"
{ 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ val_peek(2).sval + " <= " + val_peek(0).sval );
                parserActions.handleGoto();

                yyval = new ParserVal(context);
                
            }
break;
case 99:
//#line 475 "parserProyecto.y"
{ 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ val_peek(2).sval + " > " + val_peek(0).sval );
                parserActions.handleGoto();

                yyval = new ParserVal(context);
                
            }
break;
case 100:
//#line 487 "parserProyecto.y"
{ 
                Context context = new Context();
                List<Integer> x  =parserActions.makelist(parserActions.nextinstr());
                context.setVariable("B.truelist",x);
                List<Integer> y  =parserActions.makelist(parserActions.nextinstr()+1);
                context.setVariable("B.falselist",y);
                parserActions.handleIfGoto (""+ val_peek(2).sval + " >= " + val_peek(0).sval );
                parserActions.handleGoto();

                yyval = new ParserVal(context);
                
            }
break;
case 101:
//#line 499 "parserProyecto.y"
{ 
                  Context contexto = (Context) val_peek(0).obj;
                  List<Integer> B1truelist= contexto.getVariable("B.truelist");
                  List<Integer> B1falselist= contexto.getVariable("B.falselist");
        
                  Context contexto3= new Context();
                  contexto3.setVariable ("B.truelist",B1falselist );
                  contexto3.setVariable ("B.falselist",B1truelist);
                  yyval = new ParserVal(contexto3);

                  }
break;
case 102:
//#line 512 "parserProyecto.y"
{
                yyval=val_peek(0);
               
              }
break;
case 104:
//#line 517 "parserProyecto.y"
{

                  Context contexto = new Context(); 
                  contexto.setVariable("B.truelist",parserActions.makelist(parserActions.nextinstr()));
                  contexto.setVariable("B.falselist", new ArrayList<Integer>() ); 
                    
                  parserActions.handleGoto();

                  yyval= new ParserVal(contexto);
                  }
break;
case 105:
//#line 530 "parserProyecto.y"
{ 

                  Context contexto = new Context(); 
                  contexto.setVariable("B.truelist", new ArrayList<Integer>() );
                  contexto.setVariable("B.falselist",parserActions.makelist(parserActions.nextinstr()) ); 

                  parserActions.handleGoto();

                  yyval= new ParserVal(contexto);
                  }
break;
case 106:
//#line 542 "parserProyecto.y"
{
                      /* Si es un número, simplemente lo retornamos.*/
                      yyval = new ParserVal ( Double.toString( val_peek(0).dval));

                  }
break;
//#line 1242 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
