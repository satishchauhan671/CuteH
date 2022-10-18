package com.digipanther.cuteh.common

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.location.Geocoder
import android.location.LocationManager
import android.media.ExifInterface
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.SupplicantState
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.*
import android.provider.Settings
import android.provider.SyncStateContract
import android.provider.UserDictionary.Words.FREQUENCY
import android.telecom.Call
import android.telephony.TelephonyManager
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.digipanther.cuteh.R
import com.digipanther.cuteh.activity.LoginActivity
import com.digipanther.cuteh.app.MyApplication
import com.digipanther.cuteh.dbHelper.UserDataHelper
import com.digipanther.cuteh.model.UserInfoModel
import com.google.android.material.snackbar.Snackbar
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.math.BigDecimal
import java.net.*
import java.nio.charset.StandardCharsets
import java.text.*
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.experimental.xor


object Utility {
    private const val MY_PREFS_NAME = "MyPrefsFile"
    private val TAG = Utility::class.java.simpleName
    private const val update = false
    private const val cancel = 0

    //  private static Iterator<Row> rowIter;
    private val jsonString: JSONObject? = null
    private const val uploadCount = 0
    private const val errCount = 0
    fun playBeep(context: Context, beepSoundFile: String?) {
        var m = MediaPlayer()
        try {
            if (m.isPlaying) {
                m.stop()
                m.release()
                m = MediaPlayer()
            }
            val descriptor = context.assets.openFd(beepSoundFile ?: "beep.mp3")
            m.setDataSource(descriptor.fileDescriptor, descriptor.startOffset, descriptor.length)
            descriptor.close()
            m.prepare()
            m.setVolume(1f, 1f)
            m.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideSoftKeyboard(context: Context, view: View?) {
        if (view != null) {
            if (view is EditText) {
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
                view.clearFocus()
            }
        }
    }

    /* public static File convertBitmapToFile(Context context, Bitmap bitmap, String imageFileName) {
         File f = null;
         try {
             f = new File(Environment.getExternalStorageDirectory().toString() + "/Android/media/com.inventive.ugobill/" + context.getResources().getString(R.string.app_name), imageFileName);
             //Convert bitmap to byte array
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             bitmap.compress(Bitmap.CompressFormat.PNG, 100 , bos);
             byte[] bitmapdata = bos.toByteArray();

             //write the bytes in file
             FileOutputStream fos = new FileOutputStream(f);
             fos.write(bitmapdata);
             fos.flush();
             fos.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
         return f;
     }
 */
    fun convertBitmapToFile(context: Context, bitmap: Bitmap, imageFileName: String?): File? {
        var f: File? = null
        try {
            f = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName)
            val bos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos)
            val bitmapdata = bos.toByteArray()
            val fos = FileOutputStream(f)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return f
    }

    fun deleteSignature(context: Context): Boolean {
        try {
            val dir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).toString())
            if (dir.isDirectory) {
                for (file in dir.listFiles()) {
                    file.delete()
                }
                return true
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return false
    }

    fun bitmapToBASE64(bitmap: Bitmap?): String {
        return if (bitmap != null) {
            val baos = ByteArrayOutputStream()
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val imageBytes = baos.toByteArray()
            Base64.encodeToString(imageBytes, Base64.DEFAULT)
        } else ""
    }

    fun isMyServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun setStatusBarGradiant(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            val background = activity.resources.getDrawable(R.color.colorAccent, null)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#00000000")
            window.navigationBarColor = Color.parseColor("#00000000")
            window.setBackgroundDrawable(background)
        }
    }

    fun setEditTextMaxLength(editText: EditText, length: Int) {
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = LengthFilter(length)
        editText.filters = filterArray
    }

    fun isValidMail(view: View?, email: String?): Boolean {
        val check: Boolean
        val p: Pattern
        val m: Matcher
        val emailRegex = "[a-zA-Z0-9][a-zA-Z0-9_.]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+"
        val EMAIL_STRING = ("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")
        p = Pattern.compile(emailRegex)
        m = p.matcher(email)
        check = m.matches()
        if (!check) {
            snackBar(view, "Email ID is Not Valid", 1000, Color.parseColor("#f32013"))
        }
        return check
    }

