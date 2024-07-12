package com.xerox.studyrays.db.studyFocusDb

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.xerox.studyrays.ui.theme.gradient1
import com.xerox.studyrays.ui.theme.gradient2
import com.xerox.studyrays.ui.theme.gradient3
import com.xerox.studyrays.ui.theme.gradient4
import com.xerox.studyrays.ui.theme.gradient5

@Entity
data class SubjectsEntity(
    val name: String,
    val goalHours: Float,
    val colors: List<Int>,
    @PrimaryKey(autoGenerate = true)
    val subjectId: Int? = null
){
    companion object {
        val subjectCardColors = listOf(gradient1, gradient2, gradient3, gradient4, gradient5)
    }
}
