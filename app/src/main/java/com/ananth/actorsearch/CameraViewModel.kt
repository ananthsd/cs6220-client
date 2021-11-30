package com.ananth.actorsearch

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.*

data class CameraState (val image:Bitmap? = null)

class CameraViewModel(application: Application) : AndroidViewModel(application) {
    var uiState = mutableStateOf<CameraState>(CameraState())
    var networking = Networking()
    var celebrity = MutableLiveData<Celebrity>()

    fun searchImage(){
        uiState.value.image?.let { image ->
            viewModelScope.launch(Dispatchers.IO) {
                val f = File(getApplication<Application>().cacheDir, "image.png")
                f.createNewFile()
                val stream = ByteArrayOutputStream()
                image.compress(Bitmap.CompressFormat.PNG,100,stream)
                val data = stream.toByteArray()
                var fos:FileOutputStream? = null
                try {
                    fos = FileOutputStream(f)
                } catch (e: FileNotFoundException) {
                    //show snackbar
                    return@launch
                }
                try {
                    fos?.let { fos ->
                        fos.write(data)
                        fos.flush()
                        fos.close()
                    }
                } catch (e: IOException) {
                    //show snackbar
                    return@launch
                }
                val result = networking.postImage(f)
                Log.v("Network",result)
                val obj = JSONObject(result)
                if(obj.has("name")){
                    val wikipediaObj = obj.getJSONArray("wikipedia")
                    val articles = mutableListOf<WikipediaArticle>()

                    for(i in 0 until wikipediaObj.length()){
                        val articleObj = wikipediaObj.getJSONObject(i)
                        articles.add(WikipediaArticle(articleObj.getString("title"), articleObj.getString("url")))
                    }
                    val wikipedia = Wikipedia(articles)
                    val tempCeleb = Celebrity(obj.getString("name"), wikipedia)
                    withContext(Dispatchers.Main) {
                        celebrity.value = tempCeleb
                    }
                }

            }
        }
    }
}