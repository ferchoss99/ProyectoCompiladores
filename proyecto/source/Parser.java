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

  import utils.Context;







//#line 37 "Parser.java"




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
   10,    8,    8,   12,    5,    5,    7,    7,   14,   14,
   13,   15,   16,   16,   17,   20,   17,   19,   21,   21,
   21,   21,   21,   21,   21,   21,   21,   21,   21,   22,
   24,   18,   26,   26,   26,   28,   29,   29,   27,   27,
   30,   30,   31,   31,   32,   32,   33,   33,   34,   34,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   25,   25,   25,   25,   25,   25,   25,
   25,   25,   25,   23,   23,   23,   23,   23,   23,   23,
   23,   23,   23,   23,   23,   23,   23,   23,
};
final static short yylen[] = {                            2,
    0,    2,    1,    3,    8,    0,    4,    0,    2,    4,
    1,    2,    1,    1,    1,    1,    1,    1,    1,    4,
    0,    3,    1,    0,    9,    0,    1,    0,    4,    2,
    4,    1,    2,    1,    1,    0,    2,    2,    6,   10,
    7,    7,    2,    1,    3,    2,    7,    3,    2,    0,
    0,    4,    2,    1,    0,    4,    1,    1,    2,    1,
    1,    1,    4,    3,    3,    2,    1,    0,    3,    1,
    1,    3,    3,    3,    3,    3,    2,    3,    4,    3,
    3,    3,    3,    3,    3,    3,    3,    3,    2,    1,
    4,    1,    1,    4,    4,    3,    3,    3,    3,    3,
    3,    3,    2,    1,    4,    1,    1,    1,
};
final static short yydefred[] = {                         1,
    0,    0,    2,    3,    0,    0,    0,   13,   14,   15,
   16,   17,   18,   19,    0,    0,   11,    0,    0,    0,
   12,    0,    0,    9,    4,    0,   23,    0,    0,    0,
    0,    0,    0,    0,   10,    0,    0,    0,    0,    0,
   22,    7,   30,    0,    0,   20,    0,    0,    0,    0,
    5,   29,    0,    0,    0,    0,   32,    0,   25,    0,
    0,   34,   35,    0,    0,    0,    0,   59,    0,    0,
   31,   33,    0,    0,    0,    0,    0,    0,    0,    0,
   44,   37,   50,    0,    0,    0,    0,   92,   93,    0,
   71,    0,   66,    0,    0,    0,   50,    0,   43,   46,
    0,    0,    0,   49,   38,    0,   77,    0,    0,   89,
    0,    0,    0,    0,    0,   64,   50,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   65,    0,    0,
  106,  107,    0,  108,    0,    0,    0,   45,    0,   48,
   52,   75,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   87,   88,
   63,    0,    0,  103,   50,   50,   50,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   91,    0,    0,   96,
    0,    0,    0,    0,    0,    0,   99,  100,  101,  102,
   50,    0,    0,    0,  105,    0,    0,    0,    0,    0,
    0,   54,    0,    0,   51,   41,   42,   58,   57,    0,
   47,   53,   50,    0,    0,    0,   40,
};
final static short yydgoto[] = {                          1,
    3,    4,    5,   18,   25,   19,   37,   28,   16,   24,
   17,   26,   81,   38,   58,   61,   62,   63,   82,   64,
   83,  105,  135,  213,  143,  203,   65,  204,  210,   68,
   69,   70,  144,  145,
};
final static short yysindex[] = {                         0,
 -244,  472,    0,    0,  472, -275,  242,    0,    0,    0,
    0,    0,    0,    0, -233, -255,    0,    0, -230,  472,
    0, -222, -270,    0,    0, -234,    0, -281, -236,  472,
 -227,  472, -186,  472,    0, -185, -178, -211, -255, -180,
    0,    0,    0, -208,  472,    0, -170, -244, -173,  472,
    0,    0, -164, -190,  472,    0,    0, -158,    0, -280,
 -257,    0,    0,  636, -183,  -75, -153,    0, -168, -175,
    0,    0, -136, -135,  636, -161, -179, -129,  -75, -158,
    0,    0,    0,  -75,  -75,  -75,   96,    0,    0,  -75,
    0, -163,    0,  -75, -112,  140,    0, -125,    0,    0,
  265,  -75,  292,    0,    0,  323,    0, -154,  -75,    0,
  -75,  -75,  -75,  -75,  -75,    0,    0,  -75,  -75,  -75,
  -75,  -75,  -75,  -75,  -75,  -75,  346,    0,  140,  116,
    0,    0,  140,    0,  -63,  140, -102,    0,  374,    0,
    0,    0,  405,  122, -130,  428,  428, -232, -232, -232,
  -75,  604,  416,  416, -300, -300, -300, -300,    0,    0,
    0,  533,  -75,    0,    0,    0,    0,  140,  140,  140,
  140,  140,  140,  553,  -75, -121,    0,  -75,   36,    0,
  125,  636,  140,  140, -100, -100,    0,    0,    0,    0,
    0,  397, -274,  405,    0, -108,  253,  464,  636, -124,
 -279,    0, -118, -274,    0,    0,    0,    0,    0, -119,
    0,    0,    0, -158,  636, -158,    0,
};
final static short yyrindex[] = {                         0,
   37,    0,    0,    0,   48,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -86,    0,   27,    0, -111,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  -79,
    0,    0,    0,    1,    0,    0,    0,  -78,  -86,    0,
    0,    0,    0,    0,    0,    0,    0,   37,    0,  -79,
    0,    0,    0,    0,  612,   27,    0,  650,    0,  473,
  650,    0,    0,    0,    0,    0,    0,    0,  496,  519,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,  -31,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  145,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,  562,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,  -38,    0,  147,  280,  283,  215,  225,  236,
    0,  162,  -41,  152,   59,   73,   87,  138,    0,    0,
    0,    0,  145,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,  207,    0,
    0,    0,    0,    0, -256, -181,    0,    0,    0,    0,
    0,    0, -103,  -37,    0,  539, -243, -251,    0,    0,
    0,    0,    0, -103,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  650,    0,  592,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  142,  141,  136,  132,  144,    0,  188,  158,
    0,    0,  148,    0,    0,  -16,  -59,    0,    0,    0,
  -70,  -89,   40,    0,  -60,   -5,  121,    0,    0,    0,
    0,    0,   58,    0,
};
final static int YYTABLESIZE=941;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         81,
    8,   72,   70,   69,   98,   92,   97,  136,   60,   90,
  201,   95,   66,   33,   34,   20,  101,   67,  103,   94,
  125,  126,    2,  106,  107,  108,   26,  151,  115,  110,
  208,  209,   22,  127,   71,   27,    6,   23,  202,   30,
   31,  139,   32,   97,   97,   97,   97,    8,   95,   95,
  146,  147,  148,  149,  150,   35,   94,  152,  153,  154,
  155,  156,  157,  158,  159,  160,   39,  117,  118,  119,
  120,  121,  122,  123,  124,  182,  183,  184,   85,   41,
   43,   98,   86,   45,   44,   47,   87,   48,  125,  126,
  179,   50,   52,  111,  112,  113,  114,  115,   54,   83,
   55,  199,  111,  112,  113,  114,  115,   60,  142,   88,
   89,  196,   93,   84,  192,   84,  100,  194,   98,   98,
   98,   98,   95,  215,   94,   96,   97,   85,  206,   90,
  116,   91,  102,   15,   99,  109,  117,  118,  119,  120,
  121,  122,  123,  124,  217,  117,  118,  119,  120,  121,
  122,  123,  124,  128,  137,  163,   72,  125,  126,  175,
   29,   36,  177,   40,  178,  195,  125,  126,  162,  193,
  205,  207,  164,  211,   42,  174,   49,  214,   86,   21,
    8,   36,   85,   28,   27,   68,   86,   67,   55,   51,
   87,   59,   82,   53,   21,   57,   46,  216,  212,  165,
  104,   56,   80,  170,  171,  172,  173,  185,  186,  187,
  188,  189,  190,   88,   89,   81,   81,   81,   81,   81,
  181,   81,  197,  198,    0,   90,   90,   90,   90,   90,
    0,   90,    0,   90,    0,   91,  166,  167,  168,  169,
  170,  171,  172,  173,    0,    0,    0,   79,    0,    0,
    0,    0,   81,   81,   81,   74,   70,   69,   81,   81,
   81,   81,   90,   90,   90,   76,    8,    8,   90,   90,
   90,   90,   90,   90,   90,   90,   78,    8,    8,    0,
    8,    8,    8,    8,    8,    0,    0,    8,    8,   90,
   90,    8,    8,   26,   26,   26,   26,   26,   26,   26,
   26,   26,   26,   24,    6,    6,    6,    6,    6,    6,
    6,    6,    6,    6,    8,   83,   83,   83,   83,   83,
   72,   83,    0,   73,    8,    0,    0,    0,    0,   84,
   84,   84,   84,   84,    0,   84,  118,  119,  120,  121,
  122,  123,  124,   85,   85,   85,   85,   85,    0,   85,
    0,    0,   83,   83,   83,    0,  125,  126,   83,   83,
   83,   83,   83,   83,   83,   83,   84,   84,   84,    0,
    0,    0,   84,   84,   84,   84,   84,   84,   84,   84,
   85,   85,   85,    0,    0,    0,   85,   85,   85,   85,
   85,   85,   85,   85,   86,   86,   86,   86,   86,    0,
   86,  129,    0,    0,    0,  130,    0,    0,   82,   82,
   82,   82,   82,    0,   82,    0,    0,    0,   80,   80,
   80,   80,   80,    0,   80,    0,    0,    0,  131,  132,
    0,   86,   86,   86,    0,    0,    0,   86,   86,   86,
   86,   86,   86,   86,   86,   82,   82,   82,  133,    0,
  134,   82,   82,   82,   82,   80,   80,   80,    0,    0,
    0,   80,   80,   79,   79,   79,   79,   79,    0,   79,
    0,   74,   74,   74,   74,    0,    0,   74,    0,    0,
    0,   76,   76,   76,   76,    0,    0,   76,    0,    0,
    0,    0,   78,   78,   78,   78,    0,    0,   78,    0,
   79,   79,   79,    0,    0,    0,   79,    0,   74,   74,
   74,    8,    9,   10,   11,   12,   13,   14,   76,   76,
   76,  111,  112,  113,  114,  115,    0,    0,    0,   78,
   78,   78,    0,    0,    0,    0,   72,   72,    0,   73,
   73,    0,   72,    0,    0,   73,    0,    0,  111,  112,
  113,  114,  115,  167,  168,  169,  170,  171,  172,  173,
  138,    0,    0,    0,  117,  118,  119,  120,  121,  122,
  123,  124,    0,   72,   72,   72,   73,   73,   73,  111,
  112,  113,  114,  115,    0,  125,  126,  140,    0,    0,
    0,  117,  118,  119,  120,  121,  122,  123,  124,    0,
    0,    0,  111,  112,  113,  114,  115,    0,    0,    0,
    0,    0,  125,  126,    0,    0,    0,    0,  141,    0,
    0,    0,  117,  118,  119,  120,  121,  122,  123,  124,
  111,  112,  113,  114,  115,    0,  176,    0,    0,  161,
    0,    0,    0,  125,  126,  117,  118,  119,  120,  121,
  122,  123,  124,  111,  112,  113,  114,  115,    0,  200,
    0,  111,  112,  113,  114,  115,  125,  126,    0,    0,
    0,    0,    0,  117,  118,  119,  120,  121,  122,  123,
  124,    0,    0,    0,    0,    0,  113,  114,  115,    0,
    0,    0,    0,    0,  125,  126,  117,  118,  119,  120,
  121,  122,  123,  124,  117,  118,  119,  120,  121,  122,
  123,  124,    0,    0,    0,    0,    0,  125,  126,  121,
  122,  123,  124,    0,    0,  125,  126,  117,  118,  119,
  120,  121,  122,  123,  124,    0,  125,  126,   60,    6,
    7,    8,    9,   10,   11,   12,   13,   14,  125,  126,
   60,   60,   60,   60,   60,   60,   60,   60,    0,   60,
   60,   61,    0,   60,   60,  168,  169,  170,  171,  172,
  173,   60,    0,   61,   61,   61,   61,   61,   61,   61,
   61,    0,   61,   61,   62,   60,   61,   61,    0,    0,
    0,    0,    0,    0,   61,  180,   62,   62,   62,   62,
   62,   62,   62,   62,   39,   62,   62,    0,   61,   62,
   62,    0,    0,    0,    0,  191,   39,   62,   39,   39,
   39,   39,   39,   39,  104,   39,   39,    0,    0,   39,
   39,   62,  166,  167,  168,  169,  170,  171,  172,  173,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   39,  166,  167,  168,  169,  170,  171,  172,  173,
    0,  104,  104,  104,  104,  104,  104,  104,  104,   36,
    0,   36,   36,   36,   36,   36,   56,    8,   36,   36,
    0,    0,   36,   56,    0,    0,    0,    0,    0,    8,
    0,    8,    8,    8,    8,    8,    0,    0,    8,    8,
    0,    0,    8,    0,   56,  119,  120,  121,  122,  123,
  124,    0,    0,   73,    0,   74,   75,   76,   77,   78,
    0,    0,   79,   80,  125,  126,   55,   36,    0,   36,
   36,   36,   36,   36,    0,    0,   36,   36,    0,    0,
   36,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         41,
    0,   61,   41,   41,   75,   66,  263,   97,  266,   41,
  285,  263,  293,  295,  296,  291,   77,  298,   79,  263,
  321,  322,  267,   84,   85,   86,    0,  117,  261,   90,
  310,  311,  266,   94,  292,  266,    0,  293,  313,  262,
  311,  102,  277,  300,  301,  302,  303,    0,  300,  301,
  111,  112,  113,  114,  115,  292,  300,  118,  119,  120,
  121,  122,  123,  124,  125,  126,  294,  300,  301,  302,
  303,  304,  305,  306,  307,  165,  166,  167,  258,  266,
  266,  263,  262,  295,  263,  266,  266,  296,  321,  322,
  151,  262,  266,  257,  258,  259,  260,  261,  263,   41,
  291,  191,  257,  258,  259,  260,  261,  266,  263,  289,
  290,  182,  266,   41,  175,  299,  296,  178,  300,  301,
  302,  303,  298,  213,  293,  262,  262,   41,  199,  309,
  294,  311,  262,    2,  296,   40,  300,  301,  302,  303,
  304,  305,  306,  307,  215,  300,  301,  302,  303,  304,
  305,  306,  307,  266,  280,   40,  216,  321,  322,  262,
   20,   30,   41,   32,  295,   41,  321,  322,  129,  291,
  279,  296,  133,  292,   34,  136,   45,  297,   41,  266,
  292,   50,  258,  263,  263,   41,  262,   41,  292,   48,
  266,   56,   41,   50,    7,   55,   39,  214,  204,  263,
   80,   54,   41,  304,  305,  306,  307,  168,  169,  170,
  171,  172,  173,  289,  290,  257,  258,  259,  260,  261,
  163,  263,  183,  184,   -1,  257,  258,  259,  260,  261,
   -1,  263,   -1,  309,   -1,  311,  300,  301,  302,  303,
  304,  305,  306,  307,   -1,   -1,   -1,   41,   -1,   -1,
   -1,   -1,  294,  295,  296,   41,  295,  295,  300,  301,
  302,  303,  294,  295,  296,   41,  266,  267,  300,  301,
  302,  303,  304,  305,  306,  307,   41,  277,  278,   -1,
  280,  281,  282,  283,  284,   -1,   -1,  287,  288,  321,
  322,  291,  292,  267,  268,  269,  270,  271,  272,  273,
  274,  275,  276,  277,  268,  269,  270,  271,  272,  273,
  274,  275,  276,  277,  267,  257,  258,  259,  260,  261,
   41,  263,   -1,   41,  277,   -1,   -1,   -1,   -1,  257,
  258,  259,  260,  261,   -1,  263,  301,  302,  303,  304,
  305,  306,  307,  257,  258,  259,  260,  261,   -1,  263,
   -1,   -1,  294,  295,  296,   -1,  321,  322,  300,  301,
  302,  303,  304,  305,  306,  307,  294,  295,  296,   -1,
   -1,   -1,  300,  301,  302,  303,  304,  305,  306,  307,
  294,  295,  296,   -1,   -1,   -1,  300,  301,  302,  303,
  304,  305,  306,  307,  257,  258,  259,  260,  261,   -1,
  263,  262,   -1,   -1,   -1,  266,   -1,   -1,  257,  258,
  259,  260,  261,   -1,  263,   -1,   -1,   -1,  257,  258,
  259,  260,  261,   -1,  263,   -1,   -1,   -1,  289,  290,
   -1,  294,  295,  296,   -1,   -1,   -1,  300,  301,  302,
  303,  304,  305,  306,  307,  294,  295,  296,  309,   -1,
  311,  300,  301,  302,  303,  294,  295,  296,   -1,   -1,
   -1,  300,  301,  257,  258,  259,  260,  261,   -1,  263,
   -1,  257,  258,  259,  260,   -1,   -1,  263,   -1,   -1,
   -1,  257,  258,  259,  260,   -1,   -1,  263,   -1,   -1,
   -1,   -1,  257,  258,  259,  260,   -1,   -1,  263,   -1,
  294,  295,  296,   -1,   -1,   -1,  300,   -1,  294,  295,
  296,  270,  271,  272,  273,  274,  275,  276,  294,  295,
  296,  257,  258,  259,  260,  261,   -1,   -1,   -1,  294,
  295,  296,   -1,   -1,   -1,   -1,  257,  258,   -1,  257,
  258,   -1,  263,   -1,   -1,  263,   -1,   -1,  257,  258,
  259,  260,  261,  301,  302,  303,  304,  305,  306,  307,
  296,   -1,   -1,   -1,  300,  301,  302,  303,  304,  305,
  306,  307,   -1,  294,  295,  296,  294,  295,  296,  257,
  258,  259,  260,  261,   -1,  321,  322,  296,   -1,   -1,
   -1,  300,  301,  302,  303,  304,  305,  306,  307,   -1,
   -1,   -1,  257,  258,  259,  260,  261,   -1,   -1,   -1,
   -1,   -1,  321,  322,   -1,   -1,   -1,   -1,  296,   -1,
   -1,   -1,  300,  301,  302,  303,  304,  305,  306,  307,
  257,  258,  259,  260,  261,   -1,  263,   -1,   -1,  294,
   -1,   -1,   -1,  321,  322,  300,  301,  302,  303,  304,
  305,  306,  307,  257,  258,  259,  260,  261,   -1,  263,
   -1,  257,  258,  259,  260,  261,  321,  322,   -1,   -1,
   -1,   -1,   -1,  300,  301,  302,  303,  304,  305,  306,
  307,   -1,   -1,   -1,   -1,   -1,  259,  260,  261,   -1,
   -1,   -1,   -1,   -1,  321,  322,  300,  301,  302,  303,
  304,  305,  306,  307,  300,  301,  302,  303,  304,  305,
  306,  307,   -1,   -1,   -1,   -1,   -1,  321,  322,  304,
  305,  306,  307,   -1,   -1,  321,  322,  300,  301,  302,
  303,  304,  305,  306,  307,   -1,  321,  322,  266,  268,
  269,  270,  271,  272,  273,  274,  275,  276,  321,  322,
  278,  279,  280,  281,  282,  283,  284,  285,   -1,  287,
  288,  266,   -1,  291,  292,  302,  303,  304,  305,  306,
  307,  299,   -1,  278,  279,  280,  281,  282,  283,  284,
  285,   -1,  287,  288,  266,  313,  291,  292,   -1,   -1,
   -1,   -1,   -1,   -1,  299,  263,  278,  279,  280,  281,
  282,  283,  284,  285,  266,  287,  288,   -1,  313,  291,
  292,   -1,   -1,   -1,   -1,  263,  278,  299,  280,  281,
  282,  283,  284,  285,  263,  287,  288,   -1,   -1,  291,
  292,  313,  300,  301,  302,  303,  304,  305,  306,  307,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  313,  300,  301,  302,  303,  304,  305,  306,  307,
   -1,  300,  301,  302,  303,  304,  305,  306,  307,  278,
   -1,  280,  281,  282,  283,  284,  285,  266,  287,  288,
   -1,   -1,  291,  292,   -1,   -1,   -1,   -1,   -1,  278,
   -1,  280,  281,  282,  283,  284,   -1,   -1,  287,  288,
   -1,   -1,  291,   -1,  313,  302,  303,  304,  305,  306,
  307,   -1,   -1,  278,   -1,  280,  281,  282,  283,  284,
   -1,   -1,  287,  288,  321,  322,  291,  278,   -1,  280,
  281,  282,  283,  284,   -1,   -1,  287,  288,   -1,   -1,
  291,
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
"$$1 :",
"decl_func : $$1 FUNC tipo ID LPAR argumentos RPAR bloque decl_func",
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
"$$2 :",
"sentencia : $$2 resolve",
"resolve : matched_stmt M",
"matched_stmt : IF LPAR exp1 RPAR M matched_stmt",
"matched_stmt : IF LPAR exp1 RPAR M matched_stmt ELSE N M matched_stmt",
"matched_stmt : WHILE LPAR M exp1 RPAR M matched_stmt",
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

