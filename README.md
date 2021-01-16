# MxLib

[ ![Download](https://api.bintray.com/packages/karlatemp/misc/MXLib/images/download.svg) ](https://bintray.com/karlatemp/misc/MXLib/_latestVersion)

Package location: [bintray/Karlatemp/MXLib](https://bintray.com/karlatemp/misc/MXLib)

## Use in gradle

```groovy
repositories {
    jcenter() // Add the jcenter repo
}

dependencies {
    def mxlib = { artifact ->
        return "io.github.karlatemp.mxlib:mxlib-$artifact:3.0-dev-9"
    }

    implementation(mxlib('api'))
    implementation(mxlib('common'))
    implementation(mxlib('logger'))
    // .....
}

```
