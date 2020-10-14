package arte.programar.pdfiumtest

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

abstract class PermissionActivity : AppCompatActivity() {

    abstract fun onPermissionsGranted(requestCode: Int)

    abstract fun onPermissionDenied(requestCode: Int)

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionCheck: Int = PackageManager.PERMISSION_GRANTED

        grantResults.map {
            permissionCheck += it
        }

        if ((grantResults.isNotEmpty()) && permissionCheck == PackageManager.PERMISSION_GRANTED) {
            onPermissionsGranted(requestCode)
        } else {
            onPermissionDenied(PackageManager.PERMISSION_DENIED)
        }
    }

    fun requestAppPermissions(
        requestedPermissions: Array<String>,
        stringId: Int,
        requestCode: Int
    ) {
        // Check Permission
        var permissionAll: Boolean = true
        val permissionDenied = ArrayList<String>()

        requestedPermissions.map {
            if (ContextCompat.checkSelfPermission(
                    applicationContext,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionDenied.add(it)
                permissionAll = false
            }
        }

        if (permissionAll) {
            onPermissionsGranted(requestCode)
        } else {
            ActivityCompat.requestPermissions(this, permissionDenied.toTypedArray(), requestCode)
        }
    }

}