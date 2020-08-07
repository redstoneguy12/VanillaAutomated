package vanillaautomated.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class DummyScreenHandler extends ScreenHandler {
    public DummyScreenHandler(ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void onContentChanged(Inventory inventory) { }
}
