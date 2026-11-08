package model;

import java.util.Date;

/** Resposta enviada por um aluno a uma questão durante uma sessão (RF03). */
public class RespostaAluno {
    private Integer id;
    private Integer alunoId;
    private Integer sessaoId;
    private Integer questaoId;
    private String respostaSubmetida;
    private Integer pontosObtidos;
    private Date dataResposta;

    public RespostaAluno(Integer id, Integer alunoId, Integer sessaoId, Integer questaoId,
                         String respostaSubmetida, Integer pontosObtidos, Date dataResposta) {
        this.id = id;
        this.alunoId = alunoId;
        this.sessaoId = sessaoId;
        this.questaoId = questaoId;
        this.respostaSubmetida = respostaSubmetida;
        this.pontosObtidos = pontosObtidos;
        this.dataResposta = dataResposta;
    }

    public boolean isCorreta() {
        return pontosObtidos != null && pontosObtidos > 0;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAlunoId() { return alunoId; }
    public void setAlunoId(Integer alunoId) { this.alunoId = alunoId; }

    public Integer getSessaoId() { return sessaoId; }
    public void setSessaoId(Integer sessaoId) { this.sessaoId = sessaoId; }

    public Integer getQuestaoId() { return questaoId; }
    public void setQuestaoId(Integer questaoId) { this.questaoId = questaoId; }

    public String getRespostaSubmetida() { return respostaSubmetida; }
    public void setRespostaSubmetida(String respostaSubmetida) { this.respostaSubmetida = respostaSubmetida; }

    public Integer getPontosObtidos() { return pontosObtidos; }
    public void setPontosObtidos(Integer pontosObtidos) { this.pontosObtidos = pontosObtidos; }

    public Date getDataResposta() { return dataResposta; }
    public void setDataResposta(Date dataResposta) { this.dataResposta = dataResposta; }
}
