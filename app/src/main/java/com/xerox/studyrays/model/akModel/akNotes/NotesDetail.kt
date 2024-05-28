package com.xerox.studyrays.model.akModel.akNotes

data class NotesDetail(
    val docTitle: String?,
    val docUrl: String?,
    val id: Int,
    val isDownload: Int,
    val isPaid: Int,
    val notesno: Int,
    val publishedAt: String
)