package view;

import dao.QuestaoDAO;
import dao.RespostaAlunoDAO.LinhaRanking;
import dao.RespostaAlunoDAO.LinhaResultado;
import model.Aluno;
import model.Feedback;
import model.Professor;
import model.Questao;
import model.QuestaoMultiplaEscolha;
import model.QuestaoVerdadeiroFalso;
import model.Quiz;
import model.RespostaAluno;
import model.Sessao;
import model.Usuario;
import service.QuizService;
import service.SessaoService;
import service.UsuarioService;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

/** Interface de console: menus de CRUD e execução de um quiz salvo no banco. */
public class MenuConsole {
    private final Scanner sc = new Scanner(System.in);
    private final UsuarioService usuarioService = new UsuarioService();
    private final QuizService quizService = new QuizService();
    private final SessaoService sessaoService = new SessaoService();
    private final SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public void iniciar() {
        System.out.println("Bem-vindo ao Sistema de Quiz da AEP!");
        int opcao;
        do {
            System.out.println("\n========== MENU PRINCIPAL ==========");
            System.out.println("1 - Usuários (professores e alunos)");
            System.out.println("2 - Quizzes e questões");
            System.out.println("3 - Partida ao vivo (sessões com PIN)");
            System.out.println("4 - Painel de resultados do professor");
            System.out.println("0 - Sair");
            opcao = lerInt("Opção: ");
            try {
                switch (opcao) {
                    case 1 -> menuUsuarios();
                    case 2 -> menuQuizzes();
                    case 3 -> menuSessoes();
                    case 4 -> painelResultados();
                    case 0 -> System.out.println("Até logo!");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                if ("23503".equals(e.getSQLState())) { // violação de chave estrangeira
                    System.out.println("Não é possível excluir: existem registros vinculados a este item.");
                } else {
                    System.out.println("Erro no banco de dados: " + e.getMessage());
                }
            }
        } while (opcao != 0);
    }

    // ===================== USUÁRIOS =====================
    private void menuUsuarios() throws SQLException {
        System.out.println("\n--- USUÁRIOS ---");
        System.out.println("1 - Cadastrar  2 - Listar  3 - Buscar por ID  4 - Atualizar  5 - Excluir  0 - Voltar");
        int op = lerInt("Opção: ");
        try {
            switch (op) {
                case 1 -> {
                    int tipo = lerInt("Tipo (1 = Professor, 2 = Aluno): ");
                    String nome = lerTexto("Nome: ");
                    String email = lerTexto("E-mail: ");
                    Usuario u = (tipo == 1) ? new Professor(null, nome, email) : new Aluno(null, nome, email);
                    usuarioService.cadastrar(u);
                    System.out.println("Usuário cadastrado com ID " + u.getId() + ".");
                }
                case 2 -> usuarioService.listar().forEach(u -> System.out.println(formatar(u)));
                case 3 -> {
                    Usuario u = usuarioService.buscar(lerInt("ID: "));
                    System.out.println(u == null ? "Usuário não encontrado." : formatar(u));
                }
                case 4 -> {
                    Usuario u = usuarioService.buscar(lerInt("ID do usuário: "));
                    if (u == null) { System.out.println("Usuário não encontrado."); return; }
                    u.setNome(lerTextoOuManter("Nome", u.getNome()));
                    u.setEmail(lerTextoOuManter("E-mail", u.getEmail()));
                    System.out.println(usuarioService.atualizar(u) ? "Usuário atualizado." : "Nada foi alterado.");
                }
                case 5 -> System.out.println(usuarioService.excluir(lerInt("ID: "))
                        ? "Usuário excluído." : "Usuário não encontrado.");
                default -> { }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Atenção: " + e.getMessage());
        }
    }

    // ===================== QUIZZES =====================
    private void menuQuizzes() throws SQLException {
        System.out.println("\n--- QUIZZES ---");
        System.out.println("1 - Criar quiz  2 - Listar  3 - Detalhar quiz  4 - Editar quiz  5 - Excluir quiz");
        System.out.println("6 - Adicionar questão  7 - Editar questão  8 - Excluir questão  0 - Voltar");
        int op = lerInt("Opção: ");
        try {
            switch (op) {
                case 1 -> {
                    int professorId = lerInt("ID do professor responsável: ");
                    String titulo = lerTexto("Título: ");
                    String disciplina = lerTexto("Disciplina: ");
                    Quiz quiz = quizService.criarQuiz(professorId, titulo, disciplina);
                    System.out.println("Quiz criado com ID " + quiz.getId() + ".");
                }
                case 2 -> {
                    List<Quiz> quizzes = quizService.listarQuizzes();
                    if (quizzes.isEmpty()) System.out.println("Nenhum quiz cadastrado.");
                    quizzes.forEach(q -> System.out.println(formatar(q)));
                }
                case 3 -> {
                    Quiz quiz = quizService.buscarQuiz(lerInt("ID do quiz: "));
                    if (quiz == null) { System.out.println("Quiz não encontrado."); return; }
                    System.out.println(formatar(quiz));
                    quiz.getQuestoes().forEach(q -> System.out.println("   " + formatar(q)));
                }
                case 4 -> {
                    Quiz quiz = quizService.buscarQuiz(lerInt("ID do quiz: "));
                    if (quiz == null) { System.out.println("Quiz não encontrado."); return; }
                    quiz.setTitulo(lerTextoOuManter("Título", quiz.getTitulo()));
                    quiz.setDisciplina(lerTextoOuManter("Disciplina", quiz.getDisciplina()));
                    System.out.println(quizService.atualizarQuiz(quiz) ? "Quiz atualizado." : "Nada foi alterado.");
                }
                case 5 -> System.out.println(quizService.excluirQuiz(lerInt("ID do quiz: "))
                        ? "Quiz e suas questões excluídos." : "Quiz não encontrado.");
                case 6 -> adicionarQuestao();
                case 7 -> editarQuestao();
                case 8 -> System.out.println(quizService.excluirQuestao(lerInt("ID da questão: "))
                        ? "Questão excluída." : "Questão não encontrada.");
                default -> { }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Atenção: " + e.getMessage());
        }
    }

    private void adicionarQuestao() throws SQLException {
        Quiz quiz = quizService.buscarQuiz(lerInt("ID do quiz: "));
        if (quiz == null) { System.out.println("Quiz não encontrado."); return; }
        int tipo = lerInt("Tipo (1 = Múltipla escolha, 2 = Verdadeiro/Falso): ");
        String enunciado = lerTexto("Enunciado: ");
        String gabarito = lerTexto(tipo == 2 ? "Gabarito (V ou F): " : "Gabarito (resposta correta): ");
        int tempo = lerInt("Tempo limite (segundos): ");
        int pontos = lerInt("Pontos: ");

        Questao q = (tipo == 2)
                ? new QuestaoVerdadeiroFalso(null, quiz.getId(), enunciado, tempo, pontos,
                        QuestaoVerdadeiroFalso.interpretar(gabarito))
                : new QuestaoMultiplaEscolha(null, quiz.getId(), enunciado, tempo, pontos, gabarito);
        quizService.adicionarQuestao(quiz, q);
        System.out.println("Questão adicionada com ID " + q.getId() + ".");
    }

    private void editarQuestao() throws SQLException {
        Questao q = quizService.buscarQuestao(lerInt("ID da questão: "));
        if (q == null) { System.out.println("Questão não encontrada."); return; }
        q.setEnunciado(lerTextoOuManter("Enunciado", q.getEnunciado()));
        String gabarito = lerTextoOuManter("Gabarito", QuestaoDAO.gabaritoDe(q));
        if (q instanceof QuestaoVerdadeiroFalso vf) vf.setRespostaCorreta(QuestaoVerdadeiroFalso.interpretar(gabarito));
        else ((QuestaoMultiplaEscolha) q).setGabarito(gabarito);
        q.setTempoLimiteSeg(lerIntOuManter("Tempo limite (s)", q.getTempoLimiteSeg()));
        q.setPontos(lerIntOuManter("Pontos", q.getPontos()));
        System.out.println(quizService.atualizarQuestao(q) ? "Questão atualizada." : "Nada foi alterado.");
    }

    // ===================== PARTIDA AO VIVO =====================
    private void menuSessoes() throws SQLException {
        System.out.println("\n--- PARTIDA AO VIVO ---");
        System.out.println("1 - Iniciar sessão (professor)  2 - Entrar com PIN e jogar (aluno)  3 - Ver ranking");
        System.out.println("4 - Ver feedbacks  5 - Listar sessões  6 - Encerrar sessão  7 - Excluir sessão  0 - Voltar");
        int op = lerInt("Opção: ");
        try {
            switch (op) {
                case 1 -> {
                    int professorId = lerInt("ID do professor: ");
                    int quizId = lerInt("ID do quiz: ");
                    Sessao s = sessaoService.iniciarSessao(professorId, quizId);
                    System.out.println("\n************************************");
                    System.out.println("   SESSÃO ABERTA - PIN: " + s.getPinAcesso());
                    System.out.println("   (ID da sessão: " + s.getId() + ")");
                    System.out.println("************************************");
                    System.out.println("Projete este PIN para a turma entrar na partida.");
                }
                case 2 -> jogarSessao();
                case 3 -> mostrarRanking(lerSessao());
                case 4 -> {
                    List<Feedback> lista = sessaoService.feedbacks(lerSessao());
                    if (lista.isEmpty()) System.out.println("Nenhum feedback enviado.");
                    lista.forEach(f -> System.out.printf("[%d] aluno #%d | opinião: %s | dúvida: %s%n",
                            f.getId(), f.getAlunoId(), f.getOpiniaoDinamica(), f.getDuvidaConteudo()));
                }
                case 5 -> {
                    List<Sessao> lista = sessaoService.listar();
                    if (lista.isEmpty()) System.out.println("Nenhuma sessão criada.");
                    lista.forEach(s -> System.out.printf("[%d] quiz #%d | PIN %d | %s | %s%n", s.getId(),
                            s.getQuizId(), s.getPinAcesso(), s.getStatus(), formatoData.format(s.getDataCriacao())));
                }
                case 6 -> System.out.println(sessaoService.encerrar(lerSessao())
                        ? "Sessão encerrada." : "Sessão não encontrada.");
                case 7 -> System.out.println(sessaoService.excluir(lerSessao())
                        ? "Sessão excluída (com respostas e feedbacks)." : "Sessão não encontrada.");
                default -> { }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Atenção: " + e.getMessage());
        }
    }

    /** Fluxo do aluno: entra com PIN, responde, vê o ranking parcial e envia feedback. */
    private void jogarSessao() throws SQLException {
        Aluno aluno = sessaoService.buscarAluno(lerInt("Seu ID de aluno: "));
        Sessao sessao = sessaoService.entrarNaSessao(lerInt("PIN da sessão: "));
        Quiz quiz = sessaoService.quizDaSessao(sessao);

        List<Questao> pendentes = sessaoService.questoesPendentes(aluno, sessao, quiz);
        if (pendentes.isEmpty()) {
            System.out.println("Você já respondeu todas as questões desta sessão.");
            mostrarRanking(sessao.getId());
            return;
        }

        System.out.println("\nOlá, " + aluno.getNome() + "! " + quiz.getTitulo() + " (" + quiz.getDisciplina() + ")");
        if (pendentes.size() < quiz.getQuestoes().size()) {
            System.out.println("Retomando a partida: faltam " + pendentes.size() + " questão(ões).");
        }
        System.out.println("------------------------------------");
        int pontuacaoTotal = 0;
        for (Questao q : pendentes) {
            System.out.printf("Pergunta (%ds, %d pts): %s%s%n", q.getTempoLimiteSeg(), q.getPontos(),
                    q.getEnunciado(), q instanceof QuestaoVerdadeiroFalso ? " (V/F)" : "");
            String resposta = lerTexto("Sua resposta: ");
            try {
                RespostaAluno r = sessaoService.responder(aluno, sessao, q, resposta);
                pontuacaoTotal += r.getPontosObtidos();
                System.out.println(r.isCorreta()
                        ? "Correto! Você ganhou " + r.getPontosObtidos() + " pontos."
                        : "Incorreto. Você não ganhou pontos.");
            } catch (IllegalArgumentException e) {
                System.out.println("Atenção: " + e.getMessage());
            }
            mostrarRanking(sessao.getId());
            System.out.println();
        }
        System.out.println("Quiz finalizado! Pontos obtidos nesta rodada: " + pontuacaoTotal);

        System.out.println("\n--- Feedback (Enter para pular) ---");
        String opiniao = lerTexto("O que achou da dinâmica? ");
        String duvida = lerTexto("Ficou alguma dúvida sobre a matéria? ");
        if (!opiniao.isEmpty() || !duvida.isEmpty()) {
            sessaoService.enviarFeedback(aluno, sessao, opiniao, duvida);
            System.out.println("Feedback enviado ao professor. Obrigado!");
        }
    }

    private void mostrarRanking(int sessaoId) throws SQLException {
        List<LinhaRanking> ranking = sessaoService.ranking(sessaoId);
        System.out.println("== Ranking parcial ==");
        if (ranking.isEmpty()) System.out.println("Sem respostas ainda.");
        int pos = 1;
        for (LinhaRanking l : ranking) {
            System.out.printf("%dº %s - %d pts%n", pos++, l.aluno(), l.pontos());
        }
    }

    private void painelResultados() throws SQLException {
        try {
            List<LinhaResultado> linhas = sessaoService.painelResultados(lerInt("ID do professor: "));
            System.out.println("\n== Painel de resultados (acertos x erros por matéria) ==");
            if (linhas.isEmpty()) System.out.println("Ainda não há respostas nos seus quizzes.");
            for (LinhaResultado l : linhas) {
                int total = l.acertos() + l.erros();
                int pct = total == 0 ? 0 : Math.round(l.acertos() * 100f / total);
                System.out.printf("%-20s acertos: %3d | erros: %3d | aproveitamento: %3d%%%s%n",
                        l.disciplina(), l.acertos(), l.erros(), pct, pct < 60 ? "  <- revisar" : "");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Atenção: " + e.getMessage());
        }
    }

    // ===================== FORMATAÇÃO =====================
    private String formatar(Usuario u) {
        return String.format("[%d] %s <%s> - %s", u.getId(), u.getNome(), u.getEmail(),
                u instanceof Professor ? "Professor" : "Aluno");
    }

    private String formatar(Quiz q) {
        return String.format("[%d] %s - %s (professor #%d, %d questões)",
                q.getId(), q.getTitulo(), q.getDisciplina(), q.getProfessorId(), q.getQuestoes().size());
    }

    private String formatar(Questao q) {
        return String.format("[%d] %s | %s | gabarito: %s | %ds | %d pts",
                q.getId(), QuestaoDAO.tipoDe(q), q.getEnunciado(), QuestaoDAO.gabaritoDe(q),
                q.getTempoLimiteSeg(), q.getPontos());
    }

    // ===================== LEITURA =====================
    private String lerTexto(String rotulo) {
        System.out.print(rotulo);
        return sc.nextLine().trim();
    }

    /** Lê o ID ou o PIN de uma sessão e devolve o ID. */
    private int lerSessao() throws SQLException {
        return sessaoService.resolverSessaoId(lerInt("ID ou PIN da sessão: "));
    }

    private String lerTextoOuManter(String rotulo, String atual) {
        String v = lerTexto(rotulo + " [" + atual + "] (Enter mantém): ");
        return v.isEmpty() ? atual : v;
    }

    private int lerInt(String rotulo) {
        while (true) {
            try {
                return Integer.parseInt(lerTexto(rotulo));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }

    private int lerIntOuManter(String rotulo, int atual) {
        while (true) {
            String v = lerTexto(rotulo + " [" + atual + "] (Enter mantém): ");
            if (v.isEmpty()) return atual;
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            }
        }
    }
}
