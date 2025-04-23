public class InterfaceMethods {
    public static void main(String[] args) {
        class Dragon implements EggLayer, FireBreather {
            public String identifyMyself() {
                return EggLayer.super.identifyMyself() + " " + FireBreather.super.identifyMyself();
            }
        }
        
        Dragon dragon = new Dragon();
        System.out.println(dragon.identifyMyself());
    }
}

interface Animal {
    default public String identifyMyself() {
        return "I am an animal.";
    }
}

interface EggLayer extends Animal {
    default public String identifyMyself() {
        return "I am able to lay eggs.";
    }
}

interface FireBreather extends Animal { 
    default public String identifyMyself() {
        return "I am able to breathe fire.";
    }
}


