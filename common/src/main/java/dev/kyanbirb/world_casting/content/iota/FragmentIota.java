package dev.kyanbirb.world_casting.content.iota;

import at.petrak.hexcasting.api.casting.iota.Iota;
import at.petrak.hexcasting.api.casting.iota.IotaType;
import com.mojang.serialization.MapCodec;
import dev.kyanbirb.world_casting.index.WorldCastingIotaTypes;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.ClientSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.util.SableDistUtil;
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
        ClientSubLevel subLevel = getClientSubLevel();
        return display(subLevel);
    }

    public static Component display(ClientSubLevel subLevel) {
        MutableComponent component = Component.literal("Fragment");
        String centerBlock = null;
        if(subLevel == null) {
            component = Component.literal("Unknown fragment");
        } else if(subLevel.getName() != null) {
            component = Component.literal(subLevel.getName());
        } else {
            BlockPos pos = subLevel.getPlot().getCenterBlock();
            BlockState state = subLevel.getLevel().getBlockState(pos);
            if(!state.isAir()) {
                Item item = state.getBlock().asItem();
                String itemId = item.builtInRegistryHolder().key().location().toString();
                centerBlock = ": [item" + ":" + itemId + "]";
            }
        }
        MutableComponent display = component.withColor(TYPE.color());
        if(centerBlock != null) {
            display.append(centerBlock);
        }
        return display;
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

    public ClientSubLevel getClientSubLevel() {
        return (ClientSubLevel) SubLevelContainer.getContainer(SableDistUtil.getClientLevel()).getSubLevel(getSubLevelId());
    }

    public static final IotaType<FragmentIota> TYPE = new IotaType<>() {

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

    };

}
