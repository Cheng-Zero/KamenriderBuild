package cheng.build;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class SoundUtil {
    public static void playSound(Level _level, ServerPlayer serverPlayer, String sound, SoundSource soundSource){
        if (!_level.isClientSide()) {
            _level.playSound(null, new BlockPos(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()),
                    Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(sound))),
                    soundSource,
                    1.0F,
                    1.0F
            );
        } else {
            _level.playLocalSound(
                    serverPlayer.getX(),
                    serverPlayer.getY(),
                    serverPlayer.getZ(),
                    Objects.requireNonNull(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(sound))),
                    soundSource,
                    1.0F,
                    1.0F,
                    false
            );
        }
    }
    public static void playSound(Level _level, ServerPlayer serverPlayer, SoundEvent sound, SoundSource soundSource){
        if (!_level.isClientSide()) {
            _level.playSound(null, new BlockPos(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ()),
                    (sound),
                    soundSource,
                    1.0F,
                    1.0F
            );
        } else {
            _level.playLocalSound(
                    serverPlayer.getX(),
                    serverPlayer.getY(),
                    serverPlayer.getZ(),
                    (sound),
                    soundSource,
                    1.0F,
                    1.0F,
                    false
            );
        }
    }
}
