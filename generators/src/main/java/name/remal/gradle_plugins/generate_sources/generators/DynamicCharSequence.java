package name.remal.gradle_plugins.generate_sources.generators;

import java.util.function.Supplier;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DynamicCharSequence implements DelegatingCharSequence {

    private final Supplier<CharSequence> supplier;

    @Override
    public String toString() {
        return supplier.get().toString();
    }

}
