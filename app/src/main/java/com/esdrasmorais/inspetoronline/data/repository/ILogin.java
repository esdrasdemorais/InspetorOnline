package com.esdrasmorais.inspetoronline.data.repository;

import com.esdrasmorais.inspetoronline.data.Result;
import com.esdrasmorais.inspetoronline.data.model.Login;

public interface ILogin {
    Result<Login> login(String username, String password);
}
