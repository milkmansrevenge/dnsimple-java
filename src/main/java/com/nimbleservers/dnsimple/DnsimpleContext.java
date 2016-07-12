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
package com.nimbleservers.dnsimple;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nimbleservers.dnsimple.domain.Domain;
import com.nimbleservers.dnsimple.record.Record;

/**
 * Thread Safe. Does not perform any caching of data received from querying
 * DNSimple.
 * <p>
 * @author Chris Strand
 */
public class DnsimpleContext {
  
  public static final String END_POINT = "https://api.dnsimple.com/v1";
  public static final String CHARSET = "utf-8";
  
  private final Header headers[];
  private final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();
  
  private DefaultHttpClient httpClient = new DefaultHttpClient(new ThreadSafeClientConnManager());
  
  public DnsimpleContext(String email, String apiKey) {
    this.headers = new Header[3];
    this.headers[0] = new BasicHeader("Accept", "application/json");
    this.headers[1] = new BasicHeader("X-DNSimple-Token", email + ":" + apiKey);
    this.headers[2] = new BasicHeader("Content-Type", "application/json; charset=" + CHARSET);
  }
  
  /**
   * @return a list of all domains 
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public List<Domain> getDomains() throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains";
    List<Domain> result = new LinkedList<Domain>();
    
    int expectedCode = HttpStatus.SC_OK;
    int statusCode;
    
    HttpGet httpGet = null;
    HttpResponse response = null;
    HttpEntity entity = null;
    
    try {
      httpGet = new HttpGet(uri);
      httpGet.setHeaders(headers);
      
      response = httpClient.execute(httpGet);
      entity = response.getEntity();
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
      
      if(entity != null) {
        // The response is a list of maps with one entry each.
        Type collectionType = new TypeToken<LinkedList<HashMap<String, Domain>>>(){}.getType();
        Reader reader = new InputStreamReader(entity.getContent(), CHARSET);
        LinkedList<HashMap<String, Domain>> list = gson.fromJson(reader, collectionType);
        for(HashMap<String, Domain> map : list) {
          // There should only be one entry in the map, but this is neater...
          for(Domain domain : map.values())
            result.add(domain);
        }
      }
      
    } finally {
      try { EntityUtils.consume(entity); } catch(Exception e) {}
    }
    
    return result;
  }
  
  /**
   * @see #getDomain(String)
   */
  public Domain getDomain(Domain domain) throws UnexpectedResponseException, IOException {
    return getDomain(domain.getName());
  }
  
  /**
   * Gets details for a specific domain
   * @param domain the name or the ID of the domain to get
   * @return the domain details
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public Domain getDomain(String domain) throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains/" + domain;
    HttpGet httpGet = new HttpGet(uri);
    
    int expectedCode = HttpStatus.SC_OK;
    int statusCode;
    
    HttpResponse response = null;
    HttpEntity entity = null;
    
    httpGet.setHeaders(headers);
    
    try {
      response = httpClient.execute(httpGet);
      entity = response.getEntity();
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
      
      return parseDomain(entity);
      
    } finally {
      try { EntityUtils.consume(entity); } catch(Exception e) {}
    }
  }
  
  /**
   * TODO: Doesn't work (I think - not sure of expected usage/behaviour)
   * <p>
   * Adds a domain to your account.
   * <br />
   * <strong>NOTE:</strong> This does not register the domain, use
   * {@link #registerDomain(String)} for that.
   * @param domain the name or the ID of the domain
   * @return details for the created domain
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public Domain addDomain(String domain) throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains";
    HashMap<String, Domain> map = new HashMap<String, Domain>();
    HttpPost httpPost = new HttpPost(uri);
    
    int expectedCode = HttpStatus.SC_CREATED;
    int statusCode;
    
    HttpResponse response = null;
    HttpEntity entity = null;
    
    map.put("domain", new Domain(domain));
    httpPost.setHeaders(headers);
    httpPost.setEntity(new StringEntity(gson.toJson(map), CHARSET));
    
    try {
      response = httpClient.execute(httpPost);
      entity = response.getEntity();
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
      
      return parseDomain(entity);
      
    } finally {
      try { EntityUtils.consume(entity); } catch(Exception e) {}
    }
  }
  
  /**
   * Checks to see if the domain is available for registration
   * @param domain the name or the ID of the domain to check
   * @return {@code true} if <strong>domain</strong> is available, {@code false} otherwise
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public boolean isDomainAvailable(String domain) throws UnexpectedResponseException, IOException {
    
    String uri = END_POINT + "/domains/" + domain + "/check";
    
    HttpGet httpGet = null;
    HttpResponse response = null;
    
    int statusCode;
    
    httpGet = new HttpGet(uri);
    httpGet.setHeaders(headers);
    
    try {
      response = httpClient.execute(httpGet);
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode == HttpStatus.SC_NOT_FOUND) {
        return true;
      } else if(statusCode == HttpStatus.SC_OK) {
        return false;
      } else {
        // Note could also have accepted SC_NOT_FOUND
        // May need to make a more flexible exception for cases such as this
        throw new UnexpectedResponseException(HttpStatus.SC_OK, statusCode);
      }
      
    } finally {
      try { EntityUtils.consume(response.getEntity()); } catch(Exception e) {}
    }
    
  }
  
  /**
   * Not implemented.
   * @param domain 
   * @return {@code null}
   */
  public Domain registerDomain(String domain) {
    return null;
  }
  
