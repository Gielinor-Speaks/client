package com.gielinorspeaks.service;

import net.runelite.api.events.WidgetClosed;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for DialogueDetectionService edge cases and null safety.
 * Tests handling of null callbacks, null NPCs, and other edge cases.
 */
public class DialogueDetectionServiceEdgeCaseTest extends DialogueDetectionServiceTestBase {
	@Test
	public void testOnWidgetLoaded_handlesNullDialogueCallback() {
		// Arrange
		setupNpcInteraction();
		service.setDialogueCallback(null); // No callback

		WidgetLoaded event = mock(WidgetLoaded.class);
		when(event.getGroupId()).thenReturn(InterfaceID.CHAT_LEFT);
		when(mockClient.getWidget(InterfaceID.ChatLeft.TEXT)).thenReturn(mockDialogWidget);
		when(mockDialogWidget.isHidden()).thenReturn(false);
		when(mockDialogWidget.getText()).thenReturn("Hello!");

		// Act - Should not throw
		service.onWidgetLoaded(event);

		// Assert - No assertion needed, just shouldn't crash
	}

	@Test
	public void testOnWidgetClosed_handlesNullDialogueEndCallback() {
		// Arrange
		setupNpcInteraction();
		service.setDialogueEndCallback(null); // No callback

		WidgetClosed event = mock(WidgetClosed.class);
		when(event.getGroupId()).thenReturn(InterfaceID.CHAT_LEFT);

		// Act - Should not throw
		service.onWidgetClosed(event);

		// Assert - No assertion needed, just shouldn't crash
	}

	@Test
	public void testOnWidgetClosed_handlesNullCachedNpc() {
		// Arrange - Don't set up NPC interaction, so cachedInteractingNpc is null

		WidgetClosed event = mock(WidgetClosed.class);
		when(event.getGroupId()).thenReturn(InterfaceID.CHAT_LEFT);

		// Act - Should not throw
		service.onWidgetClosed(event);

		// Assert
		assertEquals("Should not fire callback when NPC is null", 0, capturedDialogueEndEvents.size());
	}
}
