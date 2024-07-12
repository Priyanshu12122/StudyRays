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
import com.xerox.studyrays.ui.screens.keyGeneratorScreen.KeyTaskCompletedScreen
import com.xerox.studyrays.ui.screens.keyGeneratorScreen.WebViewScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaChaptersScreen.KhazanaChaptersScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaLecturesScreen.KhazanaLecturesScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaScreen.KhazanaScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaSubjectsScreen.KhazanaSubjectsScreen
import com.xerox.studyrays.ui.screens.khazana.khazanaTeachersScreen.KhazanaTeachersScreen
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.UpdateBatchWebView
import com.xerox.studyrays.ui.screens.mainscreen.mainscreenutils.WatchLaterScreen
import com.xerox.studyrays.ui.screens.pdfViewerScreen.PdfViewerScreen
import com.xerox.studyrays.ui.screens.pw.chaptersScreen.ChaptersScreen
import com.xerox.studyrays.ui.screens.pw.chaptersScreen.old.ChaptersScreenOld
import com.xerox.studyrays.ui.screens.pw.classesscreen.ClassesScreen
import com.xerox.studyrays.ui.screens.pw.coursesscreen.CoursesScreen
import com.xerox.studyrays.ui.screens.pw.favouriteCoursesScreen.FavouriteCoursesScreen
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.LecturesScreen
import com.xerox.studyrays.ui.screens.pw.lecturesScreen.old.LecturesScreenOld
import com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.AnnouncementsScreen
import com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.SubjectsScreen
import com.xerox.studyrays.ui.screens.pw.subjectsAndTeachersScreen.old.SubjectsScreenOld
import com.xerox.studyrays.ui.screens.videoPlayerScreen.VideoPlayerScreen
import com.xerox.studyrays.ui.screens.videoPlayerScreen.taskCompletedScreen.TaskCompletedScreen
import com.xerox.studyrays.ui.screens.videoPlayerScreen.videoDownloaderTask.VideoDownloaderTaskScreen
import com.xerox.studyrays.utils.UseNewerVersionScreen
import com.xerox.studyrays.utils.navigateTo
import com.xerox.studyrays.utils.navigateTo2
import com.xerox.studyrays.utils.navigateTo3


