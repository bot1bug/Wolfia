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

package space.npstr.wolfia;

import io.prometheus.client.CollectorRegistry;
import java.time.Clock;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import space.npstr.wolfia.domain.UserCache;
import space.npstr.wolfia.domain.oauth2.OAuth2Requester;
import space.npstr.wolfia.domain.privacy.PrivacyCommand;
import space.npstr.wolfia.domain.setup.GameSetupService;
import space.npstr.wolfia.domain.stats.StatsService;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Extend this class from tests that require a Spring Application Context
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.name=wolfia")
@AutoConfigureMockMvc
public abstract class ApplicationTest extends PostgresAndRedisContainers {

    @LocalServerPort
    protected int port;

    @SpyBean
    protected Clock clock;

    @SpyBean
    protected GameSetupService gameSetupService;

    @SpyBean
    protected UserCache userCache;

    @SpyBean
    protected PrivacyCommand privacyCommand;

    @SpyBean
    protected StatsService statsService;

    @MockBean
    protected OAuth2Requester oAuth2Requester;

    @Autowired // is actually a mock, see DiscordApiConfig
    protected ShardManager shardManager;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .alwaysDo(print())
                .apply(springSecurity())
                .build();
    }

    /**
     * Some static metrics are giving trouble when the application context is restarted between tests.
     */
    @AfterAll
    static void clearCollectorRegistry() {
        CollectorRegistry.defaultRegistry.clear();
    }

}
