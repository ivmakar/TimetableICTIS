package ru.ivmak.search.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ru.ivmak.search.core.mvi.Action
import ru.ivmak.search.core.mvi.State
import ru.ivmak.search.core.network.models.ChoiceList
import ru.ivmak.search.ui.components.SearchBarUI

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchScene(
    navController: NavController,
    searchViewModel: SearchViewModel = hiltViewModel()
) {

    val state: State by if (LocalInspectionMode.current)
            remember { mutableStateOf(State()) }
        else
            searchViewModel.observableState.collectAsStateWithLifecycle()

    SearchBarUI(
        searchText = state.searchQuery,
        placeholderText = "Placeholder",
        onSearchTextChanged = { searchViewModel.dispatch(Action.SearchQueryChanged(it)) },
        onClearClick = { searchViewModel.dispatch(Action.SearchQueryChanged("")) },
        onNavigateBack = { navController.popBackStack() },
    ) {
        if (state.isError) {
            ErrorView()
            return@SearchBarUI
        }

        if (state.isLoading) {
            LoadingView()
            return@SearchBarUI
        }

        if (state.items.isEmpty()) {
            EmptyView()
        } else {
            ListView(state.items)
        }

    }
}

@Composable
fun ListView(items: List<ChoiceList.Choice>) {
    Box {
        LazyColumn {
            items(items) {
                SearchResultItem(text = it.name)
            }
        }
    }
}

@Composable
fun EmptyView() {
    Box {
        Text(text = "Empty")
    }
}

@Composable
fun ErrorView() {
    Box {
        Text(text = "Error")
    }
}

@Composable
fun LoadingView() {
    Box {
        Text(text = "Loading")
    }
}

@Composable
fun SearchResultItem(text: String) {
    Box {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@Composable
fun SearchScenePreview() {
    SearchScene(rememberNavController())
}
