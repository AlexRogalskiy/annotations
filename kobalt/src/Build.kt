import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.assemble
import com.beust.kobalt.plugin.publish.bintray

val p1 = project {
    name = "processor"
    directory = name

    dependencies {
        compile("com.squareup:javapoet:1.7.0")
    }
}

val p2 = project(p1) {
    name = "sample"
    directory = name
}