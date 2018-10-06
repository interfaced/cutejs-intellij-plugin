package org.cutejs.lang.lexer;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static org.cutejs.lang.psi.CuteTypes.*;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
%%

%{
    public _CuteLexer() {
        this((java.io.Reader)null);
    }
%}

%public
%class _CuteLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

Open = "{{"
Close = "}}"
LBrack = "["
RBrack = "]"
Escape = "-"
Interpolate = "="
Typedef = "*"
Namespace = "$"
Export = "@"
Partial = "#"
Inline = "%"
This = "this"
Dot = "."
Identifier = [a-zA-Z\-_][a-zA-Z0-9\-_\.]+
Comma = ","
ArraySpecifier = {LBrack}{RBrack}
ThisProperty = {This}{Dot}[a-zA-Z\-_][a-zA-Z0-9\-_]+
IncludeClose = {Comma} {WhiteSpace}* {Identifier} {ArraySpecifier}? {WhiteSpace}* {Close}

%state EXPRESSION_START, EXPRESSION, INCLUDE, INCLUDE_INPUT, TYPEDEF, DOC_TYPE, EVAL, EVAL_EXPRESSION
%%

<YYINITIAL> {Open} { yybegin(EXPRESSION_START); return T_OPEN; }

<EXPRESSION_START> {
    {Escape} { yybegin(EVAL_EXPRESSION); return T_ESCAPE; }
    {Interpolate} { yybegin(EVAL_EXPRESSION); return T_INTERPOLATE; }
    {Typedef} { yybegin(TYPEDEF); return T_TYPEDEF; }
    {Namespace} { yybegin(EXPRESSION); return T_NAMESPACE; }
    {Export} { yybegin(EXPRESSION); return T_EXPORT; }
    {Partial} { yybegin(INCLUDE); return T_PARTIAL; }
    {Inline} { yybegin(INCLUDE); return T_INLINE; }
    [^] { yypushback(1); yybegin(EVAL); }
}

<INCLUDE> {Comma} { yybegin(INCLUDE_INPUT); return T_COMMA; }
<INCLUDE_INPUT> {
    {IncludeClose} { yypushback(yylength()); yybegin(EXPRESSION); }
    [^] { return T_EVAL_EXPRESSION_CHAR; }
}

<EVAL, EVAL_EXPRESSION> {Close} { yypushback(2); yybegin(EXPRESSION); }

<EVAL> [^] { return T_EVAL_CHAR; }
<EVAL_EXPRESSION> [^] { return T_EVAL_EXPRESSION_CHAR; }

<TYPEDEF> {ThisProperty} { yybegin(DOC_TYPE); return T_THIS_PROPERTY; }
<DOC_TYPE> ~{Close} { yypushback(2); yybegin(EXPRESSION); return T_DOC_TYPE; }

<EXPRESSION, INCLUDE, TYPEDEF> {
    {Comma} { return T_COMMA; }
    {ArraySpecifier} { return T_ARRAY_SPECIFIER; }
    {Identifier} { return T_IDENTIFIER; }
    {Close} { yybegin(YYINITIAL); return T_CLOSE; }
    {WhiteSpace}+ { return WHITE_SPACE; }
    . { return BAD_CHARACTER; }
}

[^] { return T_DATA; }