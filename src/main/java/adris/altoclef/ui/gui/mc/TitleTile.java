package adris.altoclef.ui.gui.mc;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import baritone.compat.input.InputWithModifiers;
import baritone.compat.input.MouseButtonEvent;
import net.minecraft.client.sounds.SoundManager;

// the clickable stand-in for one hidden vanilla title button (a dock tile, a corner button, the copyright line). it
// draws nothing, TitleMenu draws everything in one kit pass and moves these boxes to wherever it drew last frame. a
// press goes to the vanilla button's own onPress, so other mods' hooks on those buttons still fire. its active is
// mirrored from the vanilla one every frame, which is the whole guard: AbstractButton.onClick doesn't check it
public final class TitleTile extends AbstractButton {

    final Button target;
    private final int guiScale;
    // device px, from the last frame the chrome drew. zero size until then, so nothing is clickable before it shows
    int px;
    int py;
    int pw;
    int ph;
    // the mouse went down on us and hasn't come up yet, for the pressed look
    boolean held;

    TitleTile(Button target, int guiScale) {
        super(0, 0, 1, 1, target.getMessage());
        this.target = target;
        this.guiScale = Math.max(1, guiScale);
        this.active = target.active;
    }

    // the device px box, and the gui unit box around it for vanilla's focus and narration bookkeeping
    void place(int x, int y, int w, int h) {
        px = x;
        py = y;
        pw = w;
        ph = h;
        int gx = Math.floorDiv(x, guiScale);
        int gy = Math.floorDiv(y, guiScale);
        setX(gx);
        setY(gy);
        setWidth(Math.max(1, Math.floorDiv(x + w + guiScale - 1, guiScale) - gx));
        setHeight(Math.max(1, Math.floorDiv(y + h + guiScale - 1, guiScale) - gy));
    }

    void mirror() {
        active = target.active;
        if (!active) {
            held = false;
        }
    }

    public void onPress(InputWithModifiers input) {
        if (target.active) {
            target.onPress();
        }
    }

    // hit test in device px against what was drawn, not the rounded up gui box, so two tiles never share a pixel
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (!isActive()) {
            return false;
        }
        double x = mouseX * guiScale;
        double y = mouseY * guiScale;
        return x >= px && x < px + pw && y >= py && y < py + ph;
    }

    public void onClick(MouseButtonEvent event, boolean doubleClick) {
        held = true;
        super.onClick(event.x(), event.y());
    }

    public void onRelease(MouseButtonEvent event) {
        held = false;
        super.onRelease(event.x(), event.y());
    }

    // vanilla calls this for both a click and enter/space, so one override swaps the whole button sound
    @Override
    public void playDownSound(SoundManager sounds) {
        MenuSounds.click(sounds);
    }

    @Override
    protected void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partial) {
        // TitleMenu already drew us
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput out) {
        defaultButtonNarrationText(out);
    }

    // adapt the GLFW callbacks while retaining the shared GUI event handlers
    @Override
    public void onClick(double x, double y) { onClick(new MouseButtonEvent(x, y, 0, baritone.compat.input.InputWithModifiers.currentModifiers()), false); }

    @Override
    public void onRelease(double x, double y) { onRelease(new MouseButtonEvent(x, y, 0, baritone.compat.input.InputWithModifiers.currentModifiers())); }

    @Override
    public void onPress() { onPress(new baritone.compat.input.KeyEvent(0, 0, baritone.compat.input.InputWithModifiers.currentModifiers())); }
}
