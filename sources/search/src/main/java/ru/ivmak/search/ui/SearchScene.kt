package ru.ivmak.search.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.ivmak.search.core.mvi.Action
import ru.ivmak.search.core.mvi.State

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun SearchScene(
    navController: NavController,
    groupId: String?,
    searchViewModel: SearchViewModel = hiltViewModel(),
    onSelected: (String) -> Unit
) {

    val state: State by searchViewModel.observableState.collectAsStateWithLifecycle()

    var isActive by remember { mutableStateOf(false) }

    BackHandler(!isActive) {
        if (groupId != null) {
            onSelected.invoke(groupId)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        SearchBar(
            query = state.searchQuery,
            placeholder = { Text(text = "Введите группу, аудиторию, фамилию преподавателя") },
            onQueryChange = { searchViewModel.dispatch(Action.SearchQueryChanged(it)) },
            onSearch = { searchViewModel.dispatch(Action.SearchQueryChanged(it)) },
            active = isActive,
            onActiveChange = { isActive = it },
            trailingIcon = {
                if (isActive) {
                    IconButton(onClick = { searchViewModel.dispatch(Action.SearchQueryChanged("")) }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear"
                        )
                    }
                } else {
                    IconButton(onClick = { isActive = true }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                }
            },
            leadingIcon = {
                if (groupId != null) {
                    IconButton(onClick = {
                        if (isActive) {
                            isActive = false
                        } else {
                            onSelected.invoke(groupId)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            }
        ) {
            if (state.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
                return@SearchBar
            }

            if (state.isError) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ошибка при загрузке :(",
                        fontSize = TextUnit(24f, TextUnitType.Sp)
                    )
                    Button(onClick = { searchViewModel.dispatch(Action.SearchQueryChanged(state.searchQuery)) }) {
                        Text(text = "Попробовать еще раз")
                    }
                }
                return@SearchBar
            }

            LazyColumn {
                items(state.items, key = { it.group }) {
                    ListItem(
                        modifier = Modifier
                            .animateItemPlacement()
                            .clickable { onSelected.invoke(it.group) },
                        headlineContent = { Text(text = it.name) },
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScenePreview() {
    SearchScene(rememberNavController(), null) {}
}
