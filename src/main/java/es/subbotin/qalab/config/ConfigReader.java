package es.subbotin.qalab.config;

import java.io.InputStream;
import java.util.Properties;

/**
 * Reads configuration from config.properties.
 * BASE_URL environment variable overrides base.url (used in CI).
 */
public class ConfigReader {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {
            PROPS.load(in);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load config.properties", e);
        }
    }

    /**
     * Returns base URL — from env variable in CI, from properties locally.
     *
     * @return base URL string
     */
    public static String getBaseUrl() {
        return System.getenv("BASE_URL") != null
                ? System.getenv("BASE_URL")
                : PROPS.getProperty("base.url");
    }

    /**
     * Returns explicit wait timeout in seconds.
     *
     * @return wait timeout as int
     */
    public static int getExplicitWait() {
        return Integer.parseInt(PROPS.getProperty("explicit.wait", "10"));
    }

    /**
     * Returns true if headless mode is enabled.
     *
     * @return headless flag
     */
    public static boolean isHeadless() {
        return Boolean.parseBoolean(PROPS.getProperty("headless", "true"));
    }
}