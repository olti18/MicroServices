package com.oltiberisha.cards.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.actuate.audit.listener.AuditListener;

@MappedSuperclass
@EntityListeners(AuditListener.class)
@Getter@Setter@ToString
public class BaseEntity {
}
