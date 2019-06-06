package br.com.octabus;

/**
 * Created by marcatti on 28/09/16.
 */

public class Constantes
{
    public static Boolean debug = true;

    public static String versaoApp = "1.5.1";
    public static String url = "http://104.154.148.185/";
    public static String urlVerificacaoVersao = url + "Octabus/AplicativoConfiguracao/recuperarVersaoAtualAplicativo";
    public static String urlReportarErro = url + "Octabus/Aplicativo/reportarErro";

    public static String keyApp = "6c8349cc7260ae62e3b1396831a8398f"; //md5 idEmpresa 45 - octabus

    public static String emailReport = "caiomarcatti12@gmail.com";
    public static String emailSubjectReport = "App crash report: Octabus";

    public static String urlMonitoramento = url + "Octabus/UsuarioLoginMonitoramento/registrarLocal";
    public static String urlLogin = url + "Octabus/UsuarioLogin/autenticarLogin";
    public static String urlDeslogar = url + "Octabus/UsuarioLogin/deslogar";
    public static String urlProgramacaoFuncionario = url + "Octabus/ProgramacaoHorario/listarProgramacaoHorarioPorFuncionario";
    public static String urlCodigoRecuperacaoSenha = url + "Octabus/UsuarioRecuperacaoSenha/gerarCodigo";
    public static String urlVerificaCodigoRecuperacao = url + "Octabus/UsuarioRecuperacaoSenha/validarCodigo";
    public static String urlAlteracaoSenha = url + "Octabus/Usuario/atualizarSenhaUsuarioAplicativo";

    public static String urlRecuperarDadosLinhaPorPrefixo = url + "Octabus/Programacao/recuperarDadosLinhaPorPrefixo";

    public static String urlPesquisaInfracao = url + "Octabus/Infracao/listarInfracao";
    public static String urlTipoInfracao = url + "Octabus/InfracaoTipo/listarInfracaoTipo";
    public static String urlGerarInfracao = url + "Octabus/InfracaoComunicacao/cadastrarInfracaoComunicacao";
    public static String urlRecuperaInfracao = url + "Octabus/Infracao/listarInfracao";
    public static String urlUploadFotoInfracao = url + "Octabus/InfracaoComunicacaoFoto/realizarUploadComunicacaoFoto";

    public static String urlFuncionarioCargo = url + "RH/FuncionarioCargo/listarComboFuncionarioCargo";
    public static String urlPesquisarFuncionario = url + "RH/Funcionario/listarComboFuncionario";

    public static String urlRecuperarDadosChecklist = url + "Octabus/Checklist/listarChecklistAgrupado";
    public static String urlGerarCheckList = url + "Octabus/ChecklistComunicacao/cadastrarChecklistComunicacao";

    public static String urlRecuperarDadosInformacaoPonto = url + "Octabus/InformacaoPonto/listarInformacaoPontoAgrupado";
    public static String urlGerarInformacaoPonto = url + "Octabus/InformacaoPontoComunicacao/cadastrarInformacaoPontoComunicacao";
    public static String urlUploadFotoidInformacaoPonto = url + "Octabus/InformacaoPontoFoto/realizarUploadComunicacaoFoto";


}
