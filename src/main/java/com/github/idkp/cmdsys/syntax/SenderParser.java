package com.github.idkp.cmdsys.syntax;

import com.github.idkp.cmdsys.Sender;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface SenderParser {
    List<Sender> parse(Path dir) throws IOException;
}
