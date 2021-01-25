package com.example.tabletopics;

import android.content.Context;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    @Mock
    Context context;

    @Test
    public void testWithIncorrectMail(){

        LoginActivity l = new LoginActivity(context);
        String invalidEmail = "invalid@email.com";
        String validPassword = "123456";

        l.fAuth.signInWithEmailAndPassword(invalidEmail, validPassword);
        boolean success = l.success;

        Assert.assertEquals(false, success);
    }
}