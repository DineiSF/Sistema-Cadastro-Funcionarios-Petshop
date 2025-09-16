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

public class ListagemProjetosController {

    @FXML
    private TableView<Object> projetosTable;
    
    @FXML
    private TableColumn<Object, String> nomeColumn;
    
    @FXML
    private TableColumn<Object, String> descricaoColumn;
    
    @FXML
    private TableColumn<Object, String> dataInicioColumn;
    
    @FXML
    private TableColumn<Object, String> dataFimColumn;
    
    @FXML
    private TableColumn<Object, String> statusColumn;
    
    @FXML
    private Button editarButton;
    
    @FXML
    private Button excluirButton;
    
    private ObservableList<Object> projetosData = FXCollections.observableArrayList();
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
            carregarProjetos();
            
            // Configurar seleção
            projetosTable.getSelectionModel().selectedItemProperty().addListener(
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
            exibirAlerta("Erro", "Erro ao inicializar a listagem de projetos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void configurarColunas() {
        // Configurar fábrica de células para cada coluna
        configurarColuna(nomeColumn, "getNome");
        configurarColuna(descricaoColumn, "getDescricao");
        configurarColuna(dataInicioColumn, "getDataInicio");
        configurarColuna(dataFimColumn, "getDataTerminoPrevista");
        configurarColuna(statusColumn, "getStatus");
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
    
    private void carregarProjetos() {
        try {
            // Limpar a lista atual
            projetosData.clear();
            
            // Obter todos os projetos do repositório
            Method getProjetosMethod = repositorio.getClass().getMethod("getProjetos");
            List<?> projetos = (List<?>) getProjetosMethod.invoke(repositorio);
            
            // Adicionar à lista observável
            projetosData.addAll(projetos);
            
            // Atualizar a tabela
            projetosTable.setItems(projetosData);
            
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao carregar projetos: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleNovo() {
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroProjetoView.fxml"));
            Parent root = loader.load();
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Projeto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
            
            // Recarregar a lista de projetos após o cadastro
            carregarProjetos();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao abrir tela de cadastro: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleEditar() {
        Object projeto = projetosTable.getSelectionModel().getSelectedItem();
        if (projeto == null) {
            exibirAlerta("Aviso", "Selecione um projeto para editar.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Carregar o FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/CadastroProjetoView.fxml"));
            Parent root = loader.load();
            
            // Obter o controlador
            CadastroProjetoController controller = loader.getController();
            
            // Configurar o projeto para edição
            controller.setProjetoParaEdicao(projeto);
            
            // Criar uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Editar Projeto");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            
            // Mostrar a janela e aguardar até ser fechada
            stage.showAndWait();
            
            // Recarregar a lista de projetos após a edição
            carregarProjetos();
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao abrir tela de edição: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleExcluir() {
        Object projeto = projetosTable.getSelectionModel().getSelectedItem();
        if (projeto == null) {
            exibirAlerta("Aviso", "Selecione um projeto para excluir.", Alert.AlertType.WARNING);
            return;
        }
        
        try {
            // Confirmação de exclusão
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmação");
            alert.setHeaderText("Excluir Projeto");
            alert.setContentText("Tem certeza que deseja excluir este projeto?");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Remover projeto do repositório
                Method removerProjetoMethod = repositorio.getClass().getMethod("removerProjeto", Object.class);
                boolean removido = (boolean) removerProjetoMethod.invoke(repositorio, projeto);
                
                if (removido) {
                    // Recarregar a lista
                    carregarProjetos();
                    
                    exibirAlerta("Sucesso", "Projeto excluído com sucesso!", Alert.AlertType.INFORMATION);
                } else {
                    exibirAlerta("Erro", "Não foi possível excluir o projeto.", Alert.AlertType.ERROR);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exibirAlerta("Erro", "Erro ao excluir projeto: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    @FXML
    private void handleFechar() {
        // Fechar a janela
        Stage stage = (Stage) projetosTable.getScene().getWindow();
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