//#line 588 "parserProyecto.y"



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


//#line 689 "Parser.java"
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
//#line 70 "parserProyecto.y"
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
//#line 92 "parserProyecto.y"
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
//#line 108 "parserProyecto.y"
{yyval=val_peek(1);}
break;
case 13:
//#line 118 "parserProyecto.y"
{yyval=new ParserVal("int");}
break;
case 14:
//#line 120 "parserProyecto.y"
{yyval=new ParserVal("float");}
break;
case 15:
//#line 122 "parserProyecto.y"
{yyval=new ParserVal("double");}
break;
case 22:
//#line 137 "parserProyecto.y"
{
      /*Genera ID  recursivamente */
      yyval = new ParserVal (val_peek(2).sval +","+ val_peek(0).sval);
    
    }
break;
case 23:
//#line 144 "parserProyecto.y"
{ yyval=val_peek(0); }
break;
case 24:
//#line 148 "parserProyecto.y"
{tablaSimbolos.enterScope("decl_func");}
break;
case 25:
//#line 149 "parserProyecto.y"
{tablaSimbolos.exitScope();}
break;
case 31:
//#line 161 "parserProyecto.y"
{yyval = val_peek(1);}
break;
case 33:
//#line 173 "parserProyecto.y"
{yyval = val_peek(1);}
break;
case 34:
//#line 175 "parserProyecto.y"
{yyval = val_peek(0);}
break;
case 35:
//#line 181 "parserProyecto.y"
{yyval = val_peek(0);}
break;
case 36:
//#line 183 "parserProyecto.y"
{tablaSimbolos.enterScope("resolve");}
break;
case 37:
//#line 183 "parserProyecto.y"
{yyval = val_peek(0);
                                  tablaSimbolos.exitScope();}
