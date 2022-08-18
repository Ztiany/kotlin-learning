package book.dukc.ch03_foundation;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import org.jetbrains.annotations.NotNull;
import sun.invoke.empty.Empty;

import static book.dukc.ch03_foundation._3_2_suspendKt.notSuspend;

/**
 * 通过 Java 来看协程的本质，原来也是回调。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2020/9/29 14:18
 */
public class JavaInvokeSuspend {

    public static void main(String... args) {
        Object o = notSuspend(new Continuation<Integer>() {
            @NotNull
            @Override
            public CoroutineContext getContext() {
                return EmptyCoroutineContext.INSTANCE;
            }

            @Override
            public void resumeWith(@NotNull Object o) {
                System.out.println("JavaInvokeSuspend.resumeWith");
            }
        });
    }

}
