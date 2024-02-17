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
    fun fetchRandomDuck(): LiveData<Duck> {
        Log.d("INFO", "fetchRandomDuck() is called")
        val resultLiveData = MutableLiveData<Duck>()
        api.getRandomDuck().enqueue(object : Callback<Duck> {
            override fun onResponse(call: Call<Duck>, response: Response<Duck>) {
                if (response.isSuccessful) {
                    Log.d("SUCCESS", "GET is Successful")
                    resultLiveData.postValue(response.body())
                } else {
                    Log.d("WARN", "GET is Successful, but we don't get a response")
                    resultLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<Duck>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
                resultLiveData.postValue(null)
            }
        })
        return resultLiveData
    }

    fun fetchStatusCodeDuck(statusCode: String): LiveData<Bitmap> {
        Log.d("INFO", "fetchStatusCodeDuck() is called")
        val resultLiveData = MutableLiveData<Bitmap>()
        api.getDuckByStatusCode(statusCode).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Log.d("SUCCESS", "GET is Successful")
                    val inputStream = response.body()?.byteStream()
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    resultLiveData.postValue(bitmap)
                } else {
                    Log.d("WARN", "GET is Successful, but we don't get a response")
                    resultLiveData.postValue(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("ERROR", "${t.message}")
                resultLiveData.postValue(null)
            }
        })
        return resultLiveData
    }
}
