public class Passenger {
    private int passportNum;     // מס. דרכון
    private String name;         // שם הנוסע
    private Boolean permPass;    // נוסע מתמיד, אחרי 20 אלף ק"מ
    private int km;              // ק"מ טיסות שצבר

    public Passenger(int passportNum, String name, int km) {
        this.passportNum = passportNum;
        this.name = name;
        this.km = km;
        this.permPass = (km >= 20000);
    }

    public int getPassportNum() { return passportNum; }
    public void setPassportNum(int passportNum) { this.passportNum = passportNum; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getPermPass() { return permPass; }
    public void setPermPass(Boolean permPass) { this.permPass = permPass; }

    public int getKm() { return km; }

    // If you prefer "only get/set", remove addKm() and let students update via setKm().
    public void setKm(int km) {
        this.km = km;
        this.permPass = (this.km >= 20000);
    }

    public void addKm(int add) {
        setKm(this.km + add);
    }
}
