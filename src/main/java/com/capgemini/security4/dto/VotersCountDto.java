package com.capgemini.security4.dto;

public class VotersCountDto {
    private Long electionId;
    private String electionName;
    private Long votersCount;

    public VotersCountDto(Long electionId, String electionName, Long votersCount) {
        this.electionId = electionId;
        this.electionName = electionName;
        this.votersCount = votersCount;
    }

    public Long getElectionId() {
        return electionId;
    }

    public void setElectionId(Long electionId) {
        this.electionId = electionId;
    }

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public Long getVotersCount() {
        return votersCount;
    }

    public void setVotersCount(Long votersCount) {
        this.votersCount = votersCount;
    }
}
