package org.example;
/**
Разработать классы Ночной клуб и Фейсконтролер(вышибала), где второй будет зависимостью первого.
Реализовать методы впустить, выпустить человека с учетом, количества посетителей/вместимости, стиля одежды и
дня недели (например, в пятницу пускать всех до заполнения клуба), а выпускать только после проверки отсутствия задолженности.
Написать тесты на эти классы, не забыв про заглушки, использовать все инструменты, чтобы сделать их человекочитаемыми.
По желанию, попробовать TDD.
 */

public class NightClub {
    private int places = 1;
    private final FaceController faceController;

    public NightClub(FaceController faceController) {
        this.faceController = faceController;
    }


    public boolean comeIn(Style style, boolean isFriday) {
        if (places < 1) {
            return false;
        }

        if (isFriday) {
           --places;
           return true;
        }

        boolean solution = faceController.decide(style);
        if (solution) {
            --places;
            return true;
        }
        return false;
    }

    public boolean comeOut(boolean haveDebt) {
        boolean solution = faceController.decide(haveDebt);
        if (solution) {
            ++places;
            return true;
        }
        return false;
    }
}
