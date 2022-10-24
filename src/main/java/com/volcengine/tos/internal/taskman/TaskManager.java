package com.volcengine.tos.internal.taskman;

import java.util.List;

interface TaskManager {
    void dispatch(TosTask tasks);
    void handle();
    List<TaskOutput<?>> get();
    void suspend();
    void shutdown();
}
