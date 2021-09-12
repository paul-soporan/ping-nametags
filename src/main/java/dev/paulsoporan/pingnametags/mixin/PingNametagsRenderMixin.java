package dev.paulsoporan.pingnametags.mixin;

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
import java.util.Optional;
import java.util.UUID;

@Mixin(EntityRenderer.class)
public class PingNametagsRenderMixin {
    @ModifyArgs(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderer;renderLabelIfPresent(Lnet/minecraft/entity/Entity;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void renderLabelIfPresent(Args args) {
        Entity entity = args.get(0);
        if (!(entity instanceof AbstractClientPlayerEntity)) {
            return;
        }

        AbstractClientPlayerEntity abstractClientPlayerEntity = (AbstractClientPlayerEntity) entity;
        UUID id = abstractClientPlayerEntity.getGameProfile().getId();

        MinecraftClient client = MinecraftClient.getInstance();
        Collection<PlayerListEntry> playerList = client.getNetworkHandler().getPlayerList();

        Optional<PlayerListEntry> optionalPlayerListEntry = playerList.stream()
                .filter(playerListEntry -> playerListEntry.getProfile().getId() == id)
                .findFirst();

        if (!optionalPlayerListEntry.isPresent()) {
            return;
        }

        PingNametagsConfig config = PingNametagsConfigManager.getConfig();

        PlayerListEntry relevantPlayerListEntry = optionalPlayerListEntry.get();
        int latency = relevantPlayerListEntry.getLatency();

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
}
