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
package com.nimbleservers.dnsimple.domain;

import java.util.Date;

/**
 * Data for a domain.
 * <p>
 * @author Chris Strand
 */
public class Domain {
  
  private final String id;
  private final String name;
  private final String nameServerStatus;
  private final String registrationStatus;
  private final Date createdAt;
  private final Date updatedAt;
  // DNSimple has the expiration date in two formats, this one is the same
  // format as the other dates in the JSON response
  private final Date parsedExpirationDate;
  private final String registrantId;
  private final String userId;
  private final Boolean autoRenew;
  private final Boolean privateWhois;
  
  public Domain() {
    this(null);
  }
  
  public Domain(String name) {
    this.id = null;
    this.name = name;
    this.nameServerStatus = null;
    this.registrationStatus = null;
    this.createdAt = null;
    this.updatedAt = null;
    this.parsedExpirationDate = null;
    this.registrantId = null;
    this.userId = null;
    this.autoRenew = null;
    this.privateWhois = null;
  }
  
  public String getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public String getNameServerStatus() {
    return nameServerStatus;
  }
  
  public String getRegistrationStatus() {
    return registrationStatus;
  }

  public Date getCreatedAt() {
    return createdAt;
  }
  
  public Date getUpdatedAt() {
    return updatedAt;
  }
  
  public Date getExpiresAt() {
    return parsedExpirationDate;
  }
  
  public String getRegistrantId() {
    return registrantId;
  }

  public String getUserId() {
    return userId;
  }

  public Boolean getAutoRenew() {
    return autoRenew;
  }
  
  public Boolean getPrivateWhois() {
    return privateWhois;
  }

  @Override
  public String toString() {
    return "Domain [id=" + id + ", name=" + name + ", nameServerStatus="
        + nameServerStatus + ", registrationStatus=" + registrationStatus
        + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt
        + ", parsedExpirationDate=" + parsedExpirationDate + ", registrantId="
        + registrantId + ", userId=" + userId + ", autoRenew=" + autoRenew
        + ", privateWhois=" + privateWhois + "]";
  }
  
}
