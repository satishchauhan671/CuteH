package com.digipanther.cuteh.dbHelper

import android.content.Context
import com.digipanther.cuteh.model.InstituteModel
import com.digipanther.cuteh.model.UserInfoModel
import java.lang.Exception

class UserDataHelper {

    companion object {
        fun saveUserData(userModel : UserInfoModel, context: Context?): Boolean {
            var a = false
            try {
                val userDataSource = UserDataSource.instance
                a = userDataSource!!.saveUserData(userModel, context)
            } catch (et: Exception) {
                et.printStackTrace()
            }
            return a
        }

        fun getByUserNo(mobileNo: String, context: Context?): UserInfoModel? {
            return UserDataSource.instance!!.getByUserNo(mobileNo, context)
        }

        fun getLogin(context: Context?): UserInfoModel? {
            return UserDataSource.instance!!.getLogin(context)
        }

        fun deleteRow(userModel : UserInfoModel, context: Context?) {
            return UserDataSource.instance!!.deleteRow(userModel, context)
        }

        fun deleteAll(context: Context?) {
            return UserDataSource.instance!!.deleteAll(context)
        }
    }

}