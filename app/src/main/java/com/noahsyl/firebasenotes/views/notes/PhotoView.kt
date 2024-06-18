package com.noahsyl.firebasenotes.views.notes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.rememberAsyncImagePainter
import com.noahsyl.firebasenotes.viewModels.NotesViewModel

@Composable
fun PhotoView(notesVM: NotesViewModel, idDoc: String){

    //TODO

    Column {
        val state = notesVM.state

        if (state.imagePath.isNotEmpty()){
            Image(painter = rememberAsyncImagePainter(state.imagePath),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

        }

    }

}