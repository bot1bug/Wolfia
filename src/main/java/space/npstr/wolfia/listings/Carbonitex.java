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

package space.npstr.wolfia.listings;

import org.springframework.lang.NonNull;
import net.dv8tion.jda.api.JDA;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;
import space.npstr.wolfia.App;
import space.npstr.wolfia.Launcher;

public class Carbonitex extends Listing {

    //https://www.carbonitex.net/
    //api docs: https://www.carbonitex.net/discord/data/botdata.php?key=MAH_KEY
    public Carbonitex(@NonNull final OkHttpClient httpClient) {
        super("carbonitex.net", httpClient);
    }

    @NonNull
    @Override
    protected String createPayload(@NonNull final JDA jda) {
        return new JSONObject()
                .put("key", Launcher.getBotContext().getListingsConfig().getCarbonitexKey())
                .put("servercount", Launcher.getBotContext().getShardManager().getGuildCache().size())
                .toString();
    }

    @NonNull
    @Override
    protected Request.Builder createRequest(final long botId, @NonNull final String payload) {
        final RequestBody body = RequestBody.create(payload, JSON);
        return new Request.Builder()
                .addHeader("user-agent", "Wolfia DiscordBot (" + App.GITHUB_LINK + ", " + App.VERSION + ")")
                .url("https://www.carbonitex.net/discord/data/botdata.php")
                .post(body);
    }

    @Override
    protected boolean isConfigured() {
        final String carbonitexKey = Launcher.getBotContext().getListingsConfig().getCarbonitexKey();
        return carbonitexKey != null && !carbonitexKey.isEmpty();
    }
}
