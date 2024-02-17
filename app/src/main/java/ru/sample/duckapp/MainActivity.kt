package ru.sample.duckapp

import DuckViewModel
import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: DuckViewModel
    private lateinit var imageViewDuck: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(DuckViewModel::class.java)

        viewModel.imageUrlLiveData.observe(this, Observer<String> { imageUrl ->
            loadImage(imageUrl)
        })

        viewModel.bitmapLiveData.observe(this, Observer { bitmap ->
            imageViewDuck.setImageBitmap(bitmap)
        })

        viewModel.errorLiveData.observe(this, Observer<String> { error ->
            showErrorDialog(this, "Произошла неконтролируемая ошибка")
        })


        val buttonFetchDuck: Button = findViewById(R.id.buttonFetchDuck)
        val editText: EditText = findViewById(R.id.statusCodeEntry)

        val listStatusCodes = setOf<String>(
            "100", "200", "301", "302", "400", "403", "404",
            "409", "413", "418", "420", "426", "429", "451", "500"
        )

        buttonFetchDuck.setOnClickListener {
            val str = editText.text.toString()
            if (str.isEmpty()) {
                viewModel.fetchRandomDuck(this)
            } else if (str.trimStart('0') !in listStatusCodes) {
                val errorMessage = "Ошибка: Статус код $str не поддерживается API."
                showErrorDialog(this, errorMessage)
            } else {
                viewModel.fetchStatusCodeDuck(this, str.trimStart('0'))
            }
        }

        imageViewDuck = findViewById(R.id.imageViewDuck)
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(imageViewDuck)
    }

    private fun showErrorDialog(context: Context, errorMessage: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Ошибка")
        builder.setMessage(errorMessage)
        builder.setPositiveButton(":C") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
