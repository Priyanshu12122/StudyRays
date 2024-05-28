package com.xerox.studyrays.model.pwModel

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CourseItem(
    @SerializedName("byName") val byName: String,
    @SerializedName("class") val `class`: String,
    @SerializedName("dataFrom") val dataFrom: String,
    @SerializedName("exam") val exam: String,
    @SerializedName("external_id") val external_id: String,
    @SerializedName("fee") val fee: String,
    @SerializedName("id") val id: String,
    @SerializedName("language") val language: String,
    @SerializedName("markedAsNew") val markedAsNew: String,
    @SerializedName("name") val name: String,
    @SerializedName("orientationClassBanner") val orientationClassBanner: String,
    @SerializedName("premium") val premium: String,
    @SerializedName("previewImage_baseUrl") val previewImage_baseUrl: String,
    @SerializedName("previewImage_id") val previewImage_id: String,
    @SerializedName("previewImage_key") val previewImage_key: String,
    @SerializedName("previewImage_name") val previewImage_name: String,
    @SerializedName("programId_board") val programId_board: String,
    @SerializedName("programId_class") val programId_class: String,
    @SerializedName("programId_id") val programId_id: String,
    @SerializedName("programId_language") val programId_language: String,
    @SerializedName("programId_name") val programId_name: String,
    @SerializedName("slug") val slug: String,
    @SerializedName("testCat") val testCat: String
)
