package dev.paulsoporan.pingnametags.mixin;

import com.mojang.authlib.GameProfile;
import dev.paulsoporan.pingnametags.colors.PingColors;
import dev.paulsoporan.pingnametags.config.PingNametagsConfig;
import dev.paulsoporan.pingnametags.config.PingNametagsConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.text.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import oshi.util.tuples.Pair;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mixin(EntityRenderer.class)
public class PingNametagsRenderMixin {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void renderLabelIfPresent(Args args) {
        PingNametagsConfig config = PingNametagsConfigManager.getConfig();
        if (!config.getEnabled()) {
            return;
        }

        Entity entity = args.get(0);
        if (!(entity instanceof AbstractClientPlayerEntity)) {
            return;
        }

        AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) entity;
        GameProfile gameProfile = abstractClientPlayerEntity.getGameProfile();
        String playerName = gameProfile.getName();
        UUID id = gameProfile.getId();

        MinecraftClient client = MinecraftClient.getInstance();
        Collection<PlayerListEntry> playerList = client.getNetworkHandler().getPlayerList();

        Optional<PlayerListEntry> exactMatchEntry = playerList.stream()
                .filter(playerListEntry -> playerListEntry.getProfile().getId().equals(id))
                .findFirst();

        // There are various tab list plugins that create fake player entries
        // that contain the real latency while the real entry has the wrong latency.
        // TODO: use string similarity instead of equality
        List<PlayerListEntry> fakeEntries = playerList.stream()
                .filter(playerListEntry -> {
                    Text displayName = playerListEntry.getDisplayName();
                    if (displayName == null) {
                        return false;
                    }

                    String displayNameString = collectText(displayName);

                    return displayNameString.equals(playerName);
                })
                .toList();

        PlayerListEntry selectedEntry;
        if (fakeEntries.size() == 1) {
            selectedEntry = fakeEntries.get(0);
        } else {
            if (!exactMatchEntry.isPresent()) {
                return;
            }

            selectedEntry = exactMatchEntry.get();
        }

        int latency = selectedEntry.getLatency();

        Text text = args.get(1);

        MutableText latencyText = new LiteralText(String.format(config.getPingTextFormatString(), latency))
                .setStyle(Style.EMPTY.withColor(PingColors.getColor(latency)));

        Pair<Text, Text> textComponents = switch (config.getPingTextPosition()) {
            case Left -> new Pair(latencyText, text);
            case Right -> new Pair(text, latencyText);
        };

        Text newText = new LiteralText("")
                .append(textComponents.getA())
                .append(" ")
                .append(textComponents.getB());

        args.set(1, newText);
    }

    private String collectText(Text text) {
        return text.asString().concat(String.join("", text.getSiblings().stream().map(this::collectText).toList()));
    }
}
