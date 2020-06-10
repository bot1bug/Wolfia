/*
 * Copyright (C) 2016-2020 Dennis Neufeld
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

import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;

/**
 * User that is doing the web request
 */
@Immutable
@Value.Style(
        stagedBuilder = true,
        strictBuilder = true
)
public interface UserData {

    /**
     * discord id of the user
     */
    long id();

    /**
     * Oauth2 access token
     */
    String accessToken();
}
