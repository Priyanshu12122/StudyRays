package com.xerox.studyrays.data

import android.util.Log
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
import com.xerox.studyrays.cacheDb.pwCache.courseDb.PwCourseDao
import com.xerox.studyrays.cacheDb.pwCache.courseDb.PwCourseEntity
import com.xerox.studyrays.cacheDb.pwCache.dppDb.PwDppDao
import com.xerox.studyrays.cacheDb.pwCache.dppDb.PwDppEntity
import com.xerox.studyrays.cacheDb.pwCache.lessonDb.PwLessonDao
import com.xerox.studyrays.cacheDb.pwCache.lessonDb.PwLessonEntity
import com.xerox.studyrays.cacheDb.pwCache.notesDb.PwNotesDao
import com.xerox.studyrays.cacheDb.pwCache.notesDb.PwNotesEntity
import com.xerox.studyrays.cacheDb.pwCache.videosDb.PwVideoDao
import com.xerox.studyrays.cacheDb.pwCache.videosDb.PwVideoEntity
import com.xerox.studyrays.db.exampleDb.Example
import com.xerox.studyrays.db.exampleDb.ExampleDao
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourseDao
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFav
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFavDao
import com.xerox.studyrays.db.videoDb.Video
import com.xerox.studyrays.db.videoDb.VideoDao
import com.xerox.studyrays.model.akModel.aKVideoU.AkUrl
import com.xerox.studyrays.model.pwModel.DppSolutionItem
import com.xerox.studyrays.model.pwModel.SearchItem
import com.xerox.studyrays.model.pwModel.alertItem.AlertItem
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetails
import com.xerox.studyrays.network.ApiService
import com.xerox.studyrays.network.EncryptedApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiRepository @Inject constructor(
    private val api: ApiService,
    private val encryptedApi: EncryptedApiService,
    private val dao: FavouriteCourseDao,
    private val videoDao: VideoDao,
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
) {

//    search
    suspend fun insertSearchItem(item: SearchEntity) {
        searchDao.insert(item)
    }

    suspend fun deleteSearchItem(item: SearchEntity) {
        searchDao.delete(item)
    }

    suspend fun getAllSearchItems(): Flow<List<SearchEntity>?> {
        return searchDao.getAll()
    }

    suspend fun exists(searchText: String): Boolean{
        return searchDao.exists(searchText)
    }

//
    suspend fun insertExample(example: Example) {
        exDao.insertExample(example)
    }

    suspend fun getAllExamples(): List<Example> {
        return exDao.getAllExamples()
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
        bname: String,
        commentKey: String,
        page: Int,
    ): String {
        return encryptedApi.getComments(
            bname = bname,
            commentKey = commentKey,
            page = page
        )
    }

//    Khazana lectures

    suspend fun getKhazanaVideos(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String
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
        topicName: String
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
        return khazanaLecturesDao.getKhazanaLectureString(chapterId)!!
    }

    suspend fun getKhazanaNotes(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String
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
        topicName: String
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
        topicName: String
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
        topicName: String
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
        topicName: String
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
        val apiResponse = encryptedApi.getKhazanaSubjects(id)
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
        return if (cacheResponse != null){
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

//    Video db

    fun getAllVideoLectures(): Flow<List<Video?>?> {
        return videoDao.getAllVideoLectures()
    }

    suspend fun checkIfPresentVideoDB(videoId: String): Boolean {
        return videoDao.checkIfPresentInDbVIdeo(videoId)
    }

    suspend fun insertVideo(item: Video) {
        videoDao.upsertItem(item)
    }

    suspend fun deleteVideo(item: Video) {
        videoDao.deleteItem(item)
    }

//

    suspend fun getAllCourses(classValue: String): List<PwCourseEntity>? {
        val cachedResponse = pwCourseDao.getById(classValue)
        if (!cachedResponse.isNullOrEmpty()) {
            return cachedResponse
        } else {
            val response = api.getAllCourses(classValue)
            response?.forEach { item ->
                pwCourseDao.insert(
                    PwCourseEntity(
                        id = item.id,
                        classValue = item.`class`,
                        externalId = item.external_id,
                        name = item.name,
                        language = item.language,
                        imageUrl = item.previewImage_baseUrl.plus(item.previewImage_key),
                        byName = item.byName
                    )
                )
            }
            return pwCourseDao.getById(classValue)
        }
    }

    suspend fun getAllCoursesFromApi(classValue: String): List<PwCourseEntity>? {
        val response = api.getAllCourses(classValue)
        response?.forEach { item ->
            pwCourseDao.insert(
                PwCourseEntity(
                    id = item.id,
                    classValue = item.`class`,
                    externalId = item.external_id,
                    name = item.name,
                    language = item.language,
                    imageUrl = item.previewImage_baseUrl.plus(item.previewImage_key),
                    byName = item.byName
                )
            )
        }
        return pwCourseDao.getById(classValue)
    }

    suspend fun getAllSubjects(courseId: String): BatchDetails {
        return api.getAllSubjects(courseId)
    }

    suspend fun getAllLessons(subjectId: String): List<PwLessonEntity>? {
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
                        exercises = it.exercises
                    )
                )
            }
            return pwLessonDao.getById(subjectId)
        }
    }

    suspend fun getAllLessonsFromApi(subjectId: String): List<PwLessonEntity>? {
        val response = api.getALlLessons(subjectId)
        Log.d("TAG", "getAllLessonsFromApi: response = $response")
        response?.forEach {
            Log.d("TAG", "getAllLessonsFromApi: subjectId = $subjectId")
            pwLessonDao.insert(
                PwLessonEntity(
                    subjectId = subjectId,
                    name = it.name,
                    notes = it.notes,
                    slug = it.slug,
                    videos = it.videos,
                    exercises = it.exercises
                )
            )
        }
        return pwLessonDao.getById(subjectId)
    }

    suspend fun getAllVideos(slug: String): PwVideoEntity {
        val cachedResponse = pwVideoDao.getById(slug)
        return if (cachedResponse != null) {
            cachedResponse
        } else {
            val response = encryptedApi.getAllVideos(slug)
            pwVideoDao.insert(PwVideoEntity(slug = slug, response = response))
            pwVideoDao.getById(slug)!!
        }
    }

    suspend fun getAllVideosFromApi(slug: String): PwVideoEntity {
        val response = encryptedApi.getAllVideos(slug)
        pwVideoDao.insert(PwVideoEntity(slug = slug, response = response))
        return pwVideoDao.getById(slug)!!
    }

    suspend fun getAllNotes(slug: String): List<PwNotesEntity>? {
        val cacheResponse = pwNotesDao.getById(slug)
        return if (!cacheResponse.isNullOrEmpty()){
            cacheResponse
        } else {
            val response = api.getAllNotes(slug)
            response?.forEach {
                pwNotesDao.insert(PwNotesEntity(
                    slug = slug,
                    topic = it.topic,
                    baseUrl = it.attachment_base_url,
                    attachmentKey = it.attachment_key
                ))
            }
            pwNotesDao.getById(slug)
        }
    }

    suspend fun getAllNotesFromApi(slug: String): List<PwNotesEntity>? {
        val response = api.getAllNotes(slug)
        response?.forEach {
            pwNotesDao.insert(PwNotesEntity(
                slug = slug,
                topic = it.topic,
                baseUrl = it.attachment_base_url,
                attachmentKey = it.attachment_key
                ))
        }
        return pwNotesDao.getById(slug)
    }

    suspend fun getAllDpp(slug: String): List<PwDppEntity>? {
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
                        attachmentKey = it.attachment_key
                    )
                )
            }
            return pwDppDao.getById(slug)
        }
    }

    suspend fun getAllDppFromApi(slug: String): List<PwDppEntity>? {
        val response = api.getAllDpp(slug)
        response?.forEach {
            pwDppDao.insert(
                PwDppEntity(
                    slug = slug,
                    topic = it.topic,
                    baseUrl = it.attachment_base_url,
                    attachmentKey = it.attachment_key
                )
            )
        }
        return pwDppDao.getById(slug)
    }

    suspend fun getAllDppSolution(slug: String): List<DppSolutionItem>? {
        return api.getAllDppSolution(slug)
    }


//Ak

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

}