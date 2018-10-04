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
Escape = {Open}"-"
Interpolate = {Open}"="
Typedef = {Open}"*"
Namespace = {Open}"$"
Export = {Open}"@"
Partial = {Open}"#"
Inline = {Open}"%"
This = "this"
Dot = "."
Identifier = [a-zA-Z\-_][a-zA-Z0-9\-_\.]+
Comma = ","
ArraySpecifier = {LBrack}{RBrack}
ThisProperty = {This}{Dot}[a-zA-Z\-_][a-zA-Z0-9\-_]+

%state STATEMENT, INCLUDE, INCLUDE_INPUT, TYPEDEF, DOC_TYPE, EVAL
%%

<YYINITIAL> {Open} { yypushback(2); yybegin(STATEMENT); }

<STATEMENT> {
    {Escape} { yybegin(EVAL); return T_ESCAPE; }
    {Interpolate} { yybegin(EVAL); return T_INTERPOLATE; }
    {Typedef} { yybegin(TYPEDEF); return T_TYPEDEF; }
    {Namespace} { return T_NAMESPACE; }
    {Export} { return T_EXPORT; }
    {Partial} { yybegin(INCLUDE); return T_PARTIAL; }
    {Inline} { yybegin(INCLUDE); return T_INLINE; }
    {Open} { yybegin(EVAL); return T_OPEN; }
    {Comma} { return T_COMMA; }
}

<INCLUDE> {Comma} { yybegin(INCLUDE_INPUT); return T_COMMA; }
<INCLUDE_INPUT> {InputCharacter}* {Comma} { yypushback(1); yybegin(STATEMENT); return T_EVAL; }

<TYPEDEF> {ThisProperty} { yybegin(DOC_TYPE); return T_THIS_PROPERTY; }

<EVAL> ~{Close} { yypushback(2); yybegin(STATEMENT); return T_EVAL; }
<DOC_TYPE> ~{Close} { yypushback(2); yybegin(STATEMENT); return T_DOC_TYPE; }

<STATEMENT, INCLUDE, TYPEDEF> {
    {ArraySpecifier} { return T_ARRAY_SPECIFIER; }
    {Identifier} { return T_IDENTIFIER; }
    {Close} { yybegin(YYINITIAL); return T_CLOSE; }
    {WhiteSpace}+ { return WHITE_SPACE; }
}

[^] { return T_DATA; }
. { return BAD_CHARACTER; }
