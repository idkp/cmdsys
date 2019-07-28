package com.github.foskel.cmdsys.syntax;

import com.github.foskel.cmdsys.Sender;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface SenderParser {
    List<Sender> parse(Path dir) throws IOException;
}
