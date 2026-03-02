package com.kaosmc.practice.feature.title;

import com.kaosmc.practice.feature.kit.Kit;
import com.kaosmc.practice.bootstrap.lifecycle.Service;
import com.kaosmc.practice.feature.title.model.TitleRecord;

import java.util.Map;

/**
 * @author Remi
 * @project alley-practice
 * @date 2/07/2025
 */
public interface TitleService extends Service {
    /**
     * Gets the map of all loaded titles.
     * @return A map where the key is the Kit and the value is the TitleRecord.
     */
    Map<Kit, TitleRecord> getTitles();
}
