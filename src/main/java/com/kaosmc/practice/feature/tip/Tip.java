package com.kaosmc.practice.feature.tip;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.core.locale.LocaleService;
import com.kaosmc.practice.core.locale.internal.impl.message.GlobalMessagesLocaleImpl;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Emmy
 * @project Kaos
 * @since 25/04/2025
 */
@Getter
public class Tip {
    private final List<String> tips = new ArrayList<>();

    public Tip() {
        LocaleService localeService = KaosPractice.getInstance().getService(LocaleService.class);
        List<String> loadedTips = localeService.getStringListRaw(GlobalMessagesLocaleImpl.TIPS_LIST);
        this.tips.addAll(loadedTips);
    }

    /**
     * Returns a random tip from the list of tips.
     *
     * @return A random tip as a String.
     */
    public String getRandomTip() {
        if (this.tips.isEmpty()) {
            return "&cA lista de dicas está vazia no momento. Então fica a dica: &eA prática leva à perfeição!";
        }

        return this.tips.get(ThreadLocalRandom.current().nextInt(this.tips.size()));
    }
}
