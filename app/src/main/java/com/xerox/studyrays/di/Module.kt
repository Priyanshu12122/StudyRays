package com.xerox.studyrays.di

import android.content.Context
import androidx.room.Room
import com.xerox.studyrays.cacheDb.akCache.NotesDb.AkNoteDao
import com.xerox.studyrays.cacheDb.akCache.NotesDb.AkNoteDb
import com.xerox.studyrays.cacheDb.akCache.indexDb.IndexDao
import com.xerox.studyrays.cacheDb.akCache.indexDb.IndexDb
import com.xerox.studyrays.cacheDb.akCache.lessonDb.AkLessonDao
import com.xerox.studyrays.cacheDb.akCache.lessonDb.AkLessonDb
import com.xerox.studyrays.cacheDb.akCache.subjectsDb.AkSubjectDao
import com.xerox.studyrays.cacheDb.akCache.subjectsDb.AkSubjectDb
import com.xerox.studyrays.cacheDb.akCache.videosDb.AkVideoDao
import com.xerox.studyrays.cacheDb.akCache.videosDb.AkVideoDb
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaChaptersDb.KhazanaChaptersDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaChaptersDb.KhazanaChaptersDb
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDb.KhazanaDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDb.KhazanaDb
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppDb.KhazanaDppDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppDb.KhazanaDppDb
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppLecturesDb.KhazanaDppLecturesDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaDppLecturesDb.KhazanaDppLecturesDb
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaLecturesDb.KhazanaLecturesDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaLecturesDb.KhazanaLecturesDb
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaNotesDb.KhazanaNotesDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaNotesDb.KhazanaNotesDb
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaSubjectsDb.KhazanaSubjectDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaSubjectsDb.KhazanaSubjectDb
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaTeachersDb.KhazanaTeachersDao
import com.xerox.studyrays.cacheDb.khazanaCache.khazanaTeachersDb.KhazanaTeachersDb
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavDao
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavDb
import com.xerox.studyrays.cacheDb.mainScreenCache.priceDb.PriceDao
import com.xerox.studyrays.cacheDb.mainScreenCache.priceDb.PriceDb
import com.xerox.studyrays.cacheDb.mainScreenCache.promoDb.PromoDao
import com.xerox.studyrays.cacheDb.mainScreenCache.promoDb.PromoDb
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchDao
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchDb
import com.xerox.studyrays.cacheDb.pwCache.courseDb.PwCourseDao
import com.xerox.studyrays.cacheDb.pwCache.courseDb.PwCourseDb
import com.xerox.studyrays.cacheDb.pwCache.dppDb.PwDppDao
import com.xerox.studyrays.cacheDb.pwCache.dppDb.PwDppDb
import com.xerox.studyrays.cacheDb.pwCache.lessonDb.PwLessonDao
import com.xerox.studyrays.cacheDb.pwCache.lessonDb.PwLessonDb
import com.xerox.studyrays.cacheDb.pwCache.notesDb.PwNotesDao
import com.xerox.studyrays.cacheDb.pwCache.notesDb.PwNotesDb
import com.xerox.studyrays.cacheDb.pwCache.videosDb.PwVideoDao
import com.xerox.studyrays.cacheDb.pwCache.videosDb.PwVideoDb
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.db.exampleDb.ExampleDao
import com.xerox.studyrays.db.exampleDb.ExampleDb
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourseDao
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourseDb
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFavDao
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFavDb
import com.xerox.studyrays.db.videoDb.VideoDao
import com.xerox.studyrays.db.videoDb.VideoDatabase
import com.xerox.studyrays.network.ApiService
import com.xerox.studyrays.network.EncryptedApiService
import com.xerox.studyrays.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Provides
    @Singleton
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): ApiService {
        return retrofit
            .newBuilder()
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesEncryptedApiService(retrofit: Retrofit): EncryptedApiService {
        return retrofit
            .newBuilder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(EncryptedApiService::class.java)
    }

    @Provides
    @Singleton
    fun providesRepository(
        api: ApiService,
        dao: FavouriteCourseDao,
        encryptedApiService: EncryptedApiService,
        videoDao: VideoDao,
        khazanaDao: KhazanaFavDao,
        priceDao: PriceDao,
        exampleDao: ExampleDao,
        promoDao: PromoDao,
        indexDao: IndexDao,
        khazanaCacheDao: KhazanaDao,
        khazanaSubjectDao: KhazanaSubjectDao,
        khazanaTeachersDao: KhazanaTeachersDao,
        khazanaChaptersDao: KhazanaChaptersDao,
        khazanaLecturesDao: KhazanaLecturesDao,
        khazanaNotesDao: KhazanaNotesDao,
        khazanaDppDao: KhazanaDppDao,
        khazanaDppLecturesDao: KhazanaDppLecturesDao,
        akSubjectDao: AkSubjectDao,
        akLessonDao: AkLessonDao,
        akVideoDao: AkVideoDao,
        akNoteDao: AkNoteDao,
        pwCourseDao: PwCourseDao,
        pwLessonDao: PwLessonDao,
        pwVideoDao: PwVideoDao,
        pwNotesDao: PwNotesDao,
        pwDppDao: PwDppDao,
        navDao: NavDao,
        searchDao: SearchDao,
    ): ApiRepository {
        return ApiRepository(
            api = api,
            dao = dao,
            encryptedApi = encryptedApiService,
            videoDao = videoDao,
            khazanaDao = khazanaDao,
            priceDao = priceDao,
            exDao = exampleDao,
            promoDao = promoDao,
            indexDao = indexDao,
            khazanaCacheDao = khazanaCacheDao,
            khazanaSubjectDao = khazanaSubjectDao,
            khazanaTeachersDao = khazanaTeachersDao,
            khazanaChaptersDao = khazanaChaptersDao,
            khazanaLecturesDao = khazanaLecturesDao,
            khazanaNotesDao = khazanaNotesDao,
            khazanaDppDao = khazanaDppDao,
            khazanaDppLecturesDao = khazanaDppLecturesDao,
            akSubjectDao = akSubjectDao,
            akLessonDao = akLessonDao,
            akVideoDao = akVideoDao,
            akNoteDao = akNoteDao,
            pwCourseDao = pwCourseDao,
            pwLessonDao = pwLessonDao,
            pwVideoDao = pwVideoDao,
            pwNotesDao = pwNotesDao,
            pwDppDao = pwDppDao,
            navDao = navDao,
            searchDao = searchDao
        )

    }

    @Provides
    @Singleton
    fun providesDao(db: FavouriteCourseDb): FavouriteCourseDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesFavDatabase(@ApplicationContext context: Context): FavouriteCourseDb {
        return Room.databaseBuilder(
            context,
            FavouriteCourseDb::class.java,
            "FavouritedDbbb"
        ).build()
    }


    @Provides
    @Singleton
    fun providesKhazanaFavDao(db: KhazanaFavDb): KhazanaFavDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaFavDatabase(@ApplicationContext context: Context): KhazanaFavDb {
        return Room.databaseBuilder(
            context,
            KhazanaFavDb::class.java,
            "KhazanaDbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesVideoDao(db: VideoDatabase): VideoDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesVideoDatabase(@ApplicationContext context: Context): VideoDatabase {
        return Room.databaseBuilder(
            context,
            VideoDatabase::class.java,
            "VideoDb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesPriceDao(db: PriceDb): PriceDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesPriceDatabase(@ApplicationContext context: Context): PriceDb {
        return Room.databaseBuilder(
            context,
            PriceDb::class.java,
            "CachePriceDb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesExDao(db: ExampleDb): ExampleDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesExampleDatabase(@ApplicationContext context: Context): ExampleDb {
        return Room.databaseBuilder(
            context,
            ExampleDb::class.java,
            "ExampleDb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesPromoDao(db: PromoDb): PromoDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesPromoDatabase(@ApplicationContext context: Context): PromoDb {
        return Room.databaseBuilder(
            context,
            PromoDb::class.java,
            "Promo cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesIndexDao(db: IndexDb): IndexDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesIndexDatabase(@ApplicationContext context: Context): IndexDb {
        return Room.databaseBuilder(
            context,
            IndexDb::class.java,
            "Ak Index cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKhazanaDao(db: KhazanaDb): KhazanaDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaDatabase(@ApplicationContext context: Context): KhazanaDb {
        return Room.databaseBuilder(
            context,
            KhazanaDb::class.java,
            "Khazana cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKhazanaSubjectDao(db: KhazanaSubjectDb): KhazanaSubjectDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaSubjectDatabase(@ApplicationContext context: Context): KhazanaSubjectDb {
        return Room.databaseBuilder(
            context,
            KhazanaSubjectDb::class.java,
            "Khazana Subject cache dbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKhazanaTeachersDao(db: KhazanaTeachersDb): KhazanaTeachersDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaTeachersDatabase(@ApplicationContext context: Context): KhazanaTeachersDb {
        return Room.databaseBuilder(
            context,
            KhazanaTeachersDb::class.java,
            "Khazana Teacher cache dbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKhazanaChaptersDao(db: KhazanaChaptersDb): KhazanaChaptersDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaChaptersDatabase(@ApplicationContext context: Context): KhazanaChaptersDb {
        return Room.databaseBuilder(
            context,
            KhazanaChaptersDb::class.java,
            "Khazana Chapter cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKhazanaLecturesDao(db: KhazanaLecturesDb): KhazanaLecturesDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaLecturesDatabase(@ApplicationContext context: Context): KhazanaLecturesDb {
        return Room.databaseBuilder(
            context,
            KhazanaLecturesDb::class.java,
            "Khazana Lectures cache dbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKhazanaNotesDao(db: KhazanaNotesDb): KhazanaNotesDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaNotesDatabase(@ApplicationContext context: Context): KhazanaNotesDb {
        return Room.databaseBuilder(
            context,
            KhazanaNotesDb::class.java,
            "Khazana Notes cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKhazanaDppDao(db: KhazanaDppDb): KhazanaDppDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaDppDatabase(@ApplicationContext context: Context): KhazanaDppDb {
        return Room.databaseBuilder(
            context,
            KhazanaDppDb::class.java,
            "Khazana Dpp cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKhazanaDppLecturesDao(db: KhazanaDppLecturesDb): KhazanaDppLecturesDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKhazanaDppLecturesDatabase(@ApplicationContext context: Context): KhazanaDppLecturesDb {
        return Room.databaseBuilder(
            context,
            KhazanaDppLecturesDb::class.java,
            "Khazana Dpp Lectures cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesAkSubjectDao(db: AkSubjectDb): AkSubjectDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesAkSubjectDatabase(@ApplicationContext context: Context): AkSubjectDb {
        return Room.databaseBuilder(
            context,
            AkSubjectDb::class.java,
            "Ak subjects cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesAkLessonDao(db: AkLessonDb): AkLessonDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesAkLessonDatabase(@ApplicationContext context: Context): AkLessonDb {
        return Room.databaseBuilder(
            context,
            AkLessonDb::class.java,
            "Ak lesson cache dbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesAkVideoDao(db: AkVideoDb): AkVideoDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesAkVideoDatabase(@ApplicationContext context: Context): AkVideoDb {
        return Room.databaseBuilder(
            context,
            AkVideoDb::class.java,
            "Ak video cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesAkNoteDao(db: AkNoteDb): AkNoteDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesAkNoteDatabase(@ApplicationContext context: Context): AkNoteDb {
        return Room.databaseBuilder(
            context,
            AkNoteDb::class.java,
            "Ak note cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesPwCourseDao(db: PwCourseDb): PwCourseDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesPwCourseDatabase(@ApplicationContext context: Context): PwCourseDb {
        return Room.databaseBuilder(
            context,
            PwCourseDb::class.java,
            "Pw course cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesPwLessonDao(db: PwLessonDb): PwLessonDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesPwLessonDatabase(@ApplicationContext context: Context): PwLessonDb {
        return Room.databaseBuilder(
            context,
            PwLessonDb::class.java,
            "Pw lesson cache dbbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesPwVideoDao(db: PwVideoDb): PwVideoDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesPwVideoDatabase(@ApplicationContext context: Context): PwVideoDb {
        return Room.databaseBuilder(
            context,
            PwVideoDb::class.java,
            "Pw video cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesPwNotesDao(db: PwNotesDb): PwNotesDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesPwNotesDatabase(@ApplicationContext context: Context): PwNotesDb {
        return Room.databaseBuilder(
            context,
            PwNotesDb::class.java,
            "Pw note cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesPwDppDao(db: PwDppDb): PwDppDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesPwDppDatabase(@ApplicationContext context: Context): PwDppDb {
        return Room.databaseBuilder(
            context,
            PwDppDb::class.java,
            "Pw dpp cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesNavDao(db: NavDb): NavDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesNavDatabase(@ApplicationContext context: Context): NavDb {
        return Room.databaseBuilder(
            context,
            NavDb::class.java,
            "Nav cache db"
        ).build()
    }

    @Provides
    @Singleton
    fun providesSearchDao(db: SearchDb): SearchDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesSearchDatabase(@ApplicationContext context: Context): SearchDb {
        return Room.databaseBuilder(
            context,
            SearchDb::class.java,
            "Search cache db"
        ).build()
    }

}