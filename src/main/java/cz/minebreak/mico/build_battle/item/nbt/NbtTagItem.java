package cz.minebreak.mico.build_battle.item.nbt;

import net.minecraft.server.v1_15_R1.NBTBase;
import net.minecraft.server.v1_15_R1.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class NbtTagItem extends ItemStack {

    private net.minecraft.server.v1_15_R1.ItemStack nmsItem;

    private NBTTagCompound nbtTagCompound;

    public NbtTagItem(Material material) {
        super(material);

        nmsItem = org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack.asNMSCopy(this);
        nbtTagCompound = nmsItem.getTag();
        if (nbtTagCompound == null) {
            nbtTagCompound = new NBTTagCompound();
        }
    }

    public void setNbtTagBool(String key, boolean value) {
        nbtTagCompound.setBoolean(key, value);
        updateItem();
    }

    public void setNbtTagInt(String key, int value) {
        nbtTagCompound.setInt(key, value);
        updateItem();
    }

    public void setNbtTagLong(String key, long value) {
        nbtTagCompound.setLong(key, value);
        updateItem();
    }

    public void setNbtTagFloat(String key, float value) {
        nbtTagCompound.setFloat(key, value);
        updateItem();
    }

    public void setNbtTagDouble(String key, double value) {
        nbtTagCompound.setDouble(key, value);
        updateItem();
    }

    public void setNbtTagByte(String key, byte value) {
        nbtTagCompound.setByte(key, value);
        updateItem();
    }

    public void setNbtTagShort(String key, short value) {
        nbtTagCompound.setShort(key, value);
        updateItem();
    }

    public void setNbtTagByteArray(String key, byte[] array) {
        nbtTagCompound.setByteArray(key, array);
        updateItem();
    }

    public void setNbtTagIntArray(String key, int[] array) {
        nbtTagCompound.setIntArray(key, array);
        updateItem();
    }

    public void setNbtTag(String key, NBTBase value) {
        nbtTagCompound.set(key, value);
        updateItem();
    }

    public void setNbtTagString(String key, String value) {
        nbtTagCompound.setString(key, value);
        updateItem();
    }

    /*
        Private methods
     */
    private void updateItem() {
        nmsItem.setTag(nbtTagCompound);
        setItemMeta(org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack.getItemMeta(nmsItem));
    }
}
