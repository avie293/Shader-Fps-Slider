package cc.avie.shaderfpsslider.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;

import cc.avie.shaderfpsslider.ShaderFpsSlider;
import cc.avie.shaderfpsslider.ShaderFpsConfig;

import net.caffeinemc.mods.sodium.api.config.option.OptionImpact;
import net.caffeinemc.mods.sodium.api.config.structure.ConfigBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.OptionPageBuilder;
import net.caffeinemc.mods.sodium.api.config.structure.PageBuilder;
import net.irisshaders.iris.compat.sodium.config.IrisConfig;

import net.minecraft.network.chat.Component;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = IrisConfig.class, remap = false)
public class IrisConfigMixin {
	@ModifyReturnValue(method = "createSettingsPage", at = @At("RETURN"), require = 0)
	private PageBuilder shader_fps_slider$addLimitOption(PageBuilder page, ConfigBuilder builder) {
		if (page instanceof OptionPageBuilder optionPage) {
			optionPage.addOptionGroup(builder.createOptionGroup()
				.addOption(builder.createIntegerOption(ShaderFpsSlider.id("shader_framerate_limit"))
					.setName(Component.translatable("shader-fps-slider.option.limit"))
					.setTooltip(Component.translatable("shader-fps-slider.option.limit.tooltip"))
					.setImpact(OptionImpact.VARIES)
					.setRange(ShaderFpsConfig.OFF, ShaderFpsConfig.UNLIMITED, ShaderFpsConfig.STEP)
					.setDefaultValue(ShaderFpsConfig.DEFAULT)
					.setValueFormatter(ShaderFpsConfig::format)
					.setBinding(ShaderFpsConfig::setLimit, ShaderFpsConfig::getLimit)
					.setStorageHandler(ShaderFpsConfig::save)));
		} else {
			ShaderFpsSlider.LOGGER.warn("Unexpected Iris settings page type {}, slider not added", page.getClass().getName());
		}
		return page;
	}
}
