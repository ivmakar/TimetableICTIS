package ru.ivmak.search.core.network.models

data class ChoiceList(
    val choices: List<Choice>?,
) {
    data class Choice (
        val name: String,
        val id: String,
        val group: String
    )
}
