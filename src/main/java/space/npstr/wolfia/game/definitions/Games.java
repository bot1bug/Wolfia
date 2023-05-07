/*
 * Copyright (C) 2016-2020 the original author or authors
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

package space.npstr.wolfia.game.definitions;

import java.util.HashMap;
import java.util.Map;
import space.npstr.wolfia.game.Game;
import space.npstr.wolfia.game.GameInfo;
import space.npstr.wolfia.game.mafia.Mafia;
import space.npstr.wolfia.game.mafia.MafiaInfo;
import space.npstr.wolfia.game.popcorn.Popcorn;
import space.npstr.wolfia.game.popcorn.PopcornInfo;

/**
 * All games supported by the bot
 */
public enum Games {
    POPCORN(Popcorn.class, "Popcorn"),
    MAFIA(Mafia.class, "Mafia");

    public final Class<? extends Game> clazz;
    public final String textRep;

    Games(Class<? extends Game> clazz, String textRepresentation) {
        this.clazz = clazz;
        this.textRep = textRepresentation;
    }

    private static final Map<Class<? extends Game>, GameInfo> GAME_INFOS = new HashMap<>();

    static {
        GAME_INFOS.put(POPCORN.clazz, new PopcornInfo());
        GAME_INFOS.put(MAFIA.clazz, new MafiaInfo());
    }

    public static GameInfo getInfo(Games game) {
        return GAME_INFOS.get(game.clazz);
    }

    public static GameInfo getInfo(Class<? extends Game> gameClass) {
        return GAME_INFOS.get(gameClass);
    }

    public static GameInfo getInfo(Game game) {
        return GAME_INFOS.get(game.getClass());
    }

}
