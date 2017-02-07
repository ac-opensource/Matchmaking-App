package com.youniversals.playupgo.flux

/**
 * @author Gian Darren Aquino
 */
interface AliveUiThread {

    /**
     * Runs the [Runnable] if the current context is alive.
     */
    fun runOnUiThreadIfAlive(runnable: Runnable)

    /**
     * Runs the [Runnable] if the current context is alive.
     */
    fun runOnUiThreadIfAlive(runnable: Runnable, delayMillis: Long)
}
