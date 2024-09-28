mime2045
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.mime2045/com.io7m.mime2045.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.mime2045%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/com.io7m.mime2045/com.io7m.mime2045?server=https%3A%2F%2Fs01.oss.sonatype.org&style=flat-square)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/io7m/mime2045/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m-com/mime2045.svg?style=flat-square)](https://codecov.io/gh/io7m-com/mime2045)
![Java Version](https://img.shields.io/badge/17-java?label=java&color=e65cc3)

![com.io7m.mime2045](./src/site/resources/mime2045.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/mime2045/main.linux.temurin.current.yml)](https://www.github.com/io7m-com/mime2045/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/mime2045/main.linux.temurin.lts.yml)](https://www.github.com/io7m-com/mime2045/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/mime2045/main.windows.temurin.current.yml)](https://www.github.com/io7m-com/mime2045/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/mime2045/main.windows.temurin.lts.yml)](https://www.github.com/io7m-com/mime2045/actions?query=workflow%3Amain.windows.temurin.lts)|

## mime2045

The `mime2045` package provides functions for parsing RFC 2045 MIME types.

## Features

* RFC 2045 MIME type parsing.
* Suggest file extensions for MIME types.
* High coverage test suite.
* [OSGi-ready](https://www.osgi.org/).
* [JPMS-ready](https://en.wikipedia.org/wiki/Java_Platform_Module_System).
* ISC license.

## Usage

Declare MIME types directly:

```
final var textPlain =
  MimeType.of("text", "plain");
```

Parse MIME type strings:

```
final var parsed =
  new MimeParsers().parse("text/plain");

assert textPlain.equals(parsed);
```

Suggest file extensions for MIME types:

```
final var parsed =
  new MimeParsers().parse("text/plain");

assert Optional.of("txt").equals(MimeFileExtensions.suggestFileExtension(parsed));
```

