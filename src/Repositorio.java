public class Repositorio {
    private static Repositorio instancia;

        public static Repositorio getInstancia() {
        if (instancia == null) {
            instancia = new Repositorio();
        }
        return instancia;
    }
}
