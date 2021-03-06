package vanillaautomated.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Nameable;
import vanillaautomated.VanillaAutomated;

import java.util.logging.Logger;

public class MobFarmBlockController extends CottonCraftingController {
    public MobFarmBlockController(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, Text title) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WMaxedPanel root = new WMaxedPanel();
        root.setSize(160, 150);
        setRootPanel(root);

        WGridPanel machinePanel = new WGridPanel();
        machinePanel.setSize(9, 3);

        WLabel label = new WLabel(title);
        label.setAlignment(Alignment.CENTER);
        root.add(label, 0, 0, 160, 10);

        WItemSlot mobSlot = WItemSlot.of(blockInventory, 0);
        machinePanel.add(mobSlot, 2, 0);

        WBar fire = new WBar(VanillaAutomated.flames_background, VanillaAutomated.flames, 0, 2, WBar.Direction.UP);
        machinePanel.add(fire, 2, 1);

        WItemSlot fuelSlot = WItemSlot.of(blockInventory, 1);
        machinePanel.add(fuelSlot, 2, 2);

        WBar progress = new WBar(VanillaAutomated.progress_background, VanillaAutomated.progress, 1, 3, WBar.Direction.RIGHT);
        machinePanel.add(progress, 3, 2);

        int lastSlotIndex = 2;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                WItemSlot outputSlot = WItemSlot.of(blockInventory, lastSlotIndex).setInsertingAllowed(false);
                machinePanel.add(outputSlot, j + 4, i);
                lastSlotIndex++;
            }
        }

        root.add(machinePanel, 0, 10);

        WLabel inventoryLabel = new WLabel(new TranslatableText("container.inventory"));
        inventoryLabel.setSize(256, 10);

        root.add(inventoryLabel, 0, 64);
        root.add(this.createPlayerInventoryPanel(), 0, 74);
        root.validate(this);
    }
}
