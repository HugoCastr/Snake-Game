import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Tabuleiro extends JFrame {

    private JPanel painel;
    private JPanel menu;
    private JButton iniciarButton;
    private JButton resetButton;
    private JButton pauseButton;
    private JButton modeButton;
    private JTextField placarField;
    private int x, y;
    private String direcao = "direita";
    private long tempoAtualizacao = 50;
    private int incremento = 2;
    private Quadrado obstaculo, cobra;
    private int larguraTabuleiro, alturaTabuleiro;
    private int placar = 0;

    private ArrayList<Quadrado> corpo = new ArrayList<>();
    private boolean pausar = true;
    private int modo = 1;
    private String dificuldade = "Sem Borda";
    private int palcarRefencia = 0;

    public Tabuleiro() {

        larguraTabuleiro = alturaTabuleiro = 400;

        corpo = new ArrayList<>(); // cria a ArryList

        setTitle("Jogo da Cobrinha");
        setSize(alturaTabuleiro, larguraTabuleiro + 30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        menu = new JPanel();
        menu.setLayout(new FlowLayout());

        iniciarButton = new JButton("Iniciar");
        resetButton = new JButton("Reiniciar");
        pauseButton = new JButton("Pausar");
        modeButton = new JButton(dificuldade);
        placarField = new JTextField("Placar: 0");
        placarField.setEditable(false);

        menu.add(iniciarButton);
        menu.add(resetButton);
        menu.add(pauseButton);
        menu.add(modeButton);
        menu.add(placarField);

        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                g.setColor(new Color(2, 51, 36)); // define e cor do fundo
                g.fillRect(0, 0, larguraTabuleiro, alturaTabuleiro); // pinta o fundo

                for (Quadrado quadrado : corpo) { // um laco for para desenhar o corpo da cobra
                    g.setColor(quadrado.cor);
                    g.fillOval(quadrado.x, quadrado.y, quadrado.altura, quadrado.largura);
                }

                g.setColor(obstaculo.cor);
                g.fillRect(obstaculo.x, obstaculo.y, obstaculo.largura, obstaculo.altura);

            }
        };

        add(menu, BorderLayout.NORTH);
        add(painel, BorderLayout.CENTER);

        setVisible(true);

        // ActionListener para o botão Iniciar
        iniciarButton.addActionListener(e -> {
            Iniciar();
            painel.requestFocusInWindow(); // Devolve o foco para o painel
        });

        // ActionListener para o botão Reset
        resetButton.addActionListener(e -> {
            Reiniciar();
            painel.requestFocusInWindow();
        });

        // ActionListener para o botão Pausar
        pauseButton.addActionListener(e -> {
            Pausar();

        });

        // ActionListener para o botao trocar a dificuldade
        modeButton.addActionListener(e -> {
            DificuldadeJogo();
        });

        painel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                // Exemplo de uso do campo de Texto placarField
                placarField.setText("Placar: " + placar);

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_A:
                        if (!direcao.equals("direita")) {
                            direcao = "esquerda";
                        }
                        break;
                    case KeyEvent.VK_D:
                        if (!direcao.equals("esquerda")) {
                            direcao = "direita";
                        }
                        break;
                    case KeyEvent.VK_W:
                        if (!direcao.equals("baixo")) {
                            direcao = "cima";
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (!direcao.equals("cima")) {
                            direcao = "baixo";
                        }
                        break;

                }
            }
        });

        painel.setFocusable(true);
        painel.requestFocusInWindow();

    }

    private void ComecoDoJogo() {
        cobra = new Quadrado(10, 10, Color.GREEN); // cria a cobra
        corpo.add(cobra); // adiciona a cobra na ArryList

        cobra.x = larguraTabuleiro / 2; // deixa a posicao x da cobra no meio do tabuleiro
        cobra.y = alturaTabuleiro / 2; // deixa a posicao y da cobra no meio do tabuleiro

        Maca(); // cria a maca inicial
    }

    private void Randomizar() {
        int min = 0; // início do tabuleiro
        int max = larguraTabuleiro - 10; // limite máximo na horizontal
        int maxY = alturaTabuleiro - 10; // limite máximo na vertical

        x = (int) (Math.random() * (max - min + 1)) + min; // realiza o calculo de randomizacao do x
        y = (int) (Math.random() * (maxY - min + 1)) + min; // realiza o calculo de randomizacao do y
    }

    private void Maca() {
        obstaculo = new Quadrado(10, 10, Color.RED); // cria um novo obstaculo

        Randomizar(); // chama o metodo randomizar

        obstaculo.x = x; // define a posicao x do obstaculo para o x gerado randomizado
        obstaculo.y = y; // define a posicao y do obstaculo para o y gerado randomizado
    }

    private void ColisaoComMaca() { // completo
        if ((cobra.x < obstaculo.x + 4 && cobra.x > obstaculo.x - 4) && // verfica se a cobra esta a 4 pixels a direita
                                                                        // e a esquerda da maca
                (cobra.y < obstaculo.y + 4 && cobra.y > obstaculo.y - 4)) { // verfica se a cobra esta a 4 pixels acima
                                                                            // e abaixo da maca

            placar++; // incrementa o placar
            VelocidadeJogo(); // chama o metodo para aumetar a velocidade do jogo
            Aumentar(); // chama o metodo para aumentar o tamanho da cobra
            Maca(); // chama o metodo para e cria uma nova maca no tabuleiro
        }
    }

    private void Aumentar() {
        for (int i = 5; i > 0; i--) { // um laco for para adicionar 5 quadrados, para ter mudanca visual no tamanho da
                                      // cobra
            corpo.add(new Quadrado(10, 10, Color.GREEN)); // adiciona um novo quadrado no fim da lista
        }

    }

    private void ColisaoCobra() {
        for (int i = 1; i < corpo.size(); i++) { // um laco for com o tamanho do dos elemntos dentro da ArrayList,
                                                 // comecando do segundo elemento
            Quadrado cabeca = corpo.get(0); // pega a posicao da cabeca
            if (cabeca.x == corpo.get(i).x && cabeca.y == corpo.get(i).y) { // compara se as posicoes sao igual, tanto x
                                                                            // quanto y
                GameOver(); // chama o metodo game over
                return;
            }
        }
    }

    private void MovimentoDaCobra() { // em andamento
        for (int i = corpo.size() - 1; i > 0; i--) { // um laco for com o tamanho da cobra comecando do ultimo indice
            Quadrado quadradoAtual = corpo.get(i); // um auxiliar que salva a posicao do quadrado atual
            Quadrado quadradoAnterior = corpo.get(i - 1); // outro auxiliar que salva a posicao proximo quadrado

            quadradoAtual.x = quadradoAnterior.x; // define a posicao x do quadrado atual para a do quadrado anterior
            quadradoAtual.y = quadradoAnterior.y; // deifne a posicao y do quadrado atual para a do quadrado anterior
        }

        Quadrado cabeca = corpo.get(0); // pega a posicao da cabeca pegando o indice 0 sendo o primeiro

        switch (direcao) {
            case "esquerda":
                cabeca.x -= incremento;
                break;
            case "direita":
                cabeca.x += incremento;
                break;
            case "cima":
                cabeca.y -= incremento;
                break;
            case "baixo":
                cabeca.y += incremento;
                break;
        }
        ColisaoCobra(); // chama o metodo para verificar a colisao com a cobra
    }

    private void VelocidadeJogo() { // em andamento
        if (palcarRefencia == 5 && tempoAtualizacao > 10) { // caso o placar aumente 10 e o tempo de atualizacao seja
                                                             // maior que 100
            tempoAtualizacao = -2; // reduz o valor do tempo de atualizacao em 10
            palcarRefencia = 0; // reseta a referecia do placar
        }
    }

    private void AtrevesarBorda() {
        if (cobra.x < 0) { // verifica se saiu pela esquerda
            cobra.x = larguraTabuleiro; // muda a posicao x da cobra para a largura do tabuleiro
        } else if (cobra.x > larguraTabuleiro) { // verifica se saiu pela direita
            cobra.x = 0; // muda a posicao x da cobra para 0
        }
        if (cobra.y < 0) { // verifica se saiu por cima
            cobra.y = alturaTabuleiro; // muda a posicao y da cobra para a altura do tabuleiro
        } else if (cobra.y > alturaTabuleiro) { // verifica se saiu por baixo
            cobra.y = 0; // muda a posicao y da cobra para 0
        }
    }

    private void ColisaoNasBordas() {
        if ((cobra.x < 0 || cobra.x > alturaTabuleiro) // verifica se a cobra esta no eixo x igual a 0 ou o equivalente
                                                       // ao tamanho do tabuleiro
                || // ou
                (cobra.y < 0 || cobra.y > alturaTabuleiro)) { // verifica se a cobra esta no eixo y igual a 0 ou o
                                                              // equivalente ao tamanho do tabuleiro
            GameOver(); // chama o metodo para informar a perca
        }
    }

    private void Iniciar() {
        ComecoDoJogo();
        new Thread(() -> {
            while (pausar) {
                try {
                    Thread.sleep(tempoAtualizacao);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                MovimentoDaCobra();
                ColisaoComMaca();
                switch (modo) {
                    case 1:
                        AtrevesarBorda();
                        break;

                    case 2:
                        ColisaoNasBordas();
                        break;
                }
                painel.repaint();

            }
        }).start();
    }

    private void Reiniciar() {
        corpo.clear(); // limpa o a arrylist
        tempoAtualizacao = 100; // reinicia o valor para 200
        placar = 0; // reseta o placar
        Iniciar(); // chama o metodo para inciar

        JOptionPane.showMessageDialog(this, "Jogo Reiniciado!", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Pausar() {
        // Interrompe o while(!reset) do método Iniciar() pausando o jogo.
        pausar = (!pausar); // faz com que inverta o valor booleano da classe pausar

        JOptionPane.showMessageDialog(this, "Jogo Pausado!", "Pause", JOptionPane.INFORMATION_MESSAGE);
    }

    private void GameOver() {
        JOptionPane.showMessageDialog(this, "Gamer Over", "A", JOptionPane.INFORMATION_MESSAGE);
        pausar = (!pausar); // faz com que o jogo  pare
    }

    private void DificuldadeJogo() { // em andamento
        modo++; // incrementa o valor

        if (modo > 3) { // caso o valor seja maior que 3
            modo = 1; // define o valor para 1 novamente
        }
        switch (modo) { // define o que vai estar escrito no modo de jogo
            case 1 -> dificuldade = "Sem Borda"; // se o modo for 1 define o que esta escrito para sem borda

            case 2 -> dificuldade = "Com Borda"; // se o modo for 2 define o que esta escrito para com borda
        }

        modeButton.setText(dificuldade); // atualiza o texto do botao com o modo
    }

    public static void main(String[] args) {
        new Tabuleiro();

    }
}