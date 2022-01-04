package com.tanya.tmdb

import com.tanya.base.extensions.fetchBodyWithRetry
import com.tanya.base.util.AppCoroutineDispatchers
import com.uwetrottmann.tmdb2.Tmdb
import com.uwetrottmann.tmdb2.entities.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TmdbManager @Inject constructor(
    private val tmdbClient: Tmdb,
    private val dispatchers: AppCoroutineDispatchers
) {
    private val imageProvider = MutableStateFlow(TmdbImageUrlProvider())

    fun getLatestImageProvider() = imageProvider.value

    suspend fun refreshConfiguration() {
        try {
            val config = withContext(dispatchers.io) {
                tmdbClient.configurationService().configuration().fetchBodyWithRetry()
            }
            onConfigurationLoaded(config)
        } catch (e: Exception) {

        }
    }

    private fun onConfigurationLoaded(configuration: Configuration) {
        configuration.images?.also {
            val newProvider = TmdbImageUrlProvider(
                it.secure_base_url!!,
                it.poster_sizes ?: emptyList(),
                it.backdrop_sizes ?: emptyList(),
                it.logo_sizes ?: emptyList()
            )
            imageProvider.value = newProvider
        }
    }

}