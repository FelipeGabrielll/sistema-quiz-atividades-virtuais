package model;

public abstract class Questao {
    private Integer id;
    private Integer quizId;
    private String enunciado;
    private Integer tempoLimiteSeg;
    private Integer pontos;

    public Questao(Integer id, Integer quizId, String enunciado, Integer tempoLimiteSeg, Integer pontos) {
        this.id = id;
        this.quizId = quizId;
        this.enunciado = enunciado;
        this.tempoLimiteSeg = tempoLimiteSeg;
        this.pontos = pontos;
    }

    public abstract Integer validarResposta(String respostaAluno);

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getQuizId() { return quizId; }
    public void setQuizId(Integer quizId) { this.quizId = quizId; }

    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }

    public Integer getTempoLimiteSeg() { return tempoLimiteSeg; }
    public void setTempoLimiteSeg(Integer tempoLimiteSeg) { this.tempoLimiteSeg = tempoLimiteSeg; }

    public Integer getPontos() { return pontos; }
    public void setPontos(Integer pontos) { this.pontos = pontos; }
}
