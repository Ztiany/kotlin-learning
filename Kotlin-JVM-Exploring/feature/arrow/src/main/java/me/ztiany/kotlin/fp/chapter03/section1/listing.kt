package me.ztiany.kotlin.fp.chapter03.section1

///////////////////////////////////////////////////////////////////////////
// Defining functional data structures
///////////////////////////////////////////////////////////////////////////

//tag::init[]
sealed class List<out A> // <1>

object Nil : me.ztiany.kotlin.fp.chapter03.section1.List<Nothing>() // <2>

data class Cons<out A>(val head: A, val tail: me.ztiany.kotlin.fp.chapter03.section1.List<A>) : me.ztiany.kotlin.fp.chapter03.section1.List<A>() // <3>
//end::init[]
