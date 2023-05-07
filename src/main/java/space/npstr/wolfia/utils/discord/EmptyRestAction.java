/*
 * Copyright 2015-2019 Austin Keener, Michael Ritter, Florian Spieß, and the JDA contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package space.npstr.wolfia.utils.discord;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;

/**
 * Copied over from JDA when it was removed: https://github.com/DV8FromTheWorld/JDA/commit/5a27dc3f0a6e19a5b464c52806862a0f755ab9b5
 */
public class EmptyRestAction<T> implements AuditableRestAction<T> {
    private final JDA api;
    private final T returnObj;

    public EmptyRestAction(JDA api, T returnObj) {
        this.api = api;
        this.returnObj = returnObj;
    }

    @Override
    public JDA getJDA() {
        return api;
    }

    @Override
    public AuditableRestAction<T> reason(String reason) {
        return this;
    }

    @Override
    public AuditableRestAction<T> setCheck(BooleanSupplier checks) {
        return this;
    }

    @Override
    public AuditableRestAction<T> timeout(long timeout, TimeUnit unit) {
        return this; //Noop, this cannot timeout as it does not request in the first place.
    }

    @Override
    public AuditableRestAction<T> deadline(long timestamp) {
        return this; //Noop, this cannot timeout as it does not request in the first place.
    }

    @Override
    public void queue(Consumer<? super T> success, Consumer<? super Throwable> failure) {
        if (success != null)
            success.accept(returnObj);
    }

    @Override
    public CompletableFuture<T> submit(boolean shouldQueue) {
        return CompletableFuture.completedFuture(returnObj);
    }

    @Override
    public T complete(boolean shouldQueue) {
        return returnObj;
    }
}
