package com.noahsyl.firebasenotes.views.notes

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.noahsyl.firebasenotes.R
import com.noahsyl.firebasenotes.viewModels.NotesViewModel

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteView(navController: NavController, notesVM: NotesViewModel) {
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    val context = LocalContext.current

    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile( //fábrica de Uris
        Objects.requireNonNull(context),
        context.packageName + ".provider", file
    )

    var image by remember {
        mutableStateOf<Uri>(Uri.EMPTY) //lo creamos con un Uri vacio
    }

    val imageDefault = R.drawable.photo
    val permisionCheckResult =
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)

    var cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) {
        image = uri

    }

    //permission

    val permisionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) {
            if (it != null) {
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "Permiso denegado", Toast.LENGTH_LONG)
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Nueva Nota") },
                navigationIcon = {
                    IconButton(onClick = {
                        notesVM.saveNote(title, note, image) {
                            Toast.makeText(context, "Guardado!", Toast.LENGTH_LONG).show()
                            navController.popBackStack()
                        }

                    }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
                    }
                }
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(text = "Titulo") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            )

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text(text = "Nota") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            )

            Image(
                modifier = Modifier
                    .clickable {
                        if (permisionCheckResult == PackageManager.PERMISSION_GRANTED) {
                            cameraLauncher.launch(uri)
                        } else {
                            permisionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                    .padding(16.dp, 8.dp)
                    .size(750.dp),
                painter = rememberAsyncImagePainter(if (image.path?.isNotEmpty() == true) image else imageDefault),
                contentDescription = null
            )

        }
    }

}

@SuppressLint("SimpleDateFormat")
fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
}
