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


package com.io7m.mime2045.tests;

import com.io7m.mime2045.parser.internal.MimeLexer;
import com.io7m.mime2045.parser.internal.MimeTokenType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public final class MimeLexerTest
{
  private static final Logger LOG =
    LoggerFactory.getLogger(MimeLexerTest.class);

  private static MimeLexer lexerOf(
    final String text)
  {
    final var lexer = new MimeLexer(new StringReader(text));
    lexer.setTracer(s -> LOG.debug("{}", s));
    return lexer;
  }

  @Test
  public void testEOF()
    throws Exception
  {
    final var lexer = lexerOf("");
    assertInstanceOf(MimeTokenType.EOF.class, lexer.token());
  }

  @Test
  public void testQuoted0()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append('"');
    text.append('a');
    text.append('"');

    final var lexer =
      lexerOf(text.toString());
    final var r =
      assertInstanceOf(MimeTokenType.Quoted.class, lexer.token());

    assertInstanceOf(MimeTokenType.EOF.class, lexer.token());
  }

  @Test
  public void testQuoted1()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append('"');
    text.append('\\');
    text.append('a');
    text.append('"');

    final var lexer =
      lexerOf(text.toString());
    final var r =
      assertInstanceOf(MimeTokenType.Quoted.class, lexer.token());

    assertEquals("a", r.value());
  }

  @Test
  public void testQuoted2()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append('"');
    text.append('\\');
    text.append('"');
    text.append('"');

    final var lexer =
      lexerOf(text.toString());
    final var r =
      assertInstanceOf(MimeTokenType.Quoted.class, lexer.token());

    assertEquals("\"", r.value());
  }

  @Test
  public void testError0()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append('"');
    text.append('\\');
    text.append('a');

    final var lexer =
      lexerOf(text.toString());

    assertInstanceOf(MimeTokenType.Invalid.class, lexer.token());
  }

  @Test
  public void testError1()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append('"');
    text.append('a');

    final var lexer =
      lexerOf(text.toString());

    assertInstanceOf(MimeTokenType.Invalid.class, lexer.token());
  }

  @Test
  public void testError2()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append('@');

    final var lexer =
      lexerOf(text.toString());

    assertInstanceOf(MimeTokenType.Invalid.class, lexer.token());
  }

  @Test
  public void testError3()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append('"');
    text.append('\\');

    final var lexer =
      lexerOf(text.toString());

    assertInstanceOf(MimeTokenType.Invalid.class, lexer.token());
  }

  @Test
  public void testError4()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append('\0');

    final var lexer =
      lexerOf(text.toString());

    assertInstanceOf(MimeTokenType.Invalid.class, lexer.token());
  }
}
