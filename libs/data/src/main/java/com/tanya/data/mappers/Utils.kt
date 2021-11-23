package com.tanya.data.mappers

import com.uwetrottmann.tmdb2.entities.BaseTvShow
import com.uwetrottmann.tmdb2.entities.TvShowResultsPage

internal fun <F, T1, T2> pairMapperOf(
    firstMapper: Mapper<F, T1>,
    secondMapper: Mapper<F, T2>
): suspend (List<F>) -> List<Pair<T1, T2>> = { from ->
    from.map { value ->
        firstMapper.map(value) to secondMapper.map(value)
    }
}

internal fun <F, T1, T2> pairMapperOf(
    firstMapper: Mapper<F, T1>,
    secondMapper: IndexedMapper<F, T2>
): suspend (List<F>) -> List<Pair<T1, T2>> = { from ->
    from.mapIndexed { index, value ->
        firstMapper.map(value) to secondMapper.map(index, value)
    }
}

internal inline fun <T> unwrapTmdbShowResults(
    crossinline f: suspend (List<BaseTvShow>) -> List<T>
): suspend (TvShowResultsPage) -> List<T> = {
    f(it.results ?: emptyList())
}