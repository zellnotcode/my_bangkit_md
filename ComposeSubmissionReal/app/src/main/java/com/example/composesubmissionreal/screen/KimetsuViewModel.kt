package com.example.composesubmissionreal.screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.composesubmissionreal.data.KimetsuRepository
import com.example.composesubmissionreal.model.Hashira
import kotlinx.coroutines.flow.MutableStateFlow

class KimetsuViewModel(private val repository: KimetsuRepository) : ViewModel() {
    private val _groupedHashira = MutableStateFlow(
        repository.getCharacters()
            .sortedBy { it.name }
    )
    val groupedHashira: MutableStateFlow<List<Hashira>> get() = _groupedHashira

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun search(newQuery: String) {
        _query.value = newQuery
        _groupedHashira.value = repository.searchCharacter(_query.value)
            .sortedBy { it.name }
    }

    fun getCharacterById(id: String): Hashira? {
        return repository.getCharacterById(id)
    }
}

class ViewModelFactory(private val repository: KimetsuRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KimetsuViewModel::class.java)) {
            return KimetsuViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}