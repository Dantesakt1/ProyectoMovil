package com.proyectomovil.APIs.AnimalesAPI

import androidx.lifecycle.liveData
import com.proyectomovil.BDLocal.AnimalApadrinado
import retrofit2.HttpException
import java.io.IOException

object AnimalesRepository {
    fun getEspecies(pagina: Int = 0) = liveData {
        try {
            val response = ApiAnimalesClient.service.getEspecies(page = pagina)
            if (response.isSuccessful) {
                val especies = response.body()?.result?.map { it.toAnimalApadrinado() } ?: emptyList()
                emit(Result.success(especies))
            } else {
                emit(Result.failure(Exception("Error: ${response.code()}")))
            }
        } catch (e: Exception) {
            val error = when (e) {
                is HttpException -> Exception("Error de red: ${e.code()}")
                is IOException -> Exception("Error de conexión. Verifica tu internet.")
                else -> Exception("Error desconocido: ${e.message}")
            }
            emit(Result.failure(error))
        }
    }
}