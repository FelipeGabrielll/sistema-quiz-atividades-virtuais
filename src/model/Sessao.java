package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Partida ao vivo de um quiz em sala de aula (RF02).
 * Agrega as respostas dos alunos e os feedbacks enviados ao final.
 */
public class Sessao {
    public static final String ABERTA = "ABERTA";
    public static final String ENCERRADA = "ENCERRADA";

    private Integer id;
    private Integer quizId;
    private Integer pinAcesso;
    private String status;
    private Date dataCriacao;
    private List<RespostaAluno> respostas = new ArrayList<>();
    private List<Feedback> feedbacks = new ArrayList<>();

    public Sessao(Integer id, Integer quizId, Integer pinAcesso, String status, Date dataCriacao) {
        this.id = id;
        this.quizId = quizId;
        this.pinAcesso = pinAcesso;
        this.status = status;
        this.dataCriacao = dataCriacao;
    }

    public void encerrarPartida() {
        this.status = ENCERRADA;
    }

    public boolean isAberta() {
        return ABERTA.equals(status);
    }

    public void registrarResposta(RespostaAluno r) { respostas.add(r); }
    public void registrarFeedback(Feedback f) { feedbacks.add(f); }

    public List<RespostaAluno> getRespostas() { return respostas; }
    public List<Feedback> getFeedbacks() { return feedbacks; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getQuizId() { return quizId; }
    public void setQuizId(Integer quizId) { this.quizId = quizId; }

    public Integer getPinAcesso() { return pinAcesso; }
    public void setPinAcesso(Integer pinAcesso) { this.pinAcesso = pinAcesso; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(Date dataCriacao) { this.dataCriacao = dataCriacao; }
}
