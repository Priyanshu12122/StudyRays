package com.xerox.studyrays.data

import android.content.Context
import android.util.Log
import com.xerox.studyrays.alarmManager.AlarmScheduler
import com.xerox.studyrays.cacheDb.akCache.NotesDb.AkNoteDao
import com.xerox.studyrays.cacheDb.akCache.NotesDb.AkNotesEntity
import com.xerox.studyrays.cacheDb.akCache.indexDb.IndexDao
import com.xerox.studyrays.cacheDb.akCache.indexDb.IndexEntity
import com.xerox.studyrays.cacheDb.akCache.lessonDb.AkLessonDao
import com.xerox.studyrays.cacheDb.akCache.lessonDb.AkLessonEntity
import com.xerox.studyrays.cacheDb.akCache.subjectsDb.AkSubjectDao
import com.xerox.studyrays.cacheDb.akCache.subjectsDb.AkSubjectEntity
import com.xerox.studyrays.cacheDb.akCache.videosDb.AkVideoDao
import com.xerox.studyrays.cacheDb.akCache.videosDb.AkVideoEntity
import com.xerox.studyrays.cacheDb.keyGeneratorCache.KeyGenerateDao
import com.xerox.studyrays.cacheDb.keyGeneratorCache.KeyGenerateEntity
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaChaptersDb.KhazanaChaptersDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaChaptersDb.KhazanaChaptersEntity
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDb.KhazanaDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDb.KhazanaEntity
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppDb.KhazanaDppDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppDb.KhazanaDppEntity
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppLecturesDb.KhazanaDppLecturesDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppLecturesDb.KhazanaDppLecturesEntity
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaLecturesDb.KhazanaLecturesDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaLecturesDb.KhazanaLecturesEntity
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaNotesDb.KhazanaNotesDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaNotesDb.KhazanaNotesEntity
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaSubjectsDb.KhazanaSubjectDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaSubjectsDb.KhazanaSubjectEntity
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaTeachersDb.KhazanaTeachersDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaTeachersDb.KhazanaTeachersEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavDao
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.priceDb.PriceDao
import com.xerox.studyrays.cacheDb.mainScreenCache.priceDb.PriceEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.promoDb.PromoDao
import com.xerox.studyrays.cacheDb.mainScreenCache.promoDb.PromoEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchDao
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchEntity
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.BatchDetailsDaoo
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.BatchDetailsEntityy
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.DppNotesDao
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.DppNotesEntity
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.DppVideoDao
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.DppVideos
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.LessonDao
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.LessonsEntityy
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.NotesDao
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.NotesEntityy
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.TimelineDao
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.TimelineEntity
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.VideoDao
import com.xerox.studyrays.cacheDb.pwCache.batchDetailsDb.VideoEntityy
import com.xerox.studyrays.cacheDb.pwCache.courseDb.PwCourseDao
import com.xerox.studyrays.cacheDb.pwCache.courseDb.PwCourseEntity
import com.xerox.studyrays.cacheDb.pwCache.dppDb.PwDppDao
import com.xerox.studyrays.cacheDb.pwCache.dppDb.PwDppEntity
import com.xerox.studyrays.cacheDb.pwCache.lessonDb.PwLessonDao
import com.xerox.studyrays.cacheDb.pwCache.lessonDb.PwLessonEntity
import com.xerox.studyrays.cacheDb.pwCache.notesDb.PwNotesDao
import com.xerox.studyrays.cacheDb.pwCache.notesDb.PwNotesEntity
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.BatchDetailsDao
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.BatchDetailsEntity
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.ImageIdEntity
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.SubjectEntity
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.TeacherIdEntity
import com.xerox.studyrays.cacheDb.pwCache.subjectsAndTeachersCache.relations.BatchDetailsWithSubjects
import com.xerox.studyrays.cacheDb.pwCache.videosDb.PwVideoDao
import com.xerox.studyrays.cacheDb.pwCache.videosDb.PwVideoEntity
import com.xerox.studyrays.db.alarmDb.AlarmDao
import com.xerox.studyrays.db.alarmDb.AlarmEntity
import com.xerox.studyrays.db.downloadsDb.DownloadNumberDao
import com.xerox.studyrays.db.downloadsDb.DownloadNumberEntity
import com.xerox.studyrays.db.exampleDb.Example
import com.xerox.studyrays.db.exampleDb.ExampleDao
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourseDao
import com.xerox.studyrays.db.keyDb.KeyDao
import com.xerox.studyrays.db.keyDb.KeyEntity
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFav
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFavDao
import com.xerox.studyrays.db.taskDb.TaskDao
import com.xerox.studyrays.db.taskDb.TaskEntity
import com.xerox.studyrays.db.videoNotesDb.VideoNoteDao
import com.xerox.studyrays.db.videoNotesDb.VideoNoteEntity
import com.xerox.studyrays.db.videoProgress.VideoProgressDao
import com.xerox.studyrays.db.videoProgress.VideoProgressEntity
import com.xerox.studyrays.db.watchLaterDb.WatchLaterDao
import com.xerox.studyrays.db.watchLaterDb.WatchLaterEntity
import com.xerox.studyrays.model.akModel.aKVideoU.AkUrl
import com.xerox.studyrays.model.downloads.DownloadItem
import com.xerox.studyrays.model.downloads.KeyItem
import com.xerox.studyrays.model.pwModel.NoteItem
import com.xerox.studyrays.model.pwModel.SearchItem
import com.xerox.studyrays.model.pwModel.SearchOldItem
import com.xerox.studyrays.model.pwModel.alertItem.AlertItem
import com.xerox.studyrays.model.pwModel.announcementsItem.AnnouncementItem
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetails
import com.xerox.studyrays.model.pwModel.batchDetails.Subject
import com.xerox.studyrays.model.pwModel.batchDetails.TeacherId
import com.xerox.studyrays.model.pwModel.old.DppSolutionOldItem
import com.xerox.studyrays.model.pwModel.old.NoteOldItem
import com.xerox.studyrays.model.pwModel.statusItem.StatusItem
import com.xerox.studyrays.network.ApiService
import com.xerox.studyrays.network.EncryptedApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val api: ApiService,
    private val encryptedApi: EncryptedApiService,
    private val dao: FavouriteCourseDao,
    private val khazanaDao: KhazanaFavDao,
    private val priceDao: PriceDao,
    private val exDao: ExampleDao,
    private val promoDao: PromoDao,
    private val indexDao: IndexDao,
    private val khazanaCacheDao: KhazanaDao,
    private val khazanaSubjectDao: KhazanaSubjectDao,
    private val khazanaTeachersDao: KhazanaTeachersDao,
    private val khazanaChaptersDao: KhazanaChaptersDao,
    private val khazanaLecturesDao: KhazanaLecturesDao,
    private val khazanaNotesDao: KhazanaNotesDao,
    private val khazanaDppDao: KhazanaDppDao,
    private val khazanaDppLecturesDao: KhazanaDppLecturesDao,
    private val akSubjectDao: AkSubjectDao,
    private val akLessonDao: AkLessonDao,
    private val akVideoDao: AkVideoDao,
    private val akNoteDao: AkNoteDao,
    private val pwCourseDao: PwCourseDao,
    private val pwLessonDao: PwLessonDao,
    private val pwVideoDao: PwVideoDao,
    private val pwNotesDao: PwNotesDao,
    private val pwDppDao: PwDppDao,
    private val navDao: NavDao,
    private val searchDao: SearchDao,
    private val batchDetailsDao: BatchDetailsDao,
    private val taskDao: TaskDao,
    private val downloadNumberDao: DownloadNumberDao,
    private val alarmDao: AlarmDao,
    private val keyDao: KeyDao,
    private val keyGenerateDao: KeyGenerateDao,
    private val watchLaterDao: WatchLaterDao,
    private val videoProgressDao: VideoProgressDao,
    private val videoNoteDao: VideoNoteDao,
    private val batchDetailsDaoo: BatchDetailsDaoo,
    private val lessonDao: LessonDao,
    private val videoDao: VideoDao,
    private val notesDao: NotesDao,
    private val dppVideoDao: DppVideoDao,
    private val dppNotesDao: DppNotesDao,
    private val timelineDao: TimelineDao,
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

    //
    suspend fun insertExample(example: Example) {
        exDao.insertExample(example)
    }

    suspend fun getAllExamples(): List<Example> {
        return exDao.getAllExamples()
    }

