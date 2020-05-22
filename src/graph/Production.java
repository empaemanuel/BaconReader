package graph;

import java.util.Objects;

public class Production {
    private String title, year, id;

    public Production(String title, String year, String id) {
        this.title = title;
        this.year = year;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Production that = (Production) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(year, that.year) &&
                Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, year, id);
    }

    @Override
    public String toString() {
        return title + " " + year + " " + id;
    }


}
