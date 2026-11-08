package model;

/** Avaliação da dinâmica e dúvida enviada pelo aluno ao final da partida (RF05). */
public class Feedback {
    private Integer id;
    private Integer alunoId;
    private Integer sessaoId;
    private String opiniaoDinamica;
    private String duvidaConteudo;

    public Feedback(Integer id, Integer alunoId, Integer sessaoId, String opiniaoDinamica, String duvidaConteudo) {
        this.id = id;
        this.alunoId = alunoId;
        this.sessaoId = sessaoId;
        this.opiniaoDinamica = opiniaoDinamica;
        this.duvidaConteudo = duvidaConteudo;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getAlunoId() { return alunoId; }
    public void setAlunoId(Integer alunoId) { this.alunoId = alunoId; }

    public Integer getSessaoId() { return sessaoId; }
    public void setSessaoId(Integer sessaoId) { this.sessaoId = sessaoId; }

    public String getOpiniaoDinamica() { return opiniaoDinamica; }
    public void setOpiniaoDinamica(String opiniaoDinamica) { this.opiniaoDinamica = opiniaoDinamica; }

    public String getDuvidaConteudo() { return duvidaConteudo; }
    public void setDuvidaConteudo(String duvidaConteudo) { this.duvidaConteudo = duvidaConteudo; }
}
