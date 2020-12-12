package io.github.karlatemp.mxlib.format.common;

import io.github.karlatemp.mxlib.format.FormatAction;
import io.github.karlatemp.mxlib.format.FormatTemplate;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleFormatter extends ReadAsStringFormatter {
    @SuppressWarnings("RegExpRedundantEscape")
    private static final Pattern DEFAULT = Pattern.compile(
            "\\{([0-9]+)\\}"
    ), UIX_PT = Pattern.compile("\\$\\{([0-9]+)\\}");
    public static final SimpleFormatter INSTANCE = new SimpleFormatter();
    private final Pattern pattern;
    private final int group;

    public static @NotNull SimpleFormatter newDefault() {
        return new SimpleFormatter();
    }

    public static @NotNull SimpleFormatter newUnixStyle() {
        return new SimpleFormatter(UIX_PT, 1);
    }

    public SimpleFormatter() {
        this(DEFAULT, 1);
    }

    public SimpleFormatter(@NotNull Pattern pattern, int group) {
        this.pattern = pattern;
        this.group = group;
    }

    @Override
    public @NotNull FormatTemplate parse(@NotNull String template) {
        Matcher matcher = pattern.matcher(template);
        FormatAction.Link.Builder builder = FormatAction.Link.builder();
        int pos = 0;
        while (matcher.find()) {
            int value;
            try {
                value = Integer.parseInt(matcher.group(this.group));
            } catch (NumberFormatException ignored) {
                continue;
            }
            int ppos = matcher.start();
            if (ppos > pos) {
                builder.string(template, pos, ppos);
            }
            pos = matcher.end();
            builder.arg(value);
        }
        if (pos < template.length()) {
            builder.string(template, pos, template.length());
        }
        return builder.build();
    }
}
