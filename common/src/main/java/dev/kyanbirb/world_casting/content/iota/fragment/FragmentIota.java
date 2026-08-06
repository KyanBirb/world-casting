package dev.kyanbirb.world_casting.content.iota.fragment;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import at.petrak.hexcasting.api.casting.iota.Vec3Iota;
import com.mojang.serialization.MapCodec;
import dev.kyanbirb.world_casting.index.WorldCastingIotaTypes;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.util.SableDistUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public class FragmentIota extends Iota {

    private final UUID subLevelId;

    public FragmentIota(UUID subLevelId) {
        super(WorldCastingIotaTypes.FRAGMENT::value);
        this.subLevelId = subLevelId;
    }

    @Override
    public boolean isTruthy() {
        return true;
    }

    @Override
    protected boolean toleratesOther(Iota iota) {
        if(typesMatch(this, iota) && iota instanceof FragmentIota fragmentIota) {
            return fragmentIota.subLevelId.equals(this.subLevelId);
        }

        return false;
    }

    @Override
    public Component display() {
        return display(this.subLevelId);
    }

    public static Component display(UUID uuid) {
        return Component.translatable("world_casting.display.fragment", uuid.toString());
    }

    @Override
    public int hashCode() {
        return this.subLevelId.hashCode();
    }

    public UUID getSubLevelId() {
        return subLevelId;
    }

    public SubLevel getSubLevel(Level level) {
        return SubLevelContainer.getContainer(level).getSubLevel(getSubLevelId());
    }

    public static class Type extends IotaType<FragmentIota> {

        @Override
        public MapCodec<FragmentIota> codec() {
            return UUIDUtil.CODEC.xmap(FragmentIota::new, FragmentIota::getSubLevelId).fieldOf("id");
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FragmentIota> streamCodec() {
            return UUIDUtil.STREAM_CODEC.map(FragmentIota::new, FragmentIota::getSubLevelId).mapStream(buf -> buf);
        }

        @Override
        public int color() {
            return 0xffa453da;
        }

        @Override
        public boolean validate(FragmentIota iota, ServerLevel level) {
            ServerSubLevelContainer container = SubLevelContainer.getContainer(level);
            return container != null && container.getSubLevel(iota.subLevelId) != null;
        }

    }

    public static Component getFragmentComponentClient(UUID uuid) {
        SubLevelContainer container = SubLevelContainer.getContainer(SableDistUtil.getClientLevel());
        SubLevel subLevel = container.getSubLevel(uuid);

        MutableComponent component = Component.translatable("world_casting.display.fragment.generic");
        if(subLevel == null) {
            component = Component.translatable("world_casting.display.fragment.unknown");
        } else if(subLevel.getName() != null) {
            component = Component.literal(subLevel.getName());
        } else {
            BlockPos pos = subLevel.getPlot().getCenterBlock();
            BlockState state = subLevel.getLevel().getBlockState(pos);
            if(!state.isAir()) {
                Item item = state.getBlock().asItem();
                String itemId = item.builtInRegistryHolder().key().location().toString();
                String centerBlock = "[item" + ":" + itemId + "]";
                component = Component.translatable("world_casting.display.fragment.generic_block", centerBlock);
            }
        }

        return component.withColor(WorldCastingIotaTypes.FRAGMENT.value().color());
    }

    public static Component getVec3Display(Level level, Vec3 pos) {
        SubLevel containing = Sable.HELPER.getContaining(level, pos);
        if(containing != null) {
            return getVec3Display(containing, pos);
        }
        return Vec3Iota.display(pos);
    }

    public static Component getVec3Display(SubLevel subLevel, Vec3 pos) {
        BlockPos center = subLevel.getPlot().getCenterBlock();
        return Component.literal(String.format("(%.2f, %.2f, %.2f) in ", pos.x - center.getX(), pos.y - center.getY(), pos.z - center.getZ()))
                .withStyle(ChatFormatting.RED)
                .append(FragmentIota.display(subLevel.getUniqueId()));
    }

}
