# TODO LIST #

## Important ##

Add a mod icon

Simplify arrow classes to use one class

Improve mod page with fancy graphic

## Content ##

Allow change shields (wooden shield) to also transform when dropped

Allow minestuck batteries to recharge items

### Machines ###

Atheneum add-on loader: adds extra items in the atheneum
\> It's possible to add/remove items during runtime, though there may be race conditions, prevent these from happening
\> TODO: find a way to hold a player's data in a way compatible with SburbPlayerData
\> Use additionnal player data to hold block positions & items added (instead of binding per block) -> prevents removing all copies when duplicate add-ons are loaded/unloaded
Pacarder: takes an empty card and items and stores them in the card
Unpacarder: takes a card with contents and extracts them, noop with punched cards
Grist reader: allows create to see the amount of grist from a player
Global grist reader: same as above but for session
Nuclear blaster: consumes uranium, poisons enemies in front of it
Nuclear hyper-blaster: consumes uranium, shoots lasers

### Tools ###

Grist location: reveals current location grists, allows searching for specifc grist and switching between common/uncommon/any
Grist map: map that is colored based on location grists, variants between common/uncommon/any

## Textures ##

Improve all shields' models and textures

- Add custom handles where it makes sense
- Make the halt shield look more like Halt, from Doors
- Animate the flame shield to look like it's on fire

Improve all blocks textures
