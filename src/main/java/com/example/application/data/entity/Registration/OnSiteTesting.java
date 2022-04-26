package com.example.application.data.entity.Registration;

import com.example.application.data.entity.TestingSite.TestingSite;

public class OnSiteTesting extends CovidTest{
    private TestingSite testingSite;
    public OnSiteTesting(TestingSite testingSite){
        this.testingSite = testingSite;
    }

    @Override
    public String getId() {
        return testingSite.getId();
    }
}
