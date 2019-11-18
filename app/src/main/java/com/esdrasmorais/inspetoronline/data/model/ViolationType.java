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
    NEXT_SEMAPHORE_CLOSED("Avançar Semáforo Fechado", 24),
    ARRIVE_LATE_FOR_SERVICE("Chegar Atrasado ao Serviço", 03),
    BEHAVIOR_INCONVENIENT("Comportamento/Conduta Inconveniente", 44),
    STOP_REPORTING_ACCIDENT_OCCURED("Deixar de Comunicar Acidente Ocorrido", 104),
    STOP_REPORTING("Deixar de Fazer Anotaçoes no Relatorio", 56),
    DISOBEYING_THE_SUPERVISOR("Desobedecer o Superior", 32),
    DISOBEYING_SCHEDULE("Desobedecer Tabela de Horário", 51),
    DISRESPECT_EMPLOYEE("Desrespeitar Funcionário(s)", 33),
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

    public static ViolationType of(String violationType) {
        switch (violationType) {
            case "Ausência não Justificada": case "1": return NON_JUSTIFIED_ABSENCE;
            case "Ausência Justificada": case "2" : return JUSTIFIED_ABSENCE;
            case "Abandonar Posto de Trabalho": case "4": return LEAVE_JOB;
            case "Agressão": case "31": return AGGRESSION;
            case "Alterar Itinerário da Linha": case "11": return CHANGE_LINE_ITINERARY;
            case "Anotar/Digitar Incorretamente os Dados do Dalidador": case "55":
                return NOTE_INCORRECTLY_ENTERING_VALIDATOR_DATA;
            case "Apresentar Sintomas de Embriaguez": case "35":
                return SHOW_SYMPTOMS_OF_INTOXICATION;
            case "Apresentar-se com Uniforme Irregular": case "59":
                return GET_INTRODUCED_TO_IRREGULAR_UNIFORM;
            case "Atitude Desonesta": case "36": return DISHONEST_ATTITUDE;
            case "Atrasar Entrega das Ferias": case "52": return DELAY_FAIRS_DELIVERY;
            case "Avançar Semáforo Fechado": case "24": return NEXT_SEMAPHORE_CLOSED;
            case "Chegar Atrasado ao Serviço": case "03": return ARRIVE_LATE_FOR_SERVICE;
            case "Comportamento/Conduta Inconveniente": case "44":
                return BEHAVIOR_INCONVENIENT;
            case "Deixar de Comunicar Acidente Ocorrido": case "104":
                return STOP_REPORTING_ACCIDENT_OCCURED;
            case "Deixar de Fazer Anotaçoes no Relatorio": case "56":
                return STOP_REPORTING;
            case "Desobedecer o Superior": case "32": return DISOBEYING_THE_SUPERVISOR;
            case "Desobedecer Tabela de Horário": case "51": return DISOBEYING_SCHEDULE;
            case "Desrespeitar Funcionário(s)": case "33": return DISRESPECT_EMPLOYEE;
            case "Desrespeitar Passageiro(s)/Pedestre(s)": case "34":
                return DISRESPECT_PASSENGER_PEDESTRIAN;
            case "Dirigir Falando ao Celular/Fone de Ouvido": case "106":
                return DRIVING_SPEAKING_TO_CELL_PHONE_HEADSET;
            case "Dirigir o Veiculo sem Autorização": case "37":
                return DRIVING_THE_VEHICLE_WITHOUT_AUTHORIZATION;
            case "Efetuar Ultrapassagem Perigosa": case "18":
                return PERFORMING_OVERDRIVE;
            case "Embarque/Desembarque Irregular - Ponto": case "13":
                return EMBARKATION_DISEMBARKATION_IRREGULAR_POINT;
            case "Embarque/Desembarque Irregular - Porta": case "107":
                return IRREGULAR_LOADING_UNLOADING_DOOR;
            case "Entrega Irregular de Ferias": case "53":
                return IRREGULAR_FAIRS_DELIVERY;
            case "Exceder Velocidade Maxima Permitida": case "20":
                return EXCEED_MAX_SPEED_ALLOWED;
            case "Fazer Uso de Objetos Inadequados a Função": case "64":
                return MAKING_USE_OF_OBJECTS_INADEQUATE_TO_FUNCTION;
            case "Falta de Asseio": case "60": return LACK_OF_CLEANLINESS;
            case "Freadas ou Arrancadas Bruscas": case "19":
                return BREAK_OR_HARD_START;
            case "Fumar no Interior do Coletivo": case "63":
                return SMOKING_INSIDE_THE_COLLECTIVE;
            case "Ultrapassagem em Local de Ponto": case "39":
                return OVERTAKING_IN_PLACE_OF_POINT;
            case "Trafegar sem Placa de Identificação": case "105":
                return TRAFFIC_WITHOUT_IDENTIFICATION_PLATE;
            case "Local de Trabalho Mal Conservado": case "62":
                return PLACE_OF_WORK_POORLY_PRESERVED;
            case "Não Auxiliar o Motorista": case "43":
                return DO_NOT_ASSIST_THE_DRIVE;
            case "Não Cobrar Passagem": case "40": return DO_NOT_CHARGE;
            case "Não Conferir Identificação Escolar/Especial": case "58":
                return NO_CHECK_SCHOOL_SPECIAL_IDENTIFICATION;
            default: return null;
        }
    }
}