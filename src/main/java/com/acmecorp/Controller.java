package com.acmecorp;

import java.util.Collection;

public abstract class Controller {
    protected abstract Boolean checkCurrentUserIsGuest();
    protected abstract Boolean checkCurrentUserIsAdmin();
    protected abstract Boolean checkCurrentUserIsStudent();
    protected abstract Boolean checkCurrentUserIsEntertainmentProvider();
    public abstract <T> int selectFromMenu(Collection<T> menu, String item);
}
