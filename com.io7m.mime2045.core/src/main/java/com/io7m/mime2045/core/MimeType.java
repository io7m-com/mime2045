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

import java.util.List;
import java.util.Objects;

/**
 * An RFC 2045 content type.
 *
 * @param type       The type
 * @param subtype    The subtype
 * @param parameters The parameters
 */

public record MimeType(
  String type,
  String subtype,
  List<MimeTypeParameter> parameters)
{
  /**
   * An RFC 2045 content type.
   *
   * @param type       The type
   * @param subtype    The subtype
   * @param parameters The parameters
   */

  public MimeType(
    final String type,
    final String subtype,
    final List<MimeTypeParameter> parameters)
  {
    this.type =
      Objects.requireNonNull(type, "type");
    this.subtype =
      Objects.requireNonNull(subtype, "subtype");
    this.parameters =
      Objects.requireNonNull(parameters, "parameters")
        .stream()
        .sorted()
        .toList();
  }

  /**
   * Construct a type without parameters.
   *
   * @param type    The type
   * @param subtype The subtype
   *
   * @return The type
   */

  public static MimeType of(
    final String type,
    final String subtype)
  {
    return new MimeType(type, subtype, List.of());
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder(32);
    sb.append(this.type);
    sb.append('/');
    sb.append(this.subtype);

    for (final var parameter : this.parameters) {
      sb.append(parameter);
    }
    return sb.toString();
  }
}
