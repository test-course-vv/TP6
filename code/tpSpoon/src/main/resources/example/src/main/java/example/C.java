package example;

public class C  {
    private int i;

    public C(int i) {
        System.out.println("C.C(int i)");
        this.i = i;
    }

    public int mth1() {
       System.out.println("C.mth1()");
        return 100/i;
    }
}
