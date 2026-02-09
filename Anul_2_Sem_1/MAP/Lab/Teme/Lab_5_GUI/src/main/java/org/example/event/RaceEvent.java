package org.example.event;

import org.example.model.*;

import java.util.Comparator;
import java.util.List;

/**
 * Clasa care reprezinta un eveniment de tip cursa
 */
public class RaceEvent extends Event {
    private final Long id;
    private final List<SwimmingDuck> ducks;
    private final List<Lane> lanes;

    /**
     * Constructor pentru clasa RaceEvent
     * @param ducks List<SwimmingDuck>, lista de rate participante la cursa
     * @param lanes List<Lane>, lista de piste disponibile pentru cursa
     */
    public RaceEvent(Long id, List<SwimmingDuck> ducks, List<Lane> lanes) {
        this.id = id;
        this.ducks = ducks;
        this.lanes = lanes;
    }

    /**
     * Getter pentru id-ul evenimentului de cursa
     * @return Long, id-ul evenimentului de cursa
     */
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
     * Metoda pentru inceperea cursei
     */
    public void startRace() {
        List<SwimmingDuck> sortableDucks = this.ducks.stream()
                .filter(java.util.Objects::nonNull)
                .collect(java.util.stream.Collectors.toList());

        if (sortableDucks.isEmpty()) {
            notifySubscribers("Race event aborted: No valid ducks available.");
            return;
        }

        notifySubscribers("Race event started!");

        sortableDucks.sort(Comparator.comparingDouble(SwimmingDuck::getRezistenta).reversed());

        int M = Math.min(sortableDucks.size(), lanes.size());
        List<SwimmingDuck> selected = sortableDucks.subList(0, M);

        double maxTime = 0.0;

        for (int i = 0; i < M; ++i) {
            SwimmingDuck duck = selected.get(i);
            double dist = lanes.get(i).getDistance();

            double time = 2.0 * dist / duck.getViteza();
            maxTime = Math.max(maxTime, time);

            notifySubscribers(duck.getUsername() + " swims lane " + lanes.get(i).getId() +
                    " (time: " + String.format("%.2f", time) + ")");
        }

        notifySubscribers("Race event ended! Max time: " + String.format("%.2f", maxTime));
    }

    /**
     * Suprascrierea metodei toString pentru afisarea evenimentului de cursa
     * @return String, reprezentarea evenimentului de cursa ca String
     */
    @Override
    public String toString() {
        return "RaceEvent{" +
                "id=" + id +
                ", ducks=" + ducks +
                ", lanes=" + lanes +
                '}';
    }
}
