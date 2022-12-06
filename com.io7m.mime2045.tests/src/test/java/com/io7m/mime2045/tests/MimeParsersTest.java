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

import com.io7m.mime2045.core.MimeCharacters;
import com.io7m.mime2045.core.MimeType;
import com.io7m.mime2045.core.MimeTypeParameter;
import com.io7m.mime2045.parser.MimeParsers;
import com.io7m.mime2045.parser.api.MimeParseException;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.arbitraries.ListArbitrary;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MimeParsersTest
{
  @Test
  public void testTextPlain()
    throws Exception
  {
    final var textPlain =
      MimeType.of("text", "plain");

    final var parsed =
      new MimeParsers().parse("text/plain");

    assertEquals(textPlain, parsed);
    assertEquals("text/plain", textPlain.toString());
  }

  @Test
  public void testTextPlainUTF8()
    throws Exception
  {
    final var textPlain =
      new MimeType(
        "text",
        "plain",
        List.of(new MimeTypeParameter("encoding", "utf-8"))
      );

    final var parsed =
      new MimeParsers().parse("text/plain;encoding=utf-8");

    assertEquals(textPlain, parsed);
    assertEquals("text/plain;encoding=utf-8", textPlain.toString());
  }

  @Provide(value = "TypeName")
  private Arbitrary<String> typeNames()
  {
    return Arbitraries.strings()
      .ascii()
      .ofMinLength(1)
      .filter(s -> {
        return s.chars()
          .allMatch(ch -> MimeCharacters.characterIsAllowedBare((char) ch));
      });
  }

  @Provide(value = "Values")
  private Arbitrary<String> values()
  {
    return Arbitraries.strings()
      .ascii()
      .ofMinLength(1)
      .filter(s -> {
        return s.chars()
          .allMatch(ch -> MimeCharacters.characterIsAllowedQuoted((char) ch));
      });
  }

  @Provide(value = "TypeParameters")
  private Arbitrary<MimeTypeParameter> typeParameters()
  {
    final var names =
      this.typeNames();
    final var values =
      this.values();

    return Combinators.combine(names, values)
      .as(MimeTypeParameter::new);
  }

  @Provide(value = "TypeParameterLists")
  private ListArbitrary<MimeTypeParameter> typeParameterLists()
  {
    return this.typeParameters().list();
  }

  @Property
  public void testParseIdentity(
    final @ForAll(value = "TypeName") String type,
    final @ForAll(value = "TypeName") String subtype)
    throws MimeParseException
  {
    final var expected =
      MimeType.of(type, subtype);

    assertEquals(
      expected,
      new MimeParsers().parse(expected.toString())
    );
  }

  @Property
  public void testParseIdentityWithParameters(
    final @ForAll(value = "TypeName") String type,
    final @ForAll(value = "TypeName") String subtype,
    final @ForAll(value = "TypeParameterLists") List<MimeTypeParameter> parameters)
    throws MimeParseException
  {
    final var expected =
      new MimeType(type, subtype, parameters);
    final var serialized =
      expected.toString();
    final var parsed =
      new MimeParsers().parse(serialized);

    assertEquals(expected.type(), parsed.type());
    assertEquals(expected.subtype(), parsed.subtype());

    final var expectedParameters =
      expected.parameters();
    final var parsedParameters =
      parsed.parameters();

    assertEquals(expectedParameters, parsedParameters);
    assertEquals(expected, parsed);
  }

  @Test
  public void testCase0()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append("text/plain;");
    text.append('!');
    text.append('=');
    text.append('"');
    text.append('\\');
    text.append('"');
    text.append('"');

    final var parsed =
      new MimeParsers().parse(text.toString());
  }

  @Test
  public void testErrorSpecific0()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append("text/plain;");
    text.append('a');
    text.append('=');
    text.append('=');

    assertThrows(MimeParseException.class, () -> {
      new MimeParsers().parse(text.toString());
    });
  }

  @Test
  public void testErrorSpecific1()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append("text/plain;");
    text.append('a');
    text.append('=');
    text.append('\0');

    assertThrows(MimeParseException.class, () -> {
      new MimeParsers().parse(text.toString());
    });
  }

  @Test
  public void testErrorSpecific2()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append("text/plain;");
    text.append('a');
    text.append('/');
    text.append('b');
    text.append(';');
    text.append('x');
    text.append('=');
    text.append('y');
    text.append('\0');

    assertThrows(MimeParseException.class, () -> {
      new MimeParsers().parse(text.toString());
    });
  }

  @Test
  public void testErrorSpecific3()
    throws Exception
  {
    final var text = new StringBuilder(128);
    text.append("text/plain\0");

    assertThrows(MimeParseException.class, () -> {
      new MimeParsers().parse(text.toString());
    });
  }
}
