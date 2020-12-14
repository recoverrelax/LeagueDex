package com.greater.leaguedex.util.parser

import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

class AvatarImageParserTest : TestCase() {
    @Test
    fun `test single word`() {
        val word = "JoãoMagalhães"
        val result = AvatarImageParser.buildAvatarUrl(0, word)
        Assert.assertTrue(
            "result was: $result",
            result!!.contains("js", true)
        )
    }

    @Test
    fun `test double word`() {
        val word = "João Magalhães"
        val result = AvatarImageParser.buildAvatarUrl(0, word)
        Assert.assertTrue(
            "result was: $result",
            result!!.contains("jm", true)
        )
    }
}
