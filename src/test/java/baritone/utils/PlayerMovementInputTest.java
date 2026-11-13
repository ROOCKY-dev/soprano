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

package baritone.utils;

import baritone.api.utils.IInputOverrideHandler;
import baritone.api.utils.input.Input;
import org.junit.Test;
import java.util.EnumSet;
import static org.junit.Assert.*;

public class PlayerMovementInputTest {
    private static final class Keys implements IInputOverrideHandler {
        private final EnumSet<Input> down = EnumSet.noneOf(Input.class);
        public boolean isInputForcedDown(Input input) { return down.contains(input); }
        public void setInputForceState(Input input, boolean forced) {
            if (forced) down.add(input); else down.remove(input);
        }
        public void clearAllKeys() { down.clear(); }
    }

    @Test
    public void publishesLegacyMovementFlagsAndClearsReleasedKeys() {
        Keys keys = new Keys();
        PlayerMovementInput input = new PlayerMovementInput(keys);
        keys.down.addAll(EnumSet.of(Input.MOVE_FORWARD, Input.MOVE_LEFT, Input.JUMP, Input.SNEAK));
        input.tick(false, 0.3f);
        assertTrue(input.up && input.left && input.jumping && input.shiftKeyDown);
        assertFalse(input.down || input.right);
        assertEquals(1f, input.forwardImpulse, 0f);
        assertEquals(1f, input.leftImpulse, 0f);
        keys.clearAllKeys();
        input.tick(false, 0.3f);
        assertFalse(input.up || input.down || input.left || input.right || input.jumping || input.shiftKeyDown);
        assertEquals(0f, input.forwardImpulse, 0f);
        assertEquals(0f, input.leftImpulse, 0f);
    }

    @Test
    public void appliesVanillaSlowdownAndCancelsOpposingDirections() {
        Keys keys = new Keys();
        PlayerMovementInput input = new PlayerMovementInput(keys);
        keys.down.addAll(EnumSet.of(Input.MOVE_BACK, Input.MOVE_RIGHT));
        input.tick(true, 0.3f);
        assertEquals(-0.3f, input.forwardImpulse, 0f);
        assertEquals(-0.3f, input.leftImpulse, 0f);
        keys.down.addAll(EnumSet.of(Input.MOVE_FORWARD, Input.MOVE_LEFT));
        input.tick(true, 0.3f);
        assertEquals(0f, input.forwardImpulse, 0f);
        assertEquals(0f, input.leftImpulse, 0f);
    }
}