  /**
   * @see #enableAutoRenewal(String)
   */
  public Domain enableAutoRenewal(Domain domain) throws UnexpectedResponseException, IOException {
    return enableAutoRenewal(domain.getName());
  }
  
  /**
   * Enables auto renewal of a domain
   * @param domain the name or the ID of the domain to enable auto renewal for
   * @return details for the domain
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public Domain enableAutoRenewal(String domain) throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains/" + domain + "/auto_renewal";
    HashMap<String, Object> map = new HashMap<String, Object>();
    HttpPost httpPost = new HttpPost(uri);
    
    int expectedCode = HttpStatus.SC_OK;
    int statusCode;
    
    HttpResponse response = null;
    HttpEntity entity = null;
    
    map.put("auto_renewal", new Object());
    httpPost.setHeaders(headers);
    httpPost.setEntity(new StringEntity(gson.toJson(map), CHARSET));
    
    try {
      response = httpClient.execute(httpPost);
      entity = response.getEntity();
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
      
      return parseDomain(entity);
      
    } finally {
      try { EntityUtils.consume(entity); } catch(Exception e) {}
    }
  }
  
  /**
   * @see #disableAutoRenewal(String)
   */
  public Domain disableAutoRenewal(Domain domain) throws UnexpectedResponseException, IOException {
    return disableAutoRenewal(domain.getName());
  }
  
  /**
   * Disables auto renewal of a domain
   * @param domain the name or the ID of the domain to disable auto renewal for
   * @return details for the domain
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public Domain disableAutoRenewal(String domain) throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains/" + domain + "/auto_renewal";
    HttpDelete httpDelete = new HttpDelete(uri);
    
    int expectedCode = HttpStatus.SC_OK;
    int statusCode;
    
    HttpResponse response = null;
    HttpEntity entity = null;
    
    httpDelete.setHeaders(headers);
    
    try {
      response = httpClient.execute(httpDelete);
      entity = response.getEntity();
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
      
      return parseDomain(entity);
      
    } finally {
      try { EntityUtils.consume(entity); } catch(Exception e) {}
    }
    
  }
  
  /**
   * Sets the name servers for {@code domain}.
   * @param domain the name or the ID of the domain to set the nameservers for
   * @param nameServers maximum of 6 name servers
   * @return {@code true} for success, {@code false} otherwise
   * @throws IllegalStateException If more than 6 name servers are given
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public boolean setNameServers(String domain, Collection<String> nameServers) throws IllegalStateException, UnexpectedResponseException, IOException {
    
    if(nameServers.size() > 6) {
      throw new IllegalStateException("Maximum of 6 name servers supported. Number given: " + nameServers.size());
    }
    
    String uri = END_POINT + "/domains/" + domain + "/name_servers";
    Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();
    Map<String, String> inner = new LinkedHashMap<String, String>();
    Iterator<String> it = nameServers.iterator();
    HttpPost httpPost = new HttpPost(uri);
    
    int expectedCode = HttpStatus.SC_OK;
    int statusCode;
    
    HttpResponse response = null;
    
    map.put("name_servers", inner);
    int i = 1;
    while(it.hasNext()) {
      String nameServer = it.next();
      String key = "ns" + i;
      inner.put(key, nameServer);
      i++;
    }
    
    httpPost.setHeaders(headers);
    httpPost.setEntity(new StringEntity(gson.toJson(map), CHARSET));
    
    try {
      response = httpClient.execute(httpPost);
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
    
    } finally {
      try { EntityUtils.consume(response.getEntity()); } catch(Exception e) {}
    }
    
    return true;
  }
  
  /**
   * @see #getRecords(String, Record)
   */
  public List<Record> getRecords(Domain domain) throws UnexpectedResponseException, IOException {
    return getRecords(domain.getName());
  }
  
