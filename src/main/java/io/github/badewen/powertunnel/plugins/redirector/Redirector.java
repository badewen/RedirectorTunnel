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

import io.github.badewen.powertunnel.plugins.redirector.ProxyListener;

import io.github.krlvm.powertunnel.sdk.ServerAdapter;
import io.github.krlvm.powertunnel.sdk.configuration.Configuration;
import io.github.krlvm.powertunnel.sdk.plugin.PowerTunnelPlugin;
import io.github.krlvm.powertunnel.sdk.proxy.ProxyServer;
import io.github.krlvm.powertunnel.sdk.proxy.ProxyStatus;
import io.github.krlvm.powertunnel.sdk.types.FullAddress;
import io.github.krlvm.powertunnel.sdk.utiities.TextReader;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Redirector extends PowerTunnelPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(Redirector.class);

    @Override
    public void onProxyInitialization(@NotNull ProxyServer proxy) {
        final Configuration config = readConfiguration();

        HashMap<FullAddress, FullAddress> redirectionMaps = new HashMap<>();

        redirectionMaps.put(
                new FullAddress("127.0.0.1", 5678),
                new FullAddress("127.0.0.1", 1234)
        );

        final ProxyListener proxyListener = new ProxyListener(
            redirectionMaps
        );

        LOGGER.info("registering proxy listener");

        registerProxyListener(proxyListener);
    }
}
