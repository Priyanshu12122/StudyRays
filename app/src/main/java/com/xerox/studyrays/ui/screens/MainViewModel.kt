package com.xerox.studyrays.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.priceDb.PriceEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.promoDb.PromoEntity
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchEntity
import com.xerox.studyrays.cacheDb.pwCache.courseDb.PwCourseEntity
import com.xerox.studyrays.cacheDb.pwCache.dppDb.PwDppEntity
import com.xerox.studyrays.cacheDb.pwCache.lessonDb.PwLessonEntity
import com.xerox.studyrays.cacheDb.pwCache.notesDb.PwNotesEntity
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.data.internetAvailability.NetworkManager
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourse
import com.xerox.studyrays.db.videoDb.Video
import com.xerox.studyrays.model.pwModel.DppItem
import com.xerox.studyrays.model.pwModel.DppSolutionItem
import com.xerox.studyrays.model.pwModel.NoteItem
import com.xerox.studyrays.model.pwModel.SearchItem
import com.xerox.studyrays.model.pwModel.VideoItem
import com.xerox.studyrays.model.pwModel.alertItem.AlertItem
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetails
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetailsX
import com.xerox.studyrays.model.pwModel.batchDetails.Subject
import com.xerox.studyrays.model.pwModel.batchDetails.TeacherId
import com.xerox.studyrays.model.pwModel.comments.CommentsItem
import com.xerox.studyrays.model.pwModel.navbar.NavItem
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.utils.decrypt
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val noInternetMsg = "Internet Unavailable."
    private val socketErrorMsg = "Socket timeout: Either slow internet or server issue."

    private val snackBarMsg = "Refreshed Successfully!"
    var isRefreshing by mutableStateOf(false)

    private val networkManager = NetworkManager(context)

    fun insertSearchItem(item: SearchEntity){
        viewModelScope.launch {
            val exists = repository.exists(item.searchText)
            if (!exists){
                repository.insertSearchItem(item)
            }
        }
    }

    fun deleteSearchItem(item: SearchEntity){
        viewModelScope.launch {
            repository.deleteSearchItem(item)
        }
    }

    private val _cacheSearch: MutableStateFlow<List<SearchEntity>?> = MutableStateFlow(emptyList())
    val cacheSearch = _cacheSearch.asStateFlow()

    fun getAllSearchItem(){
        viewModelScope.launch {
            repository.getAllSearchItems().collect {
                _cacheSearch.value = it
            }
        }
    }


    private val _totalFee: MutableStateFlow<Response<PriceEntity>> =
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

    private val _comments: MutableStateFlow<Response<List<CommentsItem>>> =
        MutableStateFlow(Response.Loading())
    val comments = _comments.asStateFlow()

    @SuppressLint("NewApi")
    fun getComments(
        bname: String,
        commentKey: String,
        page: Int,
    ) {

        viewModelScope.launch {
            try {
                _comments.value = Response.Loading()
                val response = repository.getComments(bname, commentKey, page)
                val decodedData = Base64.getDecoder().decode(response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<CommentsItem> =
                    gson.fromJson(decryptedData, Array<CommentsItem>::class.java).toList()
                _comments.value = Response.Success(data)
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

    private val _allFavouriteCourses: MutableStateFlow<List<FavouriteCourse?>?> =
        MutableStateFlow(emptyList())
    val allFavCourses = _allFavouriteCourses.asStateFlow()

    fun getAllFavCourses() {
        viewModelScope.launch {
            repository.getAllCourses().collect {
                _allFavouriteCourses.value = it
            }
        }
    }

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

    suspend fun checkIfItemIsPresentInDb(item: FavouriteCourse): Boolean {
        return repository.checkIfPresent(item.externalId)
    }

    suspend fun checkIfItemIsPresentInVideoDb(item: Video): Boolean {
        return repository.checkIfPresentVideoDB(item.videoId)
    }

    fun onMarkAsCompleteClicked(
        item: Video,
    ) {
        viewModelScope.launch {
            if (repository.checkIfPresentVideoDB(item.videoId)) {
                repository.deleteVideo(item)
            } else {
                repository.insertVideo(item)
            }
        }
    }


    private val _allCourses: MutableStateFlow<Response<List<PwCourseEntity>>> =
        MutableStateFlow(Response.Loading())
    val courses = _allCourses.asStateFlow()


    val neetCourses = mutableListOf<PwCourseEntity>()
    val jeeCourses = mutableListOf<PwCourseEntity>()
    val boardCourses = mutableListOf<PwCourseEntity>()
    val vidyapeethCourses = mutableListOf<PwCourseEntity>()
    val freeCourses = mutableListOf<PwCourseEntity>()
    val youtubeCourses = mutableListOf<PwCourseEntity>()
    val otherCourses = mutableListOf<PwCourseEntity>()


    fun getClasses(
        classValue: String, isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allCourses.value = Response.Loading()
                }
                val response = if (fromApi) repository.getAllCoursesFromApi(classValue)
                else repository.getAllCourses(classValue)
                response?.forEach {

                    val name = it.name.lowercase()
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

    fun refreshAllCourses(classValue: String, onComplete: () -> Unit) {
        isRefreshing = true
        getClasses(classValue = classValue, isRefresh = true, fromApi = true) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _allSubjects: MutableStateFlow<ResponseTwo<BatchDetails>> =
        MutableStateFlow(ResponseTwo.Loading())
    val subjects = _allSubjects.asStateFlow()

    val subjectsList = mutableListOf<Subject?>()
    val teachersList = mutableListOf<TeacherId?>()

    fun getAllSubjects(courseId: String) {
        viewModelScope.launch {
            try {
                _allSubjects.value = ResponseTwo.Loading()
                val response: BatchDetails = repository.getAllSubjects(courseId)
                if (response.subjects.isEmpty()) {
                    _allSubjects.value = ResponseTwo.Nothing()
                    return@launch
                }
                subjectsList.clear()
                teachersList.clear()
                response.subjects.forEach {
                    subjectsList.add(it)

                    it.teacherIds.forEach { teacherId ->
                        teachersList.add(teacherId)
                    }

                }
                _allSubjects.value = ResponseTwo.Success(response)
            } catch (e: SocketTimeoutException) {
                _allSubjects.value =
                    ResponseTwo.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allSubjects.value = ResponseTwo.Error(noInternetMsg)
            } catch (e: Exception) {
                _allSubjects.value = ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    private val _allFavSubjects: MutableStateFlow<ResponseTwo<BatchDetails>> =
        MutableStateFlow(ResponseTwo.Nothing())
    val favSubjects = _allFavSubjects.asStateFlow()

    var favCourseList = mutableListOf<BatchDetailsX?>(null)

    fun getAllFavSubjects(courseId: String) {
        viewModelScope.launch {

            favCourseList.clear()
            try {
                _allFavSubjects.value = ResponseTwo.Loading()
                val response = repository.getAllSubjects(courseId)
                if (response.subjects.isEmpty()) {
                    _allSubjects.value = ResponseTwo.Nothing()
                    return@launch
                }
                favCourseList.add(response.batch_details)
                _allFavSubjects.value = ResponseTwo.Success(response)
            } catch (e: SocketTimeoutException) {
                _allFavSubjects.value =
                    ResponseTwo.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _allFavSubjects.value = ResponseTwo.Error(noInternetMsg)
            } catch (e: Exception) {
                _allFavSubjects.value =
                    ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }


    //    Promo
    private val _promo: MutableStateFlow<Response<List<PromoEntity?>?>> =
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


    private val _allLessons: MutableStateFlow<Response<List<PwLessonEntity>>> =
        MutableStateFlow(Response.Loading())
    val lessons = _allLessons.asStateFlow()

    fun getAllLessons(
        subjectId: String, isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allLessons.value = Response.Loading()
                }
                val response = if (fromApi) repository.getAllLessonsFromApi(subjectId)
                else repository.getAllLessons(subjectId)
                _allLessons.value = Response.Success(response!!)
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

    fun refreshAllLessons(subjectId: String, onComplete: () -> Unit) {
        isRefreshing = true
        getAllLessons(subjectId = subjectId, isRefresh = true, fromApi = true) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _allVideos: MutableStateFlow<Response<List<VideoItem>>> =
        MutableStateFlow(Response.Loading())
    val videos = _allVideos.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getAllVideos(
        slug: String, isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _allVideos.value = Response.Loading()
                }
                val response = if (fromApi) repository.getAllVideosFromApi(slug)
                else repository.getAllVideos(slug)
                val decodedData = Base64.getDecoder().decode(response.response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<VideoItem> =
                    gson.fromJson(decryptedData, Array<VideoItem>::class.java).toList()
                _allVideos.value = Response.Success(data)
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
    fun refreshAllVideos(slug: String, onComplete: () -> Unit) {
        isRefreshing = true
        getAllVideos(slug = slug, isRefresh = true, fromApi = true) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _allNotes: MutableStateFlow<Response<List<PwNotesEntity>>> =
        MutableStateFlow(Response.Loading())
    val notes = _allNotes.asStateFlow()

    fun getAllNotes(slug: String, isRefresh: Boolean = false,
                    fromApi: Boolean = false,
                    onComplete: (() -> Unit)? = null,) {
        viewModelScope.launch {
            try {
                if (!isRefresh){
                    _allNotes.value = Response.Loading()
                }
                val response = if (fromApi) repository.getAllNotesFromApi(slug)
                else repository.getAllNotes(slug)
                _allNotes.value = Response.Success(response ?: emptyList())
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

    fun refreshAllNotes(slug: String, onComplete: () -> Unit) {
        isRefreshing = true
        getAllNotes(slug = slug, isRefresh = true, fromApi = true) {
            isRefreshing = false
            onComplete()
        }
    }

    private val _allDpp: MutableStateFlow<Response<List<PwDppEntity>>> =
        MutableStateFlow(Response.Loading())
    val dpp = _allDpp.asStateFlow()

    fun getAllDpp(slug: String, isRefresh: Boolean = false,
                  fromApi: Boolean = false,
                  onComplete: (() -> Unit)? = null,) {
        viewModelScope.launch {
            try {
                if (!isRefresh){
                    _allDpp.value = Response.Loading()
                }
                val response = if (fromApi) repository.getAllDppFromApi(slug)
                else repository.getAllDpp(slug)
                _allDpp.value = Response.Success(response!!)
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

    fun refreshAllDpp(slug: String, onComplete: () -> Unit) {
        isRefreshing = true
        getAllDpp(slug = slug, isRefresh = true, fromApi = true) {
            isRefreshing = false
            onComplete()
        }
    }


    private val _allDppSolution: MutableStateFlow<Response<List<DppSolutionItem>>?> =
        MutableStateFlow(Response.Loading())
    val dppSolution = _allDppSolution.asStateFlow()

    fun getAllDppSolution(slug: String) {
        viewModelScope.launch {
            try {
                _allDppSolution.value = Response.Loading()
                val response = repository.getAllDppSolution(slug)
                _allDppSolution.value = Response.Success(response!!)
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


    fun removeQueries() {
        _search.value = ResponseTwo.Nothing()
    }

    fun openUrl(context: Context, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "No app found to handle this link", Toast.LENGTH_SHORT).show()
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

    private val _isInternetAccessible: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isInternetAccessible = _isInternetAccessible.asStateFlow()

    fun observeInternetAccessibility() {
        viewModelScope.launch {
            networkManager.observeNetworkState().collect(_isInternetAccessible)
        }
    }

    fun getAllExampleItems() {
        viewModelScope.launch {
            repository.getAllExamples().forEach {
                Log.d("TAG", "Msg = ${it.msg} , time = ${it.time}")
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
