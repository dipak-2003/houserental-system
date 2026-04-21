package com.rental.houserental.service;


import com.rental.houserental.dto.Notice;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class NotificationProvider {

    private final MessageSource messageSource;
    private final Locale locale = Locale.ENGLISH;

    private Notice build(String titleKey, String messageKey, Object... args) {
        String title = messageSource.getMessage(titleKey, args, locale);
        String message = messageSource.getMessage(messageKey, args, locale);
        return new Notice(title, message);
    }

    // ================= TENANT =================

    public Notice tenantBookingAccepted(Object... args) {
        return build(
                "app.notification.to.tenant.book.accept.title",
                "app.notification.to.tenant.book.accept",
                args
        );
    }

    public Notice tenantBookingRejected(Object... args) {
        return build(
                "app.notification.to.tenant.book.reject.title",
                "app.notification.to.tenant.book.reject",
                args
        );
    }

    public Notice tenantWarning(Object... args) {
        return build(
                "app.notification.to.tenant.warning.title",
                "app.notification.to.tenant.warning",
                args
        );
    }

    // ================= OWNER =================

    public Notice ownerPropertyApproved(Object... args) {
        return build(
                "app.notification.to.owner.property.approved.title",
                "app.notification.to.owner.property.approved",
                args
        );
    }

    public Notice ownerPropertyRejected(Object... args) {
        return build(
                "app.notification.to.owner.property.rejected.title",
                "app.notification.to.owner.property.rejected",
                args
        );
    }

    public Notice ownerPaymentWarning(Object... args) {
        return build(
                "app.notification.to.owner.payment.warning.title",
                "app.notification.to.owner.payment.warning",
                args
        );
    }

    public Notice ownerPaymentSuccess(Object... args) {
        return build(
                "app.notification.to.owner.payment.success.title",
                "app.notification.to.owner.payment.success",
                args
        );
    }

    public Notice ownerPaymentFailed(Object... args) {
        return build(
                "app.notification.to.owner.payment.failed.title",
                "app.notification.to.owner.payment.failed",
                args
        );
    }

    // ================= ADMIN =================

    public Notice adminNewProperty(Object... args) {
        return build(
                "app.notification.to.admin.new.property.title",
                "app.notification.to.admin.new.property.uploaded",
                args
        );
    }

    public Notice adminNewCustomer(Object... args) {
        return build(
                "app.notification.to.admin.add.new.customer.title",
                "app.notification.to.admin.add.new.customer",
                args
        );
    }

    public Notice adminPaymentSuccess(Object... args) {
        return build(
                "app.notification.to.admin.payment.success.title",
                "app.notification.to.admin.payment.success",
                args
        );
    }
    public Notice ownerBookingRequest(Object... args){
        return build(
                "app.notification.to.owner.property.booked.title",
                "app.notification.to.owner.property.booked",
                args
        );
    }
}