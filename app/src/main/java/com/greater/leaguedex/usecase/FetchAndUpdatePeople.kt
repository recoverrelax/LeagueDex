package com.greater.leaguedex.usecase

import com.greater.leaguedex.network.PrivateApiService
import com.greater.leaguedex.network.model.PeopleDto
import com.greater.leaguedex.storage.store.PeopleStore
import com.greater.leaguedex.util.parser.IdType
import com.greater.leaguedex.util.parser.SwapApiParser
import tables.PeopleEntity
import tables.PeopleVehicles
import javax.inject.Inject

class FetchAndUpdatePeople @Inject constructor(
    private val apiService: PrivateApiService,
    private val peopleStore: PeopleStore,
) {

    suspend operator fun invoke(initialPage: Int = 1) {
        val peoplePage = apiService.getPeople(initialPage)
        storePeople(peoplePage.results)
        if (peoplePage.next != null) {
            // no more pages
            invoke(SwapApiParser.parseNextPage(peoplePage.next))
        }
    }

    private suspend fun storePeople(results: List<PeopleDto>) {
        val personEntities = results.map { people: PeopleDto ->
            PeopleEntity(
                id = SwapApiParser.parseIdFromUrl(people.url, IdType.PEOPLE),
                name = people.name,
                // the api has indication that a person may have multiple specie by the defined schema type
                // although a quick query shows no more than 1 specie is found for each person
                // let's consider just 1 for now
                // TODO: support multiple species
                specieId = people.species.firstOrNull()
                    ?.let { SwapApiParser.parseIdFromUrl(it, IdType.SPECIES) }
            )
        }
        peopleStore.insertAllPeople(personEntities)

        val vehiclesEntities = personEntities.mapIndexed { index, people ->
            results[index].vehicles.map { SwapApiParser.parseIdFromUrl(it, IdType.VEHICLES) }
                .map { PeopleVehicles(people.id, it) }
        }.flatten()

        peopleStore.insertAllPeopleVehicles(vehiclesEntities)
    }
}
