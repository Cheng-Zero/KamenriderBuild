package cheng.build.api;

import cheng.build.item.bottle.bottle.FullBottle;

import java.util.function.Supplier;

public interface IOrganicMatterBottle extends IFullBottle{
    /// 返回 满装瓶
    FullBottle getFullBottle();
}
