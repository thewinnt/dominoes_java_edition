package net.thewinnt.dominoes.ui;

import com.badlogic.gdx.graphics.Color;

import space.earlygrey.shapedrawer.ShapeDrawer;
import space.earlygrey.shapedrawer.scene2d.ShapeDrawerDrawable;

public class RectangleDrawable extends ShapeDrawerDrawable {
    public Color in_color = new Color(0.95f, 0.95f, 0.95f, 1);
    public Color out_color = new Color(0, 0, 0, 1);
    public int line_width = 2;

    public RectangleDrawable() {}

    public RectangleDrawable(ShapeDrawer drawer) {
        super(drawer);
    }

    @Override
    public void drawShapes(ShapeDrawer shapeDrawer, float x, float y, float width, float height) {
        shapeDrawer.filledRectangle(x + line_width/2, y + line_width/2,
                              width - line_width, height - line_width, in_color);
        shapeDrawer.rectangle(x, y, width, height, out_color, line_width);
    }
    
}
