package com.jiwei.base_llm.customer.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "chat_session")
public class ChatSessionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(nullable = false)
	private String userId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ChannelType channel;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SessionStatus status = SessionStatus.ACTIVE;

	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ChannelType getChannel() {
		return channel;
	}

	public void setChannel(ChannelType channel) {
		this.channel = channel;
	}

	public SessionStatus getStatus() {
		return status;
	}

	public void setStatus(SessionStatus status) {
		this.status = status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