break;
case 38:
//#line 188 "parserProyecto.y"
{parserActions.backpatch((List<Integer>) val_peek(1).obj  , val_peek(0).ival , "para la N" ) ;}
break;
case 39:
//#line 194 "parserProyecto.y"
{ 
                /*Para mandar informacion a traves de parserVal creo un Mapa   */
                Context contexto = (Context) val_peek(3).obj;
            
                /*obtengo la lista de direcciones donde falta poner la bandera */
                List<Integer> l1= contexto.getVariable("B.truelist");
                List<Integer> l2= contexto.getVariable("B.falselist");

                /*Se resuelve las banderas faltantes */
                parserActions.backpatch(l1,val_peek(1).ival, "B.truelist" );
                /*Se regresan las banderas que apuntan a la siguiente instruccion (despues de if)*/
                yyval = new ParserVal(parserActions.merge(l2, (List<Integer>) val_peek(0).obj));
                

          }
break;
case 40:
//#line 213 "parserProyecto.y"
{  Context contexto = (Context) val_peek(7).obj;
               
                /*Obtenemos direcciones*/
                List<Integer> l1= contexto.getVariable("B.truelist");
                List<Integer> l2= contexto.getVariable("B.falselist");

                /*resuelve las banderas con las direcciones y con los placeholders M y N*/
                parserActions.backpatch(l1,val_peek(5).ival ,"Btruelist1");
                parserActions.backpatch(l2,val_peek(1).ival,"Bfalselist2");
                List<Integer> temp= parserActions.merge((List<Integer>)val_peek(4).obj,(List<Integer>)val_peek(2).obj );

                yyval = new  ParserVal( parserActions.merge(temp,(List<Integer>)val_peek(0).obj) );
          }
