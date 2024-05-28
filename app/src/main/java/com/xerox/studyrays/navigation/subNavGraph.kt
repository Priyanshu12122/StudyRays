package com.xerox.studyrays.navigation

import android.content.Intent
import android.os.Build
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.xerox.studyrays.ui.screens.ak.akIndex.AkIndex
import com.xerox.studyrays.ui.screens.ak.akLesson.AkLesson
import com.xerox.studyrays.ui.screens.ak.akSubjects.AkSubjects
import com.xerox.studyrays.ui.screens.ak.akVideoPlayer.AkVideoPlayer
import com.xerox.studyrays.ui.screens.ak.akVideosAndNotes.AkVideosAndNotes
import com.xerox.studyrays.ui.screens.khazana.khazanaChaptersScreen.KhazanaChaptersScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen.KhazanaLecturesScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaScreen.KhazanaScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaSubjectsScreen.KhazanaSubjectsScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaTeachersScreen.KhazanaTeachersScreen
import com.xerox.studyrays.ui.screens.pdfViewerScreen.PdfViewerScreen
import com.xerox.studyrays.ui.screens.pw.chaptersScreen.ChaptersScreen
import com.xerox.studyrays.ui.screens.pw.classesscreen.ClassesScreen
import com.xerox.studyrays.ui.screens.pw.coursesscreen.CoursesScreen
import com.xerox.studyrays.ui.screens.pw.favouriteCoursesScreen.FavouriteCoursesScreen
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.LecturesScreen
import com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.SubjectsScreen
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoPlayerScreen
import com.xerox.studyrays.utils.UseNewerVersionScreen
import com.xerox.studyrays.utils.navigateTo2


