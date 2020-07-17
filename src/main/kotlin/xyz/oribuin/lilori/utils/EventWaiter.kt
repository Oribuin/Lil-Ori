package xyz.oribuin.lilori.utils

import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.ShutdownEvent
import net.dv8tion.jda.api.hooks.EventListener
import net.dv8tion.jda.api.hooks.SubscribeEvent
import net.dv8tion.jda.internal.utils.Checks
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
import java.util.function.Predicate
import java.util.stream.Collectors

class EventWaiter @JvmOverloads constructor(threadpool: ScheduledExecutorService = Executors.newSingleThreadScheduledExecutor(), shutdownAutomatically: Boolean = true) : EventListener {
    private var waitingEvents = mutableMapOf<Class<*>, MutableSet<WaitingEvent<GenericEvent?>>>()
    private val threadpool: ScheduledExecutorService
    private val shutdownAutomatically: Boolean

    init {
        Checks.notNull(threadpool, "ScheduledExecutorService")
        Checks.check(!threadpool.isShutdown, "Cannot construct EventWaiter with a closed ScheduledExecutorService!")
        waitingEvents = HashMap()
        this.threadpool = threadpool
        this.shutdownAutomatically = shutdownAutomatically
    }

    val isShutdown: Boolean
        get() = threadpool.isShutdown

    fun <T : Event?> waitForEvent(classType: Class<T>, condition: Predicate<T>, action: Consumer<T>) {
        waitForEvent(classType, condition, action, -1, null, null)
    }

    private fun <T : Event?> waitForEvent(classType: Class<T>, condition: Predicate<T>, action: Consumer<T>, timeout: Long, unit: TimeUnit?, timeoutAction: Runnable?) {

        Checks.check(!isShutdown, "Attempted to register a WaitingEvent while the EventWaiter's threadpool was already shut down!")
        Checks.notNull(classType, "The provided class type")
        Checks.notNull(condition, "The provided condition predicate")
        Checks.notNull(action, "The provided action consumer")
        val we: WaitingEvent<*> = WaitingEvent(condition, action)
        val set = classType.let { waitingEvents.computeIfAbsent(it) { HashSet() } }

        // Where tf am i gonna get EventWaiter.WaitingEvent<GenericEvent?>> from to add it to set
        //set?.add(we)
        if (timeout > 0 && unit != null) {
            threadpool.schedule({ if (set.remove(we) && timeoutAction != null) timeoutAction.run() }, timeout, unit)
        }
    }

    @SubscribeEvent
    override fun onEvent(event: GenericEvent) {
        var c: Class<*>? = event.javaClass

        while (c != null) {
            waitingEvents[c]?.stream()?.filter { i -> i?.attempt(event)!! }?.collect(Collectors.toSet())

            if (event is ShutdownEvent && shutdownAutomatically) {
                threadpool.shutdown()
            }
            c = c.superclass
        }
    }

    fun shutdown() {
        if (shutdownAutomatically) throw UnsupportedOperationException("Shutting down EventWaiters that are set to automatically close is unsupported!")
        threadpool.shutdown()
    }

    inner class WaitingEvent<T> internal constructor(private val condition: Predicate<T>, private val action: Consumer<T>) {
        fun attempt(event: T): Boolean {
            if (condition.test(event)) {
                action.accept(event)
                return true
            }
            return false
        }

    }

}

