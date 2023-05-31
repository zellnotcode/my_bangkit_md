package com.example.composesubmissionreal.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composesubmissionreal.data.KimetsuRepository
import com.example.composesubmissionreal.model.Hashira
import com.example.composesubmissionreal.ui.component.DetailContent

@Composable
fun DetailScreen(
    id: Long,
    navigateBack: () -> Unit,
    viewModel: KimetsuViewModel = viewModel(factory = ViewModelFactory(KimetsuRepository()))
) {
    val detailHashira: Hashira? = viewModel.getCharacterById(id.toString())

    detailHashira?.let {
        DetailContent(
            name = it.name,
            image = it.detailPhoto,
            type = it.type,
            description = it.description,
        )
    }
}