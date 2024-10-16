package com.xerox.studyrays.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.xerox.studyrays.cacheDb.keyGeneratorCache.KeyGenerateEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchEntity
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.data.internetAvailability.NetworkManager
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.db.keyDb.KeyEntity
import com.xerox.studyrays.db.watchLaterDb.WatchLaterEntity
import com.xerox.studyrays.model.downloads.DownloadItem
import com.xerox.studyrays.model.pwModel.CourseItem
import com.xerox.studyrays.model.pwModel.CourseItemX
import com.xerox.studyrays.model.pwModel.DppItem
import com.xerox.studyrays.model.pwModel.DppSolutionItem
import com.xerox.studyrays.model.pwModel.Lesson.LessonItem
import com.xerox.studyrays.model.pwModel.NoteItem
import com.xerox.studyrays.model.pwModel.PromoItem
import com.xerox.studyrays.model.pwModel.SearchItem
import com.xerox.studyrays.model.pwModel.SearchOldItem
import com.xerox.studyrays.model.pwModel.VideoItem
import com.xerox.studyrays.model.pwModel.alertItem.AlertItem
import com.xerox.studyrays.model.pwModel.announcementsItem.AnnouncementItem
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetails
import com.xerox.studyrays.model.pwModel.batchDetails.TeacherId
import com.xerox.studyrays.model.pwModel.batchDetailss.BatchDetailsssItem
import com.xerox.studyrays.model.pwModel.batchDetailss.Subject
import com.xerox.studyrays.model.pwModel.batchDetailss.Teacher
import com.xerox.studyrays.model.pwModel.comments.CommentItem
import com.xerox.studyrays.model.pwModel.fetchModel.FetchClassItem
import com.xerox.studyrays.model.pwModel.fetchModel.FetchExamItem
import com.xerox.studyrays.model.pwModel.fetchModel.FetchYearItem
import com.xerox.studyrays.model.pwModel.old.DppOldItem
import com.xerox.studyrays.model.pwModel.old.DppSolutionOldItem
import com.xerox.studyrays.model.pwModel.old.NoteOldItem
import com.xerox.studyrays.model.pwModel.old.VideoOldItem
import com.xerox.studyrays.model.pwModel.priceItem.PriceItem
import com.xerox.studyrays.model.pwModel.statusItem.StatusItem
import com.xerox.studyrays.model.videoPlayerModel.TimelineItem
import com.xerox.studyrays.navigation.NavRoutes
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.utils.Constants.PAGE_SIZE
import com.xerox.studyrays.utils.decrypt
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Base64
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ApiRepository,
    @ApplicationContext context: Context,
) : ViewModel() {

    private val key = Keys.kkk()
    private val iv = Keys.ivv()
    private val gson = Gson()

    val noInternetMsg = "Internet Unavailable."
    val nullErrorMsg = "Null"
    private val socketErrorMsg = "Socket timeout: Either slow internet or server issue."

    private val snackBarMsg = "Refreshed Successfully!"
    var isRefreshing by mutableStateOf(false)

    private val networkManager = NetworkManager(context)

    fun insertSearchItem(item: SearchEntity) {
        viewModelScope.launch {
            val exists = repository.exists(item.searchText)
            if (!exists) {
                repository.insertSearchItem(item)
            }
        }
    }

    fun deleteSearchItem(item: SearchEntity) {
        viewModelScope.launch {
            repository.deleteSearchItem(item)
        }
    }

    private val _cacheSearch: MutableStateFlow<List<SearchEntity>?> = MutableStateFlow(emptyList())
    val cacheSearch = _cacheSearch.asStateFlow()

    fun getAllSearchItem() {
        viewModelScope.launch {
            repository.getAllSearchItems().collect {
                _cacheSearch.value = it
            }
        }
    }


    private val _totalFee: MutableStateFlow<Response<PriceItem>> =
        MutableStateFlow(Response.Loading())
    val totalFee = _totalFee.asStateFlow()

    fun getTotalFee() {
        viewModelScope.launch {
            try {
                _totalFee.value = Response.Loading()
                val response = repository.getTotalFee()
                _totalFee.value = Response.Success(response)
            } catch (e: Exception) {
                _totalFee.value = Response.Error(e.localizedMessage!!)
            }
        }
    }


//    Alert items

    private val _alert: MutableStateFlow<Response<AlertItem>> = MutableStateFlow(Response.Loading())
    val alert = _alert.asStateFlow()

    fun getAlertItem() {
        viewModelScope.launch {
            try {
                _alert.value = Response.Loading()
                val response = repository.getAlertItem()
                _alert.value = Response.Success(response)
            } catch (e: Exception) {
                _alert.value = Response.Error(e.localizedMessage!!)
            }
        }
    }

//    navBar

    private val _navBar: MutableStateFlow<ResponseTwo<NavEntity>> =
        MutableStateFlow(ResponseTwo.Loading())
    val navBar = _navBar.asStateFlow()

    fun getNavItems() {
        viewModelScope.launch {
            try {
                _navBar.value = ResponseTwo.Loading()
                val response = repository.getNavItems()
                _navBar.value = ResponseTwo.Success(response)
            } catch (e: Exception) {
                _navBar.value = ResponseTwo.Error(e.localizedMessage!!)
            }
        }
    }


    //    comments

    private val _comments: MutableStateFlow<Response<List<CommentItem>?>> =
        MutableStateFlow(Response.Loading())
    val comments = _comments.asStateFlow()

    fun getComments(
        externalId: String,
        topicSlug: String,
    ) {

        viewModelScope.launch {
            try {
                _comments.value = Response.Loading()
                val response =
                    repository.getComments(externalId = externalId, topicSlug = topicSlug)
                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<CommentItem>? =
                    gson.fromJson(decryptedData, Array<CommentItem>::class.java).toList()
                if (data == null) {
                    throw NullPointerException(nullErrorMsg)
                } else {
                    _comments.value = Response.Success(data)
                }
            } catch (e: SocketTimeoutException) {
                _comments.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _comments.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _comments.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

//    Favourite batches

//    private val _allFavouriteCourses: MutableStateFlow<List<FavouriteCourse?>?> =
//        MutableStateFlow(emptyList())
//    val allFavCourses = _allFavouriteCourses.asStateFlow()
//
//    fun getAllFavCourses() {
//        viewModelScope.launch {
//            repository.getAllFavouriteCourses().collect {
//                _allFavouriteCourses.value = it
//            }
//        }
//    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showFixedToast(context: Context) {
        Toast.makeText(context, "Sorry some error occurred", Toast.LENGTH_SHORT).show()
    }

    fun onFavoriteClick(item: FavouriteCourse) {
        viewModelScope.launch {
            if (repository.checkIfPresent(item.externalId)) {
                repository.deleteFav(item)
            } else {
                repository.insertFav(item)
            }

        }
    }

    suspend fun checkIfItemIsPresentInDb(externalId: String): Boolean {
        return repository.checkIfPresent(externalId)
    }


    private val _fetchClass: MutableStateFlow<Response<List<FetchClassItem>?>> =
        MutableStateFlow(Response.Loading())
    val fetchClass = _fetchClass.asStateFlow()

    fun fetchClasses() {
        viewModelScope.launch {
            try {
                _fetchClass.value = Response.Loading()
                val response = repository.getFetchClasses()
                Log.d("TAG", "fetchClasses: classes = $response")
                _fetchClass.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _fetchClass.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _fetchClass.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _fetchClass.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }

        }
    }

    private val _fetchYears: MutableStateFlow<Response<List<FetchYearItem>?>> =
        MutableStateFlow(Response.Loading())
    val fetchYears: StateFlow<Response<List<FetchYearItem>?>> = _fetchYears.asStateFlow()

    fun fetchYears(classValue: String) {
        viewModelScope.launch {
            try {
                _fetchYears.value = Response.Loading()
                val response = repository.getFetchYears(classValue)
                _fetchYears.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _fetchYears.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _fetchYears.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _fetchYears.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }

        }
    }

    private val _fetchExams: MutableStateFlow<Response<List<FetchExamItem?>?>> =
        MutableStateFlow(Response.Loading())
    val fetchExams = _fetchExams.asStateFlow()

    fun fetchExams(classValue: String, year: String) {
        viewModelScope.launch {
            try {
                _fetchExams.value = Response.Loading()
                val response = repository.getFetchExams(classValue, year)
                _fetchExams.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _fetchExams.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _fetchExams.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _fetchExams.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }

        }
    }

    private val _fetchSearch: MutableStateFlow<Response<List<CourseItemX>?>> =
        MutableStateFlow(Response.Loading())
    val fetchSearch: StateFlow<Response<List<CourseItemX>?>> = _fetchSearch.asStateFlow()

    fun fetchSearches(searchQuery: String) {
        viewModelScope.launch {
            try {
                _fetchSearch.value = Response.Loading()
                val response = repository.getFetchSearches(searchQuery)
                _fetchSearch.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _fetchSearch.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _fetchSearch.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _fetchSearch.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    private val _fetchAllBatches: MutableStateFlow<Response<List<CourseItemX>?>> =
        MutableStateFlow(Response.Loading())
    val fetchAllBatches: StateFlow<Response<List<CourseItemX>?>> = _fetchAllBatches.asStateFlow()

    fun fetchAllBatches() {
        viewModelScope.launch {
            try {
                _fetchAllBatches.value = Response.Loading()
                val response = repository.getFetchAllBatches()
                _fetchAllBatches.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _fetchAllBatches.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _fetchAllBatches.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _fetchAllBatches.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    private val _fetchBatches: MutableStateFlow<Response<List<CourseItemX>?>> =
        MutableStateFlow(Response.Loading())
    val fetchBatches: StateFlow<Response<List<CourseItemX>?>> = _fetchBatches.asStateFlow()

    fun fetchBatches(classValue: String, year: String, exam: String) {
        viewModelScope.launch {
            try {
                _fetchBatches.value = Response.Loading()
                val response =
                    repository.getFetchBatches(classValue = classValue, year = year, exam = exam)
                _fetchBatches.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _fetchBatches.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _fetchBatches.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _fetchBatches.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    private val _allCourses: MutableStateFlow<Response<List<CourseItemX>>> =
        MutableStateFlow(Response.Loading())
    val courses = _allCourses.asStateFlow()


    val neetCourses = mutableListOf<CourseItemX>()
    val jeeCourses = mutableListOf<CourseItemX>()
    val boardCourses = mutableListOf<CourseItemX>()
    val vidyapeethCourses = mutableListOf<CourseItemX>()
    val freeCourses = mutableListOf<CourseItemX>()
    val youtubeCourses = mutableListOf<CourseItemX>()
    val otherCourses = mutableListOf<CourseItemX>()


    fun getClasses(
        classValue: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allCourses.value = Response.Loading()
                }
                val response = repository.getAllCourses(classValue)

                neetCourses.clear()
                jeeCourses.clear()
                boardCourses.clear()
                vidyapeethCourses.clear()
                freeCourses.clear()
                youtubeCourses.clear()
                otherCourses.clear()
                response?.forEach {
                    val name = it.batch_name.lowercase()
                    val byName = it.byName.lowercase()

                    when {
                        name.contains("neet") -> neetCourses.add(it)
                        name.contains("jee") -> jeeCourses.add(it)
                        name.contains("board") -> boardCourses.add(it)
                        name.contains("vidyapeeth") -> vidyapeethCourses.add(it)
                        name.contains("free") -> freeCourses.add(it)
                        name.contains("yt") || byName.contains("youtube") -> youtubeCourses.add(it)
                        else -> otherCourses.add(it)
                    }

                }

                _allCourses.value = Response.Success(response!!)
            } catch (e: SocketTimeoutException) {
                _allCourses.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allCourses.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allCourses.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllCourses(
        classValue: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getClasses(classValue = classValue, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }
    }


//        private var currentPage = 1
//        private var isLastPage = false
//         var isLoadingg = false
//        private val itemsPerPage = 2
//
//        fun getClassesPaginated(
//            classValue: String,
//            isRefresh: Boolean = false,
//            onComplete: (() -> Unit)? = null,
//        ) {
//            if (isLoadingg || isLastPage) return
//
//            viewModelScope.launch {
//                try {
//                    isLoadingg = true
//                    if (!isRefresh && currentPage == 1) {
//                        _allCourses.value = Response.Loading()
//                    }
//                    val response = repository.getAllCoursesPaginated(
//                        classValue = classValue,
//                        page = currentPage,
//                        limit = itemsPerPage
//                    )
//
//                    if (response.isNullOrEmpty()) {
//                        isLastPage = true
//                    } else {
//                        if (isRefresh) {
//                            neetCourses.clear()
//                            jeeCourses.clear()
//                            boardCourses.clear()
//                            vidyapeethCourses.clear()
//                            freeCourses.clear()
//                            youtubeCourses.clear()
//                            otherCourses.clear()
//                        }
//
//                        response.forEach {
//                            val name = it.batch_name.lowercase()
//                            val byName = it.byName.lowercase()
//
//                            when {
//                                name.contains("neet") -> neetCourses.add(it)
//                                name.contains("jee") -> jeeCourses.add(it)
//                                name.contains("board") -> boardCourses.add(it)
//                                name.contains("vidyapeeth") -> vidyapeethCourses.add(it)
//                                name.contains("free") -> freeCourses.add(it)
//                                name.contains("yt") || byName.contains("youtube") -> youtubeCourses.add(it)
//                                else -> otherCourses.add(it)
//                            }
//                        }
//
//                        val allCourses = neetCourses + jeeCourses + boardCourses + vidyapeethCourses + freeCourses + youtubeCourses + otherCourses
//                        _allCourses.value = Response.Success(allCourses)
//                        currentPage++
//                    }
//                    isLoadingg = false
//                } catch (e: SocketTimeoutException) {
//                    _allCourses.value = Response.Error(socketErrorMsg)
//                    isLoadingg = false
//                } catch (e: UnknownHostException) {
//                    _allCourses.value = Response.Error(noInternetMsg)
//                    isLoadingg = false
//                } catch (e: Exception) {
//                    _allCourses.value = Response.Error(e.localizedMessage ?: "An error occurred.")
//                    isLoadingg = false
//                } finally {
//                    if (isRefresh) {
//                        isRefreshing = false
//                    }
//                    onComplete?.invoke()
//                }
//            }
//        }
//
//        fun loadNextPage(classValue: String) {
//            Log.d("TAG", "loadNextPage: $isLoadingg  $currentPage    $isLastPage")
//            getClassesPaginated(classValue)
//        }
//
//        fun refreshClasses(classValue: String) {
//            currentPage = 1
//            isLastPage = false
//            getClasses(classValue, isRefresh = true)
//        }


    private val _allCoursesOld: MutableStateFlow<Response<List<CourseItem>>> =
        MutableStateFlow(Response.Loading())
    val coursesOld = _allCoursesOld.asStateFlow()


    val neetCoursesOld = mutableListOf<CourseItem>()
    val jeeCoursesOld = mutableListOf<CourseItem>()
    val boardCoursesOld = mutableListOf<CourseItem>()
    val vidyapeethCoursesOld = mutableListOf<CourseItem>()
    val freeCoursesOld = mutableListOf<CourseItem>()
    val youtubeCoursesOld = mutableListOf<CourseItem>()
    val otherCoursesOld = mutableListOf<CourseItem>()


    fun getClassesOld(
        classValue: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allCoursesOld.value = Response.Loading()
                }
                val response = repository.getAllCoursesOld(classValue)

                neetCoursesOld.clear()
                jeeCoursesOld.clear()
                boardCoursesOld.clear()
                vidyapeethCoursesOld.clear()
                freeCoursesOld.clear()
                youtubeCoursesOld.clear()
                otherCoursesOld.clear()
                response?.forEach {
                    val name = it.name.lowercase()
                    val byName = it.byName.lowercase()

                    when {
                        name.contains("neet") -> neetCoursesOld.add(it)
                        name.contains("jee") -> jeeCoursesOld.add(it)
                        name.contains("board") -> boardCoursesOld.add(it)
                        name.contains("vidyapeeth") -> vidyapeethCoursesOld.add(it)
                        name.contains("free") -> freeCoursesOld.add(it)
                        name.contains("yt") || byName.contains("youtube") -> youtubeCoursesOld.add(
                            it
                        )

                        else -> otherCoursesOld.add(it)
                    }

                }

                _allCoursesOld.value = Response.Success(response!!)
            } catch (e: SocketTimeoutException) {
                _allCoursesOld.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allCoursesOld.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allCoursesOld.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllCoursesOld(
        classValue: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getClassesOld(classValue = classValue, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _allSubjectsOld: MutableStateFlow<ResponseTwo<BatchDetails?>> =
        MutableStateFlow(ResponseTwo.Loading())
    val subjectsOld = _allSubjectsOld.asStateFlow()

    val subjectsListOld = mutableListOf<com.xerox.studyrays.model.pwModel.batchDetails.Subject?>()
    val teachersListOld = mutableListOf<TeacherId?>()

    fun getAllSubjectsOld(
        courseId: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allSubjectsOld.value = ResponseTwo.Loading()
                }
                val response = repository.getAllSubjects(courseId)
                if (response.subjects.isEmpty()) {
                    _allSubjectsOld.value = ResponseTwo.Nothing()
                    return@launch
                }
                subjectsListOld.clear()
                teachersListOld.clear()
                response.subjects.forEach {
                    subjectsListOld.add(it)

                    it.teacherIds.forEach { teacherId ->
                        teachersListOld.add(teacherId)
                    }

                }
                _allSubjectsOld.value = ResponseTwo.Success(response)
            } catch (e: SocketTimeoutException) {
                _allSubjectsOld.value =
                    ResponseTwo.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allSubjectsOld.value = ResponseTwo.Error(noInternetMsg)
            } catch (e: Exception) {
                _allSubjectsOld.value =
                    ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }


    fun refreshAllSubjectsOld(
        courseId: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getAllSubjectsOld(
            courseId = courseId,
            isRefresh = true,
        ) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _allSubjects: MutableStateFlow<ResponseTwo<List<BatchDetailsssItem>>> =
        MutableStateFlow(ResponseTwo.Loading())
    val subjects = _allSubjects.asStateFlow()

    val subjectsList = mutableListOf<Subject?>()
    val teachersList = mutableListOf<Teacher?>()

    fun getAllSubjects(
        batchId: String,
        classValue: String,
        slug: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allSubjects.value = ResponseTwo.Loading()
                }
                val response = repository.getBatchDetails(
                    batchId = batchId,
                    batchSlug = slug,
                    classValue = classValue
                )

                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<BatchDetailsssItem>? =
                    gson.fromJson(decryptedData, Array<BatchDetailsssItem>::class.java)?.toList()
                if (data == null) {
                    throw NullPointerException(nullErrorMsg)
                } else {

                    subjectsList.clear()
                    teachersList.clear()

                    data.forEach {
                        it.subjects.forEach { item ->
                            subjectsList.add(item)

                            item.teachers.forEach { teacher ->
                                teachersList.add(teacher)
                            }
                        }


                    }

                    _allSubjects.value = ResponseTwo.Success(data)
                }
            } catch (e: SocketTimeoutException) {
                _allSubjects.value =
                    ResponseTwo.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allSubjects.value = ResponseTwo.Error(noInternetMsg)
            } catch (e: Exception) {
                _allSubjects.value = ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllSubjects(
        batchId: String,
        classValue: String,
        slug: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getAllSubjects(
            batchId = batchId,
            classValue = classValue,
            slug = slug,
            isRefresh = true
        ) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _allFavSubjects: MutableStateFlow<ResponseTwo<List<FavouriteCourse?>>> =
        MutableStateFlow(ResponseTwo.Nothing())
    val favSubjects = _allFavSubjects.asStateFlow()

    var favCourseList = mutableListOf<FavouriteCourse?>(null)

    fun getAllFavSubjects() {
        viewModelScope.launch {

            favCourseList.clear()
            try {
                _allFavSubjects.value = ResponseTwo.Loading()
                repository.getAllFavouriteCourses().collect {
                    if (it.isNullOrEmpty()) {
                        _allFavSubjects.value = ResponseTwo.Nothing()
                    } else {
                        it.forEach { item ->
                            favCourseList.add(item)
                        }

                        _allFavSubjects.value = ResponseTwo.Success(it)
                    }
                }

            } catch (e: SocketTimeoutException) {
                _allFavSubjects.value = ResponseTwo.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allFavSubjects.value = ResponseTwo.Error(noInternetMsg)
            } catch (e: Exception) {
                _allFavSubjects.value =
                    ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    //    Promo
    private val _promo: MutableStateFlow<Response<List<PromoItem?>?>> =
        MutableStateFlow(Response.Loading())
    val promo = _promo.asStateFlow()

    fun getAllPromoItems() {
        viewModelScope.launch {
            try {
                _promo.value = Response.Loading()
                val response = repository.getAllPromoItems()
                _promo.value = Response.Success(response)
            } catch (e: Exception) {
                _promo.value = Response.Error(e.localizedMessage!!)
            }
        }
    }


    private val _allLessonsOld: MutableStateFlow<Response<List<LessonItem>>> =
        MutableStateFlow(Response.Loading())
    val lessonsOld = _allLessonsOld.asStateFlow()

    fun getAllLessonsOld(
        subjectId: String, isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allLessonsOld.value = Response.Loading()
                }
                val response = repository.getAllLessonsOld(subjectId)
                _allLessonsOld.value = Response.Success(response!!)
            } catch (e: SocketTimeoutException) {
                _allLessonsOld.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allLessonsOld.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allLessonsOld.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllLessonsOld(subjectId: String, onComplete: () -> Unit) {
        isRefreshing = true
        getAllLessonsOld(subjectId = subjectId, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _allLessons: MutableStateFlow<Response<List<LessonItem>>> =
        MutableStateFlow(Response.Loading())
    val lessons = _allLessons.asStateFlow()

    fun getAllLessons(
        batchId: String,
        subjectSlug: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allLessons.value = Response.Loading()
                }
                val response =
                    repository.getAllLessons(batchId = batchId, subjectSlug = subjectSlug)


                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<LessonItem>? =
                    gson.fromJson(decryptedData, Array<LessonItem>::class.java)?.toList()
                if (data == null) {
                    throw NullPointerException(nullErrorMsg)
                } else {
                    _allLessons.value = Response.Success(data)
                }
            } catch (e: SocketTimeoutException) {
                _allLessons.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allLessons.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allLessons.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllLessons(
        batchId: String,
        subjectSlug: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getAllLessons(
            batchId = batchId,
            subjectSlug = subjectSlug,
            isRefresh = true,
        ) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _allVideosOld: MutableStateFlow<Response<List<VideoOldItem>>> =
        MutableStateFlow(Response.Loading())
    val videosOld = _allVideosOld.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllVideosOld(
        slug: String, isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allVideosOld.value = Response.Loading()
                }
                val response = repository.getAllVideosOld(slug)
                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<VideoOldItem> =
                    gson.fromJson(decryptedData, Array<VideoOldItem>::class.java).toList()
                _allVideosOld.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _allVideosOld.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allVideosOld.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allVideosOld.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAllVideosOld(slug: String, onComplete: () -> Unit) {
        isRefreshing = true
        getAllVideosOld(slug = slug, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _allVideos: MutableStateFlow<Response<List<VideoItem>>> =
        MutableStateFlow(Response.Loading())
    val videos = _allVideos.asStateFlow()

//    @RequiresApi(Build.VERSION_CODES.O)
//    fun getAllVideos(
//        topicSlug: String,
//        subjectSlug: String,
//        batchId: String,
//        isRefresh: Boolean = false,
//        onComplete: (() -> Unit)? = null,
//    ) {
//        viewModelScope.launch {
//            try {
//                if (!isRefresh) {
//                    _allVideos.value = Response.Loading()
//                }
//                val response = repository.getAllVideos(
//                    batchId = batchId,
//                    subjectSlug = subjectSlug,
//                    topicSlug = topicSlug
//                )
//
//                val decodedData = Base64.getDecoder().decode(response)
//                val decryptedData = decrypt(decodedData, key, iv)
//                val data: List<VideoItem>? =
//                    gson.fromJson(decryptedData, Array<VideoItem>::class.java)?.toList()
//                if (data == null) {
//                    throw NullPointerException(nullErrorMsg)
//                } else {
//                    _allVideos.value = Response.Success(data)
//                }
//            } catch (e: SocketTimeoutException) {
//                _allVideos.value =
//                    Response.Error(socketErrorMsg)
//            } catch (e: UnknownHostException) {
//                _allVideos.value = Response.Error(noInternetMsg)
//            } catch (e: Exception) {
//                _allVideos.value = Response.Error(e.localizedMessage ?: "An error occurred.")
//            } finally {
//                if (isRefresh) {
//                    isRefreshing = false
//                }
//                onComplete?.invoke()
//            }
//        }
//    }

    fun loadNextItems(
        topicSlug: String,
        subjectSlug: String,
        batchId: String,
    ) {
        viewModelScope.launch {
            if ((scrollPosition + 1) >= (state.page * PAGE_SIZE)) {
                state = state.copy(isLoading = true)
                incrementPage()
                delay(1000)

                if (state.page > 1) {
                    val result = repository.getAllVideosPaginated(
                        batchId = batchId,
                        subjectSlug = subjectSlug,
                        topicSlug = topicSlug,
                        page = state.page,
                        limit = PAGE_SIZE
                    )

                    val decodedData = Base64.getDecoder().decode(result)
                    val decryptedData = decrypt(decodedData, key, iv)
                    val data: List<VideoItem>? =
                        gson.fromJson(decryptedData, Array<VideoItem>::class.java)?.toList()
                    if (data == null) {
                        state = state.copy(error = nullErrorMsg)
                    } else {
                        addNewVideos(data)
                    }
                }
                state = state.copy(isLoading = false)


            }

        }


    }

    private var scrollPosition = 0

    private fun incrementPage() {
        state = state.copy(page = state.page + 1)
    }

    fun onChangeScrollPosition(position: Int) {
        scrollPosition = position
    }

    private fun addNewVideos(items: List<VideoItem>) {
        state = state.copy(items = state.items + items)
    }

    data class ScreenState(
        val isLoading: Boolean = false,
        val items: List<VideoItem> = emptyList(),
        val error: String? = null,
        val endReached: Boolean = false,
        val page: Int = 0
    )

    var state by mutableStateOf(ScreenState())

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllVideos(
        topicSlug: String,
        subjectSlug: String,
        batchId: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {

            state = state.copy(page = 1)
            onChangeScrollPosition(0)

            try {
                if (!isRefresh) {
                    _allVideos.value = Response.Loading()
                }
                val response = repository.getAllVideos(
                    batchId = batchId,
                    subjectSlug = subjectSlug,
                    topicSlug = topicSlug
                )

                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<VideoItem>? =
                    gson.fromJson(decryptedData, Array<VideoItem>::class.java)?.toList()
                if (data == null) {
                    throw NullPointerException(nullErrorMsg)
                } else {
                    state = state.copy(items = data)
                    _allVideos.value = Response.Success(data)
                }
            } catch (e: SocketTimeoutException) {
                _allVideos.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allVideos.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allVideos.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshAllVideos(
        topicSlug: String,
        subjectSlug: String,
        batchId: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getAllVideos(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug,
            isRefresh = true,

            ) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _allNotesOld: MutableStateFlow<Response<List<NoteOldItem>>> =
        MutableStateFlow(Response.Loading())
    val notesOld = _allNotesOld.asStateFlow()

    fun getAllNotesOld(
        slug: String, isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allNotesOld.value = Response.Loading()
                }
                val response = repository.getAllNotesOld(slug)
                _allNotesOld.value = Response.Success(response ?: emptyList())
            } catch (e: SocketTimeoutException) {
                _allNotesOld.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allNotesOld.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allNotesOld.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllNotesOld(slug: String, onComplete: () -> Unit) {
        isRefreshing = true
        getAllNotesOld(slug = slug, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _allNotes: MutableStateFlow<Response<List<NoteItem>>> =
        MutableStateFlow(Response.Loading())
    val notes = _allNotes.asStateFlow()

    fun getAllNotes(
        topicSlug: String,
        subjectSlug: String,
        batchId: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allNotes.value = Response.Loading()
                }
                val response = repository.getAllNotes(
                    batchId = batchId,
                    subjectSlug = subjectSlug,
                    topicSlug = topicSlug,
                )

                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<NoteItem>? =
                    gson.fromJson(decryptedData, Array<NoteItem>::class.java)?.toList()
                if (data == null) {
                    throw NullPointerException(nullErrorMsg)
                } else {
                    _allNotes.value = Response.Success(data)
                }

            } catch (e: SocketTimeoutException) {
                _allNotes.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allNotes.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allNotes.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllNotes(
        topicSlug: String,
        subjectSlug: String,
        batchId: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getAllNotes(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug, isRefresh = true,
        ) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _allDppOld: MutableStateFlow<Response<List<DppOldItem>>> =
        MutableStateFlow(Response.Loading())
    val dppOld = _allDppOld.asStateFlow()

    fun getAllDppOld(
        slug: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allDppOld.value = Response.Loading()
                }
                val response = repository.getAllDppOld(slug)
                _allDppOld.value = Response.Success(response!!)
            } catch (e: SocketTimeoutException) {
                _allDppOld.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allDppOld.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allDppOld.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllDppOld(slug: String, onComplete: () -> Unit) {
        isRefreshing = true
        getAllDppOld(slug = slug, isRefresh = true) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _allDpp: MutableStateFlow<Response<List<DppItem>>> =
        MutableStateFlow(Response.Loading())
    val dpp = _allDpp.asStateFlow()

    fun getAllDpp(
        topicSlug: String,
        subjectSlug: String,
        batchId: String,
        isRefresh: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allDpp.value = Response.Loading()
                }
                val response = repository.getAllDpp(
                    batchId = batchId,
                    subjectSlug = subjectSlug,
                    topicSlug = topicSlug,
                )

                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<DppItem>? =
                    gson.fromJson(decryptedData, Array<DppItem>::class.java)?.toList()
                if (data == null) {
                    throw NullPointerException(nullErrorMsg)
                } else {
                    _allDpp.value = Response.Success(data)
                }

            } catch (e: SocketTimeoutException) {
                _allDpp.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allDpp.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allDpp.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshAllDpp(
        topicSlug: String,
        subjectSlug: String,
        batchId: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getAllDpp(
            batchId = batchId,
            subjectSlug = subjectSlug,
            topicSlug = topicSlug,
            isRefresh = true,
        ) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _allDppSolutionOld: MutableStateFlow<Response<List<DppSolutionOldItem>>?> =
        MutableStateFlow(Response.Loading())
    val dppSolutionOld = _allDppSolutionOld.asStateFlow()

    fun getAllDppSolutionOld(slug: String) {
        viewModelScope.launch {
            try {
                _allDppSolutionOld.value = Response.Loading()
                val response = repository.getAllDppSolutionOld(slug)
                _allDppSolutionOld.value = Response.Success(response!!)
            } catch (e: SocketTimeoutException) {
                _allDppSolutionOld.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allDppSolutionOld.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allDppSolutionOld.value =
                    Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    private val _allDppSolution: MutableStateFlow<Response<List<DppSolutionItem>>?> =
        MutableStateFlow(Response.Loading())
    val dppSolution = _allDppSolution.asStateFlow()

    fun getAllDppSolution(
        topicSlug: String,
        subjectSlug: String,
        batchId: String,
    ) {
        viewModelScope.launch {
            try {
                _allDppSolution.value = Response.Loading()
                val response = repository.getAllDppSolution(
                    batchId = batchId,
                    subjectSlug = subjectSlug,
                    topicSlug = topicSlug,
                )

                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<DppSolutionItem>? =
                    gson.fromJson(decryptedData, Array<DppSolutionItem>::class.java)?.toList()
                if (data == null) {
                    throw NullPointerException(nullErrorMsg)
                } else {
                    _allDppSolution.value = Response.Success(data)
                }

            } catch (e: SocketTimeoutException) {
                _allDppSolution.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allDppSolution.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _allDppSolution.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    private val _search: MutableStateFlow<ResponseTwo<List<SearchItem>>?> =
        MutableStateFlow(ResponseTwo.Loading())
    val search = _search.asStateFlow()

    fun getQuery(query: String) {
        viewModelScope.launch {
            try {
                _search.value = ResponseTwo.Loading()
                val response = repository.getQuery(query)
                _search.value = ResponseTwo.Success(response!!)
            } catch (e: SocketTimeoutException) {
                _search.value =
                    ResponseTwo.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _search.value = ResponseTwo.Error(noInternetMsg)
            } catch (e: Exception) {
                _search.value = ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    private val _searchOld: MutableStateFlow<ResponseTwo<List<SearchOldItem>>?> =
        MutableStateFlow(ResponseTwo.Loading())
    val searchOld = _searchOld.asStateFlow()

    fun getQueryOld(query: String) {
        viewModelScope.launch {
            try {
                _searchOld.value = ResponseTwo.Loading()
                val response = repository.getQueryOld(query)
                _searchOld.value = ResponseTwo.Success(response ?: emptyList())
            } catch (e: SocketTimeoutException) {
                _searchOld.value =
                    ResponseTwo.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _searchOld.value = ResponseTwo.Error(noInternetMsg)
            } catch (e: Exception) {
                _searchOld.value = ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    fun removeQueriesOld() {
        _searchOld.value = ResponseTwo.Nothing()
    }


    private val _downloads: MutableStateFlow<Response<DownloadItem>> =
        MutableStateFlow(Response.Loading())
    val downloads = _downloads.asStateFlow()

    fun getDownloadLinks() {
        viewModelScope.launch {
            try {
                _downloads.value = Response.Loading()
                val response = repository.getDownloadLinks()
                _downloads.value = Response.Success(response)
            } catch (e: Exception) {
                _downloads.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    private val _status: MutableStateFlow<Response<StatusItem>> =
        MutableStateFlow(Response.Loading())
    val status = _status.asStateFlow()

    fun getStatus() {
        viewModelScope.launch {
            try {
                _status.value = Response.Loading()
                val response = repository.getStatus()
                _status.value = Response.Success(response)
            } catch (e: SocketTimeoutException) {
                _status.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _status.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _status.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    fun removeQueries() {
        _search.value = ResponseTwo.Nothing()
    }


    private val _key: MutableStateFlow<Response<KeyGenerateEntity>> =
        MutableStateFlow(Response.Loading())
    val keyTask = _key.asStateFlow()

    fun getKeyTask() {
        /*runBlocking*/
        viewModelScope.launch {
            try {
                _key.value = Response.Loading()
                val response = repository.getKeyTask()
                if (response.visible != "1") {
                    _startDestination.value = NavRoutes.study
                }
                _key.value = Response.Success(response)
                _isLoading.value = false
            } catch (e: SocketTimeoutException) {
                _key.value =
                    Response.Error(socketErrorMsg)
                _isLoading.value = false
            } catch (e: UnknownHostException) {
                _key.value = Response.Error(noInternetMsg)
                _isLoading.value = false
            } catch (e: Exception) {
                _key.value = Response.Error(e.localizedMessage ?: "An error occurred.")
                _isLoading.value = false
            }
        }
    }

    fun onTaskCompleted() {
        viewModelScope.launch {
            val key = repository.getGenerateKeyById(1)
            if (key != null) {
                repository.deleteKeyGenerate(key)
            }
        }
    }


    fun insertKey(item: KeyEntity) {
        viewModelScope.launch {
            repository.insertKey(item)
        }
    }

    private val _isLoading: MutableState<Boolean> = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    private val _startDestination: MutableStateFlow<String> =
        MutableStateFlow(NavRoutes.keyGenerationScreen)
    val startDestination = _startDestination.asStateFlow()

    fun getStartDestination() {
        /*viewModelScope.launch*/
        runBlocking {
            val key = repository.getKey(1)
            if (key == null || key.timeToTriggerAt <= System.currentTimeMillis()) {
                _startDestination.value = NavRoutes.keyGenerationScreen
                if (key != null) {
                    repository.deleteKey(key)
                }
                getKeyTask()
            } else {
                _startDestination.value = NavRoutes.study
                _isLoading.value = false
            }
        }
    }

//    private val _navigate: MutableStateFlow<Boolean> = MutableStateFlow(false)
//    val navigate: StateFlow<Boolean> = _navigate.asStateFlow()
//
//
//    fun checkStartDestination() {
//        viewModelScope.launch {
//            Log.d("TAG", "checkStartDestination: chechking start destination now")
//            val key = repository.getKey(1)
//            if (key == null || key.timeToTriggerAt <= System.currentTimeMillis()) {
//                _navigate.value = true
//                Log.d("TAG", "checkStartDestination: key = $key")
//                if (key != null) {
//                    repository.deleteKey(key)
//                }
//                getKeyTask()
//            } else {
//                _navigate.value = false
//            }
//            Log.d("TAG", "checkStartDestination: ${_navigate.value}")
//        }
//    }


    fun checkStartDestinationDuringNavigation(onNavigate: () -> Unit) {
        viewModelScope.launch {
            val responseDeferred = async { repository.getKeyTask() }
            val response = responseDeferred.await()
            if (response.visible == "1") {
                val key = repository.getKey(1)
                if (key == null || key.timeToTriggerAt <= System.currentTimeMillis()) {
                    onNavigate()
                    if (key != null) {
                        repository.deleteKey(key)
                    }
                }

            }

        }
    }

//    Watch later

    private val _isSavedWatchLater: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isSavedWatchLater: StateFlow<Boolean> = _isSavedWatchLater.asStateFlow()


    fun checkIfWatchLaterSaved(videoId: String) {
        viewModelScope.launch {
            repository.checkIfPresentInWatchLaterFlow(videoId).collect {
                _isSavedWatchLater.value = it
            }
        }
    }

    fun onStarClick(item: WatchLaterEntity, context: Context) {
        viewModelScope.launch {
            if (!repository.checkIfPresentInWatchLater(item.videoId)) {
                repository.insertWatchLater(item)
                showToast(context, "${item.title} added Watch later")
            } else {
                repository.deleteWatchLaterByVideoId(item.videoId)
                showToast(context, "${item.title} removed from Watch later")
            }
        }
    }


    suspend fun checkIfPresentInWatchLater(videoId: String): Boolean {
        return repository.checkIfPresentInWatchLater(videoId)
    }

    private val _watchLater: MutableStateFlow<Response<List<WatchLaterEntity>>> =
        MutableStateFlow(Response.Loading())
    val watchLater = _watchLater.asStateFlow()

    val pwWatchLater = mutableListOf<WatchLaterEntity>()
    val khazanaWatchLater = mutableListOf<WatchLaterEntity>()


    fun getAllWatchLater() {
        viewModelScope.launch {
            try {
                _watchLater.value = Response.Loading()
                repository.getAllWatchLater().collect { list ->
                    _watchLater.value = Response.Success(list)

                    pwWatchLater.clear()
                    khazanaWatchLater.clear()

                    list.forEach { item ->
                        when {
                            item.isPw -> {
                                pwWatchLater.add(item)
                            }

                            item.isKhazana -> {
                                khazanaWatchLater.add(item)
                            }
                        }
                    }

                }
            } catch (e: Exception) {
                _watchLater.value = Response.Error(e.localizedMessage ?: "Error occurred")
            }

        }
    }

//    Timeline

    private val _timeline: MutableStateFlow<Response<List<TimelineItem>?>> =
        MutableStateFlow(Response.Loading())
    val timeline: StateFlow<Response<List<TimelineItem>?>> = _timeline.asStateFlow()

    fun getTimeline(externalId: String) {
        viewModelScope.launch {
            try {
                _timeline.value = Response.Loading()
                val response = repository.getTimeline(externalId)
                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<TimelineItem>? =
                    gson.fromJson(decryptedData, Array<TimelineItem>::class.java)?.toList()
                if (data == null) {
                    throw NullPointerException(nullErrorMsg)
                } else {
                    _timeline.value = Response.Success(data)
                }
            } catch (e: Exception) {
                _timeline.value = Response.Error(e.localizedMessage ?: "An error occurred")
            }
        }
    }

//    Announcements


    private val _announcements: MutableStateFlow<Response<List<AnnouncementItem>?>> =
        MutableStateFlow(Response.Loading())
    val announcements = _announcements.asStateFlow()

    fun getAnnouncements(batchId: String, isOld: Boolean) {
        viewModelScope.launch {
            try {
                _announcements.value = Response.Loading()
                if (isOld) {
                    val response = repository.getAnnouncements(batchId)
                    _announcements.value = Response.Success(response)
                } else {
                    val response = repository.getAnnouncementsNew(batchId)

                    val decodedData = Base64.getDecoder().decode(response)
                    val decryptedData = decrypt(decodedData, key, iv)
                    val data: List<AnnouncementItem>? =
                        gson.fromJson(decryptedData, Array<AnnouncementItem>::class.java)?.toList()
                    if (data == null) {
                        throw NullPointerException(nullErrorMsg)
                    } else {
                        _announcements.value = Response.Success(data)
                    }
                }
            } catch (e: SocketTimeoutException) {
                _announcements.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _announcements.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _announcements.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No app found to handle this link", Toast.LENGTH_SHORT).show()
        }
    }

    fun openYouTubeVideo(context: Context, videoUrl: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)).apply {
            // This line ensures that the intent opens in the YouTube app if it's available.
            setPackage("com.google.android.youtube")
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            // Fallback to the browser if YouTube app is not available
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(videoUrl)))
        }
    }

    fun openTelegram(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage("org.telegram.messenger")
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            openUrl(context, url)
        }
    }

    fun openGmail(mail: String, context: Context, subject: String, text: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(mail))
            putExtra(Intent.EXTRA_TEXT, text)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No email app found on phone", Toast.LENGTH_SHORT).show()
        }
    }

    fun shareApp(context: Context, subject: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, subject)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No app found to share", Toast.LENGTH_SHORT).show()
        }
    }


//    Internet state

    private val _isInternetAccessible: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isInternetAccessible = _isInternetAccessible.asStateFlow()

    fun observeInternetAccessibility() {
        viewModelScope.launch {
            networkManager.observeNetworkState().collect {
                _isInternetAccessible.value = it
            }
        }
    }


    fun showSnackBar(snackbarHostState: SnackbarHostState) {
        viewModelScope.launch {
            snackbarHostState.showSnackbar(message = snackBarMsg, withDismissAction = true)
        }
    }

    object Keys {

        init {
            System.loadLibrary("native-lib")
        }

        external fun kkk(): String
        external fun ivv(): String
    }


}
