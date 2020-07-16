package xyz.oribuin.lilori.utils

import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.hooks.EventListener
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors
import javax.annotation.Nonnull

class EventWaiter(private val threadpool: ScheduledExecutorService) : EventListener {
    private val waitingEvents: HashMap<Class<*>, MutableSet<WaitingEvent<*>>> = HashMap()


    val isShutdown: Boolean
        get() = threadpool.isShutdown

    fun <T : Event?> waitForEvent(tClass: Class<T>, condition: Predicate<T>, action: Consumer<T>, timeout: Long, timeUnit: TimeUnit, timeoutAction: Runnable?) {
        val waitingEvent: WaitingEvent<*> = WaitingEvent(condition, action)
        val eventSet = waitingEvents.computeIfAbsent(tClass) { HashSet() }

        eventSet.add(waitingEvent)
        if (timeout > 0) {
            threadpool.schedule({
                timeoutAction?.run()
            }, timeout, timeUnit)
        }
    }

    override fun onEvent(@Nonnull event: GenericEvent) {

        val cClass: Class<*> = event.javaClass
        if (waitingEvents.containsKey(cClass)) {
            // Out projected prohibits the use of 'public final fun attempt (event: T) Boolean? defined in xyz.oribuin.lilori.utils.WaitingEvent
            waitingEvents[cClass]?.stream()?.filter { i -> i?.attempt(event)!! }?.collect(Collectors.toSet())
        }

        if (event is ShutdownEvent) {
            threadpool.shutdown()
        }
    }

}