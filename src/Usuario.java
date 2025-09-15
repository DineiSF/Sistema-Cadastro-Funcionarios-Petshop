public class Usuario extends Pessoa {
    private String email;
    private String cargo;
    private String login;
    private String senha;

    // Construtor parametrizado
    public Usuario(String nome, String cpf, String email, String cargo, String login, String senha) {
        super(nome, cpf);
        this.email = email;
        this.cargo = cargo;
        this.login = login;
        this.senha = senha;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    // Método sobrescrito
    @Override
    public String toString() {
        return super.toString() + ", Email: " + email + ", Cargo: " + cargo;
    }
}