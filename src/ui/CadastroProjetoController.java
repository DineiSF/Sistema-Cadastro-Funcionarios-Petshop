package ui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import java.time.LocalDate;
import java.lang.reflect.Method;

public class CadastroProjetoController {
    
    @FXML
    private TextField nomeField;
    
    @FXML
    private TextArea descricaoField;
    
    @FXML
    private DatePicker dataInicioField;
    
    @FXML
    private DatePicker dataTerminoField;
    
    @FXML
    private ComboBox<String> statusComboBox;
    
    private Object projetoEmEdicao = null;
    private boolean modoEdicao = false;
    
    @FXML
    private void initialize() {
        // Inicializações, se necessário
        if (statusComboBox.getItems().isEmpty()) {
            statusComboBox.getItems().addAll("Em andamento", "Concluído", "Cancelado", "Planejado");
        }
        statusComboBox.getSelectionModel().selectFirst();
        
        dataInicioField.setValue(LocalDate.now());
        dataTerminoField.setValue(LocalDate.now().plusMonths(1));
    }
    
    /**
     * Configura o controlador para editar um projeto existente
     * @param projeto O objeto projeto a ser editado
     */
    public void setProjetoParaEdicao(Object projeto) {
        this.projetoEmEdicao = projeto;
        this.modoEdicao = true;
        
        // Preencher os campos com os dados do projeto
        try {
            Class<?> projetoClass = projeto.getClass();
            
            String nome = (String) projetoClass.getMethod("getNome").invoke(projeto);
            String descricao = (String) projetoClass.getMethod("getDescricao").invoke(projeto);
            LocalDate dataInicio = (LocalDate) projetoClass.getMethod("getDataInicio").invoke(projeto);
            LocalDate dataTermino = (LocalDate) projetoClass.getMethod("getDataTerminoPrevista").invoke(projeto);
            String status = (String) projetoClass.getMethod("getStatus").invoke(projeto);
            
            nomeField.setText(nome);
            descricaoField.setText(descricao);
            dataInicioField.setValue(dataInicio);
            dataTerminoField.setValue(dataTermino);
            
            // Selecionar o status no ComboBox
            if (!statusComboBox.getItems().contains(status)) {
                statusComboBox.getItems().add(status);
            }
            statusComboBox.setValue(status);
            
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Erro ao carregar dados do projeto");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    @FXML
    private void handleSalvar(ActionEvent event) {
        if (validarFormulario()) {
            String nome = nomeField.getText();
            String descricao = descricaoField.getText();
            LocalDate dataInicio = dataInicioField.getValue();
            LocalDate dataTermino = dataTerminoField.getValue();
            String status = statusComboBox.getValue();
            
            try {
                // Obter a instância do repositório
                Object repositorio = Class.forName("Repositorio").getMethod("getInstance").invoke(null);
                
                if (modoEdicao && projetoEmEdicao != null) {
                    // Atualizar o projeto existente
                    Class<?> projetoClass = projetoEmEdicao.getClass();
                    
                    // Atualizar os campos do projeto
                    projetoClass.getMethod("setNome", String.class).invoke(projetoEmEdicao, nome);
                    projetoClass.getMethod("setDescricao", String.class).invoke(projetoEmEdicao, descricao);
                    projetoClass.getMethod("setDataInicio", LocalDate.class).invoke(projetoEmEdicao, dataInicio);
                    projetoClass.getMethod("setDataTerminoPrevista", LocalDate.class).invoke(projetoEmEdicao, dataTermino);
                    projetoClass.getMethod("setStatus", String.class).invoke(projetoEmEdicao, status);
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Projeto atualizado com sucesso!");
                    alert.showAndWait();
                } else {
                    // Criar novo projeto usando reflexão
                    Object novoProjeto = Class.forName("Projeto")
                        .getConstructor(String.class, String.class, LocalDate.class, LocalDate.class, String.class)
                        .newInstance(nome, descricao, dataInicio, dataTermino, status);
                    
                    // Adicionar ao repositório
                    Method adicionarProjetoMethod = repositorio.getClass().getMethod("adicionarProjeto", Class.forName("Projeto"));
                    adicionarProjetoMethod.invoke(repositorio, novoProjeto);
                    
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Sucesso");
                    alert.setHeaderText(null);
                    alert.setContentText("Projeto cadastrado com sucesso!");
                    alert.showAndWait();
                }
                
                // Fechar a janela
                fecharJanela(event);
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Erro ao processar projeto");
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
            erros.append("Nome do projeto é obrigatório\n");
        }
        
        if (descricaoField.getText().isEmpty()) {
            erros.append("Descrição é obrigatória\n");
        }
        
        if (dataInicioField.getValue() == null) {
            erros.append("Data de início é obrigatória\n");
        }
        
        if (dataTerminoField.getValue() == null) {
            erros.append("Data de término é obrigatória\n");
        } else if (dataInicioField.getValue() != null && 
                  dataTerminoField.getValue().isBefore(dataInicioField.getValue())) {
            erros.append("Data de término não pode ser anterior à data de início\n");
        }
        
        if (statusComboBox.getValue() == null) {
            erros.append("Status é obrigatório\n");
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