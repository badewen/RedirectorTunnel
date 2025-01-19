/*
 * This file is part of LibertyTunnel.
 *
 * LibertyTunnel is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * LibertyTunnel is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with LibertyTunnel.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.badewen.powertunnel.plugins.redirector;

import io.github.krlvm.powertunnel.sdk.configuration.Configuration;
import io.github.krlvm.powertunnel.sdk.plugin.PowerTunnelPlugin;
import io.github.krlvm.powertunnel.sdk.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Redirector extends PowerTunnelPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(Redirector.class);

    @Override
    public void onProxyInitialization(@NotNull ProxyServer proxy) {
        final Configuration config = readConfiguration();

        HashMap<String, String> redirectionMaps = new HashMap<>();

        redirectionMaps = parseRedirectMapping(config.get("mapping", ""));

        LOGGER.info("Loaded mapping " + config.get("mapping", ""));

        proxy.setMITMEnabled(true);
        proxy.setFullRequest(true);
        proxy.setFullResponse(true);

        final ProxyListener proxyListener = new ProxyListener(
            redirectionMaps,
            proxy
        );

        LOGGER.info("registering proxy listener");

        registerProxyListener(proxyListener, 5);
    }

    public static HashMap<String, String> parseRedirectMapping(String mappings) {
        HashMap<String, String> parsedMappings = new HashMap<>();
        mappings = mappings.trim();

        var mapping = mappings.split(";");

        for (var redirectMap : mapping) {
            var trimmedRedirectMap = redirectMap.trim();
            var parsedRedirectMap = trimmedRedirectMap.split("\\|");

            if (parsedRedirectMap.length < 2) {
                LOGGER.error("Error parsing redirect mapping. malformed format. ({})", redirectMap);
                continue;
            }

            parsedMappings.put(
                    parsedRedirectMap[0],
                    parsedRedirectMap[1]
            );
        }

        return parsedMappings;
    }
}
