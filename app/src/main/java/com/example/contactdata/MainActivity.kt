package com.example.contactdata

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.contactdata.databinding.ActivityMainBinding

data class Contact(
    var name: String = "",
    var phone: String = "",
    var email: String = "",
    var address: String = ""
) {
    override fun toString(): String =
        "ФИО: $name\nТелефон: $phone\nE-mail: $email\nАдрес: $address"
}

// хранение и листание списка
class ContactViewModel : ViewModel() {

    val contacts = mutableListOf(
        Contact("Иванов Иван Иванович", "+7 (900) 111-22-33", "ivanov@mail.ru", "г. Москва, ул. Ленина, д. 1"),
        Contact("Петров Пётр Петрович", "+7 (900) 222-33-44", "petrov@mail.ru", "г. СПб, Невский пр., д. 10"),
    )

    var currentIndex = 0
        private set

    fun currentContact(): Contact? =
        if (contacts.isEmpty()) null else contacts[currentIndex]

    fun next() {
        if (contacts.isEmpty()) return
        currentIndex = (currentIndex + 1) % contacts.size
    }

    fun prev() {
        if (contacts.isEmpty()) return
        currentIndex = (currentIndex - 1 + contacts.size) % contacts.size
    }

    fun updateCurrent(contact: Contact) {
        if (contacts.isEmpty()) {
            contacts.add(contact)
            currentIndex = 0
        } else {
            contacts[currentIndex] = contact
        }
    }

    fun add(contact: Contact) {
        contacts.add(contact)
        currentIndex = contacts.size - 1
    }
}

// начальный экран
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ContactViewModel by viewModels()

    private val editLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data ?: return@registerForActivityResult
                val isNew = data.getBooleanExtra(EditActivity.EXTRA_IS_NEW, false)
                val contact = Contact(
                    name = data.getStringExtra(EditActivity.EXTRA_NAME).orEmpty(),
                    phone = data.getStringExtra(EditActivity.EXTRA_PHONE).orEmpty(),
                    email = data.getStringExtra(EditActivity.EXTRA_EMAIL).orEmpty(),
                    address = data.getStringExtra(EditActivity.EXTRA_ADDRESS).orEmpty()
                )
                if (isNew) viewModel.add(contact) else viewModel.updateCurrent(contact)
                showCurrent()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChange.setOnClickListener {
            editLauncher.launch(
                Intent(this, EditActivity::class.java).apply {
                    viewModel.currentContact()?.let { c ->
                        putExtra(EditActivity.EXTRA_NAME, c.name)
                        putExtra(EditActivity.EXTRA_PHONE, c.phone)
                        putExtra(EditActivity.EXTRA_EMAIL, c.email)
                        putExtra(EditActivity.EXTRA_ADDRESS, c.address)
                    }
                }
            )
        }

        binding.btnAdd.setOnClickListener {
            editLauncher.launch(
                Intent(this, EditActivity::class.java).apply {
                    putExtra(EditActivity.EXTRA_IS_NEW, true)
                }
            )
        }

        binding.btnPrev.setOnClickListener {
            viewModel.prev()
            showCurrent()
        }

        binding.btnNext.setOnClickListener {
            viewModel.next()
            showCurrent()
        }

        showCurrent()
    }

    private fun showCurrent() {
        binding.tvContact.text = viewModel.currentContact()?.toString() ?: "Нет данных"
    }
}
