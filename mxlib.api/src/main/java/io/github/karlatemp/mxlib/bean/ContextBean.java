package io.github.karlatemp.mxlib.bean;

import org.jetbrains.annotations.NotNull;

public interface ContextBean {
    /**
     * @return return the class of return should be same with this
     */
    @NotNull ContextBean copy(@NotNull IBeanManager newBeanManager);
}
