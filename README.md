# Block Game

A Java/Swing puzzle game, a version of "Blocky". The board is a recursively subdivided square stored as a **quadtree**. Players rotate, reflect and smash blocks to score points toward a secret colour goal.

Built as a university data structures course assignment.

<!-- TODO: add a screenshot of the game board here -->

## Tech stack

Java (no external libraries), Swing for the GUI.

## What's interesting

- **Quadtree board:**
  - Each `Block` is either a solid colour or has exactly four children (UR, UL, LL, LR).
  - A board is generated randomly to a configurable maximum depth.
  - After every change, sizes and positions are recomputed recursively (`updateSizeAndPosition`).
- **Recursive operations:**
  - `getSelectedBlock` finds the block at a point and level.
  - `rotate` and `reflect` permute children and recurse.
  - `smash` replaces a block with four new random children.
- **Flattening:** `flatten()` turns the quadtree into a 2D grid of unit cells, so scoring can work on a plain array.
- **Scoring goals:**
  - **Blob goal:** the largest connected region of the target colour, found with a recursive flood fill over the flattened grid.
  - **Perimeter goal:** target-colour cells on the board's edge, with corners counting double.

## Running it

Requirements: JDK 11 or newer.

```bash
javac -d out src/assignment3/*.java
java -cp out assignment3.BlockGame
```

At startup, dialogs ask for:
- the maximum subdivision depth;
- one or two players;
- the number of turns.

**Controls:**
- Click a block to select it.
- **↑ / ↓** select a bigger or smaller block at that spot.
- Use the buttons to **Smash**, **Rotate** (clockwise or counter-clockwise) or **Reflect** (horizontal or vertical) the selected block.
