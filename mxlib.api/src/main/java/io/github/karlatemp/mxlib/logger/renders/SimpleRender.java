package io.github.karlatemp.mxlib.logger.renders;

import io.github.karlatemp.mxlib.logger.MMarket;
import io.github.karlatemp.mxlib.logger.MessageFactory;
import io.github.karlatemp.mxlib.logger.MessageRender;
import io.github.karlatemp.mxlib.utils.StringBuilderFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class SimpleRender implements MessageRender {
    private final MessageFactory factory;
    private static final SimpleFormatter SIMPLE_FORMATTER = new SimpleFormatter();

    public SimpleRender(MessageFactory factory) {
        this.factory = factory;
    }

    @Override
    public @NotNull StringBuilder render(
            @Nullable String loggerName, @Nullable MMarket market,
            @Nullable StringBuilderFormattable message,
            boolean isError,
            @Nullable Level level,
            @Nullable LogRecord record
    ) {
        StringBuilder builder = new StringBuilder(255);
        if (message != null) {
            if (message instanceof StringBuilderFormattable.Link) {
                StringBuilderFormattable.Link link = (StringBuilderFormattable.Link) message;
                List<Object> objects = link.getList();
                if (!objects.isEmpty()) {
                    Object last = objects.get(objects.size() - 1);
                    if (last instanceof Throwable) {
                        if (objects.size() == 1) {
                            objects.set(0, factory.dump((Throwable) last, true));
                        } else {
                            objects.set(objects.size() - 1, StringBuilderFormattable.LN);
                        }
                    }
                }
            }
            message.formatTo(builder);
        }
        if (record != null) {
            if (message != null) builder.append('\n');
            Throwable thr = record.getThrown();
            record.setThrown(null);
            builder.append(SIMPLE_FORMATTER.formatMessage(record));
            record.setThrown(thr);
            if (thr != null) {
                builder.append('\n');
                factory.dump(thr, true).formatTo(builder);
            }
        }
        return builder;
    }
}
