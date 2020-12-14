package com.greater.leaguedex.usecase

import com.greater.leaguedex.network.PrivateApiService
import com.greater.leaguedex.network.model.SpecieDto
import com.greater.leaguedex.storage.store.SpecieStore
import com.greater.leaguedex.util.parser.IdType
import com.greater.leaguedex.util.parser.SwapApiParser
import tables.Specie
import javax.inject.Inject

class FetchAndUpdateSpecies @Inject constructor(
    private val apiService: PrivateApiService,
    private val specieStore: SpecieStore
) {
    private val NO_LANGUAGE = "n/a"

    suspend operator fun invoke(initialPage: Int = 1) {
        val speciesPage = apiService.getSpecies(initialPage)
        storeSpecies(speciesPage.results)
        if (speciesPage.next != null) {
            // no more pages
            invoke(SwapApiParser.parseNextPage(speciesPage.next))
        }
    }

    private suspend fun storeSpecies(results: List<SpecieDto>) {
        specieStore.insertAll(
            results.map { specie ->
                Specie(
                    id = SwapApiParser.parseIdFromUrl(specie.url, IdType.SPECIES),
                    name = specie.name,
                    language = specie.language.let { if (it == NO_LANGUAGE) null else it }
                )
            }
        )
    }
}
