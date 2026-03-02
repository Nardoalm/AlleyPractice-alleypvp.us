package com.kaosmc.practice.core.locale;

/**
 * @author Emmy
 * @project Kaos
 * @since 09/09/2025
 */
public interface LocaleEntry {
    /**
     * Method to retrieve the configuration name.
     *
     * @return The name of the configuration file.
     */
    String getConfigName();

    /**
     * Method to retrieve the configuration string path.
     *
     * @return The path to the specific string within the configuration file.
     */
    String getConfigPath();

    /**
     * Method to retrieve the default value for this locale entry.
     *
     * @return The default value associated with this locale entry.
     */
    Object getDefaultValue();
}