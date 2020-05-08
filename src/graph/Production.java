package graph;

import java.util.Objects;

public class Production {
    private String title, year, id;
    private boolean visited;

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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited){
        this.visited = visited;
    }

    @Override
    public String toString() {
        return title + " " + year + " " + id;
    }
}
