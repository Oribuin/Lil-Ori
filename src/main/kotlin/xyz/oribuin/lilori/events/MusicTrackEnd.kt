package xyz.oribuin.lilori.events

import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.events.Event

class MusicTrackEnd(val guild: Guild, val trackEndReason: AudioTrackEndReason, val audioTrack: AudioTrack, jda: JDA, responseNumber: Long) : Event(jda, responseNumber)

