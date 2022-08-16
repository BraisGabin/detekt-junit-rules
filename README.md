# Detekt JUnit rules

Rules to avoid common mistakes while using JUnit4 or JUnit5

## How to use it

Add this to your module gradle configuration:

```kotlin
dependencies {
  detektPlugins("com.braisgabin.detekt:junit:0.0.4")
}
```

Right now the project is on [jitpack]. So you need to have that repo configured too.

## Configuration

Remember that, by default, all the rules aren't enable. To activate the rules you need to write something like this in your
yaml configuration:

```yaml
## This should be set at the top of your config:
config:
  # ...
  excludes: "JUnit"

JUnit:
  TestFunctionsShouldReturnUnit:
    active: true
  MissingTestAnnotation:
    active: true
```

  [jitpack]: https://jitpack.io/#BraisGabin/detekt-junit-rules
