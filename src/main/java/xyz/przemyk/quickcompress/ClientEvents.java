package xyz.przemyk.quickcompress;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientEvents {

    public static KeyMapping compressKey;

    public static void addListeners(IEventBus eventBus) {
        eventBus.addListener(ClientEvents::registerKeyBindings);
    }

    private static void registerKeyBindings(RegisterKeyMappingsEvent event) {
        compressKey = new KeyMapping("key.quickcompress.compress_item", GLFW.GLFW_KEY_C, "key.quickcompress.category");
        event.register(compressKey);
    }

    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        if (compressKey.getKey().getValue() == event.getKeyCode()) {
            if (event.getScreen() instanceof AbstractContainerScreen<?> containerScreen) {
                Slot slot = containerScreen.getSlotUnderMouse();
                if (slot != null && slot.hasItem()) {
                    CompressPacketHandler.INSTANCE.sendToServer(new CompressPacket(slot.getContainerSlot()));
                    event.setCanceled(true);
                }
            }
        }
    }
}
