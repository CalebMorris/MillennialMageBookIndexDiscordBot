package io.honu.books.bot

import dev.kord.common.entity.Snowflake
import io.honu.books.index.view.SearchResult

data class ActiveBotMessage(
    val channelId: Snowflake,
    val messageId: Snowflake,
    val initiatingMessageId: Snowflake,
    val requestingUserId: Snowflake,
    val queryString: String,
    val queryPosition: Int,
    val results: List<SearchResult>,
)