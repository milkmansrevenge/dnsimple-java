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


/**
 * Used to show that an unexpected HTTP status code was received.
 * @author Chris Strand
 */
public class UnexpectedResponseException extends Exception {
  
  private static final long serialVersionUID = 883232997136441178L;
  private int expected = 0;
  private int received = 0;
  
  public UnexpectedResponseException(int expected, int received) {
    this("Expected status code: " + expected + " but got: " + received, expected, received);
  }
  
  public UnexpectedResponseException(String message, int expected, int received) {
    super(message);
    this.expected = expected;
    this.received = received;
  }
  
  /**
   * @return the HTTP status code that was expected
   */
  public int getExpected() {
    return expected;
  }
  
  /**
   * @return the (unexpected) HTTP Status code that was received.
   */
  public int getReceived() {
    return received;
  }

}
