package cheng.build.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.IAnimationTickable;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public abstract class HHHHNiceEntity extends AEntity {
    public static final EntityDataAccessor<String> Animation = SynchedEntityData.defineId(HHHHNiceEntity.class, EntityDataSerializers.STRING); ;
    protected HHHHNiceEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setAnimation("idle");
    }

    @Override
    public void tick() {
        super.tick();

        // 检查绑定玩家状态
        Player player = (Player) this.getOwner();
        if (player == null || !player.isAlive() || player.isRemoved()) {
            this.discard(); // 玩家不存在或已死亡时删除实体
            return;
        }

        if (player != null) {

//            ScaleData data = ScaleTypes.BASE.getScaleData(player);
//            float scale = data.getScale();
//            ScaleTypes.BASE.getScaleData(this).setScale(scale);

            // 跟随玩家位置和旋转
            double verticalOffset = 0;
            this.setPos(player.getX(), player.getY() + verticalOffset, player.getZ());
            this.setYRot(player.getYRot());
            this.yBodyRot = player.yBodyRot;
            this.yBodyRotO = player.yBodyRotO;
            this.setRot(player.getYRot(), player.getXRot());
            this.setOnGround(player.isOnGround());
        }

        if (player == null || !player.isAlive()) {
            this.discard();
        }
        if (player instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer() == GameType.SPECTATOR){
            this.discard();
        }
    }
    private double pushStrength = 1;  // 排斥力度
    private double pushRange = 6.0;      // 影响范围
    @Override
    protected void doPush(Entity entityIn) {
        entityIn.invulnerableTime = 0;
        entityIn.hurt(DamageSource.LIGHTNING_BOLT,1);
        // 计算排斥方向（从当前实体指向目标实体）
        double dx = entityIn.getX() - this.getX();
        double dz = entityIn.getZ() - this.getZ();

        // 计算距离
        double distance = Math.sqrt(dx * dx + dz * dz);

        if (distance > 0.1 && distance < pushRange) {
            // 归一化方向向量
            double nx = dx / distance;
            double nz = dz / distance;

            // 根据距离计算力度（距离越近，力度越大）
            double strength = pushStrength * (1 - distance / pushRange);

            // 只在水平方向施加力，保持垂直方向不变
            Vec3 currentMotion = entityIn.getDeltaMovement();
            entityIn.setDeltaMovement(
                    currentMotion.x + nx * strength,
                    currentMotion.y,
                    currentMotion.z + nz * strength
            );

            // 标记实体被推动
            entityIn.hurtMarked = true;
        }
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
        List<Entity> list = this.level.getEntities(this, this.getBoundingBox(), EntitySelector.pushableBy(this));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (!entity.equals(this.getOwner())) {
//                    super.doPush(entity);
                    this.doPush(entity);
                }
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putString("Animation",this.getAnimation());
    }
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("Animation"))
            this.setAnimation(pCompound.getString("Animation"));
    }
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(Animation, "idle");
    }
    public void setAnimation(String name){
        this.entityData.set(Animation,name);
    }
    public String getAnimation(){
        return this.entityData.get(Animation);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel pLevel, AgeableMob pOtherParent) {
        return null;
    }
    public static <T extends HHHHNiceEntity> void addEntity(T entity,Player player){
        if (entity == null || player == null) return;
        LevelAccessor world = player.level;
        if (world instanceof ServerLevel serverLevel) {
            if (serverLevel.isClientSide()) return;
            entity.setOwnerUUID(player.getUUID());
            double verticalOffset = 0.0;
            entity.setPos(player.getX(), player.getY() + verticalOffset, player.getZ());
            entity.setYRot(player.getYRot());
            entity.yBodyRot = player.yBodyRot;
            entity.yBodyRotO = player.yBodyRotO;
            entity.setRot(player.getYRot(), player.getXRot());
            entity.setNoGravity(true);
            serverLevel.addFreshEntity(entity);
        }
    }
    public static void setAnimation(LevelAccessor world,Player player,String animationName){
        final Vec3 _center = new Vec3(player.getX(), player.getY(), player.getZ());
        List<HHHHNiceEntity> _entfound = world.getEntitiesOfClass(HHHHNiceEntity.class, new AABB(_center, _center).inflate(4 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
        for (HHHHNiceEntity entityiterator : _entfound) {
            if (entityiterator != null && entityiterator.getOwner() == player)
                entityiterator.getEntityData().set(HHHHNiceEntity.Animation,animationName);
        }
    }
}
