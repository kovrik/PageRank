package pagerank;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class represents Web Page.
 * Each page may have link to another page.
 **/
public class Page {

    /* auto increment page ids */
    private static final AtomicInteger IDS = new AtomicInteger(0);
    private final int id = IDS.incrementAndGet();

    /* list of links from `this` page to another pages */
    private List<Page> links = new ArrayList<Page>();

    /* PageRank of this page */
    private double rank;

    /**
     * link this page to another page
     **/
    public void link(Page p) {
        if (!links.contains(p)) {
            links.add(p);
        }
    }

    public int getId() {
        return id;
    }

    public List<Page> getLinks() {
        return links;
    }

    public void setLinks(List<Page> links) {
        this.links = links;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

    /**
     * Check if this page is linked to page with id=`id`
     */
    public boolean isLinkedTo(int id) {
        for (Page p : this.getLinks()) {
            if (p.getId() == id) {
                return true;
            }
        }
        return false;
    }

    private final String linksToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean first = true;
        for (Page p : links) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(p.getId());
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Page{" + "id=" + id +
                ", links="     + linksToString() +
                ", rank="      + rank + '}';
    }
}
