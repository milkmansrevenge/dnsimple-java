# A Java wrapper for the DNSimple API

[DNSimple][0] let you do some very cool and useful things with domains via 
their REST API.

This library was created for internal use by [Nimble Servers][1] who use a lot
of Open Source Software and thought it would be nice to contribute something
back!

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

### Installation

This library is not on Maven central so must be compiled by yourself.

1. Download and install [Maven 3][4].
2. Get the dnsimple-java source code, either with Git:
    * with Git: 
    ```git clone git://github.com/milkmansrevenge/dnsimple-java.git```
    * or from the [Zip file][5].
3. Compile with Maven (from within the same directory as ```pom.xml```):
   ```mvn install```
4. In the ```target``` directory there should be two JAR files. The larger one
   contains all the other dependencies.
    * ```dnsimple-java-<version>.jar``` and
    * ```dnsimple-java-<version>-jar-with-dependencies.jar```

[0]:https://dnsimple.com/
[1]:http://nimbleservers.com/
[2]:http://code.google.com/p/google-gson/
[3]:http://hc.apache.org/httpcomponents-client-ga/
[4]:http://maven.apache.org/download.html
[5]:https://github.com/milkmansrevenge/nimbleservers-zeus/zipball/master