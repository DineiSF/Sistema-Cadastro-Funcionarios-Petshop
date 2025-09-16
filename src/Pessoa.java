public class Pessoa {
    private String nome;
    private String cpf;

    // Construtor parametrizado
    public Pessoa(String nome, String cpf) {
        this.nome = nome;
        this.cpf = cpf;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    // Método sobrescrito
    @Override
    public String toString() {
        return "Nome: " + nome + ", CPF: " + cpf;
    }
}
