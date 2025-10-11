package com.gielinorspeaks.model;

import lombok.Value;
import javax.annotation.Nullable;

/**
 * Immutable value object representing a single dialogue occurrence.
 * Contains all information needed to identify and retrieve voice audio for an NPC's dialogue.
 */
@Value
public class DialogueEvent {
	/**
	 * The NPC's game ID
	 */
	int npcId;

	/**
	 * The NPC's display name
	 */
	String npcName;

	/**
	 * The dialogue text spoken by the NPC
	 */
	String dialogueText;

	/**
	 * The source of the dialogue (dialogue box or overhead text)
	 */
	DialogueSource source;

	/**
	 * The animation ID associated with the dialogue.
	 * Only present for DIALOGUE_BOX sources.
	 * Used to capture emotion (e.g., CHATLAUGH1, CHATNEU) for voice synthesis.
	 */
	@Nullable
	Integer animationId;
}
