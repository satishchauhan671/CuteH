package com.digipanther.cuteh.dbHelper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {


    companion object {
        private const val DB_NAME = "cuteH.db"
        private const val DB_VERSION = 1
        private var databaseHelper: DatabaseHelper? = null
        val lock = Any()

        //tables
        const val HOTEL_MST = "HOTEL_MST"
        const val INSTITUTE_MST = "INSTITUTE_MST"
        const val USER_MST = "USER_MST"

        //common column fiels name
        const val ID = "ID"
        const val ADDRESS = "ADDRESS"
        const val MOBILE_NO = "MOBILE_NO"
        const val LATITUDE = "LATITUDE"
        const val LONGITUDE = "LONGITUDE"
        const val FEEDBACK = "FEEDBACK"
        const val IMAGE_PATH = "IMAGE_PATH"

        //hotel tables fields
        const val HOTEL_ID = "HOTEL_ID"
        const val BUSINESS_NAME = "BUSINESS_NAME"
        const val ROOMS = "ROOMS"
        const val RATE = "RATE"
        const val DINE_TABLE = "DINE_TABLE"
        const val FOOD_MENU = "FOOD_MENU"
        const val STAFF = "STAFF"
        const val WINE_BEER_AVAILABLE = "WINE_BEER_AVAILABLE"
        const val BOOK_NOW = "BOOK_NOW"
        const val PRICE = "PRICE"

        //institute tables fields
        const val COLLEGE_ID = "COLLEGE_ID"
        const val COLLEGE_NAME = "COLLEGE_NAME"
        const val COURSE_FEES = "COURSE_FEES"
        const val COURSE = "COURSE"
        const val BRANCH = "BRANCH"
        const val SUBJECT = "SUBJECT"
        const val QUERY_FROM = "QUERY_FROM"

        //user tables fields
        const val USER_ID = "USER_ID"
        const val USER_NAME = "USER_NAME"
        const val MOBILE = "MOBILE"
        const val PHOTO = "PHOTO"


        @Synchronized
        fun getInstance(context: Context): DatabaseHelper? {
            synchronized(lock) {
                if (databaseHelper == null) {
                    databaseHelper = DatabaseHelper(context.applicationContext)
                }
            }
            return databaseHelper
        }
    }


    override fun onCreate(db: SQLiteDatabase) {
        val TABLE_HOTEL_MST = "CREATE TABLE " + HOTEL_MST + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                HOTEL_ID + " TEXT," +
                BUSINESS_NAME + " TEXT," +
                ADDRESS + " TEXT," +
                MOBILE_NO + " TEXT," +
                ROOMS + " TEXT," +
                IMAGE_PATH + " TEXT," +
                RATE + " TEXT," +
                DINE_TABLE + " INTEGER," +
                FOOD_MENU + " TEXT," +
                STAFF + " TEXT," +
                WINE_BEER_AVAILABLE + " TEXT," +
                BOOK_NOW + " TEXT," +
                FEEDBACK + " TEXT," +
                PRICE + " TEXT," +
                LATITUDE + " TEXT," +
                LONGITUDE + " TEXT" +
                ");"



        val TABLE_INSTITUTE_MST = "CREATE TABLE " + INSTITUTE_MST + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLLEGE_ID + " TEXT," +
                COURSE_FEES + " INTEGER," +
                MOBILE_NO + " INTEGER," +
                COURSE + " TEXT," +
                BRANCH + " TEXT," +
                SUBJECT + " SUBJECT," +
                IMAGE_PATH + " IMAGE_PATH," +
                QUERY_FROM + " QUERY_FROM," +
                FEEDBACK + " FEEDBACK," +
                LATITUDE + " LATITUDE," +
                LONGITUDE + " LONGITUDE" +
                ");"

        val TABLE_USER_MST = "CREATE TABLE " + USER_MST + "(" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_NAME + " TEXT," +
                PHOTO + " TEXT," +
                MOBILE + " INTEGER" +
                ");"


        db.execSQL(TABLE_HOTEL_MST)
        db.execSQL(TABLE_INSTITUTE_MST)
        db.execSQL(TABLE_USER_MST)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }




}