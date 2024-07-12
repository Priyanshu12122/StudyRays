package com.xerox.studyrays.ui.studyfocus.subjectScreen

import androidx.compose.material3.SnackbarDuration
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xerox.studyrays.data.StudyFocusRepository
import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity
import com.xerox.studyrays.db.studyFocusDb.TaskEntityy
import com.xerox.studyrays.utils.SnackbarEvent
import com.xerox.studyrays.utils.toHours
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SubjectViewModel @Inject constructor(
    private val repository: StudyFocusRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val navArgs: Int = savedStateHandle["subjectId"]!! /*savedStateHandle.navArgs()*/

    private val _state = MutableStateFlow(SubjectState())
    val state = combine(
        _state,
        repository.getUpcomingTasksForSubject(navArgs),
        repository.getCompletedTasksForSubject(navArgs),
        repository.getRecentTenSessionsForSubject(navArgs),
        repository.getTotalSessionsDurationBySubject(navArgs)
    ) { state, upcomingTasks, completedTask, recentSessions, totalSessionsDuration ->
        state.copy(
            upcomingTasks = upcomingTasks,
            completedTasks = completedTask,
            recentSessions = recentSessions,
            studiedHours = totalSessionsDuration.toHours()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = SubjectState()
    )

    private val _snackbarEventFlow = MutableSharedFlow<SnackbarEvent>()
    val snackbarEventFlow = _snackbarEventFlow.asSharedFlow()

    init {
        fetchSubject()
    }

    fun onEvent(event: SubjectEvent) {
        when (event) {
            is SubjectEvent.OnSubjectCardColorChange -> {
                _state.update {
                    it.copy(subjectCardColors = event.color)
                }
            }

            is SubjectEvent.OnSubjectNameChange -> {
                _state.update {
                    it.copy(subjectName = event.name)
                }
            }

            is SubjectEvent.OnGoalStudyHoursChange -> {
                _state.update {
                    it.copy(goalStudyHours = event.hours)
                }
            }

            is SubjectEvent.OnDeleteSessionButtonClick -> {
                _state.update {
                    it.copy(session = event.session)
                }
            }
            is SubjectEvent.OnTaskIsCompleteChange -> {
                updateTask(event.task)
            }

            SubjectEvent.UpdateSubject -> updateSubject()
            SubjectEvent.DeleteSubject -> deleteSubject()
            SubjectEvent.DeleteSession -> deleteSession()

            SubjectEvent.UpdateProgress -> {
                val goalStudyHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f
                _state.update {
                    it.copy(
                        progress = (state.value.studiedHours / goalStudyHours).coerceIn(0f, 1f)
                    )
                }
            }
        }
    }

    private fun updateSubject() {
        viewModelScope.launch {
            try {
                repository.upsertSubject(
                    subject = SubjectsEntity(
                        subjectId = state.value.currentSubjectId,
                        name = state.value.subjectName,
                        goalHours = state.value.goalStudyHours.toFloatOrNull() ?: 1f,
                        colors = state.value.subjectCardColors.map { it.toArgb() }
                    )
                )
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(message = "Subject updated successfully.")
                )
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Couldn't update subject. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun fetchSubject() {
        viewModelScope.launch {
            repository
                .getSubjectById(navArgs)?.let { subject ->
                    _state.update {
                        it.copy(
                            subjectName = subject.name,
                            goalStudyHours = subject.goalHours.toString(),
                            subjectCardColors = subject.colors.map { colors -> Color(colors) },
                            currentSubjectId = subject.subjectId
                        )
                    }
                }
        }
    }

    private fun deleteSubject() {
        viewModelScope.launch {
            try {
                val currentSubjectId = state.value.currentSubjectId
                if (currentSubjectId != null) {
                    withContext(Dispatchers.IO) {
                        repository.deleteSubject(subjectId = currentSubjectId)
                    }
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(message = "Subject deleted successfully")
                    )
                    _snackbarEventFlow.emit(SnackbarEvent.NavigateUp)
                } else {
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(message = "No Subject to delete")
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Couldn't delete subject. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun updateTask(task: TaskEntityy) {
        viewModelScope.launch {
            try {
                repository.upsertTask(
                    task = task.copy(isComplete = !task.isComplete)
                )
                if (task.isComplete) {
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(message = "Saved in upcoming tasks.")
                    )
                } else {
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(message = "Saved in completed tasks.")
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Couldn't update task. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

    private fun deleteSession() {
        viewModelScope.launch {
            try {
                state.value.session?.let {
                    repository.deleteSession(it)
                    _snackbarEventFlow.emit(
                        SnackbarEvent.ShowSnackbar(message = "Session deleted successfully")
                    )
                }
            } catch (e: Exception) {
                _snackbarEventFlow.emit(
                    SnackbarEvent.ShowSnackbar(
                        message = "Couldn't delete session. ${e.message}",
                        duration = SnackbarDuration.Long
                    )
                )
            }
        }
    }

}