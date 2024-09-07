package mod.crontent.bootiful.interfaces;


import net.minecraft.loot.LootPool;

import java.util.ArrayList;

public interface PoolConditionsModifyHelper {

    void bootiful$wrapCondition(ArrayList<String> filterItems);

    static void wrapCondition(LootPool.Builder o, ArrayList<String> filterItems) {
        ((PoolConditionsModifyHelper)o).bootiful$wrapCondition(filterItems);
    }

}
