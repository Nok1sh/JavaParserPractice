package org.example.model;

public class TranslatorPosition {

    public String translatePosition(String position){
        switch (position){
            case "GOALKEEPER" -> {
                return "вратарь";
            }
            case "DEFENDER" -> {
                return "защитник";
            }
            case "MIDFIELD" -> {
                return "полузащитник";
            }
            case "FORWARD" -> {
                return "нападающий";
            }default -> {
                return null;
            }
        }
    }
}
