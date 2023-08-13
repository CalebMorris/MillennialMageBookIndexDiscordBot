package io.honu.books.bot

import dev.kord.core.entity.Message

object ResponseExtensions {

    suspend fun Message.addInputReactions() {
        this.addReaction(Emojis.redEx)
        this.addReaction(Emojis.leftArrow)
        this.addReaction(Emojis.stopButton)
        this.addReaction(Emojis.rightArrow)
    }

    suspend fun Message.removeInputReactions() {
        this.deleteReaction(Emojis.redEx)
        this.deleteReaction(Emojis.leftArrow)
        this.deleteReaction(Emojis.stopButton)
        this.deleteReaction(Emojis.rightArrow)
    }

}