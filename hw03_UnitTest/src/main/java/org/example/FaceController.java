package org.example;

import static org.example.Style.Classic;

public class FaceController {
    public boolean decide(Style style) {
        switch (style) {
            case CASUAL, Smart_Casual, Classic -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean decide(boolean haveDebt) {
        return !haveDebt;
    }
}
