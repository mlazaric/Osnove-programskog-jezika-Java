package hr.fer.zemris.java.hw07.demo2;

public class PrimesDemo2 {

    public static void main(String[] args) {
        PrimesCollection primesCollection = new PrimesCollection(2);

        for(Integer prime : primesCollection) {
            for(Integer prime2 : primesCollection) {
                System.out.println("Got prime pair: "+prime+", "+prime2);
            }
        }
    }

}
