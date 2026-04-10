import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.MaskFormatter;

//CLASSE PRINCIPAL DO SISTEMA DE APOSENTADORIA
public class AposentadoriaInss extends JFrame {

    //CAMPOS DE ENTRADA DE DADOS

    JTextField campoNome, campoIdade, campoContribuicao, campoEmail;
    JComboBox<String> campoSexo, campoEstado, campoCidade;
    JFormattedTextField campoCpf, campoNumero;
    JTextArea resultadoArea, areaHistorico;

    //COMPONENTES DA TABELA HISTÓRICO

    DefaultTableModel modelo;
    JTable tabela = new JTable(modelo);

        public AposentadoriaInss() {

            //CONFIGURAÇÕES DAS JANELAS
                
            setTitle("Sistema de Aposentadoria - INSS");
            setSize(1000, 400);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLayout(new BorderLayout());

            //CONFIGURAÇÃO DO CAMPO EMAIL
            campoEmail = new JTextField("@gmail.com", 20);
            campoEmail.setCaretPosition(0);

            campoCidade = new JComboBox<>();

            //BLOQUEIA A MODIFICAÇÃO NO @GMAIL.COM
            ((AbstractDocument) campoEmail.getDocument()).setDocumentFilter(new DocumentFilter() {

            private final String dominio = "@gmail.com";

            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {

                if (offset <= fb.getDocument().getLength() - dominio.length()) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void remove(FilterBypass fb, int offset, int length)
                    throws BadLocationException {

                if (offset + length <= fb.getDocument().getLength() - dominio.length()) {
                    super.remove(fb, offset, length);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {

                if (offset + length <= fb.getDocument().getLength() - dominio.length()) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });

            //BLOQUEIA LETRAS NO CAMPO DA IDADE
            DocumentFilter apenasNumeros = new DocumentFilter() {
                @Override
                public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                        throws BadLocationException {
                    if (string.matches("\\d+")) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                        throws BadLocationException {
                    if (text.matches("\\d+")) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            };

            //CRIAÇÃO DAS COLUNAS
            modelo = new DefaultTableModel();
            modelo.addColumn("Nome");
            modelo.addColumn("Idade");
            modelo.addColumn("Sexo");
            modelo.addColumn("Contribuição");
            modelo.addColumn("CPF");
            modelo.addColumn("Telefone");
            modelo.addColumn("Naturalidade");
            modelo.addColumn("Email");
            modelo.addColumn("Status");
    
            tabela = new JTable(modelo);

            //PAINEL DO FORMULÁRIO
            JPanel formPanel = new JPanel(new GridLayout(9,2,5,5));

            //CAMPOS DO FORMULÁRIO
            campoNome = new JTextField();
            campoIdade = new JTextField();
            campoSexo = new JComboBox<>(new String[]{"Selecione o Sexo", "Masculino", "Feminino"});
            campoContribuicao = new JTextField();
            campoEstado = new JComboBox<>(new String[]{"Selecione o Estado", "BA", "DF", "GO", "MG", "RJ", "SP"});
            campoCidade = new JComboBox<>(new String[]{"Selecione um estado primeiro"});

            ((AbstractDocument) campoIdade.getDocument()).setDocumentFilter(apenasNumeros);
            ((AbstractDocument) campoContribuicao.getDocument()).setDocumentFilter(apenasNumeros);


            //MÁSCARA PARA CPF
            try {
                MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
                mascaraCpf.setPlaceholderCharacter('_');
                campoCpf = new JFormattedTextField(mascaraCpf);
            } catch (Exception e) {
                campoCpf = new JFormattedTextField();
            }
            
            //MÁSCARA PARA NÚMERO DE TELEFONE
            try {
                MaskFormatter mascaraNumero = new MaskFormatter("(##)#####-####");
                mascaraNumero.setPlaceholderCharacter('_');
                campoNumero = new JFormattedTextField(mascaraNumero);
            } catch (Exception e) {
                campoNumero = new JFormattedTextField();
            }

            //ADICIONANDO CAMPOS NO FORMULÁRIO
            formPanel.add(new JLabel("Nome Completo:"));
            formPanel.add(campoNome);
            formPanel.add(new JLabel("Idade:"));
            formPanel.add(campoIdade);
            formPanel.add(new JLabel("Sexo:"));
            formPanel.add(campoSexo);
            formPanel.add(new JLabel("Anos de Contribuição:"));
            formPanel.add(campoContribuicao);
            formPanel.add(new JLabel("CPF:"));
            formPanel.add(campoCpf);
            formPanel.add(new JLabel("Número de Telefone:"));
            formPanel.add(campoNumero);
            formPanel.add(new JLabel("Email:"));
            formPanel.add(campoEmail);
            formPanel.add(new JLabel("Estado:"));
            formPanel.add(campoEstado);
            formPanel.add(new JLabel("Cidade:"));
            formPanel.add(campoCidade);

            //BOTÕES DO SISTEMA
            JButton btnCalcular = new JButton("Calcular");
            JButton btnLimpar = new JButton("Limpar");
            JButton btnSair = new JButton("Sair");

            JPanel buttonPanel = new JPanel();

            buttonPanel.add(btnCalcular);
            buttonPanel.add(btnLimpar);
            buttonPanel.add(btnSair);

            //ÁREA DE RESULTADO
            resultadoArea = new JTextArea(10, 30);
            resultadoArea.setEditable(false);

            areaHistorico = new JTextArea(10, 30);
            areaHistorico.setEditable(false);


            //CRIANDO AS COLUNAS DA TABELA HISTÓRICO
            String [] colunas = {"Nome", "Idade", "Sexo", "Contribuição", "CPF", "Telefone", "Naturalidade", "Email", "Status"};
            modelo = new DefaultTableModel(colunas, 0);
            tabela = new JTable(modelo);
            
            //ORGANIZAÇÃO DO LAYOUT
            JScrollPane scrollTabela = new JScrollPane(tabela);
            add(scrollTabela, BorderLayout.CENTER);
            add(formPanel, BorderLayout.NORTH);
            add(buttonPanel, BorderLayout.SOUTH);

            //CENTRALIZANDO OS DADOS NO HISTÓRICO
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);

            for (int i = 0; i < tabela.getColumnCount(); i++) {
                tabela.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            //HISTÓRICO FAKE
            modelo.addRow(new Object[]{
                "Saymon Oliveira Sousa", "19 anos", "Masculino", "0 anos", "708.019.571-33", "(62)98221-6163", "Goiânia-GO", "saymonoliveira@gmail.com", "Faltam 35 anos de contribuição"
            });

            modelo.addRow(new Object[]{
                "Edy Laus", "37 anos" , "Masculino", "20 anos", "987.654.321-00", "(62)98146-6180", "Goiânia-GO", "edylaus@gmail.com", "Faltam 15 anos de contribuição"
            });

            modelo.addRow(new Object[]{
                "Jefferson Dionísio dos Reis", "26 anos", "Masculino", "9 anos", "111.222.333-44", "(62)9320-0720", "Goiânia-GO", "jeffersondosreis@gmail.com", "Faltam 26 anos de contribuição"
            });


            //CONDIÇÕES PARA SELECIONAR ESTADO/CIDADE NO CADASTRO
            campoEstado.addActionListener(ee -> {
                    String estado = campoEstado.getSelectedItem().toString();
                    campoCidade.removeAllItems();


                    if (estado.equals("BA")) {
                        campoCidade.addItem("Selecione a Cidade");
                        campoCidade.addItem("Bom Jesus da Lapa");
                        campoCidade.addItem("Correntina");
                        campoCidade.addItem("Salvador");
                    }

                    else if (estado.equals("DF")) {
                        campoCidade.addItem("Selecione a Cidade");
                        campoCidade.addItem("Brasília");
                    }
                    else if (estado.equals("GO")) {
                        campoCidade.addItem("Selecione a Cidade");
                        campoCidade.addItem("Anápolis");
                        campoCidade.addItem("Goiânia");
                        campoCidade.addItem("Posse");
                    }
                    else if (estado.equals("MG")) {
                        campoCidade.addItem("Selecione a Cidade");
                        campoCidade.addItem("Belo Horizonte");
                        campoCidade.addItem("Uberlândia");
                    }
                    else if (estado.equals("RJ")) {
                        campoCidade.addItem("Selecione a Cidade");
                        campoCidade.addItem("Duque de Caxias");
                        campoCidade.addItem("Niterói");
                        campoCidade.addItem("Rio de Janeiro");
                        campoCidade.addItem("Volta Redonda");
                    }
                    else if (estado.equals("SP")) {
                        campoCidade.addItem("Selecione a Cidade");
                        campoCidade.addItem("Campinas");
                        campoCidade.addItem("Diadema");
                        campoCidade.addItem("Guarulhos");
                        campoCidade.addItem("Itaquaquecetuba");
                        campoCidade.addItem("Mogi das Cruzes");
                        campoCidade.addItem("Osasco");
                        campoCidade.addItem("Ribeirão Preto");
                        campoCidade.addItem("Santos");
                        campoCidade.addItem("São Paulo");
                    }
                    if (estado.equals("Selecione o Estado")) {
                        JOptionPane.showMessageDialog(this, "Selecione um estado!");
                        return;
                    }
                });


            //AÇÃO DO BOTÃO CALCULAR
            btnCalcular.addActionListener(e -> {

                //VALIDAÇÃO DOS CAMPOS NUMÉRICOS
                if (campoIdade.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Digite a idade!");
                return;
                }

                if (campoContribuicao.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Digite os anos de contribuição!");
                    return;
                }

                //DECLARANDO VARIAVÉIS
                String nome = campoNome.getText();

                int idade = Integer.parseInt(campoIdade.getText());
                String idadeFormatada = idade + " anos";
                
                String sexoStr = campoSexo.getSelectedItem().toString();

                int contribuicao = Integer.parseInt(campoContribuicao.getText());
                String contribuicaoFormatada = contribuicao + " anos";

                String cpfFormatado = campoCpf.getText();
                String cpf = cpfFormatado.replace("[^0-9]", "");

                String numeroFormatado = campoNumero.getText();
                String numero = numeroFormatado.replace("[^0-9]","");

                String emailCompleto = campoEmail.getText();

                String estadoStr = campoEstado.getSelectedItem().toString();
                String cidadeStr = campoCidade.getSelectedItem().toString();
                String naturalidade = cidadeStr + "-" + estadoStr;

                int faltante = 0;
                String resultado;
                String status;

                //CRIANDO MENSAGEM DE CASO DE ERRO
                if (nome.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Digite o seu nome!");
                    return;
                }

                if (!nome.contains(" ")) {
                JOptionPane.showMessageDialog(this, "Digite nome e sobrenome!");
                return;
                }

                if (idade - contribuicao < 16) {
                JOptionPane.showMessageDialog(null,
                "Erro: diferença entre idade e contribuição muito baixa (possível inconsistência)",
                "Erro de validação",
                JOptionPane.ERROR_MESSAGE);
                return; // para o programa aqui
                }

                if (sexoStr.equals("Feminino")) {
                    if (idade >= 60 || contribuicao >= 30) {
                        resultado = "Aposentado";
                        status = ("Já tem direito à aposentadoria.");
                    } else {
                        faltante = 30 - contribuicao;
                        resultado = "Não aposentado";
                        status = ("Faltam " + faltante + " anos de contribuição.");
                    }

                } else if (sexoStr.equals("Masculino")) {
                    if (idade >= 65 || contribuicao >= 35) {
                        resultado = "Aposentado";
                        status = ("já tem direito à aposentadoria.");
                    } else {
                        faltante = 35 - contribuicao;
                        resultado = "Não aposentado";
                        status = ("Faltam " + faltante + " anos de contribuição.");
                    }

                } else {
                    JOptionPane.showMessageDialog(formPanel,
                            "Selecione o Sexo!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (cpf.contains("_")) {
                    JOptionPane.showMessageDialog(this, "Preencha o CPF!");
                    return;
                }

                if (numero.contains("_")) {
                    JOptionPane.showMessageDialog(this, "Preencha o Número de Telefone!");
                    return;
                }

                if (emailCompleto.equals("@gmail.com")) {
                    JOptionPane.showMessageDialog(this, "Digite o seu e-mail!");
                    return;
                }
                
                if (estadoStr.equals("Selecione o Estado")) {
                        JOptionPane.showMessageDialog(this, "Selecione um estado!");
                        return;
                }

                if (cidadeStr.equals("Selecione a Cidade")) {
                        JOptionPane.showMessageDialog(this, "Selecione uma cidade!");
                        return;
                }

                //ORDEM DE COMO OS DADOS DEVEM IR PARA O HISTÓRICO
                modelo.addRow(new Object[]{nome, idadeFormatada, sexoStr, contribuicaoFormatada, cpfFormatado, numero, naturalidade, emailCompleto, status});

            });
            
            //AÇÃO DO BOTÃO LIMPAR DADOS
            btnLimpar.addActionListener(e -> {
            campoNome.setText("");
            campoIdade.setText("");
            campoContribuicao.setText("");
            campoCpf.setText("");
            campoNumero.setText("");
            campoEmail.setText("@gmail.com");
            resultadoArea.setText("");
        });

            //AÇÃO DO BOTÃO SAIR
            btnSair.addActionListener(e -> System.exit(0));
        }

    //MÉTODO PRINCIPAL
    public static void main(String[] args) {
        new AposentadoriaInss().setVisible(true);
    }
}
