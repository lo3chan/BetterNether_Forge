/**
 * A level-agnostic tree construction library.
 *
 * <h2>Why this lives here</h2>
 * The Forge 1.20.1 port keeps this package inside BetterNether and uses only Minecraft and the BCLib
 * APIs available on this branch. It has no WorldWeaver dependency.
 * <p>
 * The code deliberately does <em>not</em> use BCLib's {@code org.betterx.bclib.sdf} - {@link
 * org.betterx.betternether.world.tree.math.Volume} is a small standalone replacement for the part of it a
 * tree needs.
 *
 * <h2>How a tree is built</h2>
 * The pipeline is deliberately "compute everything, then write once":
 * <ol>
 *     <li>A {@link org.betterx.betternether.world.tree.skeleton.TrunkShape} produces a
 *         {@link org.betterx.betternether.world.tree.skeleton.Skeleton} - line segments with radii, plus
 *         the {@link org.betterx.betternether.world.tree.skeleton.Anchor}s where foliage attaches.</li>
 *     <li>A {@link org.betterx.betternether.world.tree.canopy.CanopyShape} turns each anchor into a
 *         {@link org.betterx.betternether.world.tree.math.Volume}.</li>
 *     <li>Both are rasterised into a {@link org.betterx.betternether.world.tree.build.TreeVoxels} buffer
 *         in tree-local coordinates - no world writes yet.</li>
 *     <li>{@link org.betterx.betternether.world.tree.decay.LeafDecay} solves the buffer so that every
 *         surviving leaf is close enough to a log for vanilla leaf decay to keep it alive, repairing the
 *         geometry where it is not.</li>
 *     <li>Only then is the buffer committed to the level.</li>
 * </ol>
 * Step 4 is the reason for the buffer. Leaf decay is a property of the <em>finished</em> tree, so it
 * cannot be decided while the tree is still being drawn, and it must be able to change the geometry -
 * which is impossible once blocks are in the world.
 *
 * @see org.betterx.betternether.world.tree.decay.LeafDecay
 */
package org.betterx.betternether.world.tree;
