package org.example;

import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordingFile;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Usage: jfr-event-parser <file.jfr>");
        }

        File file = new File(args[0]);
        try (RecordingFile recordingFile = new RecordingFile(file.toPath())) {
            while(recordingFile.hasMoreEvents()) {
                RecordedEvent event = recordingFile.readEvent();
                String eventName = event.getEventType().getName();
                if ("datadog.trace.bootstrap.InitEvent".equals(eventName)) {
                    double duration = (double) event.getLong("duration") / 1_000_000;
                    String module = event.getString("module");

                    String msg = String.format("module: %s, duration: %.3f ms", module, duration);
                    System.out.println(msg);
                }

            }
        }
    }
}