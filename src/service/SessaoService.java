package service;

import dao.FeedbackDAO;
import dao.QuizDAO;
import dao.RespostaAlunoDAO;
import dao.RespostaAlunoDAO.LinhaRanking;
import dao.RespostaAlunoDAO.LinhaResultado;
import dao.SessaoDAO;
import dao.UsuarioDAO;
import model.Aluno;
import model.Feedback;
import model.Professor;
import model.Questao;
import model.Quiz;
import model.RespostaAluno;
import model.Sessao;
import model.Usuario;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Regras da partida ao vivo: PIN (RF02), respostas e ranking (RF03), resultados (RF04) e feedback (RF05). */
public class SessaoService {
    private final SessaoDAO sessaoDAO = new SessaoDAO();
    private final QuizDAO quizDAO = new QuizDAO();
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final RespostaAlunoDAO respostaDAO = new RespostaAlunoDAO();
    private final FeedbackDAO feedbackDAO = new FeedbackDAO();

    /** O professor dono do quiz abre a sessão; o PIN gerado não pode coincidir com outra sessão aberta. */
    public Sessao iniciarSessao(int professorId, int quizId) throws SQLException {
        Professor professor = buscarProfessor(professorId);
        Quiz quiz = quizDAO.buscarPorId(quizId);
        if (quiz == null) throw new IllegalArgumentException("Quiz não encontrado.");
        if (!quiz.getProfessorId().equals(professor.getId())) {
            throw new IllegalArgumentException("Só o professor dono do quiz pode iniciar a sessão.");
        }
        if (quiz.getQuestoes().isEmpty()) {
            throw new IllegalArgumentException("O quiz não possui questões.");
        }
        Sessao sessao;
        do {
            sessao = professor.iniciarSessao(quiz);
        } while (sessaoDAO.existePinAberto(sessao.getPinAcesso()));
        return sessaoDAO.inserir(sessao);
    }

    /** Login da turma: aluno + PIN de uma sessão aberta. */
    public Sessao entrarNaSessao(int pin) throws SQLException {
        Sessao sessao = sessaoDAO.buscarAbertaPorPin(pin);
        if (sessao == null) throw new IllegalArgumentException("PIN inválido ou sessão já encerrada.");
        return sessao;
    }

    public Quiz quizDaSessao(Sessao sessao) throws SQLException {
        return quizDAO.buscarPorId(sessao.getQuizId());
    }

    public Aluno buscarAluno(int alunoId) throws SQLException {
        Usuario u = usuarioDAO.buscarPorId(alunoId);
        if (!(u instanceof Aluno aluno)) throw new IllegalArgumentException("Aluno não encontrado.");
        return aluno;
    }

    /** Questões do quiz que o aluno ainda não respondeu nesta sessão (permite retomar uma partida interrompida). */
    public List<Questao> questoesPendentes(Aluno aluno, Sessao sessao, Quiz quiz) throws SQLException {
        List<Questao> pendentes = new ArrayList<>();
        for (Questao q : quiz.getQuestoes()) {
            if (!respostaDAO.jaRespondeu(aluno.getId(), sessao.getId(), q.getId())) {
                pendentes.add(q);
            }
        }
        return pendentes;
    }

    /** Corrige (polimorfismo em validarResposta) e grava a resposta do aluno. */
    public RespostaAluno responder(Aluno aluno, Sessao sessao, Questao questao, String resposta) throws SQLException {
        if (!sessao.isAberta()) throw new IllegalArgumentException("A sessão está encerrada.");
        if (respostaDAO.jaRespondeu(aluno.getId(), sessao.getId(), questao.getId())) {
            throw new IllegalArgumentException("Você já respondeu esta questão.");
        }
        RespostaAluno r = aluno.responderQuestao(sessao, questao, resposta);
        return respostaDAO.inserir(r);
    }

    public Feedback enviarFeedback(Aluno aluno, Sessao sessao, String opiniao, String duvida) throws SQLException {
        Feedback f = aluno.enviarFeedback(sessao, opiniao, duvida);
        return feedbackDAO.inserir(f);
    }

    public List<LinhaRanking> ranking(int sessaoId) throws SQLException {
        return respostaDAO.ranking(sessaoId);
    }

    public List<Feedback> feedbacks(int sessaoId) throws SQLException {
        return feedbackDAO.listarPorSessao(sessaoId);
    }

    public List<LinhaResultado> painelResultados(int professorId) throws SQLException {
        buscarProfessor(professorId);
        return respostaDAO.resultadosPorDisciplina(professorId);
    }

    /**
     * Aceita o ID da sessão ou o PIN (6 dígitos) e devolve o ID.
     * Retorna -1 se nenhuma sessão tiver esse PIN.
     */
    public int resolverSessaoId(int idOuPin) throws SQLException {
        if (idOuPin < 100000) return idOuPin; // IDs são pequenos; PINs têm 6 dígitos
        Sessao s = sessaoDAO.buscarPorPin(idOuPin);
        return s == null ? -1 : s.getId();
    }

    public Sessao buscar(int id) throws SQLException {
        return sessaoDAO.buscarPorId(id);
    }

    public List<Sessao> listar() throws SQLException {
        return sessaoDAO.listarTodos();
    }

    public boolean encerrar(int sessaoId) throws SQLException {
        Sessao sessao = sessaoDAO.buscarPorId(sessaoId);
        if (sessao == null) return false;
        sessao.encerrarPartida();
        return sessaoDAO.atualizar(sessao);
    }

    public boolean excluir(int sessaoId) throws SQLException {
        return sessaoDAO.excluir(sessaoId);
    }

    private Professor buscarProfessor(int professorId) throws SQLException {
        Usuario u = usuarioDAO.buscarPorId(professorId);
        if (!(u instanceof Professor professor)) throw new IllegalArgumentException("Professor não encontrado.");
        return professor;
    }
}
