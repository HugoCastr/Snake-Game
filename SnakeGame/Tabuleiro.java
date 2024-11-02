import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tabuleiro extends JFrame {

    private JPanel painel;
    private JPanel menu;
    private JButton iniciarButton;
    private JButton resetButton;
    private JButton pauseButton;
    private JTextField placarField;
    private int x, y;
    private String direcao = "direita";
    private long tempoAtualizacao = 20;
    private int incremento = 2;
    private Quadrado obstaculo, cobra;
    private int larguraTabuleiro, alturaTabuleiro;
    private int placar = 0;

    public Tabuleiro() {

        larguraTabuleiro = alturaTabuleiro = 400;

        cobra = new Quadrado(10, 10, Color.BLACK);
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
        placarField = new JTextField("Placar: 0");
        placarField.setEditable(false);

        menu.add(iniciarButton);
        menu.add(resetButton);
        menu.add(pauseButton);
        menu.add(placarField);

        painel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(cobra.cor);
                g.fillRect(cobra.x, cobra.y, cobra.altura, cobra.largura);

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

    private void Maca() {
        int min = 10; // define o minimo da largura e altura do tabuleiro
        int max = alturaTabuleiro - 20; // define o maximo da largura e altura do tabuleiro
        obstaculo = new Quadrado(10, 10, Color.red); // cria um novo obstaculo
        obstaculo.x = (int) (Math.random() * (max - min + 1)) - min; // randomiza a posicao x
        obstaculo.y = (int) (Math.random() * (max - min + 1)) - min; // randomiza a posicao y
    }

    private void ColisaoComMaca() {
        if ((cobra.x < obstaculo.x + 4 && cobra.x > obstaculo.x - 4) && // verfica se a cobra esta a 4 pixels a direita ou a esquerda da maca
                (cobra.y < obstaculo.y + 4 && cobra.y > obstaculo.y - 4)) { // verfica se a cobra esta a 4 pixels acima ou abaixo da maca

            placar++; // incrementa o placar
            Maca(); // e cria uma nova maca no tabuleiro
        }
    }

    private void Iniciar() {

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(tempoAtualizacao);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                switch (direcao) {
                    case "esquerda":
                        cobra.x -= incremento;
                        break;
                    case "direita":
                        cobra.x += incremento;
                        break;
                    case "cima":
                        cobra.y -= incremento;
                        break;
                    case "baixo":
                        cobra.y += incremento;
                        break;

                }
                ColisaoComMaca();
                painel.repaint();

            }
        }).start();
    }

    private void Reiniciar() {
        // Adicione aqui a lógica para reiniciar o jogo
        cobra.x = larguraTabuleiro / 2; // faz com que a cobra retorne ao meio do tabuleiro
        cobra.y = alturaTabuleiro / 2;
        Maca(); // cria uma nova maca
        placar = 0; // reseta o placar
            
        JOptionPane.showMessageDialog(this, "Jogo Reiniciado!", "Reset", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Pausar() {
        // Interrompe o while(!reset) do método Iniciar() pausando o jogo.

        JOptionPane.showMessageDialog(this, "Jogo Pausado!", "Pause", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        new Tabuleiro();
    }
}