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

import com.io7m.mime2045.core.MimeType;
import com.io7m.mime2045.core.MimeTypeParameter;
import com.io7m.mime2045.parser.api.MimeParseException;
import com.io7m.mime2045.parser.api.MimeParserType;
import com.io7m.mime2045.parser.internal.MimeTokenType.Equals;
import com.io7m.mime2045.parser.internal.MimeTokenType.Quoted;
import com.io7m.mime2045.parser.internal.MimeTokenType.Semicolon;
import com.io7m.mime2045.parser.internal.MimeTokenType.Slash;
import com.io7m.mime2045.parser.internal.MimeTokenType.Token;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The default parser.
 */

public final class MimeParser implements MimeParserType
{
  private final Map<String, String> options;

  /**
   * The default parser.
   *
   * @param inOptions The options
   */

  public MimeParser(
    final Map<String, String> inOptions)
  {
    this.options = Map.copyOf(inOptions);
  }

  private static MimeType parseWithLexer(
    final MimeLexer lexer)
    throws MimeParseException
  {
    final var type =
      exact(lexer, Token.class);

    exact(lexer, Slash.class);

    final var subtype =
      exact(lexer, Token.class);

    final var semi =
      optional(lexer, Semicolon.class);

    if (semi.isEmpty()) {
      return new MimeType(type.value(), subtype.value(), List.of());
    }

    lexer.pushBack(semi.get());
    return new MimeType(type.value(), subtype.value(), parseParameters(lexer));
  }

  private static List<MimeTypeParameter> parseParameters(
    final MimeLexer lexer)
    throws MimeParseException
  {
    final var parameters =
      new ArrayList<MimeTypeParameter>();

    while (true) {
      final var semi =
        exact(lexer, Semicolon.class);
      final var name =
        exact(lexer, Token.class);
      final var eq =
        exact(lexer, Equals.class);
      final var value =
        oneOf(lexer, Token.class, Quoted.class);

      final String valueText;
      if (value instanceof Token token) {
        valueText = token.value();
      } else if (value instanceof Quoted quoted) {
        valueText = quoted.value();
      } else {
        throw new IllegalStateException("Unreachable code.");
      }

      parameters.add(new MimeTypeParameter(name.value(), valueText));

      final var semiNext =
        optional(lexer, Semicolon.class);

      if (semiNext.isEmpty()) {
        return List.copyOf(parameters);
      }

      lexer.pushBack(semiNext.get());
    }
  }

  @SafeVarargs
  private static MimeTokenType oneOf(
    final MimeLexer lexer,
    final Class<? extends MimeTokenType>... classes)
    throws MimeParseException
  {
    final MimeTokenType token;
    try {
      token = lexer.token();
    } catch (final IOException e) {
      throw new MimeParseException(lexer.position(), e.getMessage(), e);
    }

    for (final var required : classes) {
      if (token.getClass().isAssignableFrom(required)) {
        return token;
      }
    }

    throw new MimeParseException(
      lexer.position(),
      "Syntax error: Expected one of [%s] but received: %s"
        .formatted(
          Arrays.stream(classes)
            .map(MimeTokenType::name)
            .collect(Collectors.joining(", ")),
          token)
    );
  }

  private static <T extends MimeTokenType> Optional<T> optional(
    final MimeLexer lexer,
    final Class<T> clazz)
    throws MimeParseException
  {
    final MimeTokenType t;
    try {
      t = lexer.token();
    } catch (final IOException e) {
      throw new MimeParseException(lexer.position(), e.getMessage(), e);
    }

    if (t instanceof MimeTokenType.EOF) {
      return Optional.empty();
    }

    if (!t.getClass().isAssignableFrom(clazz)) {
      throw new MimeParseException(
        t.lexical(),
        "Syntax error: Expected %s but received %s"
          .formatted(MimeTokenType.name(clazz), t)
      );
    }

    return (Optional<T>) Optional.of(t);
  }

  private static <T extends MimeTokenType> T exact(
    final MimeLexer lexer,
    final Class<T> clazz)
    throws MimeParseException
  {
    return (T) oneOf(lexer, clazz);
  }

  @Override
  public MimeType parse(
    final String text)
    throws MimeParseException
  {
    try (var reader = new StringReader(text)) {
      return parseWithLexer(new MimeLexer(reader));
    }
  }
}
