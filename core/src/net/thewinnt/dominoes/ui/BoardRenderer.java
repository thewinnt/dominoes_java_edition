package net.thewinnt.dominoes.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import net.thewinnt.dominoes.server.Domino;
import net.thewinnt.dominoes.server.Side;
import net.thewinnt.dominoes.server.Domino.Placement;
import net.thewinnt.dominoes.ui.UIDomino.Rotation;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class BoardRenderer {
    private BitmapFont font;
    private ShapeDrawer drawer;
    private Theme theme;
    private Side[] sides;
    private Domino center;
    private String config;
    private boolean[] force_straight;
    private boolean[] just_double_turned;
    private ArrayList<Integer>[] cycles;
    private ArrayList<DominoRenderData>[] draw_queues;
    private DominoRenderState[] positions;
    private int x, y;
    private Rotation rot;
    private int pos;
    private int dmn;
    private int side;
    private Domino domino;
    private float scale;
    private int offset_x;
    private int offset_y;
    private float font_scale;
    private int min_reached_dmn;

    public BoardRenderer(String config, Side[] sides, Domino center, BitmapFont font, ShapeDrawer drawer, Theme theme) {
        this.config = config;
        this.sides = sides;
        this.font = font;
        this.drawer = drawer;
        this.center = center.clone();
        this.theme = theme;
    }

    public void setCenter(Domino new_center) {
        center = new_center;
    }

    public void setConfig(String new_config) {
        config = new_config;
    }

    public void setFontScale(float new_scale) {
        font_scale = new_scale;
    }

    public void draw(int offset_x, int offset_y) {
        draw(offset_x, offset_y, 1.0f, sides);
    }

    public void draw(float scale) {
        draw(0, 0, scale, sides);
    }

    public void draw(int offset_x, int offset_y, float scale) {
        draw(offset_x, offset_y, scale, sides);
    }

    private void turn(int side, boolean direction, boolean add) {
        boolean last_place;
        try {
            last_place = draw_queues[side].get(draw_queues[side].size() - 1).type.placement == Placement.DOUBLE;
        } catch (IndexOutOfBoundsException e) {
            if (center != null) {
                last_place = center.placement == Placement.DOUBLE;
            } else {
                last_place = true;
            }
        }
        boolean place = domino.placement == Placement.DOUBLE;
        if (just_double_turned[side]) {
            last_place = false;
            place = false;
        }
        // regular turns
        if (direction && !last_place && !place) {
            switch (rot) {
                case RIGHT:
                    x += 89;
                    y -= 30;
                    break;
                case DOWN:
                    x -= 30;
                    y -= 89;
                    break;
                case LEFT:
                    x -= 89;
                    y += 30;
                    break;
                case UP:
                    x += 30;
                    y += 89;
                    break;
            }
            rot = rot.nextRot();
        } else if (!direction && !last_place && !place) {
            switch (rot) {
                case RIGHT:
                    x += 89;
                    y += 30;
                    break;
                case DOWN:
                    x += 30;
                    y -= 89;
                    break;
                case LEFT:
                    x -= 89;
                    y -= 30;
                    break;
                case UP:
                    x -= 30;
                    y += 89;
                    break;
            }
            rot = rot.prevRot();
        } else if (place) {
            // turning at a double
            switch (rot) {
                case RIGHT:
                    x += 89;
                    break;
                case DOWN:
                    y -= 89;
                    break;
                case LEFT:
                    x -= 89;
                    break;
                case UP:
                    y += 89;
                    break;
            }
            rot = direction ? rot.nextRot() : rot.prevRot();
        } else if (last_place && !place) {
            // turning right after a double
            switch (rot) {
                case RIGHT:
                    y -= 119 * (direction ? 1 : -1);
                    break;
                case DOWN:
                    x -= 119 * (direction ? 1 : -1);
                    break;
                case LEFT:
                    y += 119 * (direction ? 1 : -1);
                    break;
                case UP:
                    x += 119 * (direction ? 1 : -1);
                    break;
            }
            rot = direction ? rot.nextRot() : rot.prevRot();
        }
        if (add) {
            just_double_turned[side] = false;
            if (place) {
                just_double_turned[side] = true;
                force_straight[side] = true;
            }
            if (domino.placement == Placement.REVERSED) {
                draw_queues[side].add(new DominoRenderData(x, y, rot.invert(), domino));
            } else {
                draw_queues[side].add(new DominoRenderData(x, y, rot, domino));
            }
        }
    }

    private void forward(int side, boolean add, float step) {
        boolean single_force = false;
        Domino last_dmn;
        try {
            last_dmn = draw_queues[side].get(draw_queues[side].size() - 1).type;
        } catch (IndexOutOfBoundsException e) {
            last_dmn = center;
            if (last_dmn == null) {
                last_dmn = new Domino(0, 0, Placement.DOUBLE);
            }
        }
        if (last_dmn == center && center.placement == Placement.DOUBLE && rot.isVertical() && domino.placement != Placement.DOUBLE) {
            force_straight[side] = true;
            single_force = true;
        }

        if (rot.isVertical()) {
            if (!force_straight[side] && (last_dmn.placement == Placement.DOUBLE || domino.placement == Placement.DOUBLE)) {
                y -= 89 * (rot == Rotation.UP ? -1 : 1) * step;
            } else {
                y -= 119 * (rot == Rotation.UP ? -1 : 1) * step;
            }
        } else {
            if (!force_straight[side] && (last_dmn.placement == Placement.DOUBLE || domino.placement == Placement.DOUBLE)) {
                x += 89 * (rot == Rotation.LEFT ? -1 : 1) * step;
            } else {
                x += 119 * (rot == Rotation.LEFT ? -1 : 1) * step;
            }
        }

        if (single_force) {
            force_straight[side] = false;
        }

        if (add) {
            switch (domino.placement) {
                case DOUBLE:
                    draw_queues[side].add(new DominoRenderData(x, y, rot.nextRot(), domino));
                    break;
                case REVERSED:
                    draw_queues[side].add(new DominoRenderData(x, y, rot.invert(), domino));
                    break;
                case NORMAL:
                    draw_queues[side].add(new DominoRenderData(x, y, rot, domino));
            }
            force_straight[side] = false;
            just_double_turned[side] = false;
        }
    }

    private boolean testForIntersection(int x, int y, Rotation rot, Placement placement) {
        Rectangle collision_test;
        Rectangle current;
        // prepare the subject
        if ((!rot.isVertical()) != (placement == Placement.DOUBLE)) { // that is xor btw
            collision_test = new Rectangle(x - 59, y + 49, 119, 99);
        } else {
            collision_test = new Rectangle(x - 49, y + 59, 99, 119);
        }
        // start checking
        boolean has_collided = false;
        // test center
        if (center != null) {
            if (center.placement == Placement.DOUBLE) {
                current = new Rectangle(offset_x-49, offset_y+59, 99, 119);
            } else {
                current = new Rectangle(offset_x-59, offset_y+49, 119, 99);
            }
            has_collided = collision_test.overlaps(current) || current.overlaps(collision_test);
        }
        if (has_collided) return true;
        for (ArrayList<DominoRenderData> line : draw_queues) {
            for (DominoRenderData i : line) {
                if ((!i.rotation.isVertical()) != (placement == Placement.DOUBLE)) {
                    current = new Rectangle(i.x - 59, i.y + 49, 119, 99);
                } else {
                    current = new Rectangle(i.x - 49, i.y + 59, 99, 119);
                }
                has_collided = collision_test.overlaps(current) || current.overlaps(collision_test);
                if (has_collided) return true;
            }
            if (has_collided) return true;
        }

        return has_collided;
    }

    private boolean testCondition(char condition) {
        boolean result = false;
        int bkp_x, bkp_y;
        Rotation bkp_rot;
        switch (condition) {
            case 'E':
                return true;
            case 'r':
                bkp_x = x;
                bkp_y = y;
                bkp_rot = rot;
                turn(side, true, false);
                forward(side, false, 1);
                if (testForIntersection(x, y, rot, domino.placement)) {
                    result = true;
                }
                forward(side, false, 1);
                if (testForIntersection(x, y, rot, domino.placement) || result) {
                    result = true;
                } else {
                    result = false;
                }
                x = bkp_x;
                y = bkp_y;
                rot = bkp_rot;
                break;
            case 'R':
                bkp_x = x;
                bkp_y = y;
                bkp_rot = rot;
                turn(side, true, false);
                forward(side, false, 1);
                if (testForIntersection(x, y, rot, domino.placement)) {
                    result = false;
                }
                forward(side, false, 1);
                if (testForIntersection(x, y, rot, domino.placement) || result) {
                    result = false;
                } else {
                    result = true;
                }
                x = bkp_x;
                y = bkp_y;
                rot = bkp_rot;
                break;
            case 'l':
                bkp_x = x;
                bkp_y = y;
                bkp_rot = rot;
                turn(side, false, false);
                forward(side, false, 1);
                if (testForIntersection(x, y, rot, domino.placement)) {
                    result = true;
                }
                forward(side, false, 1);
                if (testForIntersection(x, y, rot, domino.placement) || result) {
                    result = true;
                } else {
                    result = false;
                }
                x = bkp_x;
                y = bkp_y;
                rot = bkp_rot;
                break;
            case 'L':
                bkp_x = x;
                bkp_y = y;
                bkp_rot = rot;
                turn(side, false, false);
                forward(side, false, 1);
                if (testForIntersection(x, y, rot, domino.placement)) {
                    result = false;
                }
                forward(side, false, 1);
                if (testForIntersection(x, y, rot, domino.placement) || result) {
                    result = false;
                } else {
                    result = true;
                }
                x = bkp_x;
                y = bkp_y;
                rot = bkp_rot;
                break;
            case 'f':
                bkp_x = x;
                bkp_y = y;
                bkp_rot = rot;
                forward(side, false, 1);
                forward(side, false, 1);
                result = testForIntersection(x, y, rot, domino.placement);
                x = bkp_x;
                y = bkp_y;
                rot = bkp_rot;
            case 'F':
                bkp_x = x;
                bkp_y = y;
                bkp_rot = rot;
                forward(side, false, 1);
                forward(side, false, 1);
                result = !testForIntersection(x, y, rot, domino.placement);
                x = bkp_x;
                y = bkp_y;
                rot = bkp_rot;
            case 'A':
                bkp_x = x;
                bkp_y = y;
                bkp_rot = rot;
                forward(side, false, 1);
                forward(side, false, 1);
                Rectangle window = new Rectangle(0, 0, 1280, 720);
                Rectangle test_for;
                window.setSize(1280 / scale, 720 / scale);
                if (rot.isVertical()) {
                    test_for = new Rectangle(x - 29, y + 59, 59, 119);
                    test_for.setSize(59 / scale, 119 / scale);
                    window.setWidth(Float.MAX_VALUE);
                } else {
                    test_for = new Rectangle(x - 59, y + 29, 119, 59);
                    test_for.setSize(119 / scale, 59 / scale);
                    window.setHeight(Float.MAX_VALUE);
                }
                test_for.setPosition(x + offset_x / scale, y + offset_y / scale);
                result = window.contains(test_for);
                x = bkp_x;
                y = bkp_y;
                rot = bkp_rot;
        }
        return result;
    }

    private record DominoRenderData(int x, int y, Rotation rotation, Domino type) {}
    private record DominoRenderState(int x, int y, Rotation rotation, int code_point, int dmn_index) {}

    public void draw(int offset_x, int offset_y, float scale, Side[] sides) {
        this.sides = sides;
        this.scale = scale;
        this.offset_x = offset_x;
        this.offset_y = offset_y;
        x = y = 0; rot = Rotation.RIGHT;
        force_straight = new boolean[sides.length];
        just_double_turned = new boolean[sides.length];

        cycles = new ArrayList[sides.length];
        draw_queues = new ArrayList[sides.length];
        positions = new DominoRenderState[sides.length];
        for (int i = 0; i < sides.length; i++) {
            cycles[i] = new ArrayList<Integer>();
            draw_queues[i] = new ArrayList<DominoRenderData>();
            positions[i] = new DominoRenderState(0, 0, Rotation.fromNumber(Integer.parseInt(Character.toString(config.charAt(i)))), sides.length - 1, 0);
        }
        int repeat = 0;
        for (Side side : sides) {
            if (side.length() > repeat) repeat = side.length();
        }
        while (true) {
            min_reached_dmn = 2147483647;
            for (DominoRenderState side : positions) {
                if (min_reached_dmn >= side.dmn_index) {
                    min_reached_dmn = side.dmn_index;
                }
            }
            if (min_reached_dmn >= repeat) break;
            for (int side = 0; side < sides.length; side++) {
                x = positions[side].x;
                y = positions[side].y;
                rot = positions[side].rotation;
                pos = positions[side].code_point;
                dmn = positions[side].dmn_index;
                pos++;
                try {
                    domino = sides[side].getDomino(dmn);
                    config.charAt(pos);
                } catch (IndexOutOfBoundsException e) {
                    dmn++;
                    positions[side] = new DominoRenderState(x, y, rot, pos, dmn);
                    continue;
                }
                switch (config.charAt(pos)) {
                    case 'F':
                        forward(side, true, 1);
                        dmn++;
                        break;
                    case 'R':
                        turn(side, true, true);
                        dmn++;
                        break;
                    case 'L':
                        turn(side, false, true);
                        dmn++;
                        break;
                    case 'W':
                        cycles[side].add(pos);
                        if (testCondition(config.charAt(pos + 1))) {
                            pos += 2;
                        } else {} // TODO skip cycle
                        break;
                    case 'E':
                        cycles[side].add(pos);
                        pos++;
                        break;
                    case ']':
                        char cond;
                        if (config.charAt(cycles[side].get(cycles[side].size() - 1)) == 'E') {
                            cond = 'E';
                        } else {
                            cond = config.charAt(cycles[side].get(cycles[side].size() - 1) + 1);
                        }
                        if (testCondition(cond)) {
                            if (cond == 'E') {
                                pos = cycles[side].get(cycles[side].size() - 1) + 1;
                            } else {
                                pos = cycles[side].get(cycles[side].size() - 1) + 2;
                            }
                        } else {
                            cycles[side].remove(cycles[side].size() - 1);
                        }
                        break;
                }
                positions[side] = new DominoRenderState(x, y, rot, pos, dmn);
            }
        }
        boolean was_started = drawer.getBatch().isDrawing();
        if (!was_started) drawer.getBatch().begin();
        for (ArrayList<DominoRenderData> side : draw_queues) {
            for (DominoRenderData dmn : side) {
                UIDomino renderer = new UIDomino(dmn.type.getType(), dmn.rotation, theme, drawer, scale, font);
                if (dmn.rotation.isVertical()) {
                    renderer.setPosition(dmn.x*scale - 29*scale + offset_x, dmn.y*scale - 59*scale + offset_y);
                } else {
                    renderer.setPosition(dmn.x*scale - 59*scale + offset_x, dmn.y*scale - 29*scale + offset_y);
                }
                renderer.setFontScaleFactor(font_scale);
                renderer.setScale(scale);
                renderer.draw(drawer.getBatch(), 1);
            }
        }
        if (center != null) {
            UIDomino renderer = new UIDomino(center.getType(), center.placement == Placement.DOUBLE ? Rotation.DOWN : Rotation.RIGHT,
                                             theme, drawer, scale, font);
            
            if (center.placement == Placement.DOUBLE) {
                renderer.setPosition(-29*scale + offset_x, -59*scale + offset_y);
            } else {
                renderer.setPosition(-59*scale + offset_x, -29*scale + offset_y);
            }
            renderer.setFontScaleFactor(font_scale);
            renderer.setScale(scale);
            renderer.draw(drawer.getBatch(), 1);
        }
        if (!was_started) drawer.getBatch().end();
    }
}
