package com.digipanther.cuteh.dbHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.digipanther.cuteh.dbHelper.DatabaseHelper.Companion.ID
import com.digipanther.cuteh.dbHelper.DatabaseHelper.Companion.lock
import com.digipanther.cuteh.model.InstituteModel
import java.sql.SQLException

class InstituteDataSource private constructor() {

    companion object {
        private var hasObject = false
        private var myDataSource: InstituteDataSource? = null
        val instance: InstituteDataSource?
            get() = if (hasObject) {
                myDataSource
            } else {
                hasObject = true
                myDataSource = InstituteDataSource()
                myDataSource
            }

        fun getSQLiteDb(isWritable: Boolean, context: Context?): SQLiteDatabase {
            val databaseHelper = DatabaseHelper.getInstance(context!!)
            return if (isWritable) {
                databaseHelper!!.writableDatabase
            } else {
                databaseHelper!!.readableDatabase
            }
        }
    }


    fun saveCollageData(instituteModel : InstituteModel, context: Context?): Boolean {
        var a = false
        synchronized(lock) {
            try {
                val values = ContentValues()
                values.put(DatabaseHelper.BRANCH, instituteModel.BRANCH)
                values.put(DatabaseHelper.COLLEGE_ID, instituteModel.COLLEGE_ID)
                values.put(DatabaseHelper.COLLEGE_NAME, instituteModel.COLLEGE_NAME)
                values.put(DatabaseHelper.COURSE, instituteModel.COURSE)
                values.put(DatabaseHelper.FEEDBACK, instituteModel.FEEDBACK)
                values.put(DatabaseHelper.IMAGE_PATH, instituteModel.IMAGE_PATH)
                values.put(DatabaseHelper.LATITUDE, instituteModel.LATITUDE)
                values.put(DatabaseHelper.LONGITUDE, instituteModel.LONGITUDE)
                values.put(DatabaseHelper.MOBILE_NO, instituteModel.MOBILE_NO)
                values.put(DatabaseHelper.QUERY_FROM, instituteModel.QUERY_FROM)
                values.put(DatabaseHelper.SUBJECT, instituteModel.SUBJECT)
                values.put(DatabaseHelper.COURSE_FEES, instituteModel.TUITION_FEES)
                values.put(DatabaseHelper.FEETYPE, instituteModel.FEETYPE)


                if (instituteModel.COLLEGE_ID != null) {
                    val instituteModel1 = getByCollageId(instituteModel.COLLEGE_ID!!, context)
                    val db = getSQLiteDb(true, context)

                    val l: Long =
                        if (instituteModel1 == null)
                            db.insertWithOnConflict(
                                DatabaseHelper.INSTITUTE_MST,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE
                            ) else
                            db.update(
                                DatabaseHelper.INSTITUTE_MST,
                                values,
                                "$ID=?",
                                arrayOf(instituteModel1.ID.toString() + "")
                            ).toLong()

                    if (l > 0) a = true
                } else {
                    val db = getSQLiteDb(true, context)
                    val l: Long = db.insertWithOnConflict(
                        DatabaseHelper.INSTITUTE_MST,
                        null,
                        values,
                        SQLiteDatabase.CONFLICT_REPLACE
                    )
                    if (l > 0) a = true
                }
            } catch (se: SQLException)
            {
                se.printStackTrace()
            }
            return a
        }
    }


    fun getByCollageId(accountNo: String, context: Context?): InstituteModel? {
        synchronized(lock) {
            var instituteModel: InstituteModel? = null
            val db = getSQLiteDb(false, context)
            try {
                val cursor = db.query(
                    DatabaseHelper.INSTITUTE_MST,
                    queryData,
                    DatabaseHelper.COLLEGE_ID + "=?",
                    arrayOf(accountNo),
                    null,
                    null,
                    null
                )
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    instituteModel = cursorData(cursor)
                }
                cursor.close()
                //db.close;
            } catch (se: android.database.SQLException) {
                se.printStackTrace()
            }
            return instituteModel
        }
    }


    private val queryData: Array<String>
        get() = arrayOf(
            ID,
            DatabaseHelper.BRANCH,
            DatabaseHelper.COLLEGE_ID,
            DatabaseHelper.COLLEGE_NAME,
            DatabaseHelper.COURSE,
            DatabaseHelper.FEEDBACK,
            DatabaseHelper.IMAGE_PATH,
            DatabaseHelper.LATITUDE,
            DatabaseHelper.LONGITUDE,
            DatabaseHelper.MOBILE_NO,
            DatabaseHelper.QUERY_FROM,
            DatabaseHelper.SUBJECT,
            DatabaseHelper.COURSE_FEES,
            DatabaseHelper.FEETYPE,
        )


    @SuppressLint("Range")
    private fun cursorData(cursor: Cursor): InstituteModel {
        val instituteModel = InstituteModel()
        instituteModel.ID = cursor.getInt(cursor.getColumnIndex(ID))
        instituteModel.BRANCH = cursor.getString(cursor.getColumnIndex(DatabaseHelper.BRANCH))
        instituteModel.COLLEGE_ID = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLLEGE_ID))
        instituteModel.COLLEGE_NAME = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLLEGE_NAME))
        instituteModel.COURSE = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COURSE))
        instituteModel.FEEDBACK = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FEEDBACK))
        instituteModel.IMAGE_PATH = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.IMAGE_PATH))
        instituteModel.LATITUDE = cursor.getString(cursor.getColumnIndex(DatabaseHelper.LATITUDE))
        instituteModel.LONGITUDE = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LONGITUDE))
        instituteModel.MOBILE_NO = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOBILE_NO))
        instituteModel.QUERY_FROM = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.QUERY_FROM))
        instituteModel.SUBJECT = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.SUBJECT))
        instituteModel.TUITION_FEES = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COURSE_FEES))
        instituteModel.FEETYPE = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FEETYPE))
        return instituteModel
    }


    fun getAll(context: Context?): ArrayList<InstituteModel>? {
        synchronized(DatabaseHelper.lock) {
            val itemList: ArrayList<InstituteModel> = ArrayList<InstituteModel>()
            val db: SQLiteDatabase = getSQLiteDb(
                false,
                context
            )
            try {
                val query = "SELECT * FROM ${DatabaseHelper.INSTITUTE_MST}"
                val cursor = db.rawQuery(query, null)
                if (cursor.moveToFirst()) {
                    do {
                        itemList.add(cursorData(cursor))
                    } while (cursor.moveToNext())
                }
                cursor.close()
            } catch (se: android.database.SQLException) {
                se.printStackTrace()
            }
            return itemList
        }
    }

    fun deleteRow(ciConsumerModel: InstituteModel, context: Context?) {
        synchronized(lock) {
            val db = getSQLiteDb(true, context)
            db.delete(
                DatabaseHelper.INSTITUTE_MST,
                DatabaseHelper.COLLEGE_ID + "=?",
                arrayOf(ciConsumerModel.COLLEGE_ID)
            )
        }
    }


    fun deleteAll(context: Context?) {
        synchronized(lock) {
            try {
                val db = getSQLiteDb(true, context)
                db.execSQL("DELETE FROM " + DatabaseHelper.INSTITUTE_MST)
                //db.close;
            } catch (var8: android.database.SQLException) {
                var8.printStackTrace()
            }
        }
    }

}