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
@Table(name = "chat_message")
public class ChatMessageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	@Column(nullable = false)
	private String sessionId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private MessageRole role;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String content;

	@Column(columnDefinition = "TEXT")
	private String citations;

	private Double confidence;

	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public MessageRole getRole() {
		return role;
	}

	public void setRole(MessageRole role) {
		this.role = role;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCitations() {
		return citations;
	}

	public void setCitations(String citations) {
		this.citations = citations;
	}

	public Double getConfidence() {
		return confidence;
	}

	public void setConfidence(Double confidence) {
		this.confidence = confidence;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

}
