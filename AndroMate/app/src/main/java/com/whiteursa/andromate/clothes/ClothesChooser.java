package com.whiteursa.andromate.clothes;

public class ClothesChooser {
    private String[] manHot = {"Regular T-shirt", "Bulky shorts",
            "Parka with a hood", "Visor or kerchief", "Sunglasses",
            "Soft flat-soled shoes", "Cardigan", "Shirt"};

    private String[] womanHot = {"Regular T-shirt", "Breton",
            "Linen shirt", "Small simple dress", "Tunic dresses", "White jeans",
            "Chinos", "Shorts", "Jacket and Denim Jacket", "Flats",
            "Sandals", "Espadrilles", "Flat Sneakers", "Classic Heeled Sandals",
            "Stoles", "Shawls", "Bandanas", "Cardigan"};

    private String[] manCool = {"Classic coat", "Jacket", "Pants", "Shirt",
            "Bulky Sweater", "Loafers", "Army Boots", "Chelsea", "Beanie Hat",
            "Jeans", "Longsleeve", "Jumper", "Brockie chinos", "Scarf"};

    private String[] womanCool = {"Classic coat", "Jacket", "Pants",
            "Medium length skirt", "Thick fabric dress", "Shirt", "Beanie hat", "Jeans",
            "Classic high-waisted pencil skirt", "Trench coat", "Cashmere sweater",
            "Dress of the A-shaped silhouette", "Scarf"};

    private String[] manCold = {"Parka", "Coat", "Shirt", "Longsleeve", "Pullover",
            "Jeans", "Classic trousers", "Boots", "Winter jacket", "Men's sheepskin coat",
            "Desert boots", "Jumper with a round or V-neck", "Cardigan"};

    private String[] womanCold = {"Blouse with long sleeves", "Trousers", "Suit ensemble",
            "Turtleneck", "Lined coat", "Fur coat", "Parka", "Down jacket", "Basic woolen dress",
            "Chunky knit sweater", "Leather midi skirt", "Woolen trousers", "Boots or knee-high boots with fur",
            "Hat", "Scarf", "Fur collar", "Gloves"};

    public String[] getClothes(int temperature, boolean forMan) {
        if (temperature > 24) {
            return forMan ? manHot.clone() : womanHot.clone();
        } else if (temperature > 10) {
            return forMan ? manCool.clone() : womanCool.clone();
        } else {
            return forMan ? manCold.clone() : womanCold.clone();
        }
    }
}
