package org.cutejs.lang.lexer;

import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;

import java.util.*;
import static org.cutejs.lang.psi.CuteTypes.*;
import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;

%%

%{
  public _CuteLexer() {
    this((java.io.Reader)null);
  }

  private Stack<Integer> stack = new Stack();

  private void yypushstate(int newState)
  {
      stack.push(yystate());
      yybegin(newState);
  }

  private void yypopstate()
  {
      yybegin(stack.pop());
  }

  private void yypopstate(int count)
  {
      int newState = YYINITIAL;

      while(count-- != 0 && ! stack.isEmpty())
      {
          newState = stack.pop();
      }

      yybegin(newState);
  }

  private void yyresetstate()
  {
      while( ! stack.isEmpty())
      {
          yybegin(stack.pop());
      }
  }

  private IElementType trimElement(IElementType element)
  {
      return trimElement(element, false);
  }

  private IElementType trimElement(IElementType element, boolean pushbackWhitespace)
  {
      CharSequence text = yytext();

      if(text.length() == 0)
      {
          return null;
      }

      int trailingWhitespaceCount = 0;

      for(int i = text.length() - 1; i >= 0 && Character.isWhitespace(text.charAt(i)); i--)
      {
         trailingWhitespaceCount++;
      }

      if(pushbackWhitespace)
      {
         yypushback(trailingWhitespaceCount);
      }

      if(text.length() > trailingWhitespaceCount)
      {
         return element;
      }

      return WHITE_SPACE;
  }
%}

%public
%class _CuteLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

OPEN_BLOCK_MARKER = "{{"
CLOSE_BLOCK_MARKER = "}}"
OPEN_BLOCK_MARKER_ESCAPED = {OPEN_BLOCK_MARKER}"-"
OPEN_BLOCK_MARKER_UNESCAPED = {OPEN_BLOCK_MARKER}"="
OPEN_BLOCK_MARKER_VAR_TYPE_DECLARATION = {OPEN_BLOCK_MARKER}"*"
OPEN_BLOCK_MARKER_NAMESPACE_DECLARATION = {OPEN_BLOCK_MARKER}"$"
OPEN_BLOCK_MARKER_EXPORT_DATA = {OPEN_BLOCK_MARKER}"@"
OPEN_BLOCK_MARKER_INCLUDE = {OPEN_BLOCK_MARKER}"#"
OPEN_BLOCK_MARKER_INLINE = {OPEN_BLOCK_MARKER}"%"

HTML_BLOCK = !([^]*(
     {OPEN_BLOCK_MARKER}
    |{OPEN_BLOCK_MARKER_ESCAPED}
    |{OPEN_BLOCK_MARKER_UNESCAPED}
    |{OPEN_BLOCK_MARKER_VAR_TYPE_DECLARATION}
    |{OPEN_BLOCK_MARKER_NAMESPACE_DECLARATION}
    |{OPEN_BLOCK_MARKER_INCLUDE}
    |{OPEN_BLOCK_MARKER_INLINE}
)[^]*)

LETTER = [:letter:] | "_"
DIGIT = [:digit:]
IDENT = {LETTER} ({LETTER}|{DIGIT})*
NAMESPACE = [A-Za-z][A-Za-z0-9_]*(\.[A-Za-z][A-Za-z0-9_]*)*(\[[A-Za-z][A-Za-z0-9_]*(\.[A-Za-z][A-Za-z0-9_]*)*\])*

EXPORT_NAME = {NAMESPACE}

LINE_TERMINATOR = \r|\n|\r\n
WHITE_CHARS = [\t \f]
WHITE_SPACE = {LINE_TERMINATOR}|{WHITE_CHARS}

%state ST_BLOCK
%state ST_ESCAPED_START
%state ST_UNESCAPED_START
%state ST_DECLARE_VAR_START
%state ST_EXPORT_START
%state ST_DECLARE_NAMESPACE_START
%state ST_JS_BLOCK
%state ST_JAVASCRIPT
%state ST_JS_LIKE_BLOCK
%state ST_JAVASCRIPT_LIKE
%state ST_EXPORT_BLOCK
%state ST_TYPE_BLOCK
%state ST_TYPE_VAR
%state ST_TYPE_IDENTIFIER
%%

<YYINITIAL> {
    {HTML_BLOCK}{OPEN_BLOCK_MARKER}
    {
        IElementType el;

        yypushback(2);
        yypushstate(ST_BLOCK);

        if((el = trimElement(T_TEMPLATE_HTML_CODE)) != null) return el;
    }
    {HTML_BLOCK}
    {
        return trimElement(T_TEMPLATE_HTML_CODE);
    }
}


