/*
 * Copyright (C) 2016-2023 the original author or authors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package space.npstr.wolfia.domain.setup.lastactive;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import java.util.Optional;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.stereotype.Component;
import space.npstr.wolfia.domain.setup.GameSetupService;
import space.npstr.wolfia.system.redis.Redis;

/**
 * This component listens the keys expiring from {@link ActivityService} and takes action
 * <p>
 * Since we could lose the redis connection, or restart, or <insert any other reason to miss expiry events>,
 * a "manual" check when starting the game should still be performed.
 */
@Component
public class AutoOuter extends RedisPubSubAdapter<String, String> {

    private static final String EXPIRE_CHANNEL = "__keyevent@*__:expired";

    private final RedisKeyParser redisKeyParser = new RedisKeyParser();

    private final GameSetupService gameSetupService;
    private final ShardManager shardManager;

    public AutoOuter(Redis redis, GameSetupService gameSetupService, ShardManager shardManager) {
        this.gameSetupService = gameSetupService;
        this.shardManager = shardManager;

        var pubSub = redis.getPubSub();
        pubSub.addListener(this);
        pubSub.sync().psubscribe(EXPIRE_CHANNEL);
    }

    @Override
    public void message(String channel, String message) {
        if (EXPIRE_CHANNEL.equals(channel)) {
            this.expired(message);
        }
    }

    @Override
    public void message(String pattern, String channel, String message) {
        if (EXPIRE_CHANNEL.equals(pattern) || EXPIRE_CHANNEL.equals(channel)) {
            this.expired(message);
        }
    }

    private void expired(String key) {
        Optional<Long> parsed = redisKeyParser.fromKey(key);
        if (parsed.isEmpty()) {
            return;
        }
        outUser(parsed.get());
    }

    private void outUser(long userId) {
        this.gameSetupService.outUserDueToInactivity(userId, this.shardManager);
    }
}
