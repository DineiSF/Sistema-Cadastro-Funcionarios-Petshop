package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.lang.reflect.Method;

public class CadastroUsuarioController {
    
    @FXML
    private TextField nomeField;
    
    @FXML
    private TextField cpfField;
    
    @FXML
    private TextField emailField;
    
    @FXML
    private ComboBox<String> cargoComboBox;
    
    @FXML
    private TextField loginField;
    
    @FXML
    private PasswordField senhaField;
    
    private Object usuarioEmEdicao = null;
    private boolean modoEdicao = false;
    
    @FXML
    private void initialize() {
        // Inicializações, se necessário
        if (cargoComboBox.getItems().isEmpty()) {
            cargoComboBox.getItems().addAll("Administrador", "Atendente", "Veterinário", "Tosador");
        }
        cargoComboBox.getSelectionModel().selectFirst();
    }
    
    /**
     * Configura o controlador para editar um usuário existente
     * @param usuario O objeto usuário a ser editado
     */
    public void setUsuarioParaEdicao(Object usuario) {
        this.usuarioEmEdicao = usuario;
        this.modoEdicao = true;
        
        // Preencher os campos com os dados do usuário
        try {
            Class<?> usuarioClass = usuario.getClass();
            
            String nome = (String) usuarioClass.getMethod("getNome").invoke(usuario);
            String cpf = (String) usuarioClass.getMethod("getCpf").invoke(usuario);
            String email = (String) usuarioClass.getMethod("getEmail").invoke(usuario);
            String cargo = (String) usuarioClass.getMethod("getCargo").invoke(usuario);
            String login = (String) usuarioClass.getMethod("getLogin").invoke(usuario);
            
            nomeField.setText(nome);
            cpfField.setText(cpf);
            emailField.setText(email);
            
            // Selecionar o cargo no ComboBox
            if (!cargoComboBox.getItems().contains(cargo)) {
                cargoComboBox.getItems().add(cargo);
            }
            cargoComboBox.setValue(cargo);
            
            loginField.setText(login);
            // Não preenchemos a senha por segurança
            senhaField.setPromptText("Digite a nova senha (deixe em branco para manter a atual)");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar dados do usuário");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleSalvar(ActionEvent event) {
        if (validarFormulario()) {
            String nome = nomeField.getText();
            String cpf = cpfField.getText();
            String email = emailField.getText();
            String cargo = cargoComboBox.getValue();
            String login = loginField.getText();
            String senha = senhaField.getText();
            
            try {
                // Obter a instância do repositório
                Object repositorio = Class.forName("Repositorio").getMethod("getInstance").invoke(null);
                
                if (modoEdicao && usuarioEmEdicao != null) {
                    // Atualizar o usuário existente
                    Class<?> usuarioClass = usuarioEmEdicao.getClass();
                    
                    // Atualizar os campos do usuário
                    usuarioClass.getMethod("setNome", String.class).invoke(usuarioEmEdicao, nome);
                    usuarioClass.getMethod("setCpf", String.class).invoke(usuarioEmEdicao, cpf);
                    usuarioClass.getMethod("setEmail", String.class).invoke(usuarioEmEdicao, email);
                    usuarioClass.getMethod("setCargo", String.class).invoke(usuarioEmEdicao, cargo);
                    usuarioClass.getMethod("setLogin", String.class).invoke(usuarioEmEdicao, login);
                    
                    // Atualizar a senha apenas se foi fornecida uma nova
                    if (!senha.isEmpty()) {
                        usuarioClass.getMethod("setSenha", String.class).invoke(usuarioEmEdicao, senha);
                    }
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Usuário atualizado com sucesso!");
                    alert.showAndWait();
                } else {
                    // Criar novo usuário usando reflexão
                    Object novoUsuario = Class.forName("Usuario")
                        .getConstructor(String.class, String.class, String.class, String.class, String.class, String.class)
                        .newInstance(nome, cpf, email, cargo, login, senha);
                    
                    // Adicionar ao repositório
                    Method adicionarUsuarioMethod = repositorio.getClass().getMethod("adicionarUsuario", Class.forName("Usuario"));
                    adicionarUsuarioMethod.invoke(repositorio, novoUsuario);
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Usuário cadastrado com sucesso!");
                    alert.showAndWait();
                }
                
                // Fechar a janela
                fecharJanela(event);
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao processar usuário");
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
            erros.append("Nome é obrigatório\n");
        }
        
        if (cpfField.getText().isEmpty()) {
            erros.append("CPF é obrigatório\n");
        }
        
        if (emailField.getText().isEmpty()) {
            erros.append("Email é obrigatório\n");
        }
        
        if (cargoComboBox.getValue() == null) {
            erros.append("Cargo é obrigatório\n");
        }
        
        if (loginField.getText().isEmpty()) {
            erros.append("Login é obrigatório\n");
        }
        
        if (!modoEdicao && senhaField.getText().isEmpty()) {
            erros.append("Senha é obrigatória\n");
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