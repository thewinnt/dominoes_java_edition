package net.thewinnt.dominoes.screen;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
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

public class MainMenuScreen implements Screen {
    final Dominoes game;
    private Stage stage;
    private Texture texture;
    private ShapeDrawer drawer;

    private TextButton btn_play;
    private TextButton btn_settings;
    private TextButton btn_exit;
    private TextButton btn_load_game;
    private TextButton btn_tutorial;

    private Label title;
    private Label edition;
    private Label version;
    private Label fps;

    private Matrix4 old_matrix;
    private Matrix4 mx4_font;

    private float splash_time;
    private float frame_delay;
    private float fps_timer;
    private String final_splash = "";

    public MainMenuScreen(final Dominoes dmn) {
        game = dmn;
    }

    public void render_splash(float dt) {
        splash_time += dt * 12;
        frame_delay += dt * 12;
        float scale = 1 + (float) Math.sin(splash_time) / 30;
        // prepare splash
        if (game.splash == "\uF400") {
            final_splash = "This splash can't be translated yet";
        } else if (game.splash == "\uF401") {
            final_splash = "APRIL FOOLS!!!";
        } else if (frame_delay > 0.5f) {
            final_splash = "";
            frame_delay = 0f;
            for (int i = 0; i < game.splash.length(); i++) {
                if (game.splash.charAt(i) == '&') {
                    final_splash += game.font_chars.charAt(new Random().nextInt(game.font_chars.length()));
                } else {
                    final_splash += game.splash.charAt(i);
                }
            }
        }
        PolygonSpriteBatch temp = new PolygonSpriteBatch();
        temp.begin();
        GlyphLayout layout = game.font_splash.draw(temp, final_splash, 0, 0);
        temp.end();
        temp.dispose();
        mx4_font = new Matrix4();
        mx4_font.trn(900, 510, 0);
        mx4_font.scl(scale);
        mx4_font.rotate(new Vector3(0, 0, 1), 30);
        Batch batch = stage.getBatch();

        batch.setTransformMatrix(mx4_font);
        batch.begin();
        game.font_splash.draw(batch, final_splash, -layout.width / 2, layout.height / 2);
        batch.end();
        batch.setTransformMatrix(old_matrix);
    }

    @Override
    public void render(float dt) {
        ScreenUtils.clear(game.theme.colors[12]);
        render_splash(dt);
        fps_timer += dt;
        if (fps_timer > 0.5f) {
            fps_timer = 0;
            fps.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
            fps.setPosition(1272, 4, Align.bottomRight);
        }
        stage.act(dt);
        stage.draw();
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
        LabelStyle style_version = new LabelStyle(game.font_version, game.theme.colors[15]);
        LabelStyle style_fps = new LabelStyle(game.font_fps, game.theme.colors[15]);
        style_button.over = over;
        style_button.checkedOver = over;

        old_matrix = stage.getBatch().getTransformMatrix().cpy();

        // create UI elements
        btn_play = new TextButton("Новая игра", style_button);
        btn_settings = new TextButton("Настройки", style_inactive);
        btn_exit = new TextButton("Выход", style_button);
        btn_load_game = new TextButton("Загрузить игру", style_inactive);
        btn_tutorial = new TextButton("Как играть", style_inactive);

        title = new Label("Домино", style_title);
        edition = new Label("Java Edition", style_edition);
        version = new Label(Dominoes.GAME_VERSION, style_version);
        fps = new Label("FPS: " + Gdx.graphics.getFramesPerSecond(), style_fps);

        // set positions
        title.setPosition(640, 720, Align.top);
        edition.setPosition(640, 510, Align.top);
        version.setPosition(8, 4, Align.bottomLeft);

        btn_play.setSize(320, 55);
        btn_load_game.setSize(320, 55);
        btn_settings.setSize(320, 55);
        btn_exit.setSize(155, 55);
        btn_tutorial.setSize(155, 55);

        btn_load_game.setDisabled(true);
        btn_settings.setDisabled(true);
        btn_tutorial.setDisabled(true);

        btn_play.setPosition(480, 365, Align.topLeft);
        btn_load_game.setPosition(480, 300, Align.topLeft);
        btn_settings.setPosition(480, 235, Align.topLeft);
        btn_exit.setPosition(645, 170, Align.topLeft);
        btn_tutorial.setPosition(480, 170, Align.topLeft);


        // event listeners
        btn_play.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.changeScreen(Dominoes.GAMEMODE_SELECT);
            }
        });
        btn_exit.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Thanks for playing!");
                System.exit(0);
            }
        });

        // add them to the stage
        stage.addActor(title);
        stage.addActor(edition);
        stage.addActor(version);
        stage.addActor(fps);
        stage.addActor(btn_play);
        stage.addActor(btn_load_game);
        stage.addActor(btn_settings);
        stage.addActor(btn_exit);
        stage.addActor(btn_tutorial);
    }

    @Override
    public void resize(int width, int height) {} // might implement later

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