//    Khazana

    suspend fun cacheAllKhazana() {
        cacheKhazanaChapters()
        getKhazanaFromApi()
        cacheKhazanaDpp()
        cacheKhazanaDppLectures()
        cacheKhazanaNotes()
        cacheKhazanaSubjects()
        cacheKhazanaTeachers()
        cacheKhazanaLectures()
    }

    private suspend fun cacheKhazanaDpp() {
        val response = khazanaDppDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getKhazanaDppFromApi(
                    subjectId = it.subjectId,
                    chapterId = it.chapterId,
                    topicId = it.topicId,
                    topicName = it.topicName
                )
            }
        }
    }

    private suspend fun cacheKhazanaChapters() {
        val response = khazanaChaptersDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getKhazanaChaptersFromApi(subjectId = it.subjectId, chapterId = it.chapterId)
            }
        }
    }

    private suspend fun cacheKhazanaDppLectures() {
        val response = khazanaDppLecturesDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getKhazanaSolutionFromApi(
                    subjectId = it.subjectId,
                    chapterId = it.chapterId,
                    topicId = it.topicId,
                    topicName = it.topicName
                )

            }
        }
    }

    private suspend fun cacheKhazanaLectures() {
        val response = khazanaLecturesDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getKhazanaVideosFromApi(
                    subjectId = it.subjectId,
                    chapterId = it.chapterId,
                    topicId = it.topicId,
                    topicName = it.topicName
                )

            }
        }
    }

    private suspend fun cacheKhazanaNotes() {
        val response = khazanaNotesDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getKhazanaNotesFromApi(
                    subjectId = it.subjectId,
                    chapterId = it.chapterId,
                    topicId = it.topicId,
                    topicName = it.topicName
                )

            }
        }
    }

    private suspend fun cacheKhazanaSubjects() {
        val response = khazanaSubjectDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getKhazanaSubjectsFromApi(
                    slug = it.id
                )

            }
        }
    }

    private suspend fun cacheKhazanaTeachers() {
        val response = khazanaTeachersDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getKhazanaTeachersFromApi(
                    id = it.id
                )
            }
        }
    }

    //    Khazana fav courses
    suspend fun checkIfPresentInKhazanaDb(externalId: String): Boolean {
        return khazanaDao.checkIfPresent(externalId)
    }


    suspend fun insertKhazanaFav(item: KhazanaFav) {
        khazanaDao.updateItem(item)
    }

    suspend fun deleteKhazanaFav(item: KhazanaFav) {
        khazanaDao.deleteItem(item)
    }

    fun getAllKhazanaCourses(): Flow<List<KhazanaFav?>?> {
        return khazanaDao.getAllCourses()
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

//    Khazana lectures

    suspend fun getKhazanaVideos(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): KhazanaLecturesEntity {
        val cachedResponse = khazanaLecturesDao.getKhazanaLectureString(topicName = topicName)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val apiResponse = encryptedApi.getKhazanaVideos(
                subjectId = subjectId,
                chapterId = chapterId,
                topicId = topicId
            )
            khazanaLecturesDao.insertKhazanaLecture(
                KhazanaLecturesEntity(
                    subjectId = subjectId,
                    chapterId = chapterId,
                    topicId = topicId,
                    topicName = topicName,
                    response = apiResponse
                )
            )
            khazanaLecturesDao.getKhazanaLectureString(topicName)!!
        }
    }

    suspend fun getKhazanaVideosFromApi(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): KhazanaLecturesEntity {
        val apiResponse = encryptedApi.getKhazanaVideos(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId
        )
        khazanaLecturesDao.insertKhazanaLecture(
            KhazanaLecturesEntity(
                subjectId = subjectId,
                chapterId = chapterId,
                topicId = topicId,
                topicName = topicName,
                response = apiResponse
            )
        )
        return khazanaLecturesDao.getKhazanaLectureString(topicName)!!
    }

    suspend fun getKhazanaNotes(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): KhazanaNotesEntity {
        val cachedResponse = khazanaNotesDao.getById(topicName)
        if (cachedResponse != null) {
            return cachedResponse
        } else {
            val response = encryptedApi.getKhazanaNotes(
                subjectId = subjectId,
                chapterId = chapterId,
                topicId = topicId
            )
            khazanaNotesDao.insert(
                KhazanaNotesEntity(
                    subjectId = subjectId,
                    chapterId = chapterId,
                    topicId = topicId,
                    topicName = topicName,
                    response = response
                )
            )

            return khazanaNotesDao.getById(topicName)!!
        }
    }

    suspend fun getKhazanaNotesFromApi(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): KhazanaNotesEntity {
        val response = encryptedApi.getKhazanaNotes(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId
        )
        khazanaNotesDao.insert(
            KhazanaNotesEntity(
                subjectId = subjectId,
                chapterId = chapterId,
                topicId = topicId,
                topicName = topicName,
                response = response
            )
        )

        return khazanaNotesDao.getById(topicName)!!
    }

    suspend fun getKhazanaDpp(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): KhazanaDppEntity {
        val cachedResponse = khazanaDppDao.getById(topicName)
        if (cachedResponse != null) {
            return cachedResponse
        } else {
            val response = encryptedApi.getKhazanaDpp(
                subjectId = subjectId,
                chapterId = chapterId,
                topicId = topicId
            )

            khazanaDppDao.insert(
                KhazanaDppEntity(
                    subjectId = subjectId,
                    chapterId = chapterId,
                    topicId = topicId,
                    topicName = topicName,
                    response = response
                )
            )
            return khazanaDppDao.getById(topicName)!!
        }
    }

    suspend fun getKhazanaDppFromApi(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): KhazanaDppEntity {
        val response = encryptedApi.getKhazanaDpp(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId
        )

        khazanaDppDao.insert(
            KhazanaDppEntity(
                subjectId = subjectId,
                chapterId = chapterId,
                topicId = topicId,
                topicName = topicName,
                response = response
            )
        )
        return khazanaDppDao.getById(topicName)!!
    }

    suspend fun getKhazanaSolution(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): KhazanaDppLecturesEntity {


        val cachedResponse = khazanaDppLecturesDao.getById(topicName)
        if (cachedResponse != null) {
            return cachedResponse
        } else {
            val response = encryptedApi.getKhazanaSolution(
                subjectId = subjectId,
                chapterId = chapterId,
                topicId = topicId
            )
            khazanaDppLecturesDao.insert(
                KhazanaDppLecturesEntity(
                    subjectId = subjectId,
                    chapterId = chapterId,
                    topicId = topicId,
                    response = response,
                    topicName = topicName,
                )
            )
            return khazanaDppLecturesDao.getById(topicName)!!
        }

    }

    suspend fun getKhazanaSolutionFromApi(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
    ): KhazanaDppLecturesEntity {
        val response = encryptedApi.getKhazanaSolution(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId
        )
        khazanaDppLecturesDao.insert(
            KhazanaDppLecturesEntity(
                subjectId = subjectId,
                chapterId = chapterId,
                topicId = topicId,
                topicName = topicName,
                response = response
            )
        )
        return khazanaDppLecturesDao.getById(topicName)!!

    }


