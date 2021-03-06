package org.apache.james.mailbox.caching;

import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mailbox.store.mail.model.Mailbox;

/**
 * Caches the simple yet possibly expensive to compute metadata info 
 * about a Mailbox like all/unseen messages count and similar
 * 
 */
public interface MailboxMetadataCache {

	long countMessagesInMailbox(Mailbox mailbox,
								MessageMapper underlying) throws MailboxException;

	long countUnseenMessagesInMailbox(Mailbox mailbox,
									  MessageMapper underlying) throws MailboxException;

	Long findFirstUnseenMessageUid(Mailbox mailbox,
								   MessageMapper underlying) throws MailboxException;

	long getLastUid(Mailbox mailbox,
					MessageMapper underlying) throws MailboxException;

	long getHighestModSeq(Mailbox mailbox,
						  MessageMapper underlying) throws MailboxException;

	void invalidate(Mailbox mailbox);

//	public abstract void invalidate(MailboxPath mailboxPath);

}