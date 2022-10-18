package com.digipanther.cuteh.dbHelper

import android.content.Context
import com.digipanther.cuteh.model.InstituteModel
import java.lang.Exception

class InstituteDataHelper {

    companion object {
        fun saveCollageData(instituteModel : InstituteModel, context: Context?): Boolean {
            var a = false
            try {
                val instituteDataSource = InstituteDataSource.instance
                a = instituteDataSource!!.saveCollageData(instituteModel, context)
            } catch (et: Exception) {
                et.printStackTrace()
            }
            return a
        }

        fun getByCollageId(instituteModel: String, context: Context?): InstituteModel? {
            return InstituteDataSource.instance!!.getByCollageId(instituteModel, context)
        }

        fun getAll(context: Context?): ArrayList<InstituteModel>? {
            return InstituteDataSource.instance!!.getAll(context)
        }

        fun deleteRow(instituteModel : InstituteModel, context: Context?) {
            return InstituteDataSource.instance!!.deleteRow(instituteModel, context)
        }

        fun deleteAll(context: Context?) {
            return InstituteDataSource.instance!!.deleteAll(context)
        }
    }

}