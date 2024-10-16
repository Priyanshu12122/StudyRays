package com.xerox.studyrays.data

import android.content.Context
import android.util.Log
import com.xerox.studyrays.alarmManager.AlarmScheduler
import com.xerox.studyrays.cacheDb.keyGeneratorCache.KeyGenerateDao
import com.xerox.studyrays.cacheDb.keyGeneratorCache.KeyGenerateEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavDao
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchDao
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchEntity
import com.xerox.studyrays.db.alarmDb.AlarmDao
import com.xerox.studyrays.db.alarmDb.AlarmEntity
import com.xerox.studyrays.db.downloadsDb.DownloadNumberDao
import com.xerox.studyrays.db.downloadsDb.DownloadNumberEntity
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourseDao
import com.xerox.studyrays.db.keyDb.KeyDao
import com.xerox.studyrays.db.keyDb.KeyEntity
import com.xerox.studyrays.db.taskDb.TaskDao
import com.xerox.studyrays.db.taskDb.TaskEntity
import com.xerox.studyrays.db.videoNotesDb.VideoNoteDao
import com.xerox.studyrays.db.videoNotesDb.VideoNoteEntity
import com.xerox.studyrays.db.videoProgress.VideoProgressDao
import com.xerox.studyrays.db.videoProgress.VideoProgressEntity
import com.xerox.studyrays.db.watchLaterDb.WatchLaterDao
import com.xerox.studyrays.db.watchLaterDb.WatchLaterEntity
import com.xerox.studyrays.model.downloads.DownloadItem
import com.xerox.studyrays.model.downloads.KeyItem
import com.xerox.studyrays.model.pwModel.CourseItem
import com.xerox.studyrays.model.pwModel.CourseItemX
import com.xerox.studyrays.model.pwModel.Lesson.LessonItem
import com.xerox.studyrays.model.pwModel.PromoItem
import com.xerox.studyrays.model.pwModel.SearchItem
import com.xerox.studyrays.model.pwModel.SearchOldItem
import com.xerox.studyrays.model.pwModel.alertItem.AlertItem
import com.xerox.studyrays.model.pwModel.announcementsItem.AnnouncementItem
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetails
import com.xerox.studyrays.model.pwModel.fetchModel.FetchClassItem
import com.xerox.studyrays.model.pwModel.fetchModel.FetchExamItem
import com.xerox.studyrays.model.pwModel.fetchModel.FetchYearItem
import com.xerox.studyrays.model.pwModel.old.DppOldItem
import com.xerox.studyrays.model.pwModel.old.DppSolutionOldItem
import com.xerox.studyrays.model.pwModel.old.NoteOldItem
import com.xerox.studyrays.model.pwModel.priceItem.PriceItem
import com.xerox.studyrays.model.pwModel.statusItem.StatusItem
import com.xerox.studyrays.network.ApiService
import com.xerox.studyrays.network.EncryptedApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val api: ApiService,
    private val encryptedApi: EncryptedApiService,
    private val dao: FavouriteCourseDao,
    private val navDao: NavDao,
    private val searchDao: SearchDao,
    private val taskDao: TaskDao,
    private val downloadNumberDao: DownloadNumberDao,
    private val alarmDao: AlarmDao,
    private val keyDao: KeyDao,
    private val keyGenerateDao: KeyGenerateDao,
    private val watchLaterDao: WatchLaterDao,
    private val videoProgressDao: VideoProgressDao,
    private val videoNoteDao: VideoNoteDao,
    val context: Context,
) {

    val alarmScheduler = AlarmScheduler(context)

    //    search
    suspend fun insertSearchItem(item: SearchEntity) {
        searchDao.insert(item)
    }

    suspend fun deleteSearchItem(item: SearchEntity) {
        searchDao.delete(item)
    }

    fun getAllSearchItems(): Flow<List<SearchEntity>?> {
        return searchDao.getAll()
    }

    suspend fun exists(searchText: String): Boolean {
        return searchDao.exists(searchText)
    }

//   Comments

    suspend fun getComments(
        externalId: String,
        topicSlug: String,
    ): String {
        return encryptedApi.getComments(
            externalId = externalId,
            topicSlug = topicSlug
        )
    }


//    Total fees


    suspend fun getTotalFee(): PriceItem {
        return api.getTotalFee()
    }


//    Alert

    suspend fun getAlertItem(): AlertItem {
        return api.getAlertItem()
    }

//    NavBar

    suspend fun getNavItems(): NavEntity {
        val cacheResponse = navDao.getById(1)
        return if (cacheResponse != null) {
            cacheResponse
        } else {
            val response = api.getNavItems()
            navDao.upsert(
                NavEntity(
                    id = 1,
                    description = response.description,
                    dismissable = response.dismissable,
                    email = response.email,
                    image = response.image,
                    redirect = response.redirect,
                    share = response.share,
                    status = response.status,
                    telegram = response.telegram,
                    website = response.website
                )
            )
            navDao.getById(1)!!
        }
    }

//    fetch section

    suspend fun getFetchClasses(): List<FetchClassItem>? {
        return api.fetchClasses()
    }

    suspend fun getFetchYears(classValue: String): List<FetchYearItem>? {
        return api.fetchYears(classValue = classValue)
    }

    suspend fun getFetchExams(classValue: String, year: String): List<FetchExamItem?>? {
        return api.fetchExams(classValue = classValue, year = year)
    }

    suspend fun getFetchSearches(searchQuery: String): List<CourseItemX>? {
        return api.fetchSearches(searchQuery = searchQuery)
    }

    suspend fun getFetchAllBatches(): List<CourseItemX>? {
        return api.fetchAllBatches()
    }

    suspend fun getFetchBatches(
        classValue: String,
        year: String,
        exam: String,
    ): List<CourseItemX>? {
        return api.fetchBatches(
            classValue = classValue,
            year = year,
            exam = exam
        )
    }


//    Promo section

    suspend fun getQuery(query: String): List<SearchItem>? {
        return api.getQuery(query)
    }

    suspend fun getQueryOld(query: String): List<SearchOldItem>? {
        return api.getQueryOld(query)
    }

    suspend fun getAllPromoItems(): List<PromoItem?>? {
        return api.getAllPromoItems()
    }

//    favourite courses database

    fun getAllFavouriteCourses(): Flow<List<FavouriteCourse?>?> {
        return dao.getAllCourses()
    }

    suspend fun checkIfPresent(externalId: String): Boolean {
        return dao.checkIfPresent(externalId)
    }


    suspend fun insertFav(item: FavouriteCourse) {
        dao.updateItem(item)
    }

    suspend fun deleteFav(item: FavouriteCourse) {
        dao.deleteItem(item)
    }


// Pw

    suspend fun getAllCoursesOld(classValue: String): List<CourseItem>? {
        return api.getAllCoursesOld(classValue)

    }

    suspend fun getAllCourses(classValue: String): List<CourseItemX>? {
        return api.getAllCourses(classValue)
    }

    suspend fun getAllCoursesPaginated(
        classValue: String,
        page: Int,
        limit: Int
    ): List<CourseItemX>? {
        return api.getAllCoursesPaginated(classValue = classValue, page = page, limit = limit)
    }

    suspend fun getAllSubjects(courseId: String): BatchDetails {
        return api.getAllSubjects(courseId)
    }


    suspend fun getAllLessonsOld(subjectId: String): List<LessonItem>? {
        return api.getALlLessons(subjectId)
    }

    suspend fun getAllLessons(batchId: String, subjectSlug: String): String {
        return encryptedApi.getLessons(batchId = batchId, subjectSlug = subjectSlug)
    }

    suspend fun getAllVideosOld(slug: String): String {
        return encryptedApi.getAllVideosOld(slug)
    }


    suspend fun getAllVideos(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): String {
        return encryptedApi.getAllVideos(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug
        )
    }


    suspend fun getAllVideosPaginated(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
        page: Int,
        limit: Int,
    ): String {
        return encryptedApi.getAllVideosPaginated(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug,
            page = page,
            limit = limit
        )
    }


    suspend fun getAllNotesOld(slug: String): List<NoteOldItem>? {
        return api.getAllNotes(slug)
    }


    suspend fun getAllNotes(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): String {
        return encryptedApi.getAllNotes(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug
        )
    }


    suspend fun getAllDppOld(slug: String): List<DppOldItem>? {
        return api.getAllDpp(slug)
    }


    suspend fun getAllDpp(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): String {
        return encryptedApi.getAllDpp(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug,
        )
    }

    suspend fun getAllDppSolutionOld(slug: String): List<DppSolutionOldItem>? {
        return api.getAllDppSolution(slug)
    }

    suspend fun getAllDppSolution(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): String {
        return encryptedApi.getAllDppSolution(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug,
        )
    }


    suspend fun insertTask(item: TaskEntity) {
        taskDao.upsert(item)
    }

    suspend fun deleteTask(item: TaskEntity) {
        taskDao.delete(item)
    }

    suspend fun getTask(id: Int): TaskEntity? {
        return taskDao.getById(id)
    }

    suspend fun insertDownloadNumber(item: DownloadNumberEntity) {
        downloadNumberDao.upsert(item)
    }

    suspend fun deleteDownloadNumber(item: DownloadNumberEntity) {
        downloadNumberDao.delete(item)
    }

    suspend fun getDownloadNumberById(id: Int): DownloadNumberEntity? {
        return downloadNumberDao.getByID(id)
    }

    suspend fun getDownloadLinks(): DownloadItem {
        return api.getDownloadLinks()
    }

    suspend fun getStatus(): StatusItem {
        return api.getStatus()
    }

    suspend fun getGenerateKeyById(id: Int): KeyGenerateEntity? {
        return keyGenerateDao.getById(id)
    }

    suspend fun deleteKeyGenerate(item: KeyGenerateEntity) {
        keyGenerateDao.delete(item)
    }

    suspend fun getKeyTask(): KeyGenerateEntity {
        val cachedResponse = keyGenerateDao.getById(1)
        if (cachedResponse != null) {
            Log.d("TAG", "getKeyTask: return cached response")
            return cachedResponse
        } else {
            Log.d("TAG", "getKeyTask: Calling from api")
            val response: KeyItem = api.getKeyTask()
            keyGenerateDao.upsert(
                KeyGenerateEntity(
                    id = 1,
                    task1_url = response.task1_url,
                    task1_final_url = response.task1_final_url,
                    telegram = response.telegram,
                    visible = response.visible,
                    tutorial_url = response.tutorial_url
                )
            )

            return keyGenerateDao.getById(1)!!
        }
    }

    suspend fun insertAlarm(item: AlarmEntity) {
        alarmDao.upsert(item)
    }

    suspend fun getAlarmById(id: Int): AlarmEntity? {
        return alarmDao.getById(id)
    }

    suspend fun deleteAlarmItem(item: AlarmEntity) {
        alarmDao.delete(item)
    }


//    Key

    suspend fun insertKey(item: KeyEntity) {
        keyDao.upsert(item)
    }

    suspend fun deleteKey(item: KeyEntity) {
        keyDao.delete(item)
    }

    suspend fun getKey(id: Int): KeyEntity? {
        return keyDao.getById(id)
    }


//    Watch later

    suspend fun insertWatchLater(item: WatchLaterEntity) {
        watchLaterDao.upsert(item)
    }

    suspend fun deleteWatchLater(item: WatchLaterEntity) {
        watchLaterDao.delete(item)
    }

    suspend fun getWatchLaterById(videoId: String): WatchLaterEntity? {
        return watchLaterDao.getById(videoId)
    }

    fun getAllWatchLater(): Flow<List<WatchLaterEntity>> {
        return watchLaterDao.getAll()
    }

    suspend fun checkIfPresentInWatchLater(videoId: String): Boolean {
        return watchLaterDao.checkIfPresentInWatchLater(videoId)
    }

    fun checkIfPresentInWatchLaterFlow(videoId: String): Flow<Boolean> {
        return watchLaterDao.checkIfPresentInWatchLaterFlow(videoId)
    }

    suspend fun deleteWatchLaterByVideoId(videoId: String) {
        watchLaterDao.deleteByVideoId(videoId)
    }

//    Announcements

    suspend fun getAnnouncements(batchId: String): List<AnnouncementItem>? {
        return api.getAnnouncements(batchId)
    }

    suspend fun getAnnouncementsNew(batchId: String): String {
        return encryptedApi.getAnnouncements(batchId)
    }

    //    Timeline
    suspend fun getTimeline(externalId: String): String {
        return encryptedApi.getTimeline(externalId)
    }

//    Video progress

    suspend fun upsertVideoProgress(item: VideoProgressEntity) {
        videoProgressDao.upsert(item)
    }

    suspend fun getVideoProgressById(videoId: String): VideoProgressEntity? {
        return videoProgressDao.getById(videoId)
    }

//    Video notes

    suspend fun insertVideoNotes(item: VideoNoteEntity) {
        videoNoteDao.insert(item)
    }

    suspend fun deleteVideoNote(id: Int) {
        videoNoteDao.delete(id)
    }

    fun getVideoNoteById(videoId: String): Flow<List<VideoNoteEntity>?> {
        return videoNoteDao.getById(videoId)
    }

    fun getAllVideoNotes(): Flow<List<VideoNoteEntity>?> {
        return videoNoteDao.getAll()
    }


//

    suspend fun getBatchDetails(
        batchId: String,
        batchSlug: String,
        classValue: String,
    ): String {
        return encryptedApi.getBatchDetails(
            batchId = batchId, batchSlug = batchSlug, classValue = classValue
        )
    }

}