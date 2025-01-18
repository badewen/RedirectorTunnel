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

import io.github.krlvm.powertunnel.sdk.http.ProxyRequest;
import io.github.krlvm.powertunnel.sdk.proxy.ProxyAdapter;
import io.github.krlvm.powertunnel.sdk.types.FullAddress;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

public final class ProxyListener extends ProxyAdapter {
    private static final Logger log = LoggerFactory.getLogger(ProxyListener.class);
    private final HashMap<FullAddress, FullAddress> redirectionMaps;

    protected final MITMListener mitmListener = new MITMListener();

    public ProxyListener(
            final HashMap<FullAddress, FullAddress> redirectionMaps
    ) {
        this.redirectionMaps = redirectionMaps;
    }


    @Override
    public void onProxyToServerRequest(@NotNull ProxyRequest request) {
        if(request.isBlocked() || request.isEncrypted()
                || (request.address() != null && request.address().getPort() == 443)) return;

        if (this.redirectionMaps.containsKey(request.address())) {
            log.info("wow");
        }
    }

    @Override
    public void onClientToProxyRequest(@NotNull ProxyRequest request) {

    }

    private class MITMListener extends ProxyAdapter {
        @Override
        public Boolean isMITMAllowed(@NotNull FullAddress address) {
            return true;
        }
    }
}
