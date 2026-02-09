package com.org.example.model;

import com.org.example.utils.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class DuckTask extends Task {
    private Assignment assignment;

    public DuckTask(String id, String descriere, Assignment assignment) {
        super(id, descriere);
        this.assignment = assignment;
    }

    @Override
    public void execute() {
        double t = assignment.getTime();
        String output = String.format(
                "Duck %d on lane %d: t=%.3f s%nTask executed at: %s%n",
                assignment.getDuck().getId(),
                assignment.getLane().getId(),
                t,
                LocalDateTime.now().format(Constants.FORMATTER)
        );

        try (PrintWriter writer = new PrintWriter(new FileWriter("natatie.out", true))) {
            writer.print(output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "DuckTask{" + assignment + '}';
    }
}
