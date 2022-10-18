package com.digipanther.cuteh.dbHelper

import android.content.Context
import com.digipanther.cuteh.model.HotelModel
import java.lang.Exception

class HotelDataHelper {

    companion object {

        fun saveHotelData(hotelModel: HotelModel, context: Context?): Boolean {
            var a = false
            try {
                val hotelDataSource = HotelDataSource.instance
                a = hotelDataSource!!.saveHotelData(hotelModel, context)
            } catch (et: Exception) {
                et.printStackTrace()
            }
            return a
        }

        fun getByHotelId(hotelId: String, context: Context?): HotelModel? {
            return HotelDataSource.instance!!.getByHotelId(hotelId, context)
        }

        fun getAll(context: Context?): ArrayList<HotelModel>? {
            return HotelDataSource.instance!!.getAll(context)
        }

        fun deleteRow(hotelModel : HotelModel, context: Context?) {
            return HotelDataSource.instance!!.deleteRow(hotelModel, context)
        }

        fun deleteAll(context: Context?) {
            return HotelDataSource.instance!!.deleteAll(context)
        }
    }

}