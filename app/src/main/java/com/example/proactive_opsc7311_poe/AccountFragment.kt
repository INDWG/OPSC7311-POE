package com.example.proactive_opsc7311_poe

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.shape.ShapeAppearanceModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.provider.MediaStore
import java.io.IOException

class AccountFragment : Fragment() {

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var dateButton: Button
    private lateinit var profilePictureButton: ShapeableImageView
    private lateinit var pickImageLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>
    private lateinit var currentPhotoUri: Uri
    private lateinit var imageView: ImageView

    private val CAMERA_PERMISSION_REQUEST_CODE = 101
    private var isCameraPermissionGranted: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.account_fragment, container, false)
        dateButton = view.findViewById(R.id.datePickerButton)
        profilePictureButton = view.findViewById(R.id.profilePicture)
        imageView = view.findViewById(R.id.cameraImageView)
        initImagePickers()
        initDatePicker()
        dateButton.text = todaysDate

        // Set click listener for the profile picture button
        profilePictureButton.setOnClickListener {
            openImagePicker()
        }

        return view
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isCameraPermissionGranted = true
                showImagePickerDialog()
            } else {
                isCameraPermissionGranted = false
            }
        }
    }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            val newMonth = month + 1
            val date = makeDateString(day, newMonth, year)
            dateButton.text = date
        }

        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_DARK

        datePickerDialog = DatePickerDialog(requireContext(), style, dateSetListener, year, month, day)
    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        val formattedDay = if (day < 10) "0$day" else day.toString()
        val formattedMonth = if (month < 10) "0$month" else month.toString()
        return "$formattedDay/$formattedMonth/$year"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dateButton.setOnClickListener {
            openDateClicker()
        }
    }

    private fun openDateClicker() {
        datePickerDialog.show()
    }

    private fun openImagePicker() {
        if (hasCameraPermission()) {
            showImagePickerDialog()
        } else {
            requestCameraPermission()
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    private fun initImagePickers() {
        // Initialize the launcher for picking images
        pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { imageUri ->
                // Set the chosen image as profile picture
                setProfilePicture(imageUri)
            }
        }

        // Initialize the launcher for taking pictures
        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                setProfilePictureFromBitmap(imageBitmap)
            }
        }
    }

    private fun setProfilePictureFromBitmap(imageBitmap: Bitmap) {
        // Set the captured image as profile picture
        profilePictureButton.setImageBitmap(imageBitmap)
        profilePictureButton.visibility = View.VISIBLE
        profilePictureButton.shapeAppearanceModel = profilePictureButton.shapeAppearanceModel
            .toBuilder()
            .setAllCornerSizes(ShapeAppearanceModel.PILL)
            .build()
        profilePictureButton.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.isVisible = false
    }

    private fun showImagePickerDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Profile Picture")
            .setMessage("Select an option")
            .setPositiveButton("Take Photo") { _, _ ->
                // Launch camera app to take a photo
                val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                try {
                    if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
                        takePictureLauncher.launch(takePictureIntent)
                    }
                } catch (e: ActivityNotFoundException) {
                    // Handle the ActivityNotFoundException here
                }
            }
            .setNegativeButton("Choose from Gallery") { _, _ ->
                // Launch gallery to choose a photo
                pickImageLauncher.launch("image/*")
            }
            .show()
    }

    private fun setProfilePicture(imageUri: Uri) {
        profilePictureButton.visibility = View.VISIBLE
        profilePictureButton.setImageURI(imageUri)
        profilePictureButton.shapeAppearanceModel = profilePictureButton.shapeAppearanceModel
            .toBuilder()
            .setAllCornerSizes(ShapeAppearanceModel.PILL)
            .build()
        profilePictureButton.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.isVisible = false
    }

    private val todaysDate: String
        get() {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            var month = cal.get(Calendar.MONTH)
            month += 1
            val day = cal.get(Calendar.DAY_OF_MONTH)
            return makeDateString(day, month, year)
        }
}