break;
case 41:
//#line 232 "parserProyecto.y"
{
                    parserActions.backpatch((List<Integer>)val_peek(0).obj,val_peek(4).ival,"");

                    Context contexto = (Context) val_peek(3).obj;
                    /*Obtenemos direcciones*/
                    List<Integer> l1= contexto.getVariable("B.truelist");
                    List<Integer> l2= contexto.getVariable("B.falselist");

                    parserActions.backpatch((List<Integer>)val_peek(0).obj,val_peek(4).ival,"");
                    parserActions.backpatch(l1,val_peek(1).ival,"");
                    yyval = new ParserVal(l2);
                    parserActions.handleGoto2(""+val_peek(4).ival);

                    
                }
break;
case 50:
//#line 261 "parserProyecto.y"
{
            /*Regresa la ubicacion de la instruccion siguiente*/
            int x =  parserActions.nextinstr();
            yyval = new ParserVal(x);
            
            }
break;
case 51:
//#line 270 "parserProyecto.y"
{ 
              /*Va a regresar la ubicacion de la instruccion siguiente*/
              yyval = new ParserVal( parserActions.makelist(parserActions.nextinstr()) );
              /*imprime un salto para que se salte el else*/
              parserActions.handleGoto();
            
             }
break;
case 52:
//#line 284 "parserProyecto.y"
{
        
       /*Verifica que exista el simbolo es decir que haya sido declarado primero */
       SymbolTable.Symbol simbolo = tablaSimbolos.getSymbol(val_peek(3).sval);
        if(simbolo != null){
          
          parserActions.handleAsignacion(val_peek(3) ,val_peek(1) );
          yyval =new ParserVal ((new ArrayList<Integer>()));
          
        }         
        
        else{
          System.out.println("Simbolo no existe");
          yyval =new ParserVal ((new ArrayList<Integer>()));
        }
       
      }
