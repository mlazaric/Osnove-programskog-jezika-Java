package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.exec.ObjectMultistack;
import hr.fer.zemris.java.custom.scripting.exec.ValueWrapper;

/**
 * A demonstration program for the {@link ObjectMultistack}.
 *
 * @author Marko Lazarić
 */
public class ObjectMultistackDemo {

    public static void main(String[] args) {
        ObjectMultistack multistack = new ObjectMultistack();

        ValueWrapper year = new ValueWrapper(Integer.valueOf(2000));
        multistack.push("year", year);

        ValueWrapper price = new ValueWrapper(200.51);
        multistack.push("price", price);

        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        System.out.println("Current value for price: "
                + multistack.peek("price").getValue());
        multistack.push("year", new ValueWrapper(Integer.valueOf(1900)));
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        multistack.peek("year").setValue(
                ((Integer)multistack.peek("year").getValue()).intValue() + 50
        );
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());

        multistack.pop("year");
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());

        multistack.peek("year").add("5");
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        multistack.peek("year").add(5);
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
        multistack.peek("year").add(5.0);
        System.out.println("Current value for year: "
                + multistack.peek("year").getValue());
    }

}