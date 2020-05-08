package _old;

import java.util.*;

public class Production {
    private String title, id, year;

    public Production(String title, String year, String id) {
        this.title = title;
        this.year = year;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Production)) return false;
        Production that = (Production) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(id, that.id) &&
                Objects.equals(year, that.year);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, id, year);
    }


}
