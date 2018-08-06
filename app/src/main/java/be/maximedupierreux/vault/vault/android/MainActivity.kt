package be.maximedupierreux.vault.vault.android

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.fitCenterTransform
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.label.FirebaseVisionLabel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val urls = mutableListOf("")
    var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadNewImage(urls[0])

        btnLoad.setOnClickListener{
            loadNewImage(inputUrl.editText?.text.toString())
        }

        btnOnDevice.setOnClickListener {
            runImageLabelingOnDevice((ivImageToLabel.drawable as BitmapDrawable).bitmap)
        }

        btnCloud.setOnClickListener {
            runImageLabelingCloud((ivImageToLabel.drawable as BitmapDrawable).bitmap)
        }

    }

    private fun loadNewImage(url: String) {
        Glide.with(this)
                .load(url)
                .apply(fitCenterTransform())
                .into(ivImageToLabel)

    }


    fun runImageLabelingCloud(bitmap : Bitmap){
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val options = FirebaseVisionCloudDetectorOptions.Builder()
                .setModelType(FirebaseVisionCloudDetectorOptions.LATEST_MODEL)
                .setMaxResults(15)
                .build()
        val labelDetector = FirebaseVision.getInstance().getVisionCloudLabelDetector(options)
        labelDetector.detectInImage(image).addOnSuccessListener {
            processImageLabelingFromCloud(it)
        }
                .addOnFailureListener{
                    Log.d("LABELFAILURE",it.toString())
                }
    }

    fun runImageLabelingOnDevice(bitmap : Bitmap){
        val image = FirebaseVisionImage.fromBitmap(bitmap)
        val labelDetector = FirebaseVision.getInstance().visionLabelDetector
        labelDetector.detectInImage(image).addOnSuccessListener {
            processImageLabelingFromDevice(it)
        }.addOnFailureListener{
                    Log.d("LABELFAILURE",it.toString())
                }
    }

    fun processImageLabelingFromDevice(labels : MutableList<FirebaseVisionLabel>){
        val labelsSb = StringBuilder()
        labels.forEach {
            Log.d("IMAGELABELING",it.label)
            labelsSb.append(it.label).appendln()
        }

        AlertDialog.Builder(this)
                .setTitle("Labels from device")
                .setMessage(labelsSb.toString())
                .create()
                .show()
    }

    fun processImageLabelingFromCloud(labels : MutableList<FirebaseVisionCloudLabel>){
        val labelsSb = StringBuilder()
        labels.forEach {
            Log.d("IMAGELABELING",it.label)
            labelsSb.append(it.label).appendln()
        }

        AlertDialog.Builder(this)
                .setTitle("Labels from cloud")
                .setMessage(labelsSb.toString())
                .create()
                .show()
    }
}
