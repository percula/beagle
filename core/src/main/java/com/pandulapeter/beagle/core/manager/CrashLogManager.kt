package com.pandulapeter.beagle.core.manager

import android.app.Application
import com.pandulapeter.beagle.commonBase.currentTimestamp
import com.pandulapeter.beagle.commonBase.randomId
import com.pandulapeter.beagle.core.util.CrashLogEntry
import com.pandulapeter.beagle.core.util.extension.CRASH_LOG_PREFIX
import com.pandulapeter.beagle.core.util.extension.createPersistedCrashLogFile
import com.pandulapeter.beagle.core.util.extension.getPersistedLogsFolder
import com.pandulapeter.beagle.core.util.extension.readCrashLogEntryFromLogFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class CrashLogManager {
    var application: Application? = null
        set(value) {
            field = value
            syncIfNeeded()
        }
    private var isSyncReady = false
    private val entries = mutableListOf<CrashLogEntry>()
        get() {
            syncIfNeeded()
            return field
        }

    init {
        syncIfNeeded()
    }

    fun log(exception: Exception) {
        val entry = CrashLogEntry(
            id = randomId,
            exception = exception.message ?: "Unknown",
            stacktrace = exception.stackTraceToString(),
            timestamp = currentTimestamp
        )
        synchronized(entries) {
            entries.removeAll { it.id == entry.id }
            entries.add(0, entry)
            entries.sortByDescending { it.timestamp }
        }
        GlobalScope.launch(Dispatchers.IO) {
            application?.createPersistedCrashLogFile(entry)
        }
    }

    fun clearLogs() {
        synchronized(entries) {
            entries.clear()
        }
        GlobalScope.launch(Dispatchers.IO) {
            application?.getPersistedLogsFolder()?.listFiles()?.forEach { file ->
                if (file.name.startsWith(CRASH_LOG_PREFIX)) {
                    file.delete()
                }
            }
        }
    }

    fun getCrashLogEntries(): List<CrashLogEntry> = synchronized(entries) {
        entries.toList()
    }

    private fun syncIfNeeded() {
        if (!isSyncReady) {
            application?.let { context ->
                GlobalScope.launch(Dispatchers.IO) {
                    val persistedEntries = context.getPersistedLogsFolder().listFiles()?.mapNotNull { file ->
                        if (file.name.startsWith(CRASH_LOG_PREFIX)) readCrashLogEntryFromLogFile(file) else null
                    }.orEmpty()
                    isSyncReady = true
                    if (persistedEntries.isNotEmpty()) {
                        synchronized(entries) {
                            val allEntries = (entries + persistedEntries).distinctBy { it.id }.sortedByDescending { it.timestamp }
                            entries.clear()
                            entries.addAll(allEntries)
                        }
                    }
                }
            }
        }
    }
}