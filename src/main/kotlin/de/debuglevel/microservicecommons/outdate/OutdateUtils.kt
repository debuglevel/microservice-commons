package de.debuglevel.microservicecommons.outdate

import mu.KotlinLogging
import java.time.Duration
import java.time.LocalDateTime

object OutdateUtils {
    private val logger = KotlinLogging.logger {}

    /**
     * Checks if an item is outdated, based on the [lastSuccessfulAttempt] (if null, returns true) and an [outdatingInterval].
     */
    fun isOutdated(
        lastSuccessfulAttempt: LocalDateTime?,
        outdatingInterval: Duration
    ): Boolean {
        logger.trace { "Checking if is outdated (outdating-interval=$outdatingInterval)..." }

        val outdated = when {
            lastSuccessfulAttempt == null -> false
            lastSuccessfulAttempt.plus(outdatingInterval) < LocalDateTime.now() -> true
            else -> false
        }

        logger.trace { "Checked if is outdated (outdating-interval=$outdatingInterval): $outdated" }
        return outdated
    }
}