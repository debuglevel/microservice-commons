package de.debuglevel.microservicecommons.statistics

/**
 * Holds information about how many [calls] were made from a [caller] (e.g. a class instance)
 * within a specific [scope] (e.g. "POST /foobar" or "default" or null).
 * [durationSum] counts how long all calls took in sum (milliseconds), and
 * [averageDuration] how long a call takes in average.
 */
data class CallDuration(
    val caller: Any,
    val scope: Any?,
    var calls: Int = 0,
    var durationSum: Double? = null,
) {
    val averageDuration: Double?
        get() {
            return durationSum?.div(calls)
        }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CallDuration

        if (caller != other.caller) return false
        if (scope != other.scope) return false
        if (calls != other.calls) return false
        if (durationSum != other.durationSum) return false

        return true
    }

    override fun hashCode(): Int {
        var result = caller.hashCode()
        result = 31 * result + scope.hashCode()
        return result
    }
}
