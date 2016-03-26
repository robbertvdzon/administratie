package com.vdzon.weegschaal.crud;

import com.vdzon.weegschaal.model.Customer;
import com.vdzon.weegschaal.model.Gewicht;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class UserCrud {

    @Inject
    GewichtenCrud gewichtenCrud;

    public Customer getCustomer(String uuid){
        return new Customer("robbert",13, gewichtenCrud.getAllGewichten());
    }
}
