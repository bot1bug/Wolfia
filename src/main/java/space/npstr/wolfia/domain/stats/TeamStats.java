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

package space.npstr.wolfia.domain.stats;

import java.util.Collection;
import java.util.Set;
import space.npstr.wolfia.game.definitions.Alignments;

/**
 * Model of a team in a game.
 */
public class TeamStats {

    private final long teamId;
    private final Alignments alignment;
    private final String name;
    private final boolean isWinner;
    private final int teamSize;
    private final Set<PlayerStats> players;

    TeamStats(long teamId, Alignments alignment, boolean isWinner, String name, int teamSize, Collection<PlayerStats> players) {
        this.teamId = teamId;
        this.alignment = alignment;
        this.isWinner = isWinner;
        this.name = name;
        this.teamSize = teamSize;
        this.players = Set.copyOf(players);
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.teamId);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TeamStats t)) {
            return false;
        }
        return this.teamId == t.teamId;
    }


    public long getTeamId() {
        return this.teamId;
    }

    public Set<PlayerStats> getPlayers() {
        return this.players;
    }

    public Alignments getAlignment() {
        return this.alignment;
    }

    public String getName() {
        return this.name;
    }

    public boolean isWinner() {
        return this.isWinner;
    }

    public int getTeamSize() {
        return this.teamSize;
    }
}
