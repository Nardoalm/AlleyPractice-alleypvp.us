package com.kaosmc.practice.visual.nametag;

import lombok.Getter;

import java.util.Objects;

/**
 * @author Remi
 * @project kaos-practice
 * @date 27/06/2025
 */
@Getter
public final class NametagView {
    private final int sortWeight;
    private final String prefix;
    private final String suffix;
    private final NametagVisibility visibility;
    private final String adapterKey;

    public NametagView(String prefix, String suffix) {
        this(prefix, suffix, NametagVisibility.ALWAYS, 9999, "");
    }

    public NametagView(String prefix, String suffix, NametagVisibility visibility) {
        this(prefix, suffix, visibility, 9999, "");
    }

    public NametagView(String prefix, String suffix, NametagVisibility visibility, int sortWeight) {
        this(prefix, suffix, visibility, sortWeight, "");
    }

    public NametagView(String prefix, String suffix, NametagVisibility visibility, int sortWeight, String adapterKey) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.visibility = visibility;
        this.sortWeight = Math.max(0, sortWeight);
        this.adapterKey = adapterKey != null ? adapterKey : "";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NametagView that = (NametagView) o;
        return sortWeight == that.sortWeight
                && Objects.equals(prefix, that.prefix)
                && Objects.equals(suffix, that.suffix)
                && Objects.equals(adapterKey, that.adapterKey)
                && visibility == that.visibility;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sortWeight, prefix, suffix, visibility, adapterKey);
    }
}
