package com.ft.printerconnectandprint

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

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

fun Activity.checkPermission(context: Context, list: List<String>){
    Dexter.withContext(context)
        .withPermissions(list)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if(report.areAllPermissionsGranted()){
                        "Permission Granted".toast(context)
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        })
        .withErrorListener {
            it.name.toast(context)
        }
        .check()
}

fun Fragment.checkPermission(context: Context, list: List<String>){
    Dexter.withContext(context)
        .withPermissions(list)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                report?.let {
                    if(report.areAllPermissionsGranted()){
                        "Permission Granted".toast(context)
                    }
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permission: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        })
        .withErrorListener {
            it.name.toast(context)
        }
        .check()
}

fun FragmentActivity.changeFragment(containerId: Int, fragment : Fragment, addToBackStack : Boolean = false) {
    val fm = supportFragmentManager.beginTransaction().replace(containerId, fragment)
    if (addToBackStack) fm.addToBackStack(null)
    fm.commit()
}

fun AppCompatActivity.changeFragment(containerId: Int, fragment : Fragment, addToBackStack : Boolean = false) {
    val fm = supportFragmentManager.beginTransaction().replace(containerId, fragment)
    if (addToBackStack) fm.addToBackStack(null)
    fm.commit()
}
