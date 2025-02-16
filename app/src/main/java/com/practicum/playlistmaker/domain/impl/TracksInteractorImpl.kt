package com.practicum.playlistmaker.domain.impl

import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TrackConsumer) {
        executor.execute {
            try {
                val tracks = repository.searchTrack(expression)
                consumer.consume(tracks)
            } catch (e: Exception) {
                consumer.cunsumeError(e.localizedMessage)
            }

        }
    }
}