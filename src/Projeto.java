import java.time.LocalDate;

public class Projeto {
    private String nome;
    private String descricao;
    private LocalDate dataInicio;
    private LocalDate dataTerminoPrevista;
    private String status;

    // Construtor parametrizado
    public Projeto(String nome, String descricao, LocalDate dataInicio, LocalDate dataTerminoPrevista, String status) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataTerminoPrevista = dataTerminoPrevista;
        this.status = status;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataTerminoPrevista() {
        return dataTerminoPrevista;
    }

    public void setDataTerminoPrevista(LocalDate dataTerminoPrevista) {
        this.dataTerminoPrevista = dataTerminoPrevista;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    // Métodos de conveniência para compatibilidade
    public LocalDate getDataFim() {
        return getDataTerminoPrevista();
    }
    
    public void setDataFim(LocalDate dataFim) {
        setDataTerminoPrevista(dataFim);
    }

    @Override
    public String toString() {
        return "Projeto: " + nome + ", Descrição: " + descricao + ", Data Início: " + dataInicio +
               ", Data Término Prevista: " + dataTerminoPrevista + ", Status: " + status;
    }
}