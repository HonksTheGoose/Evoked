package com.teamtaigamodding.evoked;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class EvokedConfig {

    public static class Common {
        public static final ModConfigSpec COMMON_SPEC;
        public static final Common COMMON;

        public final ModConfigSpec.BooleanValue TotemsWorkInVoid;

        Common(ModConfigSpec.Builder builder) {
            builder.push("items");
            builder.push("totems");
            this.TotemsWorkInVoid = builder.comment("If Totems can save you in the void.").define("Void Totems", true);
            builder.pop();
            builder.pop();
        }

        static {
            Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
            COMMON_SPEC = specPair.getRight();
            COMMON = specPair.getLeft();
        }
    }
}
