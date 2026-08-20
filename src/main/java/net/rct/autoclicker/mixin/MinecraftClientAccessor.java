package net.rct.autoclicker.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Exposes MinecraftClient's private doItemUse() method (the same method
 * vanilla calls when you right-click) so the repeater thread can invoke it
 * on demand, as many times as we want, instead of only once per real click.
 */
@Mixin(MinecraftClient.class)
public interface MinecraftClientAccessor {

	@Invoker("doItemUse")
	void invokeDoItemUse();
}
