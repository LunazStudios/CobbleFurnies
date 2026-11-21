package com.lunazstudios.cobblefurnies.client.sound;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.resources.Identifier;

public abstract class SoundTracker<T> {

    protected final SoundManager manager = Minecraft.getInstance().getSoundManager();
    protected final Map<T, Map<Identifier, SoundInstance>> trackedSounds = new HashMap<>();

    public boolean isActive(T id, Identifier location) {
        Map<Identifier, SoundInstance> emitter = trackedSounds.get(id);
        if (emitter == null) {
            return false;
        }
        SoundInstance existing = emitter.get(location);
        return existing != null && manager.isActive(existing);
    }

    public void play(T id, SoundInstance sound) {
        Map<Identifier, SoundInstance> tracks = trackedSounds.computeIfAbsent(id, k -> new HashMap<>());
        SoundInstance existing = tracks.get(sound.getIdentifier());
        if (existing != null) {
            manager.stop(existing);
        }
        tracks.put(sound.getIdentifier(), sound);
        manager.play(sound);
    }

    public void stop(T id, SoundInstance sound) {
        stop(id, sound.getIdentifier());
    }

    public void stop(T id, Identifier location) {
        Map<Identifier, SoundInstance> emitter = trackedSounds.get(id);
        if (emitter == null) {
            return;
        }
        SoundInstance existing = emitter.remove(location);
        if (existing != null) {
            manager.stop(existing);
        }
    }

    public void stopAll() {
        for (Map<Identifier, SoundInstance> tracks : trackedSounds.values()) {
            for (SoundInstance sound : tracks.values()) {
                manager.stop(sound);
            }
            tracks.clear();
        }
    }

    public void stopAll(T id) {
        Map<Identifier, SoundInstance> tracks = trackedSounds.get(id);
        if (tracks == null) {
            return;
        }
        for (SoundInstance sound : tracks.values()) {
            manager.stop(sound);
        }
        tracks.clear();
    }

    public SoundInstance get(T id, Identifier location) {
        Map<Identifier, SoundInstance> emitter = trackedSounds.get(id);
        if (emitter == null) return null;
        return emitter.get(location);
    }

    public void clear() {
        stopAll();
        trackedSounds.clear();
    }

    public void clear(T id) {
        stopAll(id);
        trackedSounds.remove(id);
    }

    public static void stopAllTrackers() {
        BlockEntitySoundTracker.stopAll();
        EntitySoundTracker.stopAll();
    }

    public static void clearAllTrackers() {
        stopAllTrackers();
        BlockEntitySoundTracker.clear();
        EntitySoundTracker.clear();
    }
}