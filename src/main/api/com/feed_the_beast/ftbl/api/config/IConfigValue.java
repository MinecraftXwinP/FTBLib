package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.io.IExtendedIOObject;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IConfigValue extends IStringSerializable, IExtendedIOObject, INBTSerializable<NBTBase>, IJsonSerializable
{
    @Nullable
    Object getValue();

    String getString();

    boolean getBoolean();

    int getInt();

    default double getDouble()
    {
        return getInt();
    }

    default boolean containsInt(int val)
    {
        return getInt() == val;
    }

    IConfigValue copy();

    default boolean equalsValue(IConfigValue value)
    {
        return Objects.equals(getValue(), value.getValue());
    }

    default int getColor()
    {
        return 0x999999;
    }

    default void addInfo(IConfigKey key, List<String> list)
    {
        list.add(TextFormatting.AQUA + "Def: " + key.getDefValue().getString());
    }

    default List<String> getVariants()
    {
        return Collections.emptyList();
    }

    void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button);

    boolean setValueFromString(String text, boolean simulate);

    default boolean isNull()
    {
        return false;
    }
}