package cheng.build;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class SoundUtil {
    public static void playSound(Level _level, Entity serverPlayer, String sound, SoundSource soundSource){
        playsound(_level,serverPlayer,Objects.requireNonNull(ForgeRegistriesHelper.SoundEvent(sound)),soundSource);
    }
    public static void playSound(Level _level, Entity serverPlayer, SoundEvent sound, SoundSource soundSource){
        playsound(_level,serverPlayer,sound,soundSource);
    }
    public static void playSound(Level _level, Player serverPlayer, String sound, SoundSource soundSource){
        playsound(_level,serverPlayer,Objects.requireNonNull(ForgeRegistriesHelper.SoundEvent(sound)),soundSource);
    }
    public static void playSound(Level _level, Player serverPlayer, SoundEvent sound, SoundSource soundSource){
        playsound(_level,serverPlayer,sound,soundSource);
    }

    private static void playsound(Level _level, Entity serverPlayer, SoundEvent sound, SoundSource soundSource){
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
    private static void playsound(Level _level, Player player, SoundEvent sound, SoundSource soundSource){
        if (!_level.isClientSide()) {
            _level.playSound(player, new BlockPos(player.getX(), player.getY(), player.getZ()),
                    (sound),
                    soundSource,
                    1.0F,
                    1.0F
            );
        } else {
            _level.playLocalSound(
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    (sound),
                    soundSource,
                    1.0F,
                    1.0F,
                    false
            );
        }
    }
}
