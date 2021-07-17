# Space Figther

Welcome to Space Fighter!

Space Fighter is a 2-Player game where the objective is to damage the opposing player’s space-ship until they have no more lives left. The game ends when one player loses all their lives. When this occurs, input from the key board is disabled. A message on the screen will annouce the winner. Exit the program by pressing the red exit button on the top-right corner of the window at any time.

The game starts automatically, 2 seconds after the program is run. The queue is the moment the START photo disappears.

Each player starts off with 5 lives, represented by the 5 hearts below the green health bar on each side of the screen. The bullets coming out of your own ship do not affect your own health, but affect your opponent’s health. Damage is directly proportional to the size of the bullet shot. Bullet size can be increased by gaining Power Ups.

### Power Ups

There are two Power Ups available.

1. Bullet Power Up - this is a yellow square with a `B` in the center. Default bullet size is 10. Bullet size increases by 20 each time a Bullet Power Up is gained. 

2. Health Power Up - this is a green square with a `H` in the center. Health bar is reset to full 100 when Health Power Up is gained. One extra life is earned as well, if only if the player’s remaining number of lives is not already at the maximum of 5.

Power Ups will appear on the battle-field at random locations whenever either player loses a life. When a player does enough damage to take away one of their opponent’s lives, their Bullet is removed, for equal and fair play.

Bullets that go off the boundaries of the screen will disappear, but a Space-ship that moves "off grid" will reappear on the
opposite end of the screen.

### Controls

##### Player 1 (Cyan Space-ship)

`W` - accelerate
`S` - slow down
`A` - rotate left
`D` - rotate right
`E` - shoot bullets

##### Player 2 (Yellow Space-ship)

`I` - accelerate
`K` - slow down
`J` - rotate left
`L` - rotate right
`O` - shoot bullets

NOTE: it is recommended that players do not press and hold onto keys, because of the nature of the keyTyped() function in the SuperFighter class. It is recommended to press and release, but it's up to you.
