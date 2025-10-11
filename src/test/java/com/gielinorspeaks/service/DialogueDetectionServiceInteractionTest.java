package com.gielinorspeaks.service;

import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.events.InteractingChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for DialogueDetectionService interaction tracking.
 * Tests the onInteractingChanged event handler and NPC caching.
 */
public class DialogueDetectionServiceInteractionTest extends DialogueDetectionServiceTestBase {
	@Test
	public void testOnInteractingChanged_cachesNpcWhenPlayerInteractsWithNpc() {
		// Arrange
		InteractingChanged event = mock(InteractingChanged.class);
		when(event.getSource()).thenReturn(mockPlayer);
		when(mockPlayer.getInteracting()).thenReturn(mockNpc);

		// Act
		service.onInteractingChanged(event);

		// Assert - The NPC should be cached (we'll verify this indirectly through widget loaded)
		verify(mockPlayer).getInteracting();
	}

	@Test
	public void testOnInteractingChanged_ignoresPlayerInteractingWithNonNpc() {
		// Arrange
		InteractingChanged event = mock(InteractingChanged.class);
		Player otherPlayer = mock(Player.class);
		when(event.getSource()).thenReturn(mockPlayer);
		when(mockPlayer.getInteracting()).thenReturn(otherPlayer); // Interacting with player, not NPC

		// Act
		service.onInteractingChanged(event);

		// Assert - Should not throw, just ignore
		verify(mockPlayer).getInteracting();
	}

	@Test
	public void testOnInteractingChanged_ignoresNonLocalPlayerEvents() {
		// Arrange
		InteractingChanged event = mock(InteractingChanged.class);
		Player otherPlayer = mock(Player.class);
		when(event.getSource()).thenReturn(otherPlayer); // Not the local player

		// Act
		service.onInteractingChanged(event);

		// Assert - Should not access interacting
		verify(mockPlayer, never()).getInteracting();
	}

	@Test
	public void testOnInteractingChanged_resetsDialogueStateOnNewNpc() {
		// Arrange
		InteractingChanged event1 = mock(InteractingChanged.class);
		when(event1.getSource()).thenReturn(mockPlayer);
		when(mockPlayer.getInteracting()).thenReturn(mockNpc);

		// Simulate first interaction
		service.onInteractingChanged(event1);

		// Simulate dialogue detection (will set lastDialogueText)
		WidgetLoaded widgetEvent = mock(WidgetLoaded.class);
		when(widgetEvent.getGroupId()).thenReturn(InterfaceID.CHAT_LEFT);
		when(mockClient.getWidget(InterfaceID.ChatLeft.TEXT)).thenReturn(mockDialogWidget);
		when(mockDialogWidget.isHidden()).thenReturn(false);
		when(mockDialogWidget.getText()).thenReturn("Hello there!");
		service.onWidgetLoaded(widgetEvent);

		// Clear captured events
		capturedDialogueEvents.clear();

		// Now interact with a new NPC
		NPC newNpc = mock(NPC.class);
		when(newNpc.getId()).thenReturn(5678);
		when(newNpc.getName()).thenReturn("Bob");
		when(newNpc.getIndex()).thenReturn(99); // Different index
		when(newNpc.getAnimation()).thenReturn(-1);

		InteractingChanged event2 = mock(InteractingChanged.class);
		when(event2.getSource()).thenReturn(mockPlayer);
		when(mockPlayer.getInteracting()).thenReturn(newNpc);

		// Act
		service.onInteractingChanged(event2);

		// Now send same dialogue text - should NOT be deduplicated because state was reset
		when(widgetEvent.getGroupId()).thenReturn(InterfaceID.CHAT_LEFT);
		when(mockClient.getWidget(InterfaceID.ChatLeft.TEXT)).thenReturn(mockDialogWidget);
		when(mockDialogWidget.getText()).thenReturn("Hello there!");
		service.onWidgetLoaded(widgetEvent);

		// Assert - Should fire event because state was reset
		assertEquals("Should receive dialogue event after NPC change", 1, capturedDialogueEvents.size());
		assertEquals("NPC ID should be new NPC", 5678, capturedDialogueEvents.get(0).getNpcId());
	}
}
