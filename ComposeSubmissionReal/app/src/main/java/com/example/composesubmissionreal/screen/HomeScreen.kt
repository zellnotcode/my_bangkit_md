package com.example.composesubmissionreal.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.composesubmissionreal.data.KimetsuRepository
import com.example.composesubmissionreal.ui.component.HashiraItem
import com.example.composesubmissionreal.ui.component.SearchBar

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: KimetsuViewModel = viewModel(factory = ViewModelFactory(KimetsuRepository())),
    navigateToDetail: (Long) -> Unit
) {
    val groupedHashira by viewModel.groupedHashira.collectAsState()
    val query by viewModel.query

    Box(modifier = modifier) {
        LazyColumn{
            item {
                SearchBar(
                    query = query,
                    onQueryChange = viewModel::search,
                    modifier = Modifier.background(MaterialTheme.colors.primary)
                )
            }
            items(groupedHashira, key = {it.id}) { hashira ->
                HashiraItem(
                    name = hashira.name,
                    photoUrl = hashira.photoUrl,
                    modifier =
                    modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            navigateToDetail(hashira.id.toLong())
                        }
                )
            }
        }
    }
}