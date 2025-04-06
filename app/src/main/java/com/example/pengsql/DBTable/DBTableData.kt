package com.example.pengsql.DBTable

import kotlin.random.Random

val names = listOf(
    "Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Henry", "Ivy", "Jack",
    "Kate", "Liam", "Mia", "Noah", "Olivia", "Peter", "Quinn", "Ryan", "Sophia", "Thomas",
    "Uma", "Victor", "Willow", "Xavier", "Yara", "Zane", "Aria", "Ben", "Chloe", "Dylan",
    "Ella", "Finn", "Gia", "Hugo", "Isla", "Jasper", "Luna", "Mason", "Nora", "Owen",
    "Penelope", "Quentin", "Riley", "Scarlett", "Theodore", "Violet", "Wyatt", "Stella", "Aaron", "Hazel"
)
val nicknames = listOf(
    "Ace", "Buddy", "Champ", "Dude", "Eagle", "Flash", "Giggles", "Hawk", "Ice", "Joker",
    "King", "Lucky", "Magic", "Ninja", "Owl", "Pilot", "Queen", "Racer", "Sparky", "Tiger",
    "Unicorn", "Viper", "Wolf", "Xena", "Yeti", "Zeus", "Angel", "Bear", "Cupcake", "Dragon",
    "Echo", "Fox", "Ghost", "Hunter", "Iron", "Jewel", "Knight", "Lion", "Mystic", "Nova",
    "Onyx", "Phoenix", "Ranger", "Shadow", "Titan", "Vortex", "Wizard", "Comet", "Blaze", "Frost"
)
val genders = listOf("Male", "Female", "Other")
val foods = listOf(
    "Pizza", "Burger", "Sushi", "Pasta", "Tacos", "Salad", "Steak", "Ramen", "Curry", "Noodles",
    "Sandwich", "Soup", "Chicken", "Fish", "Rice", "Bread", "Eggs", "Fruit", "Vegetables", "Cheese",
    "Chocolate", "Ice Cream", "Cake", "Cookies", "Donuts", "Pancakes", "Waffles", "Yogurt", "Popcorn", "Chips",
    "Pretzels", "Almonds", "Cashews", "Peanuts", "Strawberries", "Bananas", "Apples", "Oranges", "Grapes", "Mangoes",
    "Pineapple", "Kiwi", "Blueberries", "Raspberries", "Blackberries", "Watermelon", "Melon", "Lemon", "Broccoli", "Spinach"
)
val sports = listOf(
    "Soccer", "Basketball", "Tennis", "Swimming", "Baseball", "Golf", "Running", "Cycling", "Volleyball", "Football",
    "Hockey", "Badminton", "Table Tennis", "Skiing", "Snowboarding", "Boxing", "Martial Arts", "Yoga", "Pilates", "Climbing",
    "Surfing", "Skateboarding", "Gymnastics", "Cricket", "Rugby", "Athletics", "Diving", "Rowing", "Sailing", "Archery",
    "Bowling", "Fishing", "Hiking", "Camping", "Chess", "Darts", "Frisbee", "Horse Riding", "Judo", "Karate",
    "Lacrosse", "Paintball", "Polo", "Rock Climbing", "Squash", "Weightlifting", "Wrestling", "Kite Surfing", "Triathlon", "Kayaking"
)
val colors = listOf(
    "Red", "Blue", "Green", "Yellow", "Orange", "Purple", "Pink", "Brown", "Black", "White",
    "Gray", "Cyan", "Magenta", "Lime", "Teal", "Olive", "Maroon", "Navy", "Silver", "Gold",
    "Violet", "Indigo", "Beige", "Coral", "Crimson", "Lavender", "Peach", "Salmon", "Tan", "Turquoise",
    "Amber", "Bronze", "Charcoal", "Copper", "Emerald", "Fuchsia", "Ivory", "Jade", "Khaki", "Lilac",
    "Mustard", "Ochre", "Plum", "Ruby", "Sapphire", "Scarlet", "Sienna", "Topaz", "Umber", "Mint"
)
val coffees = listOf(
    "Latte", "Cappuccino", "Espresso", "Americano", "Mocha", "Macchiato", "Flat White", "Iced Coffee", "Cold Brew", "Irish Coffee",
    "Turkish Coffee", "Vienna Coffee", "Cortado", "Ristretto", "Lungo", "Doppio", "Affogato", "Red Eye", "Black Eye", "Dead Eye",
    "Cafe au Lait", "Frappuccino", "Pour Over", "French Press", "Aeropress", "Chemex", "Siphon", "Drip Coffee", "Decaf", "Hazelnut Coffee",
    "Vanilla Coffee", "Caramel Coffee", "Pumpkin Spice Latte", "Peppermint Mocha", "Gingerbread Latte", "Eggnog Latte", "Matcha Latte", "Chai Latte", "Hot Chocolate", "Tea",
    "Lemonade", "Smoothie", "Juice", "Soda", "Water", "Milk", "Soy Milk", "Almond Milk", "Oat Milk", "Herbal Tea"
)

val DBTableIdSample = List(50) { Random.nextInt(1, 1000).toString() } // please fill listOf with 50 random Int

val dbTableSample1: List<String> = listOf(
    Random.nextInt(1, 1000).toString(), // id
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample2: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample3: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample4: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample5: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample6: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample7: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample8: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample9: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample10: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample11: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample12: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample13: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample14: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample15: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample16: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample17: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample18: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample19: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample20: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample21: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample22: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample23: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample24: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)
val dbTableSample25: List<String> = listOf(
    Random.nextInt(1, 1000).toString(),
    names.random(),
    genders.random(),
    nicknames.random(),
    foods.random(),
    sports.random(),
    colors.random(),
    coffees.random()
)







val DBTableHeaderName = listOf("id","name","gender","nickname","food","sports","color","coffee")

// DB 에서 전달하는 데이터 형식은 아래와 같이 가정하였습니다.
// 될 수 있다면 백엔드에서 이렇게 데이터가 올 수 있도록 처리해주시면 감사하겠습니다.

// 형식 : List<List<String>>
// [[id, name, gander, nickname],[1,kim,male,hoho],[3,joe,male,rurl],[7,sam,male,coco]]

val dbTableSample: MutableList<List<String>> = mutableListOf<List<String>>().apply {
    add(DBTableHeaderName)
    for (i in 0 until 50) {
        add(
            listOf(
                Random.nextInt(1, 1000).toString(),
                names.random(),
                genders.random(),
                nicknames.random(),
                foods.random(),
                sports.random(),
                colors.random(),
                coffees.random()
            )
        )
    }
}

