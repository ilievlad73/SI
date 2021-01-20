package com.example.si.`object`

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.si.model.Admin
import com.example.si.model.University
import com.example.si.model.User
import java.util.*
import kotlin.collections.ArrayList

object SavedPreferences {

    const val EMAIL_KEY = "email"
    const val FIRST_NAME_KEY = "first_name"
    const val LAST_NAME_KEY = "profile_picture_url"
    const val UID_KEY = "uid"
    const val CNP_KEY = "cnp"
    const val ADDRESS_KEY = "address"
    const val FILES_KEY = "files"
    const val ROLE_KEY = "role"
    const val ADMIN_UNIVERSITY_UID = "admin_university_uid"
    const val ADMIN_UNIVERSITY_NAME = "admin_university_name"

    private fun getSharedPreferences(ctx: Context?): SharedPreferences? =
        PreferenceManager.getDefaultSharedPreferences(ctx)

    private fun editor(context: Context, key: String, value: String) =
        getSharedPreferences(context)?.edit()?.putString(key, value)?.apply()

    fun getEmail(context: Context) = getSharedPreferences(context)!!.getString(EMAIL_KEY, "")!!
    fun setEmail(context: Context, email: String) = editor(context, EMAIL_KEY, email)

    fun getRole(context: Context) = getSharedPreferences(context)!!.getString(ROLE_KEY, "")!!
    fun setRole(context: Context, role: String) = editor(context, ROLE_KEY, role)

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

        return ArrayList(files.split(","))
    }

    fun setFiles(context: Context, files: List<String>) {
        if (files.isNotEmpty()) {
            val sb = StringBuilder()
            val size = files.size
            for (i in 0 until size - 1) {
                sb.append(files.get(i)).append(",")
            }
            sb.append(files.get(size - 1))
            editor(context, FILES_KEY, sb.toString())
        } else {
            editor(context, FILES_KEY, "")
        }
    }

    fun getUniversityUId(context: Context) =
        getSharedPreferences(context)!!.getString(ADMIN_UNIVERSITY_UID, "")!!

    fun setUniversityUId(context: Context, universityUId: String) =
        editor(context, ADMIN_UNIVERSITY_UID, universityUId)

    fun getUniversityName(context: Context) =
        getSharedPreferences(context)!!.getString(ADMIN_UNIVERSITY_NAME, "")!!

    fun setUniversityName(context: Context, universityName: String) =
        editor(context, ADMIN_UNIVERSITY_NAME, universityName)

    fun getUniversity(context: Context): University {
        return University(getUniversityUId(context), getUniversityName(context))
    }

    fun reset(context: Context) {
        setEmail(context, "")
        setRole(context, "")
        setFirstName(context, "")
        setLastName(context, "")
        setUID(context, "")
        setCNP(context, "")
        setAddress(context, "")
        setFiles(context, emptyList())
        setUniversityName(context, "")
        setUniversityUId(context, "")
    }

    fun set(context: Context, user: User) {
        setUID(context, user!!.uid)
        setRole(context, user.role)
        setEmail(context, user.email)
        user!!.lastName?.let { setLastName(context, it) }
        user!!.firstName?.let { setFirstName(context, it) }
        user!!.address?.let { setAddress(context, it) }
        user!!.cnp?.let { setCNP(context, it) }
        user!!.files?.let { setFiles(context, it) }
    }

    fun setAdmin(context: Context, admin: Admin) {
        setUID(context, admin!!.uid)
        setRole(context, admin.role)
        setEmail(context, admin.email)
        admin!!.lastName?.let { setLastName(context, it) }
        admin!!.firstName?.let { setFirstName(context, it) }
        admin!!.address?.let { setAddress(context, it) }
        admin!!.cnp?.let { setCNP(context, it) }
        admin!!.files?.let { setFiles(context, it) }
        setUniversityUId(context, admin.university!!.uid)
        setUniversityName(context, admin.university!!.name)
    }

    fun get(context: Context): User {
        val email = getEmail(context)
        val role = getRole(context)
        val uid = getUId(context)
        val lastName = getLastName(context)
        val firstName = getFirstName(context)
        val address = getAddress(context)
        val files = getFiles(context)
        val cnp = getCNP(context)

        return User(uid, email, role, firstName, lastName, cnp, address, files)
    }

    fun getAdmin(context: Context): Admin {
        val email = getEmail(context)
        val role = getRole(context)
        val uid = getUId(context)
        val lastName = getLastName(context)
        val firstName = getFirstName(context)
        val address = getAddress(context)
        val files = getFiles(context)
        val cnp = getCNP(context)
        val universityUId = getUniversityUId(context)
        var universityName = getUniversityName(context)

        return Admin(
            uid,
            email,
            role,
            firstName,
            lastName,
            cnp,
            address,
            files,
            University(universityUId, universityName)
        )
    }

    fun isAccountConfigurationNeeded(context: Context): Boolean {
        if (getEmail(context).isEmpty()) {
            return true
        }

        if (getFirstName(context).isEmpty()) {
            return true
        }

        if (getLastName(context).isEmpty()) {
            return true
        }

        if (getAddress(context).isEmpty()) {
            return true
        }


        if (getCNP(context).isEmpty()) {
            return true
        }

        if (getFiles(context).isEmpty()) {
            return true
        }

        return false
    }

    fun isAdmin(role: String): Boolean {
        if (role.compareTo(Configs.ADMIN_ROLE) === 0)
            return true

        return false
    }

    fun isAdmin(context: Context): Boolean {
        var role = this.getRole(context)
        if (role.compareTo(Configs.ADMIN_ROLE) === 0)
            return true

        return false
    }

}