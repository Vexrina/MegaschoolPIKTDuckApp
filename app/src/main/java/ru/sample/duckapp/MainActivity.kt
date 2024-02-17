package ru.sample.duckapp

import DuckViewModel
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

        viewModel.bitmapLiveData.observe(this, Observer{bitmap->
            imageViewDuck.setImageBitmap(bitmap)
        })

        viewModel.errorLiveData.observe(this, Observer<String> { error ->
            // Обработка ошибки, если необходимо
        })


        val buttonFetchDuck: Button = findViewById(R.id.buttonFetchDuck)
        val editText: EditText = findViewById(R.id.statusCodeEntry)

        buttonFetchDuck.setOnClickListener {
            val str = editText.text.toString()
            if (str.isEmpty()) {
                viewModel.fetchRandomDuck(this,)
            } else {
                viewModel.fetchStatusCodeDuck(this, str)
            }
        }

        imageViewDuck = findViewById(R.id.imageViewDuck)
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .into(imageViewDuck)
    }
}
