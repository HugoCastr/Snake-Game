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
    private long tempoAtualizacao = 200;
    private int incremento = 2;
    private Quadrado obstaculo, cobra;
    private int larguraTabuleiro, alturaTabuleiro;
    private int placar = 0;

    private ArrayList<Quadrado> corpo = new ArrayList<>();
    private boolean pausar = true;
    private int modo = 1;
    private String dificuldade = "Sem Borda";

    public Tabuleiro() {

        larguraTabuleiro = alturaTabuleiro = 400;

        corpo = new ArrayList<>(); // cria a ArryList
        cobra = new Quadrado(10, 10, Color.BLACK); // cria a cobra
        corpo.add(cobra); // adiciona a cobra na ArryList

        cobra.x = larguraTabuleiro / 2;
        cobra.y = alturaTabuleiro / 2;

        Maca(); // cria a maca inicial

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

                for (Quadrado quadrado : corpo) {
                    g.setColor(quadrado.cor);
                    g.fillRect(quadrado.x, quadrado.y, quadrado.altura, quadrado.largura);
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

    private void Maca() { // completo
        // int min = 40; // define o minimo da largura e altura do tabuleiro
        // int max = alturaTabuleiro - min; // define o maximo da largura e altura do
        // tabuleiro
        // obstaculo = new Quadrado(10, 10, Color.red); // cria um novo obstaculo
        // obstaculo.x = (int) (Math.random() * (max - min + 1)) - min; // randomiza a
        // posicao x
        // obstaculo.y = (int) (Math.random() * (max - min + 1)) - min; // randomiza a
        // posicao y

        int min = 0; // início do tabuleiro
        int max = larguraTabuleiro - 10; // limite máximo na horizontal
        int maxY = alturaTabuleiro - 10; // limite máximo na vertical

        obstaculo = new Quadrado(10, 10, Color.red);

        // Gera uma posição aleatória para a maçã
        obstaculo.x = (int) (Math.random() * (max - min + 1)) + min;
        obstaculo.y = (int) (Math.random() * (maxY - min + 1)) + min;
    }

    private void ColisaoComMaca() { // completo
        if ((cobra.x < obstaculo.x + 4 && cobra.x > obstaculo.x - 4) && // verfica se a cobra esta a 4 pixels a direita
                                                                        // ou a esquerda da maca
                (cobra.y < obstaculo.y + 4 && cobra.y > obstaculo.y - 4)) { // verfica se a cobra esta a 4 pixels acima
                                                                            // ou abaixo da maca

            placar++; // incrementa o placar
            Aumentar(); // chama a funcao para aumentar o tamanho da cobra
            Maca(); // chama a funcao para e cria uma nova maca no tabuleiro
        }
    }

    private void Aumentar() {
        for (int i = 5; i > 0; i--) {
            corpo.add(new Quadrado(10, 10, Color.BLACK)); // Adiciona um novo quadrado na cauda
        }

    }

    private void ColisaoCobra() {
        for (int i = 1; i < corpo.size(); i++) {
            Quadrado cabeca = corpo.get(0);
            if (cabeca.x == corpo.get(i).x && cabeca.y == corpo.get(i).y) {
                GameOver();
                return;
            }
        }
    }

    private void MovimentoDaCobra() { // em andamento
        for (int i = corpo.size() - 1; i > 0; i--) {
            Quadrado quadradoAtual = corpo.get(i);
            Quadrado quadradoAnterior = corpo.get(i - 1);

            quadradoAtual.x = quadradoAnterior.x;
            quadradoAtual.y = quadradoAnterior.y;
        }

        // Move a cabeça para a direção escolhida
        Quadrado cabeca = corpo.get(0);

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
        ColisaoCobra();
    }
    private void VelocidadeJogo(){
        if (placar % 10 == 0){
            tempoAtualizacao =- 10;
        }
    }
    private void AtrevesarBorda() {
        if (cobra.x < 0) { // verifica se saiu pela esquerda
            cobra.x = larguraTabuleiro;
        } else if (cobra.x > larguraTabuleiro) { // verifica se saiu pela direita
            cobra.x = 0;
        }
        if (cobra.y < 0) { // verifica se saiu por cima
            cobra.y = alturaTabuleiro;
        } else if (cobra.y > alturaTabuleiro) { // verifica se saiu por baixo
            cobra.y = 0;
        }
        if (cobra.x < 0) { // verifica se saiu pela esquerda
            cobra.x = larguraTabuleiro;
        } else if (cobra.x > larguraTabuleiro) { // verifica se saiu pela direita
            cobra.x = 0;
        }
        if (cobra.y < 0) { // verifica se saiu por cima
            cobra.y = alturaTabuleiro;
        } else if (cobra.y > alturaTabuleiro) { // verifica se saiu por baixo
            cobra.y = 0;
        }
    }

    private void ColisaoNasBordas() {
        if ((cobra.x < 0 || cobra.x > alturaTabuleiro) // verifica se a cobra esta no eixo x igual a 0 ou o equivalente
                                                       // ao tamanho do tabuleiro
                || // ou
                (cobra.y < 0 || cobra.y > alturaTabuleiro)) { // verifica se a cobra esta no eixo y igual a 0 ou o
                                                              // equivalente ao tamanho do tabuleiro
            GameOver(); // chama a funcao para informar a perca
        }
    }

    private void Iniciar() {

        new Thread(() -> {
            while (pausar) {
                try {
                    Thread.sleep(tempoAtualizacao);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                VelocidadeJogo();
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
        // Adicione aqui a lógica para reiniciar o jogo
        corpo.clear(); // limpa o a arrylist
        cobra = new Quadrado(10, 10, Color.BLACK); // cria um novo quadrado
        cobra.x = larguraTabuleiro / 2; // faz com que a cobra retorne ao meio do tabuleiro
        cobra.y = alturaTabuleiro / 2;
        corpo.add(cobra); // adiciona na arraylist
        Maca(); // cria uma nova maca
        placar = 0; // reseta o placar
        Iniciar();
        

        JOptionPane.showMessageDialog(this, "Jogo Reiniciado!", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Pausar() {
        // Interrompe o while(!reset) do método Iniciar() pausando o jogo.
        pausar = (!pausar);

        JOptionPane.showMessageDialog(this, "Jogo Pausado!", "Pause", JOptionPane.INFORMATION_MESSAGE);
    }

    private void GameOver() {
        JOptionPane.showMessageDialog(this, "Gamer Over", "A", JOptionPane.INFORMATION_MESSAGE);
        Reiniciar();
    }

    private void DificuldadeJogo() { // em andamento
        modo++; // incrementa o valor
        
        if (modo > 2) { // caso o valor seja maior que 3
            modo = 1; // define o valor para 1 novamente
        }
        switch (modo) { // define o que vai estar escrito no modo de jogo
            case 1 -> dificuldade = "Sem Borda";

            case 2 -> dificuldade = "Com Borda";

        }

        modeButton.setText(dificuldade); // atualiza o texto do botao com a nova dificuldade
    }

    public static void main(String[] args) {
        new Tabuleiro();

    }
}