//    Khazana chapters

    suspend fun getKhazanaChapters(subjectId: String, chapterId: String): KhazanaChaptersEntity {
        val cachedResponse = khazanaChaptersDao.getKhazanaChapterString(chapterId)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val apiResponse =
                encryptedApi.getKhazanaChapters(subjectId = subjectId, chapterId = chapterId)
            khazanaChaptersDao.insertKhazanaChapter(
                KhazanaChaptersEntity(
                    chapterId = chapterId,
                    subjectId = subjectId,
                    response = apiResponse
                )
            )
            khazanaChaptersDao.getKhazanaChapterString(chapterId = chapterId)!!
        }
    }

    suspend fun getKhazanaChaptersFromApi(
        subjectId: String,
        chapterId: String,
    ): KhazanaChaptersEntity {
        val apiResponse =
            encryptedApi.getKhazanaChapters(subjectId = subjectId, chapterId = chapterId)
        khazanaChaptersDao.insertKhazanaChapter(
            KhazanaChaptersEntity(
                chapterId = chapterId,
                subjectId = subjectId,
                response = apiResponse
            )
        )
        return khazanaChaptersDao.getKhazanaChapterString(chapterId = chapterId)!!
    }

//    Khazana teachers details

    suspend fun getKhazanaTeachers(id: String): KhazanaTeachersEntity {
        val cachedResponse = khazanaTeachersDao.getKhazanaTeacherString(id)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val apiResponse = encryptedApi.getKhazanaTeachers(id)
            khazanaTeachersDao.insertKhazanaTeacher(
                KhazanaTeachersEntity(
                    id = id,
                    response = apiResponse
                )
            )
            khazanaTeachersDao.getKhazanaTeacherString(id)!!
        }
    }

    suspend fun getKhazanaTeachersFromApi(id: String): KhazanaTeachersEntity {
        val apiResponse = encryptedApi.getKhazanaTeachers(id)
        khazanaTeachersDao.insertKhazanaTeacher(
            KhazanaTeachersEntity(
                id = id,
                response = apiResponse
            )
        )
        return khazanaTeachersDao.getKhazanaTeacherString(id)!!
    }

