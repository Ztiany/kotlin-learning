package explore.analyse

import kotlinx.coroutines.*

fun main() = runBlocking {
    val parentJob = Job() // Create a parent job

    val childJob = launch(parentJob) {
        try {
            repeat(5) { i ->
                println("Child job is working: $i")
                delay(500)
            }
        } finally {
            println("Child job finally block executed")
        }
    }

    delay(1500) // Allow child job to run for a while

    println("Cancelling the parent job")
    parentJob.cancel() // Cancel the parent job

    try {
        childJob.join() // Wait for the child job to complete (or be canceled)
    } catch (e: CancellationException) {
        println("!!! Child job was canceled: $e")
    }

    println("Main: Parent job is done or canceled")
}