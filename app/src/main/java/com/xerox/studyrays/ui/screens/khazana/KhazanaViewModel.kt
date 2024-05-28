package com.xerox.studyrays.ui.screens.khazana

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDb.KhazanaEntity
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFav
import com.xerox.studyrays.model.khazanaModel.khazanaChapters.KhazanaChaptersItem
import com.xerox.studyrays.model.khazanaModel.khazanaLectures.dpp.KhazanaDpp
import com.xerox.studyrays.model.khazanaModel.khazanaLectures.notes.KhazanaNotesItem
import com.xerox.studyrays.model.khazanaModel.khazanaLectures.solution.KhazanaSolution
import com.xerox.studyrays.model.khazanaModel.khazanaLectures.videos.KhazanaVideoItem
import com.xerox.studyrays.model.khazanaModel.khazanaSubject.KhazanaSubjectItem
import com.xerox.studyrays.model.khazanaModel.khazanaTeachersItem.KhazanaTeacherItem
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.network.ResponseTwo
import com.xerox.studyrays.ui.screens.MainViewModel
import com.xerox.studyrays.utils.decrypt
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class KhazanaViewModel @Inject constructor(
    private val repository: ApiRepository,
) : ViewModel() {

    private val key = MainViewModel.Keys.kkk()
    private val iv = MainViewModel.Keys.ivv()
    private val gson = Gson()

    private val noInternetMsg = "Internet Unavailable."
    private val socketErrorMsg = "Socket timeout: Either slow internet or server issue."

    val refreshMsg = "Refreshed Successfully!"


    private val _khazana: MutableStateFlow<ResponseTwo<List<KhazanaEntity>>> =
        MutableStateFlow(ResponseTwo.Loading())
    val khazana = _khazana.asStateFlow()
    var isRefreshing by mutableStateOf(false)

    fun getKhazana(
        isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _khazana.value = ResponseTwo.Loading()
                }
                val response =
                    if (fromApi) repository.getKhazanaFromApi() else repository.getKhazana()
                _khazana.value = ResponseTwo.Success(response)
            } catch (e: SocketTimeoutException) {
                _khazana.value =
                    ResponseTwo.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _khazana.value = ResponseTwo.Error(noInternetMsg)
            } catch (e: Exception) {
                _khazana.value = ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    fun refreshKhazana(onComplete: () -> Unit) {
        isRefreshing = true
        getKhazana(isRefresh = true, fromApi = true, onComplete = {
            isRefreshing = false
            onComplete()
        })
    }


    private val _khazanaVideo: MutableStateFlow<Response<List<KhazanaVideoItem>>> =
        MutableStateFlow(Response.Loading())
    val khazanaVideo = _khazanaVideo.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getKhazanaVideos(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
        isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _khazanaVideo.value = Response.Loading()
                }
                val response = if (fromApi) repository.getKhazanaVideosFromApi(
                    subjectId = subjectId,
                    chapterId = chapterId,
                    topicId = topicId,
                    topicName = topicName
                )
                else repository.getKhazanaVideos(
                    subjectId,
                    chapterId,
                    topicId,
                    topicName = topicName
                )
                val decodedData = Base64.getDecoder().decode(response.response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<KhazanaVideoItem> =
                    gson.fromJson(decryptedData, Array<KhazanaVideoItem>::class.java).toList()
                _khazanaVideo.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _khazanaVideo.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _khazanaVideo.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _khazanaVideo.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshKhazanaVideos(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getKhazanaVideos(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId,
            topicName = topicName,
            isRefresh = true,
            fromApi = true,
            onComplete = {
                isRefreshing = false
                onComplete()
            })
    }

//    khazana notes

    private val _khazanaNotes: MutableStateFlow<Response<List<KhazanaNotesItem?>?>> =
        MutableStateFlow(Response.Loading())
    val khazanaNotes = _khazanaNotes.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getKhazanaNotes(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
        isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _khazanaNotes.value = Response.Loading()
                }
                val response =
                    if (fromApi) repository.getKhazanaNotesFromApi(subjectId = subjectId, chapterId = chapterId, topicId = topicId, topicName = topicName)
                    else repository.getKhazanaNotes(subjectId = subjectId, chapterId = chapterId, topicId = topicId, topicName = topicName)
                val decodedData = Base64.getDecoder().decode(response.response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<KhazanaNotesItem> =
                    gson.fromJson(decryptedData, Array<KhazanaNotesItem>::class.java).toList()
                _khazanaNotes.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _khazanaNotes.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _khazanaNotes.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _khazanaNotes.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshKhazanaNotes(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getKhazanaNotes(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId,
            isRefresh = true,
            topicName = topicName,
            fromApi = true,
            onComplete = {
                isRefreshing = false
                onComplete()
            }
        )

    }

//    Khazana dpp

    private val _khazanaDpp: MutableStateFlow<Response<KhazanaDpp?>?> =
        MutableStateFlow(Response.Loading())
    val khazanaDpp = _khazanaDpp.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getKhazanaDpp(
        subjectId: String, chapterId: String, topicId: String,topicName: String,
        isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _khazanaDpp.value = Response.Loading()
                }
                val response =
                    if (fromApi) repository.getKhazanaDppFromApi(subjectId, chapterId, topicId, topicName = topicName)
                    else repository.getKhazanaDpp(subjectId, chapterId, topicId, topicName = topicName)
                val decodedData = Base64.getDecoder().decode(response.response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: KhazanaDpp =
                    gson.fromJson(decryptedData, KhazanaDpp::class.java)
                _khazanaDpp.value = Response.Success(data)

            } catch (e: SocketTimeoutException) {
                _khazanaDpp.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _khazanaDpp.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _khazanaDpp.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshKhazanaDpp(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getKhazanaDpp(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId,
            isRefresh = true,
            fromApi = true,
            topicName = topicName,
            onComplete = {
                isRefreshing = false
                onComplete()
            }
        )

    }

//    khazana solutions

    private val _khazanaSolutions: MutableStateFlow<Response<KhazanaSolution>> =
        MutableStateFlow(Response.Loading())
    val khazanaSolution = _khazanaSolutions.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getKhazanaSolution(
        subjectId: String, chapterId: String, topicId: String,topicName: String,
        isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _khazanaSolutions.value = Response.Loading()
                }
                val response =
                    if (fromApi) repository.getKhazanaSolutionFromApi(subjectId, chapterId, topicId,topicName = topicName)
                    else repository.getKhazanaSolution(subjectId, chapterId, topicId,topicName = topicName)
                val decodedData = Base64.getDecoder().decode(response.response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: KhazanaSolution =
                    gson.fromJson(decryptedData, KhazanaSolution::class.java)
                _khazanaSolutions.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _khazanaSolutions.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _khazanaSolutions.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _khazanaSolutions.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshKhazanaSolutionDpp(
        subjectId: String,
        chapterId: String,
        topicId: String,
        topicName: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getKhazanaSolution(
            subjectId = subjectId,
            chapterId = chapterId,
            topicId = topicId,
            isRefresh = true,
            fromApi = true,
            topicName = topicName,
            onComplete = {
                isRefreshing = false
                onComplete()
            }
        )

    }

//    Khazana teachers

    private val _khazanaTeachers: MutableStateFlow<Response<List<KhazanaTeacherItem>>> =
        MutableStateFlow(Response.Loading())
    val khazanaTeachers = _khazanaTeachers.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getKhazanaTeachers(
        id: String,
        isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _khazanaTeachers.value = Response.Loading()
                }
                val response = if (fromApi) repository.getKhazanaTeachersFromApi(id)
                else repository.getKhazanaTeachers(id)
                val decodedData = Base64.getDecoder().decode(response.response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<KhazanaTeacherItem> =
                    gson.fromJson(decryptedData, Array<KhazanaTeacherItem>::class.java).toList()
                _khazanaTeachers.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _khazanaTeachers.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _khazanaTeachers.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _khazanaTeachers.value = Response.Error(e.localizedMessage!!)
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshKhazanaTeachers(id: String, onComplete: () -> Unit) {
        isRefreshing = true
        getKhazanaTeachers(id = id, isRefresh = true, fromApi = true, onComplete = {
            isRefreshing = false
            onComplete()
        })
    }

//    Khazana fav courses

    private val _allKhazanaFavSubjects: MutableStateFlow<ResponseTwo<List<KhazanaFav?>>> =
        MutableStateFlow(ResponseTwo.Nothing())
    val khazanaFavSubjects = _allKhazanaFavSubjects.asStateFlow()

    val favCourseList = mutableListOf<KhazanaFav?>(null)

    fun getAllFavSubjects() {
        viewModelScope.launch {
            favCourseList.clear()
            try {
                _allKhazanaFavSubjects.value = ResponseTwo.Loading()
                repository.getAllKhazanaCourses()
                    .onStart {
                        _allKhazanaFavSubjects.value = ResponseTwo.Loading()
                    }
                    .catch { e ->
                        _allKhazanaFavSubjects.value =
                            ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
                    }
                    .onCompletion { cause ->
                        if (cause == null) {
                            _allKhazanaFavSubjects.value =
                                ResponseTwo.Success(favCourseList.toList())
                        } else {
                            _allKhazanaFavSubjects.value =
                                ResponseTwo.Error(cause.localizedMessage ?: "An error occurred.")
                        }
                    }
                    .collectLatest { response ->
                        if (response.isNullOrEmpty()) {
                            _allKhazanaFavSubjects.value = ResponseTwo.Nothing()
                            return@collectLatest
                        } else {
                            response.forEach { course ->
                                favCourseList.add(course)
                            }
                            _allKhazanaFavSubjects.value =
                                ResponseTwo.Success(favCourseList.toList())
                        }
                    }

            } catch (e: Exception) {
                _allKhazanaFavSubjects.value =
                    ResponseTwo.Error(e.localizedMessage ?: "An error occurred.")
            }
        }
    }

    fun onFavoriteClick(item: KhazanaFav) {
        viewModelScope.launch {
            if (repository.checkIfPresentInKhazanaDb(item.externalId)) {
                repository.deleteKhazanaFav(item)
            } else {
                repository.insertKhazanaFav(item)
            }
        }
    }


    private val _allFavouriteCourses: MutableStateFlow<List<KhazanaFav?>?> =
        MutableStateFlow(emptyList())
    val allFavCourses = _allFavouriteCourses.asStateFlow()

    fun getAllFavCourses() {
        viewModelScope.launch {
            repository.getAllKhazanaCourses().collect {
                _allFavouriteCourses.value = it
            }
        }
    }

    suspend fun checkIfItemIsPresentInDb(externalId: String): Boolean {
        return repository.checkIfPresentInKhazanaDb(externalId)
    }
//  khazana subjects

    private val _khazanaSubject: MutableStateFlow<Response<List<KhazanaSubjectItem>>> =
        MutableStateFlow(Response.Loading())
    val khazanaSubject = _khazanaSubject.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getKhazanaSubjects(
        slug: String,
        isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!isRefresh) {
                    _khazanaSubject.value = Response.Loading()
                }
                val response = if (fromApi) repository.getKhazanaSubjectsFromApi(slug)
                else repository.getKhazanaSubjects(slug)
                val decodedData = Base64.getDecoder().decode(response.response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<KhazanaSubjectItem> =
                    gson.fromJson(decryptedData, Array<KhazanaSubjectItem>::class.java).toList()
                _khazanaSubject.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _khazanaSubject.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _khazanaSubject.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _khazanaSubject.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshKhazanaSubjects(slug: String, onComplete: () -> Unit) {
        isRefreshing = true
        getKhazanaSubjects(slug = slug, isRefresh = true, fromApi = true, onComplete = {
            isRefreshing = false
            onComplete()
        })
    }

//    Khazana chapters

    private val _khazanaChapters: MutableStateFlow<Response<List<KhazanaChaptersItem>>> =
        MutableStateFlow(Response.Loading())
    val khazanaChapters = _khazanaChapters.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun getKhazanaChapters(
        subjectId: String,
        chapterId: String,
        isRefresh: Boolean = false,
        fromApi: Boolean = false,
        onComplete: (() -> Unit)? = null,
    ) {
        viewModelScope.launch {
            try {
                if (!isRefresh) {
                    _khazanaChapters.value = Response.Loading()
                }
                val response =
                    if (fromApi) repository.getKhazanaChaptersFromApi(subjectId, chapterId)
                    else repository.getKhazanaChapters(subjectId, chapterId)
                val decodedData = Base64.getDecoder().decode(response.response)
                val decryptedData = decrypt(decodedData, key, iv)
                val data: List<KhazanaChaptersItem> =
                    gson.fromJson(decryptedData, Array<KhazanaChaptersItem>::class.java).toList()
                _khazanaChapters.value = Response.Success(data)
            } catch (e: SocketTimeoutException) {
                _khazanaChapters.value =
                    Response.Error(socketErrorMsg)
            } catch (e: UnknownHostException) {
                _khazanaChapters.value = Response.Error(noInternetMsg)
            } catch (e: Exception) {
                _khazanaChapters.value = Response.Error(e.localizedMessage ?: "An error occurred.")
            } finally {
                if (isRefresh) {
                    isRefreshing = false
                }
                onComplete?.invoke()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun refreshKhazanaChapters(
        subjectId: String,
        chapterId: String,
        onComplete: () -> Unit,
    ) {
        isRefreshing = true
        getKhazanaChapters(
            subjectId = subjectId,
            chapterId = chapterId,
            isRefresh = true,
            fromApi = true,
            onComplete = {
                isRefreshing = false
                onComplete()
            })
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(snackBarHostState: SnackbarHostState) {
        viewModelScope.launch {
            snackBarHostState.showSnackbar(message = refreshMsg, withDismissAction = true)
        }
    }
}