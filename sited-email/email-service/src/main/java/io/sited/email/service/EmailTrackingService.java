package io.sited.email.service;

import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import io.sited.database.Query;
import io.sited.database.Repository;
import io.sited.email.api.email.EmailQuery;
import io.sited.email.api.email.SendEmailRequest;
import io.sited.email.api.email.SendEmailResponse;
import io.sited.email.api.email.SendEmailStatus;
import io.sited.email.domain.EmailTracking;
import io.sited.util.collection.QueryResponse;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * @author chi
 */
public class EmailTrackingService {
    @Inject
    Repository<EmailTracking> repository;

    @Transactional
    public EmailTracking success(SendEmailRequest request, SendEmailResponse response) {
        EmailTracking emailTracking = emailTracking(request);
        emailTracking.status = SendEmailStatus.SUCCESS;
        emailTracking.result = response.result;

        repository.insert(emailTracking);
        return emailTracking;
    }

    @Transactional
    public EmailTracking failed(SendEmailRequest request, SendEmailResponse response) {
        EmailTracking emailTracking = emailTracking(request);
        emailTracking.status = SendEmailStatus.FAILED;
        emailTracking.errorMessage = response.errorMessage;
        repository.insert(emailTracking);
        return emailTracking;
    }

    public Optional<EmailTracking> get(String id) {
        return repository.query("SELECT t from EmailTracking t WHERE t.id=?0", id).findOne();
    }

    public QueryResponse<EmailTracking> find(EmailQuery emailQuery) {
        Query<EmailTracking> query = repository.query("SELECT t from EmailTracking t WHERE 1=1");
        int index = 0;
        if (!Strings.isNullOrEmpty(emailQuery.query)) {
            query.append("AND( t.from_user LIKE ?" + index++ + " OR t.to LIKE ?" + index++ + " OR t.subject LIKE ? )", "%" + emailQuery.query + "%", "%" + emailQuery.query + "%", "%" + emailQuery.query + "%");
        }
        if (emailQuery.status != null) {
            query.append("AND t.status=?" + index, emailQuery.status);
        }
        query.limit(emailQuery.page, emailQuery.limit);
        if (!Strings.isNullOrEmpty(emailQuery.sortingField)) {
            query.sort("t." + emailQuery.sortingField, emailQuery.desc);
        }
        return query.findAll();
    }

    private EmailTracking emailTracking(SendEmailRequest request) {
        EmailTracking emailTracking = new EmailTracking();
        emailTracking.id = UUID.randomUUID().toString();
        emailTracking.from = request.from;
        emailTracking.to = Joiner.on(';').join(request.to);
        emailTracking.replyTo = request.replyTo;
        emailTracking.subject = request.subject;
        emailTracking.content = request.content;
        emailTracking.status = SendEmailStatus.FAILED;
        emailTracking.createdBy = request.requestBy;
        emailTracking.createdTime = OffsetDateTime.now();
        return emailTracking;
    }
}
