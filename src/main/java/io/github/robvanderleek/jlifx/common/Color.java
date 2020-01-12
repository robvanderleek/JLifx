package io.github.robvanderleek.jlifx.common;

public class Color {
    public static final Color WHITE = new Color(255, 255, 255);
    public static final Color LIGHT_GRAY = new Color(192, 192, 192);
    public static final Color GRAY = new Color(128, 128, 128);
    public static final Color DARK_GRAY = new Color(64, 64, 64);
    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color RED = new Color(255, 0, 0);
    public static final Color PINK = new Color(255, 175, 175);
    public static final Color ORANGE = new Color(255, 200, 0);
    public static final Color YELLOW = new Color(255, 255, 0);
    public static final Color GREEN = new Color(0, 255, 0);
    public static final Color MAGENTA = new Color(255, 0, 255);
    public static final Color CYAN = new Color(0, 255, 255);
    public static final Color BLUE = new Color(0, 0, 255);
    private int value;

    public Color(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public Color(int r, int g, int b, int a) {
        value = ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | ((b & 0xFF));
    }

    public Color(float r, float g, float b) {
        this((int) (r * 255 + 0.5), (int) (g * 255 + 0.5), (int) (b * 255 + 0.5));
    }

    public int getRed() {
        return (value >> 16) & 0xFF;
    }

    public int getGreen() {
        return (value >> 8) & 0xFF;
    }

    public int getBlue() {
        return value & 0xFF;
    }

    public int hashCode() {
        return value;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Color)) {
            return false;
        }
        Color that = (Color) obj;
        return that.value == this.value;
    }

    public static Color decode(String nm) throws NumberFormatException {
        int i = Integer.decode(nm);
        return new Color((i >> 16) & 0xFF, (i >> 8) & 0xFF, i & 0xFF);
    }

    public static float[] rgbToHsb(int r, int g, int b) {
        float hue, sat, bright;
        int min = Math.min(Math.min(r, g), b);
        int max = Math.max(Math.max(r, g), b);
        bright = ((float) max) / 255.0f;
        if (max != 0) {
            sat = ((float) (max - min)) / ((float) max);
        } else {
            sat = 0;
        }
        if (sat == 0) {
            hue = 0;
        } else {
            float delta = (float) (max - min);
            float red = ((float) (max - r)) / delta;
            float green = ((float) (max - g)) / delta;
            float blue = ((float) (max - b)) / delta;
            if (r == max) {
                hue = blue - green;
            } else if (g == max) {
                hue = 2.0f + red - blue;
            } else {
                hue = 4.0f + green - red;
            }
            hue = hue / 6.0f;
            if (hue < 0)
                hue = hue + 1.0f;
        }
        return new float[]{hue, sat, bright};
    }

}