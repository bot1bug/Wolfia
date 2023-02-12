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

package space.npstr.wolfia.webapi;

import java.util.List;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/shards")
public class ShardEndpoint {

    private final ShardManager shardManager;

    public ShardEndpoint(ShardManager shardManager) {
        this.shardManager = shardManager;
    }

    @GetMapping
    public List<Shard> getShards() {
        return this.shardManager.getShards().stream()
                .map(jda -> new Shard(jda.getShardInfo().getShardId(),
                        jda.getStatus().name())
                )
                .collect(Collectors.toList());
    }
}
