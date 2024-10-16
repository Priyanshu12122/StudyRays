package com.xerox.studyrays.ui.leaderBoard.user.utils

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.xerox.studyrays.db.userDb.UserEntity
import com.xerox.studyrays.model.userModel.PostUserItem
import com.xerox.studyrays.network.Response
import com.xerox.studyrays.ui.leaderBoard.LeaderBoardViewModel
import com.xerox.studyrays.ui.theme.DarkTeal
import com.xerox.studyrays.utils.LoadingScreen

@Composable
fun UpdateAlertDialog(
    modifier: Modifier = Modifier,
//    isOpen: Boolean,
    onDismiss: () -> Unit,
    vm: LeaderBoardViewModel,
    name: String,
    exam: String,
    gender: String,
//    userItem: PostUserItem,
) {


//    var user by rememberSaveable { mutableStateOf<UserEntity?>(null) }
    LaunchedEffect(key1 = Unit) {
        val user = vm.getUserByIdFromDb(1)!!
//        user = vm.getUserFromDb(1)!!

        Log.d("TAG", "UpdateAlertDialog: $name, $gender, $exam")
        vm.updateUserData(
            PostUserItem(
                user_id = user.userId,
                user_name = name,
                isBanned = user.isBanned,
                gender = gender,
                exam = exam,
            )
        )
    }

//    val userItem = PostUserItem(
//        user_id = user!!.userId,
//        user_name = name,
//        isBanned = user!!.isBanned,
//        gender = gender,
//        exam = exam,
//    )


    val context = LocalContext.current

    val state by vm.updateUser.collectAsState()

    var isLoading by rememberSaveable {
        mutableStateOf(true)
    }

//    if (isOpen) {

        AlertDialog(
            containerColor = DarkTeal,
            onDismissRequest = {
                if (!isLoading) {
                    onDismiss()
                }
            },
            confirmButton = {},
            text = {

                when (val result = state) {
                    is Response.Error -> {
                        isLoading = false
                        vm.showToast(context, "Error occurred, ${result.msg}", true)
                    }

                    is Response.Loading -> {
                        LoadingScreen(paddingValues = PaddingValues())
                    }

                    is Response.Success -> {
                        isLoading = false

                        LaunchedEffect(key1 = Unit) {


                            val userEntity = vm.getUserByIdFromDb(1)!!

                            vm.insertUser(
                                UserEntity(
                                    userId = userEntity.userId,
                                    userNumber = 1,
                                    name = name,
                                    exam = exam,
                                    gender = gender,
                                    isBanned = userEntity.isBanned,
                                    date = userEntity.date

                                )
                            )
                        }



                        TextComposable(
                            text = "Updated SuccessFully!!",
                            fontWeight = 1000,
                            fontSize = 28
                        )
                    }
                }

            }
        )


//    }


}