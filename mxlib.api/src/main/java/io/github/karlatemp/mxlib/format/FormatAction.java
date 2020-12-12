package io.github.karlatemp.mxlib.format;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class FormatAction implements FormatTemplate {
    public static class Const extends FormatAction {
        private final CharSequence charSequence;
        private final int start;
        private final int end;

        public Const(CharSequence charSequence, int start, int end) {
            this.charSequence = charSequence;
            this.start = start;
            this.end = end;
        }

        public Const(CharSequence charSequence) {
            this(charSequence, 0, charSequence.length());
        }

        @Override
        public void formatTo(@NotNull StringBuilder buffer, @NotNull FormatArguments arguments) {
            buffer.append(charSequence, start, end);
        }
    }

    public static class Argument extends FormatAction {
        private final int key;

        public Argument(int key) {
            this.key = key;
        }

        @Override
        public void formatTo(@NotNull StringBuilder buffer, @NotNull FormatArguments arguments) {
            if (arguments.containsKey(key)) {
                arguments.append(buffer, key);
            } else {
                buffer.append('{').append(key).append('}');
            }
        }
    }

    public static class Link extends FormatAction {
        private final Iterable<FormatTemplate> templates;

        public Link(Iterable<FormatTemplate> templates) {
            this.templates = templates;
        }

        public static Builder builder() {
            return new Builder();
        }

        @Override
        public void formatTo(@NotNull StringBuilder buffer, @NotNull FormatArguments arguments) {
            for (FormatTemplate template : templates) {
                template.formatTo(buffer, arguments);
            }
        }

        public static class Builder implements Iterable<FormatTemplate> {
            private final List<FormatTemplate> list;

            @NotNull
            @Override
            public Iterator<FormatTemplate> iterator() {
                return list.iterator();
            }

            public Builder(@NotNull List<FormatTemplate> list) {
                this.list = list;
            }

            public Builder() {
                this(new ArrayList<>());
            }

            public Builder append(@NotNull FormatTemplate template) {
                list.add(template);
                return this;
            }

            public Builder string(CharSequence charSequence) {
                return append(new FormatAction.Const(charSequence));
            }

            public Builder string(CharSequence charSequence, int start, int end) {
                return append(new FormatAction.Const(charSequence, start, end));
            }

            public Builder arg(int key) {
                return append(new FormatAction.Argument(key));
            }

            public Builder link(@NotNull Iterable<FormatTemplate> sub) {
                return append(new FormatAction.Link(sub));
            }

            public FormatAction.Link build() {
                return new FormatAction.Link(list);
            }
        }
    }
}
