package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListView;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.lang.reflect.Method;

public class CadastroEquipeController {
    
    @FXML
    private TextField nomeField;
    
    @FXML
    private TextArea descricaoField;
    
    @FXML
    private ListView<String> membrosListView;
    
    @FXML
    private ComboBox<String> projetoComboBox;
    
    private ObservableList<String> membros = FXCollections.observableArrayList();
    private List<Object> usuariosMembros = new ArrayList<>();
    private Object equipeEmEdicao = null;
    private boolean modoEdicao = false;
    private List<Object> projetos = new ArrayList<>();
    
    @FXML
    private void initialize() {
        // Inicializar a lista de membros
        membrosListView.setItems(membros);
        
        // Carregar projetos para o ComboBox
        try {
            // Obter o repositório
            Object repositorio = Class.forName("Repositorio").getMethod("getInstance").invoke(null);
            
            // Obter a lista de projetos
            projetos = (List<Object>) repositorio.getClass().getMethod("getProjetos").invoke(repositorio);
            
            // Adicionar nomes dos projetos ao ComboBox
            ObservableList<String> nomesProjetos = FXCollections.observableArrayList();
            nomesProjetos.add("Nenhum"); // Opção para não selecionar projeto
            
            for (Object projeto : projetos) {
                String nomeProjeto = (String) projeto.getClass().getMethod("getNome").invoke(projeto);
                nomesProjetos.add(nomeProjeto);
            }
            
            projetoComboBox.setItems(nomesProjetos);
            projetoComboBox.getSelectionModel().selectFirst();
            
        } catch (Exception e) {
            e.printStackTrace();
            // Apenas mostra um erro no console, não interrompe a inicialização
        }
    }
    
