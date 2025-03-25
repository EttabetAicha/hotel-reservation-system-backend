package org.aicha.hotelreservationsystembackend.domain;

import jakarta.persistence.*;
import lombok.*;
import org.aicha.hotelreservationsystembackend.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false, nullable = false)
    private UUID id;

    @OneToOne(mappedBy = "payment")
    private Reservation reservation;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String paymentMethod;

    @Column(name = "stripe_payment_intent_id")
    private String stripePaymentIntentId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "payer_name")
    private String payerName;

    @Column(name = "payer_email")
    private String payerEmail;

    @Column(name = "billing_address")
    private String billingAddress;

    @Column(name = "billing_city")
    private String billingCity;

    @Column(name = "billing_state")
    private String billingState;

    @Column(name = "billing_zip")
    private String billingZip;

    @Column(name = "billing_country")
    private String billingCountry;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        this.paymentDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public boolean processPayment() {
        if ("Credit Card".equals(this.paymentMethod) && this.stripePaymentIntentId != null) {
            this.paymentStatus = PaymentStatus.COMPLETED;
            return true;
        } else if ("PayPal".equals(this.paymentMethod)) {
            this.paymentStatus = PaymentStatus.COMPLETED;
            return true;
        } else if ("Bank Transfer".equals(this.paymentMethod)) {
            this.paymentStatus = PaymentStatus.PENDING;
            return false;
        }

        return false;
    }

    public boolean confirmPayment() {
        this.paymentStatus = PaymentStatus.COMPLETED;
        return true;
    }

    public boolean refundPayment() {
        if (this.paymentStatus == PaymentStatus.COMPLETED) {
            this.paymentStatus = PaymentStatus.FAILED;
            return true;
        }
        return false;
    }

    public boolean cancelPayment() {
        if (this.paymentStatus == PaymentStatus.PENDING) {
            this.paymentStatus = PaymentStatus.CANCELLED;
            return true;
        }
        return false;
    }
}