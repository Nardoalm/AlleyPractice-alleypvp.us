package us.alleypvp.practice.feature.title;

import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.bootstrap.lifecycle.Service;
import us.alleypvp.practice.feature.title.model.TitleRecord;

import java.util.Map;

/**
 * @author Remi
 * @project kaos-practice
 * @date 2/07/2025
 */
public interface TitleService extends Service {
    /**
     * Gets the map of all loaded titles.
     * @return A map where the key is the Kit and the value is the TitleRecord.
     */
    Map<Kit, TitleRecord> getTitles();
}
