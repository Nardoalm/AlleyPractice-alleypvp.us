package us.alleypvp.practice.feature.title.model;

import us.alleypvp.practice.feature.kit.Kit;
import us.alleypvp.practice.feature.division.Division;
import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 21/04/2025
 */
@Getter
public class TitleRecord {
    private final Kit kit;
    private final String prefix;
    private final Division requiredDivision;

    /**
     * Constructor for the TitleRecord class.
     *
     * @param kit              The kit associated with the title.
     * @param prefix           The prefix of the title.
     * @param requiredDivision The required division for the title.
     */
    public TitleRecord(Kit kit, String prefix, Division requiredDivision) {
        this.kit = kit;
        this.prefix = prefix;
        this.requiredDivision = requiredDivision;
    }
}