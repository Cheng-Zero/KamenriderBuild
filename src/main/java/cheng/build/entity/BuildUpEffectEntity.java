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

public class BuildUpEffectEntity extends HHHHNiceEntity {
	public AnimationFactory factory = GeckoLibUtil.createFactory(this);

	public BuildUpEffectEntity(EntityType<? extends TamableAnimal> type, Level worldIn) {
		super(type, worldIn);
	}

	public BuildUpEffectEntity(Level level){
		super(InitEntity.build_up.get(), level);
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
}
