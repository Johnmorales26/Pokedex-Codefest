package com.johndev.pokedex.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import coil.compose.AsyncImage
import com.johndev.pokedex.common.entities.PokemonEntity
import com.johndev.pokedex.common.utils.Retrofit
import com.johndev.pokedex.common.utils.capitalizeFirstLetter
import com.johndev.pokedex.common.utils.getImageById

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: Int, onNavigation: () -> Unit) {
    var pokemonById by remember { mutableStateOf<PokemonEntity?>(null) }

    LaunchedEffect(id) {
        try {
            pokemonById = Retrofit.service.getPokemonInfo(id.toString())
        } catch (e: Exception) {
            Log.e("PokedexScreen", "Error: $e")
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(
                title = { Text(text = pokemonById?.name?.capitalizeFirstLetter() ?: "Unknown") },
                navigationIcon = {
                    IconButton(onClick = { onNavigation() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        },
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                PokemonDetails(pokemon = pokemonById)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetails(pokemon: PokemonEntity?) {
    if (pokemon == null) {
        // Composable para indicar que esta cargando
    } else {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier.size(200.dp),
                model = getImageById(pokemon.id), contentDescription = null,
                contentScale = ContentScale.Crop
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                items(pokemon.types.size) {
                    AssistChip(
                        onClick = { },
                        label = { Text(text = pokemon.types[it].type.name.capitalizeFirstLetter()) })
                }
            }
            Text(text = "About", style = MaterialTheme.typography.headlineSmall)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${pokemon.weight} kg",
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center
                        )
                    )
                    Text(text = "Weight", style = MaterialTheme.typography.bodyMedium)
                }
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(2.dp)
                        .weight(1f)
                )
                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )  {
                    Text(text = "${pokemon.height} m",
                        style = MaterialTheme.typography.titleMedium.copy(
                            textAlign = TextAlign.Center
                        ))
                    Text(text = "Weight", style = MaterialTheme.typography.bodyMedium)
                }
            }
            ElevatedCard(modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
                LazyColumn {
                    items(pokemon.stats.size) {
                        val stat = pokemon.stats[it]
                        Row {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = stat.stat.name,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            LinearProgressIndicator(
                                progress = stat.base_stat.toFloat() / 255f,
                                modifier = Modifier.weight(3f)
                            )
                        }
                    }
                }
            }
        }
    }
}
