package com.lunazstudios.cobblefurnies.client.sound;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.resources.Identifier;

public final class EntitySoundTracker {

    private static final SoundTracker<Integer> INSTANCE = new SoundTracker<>() {};

    private EntitySoundTracker() {
    }

    public static boolean isActive(int id, Identifier location) {
        return INSTANCE.isActive(id, location);
    }

    public static void play(int id, SoundInstance sound) {
        INSTANCE.play(id, sound);
    }

    public static void stop(int id, SoundInstance sound) {
        INSTANCE.stop(id, sound);
    }

    public static void stop(int id, Identifier location) {
        INSTANCE.stop(id, location);
    }

    public static void stopAll() {
        INSTANCE.stopAll();
    }

    public static void stopAll(int id) {
        INSTANCE.stopAll(id);
    }

    public static void clear() {
        INSTANCE.clear();
    }

    public static void clear(int id) {
        INSTANCE.clear(id);
    }
}