package com.xerox.studyrays.ui.studyfocus.dashboardScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.studysmart.presentation.components.AddSubjectDialog
import com.xerox.studyrays.R
import com.xerox.studyrays.db.studyFocusDb.SessionEntity
import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity
import com.xerox.studyrays.db.studyFocusDb.TaskEntityy
import com.xerox.studyrays.ui.studyfocus.components.CountCard
import com.xerox.studyrays.ui.studyfocus.components.DeleteDialog
import com.xerox.studyrays.ui.studyfocus.components.SubjectCard
import com.xerox.studyrays.ui.studyfocus.components.studySessionsList
import com.xerox.studyrays.ui.studyfocus.components.tasksList
import com.xerox.studyrays.utils.SnackbarEvent
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest


@Composable
fun DashboardScreenRoute(
    viewModel: DashboardViewModel = hiltViewModel(),
    onSubjectCardClick: (Int) -> Unit,
    onTaskCardClick: (Int?) -> Unit,
    onSessionCardClick: () -> Unit,
) {


    val state by viewModel.state.collectAsStateWithLifecycle()
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val recentSessions by viewModel.recentSessions.collectAsStateWithLifecycle()

    DashboardScreen(
        state = state,
        tasks = tasks,
        recentSessions = recentSessions,
        onEvent = viewModel::onEvent,
        snackbarEvent = viewModel.snackbarEventFlow,
        onSubjectCardClick = { subjectId ->
            subjectId?.let {
                onSubjectCardClick(subjectId)
//                val navArg = SubjectScreenNavArgs(subjectId = subjectId)
//                navigator.navigate(SubjectScreenRouteDestination(navArgs = navArg))
            }
        },
        onTaskCardClick = { taskId ->
            onTaskCardClick(taskId)
//            val navArg = TaskScreenNavArgs(taskId = taskId, subjectId = null)
//            navigator.navigate(TaskScreenRouteDestination(navArgs = navArg))
        },
        onStartSessionButtonClick = {
            onSessionCardClick()
//            navigator.navigate(SessionScreenRouteDestination())
        }
    )
}


@Composable
private fun DashboardScreen(
    state: DashboardState,
    tasks: List<TaskEntityy>,
    recentSessions: List<SessionEntity>,
    onEvent: (DashboardEvent) -> Unit,
    snackbarEvent: SharedFlow<SnackbarEvent>,
    onSubjectCardClick: (Int?) -> Unit,
    onTaskCardClick: (Int?) -> Unit,
    onStartSessionButtonClick: () -> Unit,
) {

    var isAddSubjectDialogOpen by rememberSaveable { mutableStateOf(false) }
    var isDeleteSessionDialogOpen by rememberSaveable { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        snackbarEvent.collectLatest { event ->
            when (event) {
                is SnackbarEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message,
                        duration = event.duration
                    )
                }

                SnackbarEvent.NavigateUp -> {}
            }
        }
    }

    AddSubjectDialog(
        isOpen = isAddSubjectDialogOpen,
        subjectName = state.subjectName,
        goalHours = state.goalStudyHours,
        selectedColors = state.subjectCardColors,
        onSubjectNameChange = { onEvent(DashboardEvent.OnSubjectNameChange(it)) },
        onGoalHoursChange = { onEvent(DashboardEvent.OnGoalStudyHoursChange(it)) },
        onColorChange = { onEvent(DashboardEvent.OnSubjectCardColorChange(it)) },
        onDismissRequest = { isAddSubjectDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(DashboardEvent.SaveSubject)
            isAddSubjectDialogOpen = false
        }
    )

    DeleteDialog(
        isOpen = isDeleteSessionDialogOpen,
        title = "Delete Session?",
        bodyText = "Are you sure, you want to delete this session? Your studied hours will be reduced " +
                "by this session time. This action can not be undone.",
        onDismissRequest = { isDeleteSessionDialogOpen = false },
        onConfirmButtonClick = {
            onEvent(DashboardEvent.DeleteSession)
            isDeleteSessionDialogOpen = false
        }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = { DashboardScreenTopBar() }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                CountCardsSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    subjectCount = state.totalSubjectCount,
                    studiedHours = state.totalStudiedHours.toString(),
                    goalHours = state.totalGoalStudyHours.toString()
                )
            }
            item {
                SubjectCardsSection(
                    modifier = Modifier.fillMaxWidth(),
                    subjectList = state.subjects,
                    onAddIconClicked = { isAddSubjectDialogOpen = true },
                    onSubjectCardClick = onSubjectCardClick
                )
            }
            item {
                Button(
                    onClick = onStartSessionButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 48.dp, vertical = 20.dp)
                ) {
                    Text(text = "Start Study Session")
                }
            }
            tasksList(
                sectionTitle = "UPCOMING TASKS",
                emptyListText = "You don't have any upcoming tasks.\n " +
                        "Click the + button in subject screen to add new task.",
                tasks = tasks,
                onCheckBoxClick = { onEvent(DashboardEvent.OnTaskIsCompleteChange(it)) },
                onTaskCardClick = onTaskCardClick
            )
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
            studySessionsList(
                sectionTitle = "RECENT STUDY SESSIONS",
                emptyListText = "You don't have any recent study sessions.\n " +
                        "Start a study session to begin recording your progress.",
                sessions = recentSessions,
                onDeleteIconClick = {
                    onEvent(DashboardEvent.OnDeleteSessionButtonClick(it))
                    isDeleteSessionDialogOpen = true
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DashboardScreenTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "StudySmart",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    )
}

@Composable
private fun CountCardsSection(
    modifier: Modifier,
    subjectCount: Int,
    studiedHours: String,
    goalHours: String,
) {
    Row(modifier = modifier) {
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Subject Count",
            count = "$subjectCount"
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Studied Hours",
            count = studiedHours
        )
        Spacer(modifier = Modifier.width(10.dp))
        CountCard(
            modifier = Modifier.weight(1f),
            headingText = "Goal Study Hours",
            count = goalHours
        )
    }
}

@Composable
private fun SubjectCardsSection(
    modifier: Modifier,
    subjectList: List<SubjectsEntity>,
    emptyListText: String = "You don't have any subjects.\n Click the + button to add new subject.",
    onAddIconClicked: () -> Unit,
    onSubjectCardClick: (Int?) -> Unit,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "SUBJECTS",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 12.dp)
            )
            IconButton(onClick = onAddIconClicked) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Subject"
                )
            }
        }
        if (subjectList.isEmpty()) {
            Image(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.img_books),
                contentDescription = emptyListText
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = emptyListText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(subjectList) { subject ->
                SubjectCard(
                    subjectName = subject.name,
                    gradientColors = subject.colors.map { Color(it) },
                    onClick = { onSubjectCardClick(subject.subjectId) }
                )
            }
        }
    }
}