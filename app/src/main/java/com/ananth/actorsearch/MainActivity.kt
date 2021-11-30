package com.ananth.actorsearch

import android.app.Activity
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.ananth.actorsearch.ui.main.DisplayFragment
import com.ananth.actorsearch.ui.main.DisplayViewModel
import com.ananth.actorsearch.ui.theme.ActorSearchTheme

class MainActivity : FragmentActivity() {
    val viewModel: CameraViewModel by viewModels()
    lateinit var startForResult: ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_activity)
        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as Bitmap
                Log.v("Camera","new image taken")
                viewModel.uiState.value = CameraState(imageBitmap)
//                imageView.setImageBitmap(imageBitmap)
            }
        }

        viewModel.celebrity.observe(this, Observer { celeb ->
            val intent = Intent(this, DisplayActivity::class.java)
            intent.putExtra("celebrity",celeb)
            startActivity(intent)
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, DisplayFragment.newInstance())
//                .commitNow()
        })

        setContent {
            ActorSearchTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Main(viewModel = viewModel) { dispatchTakePictureIntent() }
                }
            }
        }
    }


    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startForResult.launch(takePictureIntent)
    }
}

@Composable
fun Main(viewModel: CameraViewModel, photoCallback : ()->Unit = {}){
//    Scaffold(topBar = TopAppBar() {
//        Constraint
//    }) {
//
//    }
    val state = viewModel.uiState.value
    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Actor Face Recognition") })
    }, floatingActionButton = {
        FloatingActionButton(onClick = { photoCallback()}) {
            Icon(Icons.Rounded.Search, contentDescription = "Search Button")
        }
    }) {
        state.image?.let { bitmap ->
            AndroidView(
                modifier = Modifier.fillMaxSize(), // Occupy the max size in the Compose UI tree
                factory = { context ->
                    // Creates custom view
//                    CustomView(context).apply {
//                        // Sets up listeners for View -> Compose communication
//                        myView.setOnClickListener {
//                            selectedItem.value = 1
//                        }
//                    }
                    ImageView(context).apply {
                        setImageBitmap(bitmap)
                    }
                },
                update = { view ->
                    // View's been inflated or state read in this block has been updated
                    // Add logic here if necessary

                    // As selectedItem is read here, AndroidView will recompose
                    // whenever the state changes
                    // Example of Compose -> View communication
//                    view.coordinator.selectedItem = selectedItem.value
                    view.apply {
                        setImageBitmap(bitmap)
                    }
                }
            )
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.weight(100f))
                Button(onClick = { viewModel.searchImage() }) {
                    Text(text = "Search")
                }
                Spacer(Modifier.weight(1f))
            }
        } ?: Text(text = "Take a picture")
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ActorSearchTheme {
        Main(viewModel = CameraViewModel(application = Application()))
    }
}