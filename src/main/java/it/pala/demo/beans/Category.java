package it.pala.demo.beans;

public class Category {

    private final String id;
    private final String name;
    private final String father;

    public Category(String id, String name, String father){
        this.name = name;
        this.father = father;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getFather() {
        return father;
    }

    public String getId() { return id; }

    public String getSpaces(){
        StringBuilder result = new StringBuilder();
        for(int i=0; i<id.length(); i++){
            result.append("  ");
        }
        return result.toString();
    }

    @Override
    public String toString(){
        return id+" "+name;
    }
}
