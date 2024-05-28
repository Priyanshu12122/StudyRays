package com.xerox.studyrays.model.pwModel.comments

data class CommentsItem(
    val _id: String,
    val childComments: List<Any>,
    val createdAt: String,
    val createdBy: CreatedBy,
    val isLiked: Boolean,
    val isPinned: Boolean,
    val text: String,
    val upVotes: List<Any>,
    val upvoteCount: Int
)