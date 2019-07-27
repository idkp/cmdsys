package com.github.foskel.cmdsys.syntax;

import com.github.foskel.cmdsys.Sender;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class CommandSenderParser {
    private static final String LINE_COMMENT = "//";

    public List<Sender> parseAll(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) {
            return Collections.emptyList();
        }

        Iterator<Path> senderFileIter = Files.list(dir).iterator();// we need to redirect the exceptions, so fuck streams
        List<Sender> allSenders = new ArrayList<>();

        while (senderFileIter.hasNext()) {
            Path senderFile = senderFileIter.next();

            allSenders.add(this.parse(senderFile));
        }

        return allSenders;
    }

    public Sender parse(Path path) throws IOException {
        Set<String> permissions = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(LINE_COMMENT)) {
                    permissions.add(line);
                }
            }
        }

        return new Sender(path.getFileName().toString(), permissions);
    }
}
