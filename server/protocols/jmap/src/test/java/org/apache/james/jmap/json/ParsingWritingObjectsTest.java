/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.jmap.json;

import static net.javacrumbs.jsonunit.core.Option.IGNORING_ARRAY_ORDER;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;
import static org.apache.james.jmap.json.ParsingWritingObjects.MESSAGE;
import static org.apache.james.jmap.json.ParsingWritingObjects.SUB_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.io.IOUtils;
import org.apache.james.jmap.methods.GetMessagesMethod;
import org.apache.james.jmap.methods.JmapResponseWriterImpl;
import org.apache.james.jmap.model.Message;
import org.apache.james.jmap.model.SubMessage;
import org.junit.Test;

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

public class ParsingWritingObjectsTest {

    @Test
    public void parsingJsonShouldWorkOnSubMessage() throws Exception {
        SubMessage expected = SUB_MESSAGE;

        SubMessage subMessage = new ObjectMapperFactory().forParsing()
            .readValue(IOUtils.toString(ClassLoader.getSystemResource("json/subMessage.json")), SubMessage.class);

        assertThat(subMessage).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void writingJsonShouldWorkOnSubMessage() throws Exception {
        String expected = IOUtils.toString(ClassLoader.getSystemResource("json/subMessage.json"));

        String json = new ObjectMapperFactory().forWriting()
                .writeValueAsString(SUB_MESSAGE);

        assertThatJson(json)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(expected);

    }

    @Test
    public void parsingJsonShouldWorkOnMessage() throws Exception {
        Message expected = MESSAGE;

        Message message = new ObjectMapperFactory().forParsing()
            .readValue(IOUtils.toString(ClassLoader.getSystemResource("json/message.json")), Message.class);

        assertThat(message).isEqualToComparingFieldByField(expected);
    }

    @Test
    public void writingJsonShouldWorkOnMessage() throws Exception {
        String expected = IOUtils.toString(ClassLoader.getSystemResource("json/message.json"));

        SimpleFilterProvider filterProvider = new SimpleFilterProvider()
                .addFilter(JmapResponseWriterImpl.PROPERTIES_FILTER, SimpleBeanPropertyFilter.serializeAll())
                .addFilter(GetMessagesMethod.HEADERS_FILTER, SimpleBeanPropertyFilter.serializeAll());

        String json = new ObjectMapperFactory().forWriting()
                .setFilterProvider(filterProvider)
                .writeValueAsString(MESSAGE);

        assertThatJson(json)
            .when(IGNORING_ARRAY_ORDER)
            .isEqualTo(expected);

    }
}
