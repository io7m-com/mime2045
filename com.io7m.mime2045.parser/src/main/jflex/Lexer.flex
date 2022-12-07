/*
 * Copyright © 2022 Mark Raynsford <code@io7m.com> https://www.io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.mime2045.parser.internal;

import com.io7m.mime2045.parser.internal.MimeTokenType;
import com.io7m.jlexing.core.LexicalPosition;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

%%

%apiprivate
%class MimeLexer
%column
%public
%final
%line
%type MimeTokenType
%unicode

%{

  private final StringBuilder buffer = new StringBuilder();
  private final LinkedList<MimeTokenType> tokenBuffer = new LinkedList<>();
  private Consumer<String> trace = text -> { };

  public MimeTokenType token()
    throws IOException
  {
    if (this.tokenBuffer.isEmpty()) {
      return this.yylex();
    }
    return this.tokenBuffer.pop();
  }

  public void pushBack(
    final MimeTokenType token)
  {
    this.tokenBuffer.push(Objects.requireNonNull(token, "token"));
  }

  public void setTracer(Consumer<String> tracer)
  {
    this.trace = Objects.requireNonNull(tracer, "tracer");
  }

  private URI file = URI.create("urn:stdin");

  public void setFile(final URI file)
  {
    this.file = Objects.requireNonNull(file, "File");
  }

  public LexicalPosition<URI> position()
  {
    final var tokenLength = this.yytext().length();
    return LexicalPosition.<URI>builder()
      .setLine(this.yyline + 1)
      .setColumn(this.yycolumn)
      .setColumnEnd(this.yycolumn + tokenLength)
      .setFile(this.file)
      .build();
  }

%}

LineTerminator  = \r|\n|\r\n
InputCharacter  = [!#$%&'*+\-\.0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ\^_`abcdefghijklmnopqrstuvwxyz{|}~]
Whitespace      = [ \t\f\r\n]
StringCharacter = [^\r\n\"\\]

%state STATE_STRING
%state STATE_STRING_ESCAPE

%%

<YYINITIAL> "/" {
  return new com.io7m.mime2045.parser.internal.MimeTokenType.Slash(this.position());
}
<YYINITIAL> ";" {
  return new com.io7m.mime2045.parser.internal.MimeTokenType.Semicolon(this.position());
}
<YYINITIAL> "=" {
  return new com.io7m.mime2045.parser.internal.MimeTokenType.Equals(this.position());
}

<YYINITIAL> {
  \"  {
    this.trace.accept("YYINITIAL -> STATE_STRING");
    this.yybegin(STATE_STRING);
    this.buffer.setLength(0);
  }

  {Whitespace} {
    /* Ignore */
  }

  {InputCharacter}+ {
    this.trace.accept("YYINITIAL: " + yytext());
    return new com.io7m.mime2045.parser.internal.MimeTokenType.Token(this.position(), yytext());
  }
}

<STATE_STRING> {
  \" {
    this.trace.accept("STATE_STRING -> YYINITIAL");
    yybegin(YYINITIAL);
    return new com.io7m.mime2045.parser.internal.MimeTokenType.Quoted(this.position(), this.buffer.toString());
  }

  \\ {
    this.trace.accept("STATE_STRING -> STATE_STRING_ESCAPE");
    yybegin(STATE_STRING_ESCAPE);
  }

  [^\\] {
    this.trace.accept("STATE_STRING: " + yytext());
    this.buffer.append(yytext());
  }

  <<EOF>> {
    this.trace.accept("STATE_STRING_ESCAPE: unexpected EOF: " + yytext());
    return new com.io7m.mime2045.parser.internal.MimeTokenType.Invalid(this.position(), yytext());
  }
}

<STATE_STRING_ESCAPE> {
  . {
    this.trace.accept("STATE_STRING_ESCAPE -> STATE_STRING (" + yytext() + ")");
    this.yybegin(STATE_STRING);
    this.buffer.append(yytext());
  }

  <<EOF>> {
    this.trace.accept("STATE_STRING_ESCAPE: unexpected EOF: " + yytext());
    return new com.io7m.mime2045.parser.internal.MimeTokenType.Invalid(this.position(), yytext());
  }
}

<<EOF>> {
  this.trace.accept("EOF");
  return new com.io7m.mime2045.parser.internal.MimeTokenType.EOF(this.position());
}

/* error fallback */
.|\n  {
  this.trace.accept("invalid: " + yytext());
  return new com.io7m.mime2045.parser.internal.MimeTokenType.Invalid(this.position(), yytext());
}
