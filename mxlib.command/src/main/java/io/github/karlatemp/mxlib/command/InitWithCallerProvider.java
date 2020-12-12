package io.github.karlatemp.mxlib.command;

import io.github.karlatemp.caller.StackFrame;

public interface InitWithCallerProvider extends CommandProvider {
    void setup(StackFrame frame);
}
