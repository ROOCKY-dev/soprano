/*
 * This file is part of Soprano.
 *
 * Soprano is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Soprano is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Soprano.  If not, see <https://www.gnu.org/licenses/>.
 */

package baritone.compat.input;

public interface InputWithModifiers {
    int modifiers();
    default boolean hasShiftDown() { return (modifiers() & 1) != 0; }
    default boolean hasControlDownWithQuirk() { return (modifiers() & (net.minecraft.client.Minecraft.ON_OSX ? 8 : 2)) != 0; }
    default boolean hasAltDown() { return (modifiers() & 4) != 0; }
    static int currentModifiers() {
        return (net.minecraft.client.gui.screens.Screen.hasShiftDown() ? 1 : 0)
            | (net.minecraft.client.gui.screens.Screen.hasControlDown() ? (net.minecraft.client.Minecraft.ON_OSX ? 8 : 2) : 0)
            | (net.minecraft.client.gui.screens.Screen.hasAltDown() ? 4 : 0);
    }
}
