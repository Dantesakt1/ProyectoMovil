package com.proyectomovil.APIs.AnimalesAPI

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiAnimalesService {
    @GET("species/region/{region}/{page}")
    suspend fun getEspecies(
        @Path("region") region: String = "am",
        @Path("page") page: Int
    ): Response<SpeciesResponse>
}