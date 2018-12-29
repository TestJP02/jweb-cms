package app.jweb.pincode.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.OffsetDateTime;

/**
 * @author chi
 */
@Entity
@Table(name = "pincode_tracking")
public class PinCodeTracking {
    @Id
    @Column(name = "id", length = 36)
    public String id;

    @Column(name = "email", length = 128)
    public String email;

    @Column(name = "phone", length = 16)
    public String phone;

    @Column(name = "ip", length = 16)
    public String ip;

    @Column(name = "text", length = 16)
    public String code;

    @Column(name = "created_time")
    public OffsetDateTime createdTime;

    @Column(name = "created_by", length = 64)
    public String createdBy;
}
