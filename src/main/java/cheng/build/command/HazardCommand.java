package cheng.build.command;

import cheng.build.var.ModVariables;
import cheng.build.var.PlayerVariables;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

@Mod.EventBusSubscriber
public class HazardCommand {
    @SubscribeEvent
    public static void command(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                Commands.literal("kamenrider")
                        // 扩展于kamenrider
                        .then(Commands.literal("build")
                                .then(Commands.literal("hazard_level")
                                        // 扩展于hazard_level
                                        .then(Commands.argument("player", EntityArgument.player()).requires(d -> d.hasPermission(2))
                                                .then(Commands.argument("argument", FloatArgumentType.floatArg(1)).executes(HazardCommand::setHazardLevel))
                                                .then(Commands.literal("get").executes(HazardCommand::getHazardLevel))
                                        )
                                        .executes(HazardCommand::predictHazardLevel)
                                )
                        )
        );
    }

    /**
     * 设置危险等级
     */
    private static int setHazardLevel(CommandContext<CommandSourceStack> context){
        try {
            // 获取实体
            ServerPlayer player = EntityArgument.getPlayer(context,"player");
            float argument = FloatArgumentType.getFloat(context, "argument");
                LazyOptional<PlayerVariables> player_capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null);
                player_capability.ifPresent(c-> {
                    c.hazard_level = argument;
                    c.syncPlayerVariables(player);
                });
                logSetHazardLevel(context.getSource(),player,argument);
        } catch (RuntimeException | CommandSyntaxException ignored) {}
        return 1;
    }
    /**
     * 获取危险等级
     */
    private static int getHazardLevel(CommandContext<CommandSourceStack> context){
        try {
            // 获取实体
            ServerPlayer player = EntityArgument.getPlayer(context,"player");
            LazyOptional<PlayerVariables> player_capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null);
            double hazardLevel = player_capability.orElse(new PlayerVariables()).hazard_level;
            context.getSource().sendSuccess(new TranslatableComponent("commands.kamenrider_build.hazard_level.get",player.getName(),hazardLevel),true);
        } catch (RuntimeException | CommandSyntaxException ignored) {}

        return 1;
    }
    /**
     * 非准确获取危险等级(误差有±0.5)
     */
    private static int predictHazardLevel(CommandContext<CommandSourceStack> context){
        try {
            // 获取实体
            Entity entity = context.getSource().getEntity();
            if (entity instanceof Player player) {
                LazyOptional<PlayerVariables> player_capability = player.getCapability(ModVariables.PLAYER_VARIABLES_CAPABILITY, null);
                double hazardLevel = player_capability.orElse(new PlayerVariables()).hazard_level;
                float randomOffset = (float) (Math.random() - 0.5);  // -0.5 到 0.5
                double v = hazardLevel + randomOffset;
                // 加一位小数，以返回需要的
                double PredictHazardLevel = Math.round(v*10)/10.0;
                context.getSource().sendSuccess(new TranslatableComponent("commands.kamenrider_build.hazard_level.get", player.getName(), PredictHazardLevel), true);
            }
        } catch (RuntimeException ignored) {}

        return 1;
    }

    private static void logSetHazardLevel(CommandSourceStack pSource,ServerPlayer pPlayer,float a) {
        if (pSource.getEntity() == pPlayer) {
            pSource.sendSuccess(new TranslatableComponent("commands.kamenrider_build.hazard_level.success.self", a), true);
        } else {
            if (pSource.getLevel().getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
                pPlayer.sendMessage(new TranslatableComponent("commands.kamenrider_build.hazard_level.changed", a), Util.NIL_UUID);
            }

            pSource.sendSuccess(new TranslatableComponent("commands.kamenrider_build.hazard_level.success.other", pPlayer.getDisplayName(),a), true);
        }
    }
}
