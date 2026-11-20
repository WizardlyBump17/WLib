package com.wizardlybump17.wlib.command.suggestion.object;

import com.wizardlybump17.wlib.command.suggestion.AbstractValuesSuggester;
import com.wizardlybump17.wlib.command.suggestion.Suggester;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public interface UUIDSuggester extends Suggester<UUID> {

    static @NotNull Values value(@NotNull UUID value) {
        return new Values(List.of(value));
    }
    
    static @NotNull Values values(@NotNull List<UUID> values) {
        return new Values(values);
    }

    static @NotNull Values values(UUID... values) {
        return new Values(List.of(values));
    }
    
    final class Values extends AbstractValuesSuggester<UUID> implements UUIDSuggester {
        
        Values(@NotNull List<UUID> values) {
            super(values);
        }
        
        @Override
        public String toString() {
            return "UUIDSuggester$Values{" +
                    "values=" + values() +
                    '}';
        }
    }
}