    /**
     * Configura o controlador para editar uma equipe existente
     * @param equipe O objeto equipe a ser editado
     */
    public void setEquipeParaEdicao(Object equipe) {
        this.equipeEmEdicao = equipe;
        this.modoEdicao = true;
        
        // Preencher os campos com os dados da equipe
        try {
            Class<?> equipeClass = equipe.getClass();
            
            String nome = (String) equipeClass.getMethod("getNome").invoke(equipe);
            String descricao = (String) equipeClass.getMethod("getDescricao").invoke(equipe);
            
            nomeField.setText(nome);
            descricaoField.setText(descricao);
            
            // Carregar membros da equipe
            List<?> membrosDaEquipe = (List<?>) equipeClass.getMethod("getMembros").invoke(equipe);
            
            // Limpar listas atuais
            membros.clear();
            usuariosMembros.clear();
            
            // Adicionar membros às listas
            for (Object membro : membrosDaEquipe) {
                String nomeMembro = (String) membro.getClass().getMethod("getNome").invoke(membro);
                String cpfMembro = (String) membro.getClass().getMethod("getCpf").invoke(membro);
                membros.add(nomeMembro + " (" + cpfMembro + ")");
                usuariosMembros.add(membro);
            }
            
            // Configurar o projeto da equipe, se houver
            Object projetoDaEquipe = equipeClass.getMethod("getProjeto").invoke(equipe);
            if (projetoDaEquipe != null) {
                String nomeProjetoDaEquipe = (String) projetoDaEquipe.getClass().getMethod("getNome").invoke(projetoDaEquipe);
                
                // Encontrar o projeto no ComboBox
                for (int i = 0; i < projetoComboBox.getItems().size(); i++) {
                    if (projetoComboBox.getItems().get(i).equals(nomeProjetoDaEquipe)) {
                        projetoComboBox.getSelectionModel().select(i);
                        break;
                    }
                }
            } else {
                projetoComboBox.getSelectionModel().selectFirst(); // "Nenhum"
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar dados da equipe");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleAdicionarMembro() {
        try {
            // Obter a lista de usuários do repositório
            Object repositorio = Class.forName("Repositorio").getMethod("getInstance").invoke(null);
            List<?> usuarios = (List<?>) repositorio.getClass().getMethod("getUsuarios").invoke(repositorio);
            
            if (usuarios.isEmpty()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("Aviso");
                alert.setHeaderText(null);
                alert.setContentText("Não há usuários cadastrados. Cadastre usuários primeiro.");
                alert.showAndWait();
                return;
            }
            
            // Criar uma lista de nomes de usuários para o diálogo
            List<String> nomeUsuarios = new ArrayList<>();
            for (Object usuario : usuarios) {
                String nome = (String) usuario.getClass().getMethod("getNome").invoke(usuario);
                String cpf = (String) usuario.getClass().getMethod("getCpf").invoke(usuario);
                nomeUsuarios.add(nome + " (" + cpf + ")");
            }
            
            // Mostrar diálogo de seleção
            ChoiceDialog<String> dialog = new ChoiceDialog<>(nomeUsuarios.get(0), nomeUsuarios);
            dialog.setTitle("Adicionar Membro");
            dialog.setHeaderText("Selecione um usuário para adicionar à equipe");
            dialog.setContentText("Usuário:");
            
            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String selectedName = result.get();
                int index = nomeUsuarios.indexOf(selectedName);
                
                // Verificar se o membro já está na equipe
                if (membros.contains(selectedName)) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Aviso");
                    alert.setHeaderText(null);
                    alert.setContentText("Este usuário já está na equipe.");
                    alert.showAndWait();
                    return;
                }
                
                // Adicionar o membro à lista
                membros.add(selectedName);
                usuariosMembros.add(usuarios.get(index));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao adicionar membro");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleRemoverMembro() {
        int selectedIndex = membrosListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            membros.remove(selectedIndex);
            usuariosMembros.remove(selectedIndex);
        } else {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("Selecione um membro para remover.");
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleSalvar(ActionEvent event) {
        if (validarFormulario()) {
            String nome = nomeField.getText();
            String descricao = descricaoField.getText();
            String projetoSelecionado = projetoComboBox.getValue();
            
            try {
                // Obter a instância do repositório
                Object repositorio = Class.forName("Repositorio").getMethod("getInstance").invoke(null);
                
                // Encontrar o projeto selecionado
                Object projetoObj = null;
                if (projetoSelecionado != null && !projetoSelecionado.equals("Nenhum")) {
                    for (Object projeto : projetos) {
                        String nomeProjeto = (String) projeto.getClass().getMethod("getNome").invoke(projeto);
                        if (nomeProjeto.equals(projetoSelecionado)) {
                            projetoObj = projeto;
                            break;
                        }
                    }
                }
                
                if (modoEdicao && equipeEmEdicao != null) {
                    // Atualizar a equipe existente
                    Class<?> equipeClass = equipeEmEdicao.getClass();
                    
                    // Atualizar os campos da equipe
                    equipeClass.getMethod("setNome", String.class).invoke(equipeEmEdicao, nome);
                    equipeClass.getMethod("setDescricao", String.class).invoke(equipeEmEdicao, descricao);
                    
                    // Atualizar projeto
                    if (projetoObj != null) {
                        Method setProjetoMethod = equipeClass.getMethod("setProjeto", projetoObj.getClass());
                        setProjetoMethod.invoke(equipeEmEdicao, projetoObj);
                    } else {
                        // Se nenhum projeto foi selecionado, definir como null
                        Method setProjetoMethod = equipeClass.getMethod("setProjeto", Class.forName("Projeto"));
                        setProjetoMethod.invoke(equipeEmEdicao, (Object)null);
                    }
                    
                    // Limpar membros atuais
                    Method limparMembrosMethod = equipeClass.getMethod("limparMembros");
                    limparMembrosMethod.invoke(equipeEmEdicao);
                    
                    // Adicionar novos membros
                    Method adicionarMembroMethod = equipeClass.getMethod("adicionarMembro", Class.forName("Usuario"));
                    for (Object usuario : usuariosMembros) {
                        adicionarMembroMethod.invoke(equipeEmEdicao, usuario);
                    }
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Equipe atualizada com sucesso!");
                    alert.showAndWait();
                } else {
                    // Criar nova equipe usando reflexão
                    Object novaEquipe = Class.forName("Equipe")
                        .getConstructor(String.class, String.class)
                        .newInstance(nome, descricao);
                    
                    // Definir o projeto da equipe
                    if (projetoObj != null) {
                        Method setProjetoMethod = novaEquipe.getClass().getMethod("setProjeto", projetoObj.getClass());
                        setProjetoMethod.invoke(novaEquipe, projetoObj);
                    } else {
                        // Definir como null se nenhum projeto foi selecionado
                        Method setProjetoMethod = novaEquipe.getClass().getMethod("setProjeto", Class.forName("Projeto"));
                        setProjetoMethod.invoke(novaEquipe, (Object)null);
                    }
                    
                    // Adicionar membros à equipe
                    for (Object usuario : usuariosMembros) {
                        Class.forName("Equipe").getMethod("adicionarMembro", Class.forName("Usuario"))
                            .invoke(novaEquipe, usuario);
                    }
                    
                    // Adicionar ao repositório
                    Method adicionarEquipeMethod = repositorio.getClass().getMethod("adicionarEquipe", Class.forName("Equipe"));
                    adicionarEquipeMethod.invoke(repositorio, novaEquipe);
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Equipe cadastrada com sucesso!");
                    alert.showAndWait();
                }
                
                // Fechar a janela
                fecharJanela(event);
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao processar equipe");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    private void handleCancelar(ActionEvent event) {
        fecharJanela(event);
    }
    
    private boolean validarFormulario() {
        StringBuilder erros = new StringBuilder();
        
        if (nomeField.getText().isEmpty()) {
            erros.append("Nome da equipe é obrigatório\n");
        }
        
        if (descricaoField.getText().isEmpty()) {
            erros.append("Descrição é obrigatória\n");
        }
        
        if (erros.length() > 0) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro de Validação");
            alert.setHeaderText("Corrija os seguintes erros:");
            alert.setContentText(erros.toString());
            alert.showAndWait();
            return false;
        }
        
        return true;
    }
    
    private void fecharJanela(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}