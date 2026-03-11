package cheng.datagen;

import cheng.build.init.InitItem;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModModelsProvider extends ItemModelProvider {
    public ModModelsProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(InitItem.buildDriver.get());
        itemModelBuilder(InitItem.smash_bottle.get());
        itemModelBuilder(InitItem.empty_bottle.get());
        itemModelBuilder(InitItem.rabbat.get());
        itemModelBuilder(InitItem.tank.get());
//        withExistingParent(InitItem.rabbat.get().toString(),Build.GeoModelPath.RabbatBottleTexture);
    }

    private void itemModelBuilder(Item item){
        getBuilder("item/" + item.toString())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("layer0", modLoc("bottles/" + item))
                .transforms()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                .translation(0, -3, 0)
                .end()
                .transform(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND)
                .translation(0, -3, 0)
                .end()
                .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, -180, 0)
                .scale(2, 2, 2)
                .end()
                .transform(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND)
                .rotation(0, -180, 0)
                .scale(2, 2, 2)
                .end()
                .transform(ItemTransforms.TransformType.GROUND)
                .translation(0, -5, 0)
                .scale(2, 2, 2)
                .end()
                .transform(ItemTransforms.TransformType.GUI)
                .rotation(0, -180, 40)
                .translation(-2.5f, -3.5f, 0)
                .scale(4, 4, 4)
                .end()
                .transform(ItemTransforms.TransformType.FIXED)
                .translation(0, -2, -1)
                .scale(3, 3, 3)
                .end()
                .end();
    }
    private void registerBaseItem(Item item) {
        // 使用 ModelBuilder 创建自定义模型
        getBuilder("item/"+item.toString()+"_bottle")
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .texture("layer0", modLoc("item/" + item+"_bottle"));
    }
}
