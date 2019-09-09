package nessiesson.tapemouse.mixins;

import nessiesson.tapemouse.LiteModTapeMouse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {
	@Shadow
	public GuiScreen currentScreen;

	@Inject(method = "runTick", at = @At("RETURN"))
	private void onPreClientTick(CallbackInfo ci) {
		if (this.currentScreen instanceof GuiChat) return;
		if (LiteModTapeMouse.keyBinding == null) return;
		if (LiteModTapeMouse.i++ < LiteModTapeMouse.delay) return;
		LiteModTapeMouse.i = 0;
		if (LiteModTapeMouse.delay == 0) KeyBinding.setKeyBindState(LiteModTapeMouse.keyBinding.getKeyCode(), true);
		KeyBinding.onTick(LiteModTapeMouse.keyBinding.getKeyCode());
	}
}
