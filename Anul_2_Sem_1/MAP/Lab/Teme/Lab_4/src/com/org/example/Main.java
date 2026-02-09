package com.org.example;

import com.org.example.factory.RaceFactory;
import com.org.example.factory.Strategy;
import com.org.example.model.*;
import com.org.example.runner.AbstractTaskRunner;
import com.org.example.runner.DelayTaskRunner;
import com.org.example.runner.PrinterTaskRunner;
import com.org.example.runner.StrategyTaskRunner;
import com.org.example.strategy.BinarySearchStrategy;
import com.org.example.strategy.OptimisationStrategy;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("natatie.in"));
        int N = sc.nextInt();
        int M = sc.nextInt();

        // Citim vitezele
        double[] speeds = new double[N];
        for (int i = 0; i < N; i++) {
            speeds[i] = sc.nextDouble();
        }

        // Citim rezistentele
        double[] endurance = new double[N];
        for (int i = 0; i < N; i++) {
            endurance[i] = sc.nextDouble();
        }

        // Citim distantele
        double[] distances = new double[M];
        for (int i = 0; i < M; i++) {
            distances[i] = sc.nextDouble();
        }

        sc.close();

        // Construim ratele
        Duck[] ducks = new Duck[N];
        for (int i = 0; i < N; i++) {
            ducks[i] = new Duck(i + 1, speeds[i], endurance[i]);
        }

        // Construim culoarele
        Lane[] lanes = new Lane[M];
        for (int i = 0; i < M; i++) {
            lanes[i] = new Lane(i + 1, distances[i]);
        }

        // Alegem strategia
        OptimisationStrategy strategy = new BinarySearchStrategy();

        // Cream cursa
        Race race = RaceFactory.createRace(ducks, lanes, strategy);
        race.computeAssignments();

        // Cream TaskRunner in stil LIFO (de la cea mai rezistenta la cea mai putin rezistenta)
        StrategyTaskRunner runner = new StrategyTaskRunner(Strategy.LIFO);
        AbstractTaskRunner printerRunner = new DelayTaskRunner(new PrinterTaskRunner(runner));

        // adaugam cate un task pt fiecare rata
        int i = 1;
        for (Assignment a : race.getAssignments()) {
            DuckTask task = new DuckTask(String.valueOf(i), "Swim lane " + a.getLane().getId(), a);
            printerRunner.addTask(task);
            i++;
        }

        // Scriem rezultatul in fisier
        try (PrintWriter pw = new PrintWriter("natatie.out")) {
            pw.println("Best time: " + String.format("%.3f s", race.getTotalDuration()));
            for (Assignment a : race.getAssignments()) {
                pw.printf("Lane %d (d=%.0f) <- Duck %d (v=%.2f, st=%.0f)%n",
                        a.getLane().getId(),
                        a.getLane().getDistance(),
                        a.getDuck().getId(),
                        a.getDuck().getSpeed(),
                        a.getDuck().getEndurance());
            }
            pw.println("\n");
        }

        // Afisam executia fiecarei rate (LIFO)
        printerRunner.executeAll();
    }
}