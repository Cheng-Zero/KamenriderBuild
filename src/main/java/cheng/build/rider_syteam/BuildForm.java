package cheng.build.rider_syteam;

import cheng.build.api.ISkill;
import cheng.build.item.bottle.bottle.FullBottle;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

import java.util.List;

public class BuildForm {
    private final String name;
    private final FullBottle OrganicBottle;  // 右侧瓶罐（通常是无机物）
    private final List<ISkill> ability;

    public BuildForm(String name, FullBottle right, List<ISkill> ability) {
        this.name = name;
        this.OrganicBottle = right;
        this.ability = ability;
    }

    public String getName() { return name; }
    public FullBottle getOrganicBottle() { return OrganicBottle; }
    public List<ISkill> getAbility() { return ability; }
}
