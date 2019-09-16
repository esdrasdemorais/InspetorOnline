package com.esdrasmorais.inspetoronline.data.model;

public enum EmployeeType {
    DRIVER("Motorista", 1),
    TICKET_COLLECTOR("Cobrador", 2),
    SUPERVISOR("Fiscal", 3),
    COORDINATOR("Coordenador", 4),
    VALET_PARKING("Manobrista", 5),
    PLANTONIST("Plantonista", 6),
    LIFEGUARD("Socorrista", 7);

    private String stringValue;
    private Integer intValue;

    private EmployeeType(String stringValue, Integer intValue) {
        this.stringValue = stringValue;
        this.intValue = intValue;
    }

    @Override
    public String toString() {
        return this.stringValue;
    }

    public static EmployeeType of(String employeeType) {
        switch (employeeType) {
            case "Motorista": return DRIVER;
            case "Cobrador": return TICKET_COLLECTOR;
            case "Fiscal": return SUPERVISOR;
            case "Coordenador": return COORDINATOR;
            case "Manobrista": return VALET_PARKING;
            case "Plantonista": return PLANTONIST;
            case "Socorrista": return LIFEGUARD;
            default: return null;
        }
    }
}
