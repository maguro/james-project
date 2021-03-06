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
package org.apache.james.mailbox.jcr;

import org.apache.commons.lang.NotImplementedException;
import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.exception.SubscriptionException;
import org.apache.james.mailbox.jcr.mail.JCRMailboxMapper;
import org.apache.james.mailbox.jcr.mail.JCRMessageMapper;
import org.apache.james.mailbox.jcr.user.JCRSubscriptionMapper;
import org.apache.james.mailbox.model.MailboxId;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.mail.AnnotationMapper;
import org.apache.james.mailbox.store.mail.AttachmentMapper;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mailbox.store.mail.ModSeqProvider;
import org.apache.james.mailbox.store.mail.NoopAttachmentMapper;
import org.apache.james.mailbox.store.mail.UidProvider;
import org.apache.james.mailbox.store.user.SubscriptionMapper;

/**
 * JCR implementation of a {@link MailboxSessionMapperFactory}
 * 
 *
 */
public class JCRMailboxSessionMapperFactory extends MailboxSessionMapperFactory {

    private final MailboxSessionJCRRepository repository;
    private final static int DEFAULT_SCALING = 2;
    private final int scaling;
    private final int messageScaling;
    private final UidProvider uidProvider;
    private final ModSeqProvider modSeqProvider;

    public JCRMailboxSessionMapperFactory(MailboxSessionJCRRepository repository, UidProvider uidProvider, ModSeqProvider modSeqProvider) {
        this(repository, uidProvider, modSeqProvider, DEFAULT_SCALING, JCRMessageMapper.MESSAGE_SCALE_DAY);
    }

    public JCRMailboxSessionMapperFactory(MailboxSessionJCRRepository repository, UidProvider uidProvider, ModSeqProvider modSeqProvider, int scaling, int messageScaling) {
        this.repository = repository;
        this.scaling = scaling;
        this.messageScaling = messageScaling;
        this.uidProvider= uidProvider;
        this.modSeqProvider = modSeqProvider;
    }
    
    @Override
    public MailboxMapper createMailboxMapper(MailboxSession session) throws MailboxException {
        return new JCRMailboxMapper(repository, session, scaling);
    }

    @Override
    public MessageMapper createMessageMapper(MailboxSession session) throws MailboxException {
        return new JCRMessageMapper(repository, session, uidProvider, modSeqProvider,  messageScaling);
    }

    @Override
    public SubscriptionMapper createSubscriptionMapper(MailboxSession session) throws SubscriptionException {
        return new JCRSubscriptionMapper(repository, session, DEFAULT_SCALING);
    }
    
    @Override
    public AttachmentMapper createAttachmentMapper(MailboxSession session) throws MailboxException {
        return new NoopAttachmentMapper();
    }
    
    public MailboxSessionJCRRepository getRepository() {
        return repository;
    }

    @Override
    public AnnotationMapper createAnnotationMapper(MailboxId mailboxId, MailboxSession session)
            throws MailboxException {
        throw new NotImplementedException();
    }

}
