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


package com.io7m.mime2045.parser;

import com.io7m.mime2045.parser.api.MimeParserFactoryType;
import com.io7m.mime2045.parser.api.MimeParserType;
import com.io7m.mime2045.parser.internal.MimeParser;

import java.util.Map;

/**
 * The default factory of parsers.
 */

public final class MimeParsers implements MimeParserFactoryType
{
  /**
   * The default factory of parsers.
   */

  public MimeParsers()
  {

  }

  @Override
  public MimeParserType create(
    final Map<String, String> options)
  {
    return new MimeParser(options);
  }
}
