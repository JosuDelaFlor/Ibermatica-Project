package demo.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * NO TOCAR
 */

public class Material {
    private String name;
    private double price;

    public Material(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public static List<Material> materialInitialize() {
        List<Material> materialList = new ArrayList<Material>();
        materialList.addAll(Arrays.asList(new Material("Calculadora", 11.99), new Material("Estuche", 5.55),
            new Material("Mochila", 50.00), new Material("Portatil", 649.99),
            new Material("USB(128Gb)", 10.00), new Material("Cuaderno", 3.99),
            new Material("Pack-Boligrafos", 5.00), new Material("Tipex", 2.49)));
        return materialList;
    }

    @Override
    public String toString() {
        return name+ ", Precio: ["+price+"€]";
    }
}
