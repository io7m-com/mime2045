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

package com.io7m.mime2045.core;

import java.util.Comparator;
import java.util.Objects;

/**
 * A type parameter.
 *
 * @param name  The parameter name
 * @param value The parameter value
 */

public record MimeTypeParameter(
  String name,
  String value)
  implements Comparable<MimeTypeParameter>
{
  /**
   * A type parameter.
   *
   * @param name  The parameter name
   * @param value The parameter value
   */

  public MimeTypeParameter
  {
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(value, "value");

    for (final var ch : name.toCharArray()) {
      if (!MimeCharacters.characterIsAllowedBare(ch)) {
        throw new MimeValidityException(
          "Parameter name '%s' contains disallowed character '%s' (0x%s)"
            .formatted(
              name,
              Character.valueOf(ch),
              Integer.toUnsignedString((int) ch, 16))
        );
      }
    }

    for (final var ch : value.toCharArray()) {
      if (!MimeCharacters.characterIsAllowedQuoted(ch)) {
        throw new MimeValidityException(
          "Parameter value '%s' contains disallowed character '%s' (0x%s)"
            .formatted(
              name,
              Character.valueOf(ch),
              Integer.toUnsignedString((int) ch, 16))
        );
      }
    }
  }

  private static String quoteIfNecessary(
    final String value)
  {
    final var text = new StringBuilder(value.length());
    boolean quote = false;
    for (final var ch : value.toCharArray()) {
      quote = quote || !MimeCharacters.characterIsAllowedBare(ch);

      switch (ch) {
        case '"', '\\', ' ' -> text.append('\\');
        default -> {

        }
      }

      text.append(ch);
    }

    if (quote) {
      return "\"%s\"".formatted(text);
    }
    return text.toString();
  }

  @Override
  public String toString()
  {
    return ";%s=%s".formatted(this.name, quoteIfNecessary(this.value));
  }

  @Override
  public int compareTo(
    final MimeTypeParameter other)
  {
    return Comparator.comparing(MimeTypeParameter::name)
      .thenComparing(MimeTypeParameter::value)
      .compare(this, other);
  }
}
