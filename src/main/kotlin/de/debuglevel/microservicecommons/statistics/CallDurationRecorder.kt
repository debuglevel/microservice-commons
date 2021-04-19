package de.debuglevel.microservicecommons.statistics

import mu.KotlinLogging
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

object CallDurationRecorder {
    private val logger = KotlinLogging.logger {}

    private val _callDurations = mutableMapOf<Pair<Any, Any?>, CallDuration>()
    val callDurations: Map<Pair<Any, Any?>, CallDuration>
        get() = _callDurations

    /**
     * Records a call [duration] from a [caller] (e.g. a class instance, but can be anything)
     * within a [scope] which identifies and groups the same or similar
     * calls (e.g. "POST /foobar", "default", "call to Google API" or null)
     * and returns the summarizing [CallDuration] object.
     */
    @ExperimentalTime
    fun record(caller: Any, scope: Any?, duration: Duration): CallDuration {
        logger.trace { "Recording call duration..." }

        val key = Pair(caller, scope)
        val callDuration = if (callDurations.contains(key)) {
            callDurations.getValue(key)
        } else {
            val callDuration = CallDuration(caller, scope)
            _callDurations[key] = callDuration
            callDuration
        }

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