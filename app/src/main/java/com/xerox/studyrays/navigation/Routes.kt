package com.xerox.studyrays.navigation

import com.xerox.studyrays.R


sealed class Routes(val icon: Int, val route: String,val title: String){

    object Study: Routes(icon = R.drawable.study, route = NavRoutes.study,title = "Main")
    object Batches: Routes(icon = R.drawable.studyhours, route = NavRoutes.batches,title = "Study")
    object Test: Routes(icon = R.drawable.leaderboard, route = NavRoutes.test,title = "Leaderboard")
    object PwStore: Routes(icon = R.drawable.user, route = NavRoutes.pwStore,title = "User")

}

object NavRoutes{
    const val study = "study"
    const val batches = "batches"
    const val test = "test"
    const val classes = "classes"
    const val pwStore = "pwStore"
    const val pdfViewerScreen = "pdfViewerScreen"
    const val announcementsScreen = "announcementsScreen"

    const val eachClass = "eachClass"
    const val eachClassOld = "eachClassOld"
    const val subjectsScreen = "subjects"
    const val subjectsScreenOld = "subjectsScreenOld"
    const val lessonsScreen = "lessons"
    const val lessonsScreenOld = "lessonsScreenOld"
    const val lecturesScreen = "lectures"
    const val lecturesScreenOld = "lecturesScreenOld"
    const val videoScreen = "video"
    const val favouriteCoursesScreen = "fav_courses"
    const val khazanaScreen = "khazana"
    const val khazanaSubjectsScreen = "khazana_subjects"
    const val khazanaTeachersScreen = "khazana_teachers"
    const val khazanaChaptersScreen = "khazana_chapters"
    const val khazanaLecturesScreen = "khazana_lectures"

    const val akIndexScreen = "akIndex"
    const val akSubjectsScreen = "akSubjects"
    const val akLessonsScreen = "akLessons"
    const val akVideosScreen = "akVideos"
    const val akVideoUScreen = "akVideosU"


    const val keyGenerationScreen = "keyGenerationScreen"
    const val webViewScreen = "webViewScreen"
    const val keyTaskCompletedScreen = "keyTaskCompletedScreen"

    const val updateBatchScreen = "updateBatchScreen"

    const val watchLaterScreen = "watchLaterScreen"

    const val videoDownloaderTaskScreen = "videoDownloaderTask"
    const val taskCompletedScreen = "taskCompleted"

//    Study focus
    const val studyFocusSubjectDetailsScreen = "studyFocusSubjectDetailsScreen"
    const val taskStudyFocusScreen = "taskStudyFocusScreen"
    const val sessionScreen = "sessionScreen"

//    user and leaderboard
    const val welcomeScreen = "welcomeScreen"
    const val loginScreen = "loginScreen"
    const val createAccountScreen = "createAccountScreen"
    const val userScreen = "userScreen"
    const val editDetailsScreen = "editDetailsScreen"
}

object NavGraphRoutes{
    const val bottomBarNavigation = "bottom"
    const val mainScreenNavigation = "main"
    const val userScreenNavigation = "userScreenNavigation"
}