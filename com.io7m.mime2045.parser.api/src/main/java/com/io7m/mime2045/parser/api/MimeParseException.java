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


package com.io7m.mime2045.parser.api;

import com.io7m.jlexing.core.LexicalPosition;
import com.io7m.jlexing.core.LexicalType;

import java.net.URI;
import java.util.Objects;

/**
 * An exception caused by a parse error.
 */

public final class MimeParseException
  extends Exception implements LexicalType<URI>
{
  private final LexicalPosition<URI> lexical;

  /**
   * An exception caused by a parse error.
   *
   * @param message  The error message
   * @param position The lexical position
   */

  public MimeParseException(
    final LexicalPosition<URI> position,
    final String message)
  {
    super(Objects.requireNonNull(message, "message"));
    this.lexical = Objects.requireNonNull(position, "position");
  }

  /**
   * An exception caused by a parse error.
   *
   * @param cause    The cause
   * @param message  The error message
   * @param position The lexical position
   */

  public MimeParseException(
    final LexicalPosition<URI> position,
    final String message,
    final Throwable cause)
  {
    super(
      Objects.requireNonNull(message, "message"),
      Objects.requireNonNull(cause, "cause")
    );
    this.lexical = Objects.requireNonNull(position, "position");
  }

  /**
   * An exception caused by a parse error.
   *
   * @param cause    The cause
   * @param position The lexical position
   */

  public MimeParseException(
    final LexicalPosition<URI> position,
    final Throwable cause)
  {
    super(Objects.requireNonNull(cause, "cause"));
    this.lexical = Objects.requireNonNull(position, "position");
  }

  @Override
  public LexicalPosition<URI> lexical()
  {
    return this.lexical;
  }
}
