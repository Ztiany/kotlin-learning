package me.ztiany.kotlin.generic


fun main() {
    testNullGeneric()
}

private interface ProducerA<out T>{

    fun getData():T

}
private interface ProducerB<out T:Any>{

    fun getData():T

}

private fun testNullGeneric() {
    var producerA:ProducerA<String>
    //var producerB:ProducerB<String?>
    //val dataA = producerA.getData()
    //val dataB = producerB.getData()
}