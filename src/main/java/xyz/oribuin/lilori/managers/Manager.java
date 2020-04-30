package xyz.oribuin.lilori.managers;

import xyz.oribuin.lilori.LilOri;

public abstract class Manager {

    protected final LilOri bot;

    public Manager(LilOri bot) {
        this.bot = bot;
    }

    public abstract void enable();
}
