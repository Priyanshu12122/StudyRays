package com.xerox.studyrays.data

import com.xerox.studyrays.db.studyFocusDb.SessionDao
import com.xerox.studyrays.db.studyFocusDb.SessionEntity
import com.xerox.studyrays.db.studyFocusDb.SubjectDao
import com.xerox.studyrays.db.studyFocusDb.SubjectsEntity
import com.xerox.studyrays.db.studyFocusDb.TaskDaoo
import com.xerox.studyrays.db.studyFocusDb.TaskEntityy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class StudyFocusRepository @Inject constructor(
    private val sessionDao: SessionDao,
    private val subjectDao: SubjectDao,
    private val taskDao: TaskDaoo,
) {

    suspend fun insertSession(session: SessionEntity) {
        sessionDao.insertSession(session)
    }

    suspend fun deleteSession(session: SessionEntity) {
        sessionDao.deleteSession(session)
    }

    fun getAllSessions(): Flow<List<SessionEntity>> {
        return sessionDao.getAllSessions()
            .map { sessions -> sessions.sortedByDescending { it.date } }
    }

    fun getRecentFiveSessions(): Flow<List<SessionEntity>> {
        return sessionDao.getAllSessions()
            .map { sessions -> sessions.sortedByDescending { it.date } }
            .take(count = 5)
    }

    fun getRecentTenSessionsForSubject(subjectId: Int): Flow<List<SessionEntity>> {
        return sessionDao.getRecentSessionsForSubject(subjectId)
            .map { sessions -> sessions.sortedByDescending { it.date } }
            .take(count = 10)
    }

    fun getTotalSessionsDuration(): Flow<Long> {
        return sessionDao.getTotalSessionsDuration()
    }

    fun getTotalSessionsDurationBySubject(subjectId: Int): Flow<Long> {
        return sessionDao.getTotalSessionsDurationBySubject(subjectId)
    }


    suspend fun upsertSubject(subject: SubjectsEntity) {
        subjectDao.upsertSubject(subject)
    }

    fun getTotalSubjectCount(): Flow<Int> {
        return subjectDao.getTotalSubjectCount()
    }

    fun getTotalGoalHours(): Flow<Float> {
        return subjectDao.getTotalGoalHours()
    }

    suspend fun deleteSubject(subjectId: Int) {
        taskDao.deleteTasksBySubjectId(subjectId)
        sessionDao.deleteSessionsBySubjectId(subjectId)
        subjectDao.deleteSubject(subjectId)
    }

    suspend fun getSubjectById(subjectId: Int): SubjectsEntity? {
        return subjectDao.getSubjectById(subjectId)
    }

    fun getAllSubjects(): Flow<List<SubjectsEntity>> {
        return subjectDao.getAllSubjects()
    }


    suspend fun upsertTask(task: TaskEntityy) {
        taskDao.upsertTask(task)
    }

    suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    suspend fun getTaskById(taskId: Int): TaskEntityy? {
        return taskDao.getTaskById(taskId)
    }

    fun getUpcomingTasksForSubject(subjectId: Int): Flow<List<TaskEntityy>> {
        return taskDao.getTasksForSubject(subjectId)
            .map { tasks -> tasks.filter { it.isComplete.not() } }
            .map { tasks -> sortTasks(tasks) }
    }

    fun getCompletedTasksForSubject(subjectId: Int): Flow<List<TaskEntityy>> {
        return taskDao.getTasksForSubject(subjectId)
            .map { tasks -> tasks.filter { it.isComplete } }
            .map { tasks -> sortTasks(tasks) }
    }

    fun getAllUpcomingTasks(): Flow<List<TaskEntityy>> {
        return taskDao.getAllTasks()
            .map { tasks -> tasks.filter { it.isComplete.not() } }
            .map { tasks -> sortTasks(tasks) }
    }

    fun sortTasks(tasks: List<TaskEntityy>): List<TaskEntityy> {
        return tasks.sortedWith(compareBy<TaskEntityy> { it.dueDate }.thenByDescending { it.priority })
    }

}