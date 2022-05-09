package net.thewinnt.dominoes.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
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

    private TextButton btn_back;
    private TextButton btn_local;
    private TextButton btn_single;
    private TextButton btn_bot_battle;
    private TextButton btn_online;

    private Label title;
    private Label edition;
    private Label select_gamemode;

    public GamemodeSelectScreen(final Dominoes dmn) {
        game = dmn;
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
        RectangleDrawable disabled = new RectangleDrawable(drawer);
        normal.setColors(game.theme.colors[14], game.theme.colors[0]);
        pressed.setColors(game.theme.colors[13], game.theme.colors[0]);
        over.setColors(game.theme.colors[12], game.theme.colors[0]);
        disabled.setColors(game.theme.colors[13], game.theme.colors[16]);
        TextButtonStyle style_button = new TextButtonStyle(normal, pressed, normal, game.font_button);
        TextButtonStyle style_inactive = new TextButtonStyle(disabled, disabled, disabled, game.font_disabled);
        LabelStyle style_title = new LabelStyle(game.font_title, game.theme.colors[15]);
        LabelStyle style_edition = new LabelStyle(game.font_edition, game.theme.colors[15]);
        LabelStyle style_select = new LabelStyle(game.font_select_gm, game.theme.colors[15]);
        style_button.over = over;
        style_button.checkedOver = over;

        // create
        btn_back = new TextButton("Назад", style_button);
        btn_local = new TextButton("Локальная игра", style_button);
        btn_single = new TextButton("В разработке...", style_inactive);
        btn_bot_battle = new TextButton("В разработке...", style_inactive);
        btn_online = new TextButton("В разработке...", style_inactive);

        title = new Label("Домино", style_title);
        edition = new Label("Java Edition", style_edition);
        select_gamemode = new Label("Выберите режим игры:", style_select);

        // position
        title.setPosition(640, 720, Align.top);
        edition.setPosition(640, 510, Align.top);
        select_gamemode.setPosition(640, 405, Align.top);

        btn_back.setSize(300, 55);
        btn_local.setSize(300, 55);
        btn_single.setSize(300, 55);
        btn_bot_battle.setSize(300, 55);
        btn_online.setSize(300, 55);

        btn_single.setDisabled(true);
        btn_bot_battle.setDisabled(true);
        btn_online.setDisabled(true);

        btn_back.setPosition(490, 120, Align.topLeft);
        btn_local.setPosition(250, 320, Align.topLeft);
        btn_online.setPosition(250, 220, Align.topLeft);
        btn_single.setPosition(730, 320, Align.topLeft);
        btn_bot_battle.setPosition(730, 220, Align.topLeft);

        btn_back.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(Dominoes.MAIN_MENU);
            }
        });

        btn_local.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(Dominoes.LOCAL_GAME);
            }
        });

        // add to stage
        stage.addActor(title);
        stage.addActor(edition);
        stage.addActor(select_gamemode);
        stage.addActor(btn_back);
        stage.addActor(btn_local);
        stage.addActor(btn_single);
        stage.addActor(btn_bot_battle);
        stage.addActor(btn_online);
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(game.theme.colors[12]);
        game.mainMenuScreen.render_splash(dt);
        if (Gdx.input.isKeyJustPressed(Keys.ESCAPE) || Gdx.input.isButtonJustPressed(Buttons.BACK)) {
            game.changeScreen(Dominoes.MAIN_MENU);
            return;
        }
        stage.act(dt);
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
