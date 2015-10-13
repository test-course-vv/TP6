package example;


public class B  {


    public void mth1(int i) {
        System.out.println("B.mth1(int i)");

        C c = new C(i);
        int result = c.mth1();

        System.out.println("result = " + result);
    }

    public void mth2() {
        System.out.println("B.mth2()");
    }

}
