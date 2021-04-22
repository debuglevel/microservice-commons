package de.debuglevel.microservicecommons.backoff

import mu.KotlinLogging
import java.time.Duration
import kotlin.math.roundToLong
import kotlin.random.Random

object DurationRandomizer {
    private val logger = KotlinLogging.logger {}

    /**
     * Randomize a [duration] within a [maximumDifference].
     * @param duration The original duration to add some randomness to
     * @param maximumDifference The maximum difference to multiply to the duration; must be non-negative
     *
     * maximumDifference = 0.25 and duration = 1min result in a range from 0.75min to 1.25min
     *
     * @param randomSeed A seed to base the randomness on (should be tied to an item and only change when the item is modified, i.e. hashcode is a bad choice); null to use a non-deterministic random number generator.
     */
    fun randomizeDuration(
        duration: Duration,
        maximumDifference: Double,
        randomSeed: Int? = null,
    ): Duration {
        require(maximumDifference >= 0) { "Maximum difference must be non-negative." }
        logger.trace { "Randomizing duration $duration (maximumDifference=$maximumDifference)..." }

        val random = if (randomSeed != null) {
            logger.trace { "Using seed=$randomSeed..." }
            Random(randomSeed)
        } else {
            logger.trace { "Using no seed..." }
            Random.Default
        }

        val durationMilliseconds = duration.toMillis()
        val differenceMultiplier = random.nextDouble(1 - maximumDifference, 1 + maximumDifference)
        val randomizedDurationSeconds = durationMilliseconds * differenceMultiplier
        val randomizedDuration = Duration.ofMillis(randomizedDurationSeconds.roundToLong())

        logger.trace { "Randomized duration $duration (maximumDifference=$maximumDifference): $randomizedDuration" }
        return randomizedDuration
    }
}