<ST_BLOCK>
{
    {OPEN_BLOCK_MARKER_ESCAPED}
    {
        yypushstate(ST_JS_BLOCK);
        return T_OPEN_BLOCK_MARKER_ESCAPED;
    }
    {OPEN_BLOCK_MARKER_UNESCAPED}
    {
        yypushstate(ST_JS_BLOCK);
        return T_OPEN_BLOCK_MARKER_UNESCAPED;
    }
    {OPEN_BLOCK_MARKER_VAR_TYPE_DECLARATION}
    {
        yypushstate(ST_TYPE_BLOCK);
        return T_OPEN_BLOCK_MARKER_VAR_TYPE_DECLARATION;
    }
    {OPEN_BLOCK_MARKER_EXPORT_DATA}
    {
         yypushstate(ST_EXPORT_BLOCK);
         return T_OPEN_BLOCK_MARKER_EXPORT_DATA;
    }
    {OPEN_BLOCK_MARKER_NAMESPACE_DECLARATION}
    {
        yypushstate(ST_JS_LIKE_BLOCK);
        return T_OPEN_BLOCK_MARKER_NAMESPACE_DECLARATION;
    }
    {OPEN_BLOCK_MARKER_INCLUDE}
    {
        yypushstate(ST_JS_LIKE_BLOCK);
        return T_OPEN_BLOCK_MARKER_INCLUDE;
    }
    {OPEN_BLOCK_MARKER_INLINE}
    {
        yypushstate(ST_JS_LIKE_BLOCK);
        return T_OPEN_BLOCK_MARKER_INLINE;
    }
    {OPEN_BLOCK_MARKER}
    {
        yypushstate(ST_JS_BLOCK);
        return T_OPEN_BLOCK_MARKER;
    }
    {CLOSE_BLOCK_MARKER}
    {
        yypopstate();
        return T_CLOSE_BLOCK_MARKER;
    }
}

<ST_TYPE_BLOCK>
{
    {CLOSE_BLOCK_MARKER}
    {
        yypushback(2);
        yypopstate();
    }
    {WHITE_SPACE}+
    {
        return WHITE_SPACE;
    }
    .
    {
        yypushback(1);
        yypushstate(ST_TYPE_VAR);
    }
}

<ST_TYPE_VAR>
{
    {CLOSE_BLOCK_MARKER}
    {
        yypushback(2);
        yypopstate();
    }
    !([^]*({WHITE_SPACE})[^]*){WHITE_SPACE}
    {
        IElementType el;

        yypushback(1);
        yypopstate();
        yypushstate(ST_TYPE_IDENTIFIER);

        if((el = trimElement(T_TEMPLATE_JAVASCRIPT_LIKE_CODE, true)) != null) return el;
    }
    !([^]*({WHITE_SPACE})[^]*)
    {
        return T_TEMPLATE_JAVASCRIPT_LIKE_CODE;
        // followed by eof
    }
}

<ST_TYPE_IDENTIFIER>
{
    {CLOSE_BLOCK_MARKER}
    {
        yypushback(2);
        yypopstate();
    }
    {WHITE_SPACE}+
    {
        return WHITE_SPACE;
    }
    {IDENT}
    {
        return T_TYPE_IDENTIFIER;
    }
}

<ST_EXPORT_BLOCK>
{
    {CLOSE_BLOCK_MARKER}
    {
        yypushback(2);
        yypopstate();
    }
    {EXPORT_NAME}
    {
        return T_EXPORT_IDENTIFIER;
    }
}

<ST_JS_BLOCK>
{
    {CLOSE_BLOCK_MARKER}
    {
        yypushback(2);
        yypopstate();
    }
    {WHITE_SPACE}+
    {
        yypushstate(ST_JAVASCRIPT);
        return WHITE_SPACE;
    }
    .
    {
        yypushback(1);
        yypushstate(ST_JAVASCRIPT);
    }
}

<ST_JS_LIKE_BLOCK>
{
    {CLOSE_BLOCK_MARKER}
    {
        yypushback(2);
        yypopstate();
    }
    {WHITE_SPACE}+
    {
        yypushstate(ST_JAVASCRIPT_LIKE);
        return WHITE_SPACE;
    }
    .
    {
        yypushback(1);
        yypushstate(ST_JAVASCRIPT_LIKE);
    }
}

<ST_JAVASCRIPT>
{
    !([^]*({CLOSE_BLOCK_MARKER})[^]*){CLOSE_BLOCK_MARKER}
    {
        IElementType el;

        yypushback(2);
        yypopstate();

        if((el = trimElement(T_TEMPLATE_JAVASCRIPT_CODE, true)) != null) return el;
    }
    !([^]*({CLOSE_BLOCK_MARKER})[^]*)
    {
        return T_TEMPLATE_JAVASCRIPT_CODE;
        // followed by eof
    }
}

<ST_JAVASCRIPT_LIKE>
{
    !([^]*({CLOSE_BLOCK_MARKER})[^]*){CLOSE_BLOCK_MARKER}
    {
        IElementType el;

        yypushback(2);
        yypopstate();

        if((el = trimElement(T_TEMPLATE_JAVASCRIPT_LIKE_CODE, true)) != null) return el;
    }
    !([^]*({CLOSE_BLOCK_MARKER})[^]*)
    {
        return T_TEMPLATE_JAVASCRIPT_LIKE_CODE;
        // followed by eof
    }
}

{WHITE_SPACE}+
{
    return WHITE_SPACE;
}
.
{
    return BAD_CHARACTER;
}