//    Total fees

    suspend fun fetchAndCacheTotalFee() {
        try {
            val response = api.getTotalFee()
            val priceEntity = PriceEntity(1, response.total_amount, System.currentTimeMillis())
            priceDao.insertResponse(priceEntity)
        } catch (e: Exception) {
            Log.e("WorkStatus", "Error in fetchAndCacheTotalFee", e)
            throw e
        }
    }

    suspend fun getTotalFee(): PriceEntity {
        val cachedResponse = priceDao.getResponse(1)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = api.getTotalFee()
            priceDao.insertResponse(
                PriceEntity(
                    1,
                    response.total_amount,
                    System.currentTimeMillis()
                )
            )
            priceDao.getResponse(1)!!
        }
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


//    Khazana

    suspend fun getKhazana(): List<KhazanaEntity> {
        val cachedResponse = khazanaCacheDao.getAllKhazanaFromDB()
        if (!cachedResponse.isNullOrEmpty()) {
            return cachedResponse
        } else {
            val apiResponse = api.getKhazana()
            apiResponse.forEach { item ->
                khazanaCacheDao.insertKhazana(
                    KhazanaEntity(
                        id = item.id,
                        name = item.name,
                        showorder = item.showorder,
                        slug = item.slug
                    )
                )
            }
            return khazanaCacheDao.getAllKhazanaFromDB()!!

        }

    }

    suspend fun getKhazanaFromApi(): List<KhazanaEntity> {
        val apiResponse = api.getKhazana()
        apiResponse.forEach { item ->
            khazanaCacheDao.insertKhazana(
                KhazanaEntity(
                    id = item.id,
                    name = item.name,
                    showorder = item.showorder,
                    slug = item.slug
                )
            )
        }
        return khazanaCacheDao.getAllKhazanaFromDB()!!
    }


    suspend fun getKhazanaSubjects(slug: String): KhazanaSubjectEntity {
        val cachedResponse = khazanaSubjectDao.getKhazanaSubjectString(slug)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val apiResponse = encryptedApi.getKhazanaSubjects(slug)
            khazanaSubjectDao.insertKhazanaSubject(
                item = KhazanaSubjectEntity(
                    id = slug,
                    response = apiResponse
                )
            )
            khazanaSubjectDao.getKhazanaSubjectString(slug)!!
        }
    }

    suspend fun getKhazanaSubjectsFromApi(slug: String): KhazanaSubjectEntity {
        val apiResponse = encryptedApi.getKhazanaSubjects(slug)
        khazanaSubjectDao.insertKhazanaSubject(
            item = KhazanaSubjectEntity(
                id = slug,
                response = apiResponse
            )
        )
        return khazanaSubjectDao.getKhazanaSubjectString(slug)!!
    }

