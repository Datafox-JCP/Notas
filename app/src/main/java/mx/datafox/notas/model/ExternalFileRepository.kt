package mx.datafox.notas.model

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class ExternalFileRepository(var context: Context) :
    NoteRepository {

    override fun addNote(note: Note) {
        // 1 Verificar si el amacenamiento está disponible
        if (isExternalStorageWritable()) {
            // 2 Abrir FileOutputStream con use
            FileOutputStream(noteFile(note.fileName)).use { output ->
                // 3  Escribor la nota
                output.write(note.noteText.toByteArray())
            }
        }
    }

    override fun getNote(fileName: String): Note {
        val note = Note(fileName, "")
        // 1 Verificar que se pueda leer
        if (isExternalStorageReadable()) {
            // 2 Abrir y consumir FileInputStream con .use
            FileInputStream(noteFile(fileName)).use { stream ->
                // 3 Abrir BufferReader con use para leer el archivo
                val text = stream.bufferedReader().use {
                    it.readText()
                }
                // 4 Asignar el contenido del archivo a la variable
                note.noteText = text
            }
        }
        // 5 Devolver la nota
        return note
    }

    override fun deleteNote(fileName: String): Boolean {
        return isExternalStorageWritable() && noteFile(fileName).delete()
    }

    private fun noteDirectory(): File? = context.getExternalFilesDir(null)

    private fun noteFile(fileName: String): File = File(noteDirectory(), fileName)

    private fun isExternalStorageWritable(): Boolean =
        Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    private fun isExternalStorageReadable(): Boolean =
        Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
}