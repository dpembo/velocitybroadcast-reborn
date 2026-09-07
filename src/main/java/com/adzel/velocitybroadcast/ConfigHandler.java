package com.adzel.velocitybroadcast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;

public class ConfigHandler {
    private final Path configPath;
    private final Logger logger;

    private boolean debugEnabled = false;
    private boolean versionCheckEnabled = true;
    private String prefix = "&9&l[&3&lServer&9&l]&r ";

    private static final String CURRENT_VERSION = VelocityBroadcast.PLUGIN_VERSION;
    private static final String VERSION_LINE = "# DO NOT EDIT\nPlugin Version: '" + CURRENT_VERSION + "' # Do not edit this value, as it will mess up version checking and break the plugin";

    public ConfigHandler(Path configPath, Logger logger) {
        this.configPath = configPath;
        this.logger = logger;
    }

    /**
     * Strips a trailing "# comment" from a config line, but only when the
     * '#' appears outside of any quoted value. This prevents hex colour
     * codes (e.g. MiniMessage gradients like <gradient:#1E90FF:#00FF7F>)
     * from being truncated as if they were comments.
     */
    private static String stripComment(String line) {
        boolean inSingle = false;
        boolean inDouble = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\'' && !inDouble) {
                inSingle = !inSingle;
            } else if (c == '"' && !inSingle) {
                inDouble = !inDouble;
            } else if (c == '#' && !inSingle && !inDouble) {
                return line.substring(0, i);
            }
        }
        return line;
    }

    public void load() {
        try {
            Files.createDirectories(configPath.getParent());

            boolean shouldSave = false;

            if (!Files.exists(configPath)) {
                save();
                return;
            }

            List<String> lines = Files.readAllLines(configPath);
            String fileVersion = null;

            for (String line : lines) {
                if (line.trim().startsWith("Plugin Version:")) {
                    fileVersion = line.replaceAll(".*'(.*?)'.*", "$1").trim();
                    break;
                }
            }

            if (fileVersion == null || !fileVersion.equals(CURRENT_VERSION)) {
                shouldSave = true;
            }

            try (BufferedReader reader = Files.newBufferedReader(configPath)) {
                Map<String, String> configMap = reader.lines()
                    .filter(line -> line.contains(":") && !line.trim().startsWith("#"))
                    .map(line -> stripComment(line).split(":", 2))
                    .collect(Collectors.toMap(
                        a -> a[0].trim(),
                        a -> a[1].trim().replaceAll("^['\"]|['\"]$", ""),
                        (a, b) -> b,
                        LinkedHashMap::new
                    ));

                debugEnabled = Boolean.parseBoolean(configMap.getOrDefault("debug-messages-enabled", "false"));
                versionCheckEnabled = Boolean.parseBoolean(configMap.getOrDefault("version-check-enabled", "true"));
                prefix = configMap.getOrDefault("prefix", "&9&l[&3&lServer&9&l]&r ");
            }

            if (shouldSave) {
                save(); // Update config with new version and preserve user values
            }

        } catch (IOException e) {
            logger.error("Failed to load VelocityBroadcast config!", e);
        }
    }

    public void save() {
        try {
            Files.createDirectories(configPath.getParent());

            String editableSection = "";
            if (Files.exists(configPath)) {
                editableSection = Files.readAllLines(configPath).stream()
                    .dropWhile(line -> !line.trim().equalsIgnoreCase("# ONLY EDIT BELOW THIS LINE"))
                    .skip(1)
                    .collect(Collectors.joining("\n"));
            }

            try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
                writer.write(VERSION_LINE + "\n\n");
                writer.write("# ONLY EDIT BELOW THIS LINE\n");

                if (!editableSection.isEmpty()) {
                    writer.write(editableSection + "\n");
                } else {
                    writer.write("debug-messages-enabled: false # Enables/disables debug messages (Default: false)\n");
                    writer.write("version-check-enabled: true # Toggles version update messages for admins (Default: true)\n");
                    writer.write("prefix: '&9&l[&3&lServer&9&l]&r ' # The prefix for broadcasts and messages\n");
                }
            }
        } catch (IOException e) {
            logger.error("Failed to save VelocityBroadcast config!", e);
        }
    }

    public void reload() {
        load();
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public boolean isVersionCheckEnabled() {
        return versionCheckEnabled;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String newPrefix) {
        this.prefix = newPrefix;
        save();
    }
}
