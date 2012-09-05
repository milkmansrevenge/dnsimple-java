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

import java.util.Date;

/**
 * DNS record for a domain.
 * <p>
 * @author Chris Strand
 */
public class Record {
  
  private final String domainId;
  private final String id;
  private final String name;
  private final String recordType;
  private final String content;
  private final Integer ttl;
  private final Integer priority;
  private final Date createdAt;
  private final Date updatedAt;
  
  public Record() {
    this.domainId = null;
    this.id = null;
    this.name = null;
    this.recordType = null;
    this.content = null;
    this.ttl = null;
    this.priority = null;
    this.createdAt = null;
    this.updatedAt = null;
  }
  
  public Record(String domainId, String id, String name, String recordType, String content, Integer ttl, Integer priority, Date createdAt, Date updatedAt) {
    this.domainId = domainId;
    this.id = id;
    this.name = name;
    this.recordType = recordType;
    this.content = content;
    this.ttl = ttl;
    this.priority = priority;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }
  
  public static Builder getBuilder() {
    return new Builder();
  }
  
  public String getDomainId() {
    return domainId;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getRecordType() {
    return recordType;
  }

  public String getContent() {
    return content;
  }

  public Integer getTtl() {
    return ttl;
  }

  public Integer getPriority() {
    return priority;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public String toString() {
    return "Record [domainId=" + domainId + ", id=" + id + ", name=" + name
        + ", recordType=" + recordType + ", content=" + content + ", ttl="
        + ttl + ", priority=" + priority + ", createdAt=" + createdAt
        + ", updatedAt=" + updatedAt + "]";
  }
  
  
  
  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((content == null) ? 0 : content.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((priority == null) ? 0 : priority.hashCode());
    result = prime * result
        + ((recordType == null) ? 0 : recordType.hashCode());
    result = prime * result + ((ttl == null) ? 0 : ttl.hashCode());
    return result;
  }
  
  @Override
  public boolean equals(Object obj) {
    
    // Comparison choices are debatable. Note that the ID field is not
    // compared; this is because a user constructing a Record is unlikely to
    // know/set the ID but may want to compare his created Record to one that
    // is returned by the DNSimple API.
    
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (getClass() != obj.getClass())
      return false;
    Record other = (Record) obj;
    if (content == null) {
      if (other.content != null)
        return false;
    } else if (!content.equals(other.content))
      return false;
    
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    
    if (priority == null) {
      if (other.priority != null)
        return false;
    } else if (!priority.equals(other.priority))
      return false;
    
    if (recordType == null) {
      if (other.recordType != null)
        return false;
    } else if (!recordType.equals(other.recordType))
      return false;
    
    if (ttl == null) {
      if (other.ttl != null)
        return false;
    } else if (!ttl.equals(other.ttl))
      return false;
    
    return true;
  }



  public static class Builder {
    
    private String domainId = null;
    private String id = null;
    private String name = null;
    private String recordType = null;
    private String content = null;
    private Integer ttl = null;
    private Integer priority = null;
    private Date createdAt = null;
    private Date updatedAt = null;
    
    public Builder setDomainId(String domainId) {
      this.domainId = domainId;
      return this;
    }
    
    public Builder setId(String id) {
      this.id = id;
      return this;
    }
    
    public Builder setName(String name) {
      this.name = name;
      return this;
    }
    
    /**
     * @see RecordTypes
     */
    public Builder setRecordType(String recordType) {
      this.recordType = recordType;
      return this;
    }
    
    /**
     * @param content what this record points to. E.g. for a 
     *    {@link RecordTypes#A} record this should be an IP address, for a
     *    {@link RecordTypes#CNAME} record this should be a domain name.
     */
    public Builder setContent(String content) {
      this.content = content;
      return this;
    }
    
    /**
     * @param ttl in seconds
     */
    public Builder setTtl(Integer ttl) {
      this.ttl = ttl;
      return this;
    }
    
    /**
     * Some types of records (such as {@link RecordTypes#MX}) have multiple
     * entries with different priorities.
     */
    public Builder setPriority(Integer priority) {
      this.priority = priority;
      return this;
    }
    
    public Builder setCreatedAt(Date createdAt) {
      this.createdAt = createdAt;
      return this;
    }
    
    public Builder setUpdatedAt(Date updatedAt) {
      this.updatedAt = updatedAt;
      return this;
    }
    
    public Record build() {
      return new com.nimbleservers.dnsimple.record.Record(domainId, id, name, recordType, content, ttl, priority, createdAt, updatedAt);
    }
    
  }
  
}
