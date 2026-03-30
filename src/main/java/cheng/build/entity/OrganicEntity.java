package cheng.build.entity;

import cheng.build.DelayedTask;
import cheng.build.GeoModelPath;
import cheng.build.data.DataManager;
import cheng.build.data.PlayerBuildData;
import cheng.build.item.armor.BuildDriver;
import cheng.build.program.RotaryDriverKeyProgram.RotaryDriverBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class OrganicEntity extends EffectEntity{
    public OrganicEntity(Level pLevel, boolean isBestMatch, ResourceLocation texrure, Player player) {
        super(pLevel);
        this.setModel(GeoModelPath.BuildRabbatArmor.model());
        this.setTexture(texrure);
        this.setAnimations(new ResourceLocation("kamenrider_build:animations/build_henshin.animation.json"));

        RotaryDriverBase util = new RotaryDriverBase() {};
        util.update(player);

        var helmetStack = player.getItemBySlot(EquipmentSlot.HEAD);
        var chestplateStack = player.getItemBySlot(EquipmentSlot.CHEST);
        PlayerBuildData data = DataManager.get(player);
        CompoundTag driverTag = data.getDriverTag();
        ItemStack
                inorganic = loadItem(driverTag, BuildDriver.inorganicMatter_item_Name),
                organic = loadItem(driverTag, BuildDriver.organicMatter_item_Name);

        boolean c =
                util.CurrentFormNotBottle(inorganic,chestplateStack) &&
                util.CurrentFormNotBottle(organic,helmetStack);

        if (isBestMatch || c){
            this.setAnimation("summon");
            DelayedTask.run(pLevel,10,()->{
                this.setAnimation("best_match");
            });
        }
        else {
            if (pLevel.getRandom().nextFloat() >= 0.5) {
                this.setAnimation("summon_organic");
                DelayedTask.run(pLevel,10,()->{
                    this.setAnimation("on_organic");
                });
            }
            else {
                this.setAnimation("summon_organic2");
                DelayedTask.run(pLevel,10,()->{
                    this.setAnimation("on_organic2");
                });
            }
        }
    }
    protected ItemStack loadItem(CompoundTag parent, String key) {
        return parent.contains(key) ? ItemStack.of(parent.getCompound(key)) : ItemStack.EMPTY;
    }
}
