package net.thewinnt.dominoes;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

import net.thewinnt.dominoes.screen.*;
import net.thewinnt.dominoes.ui.*;

public class Dominoes extends Game {
	private MainMenuScreen mainMenuScreen;
	private GamemodeSelectScreen gamemodeSelectScreen;
	private LocalGameScreen localGameScreen;

	private FreeTypeFontGenerator gen_dhm;
	private FreeTypeFontGenerator gen_bahn;
	private FreeTypeFontGenerator gen_comic;

	public BitmapFont font_title;
	public BitmapFont font_edition;
	public BitmapFont font_button;
	public BitmapFont font_disabled;
	public BitmapFont font_splash;

	public static final short MAIN_MENU = 0;
	public static final short GAMEMODE_SELECT = 1;
	public static final short LOCAL_GAME = 2;

	private Color[] theme_light = {
		Utils.rgbColor(0, 0, 0),
		Utils.rgbColor(0, 0, 0),
		Utils.rgbColor(242, 242, 242),
		Utils.rgbColor(224, 0, 0),
		Utils.rgbColor(255, 96, 0),
		Utils.rgbColor(255, 192, 0),
		Utils.rgbColor(0, 224, 0),
		Utils.rgbColor(0, 192, 255),
		Utils.rgbColor(0, 0, 224),
		Utils.rgbColor(128, 0, 255),
		Utils.rgbColor(255, 0, 255),
		Utils.rgbColor(0, 0, 0),
		Utils.rgbColor(3, 209, 255),
		Utils.rgbColor(0, 156, 191),
		Utils.rgbColor(1, 175, 216),
		Utils.rgbColor(0, 0, 0),
		Utils.rgbColor(45, 45, 45),
		Utils.rgbColor(240, 255, 0),
		Utils.rgbColor(0, 200, 0),
		Utils.rgbColor(50, 224, 225),
		Utils.rgbColor(0, 0, 255, 192),
		Utils.rgbColor(112, 0, 0),
		Utils.rgbColor(128, 48, 0),
		Utils.rgbColor(128, 96, 0),
		Utils.rgbColor(0, 112, 0),
		Utils.rgbColor(0, 96, 128),
		Utils.rgbColor(0, 0, 112),
		Utils.rgbColor(64, 0, 128),
		Utils.rgbColor(128, 0, 128),
		Utils.rgbColor(0, 0, 0)
	};

	private Theme light_theme = new Theme(theme_light, "TheWinNT", "Light", "The default light theme", 2);
	public Theme theme = light_theme;
	public String[] splashes = {
		"Java recreation!",
		"Less lag!",
		"My brain is gonna explode...",
		"Uses LibGDX!",
		"Uses LWJGL!",
		"HD!",
		"What a nice window!",
		"Indie!",
		"From Russia, with love <3",
		"Also try Minecraft!",
		"Endless possibilities!",
		"bruh",
		"Made during coronavirus pandemic!",
		"Beta!",
		"public class Domino {",
		"100% free!",
		"No one paid me for this!",
		"Quite stable!",
		"Made by a 14-year-old!",
		"Thanks, Google!",
		"Thanks, StackOverflow!",
		"I'm not a pro programmer btw",
		"&&&&&&!",
		"Getting advanced!",
		"We need more splashes!",
		"Can I put you in a bucket?",
		"thinn fonts",
		"As simple as possible!",
		"I'm lazy",
		"Contains some bugs!",
		"Tested!",
		"Offline!",
		"Instructions unclear, lost a friend",
		"Friends not included",
		"Doesn't require a friend!",
		"Split up!",
		"Almost abandoned!",
		"A bit messy!",
		"Still looks like spaghetti",
		"This code is awful!",
		"A giant bodge",
		"Can be played at parties!",
		"Look, it's pulsating!",
		"Supports gaming mouses!",
		"Overly complicated",
		"Optimized!",
		"Made by a 14-year-old!",
		"Look mom, no one helped me!",
		"69420",
		"Customizable!",
		"Multilingual!",
		"lang['translatable_splash']",
		"Totally yours!",
		"Whatever you prefer!",
		"Not exactly super advanced",
		"A bit less laggy",
		"#NoWar",
		"A peaceful game",
		"\uF400" // special: can be translated
	};

	public String font_chars = "";

	public final String splash = splashes[new Random().nextInt(splashes.length)];

	public void changeScreen(int new_screen) {
		switch (new_screen) {
			case MAIN_MENU:
				if (mainMenuScreen == null) mainMenuScreen = new MainMenuScreen(this);
				this.setScreen(mainMenuScreen);
				break;
			case GAMEMODE_SELECT:
				if (gamemodeSelectScreen == null) gamemodeSelectScreen = new GamemodeSelectScreen(this);
				this.setScreen(gamemodeSelectScreen);
				break;
			case LOCAL_GAME:
				if (localGameScreen == null) localGameScreen = new LocalGameScreen(this);
				this.setScreen(localGameScreen);
				break;
			
		}
	}

	@Override
	public void create() {
		// font definition
		gen_dhm = new FreeTypeFontGenerator(Gdx.files.internal("denhome.otf"));
		gen_bahn = new FreeTypeFontGenerator(Gdx.files.internal("bahnschrift.ttf"));
		gen_comic = new FreeTypeFontGenerator(Gdx.files.internal("comic.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		for (int i = 0x20; i < 0x7B; i++) font_chars += (char) i;
		for (int i = 0x401; i < 0x452; i++) font_chars += (char) i;
		parameter.characters = font_chars;
		parameter.size = 300;
		parameter.color = theme.colors[15];
		font_title = gen_dhm.generateFont(parameter);

		parameter.size = 30;
		font_edition = gen_bahn.generateFont(parameter);

		parameter.size = 60;
		font_button = gen_dhm.generateFont(parameter);

		parameter.color = theme.colors[16];
		font_disabled = gen_dhm.generateFont(parameter);

		parameter.size = 40;
		parameter.color = theme.colors[17];
		font_splash = gen_comic.generateFont(parameter);

		// set the screen
		changeScreen(0);
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		font_title.dispose();
		font_edition.dispose();
		font_button.dispose();
		gen_dhm.dispose();
		gen_bahn.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}
}
