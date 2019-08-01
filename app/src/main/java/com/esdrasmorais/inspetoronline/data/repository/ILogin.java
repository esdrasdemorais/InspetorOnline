package com.esdrasmorais.inspetoronline.data.repository;

import com.esdrasmorais.inspetoronline.data.Result;
import com.esdrasmorais.inspetoronline.data.model.Login;

public interface ILogin {
    public void login(String username, String password);
    public Result<Login> getByUsername(String username);
}
