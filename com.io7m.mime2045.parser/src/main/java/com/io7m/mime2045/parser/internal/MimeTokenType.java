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

import com.io7m.jlexing.core.LexicalPosition;
import com.io7m.jlexing.core.LexicalType;

import java.net.URI;
import java.util.Objects;

/**
 * The type of lexical tokens in type strings.
 */

public sealed interface MimeTokenType
  extends LexicalType<URI>
{
  /**
   * @param clazz The token class
   * @param <T>   The token type
   *
   * @return The name of the given token type
   */

  static <T extends MimeTokenType> String name(
    final Class<T> clazz)
  {
    if (Objects.equals(clazz, EOF.class)) {
      return "<EOF>";
    }
    if (Objects.equals(clazz, Slash.class)) {
      return "'/'";
    }
    if (Objects.equals(clazz, Semicolon.class)) {
      return "';'";
    }
    if (Objects.equals(clazz, Equals.class)) {
      return "'='";
    }
    if (Objects.equals(clazz, Token.class)) {
      return "A <token>";
    }
    if (Objects.equals(clazz, Quoted.class)) {
      return "A <quoted-string>";
    }
    if (Objects.equals(clazz, Invalid.class)) {
      return "An invalid token";
    }

    throw new IllegalStateException("Unreachable code.");
  }

  /**
   * The EOF token.
   *
   * @param lexical The lexical position
   */

  record EOF(LexicalPosition<URI> lexical)
    implements MimeTokenType
  {
    @Override
    public String toString()
    {
      return "<EOF>";
    }
  }

  /**
   * The slash token.
   *
   * @param lexical The lexical position
   */

  record Slash(LexicalPosition<URI> lexical)
    implements MimeTokenType
  {
    @Override
    public String toString()
    {
      return "'/'";
    }
  }

  /**
   * The semicolon token.
   *
   * @param lexical The lexical position
   */

  record Semicolon(LexicalPosition<URI> lexical)
    implements MimeTokenType
  {
    @Override
    public String toString()
    {
      return "';'";
    }
  }

  /**
   * The equals token.
   *
   * @param lexical The lexical position
   */

  record Equals(LexicalPosition<URI> lexical)
    implements MimeTokenType
  {
    @Override
    public String toString()
    {
      return "'='";
    }
  }

  /**
   * A type "token".
   *
   * @param lexical The lexical position
   * @param value   The token value
   */

  record Token(
    LexicalPosition<URI> lexical,
    String value)
    implements MimeTokenType
  {
    @Override
    public String toString()
    {
      return this.value;
    }
  }

  /**
   * A quoted type "token".
   *
   * @param lexical The lexical position
   * @param value   The token value
   */

  record Quoted(
    LexicalPosition<URI> lexical,
    String value)
    implements MimeTokenType
  {
    @Override
    public String toString()
    {
      return "\"%s\"".formatted(this.value);
    }
  }

  /**
   * An invalid token.
   *
   * @param lexical The lexical position
   * @param value   The token value
   */

  record Invalid(
    LexicalPosition<URI> lexical,
    String value)
    implements MimeTokenType
  {
    @Override
    public String toString()
    {
      return "[Invalid token %s]".formatted(this.value);
    }
  }
}
