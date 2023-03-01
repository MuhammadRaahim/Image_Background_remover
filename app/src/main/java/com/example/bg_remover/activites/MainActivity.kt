package com.example.bg_remover.activites


import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import com.example.bg_remover.utils.BackgroundRemover
import com.example.bg_remover.callbacks.OnBackgroundChangeListener
import com.example.bg_remover.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val imageResult =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { uri ->
                binding.ivImage.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setClickListeners()
    }

    private fun initViews() {
        launchPicker()
    }

    private fun setClickListeners() {
        binding.apply {
            btnBg.setOnClickListener {
                removeBg()
            }
            btnAdd.setOnClickListener {
                launchPicker()
            }
        }
    }

    private fun launchPicker() {
        imageResult.launch("image/*")
    }

    private fun removeBg() {
        binding.ivImage.invalidate()
        BackgroundRemover.bitmapForProcessing(
            binding.ivImage.drawable.toBitmap(),
            true,
            object : OnBackgroundChangeListener {
                override fun onSuccess(bitmap: Bitmap) {
                    binding.ivImage.setImageBitmap(bitmap)
                }

                override fun onFailed(exception: Exception) {
                    Toast.makeText(this@MainActivity, "Error Occur", Toast.LENGTH_SHORT).show()
                }
            })
    }
}