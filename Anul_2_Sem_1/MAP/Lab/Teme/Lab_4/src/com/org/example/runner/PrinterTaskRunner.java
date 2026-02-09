package com.org.example.runner;

import com.org.example.utils.Constants;

import java.time.LocalDateTime;

public class PrinterTaskRunner extends AbstractTaskRunner {
    public PrinterTaskRunner(TaskRunner runner) {
        super(runner);
    }

    @Override
    public void executeOneTask() {
        runner.executeOneTask();
        System.out.println("A task was executed at: " + LocalDateTime.now().format(Constants.FORMATTER));
    }
}