//@Composable
fun NavGraphBuilder.subNavGraph(navController: NavHostController) {

    val arg_key = "url"
    val title = "title"
    val bname = "batchName"
    val externalId = "externalId"
    val openApp = "openApp"
    val embedCode = "embedCode"
    val imageUrl = "imageUrl"
    val dateCreated = "dateCreated"
    val duration = "duration"
    val isWhat = "isWhat"
    val videoId = "videoId"
    val slugg = "slug"
    val classValues = "classValue"
    val subjectSluggg = "subjectSlug"




    navigation(
        startDestination = "${NavRoutes.eachClass}/{class}/{isOld}",
        route = NavGraphRoutes.mainScreenNavigation
    )
    {

        composable(route = "${NavRoutes.eachClass}/{class}/{isOld}",
            arguments = listOf(
                navArgument(name = "class") {
                    type = NavType.StringType
                },
                navArgument(name = "isOld") {
                    type = NavType.BoolType
                }
            )
        ) {
            val classValue = it.arguments?.getString("class")
            val isOld = it.arguments?.getBoolean("isOld")
            CoursesScreen(
                classValue = classValue!!,
                isOld = isOld!!,
                onBackClicked = {
                    navController.navigateUp()
                },
                onClickOld = { courseId, name ->
                    navigateTo2(
                        navController,
                        NavRoutes.subjectsScreenOld + "?$title=$name&$externalId=$courseId"
                    )
                }) { batchIdd, slug, classValuee, name ->
                navigateTo2(
                    navController,
                    NavRoutes.subjectsScreen + "?$title=$name&$externalId=$batchIdd&$slugg=$slug&$classValues=$classValuee"
                )
            }
        }


        composable(route = NavRoutes.subjectsScreenOld + "?$title={$title}&$externalId={$externalId}",

            arguments = listOf(
                navArgument(name = externalId) {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                }
            )
        ) {
            val isOld = true
            SubjectsScreenOld(
                courseId = it.arguments?.getString(externalId)!!,
                onBackIconClicked = {
                    navController.navigateUp()
                },
                onAnnouncementsClicked = { batchId ->
                    navigateTo2(navController, NavRoutes.announcementsScreen + "/$batchId/$isOld")
                },
                onClick = { subjectId, subjectName ->
                    navigateTo2(
                        navController,
                        NavRoutes.lessonsScreenOld + "?$externalId=$subjectId&$bname=$subjectName"
                    )
                }
            )

        }


        composable(route = NavRoutes.subjectsScreen + "?$title={$title}&$externalId={$externalId}&$slugg={$slugg}&$classValues={$classValues}",
            arguments = listOf(
                navArgument(name = externalId) {
                    type = NavType.StringType
                },
                navArgument(name = title) {
                    type = NavType.StringType
                },
                navArgument(name = classValues) {
                    type = NavType.StringType
                },
                navArgument(name = slugg) {
                    type = NavType.StringType
                }
            )
        ) {

            val batchId = it.arguments?.getString(externalId)
            val batchName = it.arguments?.getString(title)
            val classValuee = it.arguments?.getString(classValues)
            val slug = it.arguments?.getString(slugg)
            val isOld = false
            SubjectsScreen(
                batchId = batchId!!,
                slug = slug!!,
                classValue = classValuee!!,
                onBackIconClicked = {
                    val navBackStackEntry = navController.previousBackStackEntry
                    if (navBackStackEntry != null) {
                        navController.navigateUp()
                    }
                },
                onAnnouncementsClicked = { batchId ->
                    navigateTo2(navController, NavRoutes.announcementsScreen + "/$batchId/$isOld")

                }) { subjectSlug, subjectName ->
                navigateTo2(
                    navController,
                    NavRoutes.lessonsScreen + "?$externalId=$batchId&$slugg=$subjectSlug&$bname=$subjectName&$title=$batchName"
                )

            }

        }

        composable(
            NavRoutes.lessonsScreenOld + "?$externalId={$externalId}&$bname={$bname}",
            arguments = listOf(
                navArgument(name = externalId) {
                    type = NavType.StringType
                },
                navArgument(name = bname) {
                    type = NavType.StringType
                },
            )
        ) {

            ChaptersScreenOld(
                subjectId = it.arguments?.getString(externalId)!!,
                subject = it.arguments?.getString(bname)!!,
                onBackClicked = {
                    navController.navigateUp()
                },
                onClick = { slug, name ->
                    navigateTo2(navController, NavRoutes.lecturesScreenOld+"?$slugg=$slug&$bname=$name")
                })
        }

    }

    composable(route = NavRoutes.lessonsScreen + "?$externalId={$externalId}&$slugg={$slugg}&$bname={$bname}&$title={$title}",
        arguments = listOf(
            navArgument(name = externalId) {
                type = NavType.StringType
            },
            navArgument(name = slugg) {
                type = NavType.StringType
            },
            navArgument(name = bname) {
                type = NavType.StringType
            },
            navArgument(name = title) {
                type = NavType.StringType
            }
        )
    ) {

        val batchId = it.arguments?.getString(externalId)
        val subjectSlug = it.arguments?.getString(slugg)
        val subjectName = it.arguments?.getString(bname)
        val batchName = it.arguments?.getString(title)

        ChaptersScreen(
            batchId = batchId!!,
            subjectSlug = subjectSlug!!,
            subject = subjectName!!,
            onBackClicked = {
                navController.navigateUp()
            }) { slug, name ->
            navigateTo2(
                navController,
                NavRoutes.lecturesScreen + "?$externalId=$batchId&$subjectSluggg=$subjectSlug&$slugg=$slug&$bname=$name&$title=$batchName"
            )
        }

    }

    composable(
        route = NavRoutes.lecturesScreen + "?$externalId={$externalId}&$subjectSluggg={$subjectSluggg}&$slugg={$slugg}&$bname={$bname}&$title={$title}",
        arguments = listOf(
            navArgument(name = slugg) {
                type = NavType.StringType
            },
            navArgument(name = bname) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(name = title) {
                type = NavType.StringType
            },
            navArgument(name = externalId) {
                type = NavType.StringType
            },
            navArgument(name = subjectSluggg) {
                type = NavType.StringType
            },
        )
    ) {
        val slug = it.arguments?.getString(slugg)
        val batchId = it.arguments?.getString(externalId)
        val subjectSlug = it.arguments?.getString(subjectSluggg)
        val name = it.arguments?.getString(bname)
        val batchName = it.arguments?.getString(title)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LecturesScreen(
                topicSlug = slug ?: "",
                batchId = batchId ?: "",
                subjectSlug = subjectSlug ?: "",
                name = name!!,
                onVideoClicked = { url, titlee, id, embedCodee, videoIdd, imageUrll, createdAtt, durationn, pw ->
                    navigateTo2(
                        navController,
                        NavRoutes.videoScreen + "?$arg_key=$url&$title=$titlee&$videoId=$videoIdd&$bname=$batchName&$externalId=$id&$embedCode=$embedCodee&$imageUrl=$imageUrll&$dateCreated=$createdAtt&$duration=$durationn&$isWhat=$pw&$slugg=$slug"
                    )
                },
                onPdfViewClicked = { id, namee ->
                    navigateTo2(
                        navController,
                        NavRoutes.pdfViewerScreen + "?$externalId=$id&$title=$namee"
                    )
                }) {
                navController.navigateUp()
            }
        } else {
            UseNewerVersionScreen()
        }
    }


    composable(route = NavRoutes.lecturesScreenOld + "?$slugg={$slugg}&$bname={$bname}",
        arguments = listOf(
            navArgument(name = slugg) {
                type = NavType.StringType
            },
            navArgument(name = bname) {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LecturesScreenOld(
                slug = it.arguments?.getString(slugg)!!,
                name = it.arguments?.getString(bname)!!,
                onVideoClicked = {url, titlee, id, embedCodee, videoIdd, imageUrll, createdAtt, durationn, pw ->
                    navigateTo2(
                        navController,
                        NavRoutes.videoScreen + "?$arg_key=$url&$title=$titlee&$videoId=$videoIdd&$bname=batchName&$externalId=$id&$embedCode=$embedCodee&$imageUrl=$imageUrll&$dateCreated=$createdAtt&$duration=$durationn&$isWhat=$pw"
                    )
                },
                onPdfViewClicked = {id, namee ->
                    navigateTo2(
                        navController,
                        NavRoutes.pdfViewerScreen + "?$externalId=$id&$title=$namee"
                    )
                },
                onBackClicked = {
                    navController.navigateUp()
                }
            )

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
        ) {
            navController.navigateUp()
        }

    }

    composable(route = NavRoutes.videoScreen + "?$arg_key={$arg_key}&$title={$title}&$videoId={$videoId}&$bname={$bname}&$externalId={$externalId}&$embedCode={$embedCode}&$imageUrl={$imageUrl}&$dateCreated={$dateCreated}&$duration={$duration}&$isWhat={$isWhat}&$slugg={$slugg}",
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
            },
            navArgument(name = videoId) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(name = imageUrl) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(name = dateCreated) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(name = duration) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(name = isWhat) {
                type = NavType.StringType
                nullable = true
            },
            navArgument(name = slugg) {
                type = NavType.StringType
                nullable = true
            }

        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern =
                    "https://extractapi.xyz/?$arg_key={$arg_key}&$title={$title}&$videoId={$videoId}&$externalId={$externalId}&$openApp={$openApp}"
                action = Intent.ACTION_VIEW
            }
        )
    ) { navBackStackEntry ->
        val url = navBackStackEntry.arguments?.getString(arg_key)
        val titlee = navBackStackEntry.arguments?.getString(title)
        val batchName = navBackStackEntry.arguments?.getString(bname)
        val id = navBackStackEntry.arguments?.getString(externalId)
        val embedCodee = navBackStackEntry.arguments?.getString(embedCode)
        val videoIdd = navBackStackEntry.arguments?.getString(videoId)
        val shouldOpenApp = navBackStackEntry.arguments?.getString(openApp)
        val imageUrll = navBackStackEntry.arguments?.getString(imageUrl)
        val dateCreatedd = navBackStackEntry.arguments?.getString(dateCreated)
        val durationn = navBackStackEntry.arguments?.getString(duration)
        val isWhatt = navBackStackEntry.arguments?.getString(isWhat)
        val topicSlug = navBackStackEntry.arguments?.getString(slugg)

        when (shouldOpenApp) {
            "true" -> {
                VideoPlayerScreen(
                    url = url!!,
                    title = titlee ?: "",
                    videoId = videoIdd ?: "",
                    id = id ?: "",
                    bname = batchName ?: "",
                    embedCode = embedCodee ?: "",
                    imageUrl = imageUrll ?: "",
                    createdAt = dateCreatedd ?: "00",
                    duration = durationn ?: "00",
                    topicSlug = topicSlug ?: "",
                    isWhat = isWhatt!!,
                    onNavigateToTaskScreen = { taskUrl, shortenedUrl ->
                        navigateTo2(
                            navController,
                            NavRoutes.videoDownloaderTaskScreen + "?$arg_key=$taskUrl&$title=$shortenedUrl"
                        )
                    }
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
                    videoId = videoIdd ?: "",
                    bname = batchName ?: "",
                    onBackClicked = {
                        navController.navigateUp()
                    },
                    embedCode = embedCode,
                    imageUrl = imageUrll ?: "",
                    createdAt = dateCreatedd ?: "00",
                    duration = durationn ?: "00",
                    isWhat = isWhatt!!,
                    topicSlug = topicSlug ?: "",
                    onNavigateToTaskScreen = { taskUrl, shortenedUrl ->
                        navigateTo2(
                            navController,
                            NavRoutes.videoDownloaderTaskScreen + "?$arg_key=$taskUrl&$title=$shortenedUrl"
                        )
                    }
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
            navController.navigateUp()
        },
            onKhazanaClick = { subjectId, chapterId, imageUrl, courseName ->
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
            navController.navigateUp()
        },
            onBatchClicked = { id, name, classValue, sluggg ->
                navigateTo2(
                    navController,
//                        ?$title={$title}&$externalId={$externalId}&$slugg={$slugg}&$classValues={$classValues}
                    NavRoutes.subjectsScreen + "?$title=$name&$externalId=$id&$slugg=$sluggg&$classValues=$classValue"
                )
            },
            onOldBatchClick = { id, name ->
                navigateTo2(navController, NavRoutes.subjectsScreenOld+"?$title=$name&$externalId=$id")
            },
            onEachCardOfOldBatchClicked = { classValue, isOld ->
                navigateTo2(navController, "${NavRoutes.eachClass}/$classValue/$isOld")

            }) { classValue, isOld ->
            navigateTo2(navController, "${NavRoutes.eachClass}/$classValue/$isOld")
        }
    }

    composable(route = NavRoutes.khazanaScreen) {
        KhazanaScreen(shouldShow = true,
            onClick = {
                navigateTo2(navController, NavRoutes.khazanaSubjectsScreen + "/$it")
            }
        ) {
            navController.navigateUp()
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
                    navController.navigateUp()
                }
            ) { name, id ->
                navigateTo2(
                    navController,
                    NavRoutes.khazanaTeachersScreen + "/$id?$title=$name"
                )

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
                navController.navigateUp()
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
                onBackClick = { navController.navigateUp() }) { subjectIdd, chapterIdd, topicId, topicName ->
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
                onVideoClicked = { url, topicNamee, videoIdd, imageUrll, createdAtt, durationn, khazana ->
                    navigateTo2(
                        navController,
                        NavRoutes.videoScreen + "?$arg_key=$url&$title=$topicNamee&$videoId=$videoIdd&$embedCode=ccc&$imageUrl=$imageUrll&$dateCreated=$createdAtt&$duration=$durationn&$isWhat=$khazana"
                    )
                },
                onPdfViewClicked = { id, name ->
                    navigateTo2(
                        navController,
                        NavRoutes.pdfViewerScreen + "?$externalId=$id&$title=$name"
                    )

                }
            ) {
                navController.navigateUp()
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
                navController.navigateUp()
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
                navController.navigateUp()
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AkLesson(sid = it.arguments?.getInt("sid")!!, bid = it.arguments?.getInt("bid")!!,
                onClick = { sid, tid, bid ->
                    navigateTo2(navController, NavRoutes.akVideosScreen + "/$sid/$tid/$bid")
                }) {
                navController.navigateUp()
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
                onClick = { lid, sid, bid, tid, key, titlee, uniqueId ->
                    navigateTo2(
                        navController,
                        NavRoutes.akVideoUScreen + "/$key/$sid/$bid/$tid/$lid?$title=$titlee&$videoId=$uniqueId"
                    )
                },
                onPdfViewClicked = { id, name ->
                    navigateTo2(
                        navController,
                        NavRoutes.pdfViewerScreen + "?$externalId=$id&$title=$name"
                    )
                }
            ) {
                navController.navigateUp()
            }
        } else {
            UseNewerVersionScreen()
        }
    }

    composable(route = NavRoutes.akVideoUScreen + "/{key}/{sid}/{bid}/{tid}/{lid}?$title={$title}&$videoId={$videoId}",
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
            },
            navArgument(name = videoId) {
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
                title = it.arguments?.getString(title)!!,
                videoId = it.arguments?.getString(videoId)!!,
                onNavigateToTaskScreen = { taskUrl, shortenedUrl ->
                    navigateTo2(
                        navController,
                        NavRoutes.videoDownloaderTaskScreen + "?$arg_key=$taskUrl&$title=$shortenedUrl"
                    )
                }

            ) {
                navController.navigateUp()
            }
        } else {
            UseNewerVersionScreen()
        }
    }

    composable(route = NavRoutes.videoDownloaderTaskScreen + "?$arg_key={$arg_key}&$title={$title}",
        arguments = listOf(
            navArgument(arg_key) {
                type = NavType.StringType
            }
        )
    ) {
        val taskUrl = it.arguments?.getString(arg_key)
        val shortenedUrl = it.arguments?.getString(title)
        VideoDownloaderTaskScreen(taskUrl = taskUrl!!,
            shortenedUrl = shortenedUrl!!,
            onBackClick = {
                navController.navigateUp()
            },
            onCompleted = {
                navigateTo2(navController, NavRoutes.taskCompletedScreen)
            })
    }

    composable(
        route = NavRoutes.taskCompletedScreen
    ) {
        TaskCompletedScreen(onClick = {
            navigateTo(navController, NavRoutes.study)
        })
    }


    composable(
        route = NavRoutes.webViewScreen + "?$arg_key={$arg_key}&$title={$title}",
        arguments = listOf(
            navArgument(name = arg_key) {
                type = NavType.StringType
            },
            navArgument(name = title) {
                type = NavType.StringType
            },
        )
    ) {
        val taskUrl = it.arguments?.getString(arg_key)
        val shortenedUrl = it.arguments?.getString(title)
        WebViewScreen(onCompleted = {
            navigateTo(navController, NavRoutes.keyTaskCompletedScreen)
        }, taskUrl = taskUrl!!,
            shortenedUrl = shortenedUrl!!,
            onBackClick = {
                navigateTo3(navController, NavRoutes.keyGenerationScreen)
            })
    }

    composable(route = NavRoutes.keyTaskCompletedScreen) {
        KeyTaskCompletedScreen(
            onClick = {
                navigateTo(navController, NavRoutes.study)
            }
        )
    }

    composable(route = NavRoutes.updateBatchScreen) {
        UpdateBatchWebView()
    }

    composable(route = NavRoutes.watchLaterScreen) {
        WatchLaterScreen(
            onVideoClicked = { url, titlee, id, embedCodee, videoIdd, imageUrll, createdAtt, durationn, isWhatt ->
                navigateTo2(
                    navController,
                    NavRoutes.videoScreen + "?$arg_key=$url&$title=$titlee&$videoId=$videoIdd&$bname=$bname&$externalId=$id&$embedCode=$embedCodee$imageUrl=$imageUrll&$dateCreated=$createdAtt&$duration=$durationn&$isWhat=$isWhatt"
                )

            },
            onBackClick = {
                navController.navigateUp()
            }
        )
    }

    composable(
        route = NavRoutes.announcementsScreen + "/{batchId}/{isOld}",
        arguments = listOf(
            navArgument(name = "batchId") {
                type = NavType.StringType
            },
            navArgument(name = "isOld") {
                type = NavType.BoolType
            },
        )
    ) {
        AnnouncementsScreen(
            batchId = it.arguments?.getString("batchId")!!,
            isOld = it.arguments?.getBoolean("isOld")!!,
            onBackClick = {
                navController.navigateUp()
            })
    }

}





