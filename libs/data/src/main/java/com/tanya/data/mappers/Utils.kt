package com.tanya.data.mappers

internal fun <F, T1, T2> pairMapperOf(
    firstMapper: Mapper<F, T1>,
    secondMapper: Mapper<F, T2>
): suspend (List<F>) -> List<Pair<T1, T2>> = { from ->
    from.map { value ->
        firstMapper.map(value) to secondMapper.map(value)
    }
}