import { useTranslation } from "react-i18next";

function qualifiedLngFor(lng) {
    switch (lng) {
        case "uk":
            return "uk-UA";
        case "en":
            return "en-US";
        default:
            return lng;
    }
}

export function localizeBloodType(bloodType, lng) {
    if (qualifiedLngFor(lng) === "en-US") {
        return bloodType;
    } else {
        switch (bloodType) {
            case "O":
                return "I";
            case "A":
                return "II";
            case "B":
                return "III";
            case "AB":
                return "IV";
            default:
                return bloodType;
        }
    }
}

export function localizeRhesus(rhesus) {
    switch (rhesus) {
        case true: 
            return '+';
        case false:
            return '-';
        default:
            return rhesus;
    };
}

export function localizeBloodTypeAndRhesus(bloodType, rhesus, lng) {
    return localizeBloodType(bloodType, lng) + '(' + localizeRhesus(rhesus) + ')';
}

export function localizeEventStatus(status, t) {
    switch (status) {
        case "PLANNED":
            return t("event_status_planned");
        case "ONGOING":
            return t("event_status_ongoing");
        case "CANCELED":
            return t("event_status_canceled");
        case "FINISHED":
            return t("event_status_finished");
        default:
            return status;
    }
}

export function localizeBloodStatus(status, t) {
    switch (status) {
        case "AVAILABLE":
            return t("blood_status_available");
        case "RESERVED":
            return t("blood_status_reserved");
        case "USED":
            return t("blood_status_used");
        case "TRASHED":
            return t("blood_status_trashed");
        default:
            return status;
    }
}

export function localizeDateTime(dateTimeStr, lng) {
    return new Date(dateTimeStr).toLocaleString(qualifiedLngFor(lng));
}