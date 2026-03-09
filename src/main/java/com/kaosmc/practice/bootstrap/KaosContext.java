package com.kaosmc.practice.bootstrap;

import com.kaosmc.practice.bootstrap.annotation.Service;
import com.kaosmc.practice.KaosPractice;
import com.kaosmc.practice.common.logger.Logger;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;
import lombok.Getter;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Remi
 * @project kaos-practice
 * @date 2/07/2025
 */
@Getter
public final class KaosContext {
    private static final String SERVICE_IMPL_PACKAGE = "com.kaosmc.practice";

    private final KaosPractice plugin;
    private final Map<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>, com.kaosmc.practice.bootstrap.lifecycle.Service> serviceInstances = new ConcurrentHashMap<>();
    private final Map<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>, Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>> serviceRegistry = new HashMap<>();
    private final Set<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>> servicesBeingConstructed = new HashSet<>();

    private ScanResult scanResult;
    private List<com.kaosmc.practice.bootstrap.lifecycle.Service> sortedServices = Collections.emptyList();

    public KaosContext(KaosPractice plugin) {
        this.plugin = Objects.requireNonNull(plugin, "A instância do plugin não pode ser nula");
    }

    /**
     * Discovers, instantiates, and initializes all services.
     */
    public void initialize() throws Exception {
        Logger.logPhaseStart("Service Initialization");

        try {
            this.scanResult = new ClassGraph().enableAllInfo().acceptPackages(SERVICE_IMPL_PACKAGE).scan();
        } catch (Exception e) {
            throw new IllegalStateException("A varredura do classpath falhou.", e);
        }

        List<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>> implementationClasses = discoverServices(this.scanResult);
        if (implementationClasses.isEmpty()) {
            throw new IllegalStateException("Nenhum serviço encontrado para carregar.");
        }

        for (Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service> implClass : implementationClasses) {
            serviceRegistry.put(getProvidedInterface(implClass), implClass);
        }

        List<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>> sortedImplClasses = sortServicesByPriority(implementationClasses);
        Logger.info("Ordem de Inicialização dos Serviços: " +
                sortedImplClasses.stream().map(Class::getSimpleName).collect(Collectors.joining(" -> ")));

        for (Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service> implClass : sortedImplClasses) {
            instantiateService(implClass);
        }

        this.sortedServices = sortedImplClasses.stream()
                .map(impl -> serviceInstances.get(getProvidedInterface(impl)))
                .collect(Collectors.toList());

        Logger.logPhaseStart("Service Setup Phase");
        for (com.kaosmc.practice.bootstrap.lifecycle.Service service : sortedServices) {
            Logger.logTime(service.getClass().getSimpleName() + " Setup", () -> service.setup(this));
        }

        Logger.logPhaseStart("Service Initialization Phase");
        for (com.kaosmc.practice.bootstrap.lifecycle.Service service : sortedServices) {
            Logger.logTime(service.getClass().getSimpleName() + " Initialization", () -> service.initialize(this));
        }

        Logger.logPhaseComplete("Service Initialization");
    }

    /**
     * Shuts down all managed services in reverse order.
     */
    public void shutdown() {
        Logger.info("--- Início do Encerramento dos Serviços ---");
        if (sortedServices == null) return;

        List<com.kaosmc.practice.bootstrap.lifecycle.Service> reversedServices = new ArrayList<>(sortedServices);
        Collections.reverse(reversedServices);

        for (com.kaosmc.practice.bootstrap.lifecycle.Service service : reversedServices) {
            try {
                service.shutdown(this);
            } catch (Exception e) {
                Logger.logException("Falha ao encerrar o serviço " + service.getClass().getSimpleName(), e);
            }
        }
        Logger.info("--- Encerramento dos Serviços Concluído ---");
    }

    /**
     * Retrieves a managed service by its interface.
     * @param serviceInterface The interface of the service to get.
     * @return An Optional containing the service instance if found.
     */
    public <T extends com.kaosmc.practice.bootstrap.lifecycle.Service> Optional<T> getService(Class<T> serviceInterface) {
        return Optional.ofNullable(serviceInterface.cast(serviceInstances.get(serviceInterface)));
    }

    @SuppressWarnings("unchecked")
    private void instantiateService(Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service> implClass) throws Exception {
        Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service> providedInterface = getProvidedInterface(implClass);
        if (serviceInstances.containsKey(providedInterface)) {
            return;
        }

        if (servicesBeingConstructed.contains(implClass)) {
            throw new IllegalStateException("Dependência cíclica detectada! O ciclo envolve: " + implClass.getName());
        }
        servicesBeingConstructed.add(implClass);

        Constructor<?> constructor = Arrays.stream(implClass.getConstructors())
                .max(Comparator.comparingInt(Constructor::getParameterCount))
                .orElseThrow(() -> new NoSuchMethodException("Nenhum construtor adequado encontrado para " + implClass.getSimpleName()));

        List<Object> dependencies = new ArrayList<>();
        for (Class<?> paramType : constructor.getParameterTypes()) {
            if (KaosPractice.class.isAssignableFrom(paramType)) {
                dependencies.add(this.plugin);
                continue;
            }
            if (com.kaosmc.practice.bootstrap.lifecycle.Service.class.isAssignableFrom(paramType)) {
                Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service> dependencyInterface = (Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>) paramType;
                Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service> dependencyImpl = this.serviceRegistry.get(dependencyInterface);
                if (dependencyImpl == null) {
                    throw new ClassNotFoundException("Nenhuma implementação encontrada para a interface de serviço: " + dependencyInterface.getName());
                }

                instantiateService(dependencyImpl);
                dependencies.add(serviceInstances.get(dependencyInterface));
            } else {
                throw new IllegalStateException("Tipo de dependência não suportado em " + implClass.getSimpleName() + ": " + paramType.getSimpleName());
            }
        }

        com.kaosmc.practice.bootstrap.lifecycle.Service serviceInstance = (com.kaosmc.practice.bootstrap.lifecycle.Service) constructor.newInstance(dependencies.toArray());
        serviceInstances.put(providedInterface, serviceInstance);

        servicesBeingConstructed.remove(implClass);
    }

    @SuppressWarnings("unchecked")
    private List<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>> discoverServices(ScanResult scanResult) {
        List<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>> services = new ArrayList<>();
        for (ClassInfo classInfo : scanResult.getClassesWithAnnotation(Service.class.getName())) {
            if (!classInfo.isAbstract() && !classInfo.isInterface()) {
                services.add((Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>) classInfo.loadClass());
            }
        }
        return services;
    }

    private List<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>> sortServicesByPriority(List<Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service>> services) {
        return services.stream()
                .sorted(Comparator.comparingInt(c -> c.getAnnotation(Service.class).priority()))
                .collect(Collectors.toList());
    }

    private Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service> getProvidedInterface(Class<? extends com.kaosmc.practice.bootstrap.lifecycle.Service> implClass) {
        return implClass.getAnnotation(Service.class).provides();
    }
}
