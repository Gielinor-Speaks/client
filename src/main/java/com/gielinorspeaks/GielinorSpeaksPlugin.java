package com.gielinorspeaks;

import com.gielinorspeaks.model.DialogueEvent;
import com.gielinorspeaks.service.DialogueDetectionService;
import com.gielinorspeaks.service.OverheadTextService;
import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Gielinor Speaks"
)
public class GielinorSpeaksPlugin extends Plugin {
	@SuppressWarnings("unused") // Used for future features
	@Inject
	private Client client;

	@SuppressWarnings("unused") // Used for future config checks
	@Inject
	private GielinorSpeaksConfig config;

	@Inject
	private EventBus eventBus;

	@Inject
	private DialogueDetectionService dialogueDetectionService;

	@Inject
	private OverheadTextService overheadTextService;

	@Override
	protected void startUp() {
		log.info("Gielinor Speaks has started!");

		// Set up callbacks for dialogue events
		dialogueDetectionService.setDialogueCallback(this::onDialogueDetected);
		dialogueDetectionService.setDialogueEndCallback(this::onDialogueEnded);
		overheadTextService.setDialogueCallback(this::onDialogueDetected);

		// Register services with event bus
		eventBus.register(dialogueDetectionService);
		eventBus.register(overheadTextService);
	}

	@Override
	protected void shutDown() {
		log.info("Gielinor Speaks has stopped!");

		// Unregister services from event bus
		eventBus.unregister(dialogueDetectionService);
		eventBus.unregister(overheadTextService);

		// Clear callbacks
		dialogueDetectionService.setDialogueCallback(null);
		dialogueDetectionService.setDialogueEndCallback(null);
		overheadTextService.setDialogueCallback(null);
	}

	/**
	 * Handle detected dialogue events from both sources
	 */
	private void onDialogueDetected(DialogueEvent event) {
		// Phase 1: Just log the dialogue for verification
		log.info("=== DIALOGUE DETECTED ===");
		log.info("Source: {}", event.getSource());
		log.info("NPC: {} (ID: {})", event.getNpcName(), event.getNpcId());
		log.info("Text: {}", event.getDialogueText());
		if (event.getAnimationId() != null) {
			log.info("Animation ID: {}", event.getAnimationId());
		}
		log.info("========================");

		// Future phases will add:
		// - Dialogue hashing
		// - Cache check
		// - API request
		// - Audio playback
	}

	/**
	 * Handle dialogue end events (when player options appear or dialogue closes)
	 */
	private void onDialogueEnded(int npcId) {
		log.info("=== DIALOGUE ENDED ===");
		log.info("NPC ID: {}", npcId);
		log.info("======================");

		// Future phases will add:
		// - Stop audio playback for this NPC
	}

	@SuppressWarnings("unused") // Used by RuneLite dependency injection
	@Provides
    GielinorSpeaksConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(GielinorSpeaksConfig.class);
	}
}
