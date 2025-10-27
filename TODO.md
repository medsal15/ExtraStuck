# TODO LIST #

## Important ##

Simplify arrow classes to use one class

Improve mod page with fancy graphic

## Content ##

Allow change shields (wooden shield) to also transform when dropped

Allow minestuck batteries to recharge items

Custom page type (not component) for alchemy recipes

### Machines ###

Autostorage Plate: puts inventory items in nearby storages
Atheneum add-on loader: adds extra items in the atheneum
\> It's possible to add/remove items during runtime, though there may be race conditions, prevent these from happening
\> TODO: find a way to hold a player's data in a way compatible with SburbPlayerData
\> Use additionnal player data to hold block positions & items added (instead of binding per block) -> prevents removing all copies when duplicate add-ons are loaded/unloaded
Pacarder: takes an empty card and items and stores them in the card
Unpacarder: takes a card with contents and extracts them, noop with punched cards
Gristwidget 13000: redstone-activatable version of the Gristwidget 12000
Grist reader: allows create to see the amount of grist from a player
Global grist reader: same as above but for session
Nuclear blaster: consumes uranium, poisons enemies in front of it
Nuclear hyper-blaster: consumes uranium, shoots lasers

### Tools ###

Grist map: map that is colored based on location grists, variants between common/uncommon/any

### Bosses ###

#### Spawner ####

- Spawns boss when triggered
- Entity be changed in creative mode
- 3 stages:
  - Active: triggers when a player approaches like trial spawners
  - Hibernation: inactive for an amount of time
  - Ready: requires player to activate it with UI
- UI allows spending grist for a boss of the same type (higher spend => higher chance)

#### Lich Catacombs ####

- Rooms with tombstones and lich spawners
- Tombstones are made of the land's stone
- Breaking tombstones drops land's cobblestone & spawns a lich
- Big room with Lich Queen

##### Lich Queen #####

Boss with lots of health and ranged attacks

Has 2 states, alive (with attacks) and resurrecting

Attacks:

- Breath Cutter: fast and low damage
- Blood Pool: AoE with low damage
- Heart Stop: telegraphed with high damage
- Doom Hammer: high damage with strong recoil

#### Mechanical Ruins ####

- Walls made of gears (hopefully create compatible)
- Lots of puzzles
- 3d maze
- Top room with Grist Golem

##### Grist Golem #####

Multipart boss with high health and obvious weak point

Weak point takes double damage

Attacks:

- Ground Slam: AoE and sends in the air
- Reach: Attempts to reach and grab a player
- Body Slam: Slams player into ground/wall for kinetic damage
- Shatter: Dying results in smaller, weaker, simpler grist golems

## Textures ##

Improve all shields' models and textures

- Add custom handles where it makes sense
- Make the halt shield look more like Halt, from Doors
- Animate the flame shield to look like it's on fire

Improve all blocks textures
