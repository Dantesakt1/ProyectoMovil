import android.content.Context
import android.content.SharedPreferences
import com.proyectomovil.BDLocal.AnimalApadrinado
import com.proyectomovil.BDLocal.Estadisticas
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

object ApadrinamientosManager {

    private const val PREFS_NAME = "apadrinamientos_prefs"
    private const val KEY_ANIMALES = "animales_apadrinados"
    private const val KEY_TOTAL_DONADO = "total_donado"

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // ═══════════════════════════════════════════════════════════
    // FUNCIONES PRINCIPALES
    // ═══════════════════════════════════════════════════════════

    // Guardar lista completa de animales
    fun guardarAnimales(context: Context, animales: List<AnimalApadrinado>) {
        val type = Types.newParameterizedType(List::class.java, AnimalApadrinado::class.java)
        val adapter = moshi.adapter<List<AnimalApadrinado>>(type)
        val json = adapter.toJson(animales)

        getPrefs(context).edit()
            .putString(KEY_ANIMALES, json)
            .apply()
    }

    // Obtener todos los animales apadrinados
    fun obtenerAnimales(context: Context): List<AnimalApadrinado> {
        val json = getPrefs(context).getString(KEY_ANIMALES, "[]") ?: "[]"
        val type = Types.newParameterizedType(List::class.java, AnimalApadrinado::class.java)
        val adapter = moshi.adapter<List<AnimalApadrinado>>(type)
        return adapter.fromJson(json) ?: emptyList()
    }

    // Apadrinar un nuevo animal
    fun apadrinarAnimal(context: Context, animal: AnimalApadrinado) {
        val animales = obtenerAnimales(context).toMutableList()

        // Verificar que no esté ya apadrinado
        if (animales.none { it.id == animal.id }) {
            animales.add(animal)
            guardarAnimales(context, animales)

            // Actualizar total donado
            val nuevoTotal = obtenerTotalDonado(context) + animal.aporteMensual
            guardarTotalDonado(context, nuevoTotal)
        }
    }

    // Dejar de apadrinar un animal
    fun dejarDeApadrinar(context: Context, animalId: Int) {
        val animales = obtenerAnimales(context).toMutableList()
        val animalEliminado = animales.find { it.id == animalId }

        animales.removeAll { it.id == animalId }
        guardarAnimales(context, animales)

        // Actualizar total donado
        if (animalEliminado != null) {
            val nuevoTotal = obtenerTotalDonado(context) - animalEliminado.aporteMensual
            guardarTotalDonado(context, nuevoTotal.coerceAtLeast(0.0))
        }
    }

    // ═══════════════════════════════════════════════════════════
    // FUNCIONES DE ESTADÍSTICAS
    // ═══════════════════════════════════════════════════════════

    // Guardar total donado
    fun guardarTotalDonado(context: Context, total: Double) {
        getPrefs(context).edit()
            .putFloat(KEY_TOTAL_DONADO, total.toFloat())
            .apply()
    }

    // Obtener total donado
    fun obtenerTotalDonado(context: Context): Double {
        return getPrefs(context).getFloat(KEY_TOTAL_DONADO, 0f).toDouble()
    }

    // Calcular todas las estadísticas
    fun obtenerEstadisticas(context: Context): Estadisticas {
        val animales = obtenerAnimales(context)
        return Estadisticas(
            totalAnimales = animales.size,
            aporteMensual = animales.sumOf { it.aporteMensual },
            totalDonado = obtenerTotalDonado(context),
            actualizaciones = animales.count { it.tieneActualizacion }
        )
    }

    // ═══════════════════════════════════════════════════════════
    // FUNCIONES AUXILIARES
    // ═══════════════════════════════════════════════════════════

    // Verificar si un animal ya está apadrinado
    fun estaApadrinado(context: Context, animalId: Int): Boolean {
        return obtenerAnimales(context).any { it.id == animalId }
    }

    // Limpiar todos los datos (para testing)
    fun limpiarTodo(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}