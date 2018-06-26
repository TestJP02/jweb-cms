package io.sited.pincode.service;

import io.sited.database.Repository;
import io.sited.message.MessagePublisher;
import io.sited.pincode.PinCodeOptions;
import io.sited.pincode.api.message.SendPinCodeMessage;
import io.sited.pincode.api.pincode.CreatePinCodeRequest;
import io.sited.pincode.domain.PinCodeTracking;
import io.sited.util.exception.Exceptions;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class PinCodeService {
    @Inject
    PinCodeOptions options;
    @Inject
    Repository<PinCodeTracking> repository;
    @Inject
    PinCodeProvider pinCodeProvider;

    @Inject
    MessagePublisher<SendPinCodeMessage> publisher;

    @Transactional
    public String create(CreatePinCodeRequest request) {
        if (!canSendPinCode(request)) {
            throw Exceptions.badRequestException("pinCode", "exceed daily rate");
        }
        String code = pinCodeProvider.get();
        PinCodeTracking tracking = new PinCodeTracking();
        tracking.id = UUID.randomUUID().toString();
        tracking.ip = request.ip;
        tracking.code = code;
        tracking.email = request.email;
        tracking.phone = request.phone;
        tracking.createdBy = request.requestBy;
        tracking.createdTime = OffsetDateTime.now();
        repository.insert(tracking);

        SendPinCodeMessage pinCodeEmailMessage = new SendPinCodeMessage();
        pinCodeEmailMessage.email = request.email;
        pinCodeEmailMessage.code = code;
        pinCodeEmailMessage.phone = request.phone;
        publisher.publish(pinCodeEmailMessage);

        return code;
    }

    private boolean canSendPinCode(CreatePinCodeRequest request) {
        List<PinCodeTracking> results = repository.query("SELECT t FROM PinCodeTracking t WHERE t.ip=?0", request.ip).sort("createdTime", true).limit(1, options.dailyRate).find();
        if (results.isEmpty()) {
            return true;
        }
        Duration lastCreated = Duration.between(results.get(0).createdTime, OffsetDateTime.now());
        return lastCreated.getSeconds() >= 60 || results.size() < options.dailyRate || Duration.between(results.get(results.size() - 1).createdTime, OffsetDateTime.now()).toHours() > 24;
    }
}
