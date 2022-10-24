package com.volcengine.tos.internal.taskman;

import java.util.concurrent.Callable;

abstract class TosTask {
    public Callable<TaskOutput<?>> getCallableTask() {
        return null;
    }
}
