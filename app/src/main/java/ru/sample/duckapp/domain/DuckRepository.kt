import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.sample.duckapp.data.DucksApi
import ru.sample.duckapp.domain.Duck
import ru.sample.duckapp.infra.Api

class DuckRepository(private val api: DucksApi) {

    // Метод для выполнения запроса на получение случайной уточки
    fun fetchRandomDuck(): LiveData<Duck> {
        Log.d("Tag", "fetchRandomDuck() is called")
        val resultLiveData = MutableLiveData<Duck>()
        api.getRandomDuck().enqueue(object : Callback<Duck> {
            override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
                if (response.isSuccessful) {
                    Log.d("Success", "GET is Successful")
                    resultLiveData.postValue(response.body())
                } else {
                    // В случае ошибки передаем пустой объект Duck или сообщение об ошибке
                    Log.d("WARN", "GET is Successful, but we don't get a response")
                    resultLiveData.postValue(null) // Можете передать любой другой объект Duck или null, если это уместно
                }
            }

            override fun onFailure(call: Call<Duck>, t: Throwable) {
                Log.d("Error", "${t.message}")
                // В случае ошибки передаем пустой объект Duck или сообщение об ошибке
                resultLiveData.postValue(null) // Можете передать любой другой объект Duck или null, если это уместно
            }
        })
        return resultLiveData
    }

    fun fetchStatusCodeDuck(statusCode: String): LiveData<Bitmap> {
        Log.d("Tag", "fetchStatusCodeDuck() is called")

        val resultLiveData = MutableLiveData<Bitmap>()

        api.getDuckByStatusCode(statusCode).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("Success", "GET is Successful")
                    val inputStream = response.body()?.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    resultLiveData.postValue(bitmap)
                } else {
                    Log.d("WARN", "GET is Successful, but we don't get a response")
                    resultLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("Error", "${t.message}")
                resultLiveData.postValue(null)
            }
        })

        return resultLiveData
    }
}
