package com.example.examen1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BaseDatosLibros(contexto: Context) :
    SQLiteOpenHelper(contexto, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS) {

    companion object {
        private const val NOMBRE_BASE_DATOS = "libros.db"
        private const val VERSION_BASE_DATOS = 1
        private const val TABLA_LIBROS = "libros"
    }

    // Este método se ejecuta la primera vez que se abre la base de datos.
    override fun onCreate(baseDatos: SQLiteDatabase) {
        val crearTabla = """
            CREATE TABLE $TABLA_LIBROS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                titulo TEXT NOT NULL,
                autor TEXT NOT NULL,
                genero TEXT NOT NULL
            )
        """.trimIndent()

        baseDatos.execSQL(crearTabla)
    }

    override fun onUpgrade(baseDatos: SQLiteDatabase, versionAnterior: Int, versionNueva: Int) {
        baseDatos.execSQL("DROP TABLE IF EXISTS $TABLA_LIBROS")
        onCreate(baseDatos)
    }

    fun agregarLibro(titulo: String, autor: String, genero: String): Boolean {
        val datos = ContentValues()
        datos.put("titulo", titulo)
        datos.put("autor", autor)
        datos.put("genero", genero)

        val resultado = writableDatabase.insert(TABLA_LIBROS, null, datos)
        return resultado != -1L
    }

    fun obtenerLibros(): List<Libro> {
        val listaLibros = mutableListOf<Libro>()
        val consulta = "SELECT * FROM $TABLA_LIBROS ORDER BY id DESC"
        val cursor = readableDatabase.rawQuery(consulta, null)

        if (cursor.moveToFirst()) {
            do {
                val libro = Libro(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    titulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo")),
                    autor = cursor.getString(cursor.getColumnIndexOrThrow("autor")),
                    genero = cursor.getString(cursor.getColumnIndexOrThrow("genero"))
                )
                listaLibros.add(libro)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return listaLibros
    }

    fun editarLibro(id: Int, titulo: String, autor: String, genero: String): Boolean {
        val datos = ContentValues()
        datos.put("titulo", titulo)
        datos.put("autor", autor)
        datos.put("genero", genero)

        val filasModificadas = writableDatabase.update(
            TABLA_LIBROS,
            datos,
            "id = ?",
            arrayOf(id.toString())
        )
        return filasModificadas > 0
    }

    fun eliminarLibro(id: Int): Boolean {
        val filasEliminadas = writableDatabase.delete(
            TABLA_LIBROS,
            "id = ?",
            arrayOf(id.toString())
        )
        return filasEliminadas > 0
    }
}
