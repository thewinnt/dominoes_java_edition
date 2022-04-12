package net.thewinnt.dominoes.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Splash extends Group {
    public Label label;
    public Splash(Label splash) {
        label = splash;
        this.setTransform(true);
        this.addActor(label);
        this.addAction(Actions.rotateBy(25));
    }

    public void setScale(float scale) {
        this.addAction(Actions.scaleTo(getWidth() * scale, getHeight() * scale));
    }
}
