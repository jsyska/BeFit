package com.example.befit

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.befit.databinding.ActivityCamBinding
import com.example.befit.databinding.ActivityLoginBinding
import com.example.befit.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class CamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCamBinding
    private var processingBarcode = AtomicBoolean(false)
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private lateinit var preview: Preview
    private lateinit var imageAnalysis: ImageAnalysis
    private val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCamBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()

        checkCameraPermission()

        binding.btnBack.setOnClickListener{
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        processingBarcode.set(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkIfCameraPermissionIsGranted()
    }

    private fun checkCameraPermission() {
        try {
            val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, requiredPermissions, 0)
        } catch (e: IllegalArgumentException) {
            checkIfCameraPermissionIsGranted()
        }
    }

    private fun checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            MaterialAlertDialogBuilder(this)
                .setTitle("Permission required")
                .setMessage("This application needs to access the camera to process barcodes")
                .setPositiveButton("Ok") { _, _ ->
                    checkCameraPermission()
                }
                .setCancelable(false)
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }

    private fun startCamera() {
        try {
            // Create an instance of the ProcessCameraProvider,
            // which will be used to bind the use cases to a lifecycle owner.
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

            // Add a listener to the cameraProviderFuture.
            // The first argument is a Runnable, which will be where the magic actually happens.
            // The second argument (way down below) is an Executor that runs on the main thread.
            cameraProviderFuture.addListener({

                cameraProvider = cameraProviderFuture.get()

                preview = Preview.Builder().build()
                binding.previewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                preview.setSurfaceProvider(binding.previewView.surfaceProvider)

                // Setup the ImageAnalyzer for the ImageAnalysis use case
                val builder = ImageAnalysis.Builder()
                    .setTargetResolution(Size(1080, 1920))

                imageAnalysis = builder.build()
                    .also {
                        it.setAnalyzer(cameraExecutor, BarCodeAnalyzer { barcode ->
                            if (processingBarcode.compareAndSet(false, true)) {
                                beep()
                                Log.d("dd--", "Result: $barcode")

                                val intent = Intent()
                                intent.putExtra("BarcodeResult", barcode)
                                setResult(RESULT_OK, intent)
                                finish()

                            }
                        })
                    }

                try {
                    // Unbind any bound use cases before rebinding
                    cameraProvider.unbindAll()
                    // Bind use cases to lifecycleOwner
                    val cam =
                        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)

                } catch (e: Exception) {
                    Log.e("PreviewUseCase", "Binding failed! :(", e)
                }
            }, ContextCompat.getMainExecutor(this))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun beep() {
        val toneGen = ToneGenerator(AudioManager.STREAM_MUSIC, 100)
        toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 200)
    }
}