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

    private int expressionBracesBalance = 0;
    private int evalBracesBalance = 0;
    private int zzPostponedMarkedPos = -1;
    private int stateBeforeBraces;
    private int stateBeforeString;
    private CharSequence stringSym;

    void enterWithinString() {
        this.stateBeforeString = yystate();
        this.stringSym = yytext();
        yybegin(EVAL_WITHIN_STRING);
    }

    void enterWithinBraces(int state) {
        this.stateBeforeBraces = yystate();
        yybegin(state);
    }

    IElementType resolveEvalState(IElementType type) {
        yybegin(EXPRESSION);
        zzStartRead = zzPostponedMarkedPos;
        zzPostponedMarkedPos = -1;
        return type;
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
LBrace = "{"
RBrace = "}"
LBrack = "["
RBrack = "]"
Import = "+"
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
StringLiteral = [\"\'`]
BackSlash = \\
ArraySpecifier = {LBrack}{RBrack}
IncludeClose = {Comma}{WhiteSpace}*{Identifier}?{ArraySpecifier}?{WhiteSpace}*{Close}

%state EXPRESSION_START, EXPRESSION
%state INCLUDE, INCLUDE_EVAL_EXPRESSION
%state TYPEDEF, TYPEDEF_ATTRIBUTE, DOC_TYPE
%state EVAL, EVAL_EXPRESSION, EVAL_WITHIN_BRACES, EVAL_EXPRESSION_WITHIN_BRACES, EVAL_WITHIN_STRING
%%

<YYINITIAL> {Open} { yybegin(EXPRESSION_START); return T_OPEN; }

<EXPRESSION_START> {
    {Import} { yybegin(EVAL); return T_IMPORT; }
    {Escape} {
          yybegin(EVAL_EXPRESSION);
          zzPostponedMarkedPos = zzCurrentPos;
          return T_ESCAPE;
      }
    {Interpolate} {
          yybegin(EVAL_EXPRESSION);
          zzPostponedMarkedPos = zzCurrentPos;
          return T_INTERPOLATE;
      }
    {Typedef} { yybegin(TYPEDEF); return T_TYPEDEF; }
    {Namespace} { yybegin(EXPRESSION); return T_NAMESPACE; }
    {Export} { yybegin(EXPRESSION); return T_EXPORT; }
    {Partial} { yybegin(INCLUDE); return T_PARTIAL; }
    {Inline} { yybegin(INCLUDE); return T_INLINE; }
    {Close} { yybegin(YYINITIAL); return T_CLOSE; }
    [^] { yypushback(1); yybegin(EVAL); }
}

<INCLUDE> {
    {Comma} {
          yybegin(INCLUDE_EVAL_EXPRESSION);
          zzPostponedMarkedPos = zzCurrentPos;
          return T_COMMA;
      }
}

<INCLUDE_EVAL_EXPRESSION> {
    {StringLiteral} { this.enterWithinString(); }
    {LBrace} {
          yypushback(1);
          this.enterWithinBraces(EVAL_EXPRESSION_WITHIN_BRACES);
      }
    {Close} {
          yypushback(2);
          return resolveEvalState(T_EVAL_EXPRESSION);
      }
    {IncludeClose} {
          yypushback(yylength());
          return resolveEvalState(T_EVAL_EXPRESSION);
      }
    <<EOF>> {
          return resolveEvalState(T_EVAL_EXPRESSION);
      }
    [^] { break; }
}

<EVAL> {
    {StringLiteral} { this.enterWithinString(); }
    {LBrace} {
          yypushback(1);
          this.enterWithinBraces(EVAL_WITHIN_BRACES);
      }
    {Close} {
          yypushback(2);
          return resolveEvalState(T_EVAL);
      }
    <<EOF>> {
          return resolveEvalState(T_EVAL);
      }
    [^] { break; }
}

<EVAL_EXPRESSION> {
    {StringLiteral} { this.enterWithinString(); }
    {LBrace} {
          yypushback(1);
          this.enterWithinBraces(EVAL_EXPRESSION_WITHIN_BRACES);
      }
    {Close} {
          yypushback(2);
          return resolveEvalState(T_EVAL_EXPRESSION);
      }
    <<EOF>> {
          return resolveEvalState(T_EVAL_EXPRESSION);
      }
    [^] { break; }
}

<EVAL_WITHIN_BRACES> {
    {StringLiteral} { this.enterWithinString(); }
    {LBrace} { this.evalBracesBalance++; }
    {RBrace} {
          this.evalBracesBalance--;
          if (this.evalBracesBalance == 0) {
              yybegin(this.stateBeforeBraces);
          }
      }
    {Close} {
          // {{ [for/while/if/do/funciton/else/=>]() { }}
          if (this.evalBracesBalance == 1) {
              this.evalBracesBalance = 0;
              yypushback(2);
              yybegin(this.stateBeforeBraces);
          } else {
              this.evalBracesBalance -= 2;
              if (this.evalBracesBalance == 0) {
                  yybegin(this.stateBeforeBraces);
              }
          }
      }
    <<EOF>> { yypushback(yylength()); yybegin(this.stateBeforeBraces); }
    [^] { break; }
}

<EVAL_EXPRESSION_WITHIN_BRACES> {
    {StringLiteral} { this.enterWithinString(); }
    {LBrace} { this.expressionBracesBalance++; }
    {RBrace} {
          this.expressionBracesBalance--;
          if (this.expressionBracesBalance == 0) {
              yybegin(this.stateBeforeBraces);
          }
      }
    <<EOF>> { yypushback(yylength()); yybegin(this.stateBeforeBraces); }
    [^] { break; }
}

<EVAL_WITHIN_STRING> {
    {BackSlash}{StringLiteral} { break; }
    {StringLiteral} {
          if (CharSequence.compare(this.stringSym, yytext()) == 0) {
              yybegin(this.stateBeforeString);
          }
      }
    <<EOF>> { yypushback(yylength()); yybegin(this.stateBeforeString); }
    [^] { break; }
}

<TYPEDEF> {
    {This} { yybegin(TYPEDEF_ATTRIBUTE); return T_THIS; }
}
<TYPEDEF_ATTRIBUTE> {
    {WhiteSpace}+ { yybegin(DOC_TYPE); return WHITE_SPACE; }
    {Identifier} { yybegin(DOC_TYPE); return T_IDENTIFIER; }
}
<DOC_TYPE> ~{Close} { yypushback(2); yybegin(EXPRESSION); return T_DOC_TYPE; }

<EXPRESSION, INCLUDE, TYPEDEF, TYPEDEF_ATTRIBUTE> {
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