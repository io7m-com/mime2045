/*
 * Copyright © 2024 Mark Raynsford <code@io7m.com> https://www.io7m.com
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


package com.io7m.mime2045.fileext;

import com.io7m.mime2045.core.MimeType;
import com.io7m.mime2045.fileext.internal.MimeFileExtensionsGenerated;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Functions to suggest file extensions for MIME types.
 *
 * @since 1.1.0
 */

public final class MimeFileExtensions
{
  private MimeFileExtensions()
  {

  }

  /**
   * Suggest a file extension for the MIME type.
   *
   * @param type The type
   *
   * @return The extension, if any
   */

  public static Optional<String> suggestFileExtension(
    final MimeType type)
  {
    Objects.requireNonNull(type, "type");
    return Optional.ofNullable(MimeFileExtensionsGenerated.get(type.toString()));
  }

  /**
   * @return The set of types that have file extensions
   */

  public static Set<String> typesWithFileExtensions()
  {
    return MimeFileExtensionsGenerated.known();
  }
}
