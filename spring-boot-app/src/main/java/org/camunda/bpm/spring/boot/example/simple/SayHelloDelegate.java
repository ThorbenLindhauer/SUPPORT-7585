/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
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
package org.camunda.bpm.spring.boot.example.simple;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SayHelloDelegate implements JavaDelegate {

  private final Logger logger = LoggerFactory.getLogger(this.getClass());
  private CloseableHttpClient httpclient;
  private static final HttpGet GET_REQUEST = new HttpGet("http://localhost:8089/endpoint");;

  public SayHelloDelegate() {
    httpclient = HttpClients.createDefault();  
  }
  
  @Override
  public void execute(DelegateExecution execution) throws Exception {
    logger.info("Making HTTP request in process instance: {}", execution.getProcessInstanceId());

    boolean requestResolved = false;
    try (CloseableHttpResponse response = httpclient.execute(GET_REQUEST)) {
      HttpEntity entity = response.getEntity();
      EntityUtils.consume(entity);

      logger.info("HTTP request returned status code {} for process instance {}", 
          response.getStatusLine().getStatusCode(), 
          execution.getProcessInstanceId());
      requestResolved = true;
    }
    finally {
      if (!requestResolved) {
        logger.warn("A HTTP request did not return.");
      }
    }
  }

}
