package com.mwshubham.notes.navigation

import kotlinx.serialization.Serializable

/**
 * Nav3 navigation keys — each sealed subtype is a serializable destination.
 * The backstack is a [SnapshotStateList<NavKey>], which Nav3 observes directly.
 */
@Serializable
sealed interface NavKey : androidx.navigation3.runtime.NavKey

@Serializable
data object Splash : NavKey

@Serializable
data object HelloWorld : NavKey

@Serializable
data object NoteList : NavKey

@Serializable
data class NoteDetail(
    /** Null means "add new note"; non-null means "edit existing note". */
    val id: Long? = null
) : NavKey
