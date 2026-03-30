package cheng.build.block;

import cheng.build.block.entity.FullbottlePurifierBlockEntity;
import cheng.build.init.InitModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class FullbottlePurifierBlock extends BaseEntityBlock implements EntityBlock{
    public static final IntegerProperty ANIMATION = IntegerProperty.create("animation", 0, (int) 1);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public FullbottlePurifierBlock() {
        super(BlockBehaviour.Properties.of(Material.STONE).sound(SoundType.STONE).strength(1f, 10f).noOcclusion());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }
    @Override
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0;
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ANIMATION, FACING);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.getBlock() != pNewState.getBlock()) {

            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);

            if (blockEntity instanceof FullbottlePurifierBlockEntity industrialProcessingUnit) {
                industrialProcessingUnit.drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    // 工作方块的Tick方法（为甚是放在这而不是方块实体里）
    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        }

        // 返回 ticker - Forge 会自动调用
        return createTickerHelper(type,
                InitModBlockEntities.fullbottle_purifler.get(),
                (level1,blockPos,blockState,block)->
                        block.tick(level1,blockPos,blockState,block));  // 方法引用

    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof FullbottlePurifierBlockEntity fullbottlePurifier){
            if (!pLevel.isClientSide()) {
                fullbottlePurifier.add(pPlayer.getMainHandItem(),pPlayer);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return InitModBlockEntities.fullbottle_purifler.get().create(pPos,pState);
    }
}
