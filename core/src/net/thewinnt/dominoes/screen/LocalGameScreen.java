package net.thewinnt.dominoes.screen;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.thewinnt.dominoes.Dominoes;
import net.thewinnt.dominoes.server.Domino;
import net.thewinnt.dominoes.server.Side;
import net.thewinnt.dominoes.server.Domino.Placement;
import net.thewinnt.dominoes.ui.BoardRenderer;
import net.thewinnt.dominoes.ui.RectangleDrawable;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class LocalGameScreen implements Screen {
    final Dominoes game;
    private Stage stage;
    private Texture texture;
    private ShapeDrawer drawer;

    private Sound place_sound;
    private float scale = 1.0f;
    private int line_length = 25;

    private TextButton add_domino;
    private TextButton remove_domino;
    private TextField domino_count;

    private BoardRenderer renderer;
    private Side[] sides;
    private int offset_x = 640;
    private int offset_y = 360;

    public LocalGameScreen(final Dominoes game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport(), new PolygonSpriteBatch());
        Gdx.input.setInputProcessor(stage);
        // prepare UI elements
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        texture = new Texture(pixmap);
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
        drawer = new ShapeDrawer(stage.getBatch(), region);

        RectangleDrawable normal = new RectangleDrawable(drawer);
        RectangleDrawable pressed = new RectangleDrawable(drawer);
        RectangleDrawable over = new RectangleDrawable(drawer);
        normal.setColors(game.theme.colors[14], game.theme.colors[14]);
        pressed.setColors(game.theme.colors[13], game.theme.colors[13]);
        over.setColors(game.theme.colors[12], game.theme.colors[12]);
        TextButtonStyle style_button = new TextButtonStyle(normal, pressed, normal, game.font_edition);
        style_button.over = over;
        style_button.checkedOver = over;

        RectangleDrawable field = new RectangleDrawable(drawer);
        RectangleDrawable cursor = new RectangleDrawable(drawer);
        RectangleDrawable selection = new RectangleDrawable(drawer);
        field.setColors(game.theme.colors[2], game.theme.colors[0]);
        cursor.setColors(game.theme.colors[15], game.theme.colors[15]);
        selection.setColors(game.theme.colors[21], game.theme.colors[21]);
        TextFieldStyle style_field = new TextFieldStyle(game.font_board_debug, game.theme.colors[15], cursor, selection, field);
        
        add_domino = new TextButton("+", style_button);
        remove_domino = new TextButton("-", style_button);
        domino_count = new TextField(Integer.toString(line_length), style_field);

        renderer = new BoardRenderer("20FLLWl[F]E[LWl[F]]", sides, new Domino(100, 100, Placement.DOUBLE), game.font_domino, drawer, game.theme);
        renderer.setFontScale(0.125f);

        sides = new Side[2];
        setSides(line_length);

        add_domino.setPosition(40, 10);
        add_domino.setSize(30, 30);
        remove_domino.setPosition(10, 10);
        remove_domino.setSize(30, 30);
        domino_count.setPosition(75, 10);
        domino_count.setSize(45, 30);

        add_domino.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                line_length++;
                domino_count.setText(Integer.toString(line_length));
                setSides(line_length);
            }
        });

        remove_domino.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                line_length--;
                domino_count.setText(Integer.toString(line_length));
                setSides(line_length);
            }
        });

        domino_count.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                try {
                    line_length = Integer.parseInt(domino_count.getText());
                    setSides(line_length);
                } catch (NumberFormatException e) {
                    event.cancel();
                }
            }
        });

        stage.addActor(add_domino);
        stage.addActor(remove_domino);
        stage.addActor(domino_count);
    }

    private void setSides(int length) {
        sides[0] = new Side(0);
        sides[1] = new Side(0);
        for (int i = 0; i < length; i++) {
            sides[0].blindAdd(new Domino(0, 1, Dominoes.RANDOM.nextInt(25) == 0 ? Placement.DOUBLE : Placement.NORMAL));
            sides[1].blindAdd(new Domino(0, 2, Dominoes.RANDOM.nextInt(25) == 0 ? Placement.DOUBLE : Placement.NORMAL));
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.01172f, 0.8196f, 1, 1);
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isButtonJustPressed(Buttons.BACK)) {
            game.changeScreen(Dominoes.GAMEMODE_SELECT);
            return;
        }
        if (Gdx.input.isKeyJustPressed(Keys.SPACE)) game.changeScreen(Dominoes.LOCAL_GAME);
        if (Gdx.input.isKeyPressed(Keys.EQUALS) || Gdx.input.isKeyPressed(Keys.NUMPAD_ADD)) {
            scale *= 1.02;
        }
        if (Gdx.input.isKeyPressed(Keys.MINUS) || Gdx.input.isKeyPressed(Keys.NUMPAD_SUBTRACT)) {
            scale /= 1.02;
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) offset_y += 5 * (delta * 60);
        if (Gdx.input.isKeyPressed(Keys.DOWN)) offset_y -= 5 * (delta * 60);
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) offset_x += 5 * (delta * 60);
        if (Gdx.input.isKeyPressed(Keys.LEFT)) offset_x -= 5 * (delta * 60);
        if (Gdx.input.isKeyPressed(Keys.STAR) || Gdx.input.isKeyPressed(Keys.NUMPAD_MULTIPLY)) {
            offset_x = 640;
            offset_y = 360;
            scale = 1;
        }
        renderer.draw(offset_x, offset_y, scale, sides);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        texture.dispose();
        stage.dispose();
    }
}
