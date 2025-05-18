package com.capgemini.security4.dto;

public class CandidateDto {
	private Long candidateId;
	private String candidateName;
	private String manifesto;
	private String partyName;
	private String partyLogo;

	public CandidateDto() {
		// TODO Auto-generated constructor stub
	}

	public CandidateDto(Long candidateId, String candidateName, String manifesto, String partyName, String partyLogo) {
		super();
		this.candidateId = candidateId;
		this.candidateName = candidateName;
		this.manifesto = manifesto;
		this.partyName = partyName;
		this.partyLogo = partyLogo;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public String getCandidateName() {
		return candidateName;
	}

	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}

	public String getManifesto() {
		return manifesto;
	}

	public void setManifesto(String manifesto) {
		this.manifesto = manifesto;
	}

	public String getPartyName() {
		return partyName;
	}

	public void setPartyName(String partyName) {
		this.partyName = partyName;
	}

	public String getpartyLogo() {
		return partyLogo;
	}

	public void setpartyLogo(String partyLogo) {
		this.partyLogo = partyLogo;
	}

}
