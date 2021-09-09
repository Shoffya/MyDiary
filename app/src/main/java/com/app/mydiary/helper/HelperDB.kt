package com.app.mydiary.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.app.mydiary.model.PageVO

class HelperDB(
    context: Context
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {

    companion object {
        private val NOME_BANCO = "mydiary.db"
        private val VERSAO_ATUAL = 2
    }

    val TABLE_NAME = "page"
    val COLUMNS_ID = "id"
    val COLUMNS_TITLE = "title"
    val COLUMNS_TEXT = "text"
    val COLUMNS_DATE = "date"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_TITLE TEXT NOT NULL," +
            "$COLUMNS_TEXT TEXT NOT NULL," +
            "$COLUMNS_DATE TEXT NOT NULL," +
            "" +
            "PRIMARY KEY($COLUMNS_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion) {
            db?.execSQL(DROP_TABLE)
        }
        onCreate(db)
    }

    fun searchPages(busca: String, isBuscaPorID: Boolean = false) : List<PageVO> {
        val db = readableDatabase ?: return mutableListOf()
        var lista = mutableListOf<PageVO>()
        var where: String? = null
        var args: Array<String> = arrayOf()
        if(isBuscaPorID){
            where = "$COLUMNS_ID = ?"
            args = arrayOf("$busca")
        }else{
            where = "$COLUMNS_TITLE LIKE ?"
            args = arrayOf("%$busca%")
        }
        var cursor = db.query(TABLE_NAME,null,where,args,null,null,null)
        if (cursor == null){
            db.close()
            return mutableListOf()
        }
        while(cursor.moveToNext()){
            var page = PageVO(
                cursor.getInt((cursor.getColumnIndex((COLUMNS_ID)))),
                cursor.getString((cursor.getColumnIndex(COLUMNS_TITLE))),
                cursor.getString((cursor.getColumnIndex(COLUMNS_TEXT))),
                cursor.getString((cursor.getColumnIndex(COLUMNS_DATE)))
            )
            lista.add(page)
        }
        db.close()
        return lista
    }

    fun savePage(page: PageVO) {
        val db = writableDatabase ?: return
        var content = ContentValues()
        content.put(COLUMNS_TITLE, page.title)
        content.put(COLUMNS_TEXT, page.text)
        content.put(COLUMNS_DATE, page.date)
        db.insert(TABLE_NAME,null,content)
        db.close()
    }

    fun deletePage(id: Int) {
        val db = writableDatabase ?: return
        val sql = "DELETE FROM $TABLE_NAME WHERE $COLUMNS_ID = ?"
        val arg = arrayOf("$id")
        db.execSQL(sql,arg)
        db.close()
    }

    fun updatePage(page: PageVO) {
        val db = writableDatabase ?: return
        val sql = "UPDATE $TABLE_NAME SET $COLUMNS_TITLE = ?, $COLUMNS_TEXT = ?, $COLUMNS_DATE = ? WHERE $COLUMNS_ID = ?"
        val arg = arrayOf(page.title, page.text, page.date, page.id)
        db.execSQL(sql,arg)
        db.close()
    }
}