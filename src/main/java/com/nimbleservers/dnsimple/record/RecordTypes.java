/**
 * Copyright 2012 Nimble Servers Limited. http://nimbleservers.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.nimbleservers.dnsimple.record;

/**
 * The com.nimbleservers.dnsimple.record types supported by DNSimple
 * <p>
 * @author Chris Strand
 * @see <a href="http://en.wikipedia.org/wiki/List_of_DNS_record_types">List of DNS record types (Wikipedia)</a>
 */
public class RecordTypes {
  
  /**
   * Map a domain name to a 32 bit IPv4 address
   */
  public static final String A = "A";
  /**
   * Map a domain name to a 128 bit IPv6 address
   */
  public static final String AAAA = "AAAA";
  /**
   * Map a domain name to another domain (can be used for top level domains)
   * @see <a href="http://blog.dnsimple.com/introducing-the-alias-com.nimbleservers.dnsimple.record/">DNSimple blog post</a>
   */
  public static final String ALIAS = "ALIAS";
  /**
   * Map a domain name to another name
   */
  public static final String CNAME = "CNAME";
  /**
   * Describe host (usually hardware and OS)
   * @see <a href="https://www.poweradmin.org/trac/wiki/Documentation/DNS-HINFO">HINFO Record (PowerAdmin.org)</a>
   */
  public static final String HINFO = "HINFO";
  /**
   * Define one or more mail exchanges that can deliver email for the domain
   */
  public static final String MX = "MX";
  /**
   * Name authority pointer that maps URNs to domains
   */
  public static final String NAPTR = "NAPTR";
  /**
   * Delegate a subdomain to another name server
   */
  public static final String NS = "NS";
  /**
   * Provide a pool of CNAME records to be selected randomly
   * @see <a href="http://blog.dnsimple.com/pool-a-new-virtual-com.nimbleservers.dnsimple.record-providing-a-pool-of-cname-records/">DNSimple blog post</a>
   */
  public static final String POOL = "POOL";
  /**
   * Reverse DNS com.nimbleservers.dnsimple.record for mapping an address to a domain name
   */
  public static final String PTR = "PTR";
  /**
   * Sender policy framework com.nimbleservers.dnsimple.record that defines who can send email from a
   * domain name
   */
  public static final String SPF = "SPF";
  /**
   * Define a service that is available for the domain
   */
  public static final String SRV = "SRV";
  /**
   * Provide an SSH finger print to help verify the authenticity of the host
   */
  public static final String SSHFP = "SSHFP";
  /**
   * Custom text records
   */
  public static final String TXT = "TXT";
  /**
   * Point the domain to a URL (HTTP only)
   * @see <a href="http://blog.dnsimple.com/dns-simplified-cname-records/">DNSimple blog post</a>
   */
  public static final String URL = "URL";

}