  /**
   * Gets the DNS records for a domain
   * @param domain the name or the ID of the domain to get records for
   * @return A list of records
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public List<Record> getRecords(String domain) throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains/" + domain + "/records";
    List<Record> result = new LinkedList<Record>();
    
    int expectedCode = HttpStatus.SC_OK;
    int statusCode;
    
    HttpGet httpGet = null;
    HttpResponse response = null;
    HttpEntity entity = null;
    
    try {
      httpGet = new HttpGet(uri);
      httpGet.setHeaders(headers);
      
      response = httpClient.execute(httpGet);
      entity = response.getEntity();
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
      
      if(entity != null) {
        // The response is a list of maps with one entry each.
        Type collectionType = new TypeToken<LinkedList<HashMap<String, Record>>>(){}.getType();
        Reader reader = new InputStreamReader(entity.getContent(), CHARSET);
        LinkedList<HashMap<String, Record>> list = gson.fromJson(reader, collectionType);
        for(HashMap<String, Record> map : list) {
          // There should only be one entry in each map...
          for(Record record : map.values())
            result.add(record);
        }
      }
      
    } finally {
      try { EntityUtils.consume(entity); } catch(Exception e) {}
    }
    
    return result;
  }
  
  /**
   * @see #addRecord(String, Record)
   */
  public Record addRecord(Domain domain, Record record) throws UnexpectedResponseException, IOException {
    return addRecord(domain.getName(), record);
  }
  
  /**
   * Add a DNS record.
   * <p>
   * For example to create a subdomain:
   * <pre>
   * Record subdomain = Record.getBuilder()
   *    .setRecordType(RecordTypes.A) // A records are used for subdomains
   *    .setName("test1") // will create test1.{yourdomain}
   *    .setContent("192.168.1.1") // What it points to
   *    .setTtl(10 * 60) // How long record should be cached (in seconds)
   *    .build();
   * context.setRecord(domain, subdomain));
   * </pre>
   * <p>
   * Note that attempting to set a record that already exists will fail.
   * Running the above example twice would fail with a 422 response code.
   * If the IP is changed then a new record will be created, but with the
   * same name. Only changing the TTL will not create a new record. This
   * described behaviour is undocumented so may change at any time.
   * <p>
   * It is up to the user of this library to determine whether a record
   * already exists. If it does, {@link #updateRecord(Domain, String, Record)}
   * should be used instead.
   * 
   * @param domain the name or the ID of the domain to set the record for
   * @param record the record to set
   * @return the newly created record with all fields populated
   *    (domain ID, record ID, created at, updated at etc.)
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public Record addRecord(String domain, Record record) throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains/" + domain + "/records";
    HashMap<String, Record> map = new HashMap<String, Record>();
    HttpPost httpPost = new HttpPost(uri);
    
    int expectedCode = HttpStatus.SC_CREATED;
    int statusCode;
    
    HttpResponse response = null;
    HttpEntity entity = null;
    
    map.put("record", record);
    httpPost.setHeaders(headers);
    httpPost.setEntity(new StringEntity(gson.toJson(map), CHARSET));
    
    try {
      response = httpClient.execute(httpPost);
      entity = response.getEntity();
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
      
      return parseRecord(entity);
      
    } finally {
      try { EntityUtils.consume(entity); } catch(Exception e) {}
    }
    
  }
  
  /**
   * @see {@link #updateRecord(String, String, Record)
   */
  public Record updateRecord(Domain domain, String recordId, Record record) throws UnexpectedResponseException, IOException {
    return updateRecord(domain.getName(), recordId, record);
  }
  
