package model;

public class QuestaoMultiplaEscolha extends Questao {
    private String gabarito;

    public QuestaoMultiplaEscolha(Integer id, Integer quizId, String enunciado, Integer tempoLimiteSeg, Integer pontos, String gabarito) {
        super(id, quizId, enunciado, tempoLimiteSeg, pontos);
        this.gabarito = gabarito;
    }

    @Override
    public Integer validarResposta(String respostaAluno) {
        if (this.gabarito != null && this.gabarito.equalsIgnoreCase(respostaAluno)) {
            return this.getPontos();
        }
        return 0;
    }

    public String getGabarito() { return gabarito; }
    public void setGabarito(String gabarito) { this.gabarito = gabarito; }
}
