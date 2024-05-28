package com.xerox.studyrays.network

import com.xerox.studyrays.model.akModel.aKVideoU.AkUrl
import com.xerox.studyrays.model.pwModel.CourseItem
import com.xerox.studyrays.model.pwModel.DppItem
import com.xerox.studyrays.model.pwModel.DppSolutionItem
import com.xerox.studyrays.model.pwModel.LessonItem
import com.xerox.studyrays.model.pwModel.NoteItem
import com.xerox.studyrays.model.pwModel.PromoItem
import com.xerox.studyrays.model.pwModel.SearchItem
import com.xerox.studyrays.model.pwModel.alertItem.AlertItem
import com.xerox.studyrays.model.pwModel.batchDetails.BatchDetails
import com.xerox.studyrays.model.khazanaModel.khazana.KhazanaItem
import com.xerox.studyrays.model.pwModel.navbar.NavItem
import com.xerox.studyrays.model.pwModel.priceItem.PriceItem
import com.xerox.studyrays.utils.Constants.AK_VIDEO_URL
import com.xerox.studyrays.utils.Constants.ALERT_URL
import com.xerox.studyrays.utils.Constants.DPP_GET_URL
import com.xerox.studyrays.utils.Constants.DPP_SOLUTION_GET_URL
import com.xerox.studyrays.utils.Constants.GET_URL
import com.xerox.studyrays.utils.Constants.KHAZANA_URL
import com.xerox.studyrays.utils.Constants.LESSON_GET_URL
import com.xerox.studyrays.utils.Constants.NAV_URL
import com.xerox.studyrays.utils.Constants.NOTES_GET_URL
import com.xerox.studyrays.utils.Constants.PROMO_URL
import com.xerox.studyrays.utils.Constants.SEARCH_URL
import com.xerox.studyrays.utils.Constants.SUBJECT_GET_URL
import com.xerox.studyrays.utils.Constants.TOTAL_FEE_URL
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(GET_URL)
    suspend fun getAllCourses(@Query("class") classValue: String): List<CourseItem>?

    @GET(SUBJECT_GET_URL)
    suspend fun getAllSubjects(@Query("course_id") courseId: String): BatchDetails

    @GET(LESSON_GET_URL)
    suspend fun getALlLessons(@Query("subject_id") subjectId: String): List<LessonItem>?

    @GET(NOTES_GET_URL)
    suspend fun getAllNotes(@Query("slug") slug: String): List<NoteItem>?

    @GET(DPP_GET_URL)
    suspend fun getAllDpp(@Query("slug") slug: String): List<DppItem>?

    @GET(DPP_SOLUTION_GET_URL)
    suspend fun getAllDppSolution(@Query("slug") slug: String): List<DppSolutionItem>?

    @GET(PROMO_URL)
    suspend fun getAllPromoItems(): List<PromoItem?>?

    @GET(SEARCH_URL)
    suspend fun getQuery(@Query("q") query: String): List<SearchItem>?

    @GET(KHAZANA_URL)
    suspend fun getKhazana(): List<KhazanaItem>

    @GET(NAV_URL)
    suspend fun getNavItems(): NavItem

    @GET(ALERT_URL)
    suspend fun getAlertItem(): AlertItem

    @GET(TOTAL_FEE_URL)
    suspend fun getTotalFee(): PriceItem

    @GET(AK_VIDEO_URL)
    suspend fun getAkVideoU(
        @Query("key") key: Int,
        @Query("lid") lid: String,
        @Query("bid") bid: Int,
        @Query("tid") tid: Int,
        @Query("sid") sid: Int,

    ): AkUrl


}