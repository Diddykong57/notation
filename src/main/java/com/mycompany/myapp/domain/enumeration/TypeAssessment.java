package com.mycompany.myapp.domain.enumeration;

/**
 * The TypeAssessment enumeration.
 */
public enum TypeAssessment {
    CE("comprehensionEcrite"),
    CO("comprehensionOrale"),
    EE("expressionEcrite"),
    EO("expressionOrale");

    private final String value;

    TypeAssessment(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
