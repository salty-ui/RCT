package net.rct.autoclicker;

import net.minecraft.client.MinecraftClient;
import net.rct.autoclicker.mixin.MinecraftClientAccessor;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Runs on its own daemon thread so the delay between clicks can be measured
 * in real milliseconds instead of being locked to the 50ms game tick.
 *
 * While the "use item" (right-click) key is held, it repeatedly waits a
 * random amount of time between RCTConfig.minDelayMs and RCTConfig.maxDelayMs,
 * then schedules a right-click action on the main client thread — like a
 * repeater circuit re-triggering itself.
 */
public final class ClickerThread {

	private ClickerThread() {
	}

	public static void start(MinecraftClient client) {
		Thread thread = new Thread(() -> loop(client), "RCT-RightClickRepeater");
		thread.setDaemon(true);
		thread.start();
	}

	private static void loop(MinecraftClient client) {
		while (true) {
			try {
				if (isHoldingUse(client)) {
					int min = Math.max(1, RCTConfig.minDelayMs);
					int max = Math.max(min, RCTConfig.maxDelayMs);
					int delay = ThreadLocalRandom.current().nextInt(min, max + 1);

					Thread.sleep(delay);

					// Re-check right before firing in case the key was released
					// or a screen (inventory, chat, pause menu) was opened mid-wait.
					if (isHoldingUse(client)) {
						client.execute(() -> {
							if (isHoldingUse(client)) {
								((MinecraftClientAccessor) client).invokeDoItemUse();
							}
						});
					}
				} else {
					// Not holding right now - poll lightly instead of busy-waiting.
					Thread.sleep(15);
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			} catch (Exception ignored) {
				// Never let an unexpected exception kill the loop.
			}
		}
	}

	private static boolean isHoldingUse(MinecraftClient client) {
		return client.player != null
				&& client.currentScreen == null
				&& client.options.useKey.isPressed();
	}
}
