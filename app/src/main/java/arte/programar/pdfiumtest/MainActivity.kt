package arte.programar.pdfiumtest

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import arte.programar.materialfile.MaterialFilePicker
import arte.programar.materialfile.sample.PermissionActivity
import arte.programar.materialfile.ui.FilePickerActivity
import arte.programar.pdfium.Pdfium
import arte.programar.pdfium.util.Size
import java.io.File
import java.util.regex.Pattern

class MainActivity : PermissionActivity() {

    private var TAG: String = MainActivity::class.java.simpleName

    // Variables
    private lateinit var image: ImageView

    private val startForResultFiles = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        onActivityResult(result.resultCode, result.data)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        image = findViewById(R.id.image)

        // Request Permission
        requestAppPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

    override fun onPermissionsGranted() {
        startFileExplorer()
    }

    override fun onPermissionsDenied(permissions: Array<String>) {
        Toast.makeText(applicationContext, "Permission required", Toast.LENGTH_LONG).show()
    }

    private fun startFileExplorer() {
        MaterialFilePicker().withActivity(this)
            .withCloseMenu(true)
            .withFilter(Pattern.compile(".*\\.(pdf|PDF)$"))
            .withPath(Environment.DIRECTORY_DOWNLOADS)
            .withTitle("PDFium Test")
            .withActivityResultApi(startForResultFiles)
            .start()
    }

    private fun onActivityResult(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val path: String? = data?.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)

            if (path != null) {
                val filePath: String? = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
                renderPage(filePath)
            }
        }
    }

    private fun renderPage(filePath: String?) {
        try {
            val pdfium = Pdfium()
            val parceable = contentResolver.openFileDescriptor(Uri.fromFile(File(filePath)), "r")
            pdfium.newDocument(parceable, "Hola123")

            // Render Image
            Log.d(TAG, "Page count: ${pdfium.pageCount}")

            pdfium.openPage(0)

            val size: Size = pdfium.getPageSize(0)
            val width: Int = getScreenWidth()
            val height: Int = getScreenHeight()

            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            pdfium.renderPageBitmap(bitmap, 0, 0, 0, width, height, true)

            image.setImageBitmap(bitmap)

            // Log for text
            //val text = pdfium.extractCharacters(0, 0, pdfium.countCharactersOnPage(0))
            val text = pdfium.extractText(0)
            Log.d(TAG, text)

            pdfium.closeDocument()
        } catch (ex: Exception){
            Log.d(TAG, ex.toString())
        }
    }

    private fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    private fun getScreenWidth(): Int {
        return Resources.getSystem().displayMetrics.widthPixels
    }

}