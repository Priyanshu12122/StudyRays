package com.xerox.studyrays.navigation

import com.xerox.studyrays.R


sealed class Routes(val icon: Int, val route: String,val title: String){

    object Study: Routes(icon = R.drawable.study, route = NavRoutes.study,title = "Study")
    object Batches: Routes(icon = R.drawable.pwdarkk, route = NavRoutes.batches,title = "PW")
    object Test: Routes(icon = R.drawable.treasure, route = NavRoutes.test,title = "Khazana")
    object PwStore: Routes(icon = R.drawable.akdarkkk, route = NavRoutes.pwStore,title = "Apni Kaksha")

}

object NavRoutes{
    const val study = "study"
    const val batches = "batches"
    const val test = "test"
    const val classes = "classes"
    const val pwStore = "pwStore"
    const val pdfViewerScreen = "pdfViewerScreen"

    const val eachClass = "eachClass"
    const val subjectsScreen = "subjects"
    const val lessonsScreen = "lessons"
    const val lecturesScreen = "lectures"
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
}

object NavGraphRoutes{
    const val bottomBarNavigation = "bottom"
    const val mainScreenNavigation = "main"
}