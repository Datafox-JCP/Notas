package mx.datafox.notas.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import mx.datafox.notas.R
import mx.datafox.notas.databinding.ActivityMainBinding
import mx.datafox.notas.model.ExternalFileRepository
import mx.datafox.notas.model.InternalFileRepository
import mx.datafox.notas.model.Note
import mx.datafox.notas.model.NoteRepository
import mx.datafox.notas.utils.showToast

class MainActivity : AppCompatActivity() {

    // 1 la variable lazy implementa el NoteRepository, está inicialización se realiza la primera vez que se use repo (al hacer clic en un botón)
    //private val repo: NoteRepository by lazy { InternalFileRepository(this) }
    // Para usar el almacenamiento externo
    private val repo: NoteRepository by lazy { ExternalFileRepository(this) }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnWrite.setOnClickListener {
            // 1 Bloque if/else para asegurarse que se ha ingresado un nombre de archivo
            if (binding.editFileName.text?.isNotEmpty()!!) {
                // 2 Bloque try/catch porque puede fallar
                try {
                    // 3 Llamada a la función que guarda el archivo
                    repo.addNote(
                        Note(binding.editFileName.text.toString(),
                        binding.editNoteText.text.toString())
                    )
                } catch (e: Exception) { // 4 En caso de error muestra un mennsaje
                    showToast(getString(R.string.file_write_failed))
                    e.printStackTrace()
                }
                // 5 Prepara para la siguiente vez que se use
                binding.editFileName.text!!.clear()
                binding.editNoteText.text.clear()
            } else { // 6 Muestra un mensaje
                showToast(getString(R.string.provide_filename))
            }
        }

        binding.btnRead.setOnClickListener {
            if (binding.editFileName.text?.isNotEmpty() == true) {
                try {
                    val note = repo.getNote(binding.editFileName.text.toString())
                    binding.editNoteText.setText(note.noteText)
                } catch (e: Exception) {
                    showToast(getString(R.string.file_read_failed))
                    e.printStackTrace()
                }
            } else {
                showToast(getString(R.string.provide_filename))
            }
        }

        binding.btnDelete.setOnClickListener {
            if (binding.editFileName.text?.isNotEmpty() == true) {
                try {
                    if (repo.deleteNote(binding.editFileName.text.toString())) {
                        showToast(getString(R.string.file_deleted))
                    } else {
                        showToast(getString(R.string.file_could_not_be_deleted))
                    }
                } catch (e: Exception) {
                    showToast(getString(R.string.file_delete_failed))
                    e.printStackTrace()
                }
                binding.editFileName.text!!.clear()
                binding.editNoteText.text.clear()
            } else {
                showToast(getString(R.string.provide_filename))
            }
        }
    }
}