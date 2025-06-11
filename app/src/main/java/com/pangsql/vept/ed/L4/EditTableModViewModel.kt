package com.pangsql.vept.ed.L4

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.pangsql.vept.ed.L2.EditDB

class EditTableModViewModel : ViewModel() {
    private var editDB: EditDB? = null
    private var itemName: String? = null
    private var itemType: String? = null

    fun setEditDB(db: EditDB) {
        editDB = db
    }


    fun createTableFromFields(tableName: String, fields: List<Field>): Boolean {
        val query = generateCreateTableQuery(tableName, fields)
        return try {
            editDB?.getWritableDB()?.execSQL(query)
            Log.d("EditTableModVM", "테이블 생성 성공: $tableName")
            true
        } catch (e: Exception) {
            Log.e("EditTableModVM", "테이블 생성 실패", e)
            false
        }
    }


    @SuppressLint("Range")
    fun getEditableFieldsForTable(tableName: String): List<FieldFix> {
        val db = editDB?.getWritableDB() ?: return emptyList()
        val fieldList = mutableListOf<FieldFix>()

        val fieldInfoMap = mutableMapOf<String, FieldFix>()
        try {
            val cursor = db.rawQuery("PRAGMA table_info($tableName);", null)
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val type = cursor.getString(cursor.getColumnIndex("type")) ?: "INTEGER"
                val isPrimaryKey = cursor.getInt(cursor.getColumnIndex("pk")) == 1
                val isNotNull = cursor.getInt(cursor.getColumnIndex("notnull")) == 1
                val defaultValue = cursor.getString(cursor.getColumnIndex("dflt_value"))
                val isAutoIncrement = isPrimaryKey && type.equals("INTEGER", ignoreCase = true)

                fieldInfoMap[name] = FieldFix(
                    name = name,
                    type = type,
                    isPrimaryKey = isPrimaryKey,
                    isNotNull = isNotNull,
                    isAutoIncrement = isAutoIncrement,
                    defaultValue = defaultValue,
                    isUnique = false // 나중에 갱신
                )
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("EditTableModVM", "테이블 필드 정보 조회 실패", e)
        }
        val uniqueFields = mutableSetOf<String>()
        try {
            val indexCursor = db.rawQuery("PRAGMA index_list($tableName);", null)
            while (indexCursor.moveToNext()) {
                val isUnique = indexCursor.getInt(indexCursor.getColumnIndex("unique")) == 1
                val indexName = indexCursor.getString(indexCursor.getColumnIndex("name"))
                if (isUnique) {
                    val infoCursor = db.rawQuery("PRAGMA index_info($indexName);", null)
                    while (infoCursor.moveToNext()) {
                        val columnName = infoCursor.getString(infoCursor.getColumnIndex("name"))
                        uniqueFields.add(columnName)
                    }
                    infoCursor.close()
                }
            }
            indexCursor.close()
        } catch (e: Exception) {
            Log.e("EditTableModVM", "UNIQUE 인덱스 조회 실패", e)
        }
        val referencedFields = getReferencedFieldNames(tableName)
        for ((name, field) in fieldInfoMap) {
            val isUnique = uniqueFields.contains(name)
            val isReferenced = referencedFields.contains(name)
            fieldList.add(field.copy(isUnique = isUnique, isReferenced = isReferenced))
        }

        return fieldList
    }


    @SuppressLint("Range")
    fun getReferencedFieldNames(tableName: String): Set<String> {
        val db = editDB?.getReadableDB() ?: return emptySet()
        val referencedFields = mutableSetOf<String>()

        try {
            val cursor = db.rawQuery("PRAGMA foreign_key_list($tableName);", null)
            while (cursor.moveToNext()) {
                val column = cursor.getString(cursor.getColumnIndex("from"))
                referencedFields.add(column)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("EditTableModVM", "FOREIGN KEY 조회 실패", e)
        }
        return referencedFields
    }

    fun dropTable(tableName: String): Boolean {
        val db = editDB?.getWritableDB() ?: return false
        return try {
            db.execSQL("DROP TABLE IF EXISTS $tableName")
            true
        } catch (e: Exception) {
            Log.e("EditTableModVM", "테이블 삭제 실패", e)
            false
        }
    }

    data class ForeignKeyInfo(
        val fromColumn: String,
        val toTable: String,
        val toColumn: String,
        val onDelete: String,
        val onUpdate: String
    )


    fun getForeignKeyInfoList(tableName: String): List<ForeignKeyInfo> {
        val db = editDB?.getReadableDB() ?: return emptyList()
        val list = mutableListOf<ForeignKeyInfo>()
        try {
            val cursor = db.rawQuery("PRAGMA foreign_key_list($tableName);", null)
            while (cursor.moveToNext()) {
                val from = cursor.getString(cursor.getColumnIndexOrThrow("from"))
                val toTable = cursor.getString(cursor.getColumnIndexOrThrow("table"))
                val toColumn = cursor.getString(cursor.getColumnIndexOrThrow("to"))
                val onDelete = cursor.getString(cursor.getColumnIndexOrThrow("on_delete")) ?: "NO ACTION"
                val onUpdate = cursor.getString(cursor.getColumnIndexOrThrow("on_update")) ?: "NO ACTION"

                list.add(ForeignKeyInfo(from, toTable, toColumn, onDelete, onUpdate))
            }
            cursor.close()
        } catch (e: Exception) {
            Log.e("FixFK", "FK 추출 실패", e)
        }
        return list
    }


    fun fixTableQuery(
        tableName: String,
        fields: List<Field>,
        foreignKeys: List<ForeignKeyInfo>
    ): String {
        val definitions = fields.joinToString(",\n  ") { field ->
            buildList {
                add("${field.name} ${field.type}")
                if (field.isPrimaryKey) add("PRIMARY KEY")
                if (field.isAutoIncrement) add("AUTOINCREMENT")
                if (field.isNotNull) add("NOT NULL")
                if (field.isUnique) add("UNIQUE")
                field.defaultValue?.let {
                    val formatted = if (it.matches(Regex("^-?\\d+(\\.\\d+)?$"))) it else "'$it'"
                    add("DEFAULT $formatted")
                }
            }.joinToString(" ")
        }

        val foreignKeyDefs = foreignKeys.joinToString(",\n  ") { fk ->
            """FOREIGN KEY("${fk.fromColumn}") REFERENCES "${fk.toTable}"("${fk.toColumn}") ON DELETE ${fk.onDelete} ON UPDATE ${fk.onUpdate}"""
        }

        val allDefs = if (foreignKeyDefs.isNotBlank()) {
            "$definitions,\n  $foreignKeyDefs"
        } else {
            definitions
        }

        return """CREATE TABLE "$tableName" (
  $allDefs
);"""
    }





    fun recreateTableWithFix(tableName: String, fields: List<Field>): Boolean {
        val db = editDB?.getWritableDB() ?: return false

        return try {
            val foreignKeys = getForeignKeyInfoList(tableName)
            val createSQL = fixTableQuery(tableName, fields, foreignKeys)

            db.beginTransaction()
            db.execSQL("DROP TABLE IF EXISTS \"$tableName\"")
            db.execSQL(createSQL)
            db.setTransactionSuccessful()
            true
        } catch (e: Exception) {
            Log.e("FixTableVM", "테이블 재생성 실패", e)
            false
        } finally {
            db.endTransaction()
        }
    }



}
