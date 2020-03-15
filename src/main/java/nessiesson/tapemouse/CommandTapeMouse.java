package nessiesson.tapemouse;

import nessiesson.tapemouse.ClientCommands.mixins.IKeybinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.NumberInvalidException;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.lwjgl.input.Keyboard.KEY_NONE;

/**
 * @author Dries007
 */
public class CommandTapeMouse extends CommandBase {
	private static final Map<String, KeyBinding> KEYBIND_ARRAY = IKeybinding.getKeyBindArray();
	private boolean prevPauseSetting = true;

	@Override
	public String getName() {
		return "tm";
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return sender instanceof EntityPlayer;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return '/' + getName() + " [off|keybinding name ...] [delay]";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			throw new WrongUsageException(this.getUsage(sender));
		}
		if (args[0].equalsIgnoreCase("off")) {
			Minecraft.getMinecraft().gameSettings.pauseOnLostFocus = prevPauseSetting;
			LiteModTapeMouse.keyBinding = null;
			LiteModTapeMouse.i = 0;
			sender.sendMessage(new TextComponentString("TM off."));
		} else {
			StringBuilder askedName = new StringBuilder(args[0]);
			if (args.length > 1) {
				try {
					LiteModTapeMouse.delay = parseInt(args[args.length - 1], 0);
					for (int i = 1; i < args.length - 1; i++) askedName.append(' ').append(args[i]);
				} catch (NumberInvalidException e) {
					// Assume last part was not a number
					for (int i = 1; i < args.length; i++) askedName.append(' ').append(args[i]);
				}
				askedName = new StringBuilder(askedName.toString().trim());
			}
			for (KeyBinding keyBinding : KEYBIND_ARRAY.values()) {
				if (keyBinding == null) continue;
				String name = keyBinding.getKeyDescription();
				//noinspection ConstantConditions Because of stupid mod authors...
				if (name == null) continue;
				name = name.replaceFirst("^key\\.", "");
				if (askedName.toString().equalsIgnoreCase(name)) {
					prevPauseSetting = Minecraft.getMinecraft().gameSettings.pauseOnLostFocus;
					Minecraft.getMinecraft().gameSettings.pauseOnLostFocus = false;
					LiteModTapeMouse.keyBinding = keyBinding;
					sender.sendMessage(new TextComponentString("TM on '" + name + "' with delay " + LiteModTapeMouse.delay + " ticks."));
					return;
				}
			}

			throw new CommandException(askedName + " is unknown keybinding. If your keybind ends in a number, you *have* to specify the delay.");
		}
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
		if (args.length == 1) {
			List<String> list = new ArrayList<>();
			list.add("off");
			for (KeyBinding keyBinding : KEYBIND_ARRAY.values()) {
				if (keyBinding == null || keyBinding.getKeyCode() == KEY_NONE) continue;
				String name = keyBinding.getKeyDescription();
				//noinspection ConstantConditions Because of stupid mod authors...
				if (name == null) continue;
				name = name.replaceFirst("^key\\.", "");
				list.add(name);
			}
			return getListOfStringsMatchingLastWord(args, list);
		}
		return super.getTabCompletions(server, sender, args, pos);
	}
}
