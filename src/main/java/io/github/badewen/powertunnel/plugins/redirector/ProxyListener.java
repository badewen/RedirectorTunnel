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

import io.github.krlvm.powertunnel.sdk.http.HttpHeaders;
import io.github.krlvm.powertunnel.sdk.http.ProxyRequest;
import io.github.krlvm.powertunnel.sdk.http.ProxyResponse;
import io.github.krlvm.powertunnel.sdk.proxy.ProxyAdapter;
import io.github.krlvm.powertunnel.sdk.proxy.ProxyServer;
import io.github.krlvm.powertunnel.sdk.types.FullAddress;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public final class ProxyListener extends ProxyAdapter {
    private final HashMap<String, String> redirectionMaps;
    private final ProxyServer proxyServer;

    public ProxyListener(
            final HashMap<String, String> redirectionMaps,
            ProxyServer proxyServer
    ) {
        this.proxyServer = proxyServer;
        this.redirectionMaps = redirectionMaps;
    }

    @Override
    public void onClientToProxyRequest(@NotNull ProxyRequest request) {
        if (request.isBlocked()) return;
        String responseAddress = formatFullAddress(request.address());

        if (this.redirectionMaps.containsKey(responseAddress) && !request.isEncrypted()) {
            request.setResponse(proxyServer.getResponseBuilder("", 302)
                    .header("Location", this.redirectionMaps.get(responseAddress))
                    .header("Access-Control-Allow-Origin", "*")
                    .build()
            );
        }
    }

    @Override
    public void onProxyToClientResponse(@NotNull ProxyResponse response) {
        if (!proxyServer.isMITMEnabled()) return;
        if (!response.isDataPacket()) return;

        String responseAddress = formatFullAddress(response.address());

        if (this.redirectionMaps.containsKey(responseAddress)) {
            response.setCode(302);
            emptyHeaders(response.headers());
            response.headers().set("Location", this.redirectionMaps.get(responseAddress));
            response.headers().set("Access-Control-Allow-Origin", "*");
            response.setRaw("");
        }
    }

    @Override
    public Boolean isMITMAllowed(@NotNull FullAddress address) {
        return true;
    }

    private String formatFullAddress(@NotNull FullAddress address) {
        return address.getHost() + ":" + address.getPort();
    }

    private void emptyHeaders(@NotNull HttpHeaders headers) {
        for (var headerName : headers.names()) {
            headers.remove(headerName);
        }
    }

}
