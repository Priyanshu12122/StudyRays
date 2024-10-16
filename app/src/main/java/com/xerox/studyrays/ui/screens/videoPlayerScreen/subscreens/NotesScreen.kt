package com.xerox.studyrays.ui.screens.videoPlayerScreen.subscreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.stevdzasan.messagebar.rememberMessageBarState
import com.xerox.studyrays.R
import com.xerox.studyrays.db.videoNotesDb.VideoNoteEntity
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoViewModel
import com.xerox.studyrays.ui.screens.videoPlayerScreen.playerUiScreen.Utils
import com.xerox.studyrays.utils.DataNotFoundScreen
import com.xerox.studyrays.utils.LoadingScreen
import com.xerox.studyrays.utils.SpacerHeight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    modifier: Modifier = Modifier,
    vm: VideoViewModel = hiltViewModel(),
    videoId: String,
    onNoteClick: (Long) -> Unit,
) {

    val state by vm.videoNotes.collectAsState()
    val result = state

    LaunchedEffect(key1 = Unit) {
        vm.getVideoNotesById(videoId)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    var isAdding by rememberSaveable { mutableStateOf(false) }

    var note by rememberSaveable { mutableStateOf("") }
    var id by rememberSaveable { mutableStateOf<Int?>(null) }

    var noteItem by remember { mutableStateOf<VideoNoteEntity?>(null) }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "Notes") },
                scrollBehavior = scrollBehavior
            )

        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isAdding = true
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {


        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = modifier
                .padding(it)
                .padding(2.dp)
                .fillMaxSize()
        ) {

//            TopAppBar(
//                title = { Text(text = "Notes") },
//                scrollBehavior = scrollBehavior
//            )

            HorizontalDivider(modifier = modifier.fillMaxWidth(), thickness = 1.dp)


            AddNotesAlertDialog(
                noteText = note,
                onNoteChanged = { note = it },
                onButtonClicked = {
                    vm.insertVideoNote(
                        VideoNoteEntity(
                            timeStamp = vm.getPlayerPosition(),
                            videoId = videoId,
                            note = note,
                            id = id
                        )
                    )
                    note = ""
                    isAdding = false
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = if (id == null) "Note Added Successfully!" else "Note Updated Successfully!",
                            duration = SnackbarDuration.Short
                        )

                        id = null
                    }
                    vm.getVideoNotesById(videoId)
                },
                position = vm.getPlayerPosition(),
                isVisible = isAdding,
                onDismiss = {
                    isAdding = false
                    note = ""
                    id = null
                }
            )

            when (result) {
                is Response.Error -> {
                    DataNotFoundScreen(
                        errorMsg = result.msg,
                        state = rememberMessageBarState(),
                        shouldShowBackButton = false,
                        paddingValues = PaddingValues(),
                        onRetryClicked = { vm.getVideoNotesById(videoId) }) {
                    }
                }

                is Response.Loading -> {
                    LoadingScreen(paddingValues = PaddingValues())
                }

                is Response.Success -> {

                    val list = remember { result.data?.sortedBy { it.timeStamp } }

                    Column(
                        modifier = modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        if (list.isNullOrEmpty()) {

                            val composition by rememberLottieComposition(
                                spec = LottieCompositionSpec.RawRes(
                                    R.raw.comingsoon
                                )
                            )

                            LottieAnimation(
                                composition = composition,
                                iterations = LottieConstants.IterateForever,
                                modifier = Modifier
                                    .size(150.dp)
                            )

                            SpacerHeight(dp = 10.dp)

                            Text(
                                text = "Add Some Notes!", fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            )

                            SpacerHeight(dp = 10.dp)

                            Text(text = "Click on the '+' button to add notes!")

                        } else {

                            LazyColumn {
                                items(list, key = { it.id!! }) { noteEntity ->
                                    EachColumnForNotes(
                                        item = noteEntity,
                                        onEditClicked = {
                                            note = it.note
                                            id = it.id
                                            isAdding = true
                                        },
                                        onDeleteClicked = {
                                            noteItem = it
                                            vm.deleteVideoNote(it.id!!)

                                            scope.launch {
                                                val resultt = snackbarHostState.showSnackbar(
                                                    message = "Deleted Successfully.",
                                                    actionLabel = "Undo",
                                                    withDismissAction = true,
                                                    duration = SnackbarDuration.Indefinite
                                                )

                                                when (resultt) {
                                                    SnackbarResult.ActionPerformed -> {
                                                        vm.insertVideoNote(item = noteItem!!)
                                                        vm.getVideoNotesById(videoId)
                                                    }

                                                    SnackbarResult.Dismissed -> {
                                                        snackbarHostState.currentSnackbarData?.dismiss()
                                                    }
                                                }

                                            }

                                            vm.getVideoNotesById(videoId)

                                        },
                                        onNoteClick = {
                                            onNoteClick(it)
                                        }
                                    )
                                }
                            }
                        }


                    }


                }
            }

        }


    }
}


@Composable
fun EachColumnForNotes(
    modifier: Modifier = Modifier,
    item: VideoNoteEntity,
    onEditClicked: (VideoNoteEntity) -> Unit,
    onDeleteClicked: (VideoNoteEntity) -> Unit,
    onNoteClick: (Long) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            Column(
                modifier = modifier
                    .weight(8f)
                    .clickable {
                        onNoteClick(item.timeStamp)
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {

                Text(
                    text = Utils.formatVideoDuration(item.timeStamp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                SpacerHeight(dp = 5.dp)

                Text(text = item.note)

            }

            Column(
                modifier = modifier
                    .weight(1f),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End
            ) {

                IconButton(onClick = {
                    onEditClicked(item)
                }) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = "")
                }

                IconButton(onClick = {
                    onDeleteClicked(item)
                }) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = "")
                }

            }
        }

        HorizontalDivider(modifier = modifier.fillMaxWidth())
    }

}

@Composable
fun AddNotesAlertDialog(
    modifier: Modifier = Modifier,
    noteText: String,
    item: VideoNoteEntity? = null,
    onNoteChanged: (String) -> Unit,
    onButtonClicked: () -> Unit,
    position: Long,
    isVisible: Boolean,
    onDismiss: () -> Unit,
) {

    if (isVisible) {
        AlertDialog(onDismissRequest = { onDismiss() }, confirmButton = { },
            text = {

                Column(
                    modifier = modifier.padding(vertical = 10.dp, horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                ) {

                    Text(
                        text = Utils.formatVideoDuration(item?.timeStamp ?: position),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    SpacerHeight(dp = 8.dp)

                    OutlinedTextField(
                        value = noteText, onValueChange = { onNoteChanged(it) },
                        placeholder = {
                            Text(text = "Note Description")
                        },
                        modifier = modifier
                            .fillMaxWidth()
                            .height(80.dp)
                    )

                    SpacerHeight(dp = 8.dp)

                    Button(
                        onClick = {
                            onButtonClicked()
                        },
                        modifier = modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {

                        Text(text = "SAVE NOTE")

                    }
                }


            }
        )
    }
}
