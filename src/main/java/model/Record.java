package model;

public class Record {
    private int id;
    private Person person;
    private String name;
    private int payment;
    private String date;
    private String monthOfPayment;
    private String purchaseDeadline;
    private int cost;
    private String organization;

    public Record(int id, Person person, String name, int payment, String date, String monthOfPayment, int cost, String organization) {
        this.id = id;
        this.person = person;
        this.name = name;
        this.payment = payment;
        this.date = date;
        this.monthOfPayment = monthOfPayment;
        calculatePurchaseDeadline();
        this.cost = cost;
        this.organization = organization;
    }

    public int getId() {
        return id;
    }

    public Person getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }

    public int getPayment() {
        return payment;
    }

    public String getDate() {
        return date;
    }

    public String getMonthOfPayment() {
        return monthOfPayment;
    }

    public String getPurchaseDeadline() {
        return purchaseDeadline;
    }

    public int getCost() {
        return cost;
    }

    public String getOrganization() {
        return organization;
    }

    private void calculatePurchaseDeadline() {
        String[] string = monthOfPayment.split("-");
        int[] dates = new int[2];
        dates[0] = Integer.parseInt(string[0]);
        dates[1] = Integer.parseInt(string[1]);

        var tempMonth = dates[1] + 5;
        var tempYear = dates[0];

        if (tempMonth > 12) {
            tempMonth = tempMonth - 12;
            tempYear =+ 1;
        }

        purchaseDeadline = tempYear + "-" + tempMonth;
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "id=" + id +
                ", person=" + person +
                ", name='" + name + '\'' +
                ", payment=" + payment +
                ", date='" + date + '\'' +
                ", monthOfPyment=" + monthOfPayment +
                ", purchaseDeadline=" + purchaseDeadline +
                ", cost=" + cost +
                ", organization='" + organization + '\'' +
                '}';
    }
}
