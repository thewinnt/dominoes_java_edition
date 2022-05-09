package net.thewinnt.dominoes.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;

import net.thewinnt.dominoes.server.Domino.DominoType;
import net.thewinnt.dominoes.util.FontUtils;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class UIDomino extends Actor {
    private Theme theme;
    private ShapeDrawer drawer;
    private DominoType type;
    private BitmapFont font = null; // in case there's an undefined digit
    private Rotation rotation;
    private float scale;
    private float factor;

    public enum Rotation {
        RIGHT,
        DOWN,
        LEFT,
        UP;

        public boolean isVertical() {
            return (this.ordinal() % 2 == 1);
        }

        public boolean isInverted() {
            return (this.ordinal() / 2 == 1);
        }

        /** Returns the next rotation clockwise */
        public Rotation nextRot() {
            switch (this) {
                case RIGHT:
                    return Rotation.DOWN;
                case DOWN:
                    return Rotation.LEFT;
                case LEFT:
                    return Rotation.UP;
                case UP:
                    return Rotation.RIGHT;
                default:
                    return null;
            }
        }

        /** Returns the previous rotation clockwise */
        public Rotation prevRot() {
            switch(this) {
                case DOWN:
                    return Rotation.RIGHT;
                case LEFT:
                    return Rotation.DOWN;
                case RIGHT:
                    return Rotation.UP;
                case UP:
                    return Rotation.LEFT;
                default:
                    return null;
            }
        }

        /** Returns the opposite rotation. Equivalent to {@code rot.nextRot().nextRot()}
         * and to {@code rot.prevRot().prevRot()}
         */
        public Rotation invert() {
            switch(this) {
                case DOWN:
                    return Rotation.UP;
                case LEFT:
                    return Rotation.RIGHT;
                case RIGHT:
                    return Rotation.LEFT;
                case UP:
                    return Rotation.DOWN;
                default:
                    return null;
            }
        }

        public static Rotation fromNumber(int type) {
            if (type < 0 || type > 3) {
                throw new IllegalArgumentException("There are only 4 Rotations possible");
            }
            switch (type) {
                case 0: return Rotation.RIGHT;
                case 1: return Rotation.DOWN;
                case 2: return Rotation.LEFT;
                case 3: return Rotation.UP;
                default: return null;
            }
        }
    }

    public UIDomino(DominoType domino, Rotation rotation, Theme theme, ShapeDrawer drawer, float scale) {
        this.type = domino;
        this.theme = theme;
        this.scale = scale;
        this.drawer = drawer;
        this.rotation = rotation;
        if (rotation.isVertical()) {
            setBounds(getX(), getY(), 59, 119);
        } else {
            setBounds(getX(), getY(), 119, 59);
        }
    }

    @Override
    public void setScale(float new_scale) {
        scale = new_scale;
    }

    @Override
    public float getScaleX() {
        return scale;
    }

    @Override
    public float getScaleY() {
        return scale;
    }

    public UIDomino(DominoType domino, Rotation rotation, Theme theme, ShapeDrawer drawer, float scale, BitmapFont font) {
        this(domino, rotation, theme, drawer, scale);
        this.font = font;
    }

    public void setFontScaleFactor(float factor) {
        this.factor = factor;
    }

    private void drawDigit(int digit, float x, float y, Rotation rot, Batch batch) {
        // set color
        Color color;
        int color_id = digit % 18;
        if (color_id == 0) {
            color = theme.colors[11];
        } else if (color_id < 10) {
            color = theme.colors[2 + color_id];
        } else {
            color = theme.colors[12 + color_id];
        }
        boolean rotated = rot.isVertical();
        boolean inv_x = rot.isVertical() & rot.isInverted();
        boolean inv_y = !rot.isVertical() & !rot.isInverted();
        int mod_x = inv_x ? -1 : 1;
        int mod_y = inv_y ? -1 : 1;
        if (inv_x) x += 62 * scale;
        if (inv_y) y += 62 * scale;
        // actually draw the digit
        switch (digit) {
            case 0:
                break;
            case 1:
                drawer.filledCircle(x + 31 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                break;
            case 2:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                break;
            case 3:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                drawer.filledCircle(x + 31 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                break;
            case 4:
                drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                break;
            case 5:
                drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 31 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                break;
            case 6:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                break;
            case 7:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                drawer.filledCircle(x + 31 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                break;
            case 8:
                drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 31 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                break;
            case 9:
                drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 31 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 31 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                break;
            case 10:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                break;
            case 11:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                drawer.filledCircle(x + 31 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                break;
            case 12:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                break;
            case 13:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                break;
            case 14:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                break;
            case 15:
                if (rotated) {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                }
                break;
            case 16:
                drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 26 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 36 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 26 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 36 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 26 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 36 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 26 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 36 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                break;
            case 17:
                drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 24 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 24 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 24 * scale * mod_x, y + 24 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 31 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 38 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 38 * scale * mod_x, y + 24 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 24 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 38 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 24 * scale * mod_x, y + 38 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 24 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 38 * scale * mod_x, y + 38 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 38 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 38 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                break;
            case 18:
                drawer.filledCircle(x + 17 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 24 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 24 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 24 * scale * mod_x, y + 24 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 38 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 17 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 38 * scale * mod_x, y + 24 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 24 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 38 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 24 * scale * mod_x, y + 38 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 17 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 24 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 38 * scale * mod_x, y + 38 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 38 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 38 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                drawer.filledCircle(x + 45 * scale * mod_x, y + 45 * scale * mod_y, 3 * scale, color);
                if (rotated) {
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 26 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 31 * scale * mod_x, y + 36 * scale * mod_y, 3 * scale, color);
                } else {
                    drawer.filledCircle(x + 26 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                    drawer.filledCircle(x + 36 * scale * mod_x, y + 31 * scale * mod_y, 3 * scale, color);
                }
                break;
            default:
                if (font != null) {
                    Color color_orig = font.getColor().cpy();
                    font.getColor().set(color);
                    FontUtils.drawScaledCenteredText(font, batch, Integer.toString(digit),
                                                     x + 31 * scale * mod_x, y + 31 * scale * mod_y, scale * factor);
                    font.setColor(color_orig);
                } else {
                    throw new IllegalArgumentException("Got an invalid digit (" + digit + ") without a fallback font");
                }
                break;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        int x = (int) getX();
        int y = (int) getY();
        if (!rotation.isVertical()) {
            drawer.filledRectangle(x + 2 * scale, y + 2 * scale, 115 * scale, 55 * scale, theme.colors[2]);
            drawer.rectangle(x, y, 119 * scale, 59 * scale, theme.colors[0], 4 * scale);
            if (type != null) drawer.line(x + 57 * scale, y + 7 * scale, x + 57 * scale, y + 54 * scale, theme.colors[1], 3 * scale);
        } else {
            drawer.filledRectangle(x + 2 * scale, y + 2 * scale, 55 * scale, 115 * scale, theme.colors[2]);
            drawer.rectangle(x, y, 59 * scale, 119 * scale, theme.colors[0], 4 * scale);
            if (type != null) drawer.line(x + 7 * scale, y + 57 * scale, x + 54 * scale, y + 57 * scale, theme.colors[1], 3 * scale);
        }
        if (type != null) {
            switch (rotation) {
                case RIGHT:
                    drawDigit(type.a(), x - 2 * scale, y - 2 * scale, rotation, batch);
                    drawDigit(type.b(), x + 56 * scale, y - 2 * scale, rotation, batch);
                    break;
                case LEFT:
                    drawDigit(type.a(), x + 56 * scale, y - 2 * scale, rotation, batch);
                    drawDigit(type.b(), x - 2 * scale, y - 2 * scale, rotation, batch);
                    break;
                case DOWN:
                    drawDigit(type.a(), x - 2 * scale, y - 2 * scale, rotation, batch);
                    drawDigit(type.b(), x - 2 * scale, y + 56 * scale, rotation, batch);
                    break;
                case UP:
                    drawDigit(type.a(), x - 2 * scale, y + 56 * scale, rotation, batch);
                    drawDigit(type.b(), x - 2 * scale, y - 2 * scale, rotation, batch);
                    break;
            }
        }
    }
}
