package com.ft.printerconnectandprint

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
fun String.toast(context: Context){
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

fun String.logD(tag: String){
    Log.d(tag, this)
}
fun String.logE(tag: String){
    Log.e(tag, this)
}
fun String.logI(tag: String){
    Log.i(tag, this)
}

fun Context.prefGetFlot(key: String, value: Float): Float{
    return 48f
}

fun Fragment.changeFragment(containerId: Int, fragment : Fragment, addToBackStack : Boolean = false) {
    val fm = requireActivity().supportFragmentManager.beginTransaction().replace(containerId, fragment)
    if (addToBackStack) fm.addToBackStack(null)
    fm.commit()
}

fun AppCompatActivity.changeFragment(containerId: Int, fragment : Fragment, addToBackStack : Boolean = false) {
    val fm = supportFragmentManager.beginTransaction().replace(containerId, fragment)
    if (addToBackStack) fm.addToBackStack(null)
    fm.commit()
}
