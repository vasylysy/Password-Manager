package tests;

import org.junit.Assert;
import org.junit.Test;

import static src.Main.*;

public class MainTest {

    @Test
    public void encryptNotEqualsStatement() {
        String before = "1234566";
        String after = "pJBA7ULnid/XpYbv5lYbGw==";

        Assert.assertNotEquals(after, encrypt(before));
    }

    @Test
    public void encryptEqualsStatement() {
        String before = "password";
        String after = "S2lS/B5u4MXu/w2drD5/Kg==";

        Assert.assertEquals(after, encrypt(before));
    }

    @Test
    public void decryptEqualsStatement(){
        String before = "938NKnf3bH+y3wUnCed+kw==";
        String after = "P4ssW0r_d%";

        Assert.assertEquals(after, decrypt(before));
    }

    @Test
    public void decryptNotEqualsStatement(){
        String before = "S2lS/B5u4MXu/w2drD5/Kg==";
        String after =  "password2";

        Assert.assertNotEquals(after, decrypt(before));
    }

    @Test
    public void decryptedEncryptEqualsStatement(){
        String exp = "For the Alliance!";

        Assert.assertEquals(exp, decrypt(encrypt(exp)));
    }

    @Test
    public void getIdNotEqualsStatement(){
        int id = 15;
        String login = "test";

        Assert.assertNotEquals(id, getId(login));
    }

    @Test
    public void getIdEqualsStatement(){
        int id = 2;
        String login = "asdfsadfasdf";

        Assert.assertEquals(id, getId(login));
    }

    @Test
    public void dbLogowanieEqualsStatement(){
        String login = "test";
        String password = "pJBA7ULnid/XpYbv5lYbGw==";
        boolean exp = true;

        Assert.assertEquals(exp, dbLogowanie(login, password));
    }

    @Test
    public void dbLogowanieNotEqualsStatement(){
        String login = "ivanIvanov";
        String password = "+TCov9IJ8y3eraoQcc1wZA==";
        boolean exp = false;

        Assert.assertNotEquals(exp, dbLogowanie(login, password));
    }

    @Test
    public void dbPrzypomnienieEqualsStatement(){
        String answer = "h+Hma0S3grM1NOzEy99ldw==";
        String exp = "1111111";

        Assert.assertEquals(exp, dbPrzypomnienie(answer));
    }
}