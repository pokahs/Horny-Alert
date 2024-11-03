package io.github.pokahs.hornyalert.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.text.MutableText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.Formatting;

@Mixin(SoundSystem.class)
public class HornLoggerMixin {

    // Create list of goat horn sound ids
    private static final List<Identifier> GOAT_HORN_SOUND_IDS = SoundEvents.GOAT_HORN_SOUNDS.stream()
            .map(entry -> entry.value().getId())
            .collect(Collectors.toList());

    @Inject(method = "play", at = @At("HEAD"))
    private void onPlaySound(SoundInstance soundInstance, CallbackInfo ci) {

        // Get Id of sound
        Identifier soundId = soundInstance.getId();

        // Check if sound is a goat horn sound
        if (isGoatHornSound(soundId)) {

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null) {

                // Save coords as vars
                double x = soundInstance.getX(), y = soundInstance.getY(), z = soundInstance.getZ();

                // Format coords as "x y z" for copying
                String coordsForClipboard = String.format("%.2f %.2f %.2f", x, y, z);

                // Format coords as "x, y, z" for display
                String coordsDisplay = String.format("%.2f, %.2f, %.2f", x, y, z);

                // Create "[!!!]" prefix
                MutableText prefix = Text.literal("[!!!]")
                    .formatted(Formatting.RED);

                // Create "Horn played" notif
                MutableText hornPlayed = Text.literal(" Horn played")
                    .formatted(Formatting.WHITE, Formatting.BOLD);

                // Create "at" + newline part
                MutableText atText = Text.literal(" at\n")
                    .styled(style -> style.withColor(Formatting.GRAY));

                // Create coords text
                MutableText coordsText = Text.literal(coordsDisplay)
                .formatted(Formatting.WHITE, Formatting.UNDERLINE)
                .styled(style -> style
                    .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, coordsForClipboard))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.literal("Copy")))
                );

                // Combine message into one
                MutableText fullMessage = prefix.append(hornPlayed).append(atText).append(coordsText);

                // Send message to player
                client.player.sendMessage(fullMessage, false);
            }
        }
    }

    // Checks if soundId is a Goat Horn Id
    private boolean isGoatHornSound(Identifier soundId) {
        return GOAT_HORN_SOUND_IDS.contains(soundId);
    }
}