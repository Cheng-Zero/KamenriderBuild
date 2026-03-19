package cheng.build.entity;

import cheng.build.Build;
import cheng.build.GeoModelPath;
import cheng.build.init.InitEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Comparator;
import java.util.List;

public class EffectEntity extends EffectEntityPredecessor {
    private final AnimationFactory factory;
    private Level level;
    // 文件数据
    public static final EntityDataAccessor<String> model = SynchedEntityData.defineId(EffectEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> texture = SynchedEntityData.defineId(EffectEntity.class, EntityDataSerializers.STRING);
    public static final EntityDataAccessor<String> animations = SynchedEntityData.defineId(EffectEntity.class, EntityDataSerializers.STRING);
    // 动画控制数据
    public static final EntityDataAccessor<String> Animation = SynchedEntityData.defineId(EffectEntity.class, EntityDataSerializers.STRING);

    public EffectEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.factory = GeckoLibUtil.createFactory(this);
        this.level = pLevel;
    }
    public EffectEntity(Level pLevel) {
        this(InitEntity.effect_entity.get(), pLevel);
        this.level = pLevel;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("Model",this.getModel().toString());
        pCompound.putString("Texture",this.getTexture().toString());
        pCompound.putString("Animations",this.getAnimations().toString());
        pCompound.putString("Animation",this.getAnimation());
    }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Model"))
            this.setModel(new ResourceLocation(pCompound.getString("Model")));
        if (pCompound.contains("Texture"))
            this.setTexture(new ResourceLocation(pCompound.getString("Texture")));
        if (pCompound.contains("Animations"))
            this.setAnimations(new ResourceLocation(pCompound.getString("Animations")));
        if (pCompound.contains("Animation"))
            this.setAnimation(pCompound.getString("Animation"));
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(model, GeoModelPath.BuildRabbatArmor.model().toString());
        this.entityData.define(texture, GeoModelPath.BuildRabbatArmor.texture().toString());
        this.entityData.define(animations, GeoModelPath.BuildRabbatArmor.animation().toString());
        this.entityData.define(Animation, "idle");
    }
    // 数据设置
    public void setModel(ResourceLocation Model) {
        this.entityData.set(model, Model.toString());
    }
    public void setTexture(ResourceLocation Texture) {
        this.entityData.set(texture, Texture.toString());
    }
    public void setAnimations(ResourceLocation Animation) {
        this.entityData.set(animations, Animation.toString());
    }
    public void setAnimation(String name){
        Build.LOGGER.debug("设置当前实体{}动画为{}",this,name);
        this.entityData.set(Animation,name);
    }
    // 数据获取
    public ResourceLocation getModel() {
        return new ResourceLocation(this.entityData.get(model));
    }
    public ResourceLocation getTexture() {
        return new ResourceLocation(this.entityData.get(texture));
    }
    public ResourceLocation getAnimations() {
        return new ResourceLocation(this.entityData.get(animations));
    }
    public String getAnimation(){
        return this.entityData.get(Animation);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this,"animation",1,this::animation));
    }
    private <P extends IAnimatable> PlayState animation(AnimationEvent<P> event){
        if (!this.getAnimation().isEmpty() && this.getAnimation() != null) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.getAnimation(), ILoopType.EDefaultLoopTypes.HOLD_ON_LAST_FRAME));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }
    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public int tickTimer() {
        return tickCount;
    }

    // 实时设置方法
    public void setdiscard(Player player){
        for (EffectEntity entityiterator : getEntity(player))
            if (entityiterator != null && entityiterator.getOwner() == player)
                entityiterator.discard();
    }
    public void setModel(Player player, String modelPath){
        for (EffectEntity entityiterator : getEntity(player))
            if (entityiterator != null && entityiterator.getOwner() == player)
                entityiterator.getEntityData().set(model,modelPath);
    }
    public void setTexture(Player player, String texturePath){
        for (EffectEntity entityiterator : getEntity(player))
            if (entityiterator != null && entityiterator.getOwner() == player)
                entityiterator.getEntityData().set(texture,texturePath);
    }
    public void setAnimations(Player player, String animationPath){
        for (EffectEntity entityiterator : getEntity(player))
            if (entityiterator != null && entityiterator.getOwner() == player)
                entityiterator.getEntityData().set(animations,animationPath);
    }
    public void setAnimation(Player player, String animationName){
        for (EffectEntity entityiterator : getEntity(player))
            if (entityiterator != null && entityiterator.getOwner() == player)
                entityiterator.getEntityData().set(Animation,animationName);
    }
    public List<EffectEntity> getEntity(Player player){
        final Vec3 playerXYZ = new Vec3(player.getX(), player.getY(), player.getZ());
        AABB aabb = new AABB(playerXYZ,playerXYZ).inflate(4/2d);
        // 获取世界实体类型(Class)  目标实体.class    获取范围         条件判断具体目标
        return level.getEntitiesOfClass(EffectEntity.class, aabb, e -> true)
                // 转换成 数据流
                .stream()
                // 距离排序（影响处理实体先后顺序）
                .sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(playerXYZ)))
                // 转换为List列表
                .toList();
    }

}
