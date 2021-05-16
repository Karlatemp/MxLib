# MxLib

[![Maven Central](https://img.shields.io/maven-central/v/io.github.karlatemp.mxlib/mxlib-api.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:io.github.karlatemp.mxlib)

## Use in gradle

```groovy
repositories {
    mavenCentral()
}

dependencies {
    def mxlib = { artifact ->
        return "io.github.karlatemp.mxlib:mxlib-$artifact:3.0-dev-10"
    }

    implementation(mxlib('api'))
    implementation(mxlib('common'))
    implementation(mxlib('logger'))
    // .....
}

```