break;
case 60:
//#line 318 "parserProyecto.y"
{  yyval=val_peek(0);}
break;
case 71:
//#line 344 "parserProyecto.y"
{
         /* Si es un número, simplemente lo retornamos.*/
         /* lo regresamos como String , ya que el objetivo no es hacer la operacion aqui.*/
        yyval = new ParserVal ( Double.toString( val_peek(0).dval));

    }
break;
case 72:
//#line 353 "parserProyecto.y"
{   yyval = new ParserVal (parserActions.handleAddition(val_peek(2), val_peek(0))); }
break;
case 73:
//#line 355 "parserProyecto.y"
{   yyval = new ParserVal (parserActions.handleSubtraction(val_peek(2), val_peek(0)));   }
break;
case 74:
//#line 357 "parserProyecto.y"
{  yyval = new ParserVal (parserActions.handleMultiplication(val_peek(2), val_peek(0)));     }
break;
case 75:
//#line 360 "parserProyecto.y"
{ yyval =val_peek(1); }
break;
case 76:
//#line 362 "parserProyecto.y"
{yyval = new ParserVal (parserActions.handleDivi(val_peek(2), val_peek(0)));}
break;
case 77:
//#line 365 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleNEGATIVO(val_peek(0))); }
break;
case 78:
//#line 366 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handlePOTE(val_peek(2), val_peek(0))); }
break;
case 79:
//#line 369 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleOR(val_peek(3), val_peek(1))); }
break;
case 80:
//#line 370 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleAND(val_peek(2), val_peek(0))); }
break;
case 81:
//#line 371 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleIGUAL(val_peek(2), val_peek(0))); }
break;
case 82:
//#line 372 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleDESIGUAL(val_peek(2), val_peek(0))); }
break;
case 83:
//#line 373 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMENORQUE(val_peek(2), val_peek(0))); }
break;
case 84:
//#line 374 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMENORIGUAL(val_peek(2), val_peek(0))); }
break;
case 85:
//#line 375 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMAYORQUE(val_peek(2), val_peek(0))); }
break;
case 86:
//#line 376 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMAYORIGUAL(val_peek(2), val_peek(0))); }
break;
case 87:
//#line 377 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleMOD(val_peek(2), val_peek(0))); }
break;
case 88:
//#line 378 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleDIVENTERA(val_peek(2), val_peek(0))); }
break;
case 89:
//#line 379 "parserProyecto.y"
{ yyval = new ParserVal(parserActions.handleNEG(val_peek(0))); }
break;
case 90:
//#line 382 "parserProyecto.y"
{  yyval=val_peek(0);  }
break;
case 92:
//#line 384 "parserProyecto.y"
{ yyval= new ParserVal("true");  }
break;
case 93:
//#line 386 "parserProyecto.y"
{ yyval= new ParserVal("false"); }
break;
case 94:
//#line 414 "parserProyecto.y"
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
case 95:
//#line 430 "parserProyecto.y"
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
case 96:
//#line 449 "parserProyecto.y"
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
case 97:
//#line 461 "parserProyecto.y"
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
case 98:
//#line 473 "parserProyecto.y"
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
case 99:
//#line 485 "parserProyecto.y"
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
case 100:
//#line 497 "parserProyecto.y"
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
case 101:
//#line 509 "parserProyecto.y"
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
case 102:
//#line 521 "parserProyecto.y"
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
case 103:
//#line 533 "parserProyecto.y"
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
case 104:
//#line 546 "parserProyecto.y"
{
                yyval=val_peek(0);
               
              }
break;
case 106:
//#line 551 "parserProyecto.y"
{

                  Context contexto = new Context(); 
                  contexto.setVariable("B.truelist",parserActions.makelist(parserActions.nextinstr()));
                  contexto.setVariable("B.falselist", new ArrayList<Integer>() ); 
                    
                  parserActions.handleGoto();

                  yyval= new ParserVal(contexto);
                  }
break;
case 107:
//#line 564 "parserProyecto.y"
{ 

                  Context contexto = new Context(); 
                  contexto.setVariable("B.truelist", new ArrayList<Integer>() );
                  contexto.setVariable("B.falselist",parserActions.makelist(parserActions.nextinstr()) ); 

                  parserActions.handleGoto();

                  yyval= new ParserVal(contexto);
                  }
break;
case 108:
//#line 576 "parserProyecto.y"
{
                      /* Si es un número, simplemente lo retornamos.*/
                      yyval = new ParserVal ( Double.toString( val_peek(0).dval));

                  }
break;
//#line 1314 "Parser.java"
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
