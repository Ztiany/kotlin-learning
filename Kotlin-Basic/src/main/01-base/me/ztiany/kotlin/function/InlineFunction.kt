package me.ztiany.kotlin.function

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.TreeNode


/**
 * **内联函数**：使用高阶函数会带来一些运行时的效率损失，即每一个函数都是一个对象，并且会捕获一个闭包。
 * 即那些在函数体内会访问到的变量。 内存分配（对于函数对象和类）和虚拟调用会引入运行时间开销。
 * 但是在许多情况下通过内联化 lambda 表达式可以消除这类的开销。
 *
 *  **lambda 的开销**：在 Kotlin 中每声明一个 Lambda 表达式，就会在字节码中产生一个匿名类。该匿名类包含了一个 invoke 方法，作为 Lambda 的调用方法，每次调用的时候，
 * 还会创建一个新的对象。可想而知，Lambda 语法虽然简洁，但是额外增加的开销也不少。尤其对 Kotlin 这门语言来说，它当今优先要实现的目标，就是在 Android 这个平台
 * 上提供良好的语言特性支持。如果你熟悉 Android 开发，肯定了解 Java 6 是当今 Android 主要采用的开发语言，Kotlin 要在 Android 中引入 Lambda 语法，必须采用某种方
 * 法来优化 Lambda 带来的额外开销，也就是内联函数。
 *
 *  **java 如何优化 lambda 的开销**？Java 中是如何解决这个问题的。与 Kotlin 这种在编译期通过硬编码生成 Lambda 转换类的机制不同，Java 在 SE 7之后通过 invokedynamic
 *  技术实现了在运行期才产生相应的翻译代码。在 invokedynamic 被首次调用的时候，就会触发产生一个匿名类来替换中间码 invokedynamic，后续的调用会直接采用这个匿名类的
 *  代码。（具体参考 https://stackoverflow.com/questions/30002380/why-are-java-8-lambdas-invoked-using-invokedynamic）这种做法的好处主要体现在：
 *
 *  1. 由于具体的转换实现是在运行时产生的，在字节码中能看到的只有一个固定的 invokedynamic，所以需要静态生成的类的个数及字节码大小都显著减少；
 *  2. 与编译时写死在字节码中的策略不同，利用 invokedynamic 可以把实际的翻译策略隐藏在 JDK 库的实现，这极大提高了灵活性，在确保向后兼容性的同时，后期可以继续对翻译策略不断优化升级；
 *  3. JVM 天然支持了针对该方式的 Lambda 表达式的翻译和优化，这也意味着开发者在书写 Lambda 表达式的同时，可以完全不用关心这个问题，这极大地提升了开发的体验。
 *
 *
 * **为什么 Kotlin 不适用 invokedynamic**？invokedynamic 固然不错，但 Kotlin 不支持它的理由似乎也很充分。我们有足够的理由相信，其最大的原因是 Kotlin 在一开始就需要兼容
 * Android 最主流的 Java 版本 SE 6，这导致它无法通过 invokedynamic 来解决 Android 平台的 Lambda 开销问题。我们也可以通过阅读 Kotlin 团队核心成员在其官方论坛上的发言，
 * 来证实这一猜想。发言网址如下：https://discuss.kotlinlang.org/t/aop-invokedynamic-interceptable/660，因此，作为另一种主流的解决方案，Kotlin 拥抱了内联函数，在 C++、C#
 * 等语言中也支持这种特性。简单来说，我们可以用 inline 关键字来修饰函数，这些函数就成为了内联函数。它们的函数体在编译期被嵌入每一个被调用的地方，以减少额外生成的匿名类数，
 * 以及函数执行的时间开销。所以如果你想在用 Kotlin 开发时获得尽可能良好的性能支持，以及控制匿名类的生成数量，就有必要来学习下内联函数的相关语法。
 *
 * **内联函数特点**：
 *
 * 1. inline 修饰符影响函数本身和传给它的 lambda 表达式：所有这些都将内联到调用处。
 * 2. 内联可能导致生成的代码增加，但是如果我们使用得当（不内联大函数），它将在性能上有所提升，尤其是在循环中的“超多态（megamorphic）”调用处。
 *
 * 参考资料：
 *
 *  - [Kotlin 源码里成吨的 noinline 和 crossinline 是干嘛的？看完这个视频你转头也写了一吨](https://juejin.cn/post/6869954460634841101) 。
 */
fun main() {

}

//内联函数
private inline fun <T> lock(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body.invoke()
    } finally {
        lock.unlock()
    }
}

//非内联函数
private fun <T> noinlineLock(lock: Lock, body: () -> T): T {
    lock.lock()
    try {
        return body.invoke()
    } finally {
        lock.unlock()
    }
}

/**
 * **内联函数不是万能的**：以下情况我们应避免使用内联函数：
 *
 * 1. 由于 JVM 对普通的函数已经能够根据实际情况智能地判断是否进行内联优化，所以我们并不需要对其实使用 Kotlin 的 inline 语法，那只会让字节码变得更加复杂；
 * 2. 尽量避免对具有大量函数体的函数进行内联，这样会导致过多的字节码数量；
 * 3. 一旦一个函数被定义为内联函数，便不能获取闭包类的私有成员，除非你把它们声明为 internal。
 *
 * 禁用内联：
 *
 * 1. 如果传给一个内联函数的参数是 lambda 表达式，而且想要控制某些 lambda 参数不被内联，可以用 noinline 修饰符标记哪些不想被内联的函数。
 * 2. 可以内联的 lambda 表达式只能在内联函数内部调用或者作为可内联的参数传递， 但是 noinline 的可以以任何我们喜欢的方式操作：存储在字段中、传送它等等。
 * 3. 需要注意的是，如果一个内联函数没有可内联的函数参数并且没有具体化的类型参数，编译器会产生一个警告，因为内联这样的函数很可能并无益处
 *      （如果你确认需要内联，则可以用 @Suppress("NOTHING_TO_INLINE") 注解关掉该警告）。
 */
