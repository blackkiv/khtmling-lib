package com.github.blck.khtmling

import com.github.blck.khtmling.enums.CssProperty
import com.github.blck.khtmling.enums.HtmlTag


@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD
)
annotation class HtmlIgnore

annotation class HtmlStyle(
    val property: CssProperty,
    val value: String
)

@Target(
    AnnotationTarget.CLASS,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FIELD
)
annotation class HtmlProperty(
    val name: String = "",
    val tag: HtmlTag = HtmlTag.DIV,
    val styles: Array<HtmlStyle> = [],
    val classes: Array<String> = []
)
