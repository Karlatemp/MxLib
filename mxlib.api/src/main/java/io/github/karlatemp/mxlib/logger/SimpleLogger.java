package io.github.karlatemp.mxlib.logger;

public abstract class SimpleLogger extends AwesomeLogger {
    private final String name;

    public SimpleLogger(String name, MessageRender render) {
        super(render);
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
