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
Identifier = [a-zA-Z\-_][a-zA-Z0-9\-_]*
Comma = ","
ArraySpecifier = {LBrack}{RBrack}
IncludeClose = {Comma}{WhiteSpace}*{Identifier}{ArraySpecifier}?{WhiteSpace}*{Close}

%state EXPRESSION_START, EXPRESSION, INCLUDE, INCLUDE_INPUT, INCLUDE_CLOSE_CHECK, TYPEDEF, DOC_TYPE, EVAL, EVAL_EXPRESSION
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
    {Close} { yybegin(YYINITIAL); return T_CLOSE; }
    [^] { yypushback(1); yybegin(EVAL); }
}

<INCLUDE> {Comma} { yybegin(INCLUDE_CLOSE_CHECK); return T_COMMA; }
<INCLUDE_CLOSE_CHECK> {
    ~{IncludeClose} { yypushback(yylength()); yybegin(INCLUDE_INPUT); }
}
<INCLUDE_INPUT> {
    {IncludeClose} { yypushback(yylength()); yybegin(EXPRESSION); return T_EVAL_EXPRESSION; }
    [^] { break; }
}
<EVAL> ~{Close} { yypushback(2); yybegin(EXPRESSION); return T_EVAL; }
<EVAL_EXPRESSION> ~{Close} { yypushback(2); yybegin(EXPRESSION); return T_EVAL_EXPRESSION; }

<TYPEDEF> {
    {This} { return T_THIS; }
    {Identifier} { yybegin(DOC_TYPE); return T_IDENTIFIER; }
}
<DOC_TYPE> ~{Close} { yypushback(2); yybegin(EXPRESSION); return T_DOC_TYPE; }

<EXPRESSION, INCLUDE, TYPEDEF> {
    {Comma} { return T_COMMA; }
    {Dot} { return T_DOT; }
    {This} { return T_THIS; }
    {ArraySpecifier} { return T_ARRAY_SPECIFIER; }
    {Identifier} { return T_IDENTIFIER; }
    {Close} { yybegin(YYINITIAL); return T_CLOSE; }
    {WhiteSpace}+ { return WHITE_SPACE; }
    . { return BAD_CHARACTER; }
}

[^] { return T_DATA; }