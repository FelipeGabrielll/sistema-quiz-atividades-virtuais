package model;

import java.util.Date;

public class Aluno extends Usuario {
    public Aluno(Integer id, String nome, String email) {
        super(id, nome, email);
    }

    /** Responde uma questão na sessão; a correção é polimórfica (RF03). */
    public RespostaAluno responderQuestao(Sessao sessao, Questao questao, String resposta) {
        Integer pontos = questao.validarResposta(resposta);
        RespostaAluno r = new RespostaAluno(null, this.getId(), sessao.getId(), questao.getId(),
                resposta, pontos, new Date());
        sessao.registrarResposta(r);
        return r;
    }

    /** Envia a avaliação da dinâmica e dúvidas ao final da partida (RF05). */
    public Feedback enviarFeedback(Sessao sessao, String opiniaoDinamica, String duvidaConteudo) {
        Feedback f = new Feedback(null, this.getId(), sessao.getId(), opiniaoDinamica, duvidaConteudo);
        sessao.registrarFeedback(f);
        return f;
    }
}
