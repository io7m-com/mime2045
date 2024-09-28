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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public final class MimeGenerate
{
  private MimeGenerate()
  {

  }

  public static void main(
    final String[] args)
    throws Exception
  {
    final var mimeFile =
      Paths.get(args[0])
        .toAbsolutePath();
    final var outputFile =
      Paths.get(args[1])
        .toAbsolutePath();

    try (var lineStream = Files.lines(mimeFile, UTF_8)) {
      final var lines =
        lineStream.map(String::trim)
          .filter(s -> !s.startsWith("#"))
          .filter(s -> !s.isBlank())
          .sorted()
          .toList();

      Files.createDirectories(outputFile.getParent());

      try (var writer = Files.newBufferedWriter(outputFile, UTF_8)) {
        writer.append("package com.io7m.mime2045.fileext.internal;");
        writer.newLine();
        writer.append("import java.util.HashMap;");
        writer.newLine();
        writer.append("import java.util.Map;");
        writer.newLine();
        writer.append("import java.util.Set;");
        writer.newLine();
        writer.append("public final class MimeFileExtensionsGenerated {");
        writer.newLine();
        writer.append("  private static final Map<String, String> TYPES;");
        writer.newLine();
        writer.append("  static {");
        writer.newLine();
        writer.append("    TYPES = new HashMap<>();");
        writer.newLine();

        for (final var line : lines) {
          final var segments =
            List.of(line.split("\\s+"));

          if (segments.size() >= 2) {
            writer.append("    TYPES.put(\"%s\", \"%s\");".formatted(
              segments.get(0),
              segments.get(1)
            ));
            writer.newLine();
          }
        }

        writer.append("  }");
        writer.newLine();
        writer.append("  public static String get(String x) { return TYPES.get(x); }");
        writer.newLine();
        writer.append("  public static Set<String> known() { return TYPES.keySet(); }");
        writer.newLine();
        writer.append("}");
        writer.newLine();
        writer.flush();
      }
    }
  }
}
