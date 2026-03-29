package br.com.alura.screenmatch.Model;

public enum Categoria {
    ACAO ("Action", "Ação"),
    AVENTURA ("Adventure", "Aventura"),
    COMEDIA ("Comedy", "Comédia"),
    DRAMA ("Drama", "Drama"),
    CRIME ("Crime", "Crime"),
    SUSPENSE ("Thriller","Suspense"),
    TERROR ("Horror", "Terror");


    private String categoriaOmdb;
    private String categoriaPortugues;
    Categoria(String categoriaOmdb, String categoriaPortugues) {
        this.categoriaPortugues = categoriaPortugues;
        this.categoriaOmdb = categoriaOmdb;
    }

    public static Categoria fromString(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria não encontrada: " + text);
    }
    public static Categoria fromPortugues(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Categoria não encontrada: " + text);
    }
}
