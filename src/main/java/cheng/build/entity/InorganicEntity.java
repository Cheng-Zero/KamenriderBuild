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

public class InorganicEntity extends EffectEntity{
    public InorganicEntity(Level pLevel,boolean isBestMatch,ResourceLocation texrure,Player player) {
        super(pLevel);
        this.setModel(GeoModelPath.BuildTankArmor.model());
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
            setSummonAnimation(0);
            DelayedTask.run(pLevel,10,()-> setSummonAnimation(3));
        }
        else {
            if (pLevel.getRandom().nextFloat() >= 0.5) {
                setSummonAnimation(1);
                DelayedTask.run(pLevel,10,()-> setSummonAnimation(4));
            }
            else {
                setSummonAnimation(2);
                DelayedTask.run(pLevel,10,()-> setSummonAnimation(5));
            }
        }
    }
    public void setSummonAnimation(int v){
        switch (v){
            case 0->this.setAnimation("summon");
            case 1->this.setAnimation("summon_inorganic");
            case 2->this.setAnimation("summon_inorganic2");
            case 3->this.setAnimation("best_match");
            case 4->this.setAnimation("on_inorganic");
            case 5->this.setAnimation("on_inorganic2");
        }
    }
    protected ItemStack loadItem(CompoundTag parent, String key) {
        return parent.contains(key) ? ItemStack.of(parent.getCompound(key)) : ItemStack.EMPTY;
    }
}
