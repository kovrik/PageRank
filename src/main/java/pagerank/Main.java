package pagerank;

public class Main {

    public static void main(String[] args) {
        Page p1 = new Page();
        Page p2 = new Page();
        Page p3 = new Page();
        Page p4 = new Page();

        p1.link(p3);
        p1.link(p4);
        p1.link(p2);

        p2.link(p3);
        p2.link(p4);

        p3.link(p1);

        p4.link(p1);
        p4.link(p3);

        Web web = new Web();
        web.add(p1, p2, p3, p4);


        System.out.println(web);

        StringBuilder sb = new StringBuilder();
        double[][] rank = web.pageRank();
        for (double[] arr : rank) {
            for (double d : arr) {
                sb.append(d);
                sb.append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
        System.out.println(web);
    }
}
