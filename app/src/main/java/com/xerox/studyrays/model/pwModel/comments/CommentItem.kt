package com.xerox.studyrays.model.pwModel.comments

data class CommentItem(
    val batch_id: String,
    val batch_name: String,
    val batch_slug: String,
    val chapter_slug: String,
    val comment_id: String,
    val comment_text: String,
    val createdAt: String,
    val created_id: String,
    val created_image: String,
    val created_name: String,
    val id: Int,
    val isLiked: String,
    val isPinned: String,
    val like_count: String,
    val subject_slug: String,
    val video_external_id: String,
    val video_id: String
)