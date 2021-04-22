package de.debuglevel.microservicecommons.wait

import mu.KotlinLogging
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

object WaitUtils {
    private val logger = KotlinLogging.logger {}

    private val lastCalls = mutableMapOf<Any, LocalDateTime>()

    /**
     * Waits (at most [waitBetweenCallsNanoseconds]) until [caller] permitted to make the next call.
     * @param caller The object which executes the call
     * @param waitBetweenCallsNanoseconds How long to wait between to calls.
     */
    fun waitForNextCallAllowed(
        caller: Any,
        waitBetweenCallsNanoseconds: Long,
    ) {
        logger.trace { "Waiting until next call to '$caller' is allowed..." }

        val lastCall = lastCalls[caller]
        waitForNextCallAllowed(lastCall, waitBetweenCallsNanoseconds)

        logger.trace { "Waited until next call to '$caller' is allowed" }
    }

    /**
     * Waits [waitBetweenCallsNanoseconds] from [lastCallOn] until the next call is permitted to be made.
     * @param lastCallOn When the last call was made.
     * @param waitBetweenCallsNanoseconds How long to wait between two calls.
     */
    fun waitForNextCallAllowed(
        lastCallOn: LocalDateTime?,
        waitBetweenCallsNanoseconds: Long,
    ) {
        logger.trace { "Waiting until next call is allowed..." }

        if (lastCallOn != null) {
            val nextCallDateTime = lastCallOn.plusNanos(waitBetweenCallsNanoseconds)
            val waitingTimeMilliseconds = ChronoUnit.MILLIS.between(LocalDateTime.now(), nextCallDateTime)

            logger.trace { "Last call was on $lastCallOn, waiting duration between calls is ${waitBetweenCallsNanoseconds}ns, next call is on $nextCallDateTime, waiting time is ${waitingTimeMilliseconds}ms" }

            if (waitingTimeMilliseconds > 0) {
                logger.debug { "Sleeping ${waitingTimeMilliseconds}ms until the next call is allowed..." }
                Thread.sleep(waitingTimeMilliseconds)
            }
        }

        logger.trace { "Waited until next call is allowed" }
    }

    /**
     * Sets the last call DateTime for an [caller] to now().
     * This should be called right after the call was finished.
     * @param caller The object which executes the call
     */
    fun setLastRequestDateTime(caller: Any) {
        lastCalls[caller] = LocalDateTime.now()
    }
}