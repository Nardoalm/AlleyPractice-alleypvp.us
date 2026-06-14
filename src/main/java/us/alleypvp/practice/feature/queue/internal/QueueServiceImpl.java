package us.alleypvp.practice.feature.queue.internal;

import us.alleypvp.practice.AlleyPractice;
import us.alleypvp.practice.feature.queue.Queue;
import us.alleypvp.practice.feature.queue.QueueService;
import us.alleypvp.practice.library.menu.Menu;
import us.alleypvp.practice.feature.kit.KitService;
import us.alleypvp.practice.feature.kit.setting.types.mode.KitSettingRanked;
import us.alleypvp.practice.feature.queue.menu.QueuesMenuModern;
import us.alleypvp.practice.core.config.ConfigService;
import us.alleypvp.practice.bootstrap.KaosContext;
import us.alleypvp.practice.bootstrap.annotation.Service;
import us.alleypvp.practice.core.profile.ProfileService;
import us.alleypvp.practice.core.profile.Profile;
import us.alleypvp.practice.core.profile.enums.ProfileState;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Remi
 * @project Alley
 * @date 5/21/2024
 */
@Getter
@Service(provides = QueueService.class, priority = 90)
public class QueueServiceImpl implements QueueService {
    private final ConfigService configService;
    private final KitService kitService;
    private final ProfileService profileService;

    private final List<Queue> queues = new ArrayList<>();
    private Menu queueMenu;
    private QueueTask queueTask;

    public QueueServiceImpl(ConfigService configService, KitService kitService, ProfileService profileService) {
        this.configService = configService;
        this.kitService = kitService;
        this.profileService = profileService;
    }

    @Override
    public void initialize(KaosContext context) {
        this.queueMenu = new QueuesMenuModern();
        this.reloadQueues();

        this.queueTask = new QueueTask();
        context.getPlugin().getServer().getScheduler().runTaskTimer(AlleyPractice.getInstance(), this.queueTask, 20L, 20L);
    }

    @Override
    public void reloadQueues() {
        this.queues.clear();
        this.kitService.getKits().forEach(kit -> {
            if (!kit.isEnabled()) return;
            this.queues.add(new Queue(kit, false, false));
            this.queues.add(new Queue(kit, false, true));
            if (kit.isSettingEnabled(KitSettingRanked.class)) {
                this.queues.add(new Queue(kit, true, false));
            }
        });
    }

    /**
     * Get the player count of a specific game type
     *
     * @param queue the queue to get the player count of
     * @return the player count of the game type
     */
    public int getPlayerCountOfGameType(String queue) {
        ProfileState stateForQueue = this.getStateForQueue(queue);

        if (stateForQueue == null) {
            return 0;
        }

        return (int) this.profileService.getProfiles().values().stream()
                .filter(profile -> profile.getState().equals(stateForQueue))
                .filter(profile -> this.isMatchForQueue(profile, queue))
                .count();
    }

    /**
     * Get the state of a profile for a specific queue
     *
     * @param queue the queue to get the state for
     * @return the state of the profile for the queue
     */
    private ProfileState getStateForQueue(String queue) {
        switch (queue) {
            case "Unranked":
            case "Ranked":
            case "Duos":
                return ProfileState.PLAYING;
            case "FFA":
                return ProfileState.FFA;
            case "Bots":
                return ProfileState.FIGHTING_BOT;
            default:
                return null;
        }
    }

    /**
     * Check if a profile's match type matches the queue
     *
     * @param profile the profile to check
     * @param queue   the queue to check the profile's match for
     * @return true if the profile's match type matches the queue
     */
    private boolean isMatchForQueue(Profile profile, String queue) {
        if (queue.equals("FFA")) {
            return true;
        } else if (queue.equals("Ranked")) {
            return profile.getMatch().isRanked();
        } else if (queue.equals("Duos")) {
            return true;
        }

        return false;
    }
}