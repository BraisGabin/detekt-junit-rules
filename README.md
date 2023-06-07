# Detekt JUnit rules

Rules to avoid common mistakes while using JUnit4 or JUnit5

## How to use it

Add this to your module gradle configuration:

```kotlin
dependencies {
  detektPlugins("com.braisgabin.detekt:junit:0.0.6")
}
```

## Configuration

If you use detekt `1.21.0` or later all works out of the box. If you don't you should copy the content of [this file][config.yml]
inside your detekt configuration to make able to run the rules.

  [config.yml]: https://github.com/BraisGabin/detekt-junit-rules/blob/main/src/main/resources/config/config.yml
