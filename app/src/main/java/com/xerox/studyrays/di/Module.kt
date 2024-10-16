package com.xerox.studyrays.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.xerox.studyrays.cacheDb.keyGeneratorCache.KeyGenerateDao
import com.xerox.studyrays.cacheDb.keyGeneratorCache.KeyGenerateDb
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavDao
import com.xerox.studyrays.cacheDb.mainScreenCache.navDb.NavDb
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchDao
import com.xerox.studyrays.cacheDb.mainScreenCache.searchSectionDb.SearchDb
import com.xerox.studyrays.data.AkRepository
import com.xerox.studyrays.data.ApiRepository
import com.xerox.studyrays.data.KhazanaRepository
import com.xerox.studyrays.data.LeaderBoardRepository
import com.xerox.studyrays.data.StudyFocusRepository
import com.xerox.studyrays.db.alarmDb.AlarmDao
import com.xerox.studyrays.db.alarmDb.AlarmDb
import com.xerox.studyrays.db.downloadsDb.DownloadNumberDao
import com.xerox.studyrays.db.downloadsDb.DownloadNumberDb
import com.xerox.studyrays.db.exampleDb.ExampleDao
import com.xerox.studyrays.db.exampleDb.ExampleDb
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourseDao
import com.xerox.studyrays.db.favouriteCoursesDb.FavouriteCourseDb
import com.xerox.studyrays.db.keyDb.KeyDao
import com.xerox.studyrays.db.keyDb.KeyDb
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFavDao
import com.xerox.studyrays.db.khazanaFavDb.KhazanaFavDb
import com.xerox.studyrays.db.studyFocusDb.SessionDao
import com.xerox.studyrays.db.studyFocusDb.StudyFocusDb
import com.xerox.studyrays.db.studyFocusDb.SubjectDao
import com.xerox.studyrays.db.studyFocusDb.TaskDaoo
import com.xerox.studyrays.db.taskDb.TaskDao
import com.xerox.studyrays.db.taskDb.TaskDb
import com.xerox.studyrays.db.userDb.UserDao
import com.xerox.studyrays.db.userDb.UserDb
import com.xerox.studyrays.db.videoNotesDb.VideoNoteDao
import com.xerox.studyrays.db.videoNotesDb.VideoNoteDb
import com.xerox.studyrays.db.videoProgress.VideoProgressDao
import com.xerox.studyrays.db.videoProgress.VideoProgressDb
import com.xerox.studyrays.db.watchLaterDb.WatchLaterDao
import com.xerox.studyrays.db.watchLaterDb.WatchLaterDb
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
    fun providesLeaderBoardRepository(
        api: ApiService,
        userDao: UserDao
    ): LeaderBoardRepository {
        return LeaderBoardRepository(
            api = api,
            userDao = userDao
        )
    }

    @Provides
    @Singleton
    fun providesUserDb( @ApplicationContext context: Context): UserDb{
        return Room.databaseBuilder(
            context,
            UserDb::class.java,
            "UserDb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesUserDao(db: UserDb): UserDao {
        return db.dao
    }



    @Provides
    @Singleton
    fun providesKhazanRepository(
        api: ApiService,
        encryptedApiService: EncryptedApiService,
        khazanaDao: KhazanaFavDao,

    ): KhazanaRepository{
        return KhazanaRepository(
            api = api,
            encryptedApi= encryptedApiService,
            khazanaDao = khazanaDao,
        )
    }

    @Provides
    @Singleton
    fun providesAkRepository(
        api: ApiService,
        encryptedApiService: EncryptedApiService,
    ): AkRepository {
        return AkRepository(
            api = api,
            encryptedApi = encryptedApiService
        )

    }

    @Provides
    @Singleton
    fun providesRepository(
        api: ApiService,
        encryptedApiService: EncryptedApiService,
        dao: FavouriteCourseDao,
        navDao: NavDao,
        searchDao: SearchDao,
        taskDao: TaskDao,
        downloadNumberDao: DownloadNumberDao,
        alarmDao: AlarmDao,
        keyDao: KeyDao,
        keyGenerateDao: KeyGenerateDao,
        watchLaterDao: WatchLaterDao,
        videoProgressDao: VideoProgressDao,
        videoNoteDao: VideoNoteDao,
        @ApplicationContext context: Context,
    ): ApiRepository {
        return ApiRepository(
            api = api,
            encryptedApi = encryptedApiService,
            dao = dao,
            navDao = navDao,
            searchDao = searchDao,
            taskDao = taskDao,
            downloadNumberDao = downloadNumberDao,
            alarmDao = alarmDao,
            context = context,
            keyDao = keyDao,
            keyGenerateDao = keyGenerateDao,
            watchLaterDao = watchLaterDao,
            videoProgressDao = videoProgressDao,
            videoNoteDao = videoNoteDao,
        )

    }

    @Provides
    @Singleton
    fun providesStudyFocusRepository(
        subjectDao: SubjectDao,
        taskDao: TaskDaoo,
        sessionDao: SessionDao,
    ): StudyFocusRepository {
        return StudyFocusRepository(
            sessionDao = sessionDao,
            subjectDao = subjectDao,
            taskDao = taskDao,
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
            "FavouritedDbbbbbbbbb"
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
            "KhazanaDbbbb"
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
    fun providesNavDao(db: NavDb): NavDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesNavDatabase(@ApplicationContext context: Context): NavDb {
        return Room.databaseBuilder(
            context,
            NavDb::class.java,
            "Nav cache dbb"
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
            "Search cache dbb"
        ).build()
    }


    @Provides
    @Singleton
    fun providesTaskDao(db: TaskDb): TaskDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesTaskDatabase(@ApplicationContext context: Context): TaskDb {
        return Room.databaseBuilder(
            context,
            TaskDb::class.java,
            "Task Dbb"
        ).build()
    }


    @Provides
    @Singleton
    fun providesDownloadNumberDao(db: DownloadNumberDb): DownloadNumberDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesDownloadNumberDatabase(@ApplicationContext context: Context): DownloadNumberDb {
        return Room.databaseBuilder(
            context,
            DownloadNumberDb::class.java,
            "Downloadd Dbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesAlarmDao(db: AlarmDb): AlarmDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesAlarmDatabase(@ApplicationContext context: Context): AlarmDb {
        return Room.databaseBuilder(
            context,
            AlarmDb::class.java,
            "Alarm Dbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKeyDao(db: KeyDb): KeyDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKeyDatabase(@ApplicationContext context: Context): KeyDb {
        return Room.databaseBuilder(
            context,
            KeyDb::class.java,
            "Key Dbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesKeyGenerateDao(db: KeyGenerateDb): KeyGenerateDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesKeyGenerateDatabase(@ApplicationContext context: Context): KeyGenerateDb {
        return Room.databaseBuilder(
            context,
            KeyGenerateDb::class.java,
            "Key Generate Dbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesWatchLaterDao(db: WatchLaterDb): WatchLaterDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesWatchLaterDatabase(@ApplicationContext context: Context): WatchLaterDb {
        return Room.databaseBuilder(
            context,
            WatchLaterDb::class.java,
            "Watch later Dbbbb"
        ).build()
    }


    @Provides
    @Singleton
    fun providesVideoProgressDao(db: VideoProgressDb): VideoProgressDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesVideoProgressDatabase(@ApplicationContext context: Context): VideoProgressDb {
        return Room.databaseBuilder(
            context,
            VideoProgressDb::class.java,
            "Video Progress Dbbb"
        ).build()
    }

    @Provides
    @Singleton
    fun providesVideoNoteDao(db: VideoNoteDb): VideoNoteDao {
        return db.dao
    }

    @Provides
    @Singleton
    fun providesVideoNoteDatabase(@ApplicationContext context: Context): VideoNoteDb {
        return Room.databaseBuilder(
            context,
            VideoNoteDb::class.java,
            "Video Note Db"
        ).build()
    }


//    Study focus

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): StudyFocusDb {
        return Room
            .databaseBuilder(
                application,
                StudyFocusDb::class.java,
                "study focus .db"
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(database: StudyFocusDb): SubjectDao {
        return database.subjectDao()
    }

    @Provides
    @Singleton
    fun provideTaskDaoDao(database: StudyFocusDb): TaskDaoo {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideSessionDao(database: StudyFocusDb): SessionDao {
        return database.sessionDao()
    }


}