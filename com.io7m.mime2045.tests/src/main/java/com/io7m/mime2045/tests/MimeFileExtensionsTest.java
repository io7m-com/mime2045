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


package com.io7m.mime2045.tests;

import com.io7m.mime2045.fileext.MimeFileExtensions;
import com.io7m.mime2045.parser.MimeParsers;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class MimeFileExtensionsTest
{
  @Test
  public void testKnown()
  {
    assertNotEquals(0, MimeFileExtensions.typesWithFileExtensions().size());
  }

  @Test
  public void testAllExtensions()
    throws Exception
  {
    final var parsers = new MimeParsers();

    for (final var typeText : MimeFileExtensions.typesWithFileExtensions()) {
      final var type = parsers.parse(typeText);
      assertNotEquals(
        Optional.empty(),
        MimeFileExtensions.suggestFileExtension(type));
    }
  }
}