//    Promo section

    suspend fun getQuery(query: String): List<SearchItem>? {
        return api.getQuery(query)
    }

    suspend fun getQueryOld(query: String): List<SearchOldItem>? {
        return api.getQueryOld(query)
    }




    suspend fun fetchAndCachePromoItems() {
        try {
            val response = api.getAllPromoItems()
            response?.forEach { promoItem ->
                promoItem?.let { item ->
                    promoDao.insertPromoEntity(
                        PromoEntity(
                            description = item.description,
                            id = item.id,
                            imageUrl = item.image_url,
                            redirectionLink = item.redirection_link,
                            title = item.title
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("WorkStatus", "Error in fetchAndCacheTotalFee", e)
            throw e
        }
    }

    suspend fun getAllPromoItems(): List<PromoEntity>? {
        val response: List<PromoEntity>? = promoDao.getAllItems()
        if (!response.isNullOrEmpty()) {
            return response

        }
        val apiResponse = api.getAllPromoItems()
        apiResponse?.forEach { promoItem ->
            promoItem?.let { item ->
                promoDao.insertPromoEntity(
                    PromoEntity(
                        description = item.description,
                        id = item.id,
                        imageUrl = item.image_url,
                        redirectionLink = item.redirection_link,
                        title = item.title
                    )
                )
            }
        }
        return promoDao.getAllItems()

    }

//    favourite courses database

    fun getAllCourses(): Flow<List<FavouriteCourse?>?> {
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

    suspend fun getAndCacheAllPw() {
        cacheAllPwCourses()
        cacheAllPwDpp()
        cacheAllPwLesson()
        cacheAllPwNotes()
        cacheAllPwSubjectsAndTeachers()
        cacheAllPwVideo()
    }

    private suspend fun cacheAllPwCourses() {
        val response = pwCourseDao.getAll()
        if (!response.isNullOrEmpty()) {
            val distinctResponses = response.distinctBy { it.classValue }

            distinctResponses.forEach {
                getAllCoursesFromApi(it.classValue, it.isOld)
            }

        }
    }

    private suspend fun cacheAllPwDpp() {
        val response = dppNotesDao.getAll()
        if (!response.isNullOrEmpty()) {
            val distinctResponse = response.distinctBy { it.topicSlug }
            distinctResponse.forEach {
                getAllDppFromApi(
                    batchId = it.batchId,
                    subjectSlug = it.subjectSlug,
                    topicSlug = it.topicSlug
                )
            }

        }
    }

    private suspend fun cacheAllPwNotes() {
        val response = notesDao.getAll()
        if (!response.isNullOrEmpty()) {
            val distinctResponse = response.distinctBy { it.topicSlug }
            distinctResponse.forEach {
                getAllNotesFromApi(
                    batchId = it.batchId,
                    subjectSlug = it.subjectSlug,
                    topicSlug = it.topicSlug
                )
            }

        }
    }

    private suspend fun cacheAllPwSubjectsAndTeachers() {
        val response: List<BatchDetailsEntityy>? = batchDetailsDaoo.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getBatchDetailsFromApi(
                    batchId = it.batchId,
                    classValue = it.classValue,
                    batchSlug = it.slug
                )
            }
        }
    }

    private suspend fun cacheAllPwVideo() {
        val response = videoDao.getAll()
        if (!response.isNullOrEmpty()) {
            val distinctResponse = response.distinctBy { it.topicSlug }
            distinctResponse.forEach {
                getAllVideosFromApi(
                    batchId = it.batchId,
                    subjectSlug = it.subjectSlug,
                    topicSlug = it.topicSlug
                )
            }
        }
    }

    private suspend fun cacheAllPwLesson() {
        val response = lessonDao.getAll()
        response?.forEach {
            getAllLessonsFromApi(batchId = it.batchId, subjectSlug = it.slug)
        }

    }


    suspend fun getAllCourses(classValue: String, isOld: Boolean): List<PwCourseEntity>? {
        val cachedResponse = pwCourseDao.getById(classValue, isOld)
        if (!cachedResponse.isNullOrEmpty()) {
            return cachedResponse
        } else {
            if (isOld) {
                val response = api.getAllCoursesOld(classValue)

                response?.forEach { item ->
                    pwCourseDao.insert(
                        PwCourseEntity(
                            id = item.id,
                            classValue = item.`class`,
                            externalId = item.external_id,
                            name = item.name,
                            language = item.language,
                            imageUrl = item.previewImage_baseUrl.plus(item.previewImage_key),
                            byName = item.byName,
                            queryClassValue = classValue,
                            slug = item.slug,
                            isOld = isOld
                        )
                    )
                }

            } else {

                val response = api.getAllCourses(classValue)
                response?.forEach { item ->
                    pwCourseDao.insert(
                        PwCourseEntity(
                            id = item.id,
                            classValue = item.`class`,
                            externalId = item.batch_id,
                            name = item.batch_name,
                            language = item.language,
                            imageUrl = item.previewImage,
                            byName = item.byName,
                            queryClassValue = classValue,
                            slug = item.slug,
                            isOld = isOld
                        )
                    )
                }
            }

            return pwCourseDao.getById(classValue, isOld)
        }
    }

    suspend fun getAllCoursesFromApi(classValue: String, isOld: Boolean): List<PwCourseEntity>? {
        if (isOld) {
            val response = api.getAllCoursesOld(classValue)

            response?.forEach { item ->
                pwCourseDao.insert(
                    PwCourseEntity(
                        id = item.id,
                        classValue = item.`class`,
                        externalId = item.external_id,
                        name = item.name,
                        language = item.language,
                        imageUrl = item.previewImage_baseUrl.plus(item.previewImage_key),
                        byName = item.byName,
                        queryClassValue = classValue,
                        slug = item.slug,
                        isOld = isOld
                    )
                )
            }

        } else {

            val response = api.getAllCourses(classValue)
            response?.forEach { item ->
                pwCourseDao.insert(
                    PwCourseEntity(
                        id = item.id,
                        classValue = item.`class`,
                        externalId = item.batch_id,
                        name = item.batch_name,
                        language = item.language,
                        imageUrl = item.previewImage,
                        byName = item.byName,
                        queryClassValue = classValue,
                        slug = item.slug,
                        isOld = isOld
                    )
                )
            }
        }

        return pwCourseDao.getById(classValue, isOld)
    }

    private suspend fun insertBatchDetailsIntoDb(batchDetails: BatchDetails, courseId: String) {
        // Transform BatchDetailsX to BatchDetailsEntity
        val batchDetailsEntity = BatchDetailsEntity(
            id = batchDetails.batch_details.id,
            byName = batchDetails.batch_details.byName,
            externalId = batchDetails.batch_details.external_id,
            language = batchDetails.batch_details.language,
            name = batchDetails.batch_details.name,
            baseUrl = batchDetails.batch_details.previewImage?.baseUrl,
            key = batchDetails.batch_details.previewImage?.key,
            courseId = courseId
        )

        // Transform Subjects to SubjectEntities
        val subjectEntities = batchDetails.subjects.map { subject: Subject ->
            SubjectEntity(
                id = subject._id,
                batchDetailsId = batchDetails.batch_details.id,
                imageId = if (subject.imageId != null) subject.imageId._id else null,
                slug = subject.slug,
                subject = subject.subject,
                subjectId = subject.subjectId,
                tagCount = subject.tagCount
            )
        }


        // Transform TeacherIds to TeacherIdEntities
        val teacherIdEntities = batchDetails.subjects.flatMap { subject ->
            subject.teacherIds.map { teacher ->
                TeacherIdEntity(
                    id = teacher._id,
                    subjectId = subject._id,
                    experience = teacher.experience,
                    featuredLine = teacher.featuredLine,
                    firstName = teacher.firstName,
                    imageId = teacher.imageId?._id,
                    lastName = teacher.lastName,
                    qualification = teacher.qualification
                )
            }
        }

        // Transform ImageIds to ImageIdEntities
        val imageIdEntities: List<ImageIdEntity> = batchDetails.subjects.flatMap { subject ->
            if (subject.imageId != null) {
                listOf(
                    ImageIdEntity(
                        id = subject.imageId._id,
                        baseUrl = subject.imageId.baseUrl,
                        key = subject.imageId.key,
                        name = subject.imageId.name
                    )
                ) + subject.teacherIds.map { teacher: TeacherId ->
                    teacher.imageId?.let {
                        ImageIdEntity(
                            id = it._id,
                            baseUrl = it.baseUrl,
                            key = it.key,
                            name = it.name
                        )
                    }
                }

            } else {
                emptyList()
            }
        }.filterNotNull()

        // Insert data into the database
        batchDetailsDao.insertBatchDetails(batchDetailsEntity)
        batchDetailsDao.insertSubjects(subjectEntities)
        batchDetailsDao.insertTeacherIds(teacherIdEntities)
        batchDetailsDao.insertImageIds(imageIdEntities)
    }

    suspend fun getAllSubjects(courseId: String): BatchDetailsWithSubjects {
        val cachedResponse = batchDetailsDao.getBatchDetailsWithSubjects(courseId)
        if (cachedResponse != null) {
            return cachedResponse
        } else {
            val batchDetails: BatchDetails = api.getAllSubjects(courseId)
            insertBatchDetailsIntoDb(batchDetails, courseId)
            return batchDetailsDao.getBatchDetailsWithSubjects(courseId)!!
        }
    }

    suspend fun getAllSubjectsFromApi(courseId: String): BatchDetailsWithSubjects {
        val batchDetails: BatchDetails = api.getAllSubjects(courseId)
        insertBatchDetailsIntoDb(batchDetails, courseId)
        return batchDetailsDao.getBatchDetailsWithSubjects(courseId)!!
    }


    suspend fun getAllLessonsOld(subjectId: String): List<PwLessonEntity>? {
        val cachedResponse = pwLessonDao.getById(subjectId)
        if (!cachedResponse.isNullOrEmpty()) {
            return cachedResponse
        } else {
            val response = api.getALlLessons(subjectId)
            response?.forEach {
                pwLessonDao.insert(
                    PwLessonEntity(
                        subjectId = subjectId,
                        name = it.name,
                        notes = it.notes,
                        slug = it.slug,
                        videos = it.videos,
                        exercises = it.exercises,
                        id = it.id
                    )
                )
            }
            return pwLessonDao.getById(subjectId)!!
        }
    }

    suspend fun getAllLessonsOldFromApi(subjectId: String): List<PwLessonEntity>? {
        val response = api.getALlLessons(subjectId)
        response?.forEach {
            pwLessonDao.insert(
                PwLessonEntity(
                    subjectId = subjectId,
                    name = it.name,
                    notes = it.notes,
                    slug = it.slug,
                    videos = it.videos,
                    exercises = it.exercises,
                    id = it.id
                )
            )
        }
        return pwLessonDao.getById(subjectId)!!
    }

    suspend fun getAllLessons(batchId: String, subjectSlug: String): LessonsEntityy {
        val cachedResponse = lessonDao.getById(subjectSlug)
        if (cachedResponse != null) {
            return cachedResponse
        } else {
            val response = encryptedApi.getLessons(batchId = batchId, subjectSlug = subjectSlug)
            lessonDao.insert(
                LessonsEntityy(
                    response = response,
                    slug = subjectSlug,
                    batchId = batchId,
                )
            )
            return lessonDao.getById(subjectSlug)!!
        }
    }

    suspend fun getAllLessonsFromApi(batchId: String, subjectSlug: String): LessonsEntityy {
        val response = encryptedApi.getLessons(batchId = batchId, subjectSlug = subjectSlug)
        lessonDao.insert(
            LessonsEntityy(
                response = response,
                slug = subjectSlug,
                batchId = batchId
            )
        )
        return lessonDao.getById(subjectSlug)!!
    }

    suspend fun getAllVideosOld(slug: String): PwVideoEntity {
        val cachedResponse = pwVideoDao.getById(slug)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = encryptedApi.getAllVideosOld(slug)
            pwVideoDao.insert(PwVideoEntity(slug = slug, response = response))
            pwVideoDao.getById(slug)!!
        }
    }

    suspend fun getAllVideosFromApiOld(slug: String): PwVideoEntity {
        val response = encryptedApi.getAllVideosOld(slug)
        pwVideoDao.insert(PwVideoEntity(slug = slug, response = response))
        return pwVideoDao.getById(slug)!!
    }

    suspend fun getAllVideos(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): VideoEntityy {
        val cachedResponse = videoDao.getById(topicSlug)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = encryptedApi.getAllVideos(
                batchId = batchId,
                subjectSlug = subjectSlug,
                topicSlug = topicSlug
            )
            videoDao.insert(
                VideoEntityy(
                    response = response,
                    topicSlug = topicSlug,
                    batchId = batchId,
                    subjectSlug = subjectSlug
                )
            )
            videoDao.getById(topicSlug)!!
        }
    }

    suspend fun getAllVideosFromApi(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): VideoEntityy {
        val response = encryptedApi.getAllVideos(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug
        )
        videoDao.insert(
            VideoEntityy(
                response = response,
                topicSlug = topicSlug,
                batchId = batchId,
                subjectSlug = subjectSlug
            )
        )
        return videoDao.getById(topicSlug)!!
    }


    suspend fun getAllNotesOld(slug: String): List<PwNotesEntity>? {
        val cacheResponse = pwNotesDao.getById(slug)
        return if (!cacheResponse.isNullOrEmpty()){
            cacheResponse
        } else {
            val response: List<NoteOldItem>? = api.getAllNotes(slug)
            response?.forEach {
                pwNotesDao.insert(PwNotesEntity(
                    slug = slug,
                    topic = it.topic,
                    baseUrl = it.attachment_base_url,
                    attachmentKey = it.attachment_key,
                    id = it.id
                ))
            }
            pwNotesDao.getById(slug)
        }
    }

    suspend fun getAllNotesFromApiOld(slug: String): List<PwNotesEntity>? {
        val response = api.getAllNotes(slug)
        response?.forEach {
            pwNotesDao.insert(PwNotesEntity(
                slug = slug,
                topic = it.topic,
                baseUrl = it.attachment_base_url,
                attachmentKey = it.attachment_key,
                id = it.id
            ))
        }
        return pwNotesDao.getById(slug)
    }


    suspend fun getAllNotes(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): NotesEntityy {
        val cacheResponse = notesDao.getById(topicSlug = topicSlug)
        return if (cacheResponse != null) {
            cacheResponse
        } else {
            val response = encryptedApi.getAllNotes(
                batchId = batchId,
                subjectSlug = subjectSlug,
                topicSlug = topicSlug
            )
            notesDao.insert(
                NotesEntityy(
                    batchId = batchId,
                    subjectSlug = subjectSlug,
                    topicSlug = topicSlug,
                    response = response
                )
            )
            notesDao.getById(topicSlug)!!
        }
    }

    suspend fun getAllNotesFromApi(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): NotesEntityy {
        val response = encryptedApi.getAllNotes(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug
        )
        notesDao.insert(
            NotesEntityy(
                batchId = batchId,
                subjectSlug = subjectSlug,
                topicSlug = topicSlug,
                response = response
            )
        )
        return notesDao.getById(topicSlug)!!
    }


    suspend fun getAllDppOld(slug: String): List<PwDppEntity>? {
        val cachedResponse = pwDppDao.getById(slug)
        if (!cachedResponse.isNullOrEmpty()){
            return cachedResponse
        } else {
            val response = api.getAllDpp(slug)
            response?.forEach {
                pwDppDao.insert(
                    PwDppEntity(
                        slug = slug,
                        topic = it.topic,
                        baseUrl = it.attachment_base_url,
                        attachmentKey = it.attachment_key,
                        id = it.id
                    )
                )
            }
            return pwDppDao.getById(slug)
        }
    }

    suspend fun getAllDppFromApiOld(slug: String): List<PwDppEntity>? {
        val response = api.getAllDpp(slug)
        response?.forEach {
            pwDppDao.insert(
                PwDppEntity(
                    slug = slug,
                    topic = it.topic,
                    baseUrl = it.attachment_base_url,
                    attachmentKey = it.attachment_key,
                    id = it.id
                )
            )
        }
        return pwDppDao.getById(slug)
    }


    suspend fun getAllDpp(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): DppNotesEntity {
        val cachedResponse = dppNotesDao.getById(topicSlug)
        if (cachedResponse != null) {
            return cachedResponse
        } else {
            val response = encryptedApi.getAllDpp(
                batchId = batchId,
                subjectSlug = subjectSlug,
                topicSlug = topicSlug,
            )
            dppNotesDao.insert(
                DppNotesEntity(
                    batchId = batchId,
                    subjectSlug = subjectSlug,
                    topicSlug = topicSlug,
                    response = response
                )
            )
            return dppNotesDao.getById(topicSlug)!!
        }
    }

    suspend fun getAllDppFromApi(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): DppNotesEntity {
        val response = encryptedApi.getAllDpp(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug,
        )
        dppNotesDao.insert(
            DppNotesEntity(
                batchId = batchId,
                subjectSlug = subjectSlug,
                topicSlug = topicSlug,
                response = response
            )
        )
        return dppNotesDao.getById(topicSlug)!!
    }

    suspend fun getAllDppSolutionOld(slug: String): List<DppSolutionOldItem>? {
        return api.getAllDppSolution(slug)
    }

    suspend fun getAllDppSolution(
        batchId: String,
        subjectSlug: String,
        topicSlug: String,
    ): DppVideos {
        val response = encryptedApi.getAllDppSolution(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug,
        )

        dppVideoDao.insert(
            DppVideos(
                batchId = batchId,
                subjectSlug = subjectSlug,
                topicSlug = topicSlug,
                response = response
            )
        )
        return dppVideoDao.getById(topicSlug)!!
    }


//Ak

    suspend fun getAndCacheAllAk() {
        getIndexFromApi()
        getAndCacheAllAkSubject()
        getAndCacheAllAkLessons()
        getAndCacheAllAkNotes()
        getAndCacheAllAkVideos()

    }

    private suspend fun getAndCacheAllAkSubject() {
        val response = akSubjectDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getAkSubjectsFromApi(it.id)
            }
        }
    }

    private suspend fun getAndCacheAllAkLessons() {
        val response = akLessonDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getAkLessonsFromApi(sid = it.sid, bid = it.bid)
            }
        }
    }

    private suspend fun getAndCacheAllAkNotes() {
        val response = akNoteDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getAkNotesFromApi(sid = it.sid, bid = it.bid, tid = it.tid)
            }
        }
    }

    private suspend fun getAndCacheAllAkVideos() {
        val response = akVideoDao.getAll()
        if (!response.isNullOrEmpty()) {
            response.forEach {
                getAkVideosFromApi(sid = it.sid, bid = it.bid, tid = it.tid)
            }
        }
    }

    suspend fun getIndex(): IndexEntity {
        val cachedResponse = indexDao.getIndexString(id = 1)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val apiResponse = encryptedApi.getIndex()
            indexDao.insertIndex(IndexEntity(1, apiResponse))
            indexDao.getIndexString(1)!!
        }
    }

    suspend fun getIndexFromApi(): IndexEntity {
        val apiResponse = encryptedApi.getIndex()
        indexDao.insertIndex(IndexEntity(1, apiResponse))
        return indexDao.getIndexString(1)!!
    }

    suspend fun getAkSubjects(id: Int): AkSubjectEntity {

        val cachedResponse = akSubjectDao.getById(id)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = encryptedApi.getAkSubjects(id)
            akSubjectDao.upsert(
                AkSubjectEntity(
                    id = id,
                    response = response
                )
            )
            akSubjectDao.getById(id)!!
        }
    }

    suspend fun getAkSubjectsFromApi(id: Int): AkSubjectEntity {
        val response = encryptedApi.getAkSubjects(id)
        akSubjectDao.upsert(
            AkSubjectEntity(
                id = id,
                response = response
            )
        )
        return akSubjectDao.getById(id)!!
    }

    suspend fun getAkLessons(sid: Int, bid: Int): AkLessonEntity {
        val cachedResponse = akLessonDao.getById(sid)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = encryptedApi.getAkLessons(sid = sid, bid = bid)
            akLessonDao.upsert(
                AkLessonEntity(
                    bid = bid, sid = sid, response = response
                )
            )
            akLessonDao.getById(sid)!!
        }
    }

    suspend fun getAkLessonsFromApi(sid: Int, bid: Int): AkLessonEntity {
        val response = encryptedApi.getAkLessons(sid = sid, bid = bid)
        akLessonDao.upsert(
            AkLessonEntity(
                bid = bid, sid = sid, response = response
            )
        )
        return akLessonDao.getById(sid)!!
    }

    suspend fun getAkVideos(sid: Int, tid: Int, bid: Int): AkVideoEntity {
        val cachedResponse = akVideoDao.getById(tid)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = encryptedApi.getAkVideo(sid, tid, bid)
            akVideoDao.upsert(
                AkVideoEntity(
                    bid = bid, sid = sid, tid = tid, response = response
                )
            )
            akVideoDao.getById(tid)!!
        }
    }

    suspend fun getAkVideosFromApi(sid: Int, tid: Int, bid: Int): AkVideoEntity {
        val response = encryptedApi.getAkVideo(sid, tid, bid)
        akVideoDao.upsert(
            AkVideoEntity(
                bid = bid, sid = sid, tid = tid, response = response
            )
        )
        return akVideoDao.getById(tid)!!
    }

    suspend fun getAkNotes(sid: Int, tid: Int, bid: Int): AkNotesEntity {

        val cachedResponse = akNoteDao.getById(tid)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = encryptedApi.getAkNotes(sid, tid, bid)
            akNoteDao.upsert(
                AkNotesEntity(
                    bid = bid, sid = sid, tid = tid, response = response
                )
            )
            akNoteDao.getById(tid)!!
        }

    }

    suspend fun getAkNotesFromApi(sid: Int, tid: Int, bid: Int): AkNotesEntity {
        val response = encryptedApi.getAkNotes(sid, tid, bid)

        akNoteDao.upsert(
            AkNotesEntity(
                bid = bid, sid = sid, tid = tid, response = response
            )
        )
        return akNoteDao.getById(tid)!!
    }

    suspend fun getAkVideoU(
        sid: Int,
        tid: Int,
        bid: Int,
        key: Int,
        lid: String,
    ): AkUrl {
        return api.getAkVideoU(key = key, lid = lid, tid = tid, bid = bid, sid = sid)
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
            return cachedResponse
        } else {
            val response: KeyItem = api.getKeyTask()
            keyGenerateDao.upsert(
                KeyGenerateEntity(
                    id = 1,
                    task1_url = response.task1_url,
                    task1_final_url = response.task1_final_url,
                    telegram = response.telegram,
                    visible = response.visible
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

    suspend fun insertTimeline(item: TimelineEntity){
        timelineDao.insert(item)
    }

    suspend fun getTimelineById(externalId: String): TimelineEntity? {
        return timelineDao.getById(externalId)
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
    ): BatchDetailsEntityy {
        val cachedResponse = batchDetailsDaoo.getById(batchId)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = encryptedApi.getBatchDetails(
                batchId = batchId, batchSlug = batchSlug, classValue = classValue
            )
            batchDetailsDaoo.insert(
                BatchDetailsEntityy(
                    batchId = batchId,
                    response = response,
                    slug = batchSlug,
                    classValue = classValue
                )
            )
            batchDetailsDaoo.getById(batchId)!!

        }

    }

    suspend fun getBatchDetailsFromApi(
        batchId: String,
        batchSlug: String,
        classValue: String,
    ): BatchDetailsEntityy {
        val response = encryptedApi.getBatchDetails(
            batchId = batchId, batchSlug = batchSlug, classValue = classValue
        )
        batchDetailsDaoo.insert(
            BatchDetailsEntityy(
                batchId = batchId,
                response = response,
                slug = batchSlug,
                classValue = classValue
            )
        )
        return batchDetailsDaoo.getById(batchId)!!
    }

}