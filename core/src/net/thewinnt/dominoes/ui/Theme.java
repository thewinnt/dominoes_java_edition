package net.thewinnt.dominoes.ui;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;

/** A theme for Dominoes: Java Edition. Completely compatible with PE themes. */
public class Theme {
    public Color[] colors;
    public String author;
    public String name;
    public String desc;
    public int version;

    public Theme() {
        colors = new Color[31];
        author = null;
        name = null;
        desc = null;
        version = -1;
    }

    public Theme(Color[] colors) {
        this();
        this.colors = colors;
    }

    public Theme(Color[] colors, String author, String name, String desc, int version) {
        this.colors = colors;
        this.author = author;
        this.name = name;
        this.desc = desc;
        this.version = version;
    }

    /**
     * Updates a theme from an older version.
     * @param theme The theme to update
     * @return The updated version of the Theme
     */
    public Theme updateTheme(Theme theme) {
        return theme; // TODO updating
    }

    /**
     * Loads a theme from a Python-compatible file
     * @param code A string containing the theme
     * @return A new Theme from the given data
     */
    public static Theme fromPython(String code) {
        ArrayList<String> elements = new ArrayList<String>(35);
        Color[] colors = new Color[31];
        String author = "";
        String name = "";
        String desc = "";
        int version = 2;
        int element_id = 0;
        String element = "";
        int status = 0; // 0 - start, 1 - element reading, 2 - waiting for element
        int r, b, g;
        code = code.trim();
        for (int i = 0; i < code.length(); i++) {
            if (code.charAt(i) == '[' || code.charAt(i) == '(' || code.charAt(i) == '"' && status != 1) {
                switch (status) {
                    case 0:
                        status = 2;
                        break;
                    case 2:
                        status = 1;
                        break;
                }
            } else if (code.charAt(i) == ']' || code.charAt(i) == ')' || code.charAt(i) == '"' && status == 1) {
                element_id++;
                elements.add(element);
            } else {
                element += code.charAt(i);
            }
        }
        int next_color = 0;
        for (String item : elements) {
            if (item.matches("[\\[\\(]\\d+, \\d+, \\d+[\\]\\)]")) {
                String[] results = item.split("[\\d+]");
                r = Integer.parseInt(results[0]);
                g = Integer.parseInt(results[0]);
                b = Integer.parseInt(results[0]);
                colors[next_color] = new Color(r / 255, g / 255, b / 255, 1);
                next_color++;
            } else if (item.matches("['\"][^'\"]+['\"]")) {
                switch (element_id) {
                    case 0:
                        author = item.substring(1, item.length() - 1);
                    case 1:
                        name = item.substring(1, item.length() - 1);
                    case 2:
                        desc = item.substring(1, item.length() - 1);
                    case 3:
                        version = Integer.parseInt(item);
                }
            }
        }
        return new Theme(colors, author, name, desc, version);
    }
}
