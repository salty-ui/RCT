package net.rct.autoclicker;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class RightClickRepeaterClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		registerCommand();
		ClickerThread.start(MinecraftClient.getInstance());
	}

	private void registerCommand() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
				dispatcher.register(ClientCommandManager.literal("rct")
						.then(ClientCommandManager.argument("min", IntegerArgumentType.integer(1))
								.then(ClientCommandManager.argument("max", IntegerArgumentType.integer(1))
										.executes(context -> {
											int min = IntegerArgumentType.getInteger(context, "min");
											int max = IntegerArgumentType.getInteger(context, "max");

											// Auto-correct if typed backwards, e.g. "/rct 70 60"
											if (min > max) {
												int tmp = min;
												min = max;
												max = tmp;
											}

											RCTConfig.minDelayMs = min;
											RCTConfig.maxDelayMs = max;

											context.getSource().sendFeedback(Text.literal(
													"[RCT] Right-click repeat delay set to " + min + "-" + max + "ms"));
											return 1;
										})
								)
						)
				)
		);
	}
}
