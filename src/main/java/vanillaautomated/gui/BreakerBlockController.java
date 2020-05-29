package vanillaautomated.gui;

import io.github.cottonmc.cotton.gui.CottonCraftingController;
import io.github.cottonmc.cotton.gui.widget.WBar;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.Alignment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import vanillaautomated.VanillaAutomated;

public class BreakerBlockController extends CottonCraftingController {
    public BreakerBlockController(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, Text title) {
        super(RecipeType.SMELTING, syncId, playerInventory, getBlockInventory(context), getBlockPropertyDelegate(context));

        WMaxedPanel root = new WMaxedPanel();
        root.setSize(160, 150);
        setRootPanel(root);

        WGridPanel machinePanel = new WGridPanel();
        machinePanel.setSize(9, 3);

        WLabel label = new WLabel(title);
        label.setAlignment(Alignment.CENTER);
        root.add(label, 0, 0, 160, 10);

        WBar fire = new WBar(VanillaAutomated.flames_background, VanillaAutomated.flames, 0, 2, WBar.Direction.UP);
        machinePanel.add(fire, 3, 1);

        WItemSlot itemSlot = WItemSlot.of(blockInventory, 0);
        machinePanel.add(itemSlot, 3, 0);

        WItemSlot fuelSlot = WItemSlot.of(blockInventory, 1);
        machinePanel.add(fuelSlot, 3, 2);

        WBar progress = new WBar(VanillaAutomated.progress_background, VanillaAutomated.progress, 1, 3, WBar.Direction.RIGHT);
        machinePanel.add(progress, 5, 1);

        root.add(machinePanel, 0, 10);

        WLabel inventoryLabel = new WLabel(new TranslatableText("container.inventory"));
        inventoryLabel.setSize(256, 10);

        root.add(inventoryLabel, 0, 64);
        root.add(this.createPlayerInventoryPanel(), 0, 74);
        root.validate(this);
    }
}
