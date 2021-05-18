package fornecedores.backend.domain;

public enum TipoUsuarioEnum {

    FORNECEDOR("Este é um fornecedor comum."),
    FORNECEDOR_PREMIUM("Este é um fornecedor premium."),
    USUARIO("Este é um usuário comum."),
    USUARIO_PREMIUM("Este é um usuário premium"),
    ADMINISTRADOR("Este é um administrador"),
    INVALID("Favor informar um usuário válido (FORNECEDOR, USUARIO)");

    final String descricao;

    TipoUsuarioEnum(String descricao) { this.descricao = descricao;}

}