package name.remal.gradle_plugins.generate_sources.generators;

interface DelegatingCharSequence extends CharSequence {

    @SuppressWarnings({"Since15", "java:S1161"})
    default boolean isEmpty() {
        return length() == 0;
    }

    @Override
    default int length() {
        return toString().length();
    }

    @Override
    default char charAt(int index) {
        return toString().charAt(index);
    }

    @Override
    default CharSequence subSequence(int start, int end) {
        return toString().subSequence(start, end);
    }

}
