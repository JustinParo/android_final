package com.tingtingliu.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MainGenerator {
    public static void main(String[] args)  throws Exception {

        //place where db folder will be created inside the project folder
        Schema schema = new Schema(1,"com.codekrypt.greendao.db");

        //Entity i.e. Class to be stored in the database // ie table LOG
        Entity word_entity= schema.addEntity("LOG");

        word_entity.addIdProperty(); //It is the primary key for uniquely identifying a row

        word_entity.addStringProperty("text").notNull();  //Not null is SQL constrain

        //  ./app/src/main/java/   ----   com/codekrypt/greendao/db is the full path
        new DaoGenerator().generateAll(schema, "./app/src/main/java");

    }
}