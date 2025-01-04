package name.remal.gradle_plugins.generate_sources.generators.content;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class DynamicCharSequence implements CharSequence {

    private final Supplier<@NotNull CharSequence> supplier;

    @Override
    public String toString() {
        return supplier.get().toString();
    }

    @Override
    public int length() {
        return supplier.get().length();
    }

    @Override
    public char charAt(int index) {
        return supplier.get().charAt(index);
    }

    @Nonnull
    @Override
    public CharSequence subSequence(int start, int end) {
        return supplier.get().subSequence(start, end);
    }

}
