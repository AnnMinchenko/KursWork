package model;

public class Record {
    private int id;
    private Person person;
    private String name;
    private int payment;
    private String date;
    private int monthOfPayment;
    private int purchaseDeadline;
    private int cost;
    private String organization;

    public Record(int id, Person person, String name, int payment, String date, int monthOfPayment, int cost, String organization) {
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

    public int getMonthOfPayment() {
        return monthOfPayment;
    }

    public int getPurchaseDeadline() {
        return purchaseDeadline;
    }

    public int getCost() {
        return cost;
    }

    public String getOrganization() {
        return organization;
    }

    private void calculatePurchaseDeadline() {
        var temp = monthOfPayment + 5;
        if (temp > 12) {
            purchaseDeadline = temp - 12;
        }
        else {
            purchaseDeadline = temp;
        }
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
