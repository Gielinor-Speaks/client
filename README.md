# Gielinor Speaks

Gielinor Speaks brings voice to Old School RuneScape by delivering AI-generated dialogue for NPCs. This RuneLite plugin connects to a voice synthesis backend that generates realistic character voices based on wiki data and visual appearance, allowing you to hear NPCs speak their dialogue lines with voices that match their personality and lore.

## What It Does

When you interact with an NPC in game, the plugin retrieves pre-generated voice files that correspond to their dialogue. Each NPC has a unique voice profile created through an AI synthesis pipeline that analyzes their wiki description, appearance, and character traits. The system uses Eleven Labs' voice generation technology to create voices that feel authentic to each character, whether it's a gruff dwarf merchant or a refined elven scholar.

The plugin itself is lightweight. It doesn't generate voices on the fly but rather requests them from a backend service that has already processed NPC data and created appropriate voice profiles. This means you get high-quality synthesis without any performance impact during gameplay. Voice files are cached locally once retrieved, so subsequent interactions with the same NPC play instantly.

## How It Works

The architecture separates voice generation from voice delivery. A backend pipeline analyzes NPC wiki entries, extracting textual descriptions and visual characteristics from character images. This data feeds into an AI voice description system that creates detailed voice profiles. Eleven Labs then generates multiple candidate voices, and an automated judge selects the best match for each character. These voice profiles are stored with mappings to NPC IDs, ready for the plugin to request them.

When you encounter dialogue in game, the plugin identifies the NPC and dialogue, requests the corresponding audio file from the backend, and plays it through your speakers. The system maintains consistency, so the same NPC always sounds like themselves across different interactions and dialogue branches.

## Technical Notes

This plugin communicates with a REST API that serves pre-generated voice files. Voice profiles are permanently stored once created, so the same NPC will always have the same voice across sessions and updates. The dialogue hash system ensures that specific lines are consistently matched to their audio representations.

The backend handles all the heavy lifting around voice synthesis, wiki data extraction, and candidate selection. This repository contains only the client-side delivery mechanism, keeping the plugin simple and focused on its core purpose of enhancing your gameplay experience with voiced NPCs.

## Contributing

This project is part of a larger voice synthesis pipeline for OSRS. If you're interested in improving the voice selection algorithm, adding support for new NPCs, or enhancing the audio quality, feel free to open issues or submit pull requests. The system is designed to be extensible, and community contributions help make Gielinor feel more alive.