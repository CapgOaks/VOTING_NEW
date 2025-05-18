package com.capgemini.security4.dto;

public class RunningCandidateDto {
    private String partyName;
    private String partyLogo;
    private String manifesto;
    private String candidateName;
    private String electionName;

    public RunningCandidateDto(String partyName, String partyLogo, String manifesto, String candidateName,
            String electionName) {
        this.partyName = partyName;
        this.partyLogo = partyLogo;
        this.manifesto = manifesto;
        this.candidateName = candidateName;
        this.electionName = electionName;
    }

    public RunningCandidateDto() {
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyLogo() {
        return partyLogo;
    }

    public void setPartyLogo(String partyLogo) {
        this.partyLogo = partyLogo;
    }

    public String getManifesto() {
        return manifesto;
    }

    public void setManifesto(String manifesto) {
        this.manifesto = manifesto;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getElectionName() {
        return electionName;
    }

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    @Override
    public String toString() {
        return "RunningCandidateDto{" +
                "partyName='" + partyName + '\'' +
                ", partyLogo='" + partyLogo + '\'' +
                ", manifesto='" + manifesto + '\'' +
                ", candidateName='" + candidateName + '\'' +
                ", electionName='" + electionName + '\'' +
                '}';
    }
}
