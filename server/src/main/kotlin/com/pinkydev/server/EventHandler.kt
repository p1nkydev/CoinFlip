package com.pinkydev.server

import com.pinkydev.common.event.SocketEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EventHandler {
    private val _events = MutableStateFlow<SocketEvent?>(null)
    val events: StateFlow<SocketEvent?> = _events

    fun notifyThat(event: SocketEvent) {
        _events.value = event
    }
}