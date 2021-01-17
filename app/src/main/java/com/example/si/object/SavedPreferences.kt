package com.example.si.`object`

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

object SavedPreferences {

    const val EMAIL_KEY = "email"
    const val FIRST_NAME_KEY = "first_name"
    const val LAST_NAME_KEY = "profile_picture_url"
    const val UID_KEY = "uid"
    const val CNP_KEY = "cnp"
    const val ADDRESS_KEY = "address"
    const val FILES_KEY = "key"

    private fun getSharedPreferences(ctx: Context?): SharedPreferences? =
        PreferenceManager.getDefaultSharedPreferences(ctx)

    private fun editor(context: Context, key: String, value: String) =
        getSharedPreferences(context)?.edit()?.putString(key, value)?.apply()

    fun getEmail(context: Context) = getSharedPreferences(context)!!.getString(EMAIL_KEY, "")!!
    fun setEmail(context: Context, email: String) = editor(context, EMAIL_KEY, email)

    fun getFirstName(context: Context) = getSharedPreferences(context)!!.getString(
        FIRST_NAME_KEY,
        ""
    )!!

    fun setFirstName(context: Context, firstName: String) = editor(
        context,
        FIRST_NAME_KEY,
        firstName
    )

    fun getLastName(context: Context) =
        getSharedPreferences(context)!!.getString(LAST_NAME_KEY, "")!!

    fun setLastName(context: Context, lastName: String) = editor(context, LAST_NAME_KEY, lastName)

    fun getUId(context: Context) = getSharedPreferences(context)!!.getString(UID_KEY, "")!!
    fun setUID(context: Context, id: String) = editor(context, UID_KEY, id)

    fun getCNP(context: Context) = getSharedPreferences(context)!!.getString(CNP_KEY, "")!!
    fun setCNP(context: Context, cnp: String) = editor(context, CNP_KEY, cnp)

    fun getAddress(context: Context) = getSharedPreferences(context)!!.getString(ADDRESS_KEY, "")!!
    fun setAddress(context: Context, address: String) = editor(context, ADDRESS_KEY, address)

    fun getFiles(context: Context): ArrayList<String> {
        val files = getSharedPreferences(context)!!.getString(FILES_KEY, "")!!
        return files.split(",") as ArrayList<String>
    }

    fun setFiles(context: Context, files: ArrayList<String>) {
        val sb = StringBuilder()
        for (i in 0 until files.size) {
            sb.append(files.get(i)).append(",")
        }
        editor(context, FILES_KEY, sb.toString())
    }

    fun reset(context: Context) {
        setEmail(context, "")
        setFirstName(context, "")
        setLastName(context, "")
        setUID(context, "")
        setCNP(context, "")
        setAddress(context, "")
        setFiles(context, ArrayList<String>(0))
    }
}