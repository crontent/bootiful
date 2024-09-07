package mod.crontent.bootiful.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.util.ActionResult;

public interface EntityLandingCallback {
    Event<EntityLandingCallback> EVENT = EventFactory.createArrayBacked(EntityLandingCallback.class,
            (listeners) -> (entity) -> {
                for (EntityLandingCallback listener : listeners) {
                    ActionResult result = listener.doAction(entity);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult doAction(Entity entity);
}