fun NavGraphBuilder.subNavGraph(navController: NavHostController) {

    val arg_key = "url"
    val title = "title"
    val bname = "batchName"
    val externalId = "externalId"
    val openApp = "openApp"
    val embedCode = "embedCode"


    navigation(
        startDestination = "${NavRoutes.eachClass}/{class}",
        route = NavGraphRoutes.mainScreenNavigation
    ) {

        composable(route = "${NavRoutes.eachClass}/{class}",
            arguments = listOf(
                navArgument(name = "class") {
                    type = NavType.StringType
                }
            )
        ) {
            val classValue = it.arguments?.getString("class")
            CoursesScreen(classValue = classValue!!, onBackClicked = {
                navController.popBackStack()
            }) { externalIdd, name ->
                navigateTo2(
                    navController,
                    NavRoutes.subjectsScreen + "?$title=$name&$externalId=$externalIdd"
                )
            }
        }

        composable(route = NavRoutes.subjectsScreen + "?$title={$title}&$externalId={$externalId}",
            arguments = listOf(
                navArgument(name = externalId) {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                }
            )
        ) {
            val courseId = it.arguments?.getString(externalId)
            val batchName = it.arguments?.getString(title)
            SubjectsScreen(courseId = courseId!!, onBackIconClicked = {
                val navBackStackEntry = navController.previousBackStackEntry
                if (navBackStackEntry != null) {
                    navController.popBackStack()
                }
            }) { subjectId, subjectName ->
                navigateTo2(
                    navController,
                    NavRoutes.lessonsScreen + "/$subjectId/$subjectName?$title=$batchName"
                )

            }
        }

        composable(route = NavRoutes.lessonsScreen + "/{subjectId}/{subjectName}?$title={$title}",
            arguments = listOf(
                navArgument(name = "subjectId") {
                    type = NavType.StringType
                },
                navArgument(name = "subjectName") {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                }
            )
        ) {
            val subjectId = it.arguments?.getString("subjectId")
            val subjectName = it.arguments?.getString("subjectName")
            val batchName = it.arguments?.getString(title)
            ChaptersScreen(subjectId = subjectId!!, subject = subjectName!!, onBackClicked = {
                navController.popBackStack()
            }) { slug, name ->
                navigateTo2(
                    navController,
                    NavRoutes.lecturesScreen + "/$slug/$name?$title=$batchName"
                )
            }
        }

        composable(route = NavRoutes.lecturesScreen + "/{slug}/{name}?$title={$title}",
            arguments = listOf(
                navArgument(name = "slug") {
                    type = NavType.StringType
                },
                navArgument(name = "name") {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                }
            )
        ) {
            val slug = it.arguments?.getString("slug")
            val name = it.arguments?.getString("name")
            val batchName = it.arguments?.getString(title)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                LecturesScreen(
                    slug = slug!!,
                    name = name!!,
                    onVideoClicked = { url, titlee, id, embedCodee ->
                        navigateTo2(
                            navController,
                            NavRoutes.videoScreen + "?$arg_key=$url&$title=$titlee&$bname=$batchName&$externalId=$id&$embedCode=$embedCodee"
                        )
                    },
                    onPdfViewClicked = { id, namee ->
                        navigateTo2(
                            navController,
                            NavRoutes.pdfViewerScreen + "?$externalId=$id&$title=$namee"
                        )
                    }) {
                    navController.popBackStack()
                }
            } else {
                UseNewerVersionScreen()
            }
        }


        composable(route = NavRoutes.pdfViewerScreen + "?$externalId={$externalId}&$title={$title}",
            arguments = listOf(
                navArgument(name = externalId) {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                }
            )
        ) {
            PdfViewerScreen(
                url = it.arguments?.getString(externalId)!!,
                name = it.arguments?.getString(title)!!
            ){
                navController.popBackStack()
            }

        }

        composable(route = NavRoutes.videoScreen + "?$arg_key={$arg_key}&$title={$title}&$bname={$bname}&$externalId={$externalId}&$embedCode={$embedCode}",
            arguments = listOf(
                navArgument(name = arg_key) {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                },
                navArgument(name = openApp) {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument(name = bname) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(name = externalId) {
                    type = NavType.StringType
                    nullable = true
                },
                navArgument(name = embedCode) {
                    type = NavType.StringType
                    nullable = true
                }

            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern =
                        "https://studyrays.site/?$arg_key={$arg_key}&$title={$title}&$bname={$bname}&$externalId={$externalId}&$openApp={$openApp}"
                    action = Intent.ACTION_VIEW
                }
            )
        ) {
            val url = it.arguments?.getString(arg_key)
            val titlee = it.arguments?.getString(title)
            val batchName = it.arguments?.getString(bname)
            val id = it.arguments?.getString(externalId)
            val embedCodee = it.arguments?.getString(embedCode)

            val shouldOpenApp = it.arguments?.getString(openApp)

            when (shouldOpenApp) {
                "true" -> {
                    VideoPlayerScreen(
                        url = url!!,
                        title = titlee ?: "",
                        id = id ?: "",
                        bname = batchName ?: "",
                        embedCode = embedCodee ?: ""
                    ) {
                        navController.navigate(NavRoutes.study) {
                            popUpTo(NavRoutes.study) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }

                    }

                }

                "" -> {
                    VideoPlayerScreen(
                        title = titlee ?: "",
                        url = url ?: "",
                        id = id ?: "",
                        bname = batchName ?: "",
                        onBackClicked = {
                            navController.popBackStack()
                        },
                        embedCode = embedCode ?: ""
                    )
                }

                else -> {
                    navController.navigate(NavRoutes.study) {
                        popUpTo(NavRoutes.study) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }

                }
            }

        }

        composable(route = NavRoutes.favouriteCoursesScreen) {
            FavouriteCoursesScreen(onBackClicked = {
                navController.popBackStack()
            },
                onKhazanaClick = {subjectId, chapterId, imageUrl, courseName ->
                    navigateTo2(
                        navController,
                        NavRoutes.khazanaChaptersScreen + "/$subjectId/$chapterId?$title=$courseName&$arg_key=$imageUrl"
                    )
                }
            ) { id, name ->
                navigateTo2(
                    navController,
                    NavRoutes.subjectsScreen + "?$title=$name&$externalId=$id"
                )
            }
        }

        composable(route = NavRoutes.classes) {
            ClassesScreen(shouldShow = true, onBackClicked = {
                navController.popBackStack()
            }) { classValue ->
                navigateTo2(navController, "${NavRoutes.eachClass}/$classValue")
            }
        }

        composable(route = NavRoutes.khazanaScreen) {
            KhazanaScreen(shouldShow = true,
                onClick = {
                    navigateTo2(navController, NavRoutes.khazanaSubjectsScreen + "/$it")
                }
            ) {
                navController.popBackStack()
            }
        }

        composable(route = NavRoutes.khazanaSubjectsScreen + "/{slug}",
            arguments = listOf(
                navArgument(name = "slug") {
                    type = NavType.StringType
                }
            )
        ) {
            val slug = it.arguments?.getString("slug")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                KhazanaSubjectsScreen(slug = slug!!,
                    onBackClicked = {
                        navController.popBackStack()
                    }
                ) { name, id ->
                    navigateTo2(navController, NavRoutes.khazanaTeachersScreen + "/$id?$title=$name")

                }
            } else {
                UseNewerVersionScreen()
            }
        }

        composable(route = NavRoutes.khazanaTeachersScreen + "/{id}?$title={$title}",
            arguments = listOf(
                navArgument(name = title) {
                    type = NavType.StringType
                },
                navArgument(name = "id") {
                    type = NavType.StringType
                }
            )
        ) {
            val name = it.arguments?.getString(title)
            val id = it.arguments?.getString("id")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                KhazanaTeachersScreen(id = id!!, subject = name!!, onBackClicked = {
                    navController.popBackStack()
                }) { subjectId, chapterId, imageUrl, courseName ->
                    navigateTo2(
                        navController,
                        NavRoutes.khazanaChaptersScreen + "/$subjectId/$chapterId?$title=$courseName&$arg_key=$imageUrl"
                    )

                }
            } else {
                UseNewerVersionScreen()
            }

        }


        composable(
            route = NavRoutes.khazanaChaptersScreen + "/{subjectId}/{chapterId}?$title={$title}&$arg_key={$arg_key}",
            arguments = listOf(
                navArgument(name = "subjectId") {
                    type = NavType.StringType
                },
                navArgument(name = "chapterId") {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                },
                navArgument(name = arg_key) {
                    type = NavType.StringType
                },
            )

        ) {
            val subjectId = it.arguments?.getString("subjectId")
            val chapterId = it.arguments?.getString("chapterId")
            val courseName = it.arguments?.getString(title)
            val imageUrl = it.arguments?.getString(arg_key)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                KhazanaChaptersScreen(
                    subjectId = subjectId!!,
                    chapterId = chapterId!!,
                    courseName = courseName!!,
                    imageUrl = imageUrl!!,
                    onBackClick = { navController.popBackStack() }) { subjectIdd, chapterIdd, topicId, topicName ->
                    navigateTo2(
                        navController,
                        NavRoutes.khazanaLecturesScreen + "/$subjectIdd/$chapterIdd/$topicId?$title=$topicName"
                    )
                }
            } else {
                UseNewerVersionScreen()
            }
        }

        composable(route = NavRoutes.khazanaLecturesScreen + "/{subjectId}/{chapterId}/{topicId}?$title={$title}",
            arguments = listOf(
                navArgument(name = "subjectId") {
                    type = NavType.StringType
                },
                navArgument(name = "chapterId") {
                    type = NavType.StringType
                },
                navArgument(name = "topicId") {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                }

            )
        ) {
            val subjectId = it.arguments?.getString("subjectId")
            val chapterId = it.arguments?.getString("chapterId")
            val topicId = it.arguments?.getString("topicId")
            val topicName = it.arguments?.getString(title)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                KhazanaLecturesScreen(
                    subjectId = subjectId!!,
                    chapterId = chapterId!!,
                    topicId = topicId!!,
                    topicName = topicName!!,
                    onVideoClicked = { url, topicNamee ->
                        navigateTo2(
                            navController,
                            NavRoutes.videoScreen + "?$arg_key=$url&$title=$topicNamee"
                        )
                    },
                    onPdfViewClicked = { id, name ->
                        navigateTo2(
                            navController,
                            NavRoutes.pdfViewerScreen + "?$externalId=$id&$title=$name"
                        )

                    }
                ) {
                    navController.popBackStack()
                }
            } else {
                UseNewerVersionScreen()
            }
        }


        composable(route = NavRoutes.akIndexScreen) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AkIndex(
                    onClick = {
                        navigateTo2(navController, NavRoutes.akSubjectsScreen + "/$it")
                    },
                    shouldShow = true
                ) {
                    navController.popBackStack()

                }
            } else {
                UseNewerVersionScreen()
            }
        }



        composable(route = NavRoutes.akSubjectsScreen + "/{bid}",
            arguments = listOf(
                navArgument(name = "bid") {
                    type = NavType.IntType
                }
            )
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AkSubjects(
                    bid = it.arguments?.getInt("bid")!!,
                    onClick = { sid, bid ->
                        navigateTo2(navController, NavRoutes.akLessonsScreen + "/$sid/$bid")
                    }
                ) {
                    navController.popBackStack()
                }
            } else {
                UseNewerVersionScreen()
            }
        }

        composable(
            route = NavRoutes.akLessonsScreen + "/{sid}/{bid}",
            arguments = listOf(
                navArgument(name = "sid") {
                    type = NavType.IntType
                },
                navArgument(name = "bid") {
                    type = NavType.IntType
                },
            )
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                AkLesson(sid = it.arguments?.getInt("sid")!!, bid = it.arguments?.getInt("bid")!!,
                    onClick = { sid, tid, bid ->
                        navigateTo2(navController, NavRoutes.akVideosScreen + "/$sid/$tid/$bid")
                    }) {
                    navController.popBackStack()

                }

            } else {
                UseNewerVersionScreen()
            }
        }

        composable(
            route = NavRoutes.akVideosScreen + "/{sid}/{tid}/{bid}",
            arguments = listOf(
                navArgument(name = "sid") {
                    type = NavType.IntType
                },
                navArgument(name = "tid") {
                    type = NavType.IntType
                },
                navArgument(name = "bid") {
                    type = NavType.IntType
                },
            )
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AkVideosAndNotes(
                    sid = it.arguments?.getInt("sid")!!,
                    tid = it.arguments?.getInt("tid")!!,
                    bid = it.arguments?.getInt("bid")!!,
                    onClick = { lid, sid, bid, tid, key, titlee ->
                        navigateTo2(
                            navController,
                            NavRoutes.akVideoUScreen + "/$key/$sid/$bid/$tid/$lid?$title=$titlee"
                        )
                    },
                    onPdfViewClicked = { id,name->
                        navigateTo2(navController, NavRoutes.pdfViewerScreen+"?$externalId=$id&$title=$name")
                    }
                ) {
                    navController.popBackStack()

                }
            } else {
                UseNewerVersionScreen()
            }
        }

        composable(route = NavRoutes.akVideoUScreen + "/{key}/{sid}/{bid}/{tid}/{lid}?$title={$title}",
            arguments = listOf(
                navArgument(name = "key") {
                    type = NavType.IntType
                },
                navArgument(name = "sid") {
                    type = NavType.IntType
                },
                navArgument(name = "bid") {
                    type = NavType.IntType
                },
                navArgument(name = "tid") {
                    type = NavType.IntType
                },
                navArgument(name = "lid") {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                }
            )
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AkVideoPlayer(
                    sid = it.arguments?.getInt("sid")!!,
                    tid = it.arguments?.getInt("tid")!!,
                    bid = it.arguments?.getInt("bid")!!,
                    key = it.arguments?.getInt("key")!!,
                    lid = it.arguments?.getString("lid")!!,
                    title = it.arguments?.getString(title)!!

                ) {
                    navController.popBackStack()
                }
            } else {
                UseNewerVersionScreen()
            }
        }

    }
}


