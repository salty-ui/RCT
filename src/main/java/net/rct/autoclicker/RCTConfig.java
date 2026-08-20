package net.rct.autoclicker;

/**
 * Holds the current min/max delay (in milliseconds) between simulated
 * right-clicks. Updated live by the /rct command.
 */
public class RCTConfig {

	/** Default range: 60-70ms, matches the example in the mod's spec. */
	public static volatile int minDelayMs = 60;
	public static volatile int maxDelayMs = 70;
}
