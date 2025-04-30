package model;

public enum CampoBusca {
    TITULO("titulo"),
    AUTORES("autores"),
    ISBN("isbn");

    private final String nomeCampo;

    CampoBusca(String nomeCampo) {
        this.nomeCampo = nomeCampo;
    }

    public String getNomeCampo() {
        return nomeCampo;
    }

    @Override
    public String toString() {
        return nomeCampo;
    }
}
