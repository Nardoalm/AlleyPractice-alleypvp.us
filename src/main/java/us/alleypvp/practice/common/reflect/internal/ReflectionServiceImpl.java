package us.alleypvp.practice.common.reflect.internal;

import us.alleypvp.practice.common.constants.PluginConstant;
import us.alleypvp.practice.bootstrap.KaosContext;
import us.alleypvp.practice.bootstrap.annotation.Service;
import us.alleypvp.practice.common.logger.Logger;
import us.alleypvp.practice.common.reflect.Reflection;
import us.alleypvp.practice.common.reflect.ReflectionService;
import lombok.Getter;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Emmy
 * @project Alley
 * @since 03/04/2025
 */
@Getter
@Service(provides = ReflectionService.class, priority = 330)
public class ReflectionServiceImpl implements ReflectionService {
    private final PluginConstant pluginConstant;

    private final List<Reflection> reflectionServices = new ArrayList<>();

    /**
     * Constructor for DI.
     */
    public ReflectionServiceImpl(PluginConstant pluginConstant) {
        this.pluginConstant = pluginConstant;
    }

    @Override
    public void initialize(KaosContext context) {
        this.registerReflectionServices();
    }

    private void registerReflectionServices() {
        Reflections reflections = this.pluginConstant.getReflections();
        if (reflections == null) {
            Logger.error("ReflectionServiceImpl cannot initialize: Reflections object is null.");
            return;
        }

        for (Class<? extends Reflection> clazz : reflections.getSubTypesOf(Reflection.class)) {
            if (clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                continue;
            }

            try {
                Constructor<? extends Reflection> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                Reflection instance = constructor.newInstance();
                this.reflectionServices.add(instance);
            } catch (Exception exception) {
                Logger.logException("Falha ao registrar o servico de reflection: " + clazz.getName(), exception);
            }
        }
    }

    @Override
    public <T extends Reflection> T getReflectionService(Class<T> serviceClass) {
        return this.reflectionServices.stream()
                .filter(serviceClass::isInstance)
                .map(serviceClass::cast)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Reflection> getReflectionServices() {
        return Collections.unmodifiableList(this.reflectionServices);
    }
}
