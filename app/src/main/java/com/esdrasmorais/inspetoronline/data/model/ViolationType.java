package com.esdrasmorais.inspetoronline.data.model;

public enum ViolationType {
    NON_JUSTIFIED_ABSENCE("Ausência não Justificada", 1),
    JUSTIFIED_ABSENCE("Ausência Justificada", 2),
    LEAVE_JOB("Abandonar Posto de Trabalho", 4),
    AGGRESSION("Agressão", 31),
    CHANGE_LINE_ITINERARY("Alterar Itinerário da Linha", 11),
    NOTE_INCORRECTLY_ENTERING_VALIDATOR_DATA(
        "Anotar/Digitar Incorretamente os Dados do Dalidador", 55
    ),
    SHOW_SYMPTOMS_OF_INTOXICATION("Apresentar Sintomas de Embriaguez", 35),
    GET_INTRODUCED_TO_IRREGULAR_UNIFORM("Apresentar-se com Uniforme Irregular", 59),
    DISHONEST_ATTITUDE("Atitude Desonesta", 36),
    DELAY_FAIRS_DELIVERY("Atrasar Entrega das Ferias", 52),
    NEXT_SEMAPHORE_CLOSED("Avançar semaforo fechado", 24),
    ARRIVE_LATE_FOR_SERVICE("Chegar Atrasado ao Serviço", 03),
    BEHAVIOR_INCONVENIENT("Comportamento/Conduta Inconveniente", 44),
    STOP_REPORTING_ACCIDENT_OCCURED("Deixar de Comunicar Acidente Ocorrido", 104),
    STOP_REPORTING("Deixar de Fazer Anotaçoes no Relatorio", 56),
    DISOBEYING_THE_SUPERVISOR("Desobedecer o Superior", 32),
    DISOBEYING_SCHEDULE("Desobedecer Tabela de Horario", 51),
    DISRESPECT_EMPLOYEE("Desrespeitar Funcionario(s)", 33),
    DISRESPECT_PASSENGER_PEDESTRIAN("Desrespeitar Passageiro(s)/Pedestre(s)", 34),
    DRIVING_SPEAKING_TO_CELL_PHONE_HEADSET("Dirigir Falando ao Celular/Fone de Ouvido", 106),
    DRIVING_THE_VEHICLE_WITHOUT_AUTHORIZATION("Dirigir o Veiculo sem Autorização", 37),
    PERFORMING_OVERDRIVE("Efetuar Ultrapassagem Perigosa", 18),
    EMBARKATION_DISEMBARKATION_IRREGULAR_POINT("Embarque/Desembarque Irregular - Ponto", 13),
    IRREGULAR_LOADING_UNLOADING_DOOR("Embarque/Desembarque Irregular - Porta", 107),
    IRREGULAR_FAIRS_DELIVERY("Entrega Irregular de Ferias", 53),
    EXCEED_MAX_SPEED_ALLOWED("Exceder Velocidade Maxima Permitida", 20),
    MAKING_USE_OF_OBJECTS_INADEQUATE_TO_FUNCTION("Fazer Uso de Objetos Inadequados a Função", 64),
    LACK_OF_CLEANLINESS("Falta de Asseio", 60),
    BREAK_OR_HARD_START("Freadas ou Arrancadas Bruscas", 19),
    SMOKING_INSIDE_THE_COLLECTIVE("Fumar no Interior do Coletivo", 63),
    OVERTAKING_IN_PLACE_OF_POINT("Ultrapassagem em Local de Ponto", 39),
    TRAFFIC_WITHOUT_IDENTIFICATION_PLATE("Trafegar sem Placa de Identificação", 105),
    PLACE_OF_WORK_POORLY_PRESERVED("Local de Trabalho Mal Conservado", 62),
    DO_NOT_ASSIST_THE_DRIVE("Não Auxiliar o Motorista", 43),
    DO_NOT_CHARGE("Não Cobrar Passagem", 40),
    NO_CHECK_SCHOOL_SPECIAL_IDENTIFICATION("Não Conferir Identificação Escolar/Especial", 58);

    private String stringValue;
    private Integer intValue;

    private ViolationType(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }
}
