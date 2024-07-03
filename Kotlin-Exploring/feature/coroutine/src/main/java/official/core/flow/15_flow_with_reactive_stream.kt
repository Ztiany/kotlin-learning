package official.core.flow


/**
 * For those who are familiar with Reactive Streams or reactive frameworks such as RxJava and project Reactor, design of the Flow may look very familiar.
 *
 * Indeed, its design was inspired by Reactive Streams and its various implementations. But Flow main goal is to have as simple design as possible, be
 * Kotlin and suspension friendly and respect structured concurrency. Achieving this goal would be impossible without reactive pioneers and their
 * tremendous work. You can read the complete story in [Reactive Streams and Kotlin Flows article](https://medium.com/@elizarov/reactive-streams-and-kotlin-flows-bfd12772cda4).
 *
 * While being different, conceptually, Flow is a reactive stream and it is possible to convert it to the reactive (spec and TCK compliant) Publisher
 * and vice versa. Such converters are provided by kotlinx.coroutines out-of-the-box and can be found in corresponding reactive modules
 * (kotlinx-coroutines-reactive for Reactive Streams, kotlinx-coroutines-reactor for Project Reactor and kotlinx-coroutines-rx2/kotlinx-coroutines-rx3
 * for RxJava2/RxJava3). Integration modules include conversions from and to Flow, integration with Reactor's Context and suspension-friendly ways to
 * work with various reactive entities.
 */
fun main() {
    println("Flow with Reactive Streams.")
}