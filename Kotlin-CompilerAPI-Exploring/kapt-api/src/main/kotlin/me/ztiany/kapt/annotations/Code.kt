package me.ztiany.kapt.annotations

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Code(
    val author: String,
    val date: String
)
