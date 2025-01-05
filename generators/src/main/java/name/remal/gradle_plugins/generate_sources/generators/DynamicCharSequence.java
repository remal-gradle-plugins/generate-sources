package name.remal.gradle_plugins.generate_sources.generators;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DynamicCharSequence implements DelegatingCharSequence {

    private final Supplier<@NotNull CharSequence> supplier;

    @Override
    public String toString() {
        return supplier.get().toString();
    }

}
