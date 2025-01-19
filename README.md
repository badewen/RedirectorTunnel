# RedirectorTunnel
Simple PowerTunnel plugin that sends http/https redirect to client.

## Configuration
Installing PowerTunnel's Root CA is mandatory<br>
Just type in the redirect mappings to the plugin setting page
```declarative
target_ip:target_port|redirect_url # redirect url is directly pasted to the http redirect Location header

# Example: 
www.growtopia1.com:443|https://google.com # this will send http redirects https://google.com to the client

# To specify multiple mappings, use ';' as seperator
github.com|https://www.google.com; w3.org|https://www.google.com; youtube.com|www.google.com
```

# Limitations
For Https traffic, you need to make sure you can connect to target host and the target host sends Http packet data back to you, 
because it works by making workaround to PowerTunnel limitation of sending response to https connection. Attempting to do so will cause it to send plain Http response into Https connection<br>
The current workaround is by modifying Https response  into a Http redirect. <br>
Though for Http traffic, the workaround is not needed and therefore, redirection is guaranteed to happen

# Future plans
- [ ] Maybe a real Proxy that can redirect traffic without sending redirects to the client

