package com.digipanther.cuteh.dbHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.digipanther.cuteh.dbHelper.DatabaseHelper.Companion.ID
import com.digipanther.cuteh.dbHelper.DatabaseHelper.Companion.lock
import com.digipanther.cuteh.model.HotelModel
import java.sql.SQLException

class HotelDataSource private constructor() {

    companion object {
        private var hasObject = false
        private var myDataSource: HotelDataSource? = null
        val instance: HotelDataSource?
            get() = if (hasObject) {
                myDataSource
            } else {
                hasObject = true
                myDataSource = HotelDataSource()
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


    fun saveHotelData(hotelMode: HotelModel, context: Context?): Boolean {
        var a = false
        synchronized(lock) {
            try {
                val values = ContentValues()
                values.put(DatabaseHelper.HOTEL_ID, hotelMode.HOTEL_ID)
                values.put(DatabaseHelper.BUSINESS_NAME, hotelMode.BUSINESS_NAME)
                values.put(DatabaseHelper.ADDRESS, hotelMode.ADDRESS)
                values.put(DatabaseHelper.MOBILE_NO, hotelMode.MOBILE_NO)
                values.put(DatabaseHelper.ROOMS, hotelMode.ROOMS)
                values.put(DatabaseHelper.IMAGE_PATH, hotelMode.IMAGE_PATH)
                values.put(DatabaseHelper.RATE, hotelMode.RATE)
                values.put(DatabaseHelper.DINE_TABLE, hotelMode.DINE_TABLE)
                values.put(DatabaseHelper.FOOD_MENU, hotelMode.FOOD_MENU)
                values.put(DatabaseHelper.STAFF, hotelMode.STAFF)
                values.put(DatabaseHelper.WINE_BEER_AVAILABLE, hotelMode.WINE_BEER_AVAILABLE)
                values.put(DatabaseHelper.BOOK_NOW, hotelMode.BOOK_NOW)
                values.put(DatabaseHelper.FEEDBACK, hotelMode.FEEDBACK)
                values.put(DatabaseHelper.LATITUDE, hotelMode.LATITUDE)
                values.put(DatabaseHelper.LONGITUDE, hotelMode.LONGITUDE)


                if (hotelMode.HOTEL_ID != null) {
                    val hotelMode1 = getByHotelId(hotelMode.HOTEL_ID!!, context)
                    val db = getSQLiteDb(true, context)

                    val l: Long =
                        if (hotelMode1 == null)
                            db.insertWithOnConflict(
                                DatabaseHelper.HOTEL_MST,
                                null,
                                values,
                                SQLiteDatabase.CONFLICT_REPLACE
                            ) else
                            db.update(
                                DatabaseHelper.HOTEL_MST,
                                values,
                                "$ID=?",
                                arrayOf(hotelMode1.ID.toString() + "")
                            ).toLong()

                    if (l > 0) a = true
                } else {
                    val db = getSQLiteDb(true, context)
                    val l: Long = db.insertWithOnConflict(
                        DatabaseHelper.HOTEL_MST,
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


    fun getByHotelId(accountNo: String, context: Context?): HotelModel? {
        synchronized(lock) {
            var hotelModel: HotelModel? = null
            val db = getSQLiteDb(false, context)
            try {
                val cursor = db.query(
                    DatabaseHelper.HOTEL_MST,
                    queryData,
                    DatabaseHelper.HOTEL_ID + "=?",
                    arrayOf(accountNo),
                    null,
                    null,
                    null
                )
                // looping through all rows and adding to list
                if (cursor.moveToFirst()) {
                    hotelModel = cursorData(cursor)
                }
                cursor.close()
                //db.close;
            } catch (se: android.database.SQLException) {
                se.printStackTrace()
            }
            return hotelModel
        }
    }


    private val queryData: Array<String>
        get() = arrayOf(
            ID,
            DatabaseHelper.HOTEL_ID,
            DatabaseHelper.BUSINESS_NAME,
            DatabaseHelper.ADDRESS,
            DatabaseHelper.MOBILE_NO,
            DatabaseHelper.ROOMS,
            DatabaseHelper.IMAGE_PATH,
            DatabaseHelper.RATE,
            DatabaseHelper.DINE_TABLE,
            DatabaseHelper.FOOD_MENU,
            DatabaseHelper.STAFF,
            DatabaseHelper.WINE_BEER_AVAILABLE,
            DatabaseHelper.BOOK_NOW,
            DatabaseHelper.FEEDBACK,
            DatabaseHelper.PRICE,
            DatabaseHelper.LATITUDE,
            DatabaseHelper.LONGITUDE,
        )


    @SuppressLint("Range")
    private fun cursorData(cursor: Cursor): HotelModel {
        val hotelModel = HotelModel()
        hotelModel.ID = cursor.getInt(cursor.getColumnIndex(ID))
        hotelModel.HOTEL_ID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.HOTEL_ID))
        hotelModel.BUSINESS_NAME = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.BUSINESS_NAME))
        hotelModel.ADDRESS = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ADDRESS))
        hotelModel.MOBILE_NO = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.MOBILE_NO))
        hotelModel.ROOMS = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.ROOMS))
        hotelModel.IMAGE_PATH = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.IMAGE_PATH))
        hotelModel.RATE = cursor.getString(cursor.getColumnIndex(DatabaseHelper.RATE))
        hotelModel.DINE_TABLE = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.DINE_TABLE))
        hotelModel.FOOD_MENU = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FOOD_MENU))
        hotelModel.STAFF = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.STAFF))
        hotelModel.WINE_BEER_AVAILABLE = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.WINE_BEER_AVAILABLE))
        hotelModel.BOOK_NOW = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.BOOK_NOW))
        hotelModel.FEEDBACK = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FEEDBACK))
        hotelModel.PRICE = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PRICE))
        hotelModel.LATITUDE = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LATITUDE))
        hotelModel.LONGITUDE = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LONGITUDE))
        return hotelModel
    }


    fun getAll(context: Context?): ArrayList<HotelModel>? {
        synchronized(DatabaseHelper.lock) {
            val itemList: ArrayList<HotelModel> = ArrayList<HotelModel>()
            val db: SQLiteDatabase = getSQLiteDb(
                false,
                context
            )
            try {
                val query = "SELECT * FROM ${DatabaseHelper.HOTEL_MST}"
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

    fun deleteRow(ciConsumerModel: HotelModel, context: Context?) {
        synchronized(lock) {
            val db = getSQLiteDb(true, context)
            db.delete(
                DatabaseHelper.HOTEL_MST,
                DatabaseHelper.HOTEL_ID + "=?",
                arrayOf(ciConsumerModel.HOTEL_ID)
            )
        }
    }


    fun deleteAll(context: Context?) {
        synchronized(lock) {
            try {
                val db = getSQLiteDb(true, context)
                db.execSQL("DELETE FROM " + DatabaseHelper.HOTEL_MST)
                //db.close;
            } catch (var8: android.database.SQLException) {
                var8.printStackTrace()
            }
        }
    }

}