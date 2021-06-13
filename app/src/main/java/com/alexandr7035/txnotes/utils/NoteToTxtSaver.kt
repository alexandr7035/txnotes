package com.alexandr7035.txnotes.utils

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.text.format.DateFormat
import android.util.Log
import com.alexandr7035.txnotes.db.NoteEntity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class NoteToTxtSaver {

    companion object {

        private val NOTES_DIR_NAME = "TXNotes"
        private val COPY_BUFFER_SIZE = 4096
        private val TEXT_DATE_FORMAT = "dd.MM.yyyy HH:mm"

        private const val LOG_TAG = "DEBUG_TXNOTES"

        //fun saveNotesToTxt(context: Context, notes: List<NoteEntity>) {
        fun saveNotesToTxt(context: Context, notes: List<NoteEntity>) {

            // API 29+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            }
            // API before 29
            else {

                val pathToSave = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator + NOTES_DIR_NAME + File.separator


                if (! File(pathToSave).exists()) {
                    Log.d(LOG_TAG, "create dir")
                    File(pathToSave).mkdirs()
                }


                // Export notes consistently
                notes.forEach {
                    var text = ""
                    text += "Creation date: ${DateFormat.format(TEXT_DATE_FORMAT, it.noteCreationDate * 1000)}\n"

                    if (it.noteModificationDate != it.noteCreationDate) {
                        text += "Modification date: ${DateFormat.format(TEXT_DATE_FORMAT, it.noteModificationDate * 1000)}\n"
                    }
                    else {
                        text += "Modification date: -\n"
                    }

                    text += "\n${it.note_title}\n"
                    text += "\n${it.note_text}\n"

                    val file = File(pathToSave, it.note_title + ".txt")
                    val inputStream = text.byteInputStream()
                    val outputStream = FileOutputStream(file)
                    val bytes: Long = inputStream.copyTo(outputStream, COPY_BUFFER_SIZE)

                    inputStream.close()
                    outputStream.close()
                }

            }
        }

    }
}