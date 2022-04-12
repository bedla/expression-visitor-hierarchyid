grammar SimpleBoolean;

parse
 : expression EOF
 ;

expression
 : NOT expression                           #notExpression
 | left=expression AND right=expression     #andExpression
 | left=expression OR right=expression      #orExpression
 | IDENTIFIER                               #identifierExpression
 ;

AND        : 'AND';
OR         : 'OR';
NOT        : 'NOT';
IDENTIFIER : [a-zA-Z_] [a-zA-Z_0-9]*;
WS         : [ \r\t\u000C\n]+ -> skip;
