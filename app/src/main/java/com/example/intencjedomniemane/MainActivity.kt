package com.example.intencjedomniemane

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private var photoUri: Uri? = null

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){ success ->
        if(success){
            findViewById<ImageView>(R.id.image).setImageURI(photoUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonWWW = findViewById<Button>(R.id.wwwButton)
        buttonWWW.setOnClickListener {
            val editText = findViewById<EditText>(R.id.www)
            var url = editText.text.toString()
            if(!url.startsWith("http://") and !url.startsWith("https://")){
                url = "http://$url"
            }
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }
            startActivity(intent)
        }

        val buttonPhone = findViewById<Button>(R.id.phoneButton)
        buttonPhone.setOnClickListener {
            val phoneNumber = findViewById<EditText>(R.id.phone).text.toString()
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)

        }

        val buttonShare = findViewById<Button>(R.id.shareButton)
        buttonShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, findViewById<EditText>(R.id.share).text.toString())
            }
            startActivity(Intent.createChooser(intent, "Udostępnij za pomocą"))
            //startActivity(intent)
        }

        val buttonPhoto = findViewById<Button>(R.id.photoButton)
        /*
        buttonPhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(intent)
        }
         */

        buttonPhoto.setOnClickListener {
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "IMG${System.currentTimeMillis()}.jpg")
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/MyApp")
            }

            photoUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            takePictureLauncher.launch(photoUri!!)

        }

        val buttonSettings = findViewById<Button>(R.id.settingButton)
        buttonSettings.setOnClickListener {
            startActivity(Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS))
        }

        val buttonTimer = findViewById<Button>(R.id.timerButton)
        buttonTimer.setOnClickListener {
            val intent = Intent(AlarmClock.ACTION_SET_TIMER).apply{
                putExtra(AlarmClock.EXTRA_LENGTH, 60)
                putExtra(AlarmClock.EXTRA_MESSAGE, "Alarm!!!")
                putExtra(AlarmClock.EXTRA_SKIP_UI, false)
            }
            startActivity(intent)
        }



        ActivityCompat.requestPermissions(this,
            arrayOf(android.Manifest.permission.CAMERA, android.Manifest.permission.SET_ALARM),
            1
        )
    }
}