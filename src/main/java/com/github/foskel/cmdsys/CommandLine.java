package com.github.foskel.cmdsys;

import com.github.foskel.cmdsys.execution.CommandExecutor;
import com.github.foskel.cmdsys.execution.DefaultCommandExecutor;
import com.github.foskel.cmdsys.execution.ExecutionResult;
import com.github.foskel.cmdsys.io.CommandLogger;
import com.github.foskel.cmdsys.syntax.ArgumentMapper;
import com.github.foskel.cmdsys.syntax.CommandParser;
import com.github.foskel.cmdsys.syntax.ParsedCommand;
import com.github.foskel.cmdsys.syntax.SyntaxScheme;
import com.github.foskel.cmdsys.syntax.parameter.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class CommandLine {
    private final Map<String, Sender> senders;

    private final CommandRegistry registry;
    private final CommandParser parser;
    private final CommandExecutor executor;
    private final ArgumentMapper argumentMapper;
    private final CommandLogger logger;

    public CommandLine(CommandRegistry registry, CommandExecutor executor, ArgumentMapper argumentMapper, CommandLogger logger) {
        this.argumentMapper = argumentMapper;
        this.logger = logger;
        this.senders = new HashMap<>();
        this.registry = registry;
        this.parser = new CommandParser();
        this.executor = executor;
    }

    public void addSender(Sender sender) {
        this.senders.put(sender.getId(), sender);
    }

    public void addSenders(Collection<Sender> senders) {
        for (Sender sender : senders) {
            this.addSender(sender);
        }
    }

    public void removeSender(Sender sender) {
        this.senders.remove(sender.getId());
    }

    public Sender getSender(String id) {
        return this.senders.get(id);
    }

    public void addCommand(Object command) {
        this.registry.register(command);
    }

    public void removeCommand(Object command) {
        this.registry.unregister(command);
    }

    public ParsedCommand parseCommand(Sender sender, String input) {
        return this.parser.parse(sender, this.registry, input);
    }

    public ExecutionResult runCommand(SyntaxScheme syntaxScheme) {
        return this.executor.execute(syntaxScheme.getCommand(), syntaxScheme.getSelectedBranch(), syntaxScheme.getParameters());
    }

    //TODO
    public void runCommand(Sender sender, String input, BiConsumer<ParsedCommand, ExecutionResult> consumer) {
        ParsedCommand parsed = parseCommand(sender, input);
        String error = parsed.getErrorMessage();

        if (error != null) {
            consumer.accept(parsed, new ExecutionResult(error));
        } else {
            consumer.accept(parsed, runCommand(parsed.getSyntaxScheme()));
        }
    }

    public void clearCommands() {
        this.registry.clear();
    }

    public ArgumentMapper getArgumentMapper() {
        return this.argumentMapper;
    }

    public static Builder builder() {
        return new Builder();
    }

    public CommandLogger getLogger() {
        return logger;
    }

    public static class Builder {
        private static final ParameterParser[] DEFAULT_PARAMETER_PARSERS = {
                new CharSequenceParameterParser(),
                NumberParameterParser.INSTANCE,
                new BooleanParameterParser(),
                new EnumParameterParser()
        };

        private CommandRegistry registry;
        private CommandExecutor executor;
        private ArgumentMapper argumentMapper = new ArgumentMapper();
        private CommandLogger logger;

        public Builder withRegistry(CommandRegistry registry) {
            this.registry = registry;

            return this;
        }

        public Builder withExecutor(CommandExecutor executor) {
            this.executor = executor;

            return this;
        }

        public <T> Builder addArgumentMapping(Class<? extends T> type, Supplier<T> instanceSupplier) {
            this.argumentMapper.registerMapping(type, instanceSupplier);

            return this;
        }

        public <T> Builder addArgumentMapping(Class<? extends T> type, String id, Supplier<T> instanceSupplier) {
            this.argumentMapper.registerMapping(type, id, instanceSupplier);

            return this;
        }

        public Builder withLogger(CommandLogger logger) {
            this.logger = logger;

            return this;
        }

        public CommandLine build() {
            for (ParameterParser defaultParser : DEFAULT_PARAMETER_PARSERS) {
                this.registry.registerParser(defaultParser, false);
            }

            this.registry.registerParser(new ArrayParameterParser(this.registry), true);

            if (this.executor == null) {
                this.executor = new DefaultCommandExecutor(this.registry.getParameterParsers(), this.argumentMapper);
            }

            return new CommandLine(this.registry, this.executor, this.argumentMapper, this.logger);
        }
    }
}
