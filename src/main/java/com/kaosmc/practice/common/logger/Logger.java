package com.kaosmc.practice.common.logger;

import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.constants.PluginConstant;
import com.kaosmc.practice.common.text.CC;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Remi
 * @project Kaos
 * @date 5/27/2024
 */
@UtilityClass
public class Logger {
    private final static ConsoleCommandSender consoleSender;
    private static final Map<UUID, Exception> storedExceptions;

    private static final String PHASE_HEADER_PREFIX = "&6&l--- ";
    private static final String PHASE_HEADER_SUFFIX = " ---";
    private static final String TASK_PREFIX_SUCCESS = "&a✔  &f";
    private static final String TASK_PREFIX_FAIL = "&c✖ &f";

    static {
        consoleSender = KaosPractice.getInstance().getServer().getConsoleSender();
        storedExceptions = new HashMap<>();
    }

    /**
     * Log a message to the console.
     *
     * @param message the message to log
     */
    public void info(String message) {
        consoleSender.sendMessage(CC.translate(CC.PREFIX + message));
    }

    /**
     * Log a message to the console without any prefix.
     *
     * @param message the message to log
     */
    public void infoNoPrefix(String message) {
        consoleSender.sendMessage(CC.translate(message));
    }

    /**
     * Log an error to the console.
     *
     * @param message the error message to log
     */
    public void error(String message) {
        consoleSender.sendMessage(CC.translate(CC.ERROR_PREFIX + "&c(ERROR) &8" + message));
    }

    /**
     * Log a warning to the console.
     *
     * @param message the warning message to log
     */
    public void warn(String message) {
        consoleSender.sendMessage(CC.translate(CC.WARNING_PREFIX + "&e(WARNING) &8" + message));
    }

    /**
     * Log an exception to the console.
     *
     * @param message   the info message or the class name
     * @param exception the exception to log
     */
    public static void logException(String message, Exception exception) {
        UUID errorId = UUID.randomUUID();
        storedExceptions.put(errorId, exception);

        Arrays.asList(
                "",
                CC.ERROR_PREFIX + "&c&lEXCEPTION",
                " &f" + message + ": &r" + exception.getMessage(),
                "",
                " &c(Type &4viewerror " + errorId + " &cin console to see details)",
                ""
        ).forEach(line -> consoleSender.sendMessage(CC.translate(line)));
    }

    /**
     * Retrieve and print the full stack trace of a stored exception.
     *
     * @param errorId The UUID of the error.
     */
    @SuppressWarnings("all")
    public static void viewException(UUID errorId) {
        Exception exception = storedExceptions.get(errorId);
        if (exception == null) {
            consoleSender.sendMessage(CC.translate(CC.ERROR_PREFIX + "&cNo exception found with ID: " + errorId));
            return;
        }

        Arrays.asList(
                "",
                CC.MENU_BAR + CC.MENU_BAR + CC.MENU_BAR + CC.MENU_BAR,
                "",
                "&c&lVIEWING ERROR: " + errorId,
                ""
        ).forEach(line -> consoleSender.sendMessage(CC.translate(line)));

        exception.printStackTrace();

        StackTraceElement[] stackTrace = exception.getStackTrace();
        String locationMessage = "&cError occurred at: Unknown location";

        for (StackTraceElement element : stackTrace) {
            if (element.getClassName().startsWith(KaosPractice.getInstance().getService(PluginConstant.class).getPackageDirectory())) {
                locationMessage = "&cError occurred at: " + element.getClassName() + " (Line " + element.getLineNumber() + ")";
                break;
            }
        }

        consoleSender.sendMessage("");
        consoleSender.sendMessage(CC.translate(locationMessage));
        consoleSender.sendMessage("");
        consoleSender.sendMessage(CC.MENU_BAR + CC.MENU_BAR + CC.MENU_BAR + CC.MENU_BAR);
        consoleSender.sendMessage("");

        storedExceptions.remove(errorId);
    }

    /**
     * Log the time it takes to run a task with clear success/failure indication.
     *
     * @param taskName the name of the task to run
     * @param runnable the task to run
     */
    public void logTime(String taskName, Runnable runnable) {
        long start = System.currentTimeMillis();
        boolean success = false;
        try {
            runnable.run();
            success = true;
        } catch (Exception exception) {
            logException("Failed to run the " + taskName + " task", exception);
        } finally {
            long end = System.currentTimeMillis();
            String prefix = success ? TASK_PREFIX_SUCCESS : TASK_PREFIX_FAIL;
            String message = success ? "&fInicializado com sucesso -> &6" : "&cFalha ao inicializar -> &6";
            consoleSender.sendMessage(CC.translate(prefix + message + taskName + " &fem &6" + (end - start) + " ms&f."));
        }
    }
    /**
     * Log the time it takes to run a task.
     *
     * @param runnableTaskName the name of the task to run
     * @param runnable         the task to run
     */
    public void logTimeTask(String runnableTaskName, Runnable runnable) {
        long start = System.currentTimeMillis();
        boolean success = false;
        try {
            runnable.run();
            success = true;
        } catch (Exception exception) {
            logException("Failed to run the " + runnableTaskName + " task", exception);
        } finally {
            long end = System.currentTimeMillis();
            String prefix = success ? TASK_PREFIX_SUCCESS : TASK_PREFIX_FAIL;
            String message = success ? "&fExecutado com sucesso -> &6" : "&cFailed to run &6";
            consoleSender.sendMessage(CC.translate( prefix + message + runnableTaskName + " &fem &6" + (end - start) + " ms&f."));
        }
    }

    /**
     * Measure the runtime of a task and log it to the console with the provided action in its parameter.
     *
     * @param action   the action
     * @param task     the task to measure
     * @param runnable the task to run
     */
    public void logTimeWithAction(String action, String task, Runnable runnable) {
        long start = System.currentTimeMillis();
        boolean success = false;
        try {
            runnable.run();
            success = true;
        } catch (Exception exception) {
            logException("Failed to " + action + " the " + task + " task", exception);
        } finally {
            long runtime = System.currentTimeMillis() - start;
            String prefix = success ? TASK_PREFIX_SUCCESS : TASK_PREFIX_FAIL;
            String message = success ? "&fSucesso " + action + "&f the &6" : "&cFailed to " + action + "&f the &6";
            consoleSender.sendMessage(CC.translate(prefix + message + task + " &fin &6" + runtime + "ms&f."));
        }
    }

    /**
     * Logs the start of a major initialization phase.
     * @param phaseName The name of the phase (e.g., "Service Setup Phase").
     */
    public void logPhaseStart(String phaseName) {
        consoleSender.sendMessage(CC.translate(""));
        consoleSender.sendMessage(CC.translate(PHASE_HEADER_PREFIX + phaseName.toUpperCase() + PHASE_HEADER_SUFFIX));
    }

    /**
     * Logs the completion of a major initialization phase.
     * @param phaseName The name of the phase (e.g., "Service Initialization").
     */
    public void logPhaseComplete(String phaseName) {
        consoleSender.sendMessage(CC.translate(PHASE_HEADER_PREFIX + phaseName.toUpperCase() + " COMPLETE" + PHASE_HEADER_SUFFIX));
        consoleSender.sendMessage(CC.translate(""));
    }

    /**
     * Sends a message to both the command sender and the console log with a standard prefix.
     *
     * @param sender  The command sender to send the message to.
     * @param message The message to send.
     */
    public void sendMessageAndLog(CommandSender sender, String message) {
        sender.sendMessage(CC.translate(CC.PREFIX + message));
        Logger.info(message);
    }
}