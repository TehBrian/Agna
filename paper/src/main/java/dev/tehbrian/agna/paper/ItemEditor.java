package dev.tehbrian.agna.paper;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.google.common.base.Predicates.alwaysTrue;

@SuppressWarnings("UnstableApiUsage")
public final class ItemEditor {

	private ItemStack item;

	private ItemEditor(final ItemStack item) {
		this.item = item.clone();
	}

	private ItemEditor(final ItemType type) {
		this.item = type.createItemStack();
	}

	public static ItemEditor edit(final ItemStack item) {
		return new ItemEditor(item);
	}

	public static ItemEditor edit(final ItemType type) {
		return new ItemEditor(type);
	}

	public ItemStack item() {
		return this.item;
	}

	public ItemEditor amount(final int amount) {
		this.item.setAmount(amount);
		return this;
	}

	public ItemEditor type(final ItemType type) {
		final var repl = type.createItemStack();
		repl.copyDataFrom(this.item, alwaysTrue());
		repl.setAmount(this.item.getAmount());
		this.item = repl;
		return this;
	}

	public ItemEditor set(final DataComponentType.NonValued type) {
		this.item.setData(type);
		return this;
	}

	public <T> @Nullable T get(final DataComponentType.Valued<T> type) {
		return this.item.getData(type);
	}

	public <T> ItemEditor set(final DataComponentType.Valued<T> type, final T data) {
		this.item.setData(type, data);
		return this;
	}

	public ItemEditor unset(final DataComponentType type) {
		this.item.unsetData(type);
		return this;
	}

	public ItemEditor reset(final DataComponentType type) {
		this.item.resetData(type);
		return this;
	}

	public <T> ItemEditor update(
			final DataComponentType.Valued<T> type,
			final UnaryOperator<T> operator
	) {
		final T current = this.get(type);
		if (current == null) {
			return this;
		}
		return this.set(type, operator.apply(current));
	}

	public <T> ItemEditor compute(
			final DataComponentType.Valued<T> type,
			final Function<@Nullable T, T> operator
	) {
		return this.set(type, operator.apply(this.get(type)));
	}

	public ItemEditor itemName(final Component data) {
		this.set(DataComponentTypes.ITEM_NAME, data);
		return this;
	}

	public ItemEditor customName(final Component data) {
		this.set(DataComponentTypes.CUSTOM_NAME, data);
		return this;
	}

	public ItemEditor lore(final List<Component> data) {
		this.set(DataComponentTypes.LORE, ItemLore.lore(data));
		return this;
	}

	public ItemEditor textures(final String data) {
		this.set(
				DataComponentTypes.PROFILE,
				ResolvableProfile.resolvableProfile().addProperty(
						new ProfileProperty("textures", data)
				).build()
		);
		return this;
	}

	public ItemEditor bannerPatterns(final List<Pattern> data) {
		this.set(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers(data));
		return this;
	}

	public ItemEditor dyedColor(final Color data) {
		this.set(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(data));
		return this;
	}

}
