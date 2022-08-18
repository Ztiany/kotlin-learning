package me.ztiany.kotlin.advanced

import kotlin.reflect.KProperty
import kotlin.reflect.full.*

/**
 * 元编程
 */
fun main() {

}

///////////////////////////////////////////////////////////////////////////
// 一个例子
///////////////////////////////////////////////////////////////////////////

/**
 * 需求：实现一个通用的将对象转化为 Map 的函数。下面利用反射我们完美地实现了需求。
 *
 * 1. 该函数适用于所有 data class。只需要调用一个 Mapper.toMap 函数我们就能将所有类型转化成Map。
 * 2. 相比手动写转换方法，不再需要手工创建 Map。所有的属性名都是自动根据 KClass 对象获取的，不存在写错的可能。
 */
private object Mapper {
    //获取A的所有属性
    fun <A : Any> toMap(a: A): Map<String, Any?> {
        return a::class.memberProperties.map { m ->
            val p = m as KProperty<*>
            p.name to p.call(a)
        }.toMap()
    }
}

/*
元数据：现在我们来审视一下上述代码中的 a::class 。

    a::class 的类型是 KClass，是 Kotlin 中描述类型的类型（通常被称为 metaclass）。如果我们将 User 看成是描述现实概念的数据结构，那么在传入参数类型
    为 User 时，a::class 则可以看成描述 User 类型的数据。这样描述数据的数据就可以称之为元数据。
 */

///////////////////////////////////////////////////////////////////////////
// 元编程
///////////////////////////////////////////////////////////////////////////
/**
描述数据的数据可以称之为元素据。我们将程序看成描述需求的数据，那么描述程序的数据就是程序的元数据。
而像这样操作元数据的编程就可以称之为元编程。

也许有人会说，元编程不就是反射吗？这个说法是不全面的，元编程可以用一句话概括：**程序即是数据，数据即是程序**。

注意这句话包含两个方面意思：

1. 前半句指的是访问描述程序的数据，如我们通过反射获取类型信息；
2. 后半句则是指将这些数据转化成对应的程序，也就是所谓代码生成。

反射就是获取描述程序信息的典型例子，而而代码生成我们则相对陌生一些。参考下面示例：

 ```
 # !/bin/sh
# metaprogram
echo `#!/bin/sh` > program
for i in $(seq 992)
do
    echo "echo $i" >> program
done
chmod +x program
 ```

这个脚本创建了一个名为 program 的文件，并通过 echo 命令将代码写入该文件。这就是一个典型的生成代码的例子。这个例子将程序作为程序的输出。

仔细思考之后不难发现，元编程就像高阶函数一样，是一种更高阶的抽象，高阶函数将函数作为输入或输出，而元编程则是将程序本身作为输入或输出。

更高级的元编程技术：Lisp 的同像性（homoiconicity）。在计算机编程中，同像性（homoiconicity，来自希腊语单词 homo，意为与符号含义表示相同）
是某些编程语言的特殊属性，它意味着一个程序的结构与其句法是相似的，因此易于通过阅读程序来推测程序的内在涵义。如果一门编程语言具备了同像性，
说明该语言的文本表示（通常指源代码）与其抽象语法树（AST）具有相同的结构（即，AST和语法是同形的）。该特性允许使用相同的表示语法，将语言
中的所有代码当成资料来存取以及转换，提供了“代码即数据”的理论前提。

 总结：

1. 元编程是指操作元数据的编程。它通常需要获取程序本身的信息或者直接生成程序的一部分或者两者兼而有之。
2. 元编程可以消除某些样板代码。如前文例子那样，原本需要对每个类型编写特定转化代码，而现在只需要统一的一个函数即可实现。

然而元编程不是只有优点，同样也存在缺点：

1. 它有一定的学习成本，在没听说过相关的技术之前，程序员们通常会感觉到摸不着头脑。
2. 它编写的代码不够直接，需要进一步思考才能被理解。

**常见的元编程技术**：

理解了元编程的概念之后，我们继续讨论元编程技术常见的实现手段。目前主流的实现方式包括：

 1. 运行时通过 API 暴露程序信息。反射就是这种实现思路。
 2. 动态执行代码。多见于脚本语言，如 JavaScript 就有 eval 函数，可以动态地将文本作为代码执行。·
3. 通过外部程序实现目的。如编译器，在将源文件解析为 AST 之后，可以针对这些 AST 做各种转化。
这种实现思路最典型的例子是我们常常谈论的语法糖，编译器会将这部分代码 AST 转化为相应的等价的 AST，这个过程通常被称为 desuger（解语法糖）。

以上便是常见的实现思路，下面看看这些思路在编程语言中的体现。

 1. 反射：Kotlin 中的 KClass 就是如此，它是一个 Kotlin 的类，同时它的实例又能作为描述其他类的元数据。像这样用 Kotlin 描述 Kotlin 自身信息的行为就是
所谓的反射或者自反。自反实际上更贴合这个定义，也容易理解。除了 Kotlin 和 Java 以外，还有许多编程语言，如 Ruby、Python 等都支持反射技术。
 2. 宏：C 语言中的宏替换。
 3. 模板元编程：这是 C++ 的招牌特性，甚至有本名为《Modern C++ Design》的书通篇都是围绕这一特性来展示各种奇技淫巧。
 4. 路径依赖类型：维基百科上将此特性归为一种元编程，支持路径依赖类系的语言通常可以在编译的时候从类型层面避免大部分 bug。
由于这个特性通常只在一些学术型编程语言如 Haskell、Scala 中出现，所有实践中应用并不广泛。

 此外，还有注解处理器。
 */
sealed class Nat {
    companion object {
        object Zero : Nat()
    }

    val Companion._0
        get() = Zero

    fun <A : Nat> Succ<A>.preceed(): A {
        return this.prev
    }
}

data class Succ<N : Nat>(val prev: N) : Nat()

fun <A : Nat> Nat.plus(other: A): Nat {
    return when {
        other is Succ<*> -> Succ(this.plus(other.prev)) // a + S(b) = S(a + b)
        else -> this // a + 0 = a
    }
}

fun plusDemo() {
    val _0 = Nat.Companion.Zero
    val _1 = Succ(_0)
    val _2 = Succ(_1)
    val _3 = Succ(_2)
    println(_0.plus(_1) == _1)
    println(_1.plus(_2) == _3)
}

fun reflectDemo() {
    println(Nat.Companion::class.isCompanion)
    println(Nat::class.isSealed)
    println(Nat.Companion::class.objectInstance)
    println(Nat::class.companionObjectInstance)
    println(Nat::class.declaredMemberExtensionFunctions.map { it.name })
    println(Succ::class.declaredMemberExtensionFunctions.map { it.name })
    println(Succ::class.memberExtensionFunctions.map { it.name })
    println(Nat::class.declaredMemberExtensionProperties.map { it.name })
    println(Succ::class.declaredMemberExtensionProperties.map { it.name })
    println(Succ::class.memberExtensionProperties.map { it.name })
    println(Succ::class.starProjectedType)
}