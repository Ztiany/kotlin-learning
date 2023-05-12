# 03 Functional data structures

- Immutable data structures are objects that can be acted on by pure functions. 
- A sealed class has a finite amount of implementations, restricting the data structure grammar. 
- The when construct can match typed data structures and select an appropriate outcome evaluation.
- Kotlin matching helps work with data structures but falls short of pattern matching supported by other functional languages.
- Data sharing through the use of immutable data structures allows safe access without the need for copying structure contents.
- List operations are expressed through recursive, generalized HOFs, promoting code reuse and modularity.
- Kotlin standard library lists are read-only, not immutable, allowing data corruption to occur when acted on by pure functions.
- Algebraic data types (ADTs) are the formal name of immutable data structures and can be modeled by data classes, Pairs, and Triples in Kotlin.
- Both List and Tree developed in this chapter are examples of ADTs.
