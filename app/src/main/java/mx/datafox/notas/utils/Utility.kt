package mx.datafox.notas.utils

import android.app.Activity
import android.widget.Toast

/**
 * Utility class para mostrar un Toast
 */
fun Activity.showToast(msg: String) = Toast
    .makeText(this, msg, Toast.LENGTH_LONG)
    .show()