package com.example.contactdata

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contactdata.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etName.setText(intent.getStringExtra(EXTRA_NAME))
        binding.etPhone.setText(intent.getStringExtra(EXTRA_PHONE))
        binding.etEmail.setText(intent.getStringExtra(EXTRA_EMAIL))
        binding.etAddress.setText(intent.getStringExtra(EXTRA_ADDRESS))

        binding.btnSave.setOnClickListener {
            setResult(
                RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_NAME, binding.etName.text.toString())
                    putExtra(EXTRA_PHONE, binding.etPhone.text.toString())
                    putExtra(EXTRA_EMAIL, binding.etEmail.text.toString())
                    putExtra(EXTRA_ADDRESS, binding.etAddress.text.toString())
                    putExtra(EXTRA_IS_NEW, intent.getBooleanExtra(EXTRA_IS_NEW, false))
                }
            )
            finish()
        }
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_PHONE = "extra_phone"
        const val EXTRA_EMAIL = "extra_email"
        const val EXTRA_ADDRESS = "extra_address"
        const val EXTRA_IS_NEW = "extra_is_new"
    }
}
