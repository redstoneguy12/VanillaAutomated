package vanillaautomated;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import vanillaautomated.blockentities.CrafterBlockEntity;
import vanillaautomated.blockentities.TimerBlockEntity;
import vanillaautomated.recipes.CrusherRecipe;
import vanillaautomated.recipes.CrusherRecipeSerializer;
import vanillaautomated.recipes.FarmerRecipe;
import vanillaautomated.recipes.FarmerRecipeSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

public class VanillaAutomated implements ModInitializer {

    public static String prefix = "vanillaautomated";
    public static Identifier flames = new Identifier(prefix, "textures/gui/flames.png");
    public static Identifier flames_background = new Identifier(prefix, "textures/gui/flames_background.png");
    public static Identifier progress = new Identifier(prefix, "textures/gui/progress.png");
    public static Identifier progress_background = new Identifier(prefix, "textures/gui/progress_background.png");
    public static Identifier bucket_slot = new Identifier(prefix, "textures/gui/bucket_slot.png");
    public static Identifier tool_slot = new Identifier(prefix, "textures/gui/tool_slot.png");
    public static Identifier timer_configuration_packet = new Identifier(prefix, "timer_configuration");
    public static Identifier crafter_reset_packet = new Identifier(prefix, "crafter_reset");

    public static final ItemGroup ITEM_GROUP = FabricItemGroupBuilder.build(
            new Identifier(prefix, "machines"),
            () -> new ItemStack(VanillaAutomatedBlocks.machineBlock));

    public static RecipeType farmerRecipeType;
    public static RecipeType crusherRecipeType;

    // Vanilla stuff
    public static final Gson gson = new Gson();

    public static ArrayList<CraftingRecipe> craftingRecipes;

    @Override
    public void onInitialize() {
        VanillaAutomatedBlocks.register();
        VanillaAutomatedItems.register();

        // Recipe type
        Registry.register(Registry.RECIPE_SERIALIZER, FarmerRecipeSerializer.ID, FarmerRecipeSerializer.INSTANCE);
        farmerRecipeType = Registry.register(Registry.RECIPE_TYPE, new Identifier(prefix, FarmerRecipe.Type.ID), FarmerRecipe.Type.INSTANCE);
        Registry.register(Registry.RECIPE_SERIALIZER, CrusherRecipeSerializer.ID, CrusherRecipeSerializer.INSTANCE);
        crusherRecipeType = Registry.register(Registry.RECIPE_TYPE, new Identifier(prefix, CrusherRecipe.Type.ID), CrusherRecipe.Type.INSTANCE);

        // Packets
        registerTimerPacket();
        registerCrafterPacket();
    }

    private void registerTimerPacket() {
        ServerSidePacketRegistry.INSTANCE.register(timer_configuration_packet, (packetContext, attachedData) -> {
            BlockPos blockPos = attachedData.readBlockPos();
            int time = attachedData.readInt();
            packetContext.getTaskQueue().execute(() -> {
                if (packetContext.getPlayer().world.canPlayerModifyAt(packetContext.getPlayer(), blockPos)) {
                    // Change time
                    ((TimerBlockEntity) packetContext.getPlayer().world.getBlockEntity(blockPos)).modifyTime(time);
                }
            });
        });
    }

    private void registerCrafterPacket() {
        ServerSidePacketRegistry.INSTANCE.register(crafter_reset_packet, (packetContext, attachedData) -> {
            BlockPos blockPos = attachedData.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                if (packetContext.getPlayer().world.canPlayerModifyAt(packetContext.getPlayer(), blockPos)) {
                    // Reset recipe items
                    ((CrafterBlockEntity) packetContext.getPlayer().world.getBlockEntity(blockPos)).resetRecipe();
                }
            });
        });
    }

    public static Collection<CraftingRecipe> getOrCreateCraftingRecipes (World world) {
        if (craftingRecipes == null) {
            Collection<Recipe<?>> allRecipes = world.getRecipeManager().values();

            craftingRecipes = new ArrayList<CraftingRecipe>();
            for (Recipe recipe : allRecipes) {
                if (recipe.getType() == RecipeType.CRAFTING) {
                    craftingRecipes.add((CraftingRecipe)recipe);
                }
            }
        }

        return craftingRecipes;
    }
}
