package me.ztiany.extenstions.arrow.typederror.demo


private val microchipStore: ContextReceiversPetService.MicrochipStore = TODO()
private val petStore: ContextReceiversPetService.PetStore = TODO()
private val petOwnerStore: ContextReceiversPetService.PetOwnerStore = TODO()

suspend fun main() {
    val petService = ContextReceiversPetService(
        microchipStore,
        petStore,
        petOwnerStore
    )
}