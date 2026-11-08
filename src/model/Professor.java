package model;

import java.util.Date;
import java.util.Random;

public class Professor extends Usuario {
    private static final Random RANDOM = new Random();

    public Professor(Integer id, String nome, String email) {
        super(id, nome, email);
    }

    /** Cria um novo quiz pertencente a este professor (RF01). */
    public Quiz criarQuiz(String titulo, String disciplina) {
        return new Quiz(null, titulo, disciplina, this.getId());
    }

    /** Abre uma partida ao vivo do quiz com um PIN de 6 dígitos (RF02). */
    public Sessao iniciarSessao(Quiz quiz) {
        int pin = 100000 + RANDOM.nextInt(900000);
        return new Sessao(null, quiz.getId(), pin, Sessao.ABERTA, new Date());
    }
}
