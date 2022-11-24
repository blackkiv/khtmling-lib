package com.github.blck.khtmling

import com.github.blck.khtmling.enums.HtmlTag
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties

fun Any.htmling(): String = buildString { htmlingObject(this@htmling) }

private fun StringBuilder.htmlingObject(obj: Any) {
    val kClass = obj.javaClass.kotlin
    val (prefix, postfix) = kClass.prefixToPostfix(kClass.simpleName)
    kClass.memberProperties.filter { ! it.hasAnnotation<HtmlIgnore>() }
            .joinToStringBuilder(
                this, prefix = prefix, postfix = postfix
            ) {
                htmling(it, obj)
            }
}

private fun <T> Iterable<T>.joinToStringBuilder(
    stringBuilder: StringBuilder,
    separator: CharSequence = "",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = - 1,
    truncated: CharSequence = "...",
    callback: ((T) -> Unit)? = null
): StringBuilder {
    return joinTo(stringBuilder, separator, prefix, postfix, limit, truncated) {
        if (callback == null) return@joinTo it.toString()
        callback(it)
        ""
    }
}

private fun KAnnotatedElement.prefixToPostfix(defaultName: String?): Pair<String, String> {
    val tag = this.resolveHtmlTag()
    val styles = this.resolveStyles()
    val classes = this.resolveClasses()
    val name = this.resolveName(defaultName)

    val prefix = """
            <${tag} 
            ${if (name != null) "title=\"${name.lowercase()}\"" else ""} 
            ${
        styles?.toList()
                .htmlingStyles()
    }
            ${
        classes?.toList()
                .htmlingClasses()
    }
            >
        """.replace(Regex("\\s{2,}"), " ")
            .trimIndent()
    val postfix = "</${tag}>"

    return prefix to postfix
}

private fun List<HtmlStyle>?.htmlingStyles() = this?.joinToString(
    prefix = "style=\"", postfix = "\"", separator = ";"
) {
    it.toHtml()
}
    ?: ""

private fun List<String>?.htmlingClasses() = this?.joinToString(
    prefix = "class=\"", postfix = "\"", separator = " "
) {
    it
}

private fun HtmlStyle.toHtml(): String = """${this.property}: ${this.value}"""

private fun KAnnotatedElement.resolveHtmlTag(): HtmlTag = this.findAnnotation<HtmlProperty>()?.tag
    ?: HtmlTag.DIV

private fun KAnnotatedElement.resolveStyles(): Array<HtmlStyle>? =
    this.findAnnotation<HtmlProperty>()?.styles

private fun KAnnotatedElement.resolveName(defaultName: String?): String? =
    this.findAnnotation<HtmlProperty>()?.name
        ?: defaultName

private fun KAnnotatedElement.resolveClasses(): Array<String>? =
    this.findAnnotation<HtmlProperty>()?.classes

private fun StringBuilder.htmling(property: KProperty1<Any, *>, obj: Any) {
    val (prefix, postfix) = property.prefixToPostfix(property.name)
    append(prefix)
    htmlingValue(property.get(obj))
    append(postfix)
}

private fun StringBuilder.htmlingValue(value: Any?) {
    when (value) {
        null                  -> append("null")
        is String             -> append(value)
        is Number, is Boolean -> append(value.toString())
        is Iterable<*>        -> htmlingIterable(value)
        else                  -> htmlingObject(value)
    }
}

private fun StringBuilder.htmlingIterable(iterable: Iterable<Any?>) {
    iterable.joinToStringBuilder(this) {
        htmlingValue(it)
    }
}
