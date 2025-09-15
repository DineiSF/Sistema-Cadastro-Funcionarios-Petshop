package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class ListagemUsuariosController {

    @FXML
    private TableView<Object> usuariosTable;
    
    @FXML
    private TableColumn<Object, String> nomeColumn;
    
    @FXML
    private TableColumn<Object, String> cpfColumn;
    
    @FXML
    private TableColumn<Object, String> emailColumn;
    
    @FXML
    private TableColumn<Object, String> cargoColumn;
    
    @FXML
    private TableColumn<Object, String> loginColumn;
    
    @FXML
    private Button editarButton;
    
    @FXML
    private Button excluirButton;
    
    private ObservableList<Object> usuariosData = FXCollections.observableArrayList();
    private Object repositorio;
    
    @FXML
    private void initialize() {
        try {
            // Obter o repositório
            Class<?> repositorioClass = Class.forName("Repositorio");
            Method getInstanceMethod = repositorioClass.getMethod("getInstance");
            repositorio = getInstanceMethod.invoke(null);
            
            // Configurar as colunas
            configurarColunas();
            
            // Carregar dados
            carregarUsuarios();
            
            // Configurar seleção
            usuariosTable.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        boolean temSelecao = newValue != null;
                        editarButton.setDisable(!temSelecao);
                        excluirButton.setDisable(!temSelecao);
                    });
            
            // Iniciar botões desabilitados
            editarButton.setDisable(true);
            excluirButton.setDisable(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao inicializar a listagem de usuários: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void configurarColunas() {
        // Configurar fábrica de células para cada coluna
        configurarColuna(nomeColumn, "getNome");
        configurarColuna(cpfColumn, "getCpf");
        configurarColuna(emailColumn, "getEmail");
        configurarColuna(cargoColumn, "getCargo");
        configurarColuna(loginColumn, "getLogin");
    }
    
    private void configurarColuna(TableColumn<Object, String> coluna, String metodoGetter) {
        coluna.setCellValueFactory(cellData -> {
            try {
                Object item = cellData.getValue();
                Method method = item.getClass().getMethod(metodoGetter);
                Object valor = method.invoke(item);
                return new javafx.beans.property.SimpleStringProperty(valor != null ? valor.toString() : "");
            } catch (Exception e) {
                e.printStackTrace();
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });
    }
    
    private void carregarUsuarios() {
        try {
            // Limpar a lista atual
            usuariosData.clear();
            
            // Obter todos os usuários do repositório
            Method getUsuariosMethod = repositorio.getClass().getMethod("getUsuarios");
            List<?> usuarios = (List<?>) getUsuariosMethod.invoke(repositorio);
            
            // Adicionar à lista observável
            usuariosData.addAll(usuarios);
            
            // Atualizar a tabela
            usuariosTable.setItems(usuariosData);
            
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao carregar usuários: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleNovo() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroUsuarioView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Usuário");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
            
            // Recarregar a lista de usuários após o cadastro
            carregarUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao abrir tela de cadastro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleEditar() {
        Object usuario = usuariosTable.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            exibirAlerta("Aviso", "Selecione um usuário para editar.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroUsuarioView.fxml"));
            Parent root = loader.load();
            
            // Obter o controlador
            CadastroUsuarioController controller = loader.getController();
            
            // Configurar o usuário para edição
            controller.setUsuarioParaEdicao(usuario);
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Editar Usuário");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
            
            // Recarregar a lista de usuários após a edição
            carregarUsuarios();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao abrir tela de edição: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleExcluir() {
        Object usuario = usuariosTable.getSelectionModel().getSelectedItem();
        if (usuario == null) {
            exibirAlerta("Aviso", "Selecione um usuário para excluir.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Confirmação de exclusão
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Excluir Usuário");
            alert.setContentText("Tem certeza que deseja excluir este usuário?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remover usuário do repositório
                Method removerUsuarioMethod = repositorio.getClass().getMethod("removerUsuario", Object.class);
                boolean removido = (boolean) removerUsuarioMethod.invoke(repositorio, usuario);
                
                if (removido) {
                    // Recarregar a lista
                    carregarUsuarios();
                    
                    exibirAlerta("Sucesso", "Usuário excluído com sucesso!", Alert.AlertType.INFORMATION);
                } else {
                    exibirAlerta("Erro", "Não foi possível excluir o usuário.", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao excluir usuário: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleFechar() {
        // Fechar a janela
        Stage stage = (Stage) usuariosTable.getScene().getWindow();
        stage.close();
    }
    
    private void exibirAlerta(String titulo, String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}