package com.youniversals.playupgo.flux

/**
 * @author Gian Darren Aquino
 */
class Action(
        val type: String,
        val data: Any?
) {
    companion object {
        val ACTION_NO_ACTION = "ACTION_NO_ACTION"
    }
}
