package com.github.idkp.cmdsys.syntax;

import com.github.idkp.cmdsys.Sender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public final class PerFileSenderParser implements SenderParser {
    private static final String LINE_COMMENT = "//";

    @Override
    public List<Sender> parse(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return Collections.emptyList();
        }

        Iterator<Path> senderFileIter = Files.list(dir).iterator();
        List<Sender> allSenders = new ArrayList<>();

        while (senderFileIter.hasNext()) {
            Path senderFile = senderFileIter.next();

            allSenders.add(parseSingle(senderFile));
        }

        return allSenders;
    }

    private static Sender parseSingle(Path file) throws IOException {
        Set<String> permissions = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(LINE_COMMENT)) {
                    permissions.add(line);
                }
            }
        }

        return new Sender(file.getFileName().toString(), permissions);
    }
}
