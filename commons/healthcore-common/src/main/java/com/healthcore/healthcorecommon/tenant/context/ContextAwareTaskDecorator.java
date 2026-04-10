package com.healthcore.healthcorecommon.tenant.context;

import org.jspecify.annotations.NullMarked;
import org.springframework.core.task.TaskDecorator;

public class ContextAwareTaskDecorator implements TaskDecorator {

    @Override
    @NullMarked
    public Runnable decorate(Runnable runnable) {
        // Capture the context from the parent thread.
        // We use .orElse(null) because if context is missing,
        // we simply don't want to set anything in the child thread.
        RequestContext.Context parentContext = RequestContext.get().orElse(null);

        return () -> {
            try {
                if (parentContext != null) {
                    RequestContext.set(parentContext);
                }
                runnable.run();
            } finally {
                RequestContext.clear();
            }
        };
    }
}