package dev.kyanbirb.world_casting.neoforge.data;

import dev.kyanbirb.world_casting.WorldCasting;
import dev.kyanbirb.world_casting.data.WorldCastingLang;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class WorldCastingLanguageProvider extends LanguageProvider {
    private final String locale;

    public WorldCastingLanguageProvider(PackOutput output, String locale) {
        super(output, WorldCasting.MOD_ID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        WorldCastingLang.provideLang(this::add, this.locale);
    }
}