private class NoinlineSub {
    var func: (() -> Unit)? = null
}

private inline fun noinlineSample(inlined: () -> Unit, /*使用 noinline 修饰*/noinline notInlined: () -> Unit) {
    val a = NoinlineSub()
    //a.func = inlined //由于inlined是内联lambda表达式，所以不能赋值给NoinlineSub的func字段。
    a.func = notInlined
    inlined.invoke()
    notInlined.invoke()
}


/**
 * 非局部返回：在 Kotlin 中，我们可以只使用一个正常的、非限定的 return 来退出一个命名或匿名函数。
 *           这意味着要退出一个 lambda 表达式，我们必须使用一个标签，
 *           并且 在 lambda 表达式内部禁止使用裸 return，因为 lambda 表达式不能使包含它的函数返回。
 */
private fun inlineReturn1() {
    noinlineLock(ReentrantLock()) {
        // 禁止使用裸 return，因为 lambda 表达式不能使包含它的函数返回：
        // 错误：不能使 `foo` 在此处返回
        //return
    }

    //内联函数可以直接从函数返回，因为运行时它将会被内联到该函数体内
    lock(ReentrantLock()) {
        return
    }

    crossinlineSample {
        val a = 3
        // 禁止 return
        //return
    }
}

private fun inlineReturn2() {
    //但是如果 lambda 表达式传给的函数是内联的，该 return 也可以内联，所以它是允许的
    //这种返回（位于 lambda 表达式中，但退出包含它的函数）称为非局部返回
    fun hasZeros(ints: List<Int>): Boolean {
        //forEach是是一个内联扩展
        ints.forEach {
            if (it == 0) return true // 从 hasZeros 返回
        }
        return false
    }
}

//名函数默认使用局部返回，fun 就近原则
private fun inlineReturn3(people: List<String>) {
    people.forEach(fun(s) {
        if (s == "Alice") return
        println("$s is not Alice")
    })
}


/**
 * crossinline：
 *
 *       一些内联函数可能调用传给它们的不是直接来自函数体、而是来自另一个执行上下文的 lambda 表达式参数。
 *        例如来自局部对象或嵌套函数。在这种情况下，该 lambda 表达式中也不允许非局部控制流。
 *        为了标识这种情况，该 lambda 表达式参数需要用 crossinline 修饰符标记。
 *
 *具体来说就是：非局部返回虽然在某些场合下非常有用，但可能也存在危险。因为有时候，我们内联的函数所接收的 Lambda 参数常常来自于上下文其他地方。
 * 为了避免带有 return 的 Lambda 参数产生破坏，我们还可以使用 crossinline 关键字来修饰该参数，从而杜绝此类问题的发生。
 */
private inline fun crossinlineSample(/*使用 crossinline 修饰*/crossinline body: () -> Unit) {
    //这里没有直接调用 body，而是作为 Runnable 的实现，这种情况下，不允许内联。
    val f = Runnable { body() }
}

/**
 * 具体化的类型参数：
 *
 *      有时候我们需要访问一个作为参数传给我们的一个类型，这时可以使用具体化的类型参数：`reified`，
 *      使用 reified 修饰代码类型参数的函数，在函数体内，可以像使用实际类型那样使用类型参数声明，因为它将会被内联。
 */
private fun <T> TreeNode.findParentOfType1(clazz: Class<T>): T? {
    //在这里我们向上遍历一棵树并且检查每个节点是不是特定的类型。 这都没有问题，但是调用处不是很优雅
    var p = parent
    while (p != null && !clazz.isInstance(p)) {
        p = p.parent
    }
    @Suppress("UNCHECKED_CAST")
    return p as T?
}

//内联函数支持具体化的类型参数，于是我们可以这样写：
//  使用 reified 修饰符来限定类型参数，现在可以在函数内部访问它了， 几乎就像是一个普通的类一样。
//  由于函数是内联的，不需要反射，正常的操作符如 !is 和 as 现在都能用了
//  普通的函数（未标记为内联函数的）不能有具体化参数。 不具有运行时表示的类型
private inline fun <reified T> TreeNode.findParentOfType2(): T? {
    var p = parent
    while (p != null && p !is T) {
        p = p.parent
    }
    return p as T?
}

private fun testfindParentOfType() {
    val treeNode: TreeNode? = null
    treeNode?.findParentOfType1(DefaultMutableTreeNode::class.java)
    treeNode?.findParentOfType2<TreeNode>()
}


/**
 * 内联属性：
 *
 *      inline 修饰符可用于没有幕后字段的属性的访问器。 可以标注独立的属性访问器
 *      在调用处，内联访问器如同内联函数一样内联。
 */
private class Pa {}

private class Pb {}

private fun getPa(): Pa {
    return Pa()
}

private class InlinePropertySub {

    val foo: Pa
        inline get() = getPa()

}