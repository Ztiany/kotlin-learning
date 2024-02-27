package me.ztiany.kotlin.function;

/**
 * @see ReturnUnitKt, 《Kotlin 核心编程》第二章
 */
public class ReturnVoid {

    private interface Function<Arg, Return> {
        Return apply(Arg arg);
    }

    public static void main(String[] args) {
        // 如何理解 Unit？其实它与 int 一样，都是一种类型，然而它不代表任何信息，用面向对象的术语来描述就是一个单例，它的实例只有一个，可写为 ()。
        // 那么，Kotlin 为什么要引入 Unit 呢？一个很大的原因是函数式编程侧重于组合，尤其是很多高阶函数，在源码实现的时候都是采用泛型来实现的。
        // 然而 void 在涉及泛型的情况下会存在问题。
        Function<String, Integer> stringLen = new Function<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        };

        int result = stringLen.apply("ac");

        // 看上去似乎没什么问题。我们再来改造下，这一次希望重新实现一个 print 方法。于是，难题出现了，Return 的类型用什么来表示呢？可能你会想到 void，
        // 但 Java 中是不能这么干的。无奈之下，我们只能把 Return 换成 Void，即 Function<String,Void>，由于 Void 没有实例，则返回一个 null。
        // 这种做法严格意义上讲，相当丑陋。
        Function<String, Void> printStr = new Function<String, Void>() {
            @Override
            public Void apply(String s) {
                System.out.println(s);
                return null;
            }
        };

        printStr.apply("ac");

        /*
        Java 8 实际解决办法是通过引入 Action<T> 这种函数式接口来解决问题，比如：

                Consumer<T>，接收一个输入参数并且无返回的操作。
                BiConsumer<T, U>，接收两个输入参数的操作，并且不返回任何结果。
                ObjDoubleConsumer<T>，接收一个 object 类型和一个 double 类型的输入参数，无返回值。
                ObjIntConsumer<T>，接收一个 object 类型和一个 int 类型的输入参数，无返回值。
                ObjLongConsumer<T>，接收一个 object 类型和一个 long 类型的输入参数，无返回值。

         虽然解决了问题，但这种方案不可避免地创造了大量的重复劳动，所以，最好的解决办法就是引入一个单例类型 Unit，除了不代表任何意义的以外，它与其他常规类型并没有什么差别。
          */
    }

}