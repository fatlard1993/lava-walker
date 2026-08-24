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

## Getting One

Lava Walker is a treasure enchantment: the enchanting table will never give it to you. It comes out of chests and off fishing rods, and neither of those is a plan.

So with [village-quests](https://github.com/fatlard1993/village-quests) installed, a librarian who trusts you a great deal (60 reputation) will sell the one book they do not put on the shelf, for 32 emeralds.

A shop rather than a quest, because there is nothing to do: you have emeralds, they have the book, and they have decided you are the sort of person who should have it.

Optional and guarded: without village-quests the mod behaves exactly as before.

## Screenshot

![Lava Walker](img.png)

## Pandorical

Lava Walker runs server-side, and Pandorical is a hard dependency (`fabric.mod.json`): the server will not load this mod without it. It syncs the enchantment's translations through Pandorical's content sync, and that is the whole of its Pandorical usage.

Clients are the optional half, and the stake is only the name. A player on a Pandorical client sees "Lava Walker"; a player on a vanilla client sees the raw translation key. The enchantment works identically either way.

## Installation

Install server-side alongside its declared dependencies (see `fabric.mod.json`); connecting clients need only Pandorical. Version targets live in `gradle.properties` (Minecraft, loader, Fabric API) and `fabric.mod.json` (Java).

## License

MIT, see [LICENSE](LICENSE).
