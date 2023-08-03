grammar Stellaris;

file
   : (pair|var)*
   ;

map
   : '{' (pair | 'optimize_memory' )* '}'
   ;

pair
   : BAREWORD SPECIFIER value
   | NUMBER SPECIFIER value
   ;

var
   : VARIABLE SPECIFIER NUMBER
   ;

array
   : '{' value+ '}'
   ;

valueSpec
   : BAREWORD ':' BAREWORD (( '|' BAREWORD )* '|')?
   ;

value
   : NUMBER
   | BOOLEAN
   | DATE
   | STRING
   | VARIABLE
   | BAREWORD
   | valueSpec
   | map
   | array
   ;

BOOLEAN
   : 'yes'
   | 'no'
   | 'true'
   | 'false'
   ;

VARIABLE
   : '@'([A-Za-z][A-Za-z_0-9.%-]*)
   ;

SPECIFIER
   : '=' | '!=' | '<>' | '>' | '<' | '<=' | '>=' ;

NUMBER
   : '-'?[0-9]+'%'
   | '-'?[0-9]+'.'[0-9]+
   | '-'?[0-9]+
   ;

DATE
   : [0-9]+'.'[0-9]+'.'[0-9]+;

BAREWORD
   : [A-Za-z][@A-Za-z_0-9.%/-]*
   ;

STRING
   : '"'(~["])*'"'
   ;

WS
   : [ \t\n\r]+ -> skip
   ;

LINE_COMMENT
   : '#'~[\r\n]* -> channel(HIDDEN)
   ;
