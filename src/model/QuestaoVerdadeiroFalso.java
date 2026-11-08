package model;

public class QuestaoVerdadeiroFalso extends Questao {
    private Boolean respostaCorreta;

    public QuestaoVerdadeiroFalso(Integer id, Integer quizId, String enunciado, Integer tempoLimiteSeg, Integer pontos, Boolean respostaCorreta) {
        super(id, quizId, enunciado, tempoLimiteSeg, pontos);
        this.respostaCorreta = respostaCorreta;
    }

    @Override
    public Integer validarResposta(String respostaAluno) {
        Boolean resposta = interpretar(respostaAluno);
        if (this.respostaCorreta != null && this.respostaCorreta.equals(resposta)) {
            return this.getPontos();
        }
        return 0;
    }

    /** Converte "V", "Verdadeiro", "F", "Falso" em Boolean (null se inválido). */
    public static Boolean interpretar(String texto) {
        if (texto == null) return null;
        String t = texto.trim().toUpperCase();
        if (t.equals("V") || t.equals("VERDADEIRO")) return Boolean.TRUE;
        if (t.equals("F") || t.equals("FALSO")) return Boolean.FALSE;
        return null;
    }

    public Boolean getRespostaCorreta() { return respostaCorreta; }
    public void setRespostaCorreta(Boolean respostaCorreta) { this.respostaCorreta = respostaCorreta; }
}
