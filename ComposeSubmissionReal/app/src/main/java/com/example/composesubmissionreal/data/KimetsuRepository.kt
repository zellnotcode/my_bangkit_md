package com.example.composesubmissionreal.data

import com.example.composesubmissionreal.model.Hashira
import com.example.composesubmissionreal.model.HashiraData

class KimetsuRepository {
    fun getCharacters() : List<Hashira> {
        return HashiraData.hashira
    }

    fun searchCharacter(query: String): List<Hashira> {
        return HashiraData.hashira.filter {
            it.name.contains(query, ignoreCase = true)
        }
    }

    fun getCharacterById(id: String) : Hashira? {
        return HashiraData.hashira.find { it.id == id }
    }
}