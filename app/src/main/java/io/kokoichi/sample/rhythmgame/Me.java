package io.kokoichi.sample.rhythmgame;

public class Me {

    int exp;

    Me() {
        exp = getExp();
    }

    int getExp() {
        // TODO:
        //  get from database
        //  Maybe use another dbHelper (DatabaseHelperMe?)
        return 3;
    }
}
