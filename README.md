# Lava Walker

A Fabric mod that adds the **Lava Walker** enchantment.

## Features

**Lava Walker** works like Frost Walker, but for lava - walk across lava lakes by turning lava source blocks into cobblestone beneath your feet.

- Enchantment for boots
- Converts lava source blocks to cobblestone when walking nearby
- Small 2-block radius for balance
- Cobblestone is permanent (unlike Frost Walker's ice)
- Treasure enchantment (found in loot, not enchanting table)
- Mutually exclusive with Frost Walker and Depth Strider

## Screenshot

![Lava Walker](img.png)

## Requirements

- Targets the Minecraft, Fabric Loader, and Fabric API versions declared in this mod's `gradle.properties`; check there for the exact currently-supported version
- Java version as declared in `fabric.mod.json`'s `depends` block

## Pandorical

Lava Walker runs server-side, and Pandorical is a hard dependency (`fabric.mod.json`): the server will not load this mod without it. It syncs the enchantment's translations through Pandorical's content sync, and that is the whole of its Pandorical usage.

Clients are the optional half, and the stake is only the name. A player on a Pandorical client sees "Lava Walker"; a player on a vanilla client sees the raw translation key. The enchantment works identically either way.

## Installation

Install server-side alongside its declared dependencies (see `fabric.mod.json`), Pandorical included. No client needs the Lava Walker jar.

## License

MIT License - see [LICENSE](LICENSE)
