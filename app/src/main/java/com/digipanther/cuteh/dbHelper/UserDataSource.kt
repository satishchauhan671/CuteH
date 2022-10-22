package com.digipanther.cuteh.dbHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.digipanther.cuteh.dbHelper.DatabaseHelper.Companion.MOBILE
import com.digipanther.cuteh.dbHelper.DatabaseHelper.Companion.USER_NAME
import com.digipanther.cuteh.dbHelper.DatabaseHelper.Companion.lock
import com.digipanther.cuteh.model.UserInfoModel
import java.sql.SQLException

class UserDataSource private constructor() {

    companion object {
        private var hasObject = false
        private var myDataSource: UserDataSource? = null
        val instance: UserDataSource?
            get() = if (hasObject) {
                myDataSource
            } else {
                hasObject = true
                myDataSource = UserDataSource()
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


    fun saveUserData(userModel : UserInfoModel, context: Context?): Boolean {
        var a = false
        synchronized(lock) {
            try {
                val values = ContentValues()
                values.put(USER_NAME, userModel.userName)
                values.put(MOBILE, userModel.mobile)

                if (userModel.mobile != null) {
                    val userModel1 = getByUserNo(userModel.mobile!!, context)
                    val db = getSQLiteDb(true, context)

                    val l: Long =
                        if (userModel1 == null)
                            db.insertWithOnConflict(
                                DatabaseHelper.USER_MST,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE
                            ) else
                            db.update(
                                DatabaseHelper.USER_MST,
                                values,
                                "$MOBILE=?",
                                arrayOf(userModel.mobile.toString() + "")
                            ).toLong()

                    if (l > 0) a = true
                } else {
                    val db = getSQLiteDb(true, context)
                    val l: Long = db.insertWithOnConflict(
                        DatabaseHelper.USER_MST,
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


    fun getByUserNo(mobileNo: String, context: Context?): UserInfoModel? {
        synchronized(lock) {
            var userModel: UserInfoModel? = null
            val db = getSQLiteDb(false, context)
            try {
                val cursor = db.query(
                    DatabaseHelper.USER_MST,
                    queryData,
                    MOBILE + "=?",
                    arrayOf(mobileNo),
                    null,
                    null,
                    null
                )
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    userModel = cursorData(cursor)
                }
                cursor.close()
                //db.close;
            } catch (se: android.database.SQLException) {
                se.printStackTrace()
            }
            return userModel
        }
    }


    private val queryData: Array<String>
        get() = arrayOf(
            DatabaseHelper.USER_NAME,
            MOBILE,
            DatabaseHelper.PHOTO,
        )


    @SuppressLint("Range")
    private fun cursorData(cursor: Cursor): UserInfoModel {
        val userModel = UserInfoModel()
        userModel.userName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.USER_NAME))
        userModel.mobile = cursor.getString(cursor.getColumnIndexOrThrow(MOBILE))
        userModel.img = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PHOTO))
        return userModel
    }


    fun getLogin(context: Context?): UserInfoModel? {
        var login: UserInfoModel? = null
        synchronized(lock) {
            val db: SQLiteDatabase =getSQLiteDb(false, context)
            try {
                val query = "SELECT * FROM ${DatabaseHelper.USER_MST}"
                val cursor = db.rawQuery(query, null)
                if (cursor.moveToFirst()) login = cursorData(cursor)
                cursor.close()
            } catch (se: android.database.SQLException) {
                se.printStackTrace()
            }
            return login
        }
    }

    fun deleteRow(userModel : UserInfoModel, context: Context?) {
        synchronized(lock) {
            val db = getSQLiteDb(true, context)
            db.delete(
                DatabaseHelper.USER_MST,
                MOBILE + "=?",
                arrayOf(userModel.mobile)
            )
        }
    }


    fun deleteAll(context: Context?) {
        synchronized(lock) {
            try {
                val db = getSQLiteDb(true, context)
                db.execSQL("DELETE FROM " + DatabaseHelper.USER_MST)
                //db.close;
            } catch (var8: android.database.SQLException) {
                var8.printStackTrace()
            }
        }
    }

}