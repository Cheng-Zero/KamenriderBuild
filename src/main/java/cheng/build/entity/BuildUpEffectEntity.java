package cheng.build.entity;

import cheng.build.init.InitEntity;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class BuildUpEffectEntity extends EffectEntity {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public BuildUpEffectEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
		super(type, worldIn);
	}

	public BuildUpEffectEntity(Level level){
		super(InitEntity.build_up.get(), level);
	}

	@Override
	public AnimationFactory getFactory() {
		return this.factory;
	}

	@Override
	public int tickTimer() {
		return tickCount;
	}
}
