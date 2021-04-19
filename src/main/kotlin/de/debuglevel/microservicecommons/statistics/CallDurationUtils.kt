package de.debuglevel.microservicecommons.statistics

import mu.KotlinLogging
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

object CallDurationUtils {
    private val logger = KotlinLogging.logger {}

    private val callDurations = mutableSetOf<CallDuration>()

    /**
     * Records a call [duration] from a [caller] (e.g. a class instance, but can be anything)
     * within a [scope] which identifies and groups the same or similar
     * calls (e.g. "POST /foobar", "default", "call to Google API" or null)
     * and returns the summarizing [CallDuration] object.
     */
    @ExperimentalTime
    fun record(caller: Any, scope: Any, duration: Duration): CallDuration {
        logger.trace { "Recording call duration..." }

        val callDuration = callDurations.firstOrNull { it.caller == caller && it.scope == scope }
            ?: CallDuration(caller, scope)

        val oldDurationSum = callDuration.durationSum
        callDuration.durationSum = when (oldDurationSum) {
            null -> duration.inSeconds
            else -> oldDurationSum + duration.inSeconds
        }
        callDuration.calls += 1

        logger.trace { "Recorded call duration: $callDuration" }
        return callDuration
    }
}