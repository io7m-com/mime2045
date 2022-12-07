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

import java.util.HashSet;
import java.util.Set;

/**
 * Characters that can appear in types and parameters.
 */

public final class MimeCharacters
{
  private MimeCharacters()
  {

  }

  private static final Set<Character> ALLOWED_BARE =
    Set.of(
      Character.valueOf('!'),
      Character.valueOf('#'),
      Character.valueOf('$'),
      Character.valueOf('%'),
      Character.valueOf('&'),
      Character.valueOf('\''),
      Character.valueOf('*'),
      Character.valueOf('+'),
      Character.valueOf('-'),
      Character.valueOf('.'),
      Character.valueOf('0'),
      Character.valueOf('1'),
      Character.valueOf('2'),
      Character.valueOf('3'),
      Character.valueOf('4'),
      Character.valueOf('5'),
      Character.valueOf('6'),
      Character.valueOf('7'),
      Character.valueOf('8'),
      Character.valueOf('9'),
      Character.valueOf('A'),
      Character.valueOf('B'),
      Character.valueOf('C'),
      Character.valueOf('D'),
      Character.valueOf('E'),
      Character.valueOf('F'),
      Character.valueOf('G'),
      Character.valueOf('H'),
      Character.valueOf('I'),
      Character.valueOf('J'),
      Character.valueOf('K'),
      Character.valueOf('L'),
      Character.valueOf('M'),
      Character.valueOf('N'),
      Character.valueOf('O'),
      Character.valueOf('P'),
      Character.valueOf('Q'),
      Character.valueOf('R'),
      Character.valueOf('S'),
      Character.valueOf('T'),
      Character.valueOf('U'),
      Character.valueOf('V'),
      Character.valueOf('W'),
      Character.valueOf('X'),
      Character.valueOf('Y'),
      Character.valueOf('Z'),
      Character.valueOf('^'),
      Character.valueOf('_'),
      Character.valueOf('`'),
      Character.valueOf('a'),
      Character.valueOf('b'),
      Character.valueOf('c'),
      Character.valueOf('d'),
      Character.valueOf('e'),
      Character.valueOf('f'),
      Character.valueOf('g'),
      Character.valueOf('h'),
      Character.valueOf('i'),
      Character.valueOf('j'),
      Character.valueOf('k'),
      Character.valueOf('l'),
      Character.valueOf('m'),
      Character.valueOf('n'),
      Character.valueOf('o'),
      Character.valueOf('p'),
      Character.valueOf('q'),
      Character.valueOf('r'),
      Character.valueOf('s'),
      Character.valueOf('t'),
      Character.valueOf('u'),
      Character.valueOf('v'),
      Character.valueOf('w'),
      Character.valueOf('x'),
      Character.valueOf('y'),
      Character.valueOf('z'),
      Character.valueOf('{'),
      Character.valueOf('|'),
      Character.valueOf('}'),
      Character.valueOf('~')
    );

  private static final Set<Character> TSPECIALS =
    Set.of(
      Character.valueOf('('),
      Character.valueOf(')'),
      Character.valueOf('<'),
      Character.valueOf('>'),
      Character.valueOf('@'),
      Character.valueOf(','),
      Character.valueOf(';'),
      Character.valueOf(':'),
      Character.valueOf('\\'),
      Character.valueOf('"'),
      Character.valueOf('/'),
      Character.valueOf('['),
      Character.valueOf(']'),
      Character.valueOf('?'),
      Character.valueOf('=')
    );

  private static final Set<Character> ALLOWED_QUOTED;

  static {
    final var quoted = new HashSet<Character>();
    quoted.addAll(ALLOWED_BARE);
    quoted.addAll(TSPECIALS);
    ALLOWED_QUOTED = Set.copyOf(quoted);
  }

  /**
   * Characters that can appear in unquoted tokens.
   *
   * @param ch The character
   *
   * @return {@code true} if the character can appear in an unquoted token
   */

  public static boolean characterIsAllowedBare(
    final char ch)
  {
    return ALLOWED_BARE.contains(Character.valueOf(ch));
  }

  /**
   * Characters that can appear in quoted tokens.
   *
   * @param ch The character
   *
   * @return {@code true} if the character can appear in a quoted token
   */

  public static boolean characterIsAllowedQuoted(
    final char ch)
  {
    return ALLOWED_QUOTED.contains(Character.valueOf(ch));
  }
}
