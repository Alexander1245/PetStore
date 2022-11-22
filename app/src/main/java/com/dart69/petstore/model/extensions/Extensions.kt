package com.dart69.petstore.model.extensions

fun <T> T.use(block: T.() -> Unit) = block(this)

infix fun <T, R> T.then(right: R): R = right

fun <T> MutableCollection<T>.clearIfNotEmpty(): Boolean =
    if(isEmpty()) false else clear() then true