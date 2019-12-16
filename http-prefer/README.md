# [RFC 7240: *Prefer Header for HTTP*](https://tools.ietf.org/html/rfc7240)

[![Javadoc](http://javadoc.io/badge/io.github.whiskeysierra/http-prefer.svg)](http://www.javadoc.io/doc/io.github.whiskeysierra/http-prefer)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.whiskeysierra/http-prefer.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.whiskeysierra/http-prefer)

Put a meaningful, short, plain-language description of what
this project is trying to accomplish and why it matters.
Describe the problem(s) this project solves.
Describe how this software can improve the lives of its audience.

- **Technology stack**: Java, HTTP, server-side

## Example

```java
Prefer prefer = Prefer.value(request.getHeader(Prefer.PREFER));

if (prefer.applies(Prefer.RESPOND_ASYNC)) {
    // TODO implement
}

response.setHeader(Prefer.PREFERENCE_APPLIED, prefer.applied());
```

## Features

- 


## Dependencies

- Java 8

## Installation

Add the following dependency to your project:

```xml
<dependency>
    <groupId>io.github.whiskeysierra</groupId>
    <artifactId>http-prefer</artifactId>
    <version>${http-toolbox.version}</version>
</dependency>
```

## Usage

### Classic

```java
Prefer prefer = Prefer.value(request.getHeader(Prefer.PREFER));
```

### Spring Web

```java
@RequestHeader(name = Prefer.PREFER, required = false, defaultValue = "") Prefer prefer
```
