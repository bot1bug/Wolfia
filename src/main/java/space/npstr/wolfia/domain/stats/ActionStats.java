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

import org.springframework.lang.Nullable;
import space.npstr.wolfia.game.definitions.Actions;
import space.npstr.wolfia.game.definitions.Phase;

/**
 * Describe an action that happened during a game
 */
public class ActionStats {

    private final long actionId;
    //chronological order of the actions
    //order is a reserved keyword in postgres, so we use 'sequence' in the table instead
    private final int order;
    // the difference between these two timestamps is the following: an action may be submitted before it actually
    // happens (example: nk gets submitted during the night, but actually "happens" when the day starts and results are
    // announced). these two timestamps try to capture that data as accurately as possible
    private final long timeStampSubmitted;
    private final long timeStampHappened;
    //n0, d1 + n1, d2 + n2 etc
    private final int cycle;
    private final Phase phase;
    //userId of the discord user; there might be special negative values for factional actors/targets in the future
    private final long actor;
    private final Actions actionType;
    //userId of the discord user
    private final long target;
    //save any additional info of an action in here
    @Nullable
    private final String additionalInfo;

    ActionStats(long actionId, String actionType, long actor, int cycle, int order, long target, long happened,
                long submitted, String phase, @Nullable String additionalInfo) {

        this.actionId = actionId;
        this.actionType = Actions.valueOf(actionType);
        this.actor = actor;
        this.cycle = cycle;
        this.order = order;
        this.target = target;
        this.timeStampHappened = happened;
        this.timeStampSubmitted = submitted;
        this.phase = Phase.valueOf(phase);
        this.additionalInfo = additionalInfo;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(actionId);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ActionStats a)) {
            return false;
        }
        return this.actionId == a.actionId;
    }

    public long getActionId() {
        return this.actionId;
    }

    public int getOrder() {
        return this.order;
    }

    public long getTimeStampSubmitted() {
        return this.timeStampSubmitted;
    }

    public long getTimeStampHappened() {
        return this.timeStampHappened;
    }

    public int getCycle() {
        return this.cycle;
    }

    public Phase getPhase() {
        return this.phase;
    }

    public long getActor() {
        return this.actor;
    }

    public Actions getActionType() {
        return this.actionType;
    }

    public long getTarget() {
        return this.target;
    }

    @Nullable
    public String getAdditionalInfo() {
        return this.additionalInfo;
    }

}
