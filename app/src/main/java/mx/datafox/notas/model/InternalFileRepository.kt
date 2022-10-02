package mx.datafox.notas.model

import android.content.Context
import java.io.File

class InternalFileRepository(var context: Context) :
    NoteRepository {

    override fun addNote(note: Note) {
        // 1 Crear y guardar el archivo en modo privado
        context.openFileOutput(note.fileName, Context.MODE_PRIVATE).use { output ->
            output.write(note.noteText.toByteArray())
        }
    }

    override fun getNote(fileName: String): Note {
        // 1 Crear la nota con el nombre de archivo
        val note = Note(fileName, "")
        // 2 Abrir y consumir el FileInputStream con la instrucción .use
        context.openFileInput(fileName).use { stream ->
            // 3 Abrir el BufferReader con .use para leer el archivo
            val text = stream.bufferedReader().use {
                it.readText()
            }
            // 4 Asignar el texto leído a la variable
            note.noteText = text
        }
        // 5 Devolver la nota
        return note
    }

    override fun deleteNote(fileName: String): Boolean {
        // 1  Devuelve que se ha borrado la nota
        return noteFile(fileName).delete()
    }

    private fun noteFile(fileName: String): File = File(noteDirectory(), fileName)

    private fun noteDirectory(): String = context.filesDir.absolutePath

}