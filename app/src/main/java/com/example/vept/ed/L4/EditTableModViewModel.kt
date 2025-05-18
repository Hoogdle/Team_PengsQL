package com.example.vept.ed.L4

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vept.ed.L2.EditDB

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

        // Step 1: 테이블 정보
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

        // Step 2: UNIQUE 인덱스 조회
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

        // Step 3: 참조 필드 조회
        val referencedFields = getReferencedFieldNames(tableName)

        // Step 4: 최종 리스트 구성
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

        // 향후 트리거 분석 추가 가능

        return referencedFields
    }

}
