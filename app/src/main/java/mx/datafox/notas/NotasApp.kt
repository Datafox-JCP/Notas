package mx.datafox.notas

import android.app.Application
import com.google.android.material.color.DynamicColors

class NotasApp: Application() {
    /**
     * App file para adaptarse a colores dinámicos
     */
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)
    }
}