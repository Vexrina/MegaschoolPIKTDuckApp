import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.sample.duckapp.infra.Api

class DuckViewModel(private val duckRepository: DuckRepository) : ViewModel() {
    private val _imageUrlLiveData = MutableLiveData<String>()
    val imageUrlLiveData: LiveData<String> = _imageUrlLiveData

    private val _bitmapLiveData = MutableLiveData<Bitmap>()
    val bitmapLiveData: LiveData<Bitmap> = _bitmapLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun fetchRandomDuck(owner: LifecycleOwner) {
        duckRepository.fetchRandomDuck().observe(owner) { duck ->
            duck?.let {
                _imageUrlLiveData.postValue(duck.url)
            } ?: run {
                _errorLiveData.postValue("Failed to fetch duck")
            }
        }
    }

    fun fetchStatusCodeDuck(owner: LifecycleOwner, statusCode: String) {
        duckRepository.fetchStatusCodeDuck(statusCode).observe(owner) { bitmap ->
            bitmap?.let {
                Log.d("Success", "get status code duck")
                _bitmapLiveData.postValue(bitmap)
            } ?: run {
                _errorLiveData.postValue("Failed to fetch duck")
            }
        }
    }

    constructor() : this(DuckRepository(Api.ducksApi))
}

