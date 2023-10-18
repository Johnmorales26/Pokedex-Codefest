package com.johndev.pokedex.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.johndev.pokedex.Greeting
import com.johndev.pokedex.R
import com.johndev.pokedex.common.entities.PokemonEntity
import com.johndev.pokedex.common.entities.Sprites
import com.johndev.pokedex.common.entities.Type
import com.johndev.pokedex.common.utils.Retrofit.service
import com.johndev.pokedex.common.utils.capitalizeFirstLetter
import com.johndev.pokedex.common.utils.formatId
import com.johndev.pokedex.common.utils.getImageById
import com.johndev.pokedex.ui.theme.PokedexTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokedexScreen() {
    val pokemonListState = remember { mutableStateOf<List<PokemonEntity>?>(null) }
    val tempList = mutableListOf<PokemonEntity>()
    // Realiza la solicitud de los 10 primeros Pokemon
    for (index in 1..20) {
        LaunchedEffect(index) {
            try {
                val response = service.getPokemonInfo(index.toString())
                tempList.add(response)
                if (tempList.size == 20) {
                    pokemonListState.value = tempList.toList().sortedByDescending { it.id }
                }
            } catch (e: Exception) {
                Log.e("PokedexScreen", "Error: $e")
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(id = R.string.app_name)) })
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                MainContent(pokemonListState.value)
            }
        }
    )
}

@Composable
fun MainContent(listPokemon: List<PokemonEntity>?) {
    if (listPokemon == null) {
        // Composable para indicar que esta cargando
        CircularProgressIndicator()
    } else {
        //  Pintar los datos cargados
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            content = {
                items(listPokemon.size) {
                    CardPokemonItem(listPokemon[it])
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPokemonItem(pokemonEntity: PokemonEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxHeight()
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
                        val type = pokemonEntity.types[it].name
                        AssistChip(
                            onClick = { },
                            label = { Text(text = if (type != null) type.capitalizeFirstLetter() else "" ) })
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PokedexTheme {
        val pokemonEntity = PokemonEntity(
            id = 1,
            name = "Bulbasaur",
            height = 7,
            peso = 69.0,
            types = listOf(
                Type(
                    name = "Grass",
                )
            ),
            sprites = Sprites(
                image = ""
            )
        )
        CardPokemonItem(pokemonEntity = pokemonEntity)
    }
}