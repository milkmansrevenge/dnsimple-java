# A Java wrapper for the DNSimple API

[DNSimple][0] let you do some very cool and useful things with domains via 
their REST API.

This library was created for internal use by [Nimble Servers][1] who, as they
use so much Open Source Software, thought it would be just plain rude not to 
contribute something back!

### Example Usage

There is currently only support for authentication with your API key, not your
password.

```java
DnsimpleContext context = new DnsimpleContext("email@domain.com", "apikey");
List<Domain> domains = context.getDomains();
Domain firstDomain = domains.get(0);
List<Record> records = context.getRecords(firstDomain);
context.close();
```

### Implemented Features

Not all of DNSimple's features are implemented, here is what we have so far:

*   Getting details about a specific domain
*   Listing all domains
*   Checking domain availability
*   Enabling/disabling auto-renewal for domains
*   Setting name servers for domains
*   Getting DNS records for a domain
*   Adding DNS records
*   Updating DNS records

### Design Goals

*   Few dependencies (just [GSON][2] and [Apache HttpClient][3]).
*   Simple and easy to read code structure.
*   Thread safety.

[0]:https://dnsimple.com/
[1]:http://nimbleservers.com/
[2]:http://code.google.com/p/google-gson/
[3]:http://hc.apache.org/httpcomponents-client-ga/