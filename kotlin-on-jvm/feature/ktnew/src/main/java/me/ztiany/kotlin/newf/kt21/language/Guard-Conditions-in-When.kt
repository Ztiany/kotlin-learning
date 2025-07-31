package me.ztiany.kotlin.newf.kt21.language

private sealed interface Animal {
    data class Cat(val mouseHunter: Boolean) : Animal {
        fun feedCat() {}
    }

    data class Dog(val breed: String) : Animal {
        fun feedDog() {}
    }
}

private fun feedAnimal(animal: Animal) {
    when (animal) {
        // Branch with only the primary condition. Calls `feedDog()` when `animal` is `Dog`
        is Animal.Dog -> animal.feedDog()
        // Branch with both primary and guard conditions. Calls `feedCat()` when `animal` is `Cat`
        // and is not `mouseHunter`
        is Animal.Cat if !animal.mouseHunter -> animal.feedCat()
        // Prints "Unknown animal" if none of the above conditions match
        else -> println("Unknown animal")
    }
}

/**
 * Starting from 2.1.0, you can use guard conditions in when expressions or statements with subjects.
 *
 * Guard conditions allow you to include more than one condition for the branches of a when
 * expression, making complex control flows more explicit and concise as well as flattening the code
 * structure.
 *
 * To include a guard condition in a branch, place it after the primary condition, separated by `if`:
 */
fun main() {
    feedAnimal(Animal.Cat(false))
    feedAnimal(Animal.Cat(true))
}