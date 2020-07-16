package xyz.oribuin.lilori.utils

import net.dv8tion.jda.api.events.GenericEvent
import java.util.function.Consumer
import java.util.function.Predicate

class WaitingEvent<T : GenericEvent?> (private val condition: Predicate<T>, private val action: Consumer<T>) {
    fun attempt(event: T): Boolean {
        if (condition.test(event)) {
            action.accept(event)
            return true
        }
        return false
    }
}