    fun getBatteryStatus(context: Context): Int {
        var batLevel = 0
        try {
            val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
            batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return batLevel
    }

    fun isValidMobile(view: View?, mobile: String): Boolean {
        val check: Boolean
        val mobileRegex = "[6-9][0-9]{9}"
        check = mobile.matches(mobileRegex.toRegex())
        if (!check) {
            snackBar(view, "Mobile Number is not valid", 1200, Color.parseColor("#f32013"))
        }
        return check
    }

    fun isValidPinCode(view: View?, mobile: String): Boolean {
        val check: Boolean
        val pinCodeRegex = "[1-9][0-9]{5}"
        check = mobile.matches(pinCodeRegex.toRegex())
        if (!check) {
            snackBar(view, "Please Enter Valid PinCode", 1200, Color.parseColor("#f32013"))
        }
        return check
    }

    fun hideSoftKeyboard(activity: Activity) {
        hideSoftKeyboard(activity, activity.currentFocus)
    }

    fun addFragment(fragment: Fragment?, fragmentManager: FragmentManager, resId: Int) {
        try {
            if (fragment != null) {
                val fragmentTransaction = fragmentManager.beginTransaction()
                fragmentTransaction.add(resId, fragment).commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun replaceFragment(fragment: Fragment?, fragmentManager: FragmentManager, resId: Int) {
        if (fragment != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            //   fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(resId, fragment)
            // fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    @JvmStatic
    fun replaceFragmentWithBundle(
        fragment: Fragment?,
        fragmentManager: FragmentManager,
        resId: Int,
        bundle: Bundle
    ) {
        if (fragment != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            //   fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragment.arguments = bundle
            fragmentTransaction.replace(resId, fragment)
            // fragmentTransaction.commit();
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commitAllowingStateLoss()
        }
    }

    fun clearAllFragment(fragmentActivity: FragmentActivity) {
        val fm = fragmentActivity.supportFragmentManager // or 'getSupportFragmentManager();'
        val count = fm.backStackEntryCount
        for (i in 0 until count) {
            fm.popBackStack()
        }
    }


    /*ConnectivityManager cm = (ConnectivityManager) MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                //isRightWifi = activeNetwork.getExtraInfo().contains("INVENTIA");
                isRightWifi = true;
            }*/


    fun isOnline(context: Context): Boolean {
        var connection = false
        try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            connection = netInfo != null && netInfo.isConnectedOrConnecting
            if (connection) {
                // INVENTIVE_WIFI device does not have internet access
                val wifiManager =
                    context.getSystemService(
                        Context.WIFI_SERVICE
                    ) as WifiManager
                val ssid = wifiManager.connectionInfo.ssid
                if (ssid.contains("INVENTIA")) connection = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return connection
    }

    fun getRandom(maxNumber: Int): Int {
        val random = Random()
        return random.nextInt(maxNumber)
    }

    fun saveBooleanPreference(key: String?, value: Boolean) {
        val editor: SharedPreferences.Editor = MyApplication.mInstance?.getSharedPreferences(
            MY_PREFS_NAME, Context.MODE_PRIVATE
        )!!.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanPreference(key: String?): Boolean {
        return MyApplication.mInstance?.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
            ?.getBoolean(key, false)!!
    }

    fun saveStringPreference(key: String?, value: String?) {
        val editor: SharedPreferences.Editor = MyApplication.mInstance?.getSharedPreferences(
            MY_PREFS_NAME, Context.MODE_PRIVATE
        )!!.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveLongPreference(key: String?, value: Long) {
        val editor: SharedPreferences.Editor = MyApplication.mInstance?.getSharedPreferences(
            MY_PREFS_NAME, Context.MODE_PRIVATE
        )!!.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun saveIntPreference(key: String?, value: Int) {
        val editor: SharedPreferences.Editor = MyApplication.mInstance?.getSharedPreferences(
            MY_PREFS_NAME, Context.MODE_PRIVATE
        )!!.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getIntPreference(key: String?): Int {
        return MyApplication.mInstance?.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
            ?.getInt(key, 0)!!
    }

    fun getLongPreference(key: String?): Long {
        return MyApplication.mInstance?.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
            ?.getLong(key, 0)!!
    }

    fun getStringPreference(key: String?): String {
        return MyApplication.mInstance?.getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE)
            ?.getString(key, null)!!
    }

    fun getMaxArrayValue(arr: DoubleArray): Double {
        var max = arr[0]
        // max = a[0];
        for (anArr in arr) {
            if (max < anArr) {
                max = anArr
            }
        }
        return max
    }

    fun createStringDate(day: Int, month: Int, year: Int): String {
        return String.format(Locale.ENGLISH, "%02d", day) + "-" + String.format(
            Locale.ENGLISH,
            "%02d",
            month
        ) + "-" + year
    }

    fun createStringDate(day: Int, month: String, year: Int): String {
        return String.format(Locale.ENGLISH, "%02d", day) + "-" + month + "-" + year
    }

    // Old IbillApp
    fun correctDateFormatWithTime(date: String?): String? {
        /*
         * 3/5/18 02:04, 3/5/18 2:4, 3/5/1802:04, 3/5/201802:04, 3/5/18 02:04:AM, 3/5/1802:04:PM, 3/5/1802:04:AM
         * 3/5/2018 2:04, 3/5/2018 02:04, 28/05/18 5:00:00:PM, 28/05/185:00:00:PM, 28/05/2018 5:00:00:PM, 28/05/20185:00:00:PM
         * 28/05/2018 5:00:AM, 28/05/20185:00:AM, 28/05/2018 5:00:PM, 28/05/20185:00:PM, 28/05/18 5:00:00:000
         * 28/05/18 5:00:00, 28/05/18 5:00:00:000:PM, 28/05/185:00:00:000:AM, 28/05/2018 5:00:00:000:PM, 28/05/185:00:00:000:PM
         */
        var date1 = date
        if (date != null && !date.isEmpty()) {
            if (date.equals("12:00AM", ignoreCase = true) || date.equals("12:AM", ignoreCase = true)
                || date.equals("00:00:00", ignoreCase = true) || date.equals(
                    "0/0/0 0:0",
                    ignoreCase = true
                )
                || date.equals("0/0/0 0:0:0", ignoreCase = true)
                || date.equals(
                    "00/00/00 00:00",
                    ignoreCase = true
                ) || date.equals("00/00/00 00:00:00", ignoreCase = true)
            ) {
                return "00-000-0000 00:00:00"
            }
            if (date.contains("/")) {
                val a = date.split("/".toRegex()).toTypedArray()
                if (a.size == 3) {
                    if (!a[2].contains(" ")) date1 = padString(a[0]) + "-" + padString(
                        a[1]
                    ) + "-" + correctYearTimeFormat(a[2]) else {
                        val b: Array<String?> = a[2].split(" ".toRegex()).toTypedArray()
                        if (b.size == 3) {
                            if (b[0]!!.length == 2) b[0] = "20" + b[0]
                            // b[1] = timeAMPMFormat(b[1], b[2])
                            date1 = padString(a[0]) + "-" + padString(
                                a[1]
                            ) + "-" + b[0] + " " + b[1]
                        } else if (b.size == 2) {
                            if (b[0]!!.length == 2) b[0] = "20" + b[0]
                            //   b[1] = timeAMPMFormat(b[1])
                            date1 = padString(a[0]) + "-" + padString(
                                a[1]
                            ) + "-" + b[0] + " " + b[1]
                        }
                    }
                }
            } else if (date.contains("-")) {
                val a = date.split("-".toRegex()).toTypedArray()
                if (a.size == 3) {
                    val b = a[2].split(" ".toRegex()).toTypedArray()
                    date1 = if (b[0].length == 4) {
                        val d = b[0].substring(2)
                        padString(a[0]) + "-" + padString(
                            a[1]
                        ) + "-20" + d + " "
                        //+ timeAMPMFormat(b[1])
                    } else padString(a[0]) + "-" + padString(
                        a[1]
                    ) + "-" + correctYearTimeFormat(a[2])
                }
            }
        }
        return dateStringWithTime(stringToDateWithTime(date1))
    }


    fun getNetworkClass(context: Context): String? {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = cm.activeNetworkInfo
        if (info == null || !info.isConnected) return "-" // not connected
        if (info.type == ConnectivityManager.TYPE_WIFI) return "WIFI"
        if (info.type == ConnectivityManager.TYPE_MOBILE) {
            return when (info.subtype) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN, TelephonyManager.NETWORK_TYPE_GSM -> "2G"
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP, TelephonyManager.NETWORK_TYPE_TD_SCDMA -> "3G"
                TelephonyManager.NETWORK_TYPE_LTE, TelephonyManager.NETWORK_TYPE_IWLAN, 19 -> "4G"
                TelephonyManager.NETWORK_TYPE_NR -> "5G"
                else -> "?"
            }
        }
        return "?"
    }


    // Old IbillApp
    fun stringToDateWithTime(Date: String?): Date? {
        var date: Date? = null
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
            date = formatter.parse(Date)
        } catch (pe: ParseException) {
            // pe.printStackTrace();
            try {
                val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH)
                date = formatter.parse(Date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        return date
    }

    fun dateToMilli(date: String?): Long {
        var l: Long = 0
        val sdf = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        try {
            val mDate = sdf.parse(date)
            l = mDate.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return l
    }

    fun setDownloadDate(strDate: String?): String? {
        var date1: String? = null
        var dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
        try {
            val varDate = dateFormat.parse(strDate)
            dateFormat = SimpleDateFormat("dd-MMM-yyyy")
            date1 = dateFormat.format(varDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date1
    }

    fun convertGraphDate(strDate: String?): String? {
        var date1: String? = null
        var dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH)
        try {
            val varDate = dateFormat.parse(strDate)
            dateFormat = SimpleDateFormat("MMM dd")
            date1 = dateFormat.format(varDate)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return date1
    }

    fun addMonths(date: Date?, months: Int): Date {
        val calender = Calendar.getInstance()
        calender.time = date
        calender.add(Calendar.MONTH, months)
        return calender.time
    }

    fun addDays(date: Date?, days: Int): Date {
        val calender = Calendar.getInstance()
        calender.time = date
        calender.add(Calendar.DAY_OF_MONTH, days)
        return calender.time
    }

    fun addDays_format(date: Date?, days: Int): String {
        val dateFormat: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
        val calender = Calendar.getInstance()
        calender.time = date
        calender.add(Calendar.DAY_OF_MONTH, days)
        return dateFormat.format(calender.time)
    }

    fun getMonth(date: Date?): Int {
        val calender = Calendar.getInstance()
        calender.time = date
        return calender[Calendar.MONTH] + 1
    }

    fun getYear(date: Date?): Int {
        val calender = Calendar.getInstance()
        calender.time = date
        return calender[Calendar.YEAR]
    }

    val currentDate: String?
        get() = dateToString(Date())

    fun setDate(view: TextView) {
        val today = Calendar.getInstance().time //getting date
        val formatter = SimpleDateFormat("dd-MMM-yyyy") //formating according to my need
        val date = formatter.format(today)
        view.text = date
    }

    fun dateToString(date: Date?): String? {
        var date1: String? = null
        try {
            val formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            date1 = formatter.format(date)
        } catch (pe: Exception) {
            pe.printStackTrace()
        }
        return date1
    }

    fun dateStringWithTime(date: Date?): String? {
        var date1: String? = null
        try {
            val formatter = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH)
            date1 = formatter.format(date)
        } catch (pe: Exception) {
            pe.printStackTrace()
        }
        return date1
    }

    fun dateStringWithTimeHrMinute(date: Date?): String? {
        var date1: String? = null
        try {
            val formatter = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH)
            date1 = formatter.format(date)
        } catch (pe: Exception) {
            pe.printStackTrace()
        }
        return date1
    }

    fun dateWithHrMinute(date: String?): Date? {
        var date1: Date? = null
        try {
            val formatter = SimpleDateFormat("dd-MMM-yyyy HH:mm", Locale.ENGLISH)
            date1 = formatter.parse(date)
        } catch (pe: Exception) {
            pe.printStackTrace()
        }
        return date1
    }

    fun getDate_WithoutTime(date: Date?): Date? {
        var dateWithoutTime: Date? = null
        try {
            val sdf = SimpleDateFormat("dd-MMM-yyyy")
            dateWithoutTime = sdf.parse(sdf.format(date))
        } catch (e: Exception) {
            e.message
        }
        return dateWithoutTime
    }

    fun getDay(date: Date?): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal[Calendar.DAY_OF_MONTH]
    }

    fun getBillingPercentage(totalVisited: Double, totalConsumer: Double): Double {
        val df = DecimalFormat("#.00")
        val total = df.format(totalVisited * 100 / totalConsumer)
        return getDoubleValue(total)
    }

    val date: String?
        get() {
            val sdf = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH)
            val cal = Calendar.getInstance()
            val date = cal.time
            return setDownloadDate(sdf.format(date))
        }

    fun stringToDate(Date: String?): Date? {
        var date: Date? = null
        try {
            val formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            date = formatter.parse(Date)
        } catch (pe: Exception) {
            pe.printStackTrace()
        }
        return date
    }

    val hour_Minute: String
        get() {
            try {
                val localDateFormat = SimpleDateFormat("HH:mm")
                return localDateFormat.format(Date())
            } catch (e: Exception) {
                e.message
            }
            return ""
        }

    fun stringToDateMM(Date: String?): Date? {
        var date: Date? = null
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
            date = formatter.parse(Date)
        } catch (pe: ParseException) {
            pe.printStackTrace()
        }
        return date
    }

    fun weekDay(): String? {
        var weekDay: String? = null
        try {
            val dayFormat = SimpleDateFormat("EEEE", Locale.US)
            val calendar = Calendar.getInstance()
            weekDay = dayFormat.format(calendar.time)
        } catch (pe: Exception) {
            pe.printStackTrace()
        }
        return weekDay
    }

    fun padString(str: String?): String? {
        var d1: String? = null
        if (str != null && !str.isEmpty()) d1 = if (str.length == 1) "0$str" else str
        return d1
    }

    private fun getIntegerValue(value: String?): Int {
        var i = 0
        try {
            i = value!!.toInt()
        } catch (e: Exception) {
        }
        return i
    }

    fun isMatchAddress(sender: String): Boolean {
        val check: Boolean
        val MOBILE_REGEX = "[A-Z]{2}[-]" + "ISSPLN"
        check = sender.matches(MOBILE_REGEX.toRegex())
        return check
    }

    fun getStringValue(value: String?): String? {
        var str = value
        try {
            if (str!!.isEmpty() || str.toLowerCase() == "null") str = null
        } catch (e: Exception) {
        }
        return str
    }

    fun getDoubleValue(value: String): Double {
        var value = value
        var i = 0.0
        try {
            value = value.trim { it <= ' ' }
            i = value.toDouble()
        } catch (e: Exception) {
        }
        return i
    }

    fun isNullOrEmpty(var0: String?): Boolean {
        return var0 == null || var0.trim { it <= ' ' }.isEmpty() || var0.trim { it <= ' ' }
            .equals("null", ignoreCase = true)
    }

    fun getTimeHour(hour: Int): Long {
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = hour
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.timeInMillis
    }

    fun formatTime(millis: Long): String {
        val secs = millis / 1000
        return String.format("%02d:%02d", secs % 3600 / 60, secs % 60)
    }




    fun setAsteriskSign(context: Context, value: String?): Spannable {
        val colored = "*"
        val builder = SpannableStringBuilder()
        builder.append(value)
        val start = builder.length
        builder.append(colored)
        val end = builder.length
        builder.setSpan(
            ForegroundColorSpan(context.resources.getColor(R.color.color_747474)), start, end,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return builder
    }

    fun snackBar(view: View?, msg: String?, duration: Int?, color: Int) {
        val sb = Snackbar.make(view!!, msg!!, duration!!)
        val sbView = sb.view
        sbView.setBackgroundColor(color)
        sb.show()
    }

    fun makeServiceCall(requestURL: String?, data: String, token: String): String? {
        var response: String? = null
        var connection: HttpURLConnection? = null
        try {
            val postDataBytes = data.toByteArray(StandardCharsets.UTF_8)
            val url = URL(requestURL)
            connection = url.openConnection() as HttpURLConnection
            connection!!.setRequestProperty("Authorization", "Bearer $token")
            connection.requestMethod = "POST"
            connection.connectTimeout = 200 * 1000
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            connection.setRequestProperty("content-length", postDataBytes.size.toString())
            connection.doOutput = true
            connection.useCaches = false
            connection.setRequestProperty("Accept-Encoding", "identity")
            connection.outputStream.write(postDataBytes)
            if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                val `in`: InputStream = BufferedInputStream(connection.inputStream)
                response = convertStreamToString(`in`)
            }

            /*else{

                InputStream in = new BufferedInputStream(connection.getInputStream());
                response = convertStreamToString(in);
            }*/
        } catch (e: MalformedURLException) {
            e.message
        } catch (e: ProtocolException) {
            e.message
        } catch (e: IOException) {
            e.message
        } catch (e: Exception) {
            e.message
        } finally {
            connection?.disconnect()
        }
        return response
    }

    fun getCompleteAddressString(context: Context?, LATITUDE: Double, LONGITUDE: Double): String {
        var strAdd = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1)
            if (addresses != null) {
                val returnedAddress = addresses[0]
                val strReturnedAddress = StringBuilder("")
                for (i in 0..returnedAddress.maxAddressLineIndex) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n")
                }
                strAdd = strReturnedAddress.toString().trim { it <= ' ' }
                Log.e("Location1 : ", strReturnedAddress.toString())
            } else {
                Log.e("Location2 : ", "No Address returned!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Location3 : ", "Can not get Address!")
        }
        return strAdd
    }

    fun convertStreamToString(`in`: InputStream): String {
        val reader = BufferedReader(InputStreamReader(`in`))
        val sb = StringBuilder()
        var line: String?
        try {
            while (reader.readLine().also { line = it } != null) {
                sb.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                `in`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return sb.toString()
    }//handle exception

    // res1.append(Integer.toHexString(b & 0xFF) + ":");
    val macAddress: String
        get() {
            try {
                val all: List<NetworkInterface> =
                    Collections.list(NetworkInterface.getNetworkInterfaces())
                for (nif in all) {
                    if (!nif.name.equals("wlan0", ignoreCase = true)) continue
                    val macBytes = nif.hardwareAddress ?: return ""
                    val res1 = StringBuilder()
                    for (b in macBytes) {
                        // res1.append(Integer.toHexString(b & 0xFF) + ":");
                        res1.append(String.format("%02X:", b))
                    }
                    if (res1.length > 0) {
                        res1.deleteCharAt(res1.length - 1)
                    }
                    return res1.toString()
                }
            } catch (ex: Exception) {
                //handle exception
            }
            return "NULL"
        }

    fun setPic(imagePath: String?, maxHeight: Int, maxWidth: Int) {
        try {
            var photoBm = getBitmap(imagePath)
            val bmOriginalWidth = photoBm!!.width
            val bmOriginalHeight = photoBm.height
            val originalWidthToHeightRatio = 1.0 * bmOriginalWidth / bmOriginalHeight
            val originalHeightToWidthRatio = 1.0 * bmOriginalHeight / bmOriginalWidth
            photoBm = getScaledBitmap(
                photoBm, bmOriginalWidth, bmOriginalHeight,
                originalWidthToHeightRatio, originalHeightToWidthRatio,
                maxHeight, maxWidth
            )
            val bytes = ByteArrayOutputStream()
            photoBm!!.compress(Bitmap.CompressFormat.PNG, 80, bytes)
            val fo = FileOutputStream(imagePath)
            fo.write(bytes.toByteArray())
            fo.close()
        } catch (se: Exception) {
        }
    }

    fun getScaledBitmap(
        bm: Bitmap?,
        bmOriginalWidth: Int,
        bmOriginalHeight: Int,
        originalWidthToHeightRatio: Double,
        originalHeightToWidthRatio: Double,
        maxHeight: Int,
        maxWidth: Int
    ): Bitmap? {
        var bm = bm
        if (bmOriginalWidth > maxWidth || bmOriginalHeight > maxHeight) {
            Log.v(
                TAG,
                String.format("RESIZING bitmap FROM %sx%s ", bmOriginalWidth, bmOriginalHeight)
            )
            bm = if (bmOriginalWidth > bmOriginalHeight) {
                scaleDeminsFromWidth(
                    bm,
                    maxWidth,
                    bmOriginalHeight,
                    originalHeightToWidthRatio
                )
            } else {
                scaleDeminsFromHeight(
                    bm,
                    maxHeight,
                    bmOriginalHeight,
                    originalWidthToHeightRatio
                )
            }
            Log.v(TAG, String.format("RESIZED bitmap TO %sx%s ", bm!!.width, bm.height))
        }
        return bm
    }

    private fun scaleDeminsFromHeight(
        bm: Bitmap?,
        maxHeight: Int,
        bmOriginalHeight: Int,
        originalWidthToHeightRatio: Double
    ): Bitmap? {
        var bm = bm
        val newHeight = Math.min(maxHeight, bmOriginalHeight)
        val newWidth = (newHeight * originalWidthToHeightRatio).toInt()
        bm = Bitmap.createScaledBitmap(bm!!, newWidth, newHeight, true)
        return bm
    }

    private fun scaleDeminsFromWidth(
        bm: Bitmap?,
        maxWidth: Int,
        bmOriginalWidth: Int,
        originalHeightToWidthRatio: Double
    ): Bitmap? {
        //scale the width
        var bm = bm
        val newWidth = Math.min(maxWidth, bmOriginalWidth)
        val newHeight = (newWidth * originalHeightToWidthRatio).toInt()
        bm = Bitmap.createScaledBitmap(bm!!, newWidth, newHeight, true)
        return bm
    }

    fun getBitmapAsByteArray(bitmap: Bitmap): ByteArray {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream)
        return outputStream.toByteArray()
    }

    fun getBitmapByStringImage(base64: String?): Bitmap {
        val decodedString = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }

    fun getBitmapImage(image: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(image, 0, image.size)
    }

    fun getBase64Image(image: ByteArray?): String {
        return Base64.encodeToString(image, Base64.DEFAULT)
    }

    fun getBitmapFromResource(context: Context, resId: Int): Bitmap {
        return BitmapFactory.decodeResource(context.resources, resId)
    }


    fun getBitmap(path: String?): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeFile(path, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }

    fun getRotatedImage(path: String?) {
        try {
            val exif = ExifInterface(path!!)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                else -> {}
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    fun logout(context: Context, message: String?) {
        try {
            AlertDialog.Builder(context, R.style.AlertDialogCustom_1)
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id -> logout(context) }
                .setNegativeButton("No", null)
                .show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun logout(context: Context) {
        try {
            UserDataHelper.deleteAll(context);
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
            (context as AppCompatActivity).finishAffinity()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun publishStatus(
        progressDialog: ProgressDialog,
        totalCount: Int,
        uploadedCount: Int,
        failedCount: Int,
        uploadingType: String,
        failedMessage: String
    ) {
        Handler(Looper.getMainLooper()).post {
            progressDialog.setMessage(
                """
    Uploading $uploadingType......
    
    Total Count    :$totalCount
    Upload Count :$uploadedCount
    Failed Count  :$failedCount
    Failed Message:$failedMessage
    """.trimIndent()
            )
        }
    }

    fun storeBoolean(value: Boolean): String {
        var v = "false"
        if (value) v = "true"
        return v
    }

    fun fetchBoolean(value: String?): Boolean {
        var v = false
        if (value != null && !TextUtils.isEmpty(value) && value.equals(
                "true",
                ignoreCase = true
            )
        ) v = true
        return v
    }

    fun booleanToInteger(value: Boolean): Int {
        var value1 = 0
        if (value) value1 = 1
        return value1
    }

    fun dateToString_withTime(date: Date?): String? {
        var date1: String? = null
        try {
            val formatter = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH)
            date1 = formatter.format(date)
        } catch (pe: Exception) {
            pe.printStackTrace()
        }
        return date1
    }

    fun createDate(day: Int, month: Int, year: Int): Date? {
        val date = String.format(Locale.ENGLISH, "%02d", day) + "-" + String.format(
            Locale.ENGLISH,
            "%02d",
            month
        ) + "-" + year
        return stringToDateMM(date)
    }

    fun IsCurrrent(date: String?): Boolean? {
        var isCurrent: Boolean? = null
        val date1 = stringToDate_withTime(date)
        if (date1 != null) {
            val month = getMonth(date1)
            val year = getYear(date1)
            val date2 = addDays(addMonths(createDate(1, month, year), 1), -1)
            isCurrent = if (date1 === date2) false else true
        }
        return isCurrent
    }

    fun getHour(date: Date?): Int {
        val calender = Calendar.getInstance()
        calender.time = date
        return calender[Calendar.HOUR_OF_DAY]
    }

    fun getMinute(date: Date?): Int {
        val calender = Calendar.getInstance()
        calender.time = date
        return calender[Calendar.MINUTE]
    }

    fun getSecond(date: Date?): Int {
        val calender = Calendar.getInstance()
        calender.time = date
        return calender[Calendar.SECOND]
    }

    fun stringToDate_withTime(Date: String?): Date? {
        var date: Date? = null
        try {
            val formatter = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH)
            date = formatter.parse(Date)
        } catch (pe: ParseException) {
            pe.printStackTrace()
        }
        return date
    }

    fun stringToDate_withTimeMM(Date: String?): Date? {
        var date: Date? = null
        try {
            val formatter = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
            date = formatter.parse(Date)
        } catch (pe: ParseException) {
            pe.printStackTrace()
        }
        return date
    }

    fun getDifferenceDays(d1: Date, d2: Date): Long {
        val diff = d1.time - d2.time
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)
    }

    fun makeDateTime(
        day: String?,
        month: String?,
        year: String?,
        hh: String?,
        mm: String?,
        ss: String?
    ): String? {
        var hh = hh
        var mm = mm
        var ss = ss
        var date: String? = null
        if (!TextUtils.isEmpty(day) && !TextUtils.isEmpty(month) && !TextUtils.isEmpty(year)) {
            hh = if (TextUtils.isEmpty(hh)) "00" else padString(hh)
            mm = if (TextUtils.isEmpty(mm)) "00" else padString(mm)
            ss = if (TextUtils.isEmpty(ss)) "00" else padString(ss)
            date =
                padString(day) + "-" + padString(month) + "-" + padString(year) + " " + hh + ":" + mm + ":" + ss
        }
        if (date != null) {
            try {
                val date1 = stringToDate_withTimeMM(date)
                val formatter = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH)
                date = formatter.format(date1)
            } catch (pe: Exception) {
                pe.printStackTrace()
            }
        }
        return date
    }

    fun getBooleanValue(value: String?): Boolean {
        var i = false
        try {
            i = java.lang.Boolean.parseBoolean(value)
        } catch (e: Exception) {
        }
        return i
    }

    fun correctYearTimeFormat(str: String?): String? {
        // date = 181:30:00:PM
        var str1 = str
        if (str != null && !str.isEmpty()) {
            if (str.contains(":")) {
                val a = str.split(":".toRegex()).toTypedArray()
                var year = a[0]
                var hour = ""
                if (a[0].length > 2) {
                    if (a[0].length > 4) {
                        year = a[0].substring(0, 4)
                        hour = a[0].substring(4, a[0].length)
                    } else {
                        year = a[0].substring(0, 2)
                        year = "20$year"
                        hour = a[0].substring(2, a[0].length)
                    }
                    if (a[a.size - 1] == "AM" || a[a.size - 1] == "PM") {
                        if (a[a.size - 1] == "PM") hour = (getIntegerValue(hour) + 12).toString()
                        a[a.size - 1] = ""
                    }
                }
                str1 = year + " " + padString(hour)
                for (i in 1 until a.size) {
                    if (a[i].length > 0) str1 = str1 + ":" + a[i]
                }
            } else if (str.length == 2) str1 = "20$str 00:00:00"
        }
        return str1
    }

    fun correctDateFormat(date: String?): String? {
        var date1 = date
        if (date != null && !date.isEmpty()) {
            if (date.contains("/")) {
                val a = date.split("/".toRegex()).toTypedArray()
                if (a.size == 3) date1 = padString(a[0]) + "-" + padString(
                    a[1]
                ) + "-" + correctYearTimeFormat(a[2])
            } else if (date.contains("-")) {
                val a = date.split("-".toRegex()).toTypedArray()
                if (a.size == 3) date1 = padString(a[0]) + "-" + padString(
                    a[1]
                ) + "-" + correctYearTimeFormat(a[2])
            }
        }
        return date1
    }

    /*public static void getMasterData(final Context context) {
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Work List downloading...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        Retrofit retrofit = MyApplication.instance.getRetrofitinstance();
        Api gerritAPI = retrofit.create(Api.class);
        try {

            Login login = Login_Helper.getLogin(MyApplication.instance);
            UserAudit userAudit = Utility.getUserAudit(login.getLOGIN_ID(), login.getTOKEN(), login.getAPP_EXPIRY_DATE(), Utility.getStringPreference("latitude"), Utility.getStringPreference("longitude"));
            Call<MasterDataModels> call = gerritAPI.getMasterData("Bearer " + login.getTOKEN(), userAudit);
            call.enqueue(new Callback<MasterDataModels>() {
                @Override
                public void onResponse(Call<MasterDataModels> call, Response<MasterDataModels> response) {
                    responseListener.responseCallBack(response,progressDialog);
                }
                @Override
                public void onFailure(Call<MasterDataModels> call, Throwable t) {
                    progressDialog.cancel();
                }
            });
        } catch (Exception e) {
            progressDialog.cancel();
        }
    }*/
    fun deleteImages(context: Context): Boolean {
        try {
            val dir = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Android/media/com.inventive.ugobill/" + context.resources.getString(
                    R.string.app_name
                )
            )
            if (dir.isDirectory) {
                for (file in dir.listFiles()) {
                    file.delete()
                }
                return true
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return false
    }


    fun stringToDate_WithSlash(Date: String?): Date? {
        var date: Date? = null
        try {
            val formatter = SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH)
            date = formatter.parse(Date)
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
            if (date != null) date = dateFormat.parse(dateFormat.format(date))
        } catch (pe: ParseException) {
            pe.printStackTrace()
        }
        return date
    }

    fun getTimeZone(deviation: Int, dst: Boolean): TimeZone? {
        // Return current time zone if time zone is not used.
        if (deviation == 0x8000 || deviation == -32768) {
            return Calendar.getInstance().timeZone
        }
        var tz = Calendar.getInstance().timeZone
        if (dst) {
            // If meter is in same time zone than meter reading application.

            //   if (tz.observesDaylightTime() && tz.getRawOffset() / 60000 == deviation - 60) {
            if ((tz.useDaylightTime() || tz.inDaylightTime(Date())) && tz.rawOffset / 60000 == deviation - 60) {
                return tz
            }
            val ids = TimeZone.getAvailableIDs((deviation - 60) * 60000)
            tz = null
            for (pos in ids.indices) {
                tz = TimeZone.getTimeZone(ids[pos])
                //  if (tz.observesDaylightTime() && tz.getRawOffset() / 60000 == deviation - 60) {
                if ((tz.useDaylightTime() || tz.inDaylightTime(Date())) && tz.rawOffset / 60000 == deviation - 60) {
                    break
                }
                tz = null
            }
            if (tz != null) {
                return tz
            }
        }
        //   if (tz != null && !tz.observesDaylightTime() && tz.getRawOffset() / 60000 == deviation) {
        if (tz != null && !(tz.useDaylightTime() || tz.inDaylightTime(Date())) && tz.rawOffset / 60000 == deviation) {
            return tz
        }
        val str: String
        val df = DecimalFormat("00")
        val tmp = df.format((deviation / 60).toLong()) + ":" + df.format((deviation % 60).toLong())
        str = if (deviation == 0) {
            "GMT"
        } else if (deviation > 0) {
            "GMT+$tmp"
        } else {
            "GMT$tmp"
        }
        return TimeZone.getTimeZone(str)
    }

    fun hexToAscii(hxStr: String): String {
        val output = StringBuilder("")
        var i = 0
        while (i < hxStr.length) {
            val str = hxStr.substring(i, i + 2)
            output.append(str.toInt(16).toChar())
            i += 2
        }
        return output.toString()
    }

    fun Multiply_By_K_Parameter(value: String?): String? {
        var value = value
        try {
            if (value != null) {
                value = BigDecimal.valueOf(value.toDouble() * Math.pow(10.0, -3.0)).toString()
            }
        } catch (ex: Exception) {
            ex.message
        }
        return value
    }

    fun MilliToDate(miliSec: Long): String {
        var date = ""
        try {
            val simple: DateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH)
            val result = Date(miliSec)
            date = simple.format(result).toString()
        } catch (e: Exception) {
            e.message
        }
        return date
    }

    fun getResponseDate(oldDate: String?): String {
        var formattedDate = ""
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val outputFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss")
            val new_date = inputFormat.parse(oldDate)
            formattedDate = outputFormat.format(new_date)
        } catch (e: Exception) {
            e.message
        }
        return formattedDate
    }

    fun get_Milli_To_Time(millis: Long): String {
        var time = ""
        try {
            time = String.format(
                Locale.ENGLISH, "%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(
                    TimeUnit.MILLISECONDS.toHours(
                        millis
                    )
                ),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(
                    TimeUnit.MILLISECONDS.toMinutes(
                        millis
                    )
                )
            )
        } catch (e: Exception) {
            e.message
        }
        return time
    }

    fun isGPSEnabled(context: Context): Boolean {
        var gpsEnabled = false
        var networkEnabled = false
        val lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            e.message
        }
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            e.message
        }
        return gpsEnabled || networkEnabled
    }

    fun AuthPassword(): ByteArray {
        return PacketFormation(0x16.toByte(), 0x14.toByte(), "Indi@123".toByteArray())
    }

    /**
     * don't delete below methods from changePassword to packet formation either they are used or not..
     *
     * @return
     */
    //---------------------------------------------------------------------------------------------------
    fun ChangePassword(): ByteArray {
        return PacketFormation(0x16.toByte(), 0x10.toByte(), "Indi@123".toByteArray())
    }

    fun ChangeAccessPointPassword(): ByteArray {
        //ssid_name_password=Inventive_Device
        val Packet = byteArrayOf(
            0x49,
            0x6e,
            0x76,
            0x65,
            0x6e,
            0x74,
            0x69,
            0x76,
            0x65,
            0x5f,
            0x44,
            0x65,
            0x76,
            0x69,
            0x63,
            0x65
        )
        return PacketFormation(0x16.toByte(), 0x11.toByte(), Packet)
    }

    fun ConfigureCommunicationParameters(baudrate: Int, databits: Int, parity: Int): ByteArray {
        val BaudRate = getBaudrate(baudrate)
        val DataBits = getDataBits(databits)
        val StopBits = byteArrayOf(0x31)
        val Parity = getParity(parity)
        val Packet = byteArrayOf(
            BaudRate[0],
            BaudRate[1],
            BaudRate[2],
            BaudRate[3],
            DataBits[0],
            StopBits[0],
            Parity[0]
        )
        return PacketFormation(0x0D.toByte(), 0x12.toByte(), Packet)
    }

    fun getBaudrate(baudrate: Int): ByteArray {
        return when (baudrate) {
            300 -> byteArrayOf(0x2C.toByte(), 0x01, 0x00, 0x00)
            4800 -> byteArrayOf(0xC0.toByte(), 0x12, 0x00, 0x00)
            else -> byteArrayOf(0x80.toByte(), 0x25, 0x00, 0x00)
        }
    }

    fun getDataBits(databits: Int): ByteArray {
        return when (databits) {
            7 -> byteArrayOf(0x37)
            else -> byteArrayOf(0x38)
        }
    }

    fun getParity(parity: Int): ByteArray {
        return when (parity) {
            1 -> byteArrayOf(0x31)
            2 -> byteArrayOf(0x32)
            else -> byteArrayOf(0x30)
        }
    }

    fun EnableCommandMode(): ByteArray {
        return PacketFormation(0x07.toByte(), 0x14.toByte(), byteArrayOf(0x01))
    }

    fun BatteryVoltageData(): ByteArray {
        return PacketFormation(0x06.toByte(), 0x16.toByte(), byteArrayOf())
    }

    fun DisConnect(): ByteArray {
        return PacketFormation(0x06.toByte(), 0x17.toByte(), byteArrayOf())
    }

    fun PacketFormation(packet_length: Byte, packet_id: Byte, dataPacket: ByteArray): ByteArray {
        val header_bytes = byteArrayOf(0x55, 0xAA.toByte())
        val Packet = ByteArray(packet_length - 2)
        Packet[0] = header_bytes[0]
        Packet[1] = header_bytes[1]
        Packet[2] = packet_length
        Packet[3] = packet_id
        for (i in 4 until dataPacket.size + 4) {
            Packet[i] = dataPacket[i - 4]
        }
        val crcByte = Calculate_Crc(Packet, Packet.size)
        val PacketWithCRC = ByteArray(Packet.size + 2)
        System.arraycopy(Packet, 0, PacketWithCRC, 0, Packet.size)
        PacketWithCRC[Packet.size] = crcByte[0]
        PacketWithCRC[Packet.size + 1] = crcByte[1]
        return PacketWithCRC
    }

    //--------------------------------------------------------------------------------------------------------
    @get:SuppressLint("MissingPermission", "HardwareIds")
    val iMEINumber: String?
        get() {
            var myuniqueID: String? = null
            try {
                val myversion = Integer.valueOf(Build.VERSION.SDK_INT)
                if (myversion < 23) {
                    val manager =
                        MyApplication.mInstance!!.applicationContext.getSystemService(
                            Context.WIFI_SERVICE
                        ) as WifiManager
                    val info = manager.connectionInfo
                    myuniqueID = info.macAddress
                    if (myuniqueID == null) {
                        val mngr = MyApplication.mInstance!!
                            .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                        if (ActivityCompat.checkSelfPermission(
                                MyApplication.mInstance!!,
                                Manifest.permission.READ_PHONE_STATE
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            return ""
                        }
                        myuniqueID = mngr.deviceId
                    }
                } else if (myversion > 23 && myversion < 29) {
                    val mngr = MyApplication.mInstance!!
                        .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    if (ActivityCompat.checkSelfPermission(
                            MyApplication.mInstance!!,
                            Manifest.permission.READ_PHONE_STATE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        return ""
                    }
                    myuniqueID = mngr.deviceId
                } else {
                    val androidId = Settings.Secure.getString(
                        MyApplication.mInstance!!.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )
                    myuniqueID = androidId
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return myuniqueID
        }

    fun Calculate_Crc(bytes_to_calculate_crc: ByteArray, length: Int): ByteArray {
        var i = 0
        var crc_lower_byte = 0xFF.toByte()
        var crc_upper_byte = 0xFF.toByte()
        i = 0
        while (i < length) {
            if (i % 2 == 0) crc_lower_byte =
                crc_lower_byte xor bytes_to_calculate_crc[i] else crc_upper_byte =
                crc_upper_byte xor bytes_to_calculate_crc[i]
            i++
        }
        return byteArrayOf(crc_lower_byte, crc_upper_byte)
    }

    fun OwnSleep(milli: Long) {
        try {
            Thread.sleep(milli)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun ByteArrayToHexString(byteArray: ByteArray): String {
        var hexString = ""
        for (i in byteArray.indices) {
            var thisByte = String.format("%x", byteArray[i])
            if (thisByte.length == 1) {
                thisByte = "0$thisByte"
            }
            hexString += thisByte
        }
        return hexString
    }

    fun EnableTransparenetMode(): ByteArray {
        return PacketFormation(0x07.toByte(), 0x13.toByte(), byteArrayOf(0x01))
    }

    fun HexToFloat(hexValue: String): Float {
        var floatValue: Float? = null
        try {
            val i = hexValue.toLong(16)
            floatValue = java.lang.Float.intBitsToFloat(i.toInt())
        } catch (e: Exception) {
            e.message.toString()
        }
        return floatValue!!
    }

    fun find_Largest(arr: Array<String>): Double {
        var i: Int
        var max = arr[0].toDouble()
        i = 1
        while (i < arr.size) {
            if (arr[i].toDouble() > max) max = arr[i].toDouble()
            i++
        }
        return max
    }

    val currentMonthName: String?
        get() {
            var monthName: String? = null
            val cal = Calendar.getInstance()
            val month_date = SimpleDateFormat("MMMM")
            val month_name = month_date.format(cal.time)
            monthName = month_name
            return monthName
        }

    fun getRotation(photoPath: String?): Int {
        var ei: ExifInterface? = null
        var rotation = 0
        try {
            ei = ExifInterface(photoPath!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var orientation = 0
        if (ei != null) {
            orientation = ei.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
        }
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270
            ExifInterface.ORIENTATION_TRANSVERSE -> rotation = -90
        }
        return rotation
    }

    fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height,
            matrix, true
        )
    }

    fun hex2decimal(s: String): Int {
        var s = s
        val digits = "0123456789ABCDEF"
        s = s.toUpperCase()
        var `val` = 0
        for (i in 0 until s.length) {
            val c = s[i]
            val d = digits.indexOf(c)
            `val` = 16 * `val` + d
        }
        return `val`
    }

    fun toTitleCase(str: String?): String? {
        if (isNullOrEmpty(str)) {
            return null
        }
        var space = true
        val builder = StringBuilder(str)
        val len = builder.length
        for (i in 0 until len) {
            val c = builder[i]
            if (space) {
                if (!Character.isWhitespace(c)) {
                    // Convert to title case and switch out of whitespace mode.
                    builder.setCharAt(i, Character.toTitleCase(c))
                    space = false
                }
            } else if (Character.isWhitespace(c)) {
                space = true
            } else {
                builder.setCharAt(i, Character.toLowerCase(c))
            }
        }
        return builder.toString()
    }

    /*  public static TreeMap<Integer, String> prepareString(Context context, UserBillModel userBillModel) {

          HashMap<Integer, String> printString = new HashMap<>();

          List<Configuration_Bill_Print> configuration_bill_prints_List = Configuration_Bill_Print_Helper.getAll(context);
          for (Configuration_Bill_Print configuration_bill_print : configuration_bill_prints_List) {
              try {
                  Field field = userBillModel.getClass().getDeclaredField(configuration_bill_print.getMAPPING_USER_BILL_COLUMN());
                  if (!is_Print_Skip(String.valueOf(field.get(userBillModel)), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                      if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                          printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + checkNull(String.valueOf(field.get(userBillModel))) + "\n");
                      else
                          printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + checkNull(String.valueOf(field.get(userBillModel))) + "\n");
                  }
              } catch (Exception e) {
                  e.getMessage();

              }
          }

          Configuration_Bill_Print configuration_bill_print = null;

          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.BILL_DATE);
          if (configuration_bill_print != null) {
              if (!is_Print_Skip(userBillModel.getBILL_DATE(), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + userBillModel.getBILL_DATE() + " " + getHour_Minute() + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + userBillModel.getBILL_DATE() + " " + getHour_Minute() + "\n");
              }
          }

          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.CONSUMER_NAME);
          if (configuration_bill_print != null) {
              String name = "";
              if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                  name = checkNull(userBillModel.getCONSUMER_NAME_REGIONAL());
              else
                  name = checkNull(userBillModel.getNAME());
              if (name != null && name.length() > 30) {
                  name = name.substring(0, 29);
              }

              if (!is_Print_Skip(name, configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + name + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + name + "\n");
              }
          }

          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.CONSUMER_ADDRESS);
          if (configuration_bill_print != null) {
              String address = "";
              if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                  address = checkNull(userBillModel.getCONSUMER_ADDRESS_REGIONAL());
              else
                  address = checkNull(userBillModel.getADDRESS());
              if (address != null && address.length() > 30) {
                  address = address.substring(0, 29);
              }

              if (!is_Print_Skip(address, configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + address + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + address + "\n");
              }
          }


          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.CONTRACT_LOAD);
          if (configuration_bill_print != null) {
              if (!is_Print_Skip(String.valueOf(userBillModel.getCONTRACT_LOAD()), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + userBillModel.getCONTRACT_LOAD() + " " + userBillModel.getCONTRACT_LOAD_UNIT_NAME() + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + userBillModel.getCONTRACT_LOAD() + " " + userBillModel.getCONTRACT_LOAD_UNIT_NAME() + "\n");
              }
          }

          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.COURT_CASE_AMOUNT);
          if (configuration_bill_print != null) {
              double court_Case_Amt = (userBillModel.getDEFFERED_UTILITY_CHARGE() + userBillModel.getDEFFERED_TAX() + userBillModel.getDEFFERED_SURCHARGE());
              if (!is_Print_Skip(String.valueOf(court_Case_Amt), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + court_Case_Amt + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + court_Case_Amt + "\n");
              }
          }

          String lstDate = "";
          double lstamt = 0.0;
          if (userBillModel.getPAYMENT3_DATE() != null) {
              lstDate = userBillModel.getPAYMENT3_DATE();
              lstamt = userBillModel.getPAYMENT3();
          } else if (userBillModel.getPAYMENT2_DATE() != null) {
              lstDate = userBillModel.getPAYMENT2_DATE();
              lstamt = userBillModel.getPAYMENT2();
          } else if (userBillModel.getPAYMENT1_DATE() != null) {
              lstDate = userBillModel.getPAYMENT1_DATE();
              lstamt = userBillModel.getPAYMENT1();
          }


          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.PAYMENT_UPTO);
          if (configuration_bill_print != null) {
              if (!is_Print_Skip(lstDate, configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + lstDate + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + lstDate + "\n");
              }
          }
          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.PAYMENT_AMOUNT);
          if (configuration_bill_print != null) {
              if (!is_Print_Skip(String.valueOf(lstamt), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + lstamt + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + lstamt + "\n");
              }
          }


          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.TARIFF_CHANGE_CHARGED);
          if (configuration_bill_print != null) {
              double sum = (userBillModel.getTARIFF_CHANGE_UTILITY_CHARGE() + userBillModel.getTARIFF_CHANGE_TAX() + userBillModel.getTARIFF_CHANGE_SURCHARGE());
              if (!is_Print_Skip(String.valueOf(sum), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + sum + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + sum + "\n");
              }
          }

          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.PREV_OUTSTANDING);
          if (configuration_bill_print != null) {
              double sum = Round2(userBillModel.getOUTSTANDING_SURCHARGE() + userBillModel.getOUTSTANDING_TAX() + userBillModel.getOUTSTANDING_UTILITY_CHARGE());
              if (!is_Print_Skip(String.valueOf(sum), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + sum + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + sum + "\n");
              }

          }

          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.BILL_REVISION_AMOUNT);
          if (configuration_bill_print != null) {
              double sum = (userBillModel.getBR_UTILITY_CHARGE() + userBillModel.getBR_TAX() + userBillModel.getBR_SURCHARGE());
              if (!is_Print_Skip(String.valueOf(sum), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + sum + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + sum + "\n");
              }

          }

          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.OUTSTANDING_AFTER_DUE_DATE);
          if (configuration_bill_print != null) {
              double sum = Math.ceil(userBillModel.getLPS_AFTER_DUE_DATE() + userBillModel.getOUTSTANDING_TOTAL());
              if (!Utility.isNullOrEmpty(userBillModel.DEFAULT_ROUNDOFF) && userBillModel.DEFAULT_ROUNDOFF.equalsIgnoreCase("EXACT"))
                  sum = Math.ceil(sum);
              else
                  sum = Round2(sum);
              if (!is_Print_Skip(String.valueOf(sum), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + sum + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + sum + "\n");
              }

          }

          configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.LOGIN_ID);
          if (configuration_bill_print != null) {
              if (!is_Print_Skip(Login_Helper.getLogin(context).getLOGIN_ID(), configuration_bill_print.isIS_PRINTABLE_ON_SPOT_SKIP(), configuration_bill_print.isACCOUNT_SUB_HEAD_FLAG())) {
                  if (userBillModel.isBILL_PRINT_REGIONAL_FLAG())
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_REGIONAL() + " : " + Login_Helper.getLogin(context).getLOGIN_ID() + "\n");
                  else
                      printString.put(configuration_bill_print.getSEQUENCE(), configuration_bill_print.getLABEL_ENGLISH() + " : " + Login_Helper.getLogin(context).getLOGIN_ID() + "\n");
              }
          }


          if (userBillModel.getPF_CHARGES() == 0) {
              configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.PF);
              if (configuration_bill_print != null)
                  printString.remove(configuration_bill_print.getSEQUENCE());
          }
          if (!userBillModel.getMETERED_FLAG()) {
              configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.METER_MF);
              if (configuration_bill_print != null)
                  printString.remove(configuration_bill_print.getSEQUENCE());
          }

          if (!userBillModel.getMETERED_FLAG()) {
              configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.CONSUMPTION_CHARGED);
              if (configuration_bill_print != null)
                  printString.remove(configuration_bill_print.getSEQUENCE());
          }

          if (!userBillModel.getMETERED_FLAG()) {
              configuration_bill_print = Configuration_Bill_Print_Helper.getByUserBillMappingColumnName(context, Bill_Print.DEMAND_CHARGED);
              if (configuration_bill_print != null)
                  printString.remove(configuration_bill_print.getSEQUENCE());
          }

          TreeMap<Integer, String> treeMap = new TreeMap<Integer, String>(printString);

          return treeMap;
      }*/
    private fun is_Print_Skip(
        `val`: String,
        skip_Flag: Boolean,
        account_SubHead_Flag: Boolean
    ): Boolean {
        return if (skip_Flag) {
            if (getDoubleValue(`val`) == 0.0 && skip_Flag) true else false
        } else skip_Flag
    }

    private fun checkNull(value: String): String {
        return if (isNullOrEmpty(value)) "" else value
    }

    private fun Round2(v: Double): Double {
        return Math.round(v * 100.0) / 100.0
    }

    fun saveJsonFile(fileName: String, content: String) {
        val outputStream: FileOutputStream
        val myDir: File = MyApplication.mInstance!!.applicationContext.getFilesDir()
        val documentsFolder = File(myDir, "ugobill")
        if (!documentsFolder.exists()) {
            documentsFolder.mkdirs()
        }
        try {
            outputStream = FileOutputStream(File(documentsFolder.path + "/" + fileName + ".json"))
            outputStream.write(content.toByteArray())
            outputStream.flush()
            outputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun openWhatsappContact(number: String, text: String?) {
        if (number.length > 7) {
            val sharingIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(
                    String.format(
                        "https://api.whatsapp.com/send?phone=%s&text=%s",
                        number,
                        text
                    )
                )
            )
            sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            MyApplication.mInstance!!.applicationContext.startActivity(sharingIntent)
        }
    }

    fun clearSharedPreferenceData() {
        saveStringPreference("AccountNo", "")
        saveStringPreference("Consumername", "")
        saveStringPreference("latitude", "")
        saveStringPreference("longitude", "")
        saveStringPreference("survey_latitude", "")
        saveStringPreference("survey_longitude", "")
        saveStringPreference("feeder_latitude", "")
        saveStringPreference("feeder_longitude", "")
        saveStringPreference("payment_latitude", "")
        saveStringPreference("payment_longitude", "")
    }

    fun IsRepeatedString(str: String): Boolean {
        var isRepeated = false
        try {
            val len = str.length
            val c = str[0]
            for (i in 0 until len) {
                var count = 0
                for (j in i + 1 until len) {
                    if (str[i] != str[j]) break
                    count++
                }
                if (count >= 5) {
                    isRepeated = true
                    break
                }
            }
        } catch (e: Exception) {
            e.message
        }
        return isRepeated
    }

    fun isAllCharactersSame(s: String): Boolean {
        val n = s.length
        for (i in 1 until n) {
            if (s[i] != s[0]) return false
        }
        return true
    }

    fun isMeterNumberValid(meterNo: String): Boolean {
        var isValid = false
        try {
            val regex = "^[a-zA-Z0-9][a-zA-Z0-9\\-\\ ]+$"
            isValid = meterNo.matches(regex.toRegex())
        } catch (e: Exception) {
            e.message
        }
        return isValid
    }


    fun getMonthNameInSmall(monthNum: Int): String { // month name starts from 1 to 12
        var monthname = "Jan"
        when (monthNum) {
            0 -> monthname = "Jan"
            1 -> monthname = "Feb"
            2 -> monthname = "Mar"
            3 -> monthname = "Apr"
            4 -> monthname = "May"
            5 -> monthname = "Jun"
            6 -> monthname = "Jul"
            7 -> monthname = "Aug"
            8 -> monthname = "Sep"
            9 -> monthname = "Oct"
            10 -> monthname = "Nov"
            11 -> monthname = "Dec"
        }
        return monthname
    }


    fun ConvertDoubleToLong(`val`: Double): String {
        try {
            val formatter: NumberFormat = DecimalFormat("#0.00")
            return formatter.format(`val`)
        } catch (e: Exception) {
        }
        return `val`.toString()
    }


}