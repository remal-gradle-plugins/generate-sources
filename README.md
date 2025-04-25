**Tested on Java LTS versions from <!--property:java-runtime.min-version-->11<!--/property--> to <!--property:java-runtime.max-version-->21<!--/property-->.**

**Tested on Gradle versions from <!--property:gradle-api.min-version-->7.1<!--/property--> to <!--property:gradle-api.max-version-->8.14<!--/property-->.**

# `name.remal.generate-sources` plugin

[![configuration cache: supported from v2](https://img.shields.io/static/v1?label=configuration%20cache&message=supported%20from%20v2&color=success)](https://docs.gradle.org/current/userguide/configuration_cache.html)

<!--plugin-usage:name.remal.generate-sources-->
```groovy
plugins {
    id 'name.remal.generate-sources' version '2.0.0-rc-3'
}
```
<!--/plugin-usage-->

&nbsp;

This plugin simplifies sources generation for JVM projects.
It helps with registering all the necessary dependencies and makes generated sources visible to an IDE.

Basic configuration looks like this:

```groovy
generateSources {
  forMainSourceSet {
    java { /* ... */ } // configure Java sources generation
    resources { /* ... */ } // configure resources generation
    groovy { /* ... */ } // configure Groovy sources generation
  }

  forSourceSet(sourceSets.test) { /* ... */ } // to generate sources for other source sets (`test` in this case)
}
```

## Generate Java sources

This configuration

```groovy
generateSources.forMainSourceSet.java {
  classFile('pkg', 'Logic') {
    addStaticImport('java.util.Arrays', 'asList')
    addImport('java.util.List')
    block("public class ${simpleName}") {
      line()
      suppressWarningsLine('unchecked', 'rawtypes')
      block('public static List execute()') {
        line('return asList(')
        ident {
          lines(
            "\"${escapeString('multi\bline\nstring')}\"", // `escapeString` will escape Java string
            '2',
          )
        }
        line(');')
      }
      line()
    }
  }
}
```

... generates `build/generated/generateJava/pkg/Logic.java` file:

```java
package pkg;

import static java.util.Arrays.asList;

import java.util.List;

public class Logic {

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static List execute() {
    return asList(
      "multi\bline\nstring",
      2
    );
  }

}
```

The generated file will be compiled by the `compileJava` task.

## Generate text resources

This configuration

```groovy
generateSources.forMainSourceSet.resources {
  textFile('file.txt') {
    line('This is a text file')
    line('with many lines')
  }
}
```

... generates `build/generated/generateResources/file.txt` file:

```
This is a text file
with many lines
```

The generated file will be processed by the `processResources` task.

The delegate object of the `generateSources.forMainSourceSet.resources.textFile {}` closure
is an extended [`java.io.BufferedWriter`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/BufferedWriter.html) with these additional methods:

* `writeFormat(String format, Object... args)` - creates a string using [`String.format()`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html#format%28java.lang.String,java.lang.Object...%29) and passes it to the [`write()`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/Writer.html#write%28java.lang.String%29)
* `copyFrom(java.io.Reader reader)` - copy the content from the [reader](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/Reader.html)
* `newLine()` - overrides [`java.io.BufferedWriter.newLine()`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/BufferedWriter.html#newLine%28%29) by writing a line separator from `.editorconfig` or `\n` by default (the default implementation writes system line separator that depend on the current OS)
* `line(String line)` - [`write(line)`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/BufferedWriter.html#write%28java.lang.String%29) + `newLine()`
* `line(String format, Object... args)` - creates a string using [`String.format()`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html#format%28java.lang.String,java.lang.Object...%29) and passes it to `line()`
* `line()` - executes `newLine()`

The goals of the additional methods are to simplify the usage of `java.io.BufferedWriter` (by adding `writeFormat()` and `copyFrom()` methods)
and to make usage experience consistent with classes generation (by adding `line()` methods).

## Generate binary resources

This configuration

```groovy
generateSources.forMainSourceSet.resources {
  binaryFile('file.bin') {
    write([1, 2, 3] as byte[])
  }
}
```

... generates `build/generated/generateResources/file.bin` file with the content of three bytes: `1`, `2`, and `3`.

The generated file will be processed by the `processResources` task.

The delegate object of the `generateSources.forMainSourceSet.resources.binaryFile {}` closure
is an extended [`java.io.BufferedOutputStream`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/BufferedOutputStream.html) with this additional method:

* `copyFrom(java.io.InputStream inputStream)` - copy the content from the [input stream](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/InputStream.html)

The goal of the additional method is to simplify the usage of [`java.io.BufferedOutputStream`](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/BufferedOutputStream.html) (by adding `copyFrom()` method).

## Generate Groovy sources

This configuration

```groovy
generateSources.forMainSourceSet.groovy {
  classFile('pkg', 'Logic') {
    addStaticImport('java.util.Arrays', 'asList')
    addImport('java.util.List')
    block("class ${simpleName}") {
      line()
      suppressWarningsLine('unchecked', 'rawtypes')
      block('static List execute()') {
        line('return asList(')
        ident {
          lines(
            "\"${escapeString('multi\bline\nstring')}\"", // `escapeString` will escape Groovy string
            '2,',
          )
        }
        line(')')
      }
      line()
    }
  }
}
```

... generates `build/generated/generateGroovy/pkg/Logic.groovy` file:

```groovy
package pkg

import static java.util.Arrays.asList

class Logic {

  @SuppressWarnings(["unchecked", "rawtypes"])
  static List execute() {
    return asList(
      "multi\bline\nstring",
      2,
    )
  }

}
```

The generated file will be compiled by the `compileGroovy` task.

# Migration guide

## Version 1.* to 2.*

The plugin was fully rewritten. There were no intentions to keep it backward compatible.
