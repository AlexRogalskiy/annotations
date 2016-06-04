package com.example

import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeSpec

import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import java.io.IOException
import java.util.LinkedList

class Processor : AbstractProcessor() {

    private lateinit var filer: Filer
    private lateinit var messager: Messager

    override fun getSupportedAnnotationTypes() = setOf(Example::class.qualifiedName)

    override fun getSupportedSourceVersion() = SourceVersion.latestSupported()

    override fun process(annotations: Set<TypeElement>, roundEnv: RoundEnvironment): Boolean {

        val annotatedElements = LinkedList<Element>()

        for (annotatedElement in roundEnv.getElementsAnnotatedWith(Example::class.java)) {
            if (annotatedElement.kind != ElementKind.CLASS) {
                error(annotatedElement, "Only classes can be annotated with @%s", Example::class.java.simpleName)
                continue
            }
            annotatedElements.add(annotatedElement)
        }

        if (annotatedElements.size > 0) {

            var methodBuilder: MethodSpec.Builder = MethodSpec.methodBuilder("sayHello").addModifiers(Modifier.PUBLIC).returns(Void.TYPE)

            for (e in annotatedElements) {
                val msg = e.getAnnotation(Example::class.java).text
                methodBuilder = methodBuilder.addStatement("\$T.out.println(\$S)", System::class.java, msg)
            }

            val method = methodBuilder.build()

            val clazz = TypeSpec.classBuilder("GeneratedClass").addModifiers(Modifier.PUBLIC, Modifier.FINAL).addMethod(method).build()

            val file = JavaFile.builder("com.example", clazz).build()

            try {
                file.writeTo(filer)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }

        return true
    }

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        filer = processingEnv.filer
        messager = processingEnv.messager
    }

    private fun error(element: Element, msg: String, vararg args: Any) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, *args), element)
    }
}

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.CLASS)
annotation class Example(val text: String)