package cc.avie.shaderfpsslider;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import net.fabricmc.loader.api.FabricLoader;

import net.minecraft.network.chat.Component;

public final class ShaderFpsConfig {
	public static final int OFF = 0;
	public static final int UNLIMITED = 260;
	public static final int MIN = 10;
	public static final int STEP = 10;
	public static final int DEFAULT = 60;

	private static final String KEY = "shaderFramerateLimit";

	private static volatile int limit = DEFAULT;

	private ShaderFpsConfig() {
	}

	private static Path path() {
		return FabricLoader.getInstance().getConfigDir().resolve(ShaderFpsSlider.MOD_ID + ".properties");
	}

	public static int getLimit() {
		return limit;
	}

	public static void setLimit(int value) {
		limit = sanitize(value);
	}

	private static int sanitize(int value) {
		if (value <= OFF) {
			return OFF;
		}
		return Math.min(UNLIMITED, Math.max(MIN, value));
	}

	public static void load() {
		Path path = path();
		if (!Files.exists(path)) {
			save();
			return;
		}
		Properties props = new Properties();
		try (Reader reader = Files.newBufferedReader(path)) {
			props.load(reader);
			limit = sanitize(Integer.parseInt(props.getProperty(KEY, String.valueOf(DEFAULT)).trim()));
		} catch (IOException | NumberFormatException e) {
			ShaderFpsSlider.LOGGER.warn("Could not read {}, using defaults", path, e);
			limit = DEFAULT;
		}
	}

	public static void save() {
		Path path = path();
		Properties props = new Properties();
		props.setProperty(KEY, String.valueOf(limit));
		try {
			Files.createDirectories(path.getParent());
			try (Writer writer = Files.newBufferedWriter(path)) {
				props.store(writer, "Max framerate while an Iris shader pack is active. 0 = off (use normal Max Framerate), 10-250, 260 = unlimited");
			}
		} catch (IOException e) {
			ShaderFpsSlider.LOGGER.error("Could not save {}", path, e);
		}
	}

	public static Component format(int value) {
		if (value <= OFF) {
			return Component.translatable("shader-fps-slider.option.off");
		}
		if (value >= UNLIMITED) {
			return Component.translatable("options.framerateLimit.max");
		}
		return Component.translatable("options.framerate", value);
	}

	public static String describe(int value) {
		return value <= OFF ? "off" : value >= UNLIMITED ? "unlimited" : value + " fps";
	}
}
