package me.ztiany.kotlin.newf.kt14_3


// In Kotlin 1.4.30, we're shipping the prototype of sealed interfaces. They complement sealed classes and make it possible to build more flexible restricted class hierarchies.
// They can serve as "internal" interfaces that cannot be implemented outside the same module. You can rely on that fact, for example, to write exhaustive when expressions.
private sealed interface Polygon

private class Rectangle() : Polygon
private class Triangle() : Polygon

// when() is exhaustive: no other polygon implementations can appear
// after the module is compiled
private fun draw(polygon: Polygon) = when (polygon) {
    is Rectangle -> {}
    is Triangle -> {}
}


// Another use-case: with sealed interfaces, you can inherit a class from two or more sealed superclasses.
private sealed interface Fillable {
    fun fill()
}

private class Point
private sealed interface Polygon2 {
    val vertices: List<Point>
}

private class Rectangle2(override val vertices: List<Point>) : Fillable, Polygon2 {
    override fun fill() { /*...*/
    }
}