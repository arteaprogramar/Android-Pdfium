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
import android.widget.Toast
import arte.programar.materialfile.MaterialFilePicker
import arte.programar.materialfile.ui.FilePickerActivity
import arte.programar.pdfium.Pdfium
import arte.programar.pdfium.util.Size
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.regex.Pattern

class MainActivity : PermissionActivity() {

    // Variables
    private var TAG: String = MainActivity::class.java.simpleName
    private val FILE_PICKER_REQUEST_CODE: Int = 989


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request Permission
        requestAppPermissions(
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ),
            R.string.app_name, 39
        )
    }

    override fun onPermissionsGranted(requestCode: Int) {
        startFileExplorer()
    }

    override fun onPermissionDenied(requestCode: Int) {
        Toast.makeText(applicationContext, "Permission required", Toast.LENGTH_LONG).show()
    }

    private fun startFileExplorer() {
        MaterialFilePicker().withActivity(this)
            .withCloseMenu(true)
            .withFilter(Pattern.compile(".*\\.(pdf|PDF)$"))
            .withPath(Environment.DIRECTORY_DOWNLOADS)
            .withTitle("PDFium Test")
            .withRequestCode(FILE_PICKER_REQUEST_CODE)
            .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val filePath: String? = data!!.getStringExtra(FilePickerActivity.RESULT_FILE_PATH)
            renderPage(filePath)
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