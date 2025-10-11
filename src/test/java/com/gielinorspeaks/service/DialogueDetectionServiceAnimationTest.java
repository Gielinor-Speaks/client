package com.gielinorspeaks.service;

import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.gameval.InterfaceID;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for DialogueDetectionService animation extraction.
 * Tests animation ID extraction from widgets and NPC animations.
 */
public class DialogueDetectionServiceAnimationTest extends DialogueDetectionServiceTestBase {
	@Test
	public void testAnimationExtraction_fromChatLeftWidget() {
		// Arrange
		setupNpcInteraction();

		when(mockClient.getWidget(InterfaceID.ChatLeft.HEAD)).thenReturn(mockHeadWidget);
		when(mockHeadWidget.getAnimationId()).thenReturn(9850); // Some animation

		WidgetLoaded event = mock(WidgetLoaded.class);
		when(event.getGroupId()).thenReturn(InterfaceID.CHAT_LEFT);
		when(mockClient.getWidget(InterfaceID.ChatLeft.TEXT)).thenReturn(mockDialogWidget);
		when(mockDialogWidget.isHidden()).thenReturn(false);
		when(mockDialogWidget.getText()).thenReturn("Hello!");

		// Act
		service.onWidgetLoaded(event);

		// Assert
		assertEquals("Should capture animation ID", Integer.valueOf(9850), capturedDialogueEvents.get(0).getAnimationId());
	}

	@Test
	public void testAnimationExtraction_fromChatRightWidget() {
		// Arrange
		setupNpcInteraction();

		when(mockClient.getWidget(InterfaceID.ChatLeft.HEAD)).thenReturn(null);
		when(mockClient.getWidget(InterfaceID.ChatRight.HEAD)).thenReturn(mockHeadWidget);
		when(mockHeadWidget.getAnimationId()).thenReturn(9851);

		WidgetLoaded event = mock(WidgetLoaded.class);
		when(event.getGroupId()).thenReturn(InterfaceID.CHAT_RIGHT);
		when(mockClient.getWidget(InterfaceID.ChatRight.TEXT)).thenReturn(mockDialogWidget);
		when(mockDialogWidget.isHidden()).thenReturn(false);
		when(mockDialogWidget.getText()).thenReturn("Hello!");

		// Act
		service.onWidgetLoaded(event);

		// Assert
		assertEquals("Should capture animation ID from right widget", Integer.valueOf(9851), capturedDialogueEvents.get(0).getAnimationId());
	}

	@Test
	public void testAnimationExtraction_fallbackToNpcAnimation() {
		// Arrange
		setupNpcInteraction();
		when(mockNpc.getAnimation()).thenReturn(1234); // NPC has animation

		when(mockClient.getWidget(InterfaceID.ChatLeft.HEAD)).thenReturn(null);
		when(mockClient.getWidget(InterfaceID.ChatRight.HEAD)).thenReturn(null);

		WidgetLoaded event = mock(WidgetLoaded.class);
		when(event.getGroupId()).thenReturn(InterfaceID.CHAT_LEFT);
		when(mockClient.getWidget(InterfaceID.ChatLeft.TEXT)).thenReturn(mockDialogWidget);
		when(mockDialogWidget.isHidden()).thenReturn(false);
		when(mockDialogWidget.getText()).thenReturn("Hello!");

		// Act
		service.onWidgetLoaded(event);

		// Assert
		assertEquals("Should fall back to NPC animation", Integer.valueOf(1234), capturedDialogueEvents.get(0).getAnimationId());
	}

	@Test
	public void testAnimationExtraction_nullWhenNoAnimation() {
		// Arrange
		setupNpcInteraction();
		when(mockNpc.getAnimation()).thenReturn(-1); // No animation

		when(mockClient.getWidget(InterfaceID.ChatLeft.HEAD)).thenReturn(null);
		when(mockClient.getWidget(InterfaceID.ChatRight.HEAD)).thenReturn(null);

		WidgetLoaded event = mock(WidgetLoaded.class);
		when(event.getGroupId()).thenReturn(InterfaceID.CHAT_LEFT);
		when(mockClient.getWidget(InterfaceID.ChatLeft.TEXT)).thenReturn(mockDialogWidget);
		when(mockDialogWidget.isHidden()).thenReturn(false);
		when(mockDialogWidget.getText()).thenReturn("Hello!");

		// Act
		service.onWidgetLoaded(event);

		// Assert
		assertNull("Animation ID should be null when none available", capturedDialogueEvents.get(0).getAnimationId());
	}
}
