package net.thewinnt.dominoes.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import net.thewinnt.dominoes.Dominoes;
import net.thewinnt.dominoes.ui.RectangleDrawable;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class GamemodeSelectScreen implements Screen {
    final Dominoes game;
    private Stage stage;
    private Texture texture;
    private ShapeDrawer drawer;

    public GamemodeSelectScreen(final Dominoes dmn) {
        game = dmn;
        stage = new Stage(new ScreenViewport(), new PolygonSpriteBatch());
        Gdx.input.setInputProcessor(stage);

        // prepare UI elements
        Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.drawPixel(0, 0);
        texture = new Texture(pixmap); //remember to dispose of later
        pixmap.dispose();
        TextureRegion region = new TextureRegion(texture, 0, 0, 1, 1);
        drawer = new ShapeDrawer(stage.getBatch(), region);
        RectangleDrawable normal = new RectangleDrawable(drawer);
        RectangleDrawable pressed = new RectangleDrawable(drawer);
        RectangleDrawable over = new RectangleDrawable(drawer);
        normal.out_color = game.theme.colors[0];
        normal.in_color = game.theme.colors[14];
        pressed.out_color = game.theme.colors[0];
        pressed.in_color = game.theme.colors[13];
        over.out_color = game.theme.colors[0];
        over.in_color = game.theme.colors[12];
        TextButtonStyle button_style = new TextButtonStyle(normal, pressed, normal, game.font_button);
        LabelStyle text_style_title = new LabelStyle(game.font_title, game.theme.colors[15]);
        LabelStyle text_style_edition = new LabelStyle(game.font_edition, game.theme.colors[15]);
        button_style.over = over;
        button_style.checkedOver = over;
    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.01172f, 0.8196f, 1, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
        
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
    
}