  /**
   * Updates an existing record for a domain
   * @param domain the name or the ID of the domain to update the record for
   * @param recordId the ID of the record to be updated
   * @param record what the record should be updated to
   * @return the updated record
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public Record updateRecord(String domain, String recordId, Record record) throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains/" + domain + "/records/" + recordId;
    HashMap<String, Record> map = new HashMap<String, Record>();
    HttpPut httpPut = new HttpPut(uri);
    
    int expectedCode = HttpStatus.SC_OK;
    int statusCode;
    
    HttpResponse response = null;
    HttpEntity entity = null;
    
    map.put("record", record);
    httpPut.setHeaders(headers);
    httpPut.setEntity(new StringEntity(gson.toJson(map), CHARSET));
    
    try {
      response = httpClient.execute(httpPut);
      entity = response.getEntity();
      statusCode = response.getStatusLine().getStatusCode();
      
      if(statusCode != expectedCode) {
        throw new UnexpectedResponseException(expectedCode, statusCode);
      }
      
      return parseRecord(entity);
      
    } finally {
      try { EntityUtils.consume(entity); } catch(Exception e) {}
    }
    
  }

  public void deleteRecord(Domain domain, String recordId) throws UnexpectedResponseException, IOException {
    deleteRecord(domain.getName(), recordId);
  }

  /**
   * Deletes an existing record from a domain
   * @param domain the name or the ID of the domain to delete the record from
   * @param recordId the ID of the record to be deleted
   * @throws UnexpectedResponseException If the HTTP response code from
   *    DNSimple's API was not what was expected
   * @throws IOException If the connection was aborted
   */
  public void deleteRecord(String domain, String recordId) throws UnexpectedResponseException, IOException {
    String uri = END_POINT + "/domains/" + domain + "/records/" + recordId;
    HttpDelete httpDelete = new HttpDelete(uri);

    int expectedCode = HttpStatus.SC_OK;
    int statusCode;

    httpDelete.setHeaders(headers);

    HttpResponse response = httpClient.execute(httpDelete);
    statusCode = response.getStatusLine().getStatusCode();

    if (statusCode != expectedCode) {
      throw new UnexpectedResponseException(expectedCode, statusCode);
    }

  }
  
  /**
   * Closes all connections.
   */
  public void close() {
    httpClient.getConnectionManager().shutdown();
  }
  
  /**
   * Gets GSON to parse the Domain from the entity
   */
  private Domain parseDomain(HttpEntity entity) throws IOException {
    
    if(entity == null)
      return null;
    
    Domain result = null;
    
    try {
      // Get the type so GSON knows how to parse and what to return
      // It should be a map with one element (the domain) in.
      Type collectionType = new TypeToken<HashMap<String, Domain>>(){}.getType();
      Reader reader = new InputStreamReader(entity.getContent(), CHARSET);
      HashMap<String, Domain> map = gson.fromJson(reader, collectionType);
      Iterator<Domain> it = map.values().iterator();
      
      if(!it.hasNext()) {
        return null;
      } else {
        result = it.next();
      }
      
    } catch (UnsupportedEncodingException e) {
      
    }
    
    return result;
  }
  
  /**
   * Gets GSON to parse the Record from the entity
   */
  private Record parseRecord(HttpEntity entity) throws IOException {
    
    if(entity == null)
      return null;
    
    Record result = null;
    
    try {
      Type collectionType = new TypeToken<HashMap<String, Record>>(){}.getType();
      Reader reader = new InputStreamReader(entity.getContent(), CHARSET);
      HashMap<String, Record> map = gson.fromJson(reader, collectionType);
      Iterator<Record> it = map.values().iterator();
      
      if(!it.hasNext()) {
        return null;
      } else {
        result = it.next();
      }
      
    } catch (UnsupportedEncodingException e) {
      
    }
    
    return result;
  }

}
