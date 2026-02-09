package org.example.event;

import org.example.model.*;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Clasa care reprezinta un eveniment de tip cursa
 */
public class RaceEvent extends Event {
    private final Long id;
    private final List<SwimmingDuck> ducks;
    private final List<Lane> lanes;
    private final ExecutorService executorService;

    /**
     * Constructor pentru clasa RaceEvent
     * @param id Long, id-ul evenimentului
     * @param ducks List<SwimmingDuck>, lista de rate participante la cursa
     * @param lanes List<Lane>, lista de piste disponibile pentru cursa
     */
    public RaceEvent(Long id, List<SwimmingDuck> ducks, List<Lane> lanes) {
        this.id = id;
        this.ducks = ducks;
        this.lanes = lanes;
        this.executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );
    }

    /**
     * Constructor cu ExecutorService personalizat
     */
    public RaceEvent(Long id, List<SwimmingDuck> ducks, List<Lane> lanes, ExecutorService executorService) {
        this.id = id;
        this.ducks = ducks;
        this.lanes = lanes;
        this.executorService = executorService;
    }

    /**
     * Getter pentru id-ul evenimentului
     * @return Long, id-ul evenimentului
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Getter pentru lista de rate participante la cursa
     * @return List<SwimmingDuck>, lista de rate participante la cursa
     */
    public List<SwimmingDuck> getDucks() {
        return ducks;
    }

    /**
     * Getter pentru lista de piste disponibile pentru cursa
     * @return List<Lane>, lista de piste disponibile pentru cursa
     */
    public List<Lane> getLanes() {
        return lanes;
    }

    /**
     * Metoda pentru notificarea asincrona a tuturor abonatilor
     * @param message String, mesajul de notificare
     * @return CompletableFuture<Void>
     */
    public CompletableFuture<Void> notifySubscribersAsync(String message) {
        return CompletableFuture.runAsync(() -> {
            for (User user : subscribers) {
                if (user != null) {
                    user.receiveMessage(message);
                }
            }
        }, executorService);
    }

    /**
     * Metoda pentru abonarea asincrona a unui utilizator
     * @param user User, utilizatorul care se aboneaza
     * @return CompletableFuture<Boolean>
     */
    public CompletableFuture<Boolean> subscribeAsync(User user) {
        return CompletableFuture.supplyAsync(() -> {
            if (!subscribers.contains(user)) {
                subscribers.add(user);
                return true;
            }
            return false;
        }, executorService);
    }

    /**
     * Metoda pentru dezabonarea asincrona a unui utilizator
     * @param user User, utilizatorul care se dezaboneaza
     * @return CompletableFuture<Boolean>
     */
    public CompletableFuture<Boolean> unsubscribeAsync(User user) {
        return CompletableFuture.supplyAsync(() -> {
            return subscribers.remove(user);
        }, executorService);
    }

    /**
     * Metoda pentru inceperea cursei in mod asincron
     * @return CompletableFuture<String> cu rezultatul final al cursei
     */
    public CompletableFuture<String> startRaceAsync() {
        return CompletableFuture.supplyAsync(() -> {
                    List<SwimmingDuck> sortableDucks = ducks.stream()
                            .filter(duck -> duck != null)
                            .sorted(Comparator.comparingDouble(SwimmingDuck::getRezistenta).reversed())
                            .collect(Collectors.toList());

                    if (sortableDucks.isEmpty()) {
                        return "Race event aborted: No valid ducks available.";
                    }

                    return "Race preparation complete";
                }, executorService)
                .thenCompose(prepMessage -> {
                    String header = "\n" + "=".repeat(60) + "\n" +
                            "🏁 RACE EVENT #" + this.getId() + " STARTING 🏁\n" +
                            "=".repeat(60);
                    return notifySubscribersAsync(header).thenApply(v -> prepMessage);
                })
                .thenCompose(prepMessage -> {
                    return processRaceAsync();
                })
                .thenCompose(maxTime -> {
                    String resultText = "RACE EVENT ENDED! MAX TIME: " + String.format("%.2f", maxTime);
                    String footer = "\n" + "=".repeat(60) + "\n" +
                            "🏆 " + resultText + "\n" +
                            "=".repeat(60);
                    return notifySubscribersAsync(footer).thenApply(v -> resultText);
                })
                .exceptionally(ex -> {
                    String errorMsg = "Race failed: " + ex.getMessage();
                    notifySubscribersAsync(errorMsg);
                    return errorMsg;
                });
    }

    /**
     * Proceseaza cursa in mod asincron, fiecare rata intr-un task separat
     * @return CompletableFuture<Double> cu timpul maxim
     */
    private CompletableFuture<Double> processRaceAsync() {
        List<SwimmingDuck> sortableDucks = ducks.stream()
                .filter(duck -> duck != null)
                .sorted(Comparator.comparingDouble(SwimmingDuck::getRezistenta).reversed())
                .collect(Collectors.toList());

        int M = Math.min(sortableDucks.size(), lanes.size());
        List<SwimmingDuck> selected = sortableDucks.subList(0, M);

        List<CompletableFuture<Double>> raceFutures = selected.stream()
                .limit(M)
                .map(duck -> {
                    int index = selected.indexOf(duck);
                    Lane lane = lanes.get(index);

                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            Thread.sleep(500 + (long)(Math.random() * 1000));

                            double dist = lane.getDistance();
                            double time = 2.0 * dist / duck.getViteza();

                            String message = duck.getUsername() + " swims lane " + lane.getId() +
                                    " (time: " + String.format("%.2f", time) + ")";

                            notifySubscribersAsync(message);

                            Thread.sleep(2000);

                            return time;
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return 0.0;
                        }
                    }, executorService);
                })
                .collect(Collectors.toList());

        return CompletableFuture.allOf(raceFutures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    return raceFutures.stream()
                            .map(CompletableFuture::join)
                            .max(Double::compare)
                            .orElse(0.0);
                });
    }

    /**
     * Metoda sincrona pentru backward compatibility
     */
    public void startRace() {
        try {
            String result = startRaceAsync().get();
            System.out.println(result);
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("Race execution failed: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Metoda pentru inchiderea executorService-ului
     */
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Suprascrierea metodei toString pentru clasa RaceEvent
     * @return String, reprezentarea evenimentului de tip cursa ca String
     */
    @Override
    public String toString() {
        return "RaceEvent{" +
                "id=" + id +
                ", ducks=" + ducks.size() +
                ", lanes=" + lanes.size() +
                ", subscribers=" + subscribers.size() +
                '}';
    }
}
