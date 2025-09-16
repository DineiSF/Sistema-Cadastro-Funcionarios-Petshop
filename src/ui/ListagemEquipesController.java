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

public class ListagemEquipesController {

    @FXML
    private TableView<Object> equipesTable;
    
    @FXML
    private TableColumn<Object, String> nomeColumn;
    
    @FXML
    private TableColumn<Object, String> descricaoColumn;
    
    @FXML
    private TableColumn<Object, String> projetoColumn;
    
    @FXML
    private Button editarButton;
    
    @FXML
    private Button excluirButton;
    
    private ObservableList<Object> equipesData = FXCollections.observableArrayList();
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
            carregarEquipes();
            
            // Configurar seleção
            equipesTable.getSelectionModel().selectedItemProperty().addListener(
                    (_, _, newValue) -> {
                        boolean temSelecao = newValue != null;
                        editarButton.setDisable(!temSelecao);
                        excluirButton.setDisable(!temSelecao);
                    });
            
            // Iniciar botões desabilitados
            editarButton.setDisable(true);
            excluirButton.setDisable(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao inicializar a listagem de equipes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void configurarColunas() {
        // Configurar fábrica de células para cada coluna
        configurarColuna(nomeColumn, "getNome");
        configurarColuna(descricaoColumn, "getDescricao");
        
        // Para a coluna de projeto, precisamos obter o nome do projeto
        projetoColumn.setCellValueFactory(cellData -> {
            try {
                Object equipe = cellData.getValue();
                Method getProjeto = equipe.getClass().getMethod("getProjeto");
                Object projeto = getProjeto.invoke(equipe);
                
                if (projeto != null) {
                    Method getNome = projeto.getClass().getMethod("getNome");
                    String nomeProjeto = (String) getNome.invoke(projeto);
                    return new javafx.beans.property.SimpleStringProperty(nomeProjeto);
                }
                return new javafx.beans.property.SimpleStringProperty("Sem projeto");
            } catch (Exception e) {
                e.printStackTrace();
                return new javafx.beans.property.SimpleStringProperty("Erro");
            }
        });
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
    
    private void carregarEquipes() {
        try {
            // Limpar a lista atual
            equipesData.clear();
            
            // Obter todas as equipes do repositório
            Method getEquipesMethod = repositorio.getClass().getMethod("getEquipes");
            List<?> equipes = (List<?>) getEquipesMethod.invoke(repositorio);
            
            // Adicionar à lista observável
            equipesData.addAll(equipes);
            
            // Atualizar a tabela
            equipesTable.setItems(equipesData);
            
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao carregar equipes: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleNovo() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroEquipeView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Equipe");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
            
            // Recarregar a lista de equipes após o cadastro
            carregarEquipes();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao abrir tela de cadastro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleEditar() {
        Object equipe = equipesTable.getSelectionModel().getSelectedItem();
        if (equipe == null) {
            exibirAlerta("Aviso", "Selecione uma equipe para editar.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroEquipeView.fxml"));
            Parent root = loader.load();
            
            // Obter o controlador
            CadastroEquipeController controller = loader.getController();
            
            // Configurar a equipe para edição
            controller.setEquipeParaEdicao(equipe);
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Editar Equipe");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
            
            // Recarregar a lista de equipes após a edição
            carregarEquipes();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao abrir tela de edição: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleExcluir() {
        Object equipe = equipesTable.getSelectionModel().getSelectedItem();
        if (equipe == null) {
            exibirAlerta("Aviso", "Selecione uma equipe para excluir.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Confirmação de exclusão
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Excluir Equipe");
            alert.setContentText("Tem certeza que deseja excluir esta equipe?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remover equipe do repositório
                Method removerEquipeMethod = repositorio.getClass().getMethod("removerEquipe", Object.class);
                boolean removido = (boolean) removerEquipeMethod.invoke(repositorio, equipe);
                
                if (removido) {
                    // Recarregar a lista
                    carregarEquipes();
                    
                    exibirAlerta("Sucesso", "Equipe excluída com sucesso!", Alert.AlertType.INFORMATION);
                } else {
                    exibirAlerta("Erro", "Não foi possível excluir a equipe.", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao excluir equipe: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleFechar() {
        // Fechar a janela
        Stage stage = (Stage) equipesTable.getScene().getWindow();
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