package com.johndev.pokedex.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.johndev.pokedex.R
import com.johndev.pokedex.common.entities.PokemonEntity
import com.johndev.pokedex.common.utils.Retrofit.service
import com.johndev.pokedex.common.utils.capitalizeFirstLetter
import com.johndev.pokedex.common.utils.formatId
import com.johndev.pokedex.common.utils.getImageById

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen(onNavigation: (Int) -> Unit) {
    val pokemonListState = remember { mutableStateOf<List<PokemonEntity>?>(null) }
    val tempList = mutableListOf<PokemonEntity>()
    val offset = remember { mutableStateOf(0) }
    val limit = remember { mutableStateOf(50) }
    // Realiza la solicitud de los 10 primeros Pokemon
    for (index in offset.value..limit.value) {
        LaunchedEffect(index) {
            try {
                val response = service.getPokemonInfo(index.toString())
                tempList.add(response)
                if (tempList.size == 50) {
                    pokemonListState.value = tempList.toList().sortedBy { it.id }
                }
            } catch (e: Exception) {
                Log.e("PokedexScreen", "Error: $e")
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(id = R.string.app_name)) })
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MainContent(
                    listPokemon = pokemonListState.value,
                    callback = {
                        offset.value += 50
                        limit.value += 50
                        pokemonListState.value = null
                    },
                    onNavigation = {
                        onNavigation(it)
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                offset.value += 50
                limit.value += 50
                pokemonListState.value = null
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    )
}

@Composable
fun MainContent(listPokemon: List<PokemonEntity>?, callback: () -> Unit, onNavigation: (Int) -> Unit) {
    if (listPokemon == null) {
        // Composable para indicar que esta cargando
        CircularProgressIndicator()
    } else {
        //  Pintar los datos cargados
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            /*LazyVerticalGrid(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                columns = GridCells.Fixed(2),
                content = {
                items(listPokemon.size) {
                    CardPokemonItemGrid(pokemonEntity = listPokemon[it])
                }
            })*/
            listPokemon.forEach {
                CardPokemonItem(it) { id ->
                    onNavigation(id)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            /*LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    items(listPokemon.size) {
                        CardPokemonItem(listPokemon[it])
                    }
                })*/
        }
    }
}

@Composable
fun CardPokemonItemGrid(pokemonEntity: PokemonEntity) {
    Box(
        modifier = Modifier
            .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(8.dp))
            .size(100.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = formatId(pokemonEntity.id))
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                model = getImageById(pokemonEntity.id),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Text(text = pokemonEntity.name.capitalizeFirstLetter())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPokemonItem(pokemonEntity: PokemonEntity, callback: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable {  callback(pokemonEntity.id) },
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = formatId(pokemonEntity.id))
                Text(text = pokemonEntity.name.capitalizeFirstLetter())
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(pokemonEntity.types.size) {
                        val type = pokemonEntity.types[it].type.name
                        AssistChip(
                            onClick = { },
                            label = { Text(text = if (type != null) type.capitalizeFirstLetter() else "") })
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(
                    Color(0xFFf0f0f0),
                    shape = RoundedCornerShape(topStart = 64.dp, bottomStart = 64.dp)
                )
        ) {
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = getImageById(pokemonEntity.id),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}

