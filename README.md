# Gradle build time logger

[![CircleCI](https://circleci.com/gh/Skyscanner/gradle-time-logger-plugin.svg?style=shield)](https://circleci.com/gh/Skyscanner/gradle-time-logger-plugin)

Do you want to know how much time it takes for you and your team to build? Now you can.

## Usage

### Gradle 

```groovy
plugins {
    id 'net.skyscanner.gradletimelogger' version '1.0.0'
}

buildTimeLogger {
    mixpanelProjectToken = "<your mixpanel token>"
    reports = [Report.CONSOLE, Report.MIXPANEL]
}
```

### Kotlin DSL for Gradle

```kotlin
plugins {
    id("net.skyscanner.gradletimelogger") version "1.0.0"
}

buildTimeLogger {
    mixpanelProjectToken = "<your mixpanel token>"
    reports = setOf(Report.CONSOLE, Report.MIXPANEL)
}
```

For a complete example, check https://github.com/skyscanner/gradle-time-logger-plugin/tree/master/example

## Console reporter

The console reporter just dumps all info to the `lifecycle` logger 

## Mixpanel reporter

The Mixpanel reporter tracks the following properties:

### All events

| Property                  | Type   | Example                                   |
|---------------------------|--------|-------------------------------------------|
| buildAction               | String | Build                                     |
| gradleVersion             | String | 5.1.1                                     |
| gradleProjectName         | String | example                                   |
| cpuIdentifier             | String | Intel(R) Core(TM) i9-8950HK CPU @ 2.90GHz |
| hostname                  | String | GuillermosMacbookPro                      |
| localMidnightUTCTimestamp | Number | 1551312000                                |
| maxMemory                 | Number | 34359738368                               |
| osIdentifier              | String | Mac OS X 10.14.1 x86_64                   |
| totalElapsedBuildTimeMs   | Number | 470                                       |

### Build 

Event generated once per build.

| Property     | Type    | Example                                     |
|--------------|---------|---------------------------------------------|
| buildFailed  | Boolean | true                                        |
| buildFailure | String  | Execution failed for task ':compileKotlin'. |

### BuildTask 

Event generated once per task in the build graph.

| Property    | Type    | Example        |
|-------------|---------|----------------|
| buildTimeMs | Number  | 334            |
| task        | String  | :compileKotlin |
| successs    | Boolean | false          |
| didWork     | Boolean | true           |
| skipped     | Boolean | false          |
| skipMessage | String  |                |
| upToDate    | Boolean | false          |
| noSource    | Boolean | false          |

## Releasing a new version

Please look at [RELEASING.md](RELEASING.md)
