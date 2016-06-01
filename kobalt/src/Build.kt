import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.application.*
import com.beust.kobalt.plugin.publish.bintray

val p1 = project {
    name = "processor"
    directory = name

    dependencies {
        compile("com.squareup:javapoet:1.7.0")
    }

    assemble {
        jar {
        }
    }
}

val p2 = project(p1) {
    name = "sample"
    directory = name

    assemble {
        jar {
        }
    }

    application {
        mainClass = "com.example.MainKt"
    }
}
