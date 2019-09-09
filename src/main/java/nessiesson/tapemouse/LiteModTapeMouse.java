package nessiesson.tapemouse;

import com.mumfrey.liteloader.Tickable;
import nessiesson.tapemouse.ClientCommands.ClientCommandHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;

import java.io.File;

public class LiteModTapeMouse implements Tickable {
	private static final String NAME = "TM";
	public static int delay = 10;
	public static KeyBinding keyBinding;
	public static int i = 0;

	@Override
	public String getVersion() {
		return "@VERSION@";
	}

	@Override
	public void init(File configPath) {
		ClientCommandHandler.instance.registerCommand(new CommandTapeMouse());
	}

	@Override
	public void upgradeSettings(String version, File configPath, File oldConfigPath) {
	}

	@Override
	public String getName() {
		return "@NAME@";
	}

	private void drawStringInCorner(String msg) {
		FontRenderer font = Minecraft.getMinecraft().fontRenderer;
		GlStateManager.pushMatrix();
		GlStateManager.scale(0.5, 0.5, 0.5);
		font.drawString(msg, 5, 5, 0xFFFFFF, true);
		GlStateManager.popMatrix();
	}

	@Override
	public void onTick(Minecraft minecraft, float partialTicks, boolean inGame, boolean clock) {
		if (keyBinding == null) return;
		final Minecraft mc = Minecraft.getMinecraft();
		if (mc.currentScreen instanceof GuiMainMenu) {
			drawStringInCorner(NAME + " paused.");
		} else if (mc.currentScreen instanceof GuiChat) {
			drawStringInCorner(NAME + " paused.");
		} else {
			drawStringInCorner(keyBinding.getKeyDescription().replaceFirst("^key\\.", "") + " " + i + " / " + delay);
		}
	}
}
