package dev.tehbrian.agna.paper;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.papermc.paper.datacomponent.DataComponentType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.BannerPatternLayers;
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.block.banner.Pattern;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.google.common.base.Predicates.alwaysTrue;

@SuppressWarnings("UnstableApiUsage")
public final class FluentItem {

	private static final Component DISABLE_ITALICS = Component.empty().decoration(TextDecoration.ITALIC, false);

	private ItemStack item;

	private FluentItem(final ItemStack item) {
		this.item = item;
	}

	public static FluentItem editItem(final ItemStack item) {
		return new FluentItem(item);
	}

	public static FluentItem cloneItem(final ItemStack item) {
		return new FluentItem(item.clone());
	}

	public static FluentItem createItem(final ItemType type) {
		return new FluentItem(type.createItemStack());
	}

	public ItemStack item() {
		return this.item;
	}

	public FluentItem amount(final int amount) {
		this.item.setAmount(amount);
		return this;
	}

	public FluentItem type(final ItemType type) {
		final var repl = type.createItemStack();
		repl.copyDataFrom(this.item, alwaysTrue());
		repl.setAmount(this.item.getAmount());
		this.item = repl;
		return this;
	}

	//region data components

	public FluentItem set(final DataComponentType.NonValued type) {
		this.item.setData(type);
		return this;
	}

	public <T> @Nullable T get(final DataComponentType.Valued<T> type) {
		return this.item.getData(type);
	}

	public <T> FluentItem set(final DataComponentType.Valued<T> type, final T data) {
		this.item.setData(type, data);
		return this;
	}

	public FluentItem unset(final DataComponentType type) {
		this.item.unsetData(type);
		return this;
	}

	public FluentItem reset(final DataComponentType type) {
		this.item.resetData(type);
		return this;
	}

	public <T> FluentItem update(
			final DataComponentType.Valued<T> type,
			final UnaryOperator<T> operator
	) {
		final T current = this.get(type);
		if (current == null) {
			return this;
		}
		return this.set(type, operator.apply(current));
	}

	public <T> FluentItem compute(
			final DataComponentType.Valued<T> type,
			final Function<@Nullable T, T> operator
	) {
		return this.set(type, operator.apply(this.get(type)));
	}

	//endregion

	//region PDC

	/**
	 * Gets persistent data from the item's {@link org.bukkit.persistence.PersistentDataContainer}.
	 *
	 * @param key  the {@code NamespacedKey} to use
	 * @param type the {@code PersistentDataType to use}
	 * @param <T>  the primary object type of the data
	 * @param <Z>  the retrieve object type of the data
	 * @return the data
	 */
	public <T, Z> @Nullable Z pdcGet(
			final NamespacedKey key,
			final PersistentDataType<T, Z> type
	) {
		return this.item.getPersistentDataContainer().get(key, type);
	}

	/**
	 * Adds persistent data to the item's {@link org.bukkit.persistence.PersistentDataContainer}.
	 *
	 * @param key    the {@code NamespacedKey} to use
	 * @param type   the {@code PersistentDataType} to use
	 * @param object the data to set
	 * @param <T>    the primary object type of the data
	 * @param <Z>    the retrieve object type of the data
	 * @return the builder
	 */
	public <T, Z> FluentItem pdcSet(
			final NamespacedKey key,
			final PersistentDataType<T, Z> type,
			final Z object
	) {
		this.item.editPersistentDataContainer(c -> c.set(key, type, object));
		return this;
	}

	/**
	 * Removes persistent data from the item's {@link org.bukkit.persistence.PersistentDataContainer}.
	 *
	 * @param key the {@code NamespacedKey} to use
	 * @return the builder
	 */
	public FluentItem pdcRemove(
			final NamespacedKey key
	) {
		this.item.editPersistentDataContainer(c -> c.remove(key));
		return this;
	}

	//endregion

	// region data component helper utilities

	public FluentItem itemName(final Component data) {
		this.set(DataComponentTypes.ITEM_NAME, data);
		return this;
	}

	public FluentItem customName(final Component data) {
		this.set(DataComponentTypes.CUSTOM_NAME, data);
		return this;
	}

	public FluentItem lore(final List<Component> data) {
		// sidestep default formatting by appending each component to a dummy
		this.set(DataComponentTypes.LORE, ItemLore.lore(data.stream().map(DISABLE_ITALICS::append).toList()));
		return this;
	}

	/**
	 * A utility method that converts the provided {@code lines} into a
	 * {@code List} using {@link List#of(Object[])}, and calls
	 * {@link #lore(List)} using the new {@code List} as the argument.
	 * <p>
	 * P.S. This is ripped straight from corn's AbstractItemBuilder, which
	 * I wrote about 4 years ago, which is why this Javadoc is the way it is.
	 * <p>
	 * P.P.S. Brian, never write Javadocs like this stupid dumb fucking one again.
	 *
	 * @param lines the lines of the lore
	 * @return the builder
	 */
	public FluentItem loreList(final Component... lines) {
		return this.lore(List.of(lines));
	}

	public FluentItem textures(final String data) {
		this.set(
				DataComponentTypes.PROFILE,
				ResolvableProfile.resolvableProfile().addProperty(
						new ProfileProperty("textures", data)
				).build()
		);
		return this;
	}

	public FluentItem bannerPatterns(final List<Pattern> data) {
		this.set(DataComponentTypes.BANNER_PATTERNS, BannerPatternLayers.bannerPatternLayers(data));
		return this;
	}

	public FluentItem dyedColor(final Color data) {
		this.set(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor(data));
		return this;
	}

	public FluentItem hideTooltip(final boolean data) {
		this.set(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().hideTooltip(data).build());
		return this;
	}

	// endregion

}
