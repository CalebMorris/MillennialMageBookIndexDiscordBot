package io.honu.books.bot

import dev.kord.core.entity.Message

object ResponseExtensions {

    suspend fun Message.addInputReactions() {
        this.addReaction(Emojis.redEx)
        this.addReaction(Emojis.leftArrow)
        this.addReaction(Emojis.stopButton)
        this.addReaction(Emojis.rightArrow)
    }

}